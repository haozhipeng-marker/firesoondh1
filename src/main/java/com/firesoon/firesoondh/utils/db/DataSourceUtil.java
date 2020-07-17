package com.firesoon.firesoondh.utils.db;

import com.alibaba.druid.pool.DruidDataSource;
import com.firesoon.firesoondh.model.dtotype.db.DateSourceDTO;
import com.firesoon.firesoondh.model.enumtype.DataSourceEnum;
import com.firesoon.firesoondh.utils.CacheUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @description: 数据库工具
 * @author: Yz
 * @date: 2020/6/11
 */
public class DataSourceUtil {


    /**
     * @param dateSourceDTO 数据源
     * @return jdbcTemplate
     */
    public static JdbcTemplate getJdbcTemplate(DateSourceDTO dateSourceDTO) {
        //获取连接类型
        DataSourceEnum dataSourceEnum = dateSourceDTO.getDataSourceEnum();

        //判断缓存中是否存在
        String key = StringUtils.join(new String[]{dataSourceEnum.getClassName(), dateSourceDTO.getUrl(),
                        dateSourceDTO.getUserName(), dateSourceDTO.getPassWord()},
                "|");
        JdbcTemplate jdbcTemplate = (JdbcTemplate) CacheUtil.get(key);
        if (ObjectUtils.allNotNull(jdbcTemplate)) {
            return jdbcTemplate;
        }
        //生成jdbcTemplate
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(dataSourceEnum.getClassName());
        druidDataSource.setUrl(dateSourceDTO.getUrl());
        druidDataSource.setPassword(dateSourceDTO.getPassWord());
        druidDataSource.setUsername(dateSourceDTO.getUserName());
        druidDataSource.setInitialSize(1);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(5);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setValidationQuery(dataSourceEnum.getConnectionTestQuery());
        jdbcTemplate = new JdbcTemplate(druidDataSource);
        CacheUtil.put(key, jdbcTemplate);
        return jdbcTemplate;
    }
}
