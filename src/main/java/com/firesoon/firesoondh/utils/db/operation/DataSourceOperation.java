package com.firesoon.firesoondh.utils.db.operation;

import com.firesoon.firesoondh.model.dtotype.db.DbColumnDTO;
import com.firesoon.firesoondh.model.dtotype.db.DbCommentsDTO;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 * @description: 对数据源的操作
 * @author: Yz
 * @date: 2020/6/11
 */
public interface DataSourceOperation {
    /**
     * 获取所有的表以及注释
     *
     * @param jdbcTemplate
     * @return
     */
    List<? extends DbCommentsDTO> getTables(JdbcTemplate jdbcTemplate);

    /**
     * 获取某张表的所有列信息
     *
     * @param jdbcTemplate
     * @param tableName
     * @return
     */
    List<? extends DbColumnDTO> getColumns(JdbcTemplate jdbcTemplate, String tableName);

    /**
     * 获取某张表的主键
     *
     * @param jdbcTemplate
     * @param tableName
     * @return
     */
    String getPrimaryKey(JdbcTemplate jdbcTemplate, String tableName);

    /**
     * 获取某张表的建表语句
     *
     * @param jdbcTemplate
     * @param tableName
     * @return
     */
    String getCreateTableSql(JdbcTemplate jdbcTemplate, String tableName);
}
