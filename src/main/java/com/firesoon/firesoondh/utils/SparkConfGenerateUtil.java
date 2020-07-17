package com.firesoon.firesoondh.utils;

import com.firesoon.firesoondh.constant.DataAccessConts;
import com.firesoon.firesoondh.constant.DbConts;
import com.firesoon.firesoondh.constant.TemplateConts;
import com.firesoon.firesoondh.mapper.common.DBOperationMapper;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import com.firesoon.firesoondh.model.dtotype.data.access.SparkConfDTO;
import com.firesoon.firesoondh.model.dtotype.db.DateSourceDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDetailDTO;
import com.firesoon.firesoondh.model.enumtype.DataSourceEnum;
import com.firesoon.firesoondh.utils.db.DataSourceUtil;
import com.firesoon.firesoondh.utils.db.operation.DataSourceOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * @description: spark配置生成
 * @author: Yz
 * @date: 2020/6/22
 */
@Component
public class SparkConfGenerateUtil {

    @Autowired
    private DataSourceProperties dataSourceProperties;
    @Autowired
    private DBOperationMapper dbOperationMapper;

    @Autowired
    private ThreadPoolTaskExecutor dhThreadPool;

    public File dataAccess(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        //防止 方法内部set导致后续插入表数据不对问题
        DataAccessConfDO temp = ConvertUtil.convert(dataAccessConf, DataAccessConfDO.class);
        //类型判断
        File file = null;
        if (DbConts.YES.equals(dataAccessConf.getIsFull())) {
            file = dataAccessFull(temp, dataSourceDetailDTO);
        } else if (DbConts.ADD_TYPE_INSERT.equals(dataAccessConf.getAddType())) {
            file = dataAccessAddByInsert(temp, dataSourceDetailDTO);
        } else if (DbConts.ADD_TYPE_MERGE.equals(dataAccessConf.getAddType())) {
            file = dataAccessAddByMerge(temp, dataSourceDetailDTO);
        }
        return file;
    }

    /**
     * 数据同步 全量
     *
     * @param dataAccessConf      同步配置
     * @param dataSourceDetailDTO 源数据源信息
     * @return 配置文件
     */
    private File dataAccessFull(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        //建表
        dhThreadPool.execute(() -> createTable(dataSourceDetailDTO, dataAccessConf.getOriginTable(), dataAccessConf.getTargetTable(), true));

        //获取配置结构
        SparkConfDTO sparkConfDTO = dataAccessCommon(dataAccessConf, dataSourceDetailDTO);
        sparkConfDTO.getUnits().get(1).setDllPreSql(null);
        //建表
        String fileName = TemplateConts.GENERATE_FILE_PATH + dataSourceDetailDTO.getName() + "/" + dataAccessConf.getOriginTable() + "/application.conf";
        FreemarkerUtil.generateFile(TemplateConts.SPARK_CONF_TEMPLATE_NAME,
                ConvertUtil.convert(sparkConfDTO, Map.class),
                fileName);

        return new File(fileName);
    }

    /**
     * 数据同步 增量-merge
     *
     * @param dataAccessConf      同步配置
     * @param dataSourceDetailDTO 源数据源信息
     * @return 配置文件
     */
    private File dataAccessAddByMerge(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        String mergeTemp = "DH_MERGE_" + dataAccessConf.getTargetTable();
        //建表
        dhThreadPool.execute(() -> createTable(dataSourceDetailDTO, dataAccessConf.getOriginTable(), mergeTemp, true));
        dhThreadPool.execute(() -> createTable(dataSourceDetailDTO, dataAccessConf.getOriginTable(), dataAccessConf.getTargetTable(), false));
        String targetTable = dataAccessConf.getTargetTable();
        //merger缓存表
        dataAccessConf.setTargetTable(mergeTemp);
        SparkConfDTO sparkConfDTO = dataAccessCommon(dataAccessConf, dataSourceDetailDTO);


        //添加目标表
        //write
        SparkConfDTO.Unit writeUnit = new SparkConfDTO.Unit();
        writeUnit.setName("write2");
        writeUnit.setActor("com.firesoon.component.write.OracleWriter");
        writeUnit.setDb(sparkConfDTO.getUnits().get(1).getDb());
        writeUnit.setTable(targetTable);
        writeUnit.setMode("merge");
        writeUnit.setTTable(mergeTemp);
        writeUnit.setPriKey(dataAccessConf.getPrimaryKey());
        writeUnit.setView("write2_" + targetTable);
        sparkConfDTO.getUnits().add(writeUnit);
        sparkConfDTO.getBlueprint().add("write2");

        String fileName = TemplateConts.GENERATE_FILE_PATH + dataSourceDetailDTO.getName() + "/" + dataAccessConf.getOriginTable() + "/application.conf";
        FreemarkerUtil.generateFile(TemplateConts.SPARK_CONF_TEMPLATE_NAME,
                ConvertUtil.convert(sparkConfDTO, Map.class),
                fileName);

        return new File(fileName);
    }

    /**
     * 数据同步 增量-insert
     *
     * @param dataAccessConf      同步配置
     * @param dataSourceDetailDTO 源数据源信息
     * @return 配置文件
     */
    private File dataAccessAddByInsert(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        //创建表
        dhThreadPool.execute(() -> createTable(dataSourceDetailDTO, dataAccessConf.getOriginTable(), dataAccessConf.getTargetTable(), false));
        SparkConfDTO sparkConfDTO = dataAccessCommon(dataAccessConf, dataSourceDetailDTO);
        String fileName = TemplateConts.GENERATE_FILE_PATH + dataSourceDetailDTO.getName() + "/" + dataAccessConf.getOriginTable() + "/application.conf";
        FreemarkerUtil.generateFile(TemplateConts.SPARK_CONF_TEMPLATE_NAME,
                ConvertUtil.convert(sparkConfDTO, Map.class),
                fileName);

        return new File(fileName);
    }

    /**
     * 数据同步 通用
     *
     * @param dataAccessConf      同步配置
     * @param dataSourceDetailDTO 源数据源信息
     * @return 配置实体
     */

    private SparkConfDTO dataAccessCommon(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        SparkConfDTO sparkConfDTO = new SparkConfDTO();
        //数据源配置组装
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf(dataSourceDetailDTO.getType().toUpperCase());
        String dataSourceType = dataSourceEnum.name().toLowerCase();
        if (CollectionUtils.isEmpty(sparkConfDTO.getDbs().get(dataSourceType))) {
            sparkConfDTO.getDbs().put(dataSourceType, new ArrayList<>());
        }
        if (CollectionUtils.isEmpty(sparkConfDTO.getDbs().get(DataSourceEnum.ORACLE.name().toLowerCase()))) {
            sparkConfDTO.getDbs().put(DataSourceEnum.ORACLE.name().toLowerCase(), new ArrayList<>());
        }
        //源库
        SparkConfDTO.DbConf orgDb = new SparkConfDTO.DbConf();
        orgDb.setDbName("org_" + dataSourceDetailDTO.getUserName());
        orgDb.setDriver(dataSourceEnum.getClassName());
        orgDb.setUsername(dataSourceDetailDTO.getUserName());
        orgDb.setPassword(dataSourceDetailDTO.getPassword());
        orgDb.setUrl(String.format(dataSourceEnum.getUrlTemplate(), dataSourceDetailDTO.getHost(),
                dataSourceDetailDTO.getPort(), dataSourceDetailDTO.getDatabase()));
        sparkConfDTO.getDbs().get(dataSourceType).add(orgDb);
        //目标库
        SparkConfDTO.DbConf tarDb = new SparkConfDTO.DbConf();
        tarDb.setDbName("tar_" + DataAccessConts.ODS_NAME);
        tarDb.setDriver(dataSourceProperties.getDriverClassName());
        tarDb.setUsername(DataAccessConts.ODS_NAME);
        tarDb.setPassword(DataAccessConts.ODS_PASSWORD);
        tarDb.setUrl(dataSourceProperties.getUrl());
        sparkConfDTO.getDbs().get(DataSourceEnum.ORACLE.name().toLowerCase()).add(tarDb);

        //read
        SparkConfDTO.Unit readUnit = new SparkConfDTO.Unit();
        readUnit.setName("read");
        readUnit.setActor(dataSourceEnum.getDsReadActor());
        readUnit.setDb(orgDb.getDbName());
        readUnit.setTable(dataAccessConf.getOriginTable());
        readUnit.setQuery("select * from "
                + dataAccessConf.getOriginTable()
                + (StringUtils.isNotBlank(dataAccessConf.getFilterSql()) ? " where " + dataAccessConf.getFilterSql() : ""));
        readUnit.setFetchSize(dataAccessConf.getFetchSize());
        readUnit.setView("read_" + dataAccessConf.getOriginTable());
        sparkConfDTO.getUnits().add(readUnit);

        //write
        SparkConfDTO.Unit writeUnit = new SparkConfDTO.Unit();
        writeUnit.setName("write");
        writeUnit.setActor("com.firesoon.component.write.OracleWriter");
        writeUnit.setDb(tarDb.getDbName());
        writeUnit.setTable(dataAccessConf.getTargetTable());
        writeUnit.setView("write_" + dataAccessConf.getTargetTable());
        //insert 添加dllpresql
        if (DbConts.ADD_TYPE_INSERT.equals(dataAccessConf.getAddType())) {
            writeUnit.setDllPreSql("delete from "
                    + dataAccessConf.getTargetTable()
                    + (StringUtils.isNotBlank(dataAccessConf.getPreSql()) ? " where " + dataAccessConf.getPreSql() : ""));
        }
        writeUnit.setMode("truncate");
        sparkConfDTO.getUnits().add(writeUnit);

        //流程设置
        sparkConfDTO.getBlueprint().add("read");
        sparkConfDTO.getBlueprint().add("write");

        return sparkConfDTO;

    }


    /**
     * 创建表
     *
     * @param dataSourceDetailDTO 数据源相关信息
     * @param originTable         原表
     * @param targetTable         需要生成的表名
     * @param deleteExist         存在时是否删除重建
     */
    private void createTable(DataSourceDetailDTO dataSourceDetailDTO, String originTable, String targetTable, boolean deleteExist) {
        //判断表是否存在
        boolean tableExist = dbOperationMapper.selectTableExist(targetTable, DataAccessConts.ODS_NAME);
        if (tableExist && deleteExist) {
            dbOperationMapper.dropTable(targetTable, DataAccessConts.ODS_NAME);
        }
        if (tableExist && !deleteExist) {
            return;
        }

        //获取连接类型
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf(dataSourceDetailDTO.getType().toUpperCase());
        //获取url
        String url = String.format(dataSourceEnum.getUrlTemplate(), dataSourceDetailDTO.getHost(),
                dataSourceDetailDTO.getPort(), dataSourceDetailDTO.getDatabase());
        //获取动态连接
        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTemplate(
                new DateSourceDTO(
                        dataSourceDetailDTO.getPassword(),
                        dataSourceDetailDTO.getUserName(), url, dataSourceEnum));
        DataSourceOperation dataSourceOperation = dataSourceEnum.getDataSourceOperation();
        //获取建表语句
        String createTableSql = dataSourceOperation.getCreateTableSql(jdbcTemplate, originTable);
        //替换表名 以及主键等名称
        createTableSql = createTableSql.replaceFirst(originTable, DataAccessConts.ODS_NAME + "." + targetTable);
        createTableSql = createTableSql.replaceAll("CONSTRAINT[\\s]*[A-Za-z0-9_]+", "");


        dbOperationMapper.createTable(createTableSql);
    }

}
