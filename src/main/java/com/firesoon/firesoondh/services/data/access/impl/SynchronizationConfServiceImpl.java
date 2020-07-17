package com.firesoon.firesoondh.services.data.access.impl;

import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.mapper.data.access.DataAccessAssociateMapper;
import com.firesoon.firesoondh.mapper.data.access.DataAccessConfAssociateMapper;
import com.firesoon.firesoondh.mapper.data.access.DataAccessConfMapper;
import com.firesoon.firesoondh.mapper.data.access.DataAccessMapper;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessAssociateDO;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfAssociateDO;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessDO;
import com.firesoon.firesoondh.model.dtotype.data.access.SynchronizationConfUpdateReqDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.*;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.data.access.SynchronizationConfService;
import com.firesoon.firesoondh.services.dsagent.datasource.AgentDataSourceService;
import com.firesoon.firesoondh.services.dsagent.process.AgentProcessService;
import com.firesoon.firesoondh.services.dsagent.project.AgentProjectService;
import com.firesoon.firesoondh.services.dsagent.upload.AgentUploadService;
import com.firesoon.firesoondh.utils.SparkConfGenerateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/17
 */
@Slf4j
@Service
public class SynchronizationConfServiceImpl implements SynchronizationConfService {


    @Autowired
    private DataAccessMapper dataAccessMapper;
    @Autowired
    private DataAccessConfMapper dataAccessConfMapper;
    @Autowired
    private DataAccessConfAssociateMapper dataAccessConfAssociateMapper;

    @Autowired
    private AgentProcessService agentProcessService;
    @Autowired
    private DataAccessAssociateMapper dataAccessAssociateMapper;
    @Autowired
    private AgentProjectService agentProjectService;
    @Autowired
    private AgentUploadService agentUploadService;
    @Autowired
    private AgentDataSourceService agentDataSourceService;
    @Autowired
    private SparkConfGenerateUtil sparkConfGenerateUtil;

    @Override
    public String synchronizationConfAdd(String dataAccessId, Integer dataSourceId, String tableName) {
        String id = UUID.randomUUID().toString();
        dataAccessConfMapper.insertSelective(DataAccessConfDO.builder()
                .id(id)
                .dataAccessId(dataAccessId)
                .originTable(tableName.toUpperCase())
                .targetTable((tableName + "_" + dataSourceId).toUpperCase())
                .createTime(new Date()).build());

        return id;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronizationConfDelete(String dataAccessConfId) {
        //获取项目名称
        DataAccessConfDO dataAccessConf = dataAccessConfMapper.selectByPrimaryKey(dataAccessConfId);
        DataAccessAssociateDO dataAccessAssociate = dataAccessAssociateMapper.selectByPrimaryKey(dataAccessConf.getDataAccessId());
        ProjectDTO project = agentProjectService.getById(dataAccessAssociate.getProjectId());
        //查询相关关联
        DataAccessConfAssociateDO dataAccessConfAssociate = dataAccessConfAssociateMapper.selectByPrimaryKey(dataAccessConfId);
        if (ObjectUtils.allNotNull(dataAccessConfAssociate)) {
            //删除 工作流
            agentProcessService.deleteProcess(dataAccessConfAssociate.getProcessId(), project.getName());
            //删除 资源
            agentUploadService.deleteResource(dataAccessConfAssociate.getResourcesId());

        }

        //删除数据接入配置
        dataAccessConfMapper.deleteByPrimaryKey(dataAccessConfId);
        //删除关联
        dataAccessConfAssociateMapper.deleteByPrimaryKey(dataAccessConfId);


    }

    @Override
    public DataAccessConfDO dataAccessConfDetail(String dataAccessConfId) {
        return dataAccessConfMapper.selectByPrimaryKey(dataAccessConfId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void synchronizationConfUpdate(SynchronizationConfUpdateReqDTO synchronizationConfUpdateReqDTO) {

        //查询基础信息
        DataAccessConfDO dataAccessConf = dataAccessConfMapper.selectByPrimaryKey(synchronizationConfUpdateReqDTO.getDataAccessConfId());
        dataAccessConf.setAddType(synchronizationConfUpdateReqDTO.getAddType());
        dataAccessConf.setFetchSize(synchronizationConfUpdateReqDTO.getFetchSize());
        dataAccessConf.setFilterSql(synchronizationConfUpdateReqDTO.getFilterSql());
        dataAccessConf.setPreSql(synchronizationConfUpdateReqDTO.getPreSql());
        dataAccessConf.setIsFull(synchronizationConfUpdateReqDTO.getIsFull());
        dataAccessConf.setPrimaryKey(synchronizationConfUpdateReqDTO.getPrimaryKey());
        //获取关联信息
        DataAccessConfAssociateDO dataAccessConfAssociate = dataAccessConfAssociateMapper.selectByPrimaryKey(synchronizationConfUpdateReqDTO.getDataAccessConfId());
        //获取项目名称
        DataAccessAssociateDO dataAccessAssociate = dataAccessAssociateMapper.selectByPrimaryKey(dataAccessConf.getDataAccessId());
        ProjectDTO project = agentProjectService.getById(dataAccessAssociate.getProjectId());
        //获取数据接入信息
        DataAccessDO dataAccessDO = dataAccessMapper.selectByPrimaryKey(dataAccessConf.getDataAccessId());
        //获取数据源信息
        DataSourceDetailDTO dataSourceDetailDTO = agentDataSourceService.getDataSourceDetails(dataAccessDO.getDataSourceId());

        //判断是否有工作流
        if (ObjectUtils.allNotNull(dataAccessConfAssociate)) {
            //判断工作流是否上线
            ProcessDefinitionInfoDTO processDefinitionInfo = agentProcessService.selectProcess(dataAccessConfAssociate.getProcessId(), project.getName());
            if (DsConts.PROCESS_ONLINE.equals(processDefinitionInfo.getReleaseState())) {
                throw new BusinessException(ResErro.ERRO_519);
            }
            //有则删除原始配置文件
            agentUploadService.deleteResource(dataAccessConfAssociate.getResourcesId());
            // 创建新的配置文件进行上传
            List<FileDTO> uploadFile = upload(dataSourceDetailDTO.getName()
                    , dataAccessConf.getOriginTable()
                    , generateConf(dataAccessConf, dataSourceDetailDTO));

            dataAccessConfAssociateMapper.updateByPrimaryKeySelective(DataAccessConfAssociateDO.builder()
                    .dataAccessConfId(dataAccessConf.getId())
                    .resourcesId(uploadFile.get(0).getPid())
                    .build());
        } else {
            // 创建文件创建工作流  关联入库
            dataAccessConfAssociateMapper.insertSelective(run(dataAccessConf
                    , project.getName(), dataSourceDetailDTO));
        }

        //数据更新配置入库
        dataAccessConf.setUpdateTime(new Date());
        dataAccessConfMapper.updateByPrimaryKeySelective(dataAccessConf);


    }


    private DataAccessConfAssociateDO run(DataAccessConfDO dataAccessConf
            , String projectName
            , DataSourceDetailDTO dataSourceDetailDTO) {


        //生成配置文件 并且上传资源文件
        List<FileDTO> uploadFiles = upload(dataSourceDetailDTO.getName(),
                dataAccessConf.getOriginTable(),
                generateConf(dataAccessConf, dataSourceDetailDTO));
        //生成工作流
        Integer processId = createProcess(projectName, dataAccessConf, uploadFiles);
        //返回关联关系
        return DataAccessConfAssociateDO.builder()
                .dataAccessConfId(dataAccessConf.getId())
                .processId(processId)
                .resourcesId(uploadFiles.get(0).getPid()).build();
    }


    /**
     * 创建工作流
     *
     * @param dataAccessConf 配置信息
     * @return 工作流id
     */
    private Integer createProcess(String projectName, DataAccessConfDO dataAccessConf, List<FileDTO> uploadFiles) {
        ProcessDefinitionDTO processDefinitionDTO = new ProcessDefinitionDTO();
        //设置基础信息
        processDefinitionDTO.setName(dataAccessConf.getOriginTable().concat("数据同步"));
        processDefinitionDTO.setDescription(
                String.format("%s源数据表同步到%s表,同步方式为%s",
                        dataAccessConf.getOriginTable(),
                        dataAccessConf.getTargetTable(),
                        dataAccessConf.getIsFull() == 1 ? "全量" : "增量"
                )
        );
        //获取父级目录
        String[] pathList = uploadFiles.get(0).getFullName().split("/");
        List<String> paths = new ArrayList<>(Arrays.asList(pathList));
        paths.remove(pathList.length - 1);
        paths.remove(0);
        //添加shell 脚本
        processDefinitionDTO.getProcessDefinitionJson()
                .getTasks().get(0).getParams()
                .setRawScript(
                        String.format(" java  -cp %s:/opt/FireSoonDS/conf:/opt/FireSoonDS/newFDS/FireSoonDS_2.12-1.0.0.jar:/opt/FireSoonDS/dep_lib/*: com.firesoon.DSApp"
                                , String.join("/",paths)));
        //添加配置文件
        List<ProcessDefinitionDTO.ProcessDefinitionJson.Tasks.Params.ResourceList> resourceList = processDefinitionDTO.getProcessDefinitionJson()
                .getTasks().get(0).getParams().getResourceList();
        uploadFiles.forEach(fileDTO -> resourceList
                .add(new ProcessDefinitionDTO.ProcessDefinitionJson.Tasks.Params.ResourceList(
                        fileDTO.getId(), fileDTO.getFileName(), fileDTO.getFullName()
                ))
        );

        processDefinitionDTO.setProjectName(projectName);
        return agentProcessService.saveProcess(processDefinitionDTO);

    }

    /**
     * 创建文件夹
     *
     * @param datasourceName 数据源名称
     * @param orginTableName 表名称
     * @return 文件文件夹信息
     */
    private FileDTO createDir(String datasourceName, String orginTableName) {

        Integer dbDirId;
        //判断是否有数据源目录 无则创建
        String dataSourceDirName = "/".concat(datasourceName);
        if (agentUploadService.verifyName(dataSourceDirName)) {
            dbDirId = agentUploadService.addDir(null, datasourceName, "/");
        } else {
            FileDTO resource = agentUploadService.selectResource(dataSourceDirName);
            dbDirId = resource.getId();
        }
        Integer tableDirId;
        //判断是是否有同步表目录
        String tableDirName = "/".concat(datasourceName)
                .concat("/")
                .concat(orginTableName);
        if (agentUploadService.verifyName(tableDirName)) {
            tableDirId = agentUploadService.addDir(dbDirId, orginTableName, dataSourceDirName);
        } else {
            return agentUploadService.selectResource(tableDirName);
        }
        return agentUploadService.selectResource(tableDirId);
    }


    /**
     * 上传文件
     *
     * @param datasourceName 数据源名称
     * @param orginTableName 同步表名称
     * @param files          需要上传的文件
     * @return 文件信息
     */
    private List<FileDTO> upload(String datasourceName, String orginTableName, List<File> files) {
        //生成目录
        FileDTO tableDir = createDir(datasourceName, orginTableName);
        //上传配置文件
        files.forEach(file -> agentUploadService.addFile(tableDir.getId(), file.getName(), file, tableDir.getFullName()));
        return agentUploadService.selectDir(tableDir.getId());
    }

    /**
     * 生成配置文件
     *
     * @param dataAccessConf      数据同步配置
     * @param dataSourceDetailDTO 源数据源信息
     * @return 文件列表
     */
    private List<File> generateConf(DataAccessConfDO dataAccessConf, DataSourceDetailDTO dataSourceDetailDTO) {
        return new ArrayList<File>() {{
            add(sparkConfGenerateUtil.dataAccess(dataAccessConf, dataSourceDetailDTO));
        }};
    }
}
