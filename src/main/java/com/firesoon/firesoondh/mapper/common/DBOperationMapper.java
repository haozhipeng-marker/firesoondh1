package com.firesoon.firesoondh.mapper.common;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @description: db的一些公共操作
 * @author: Yz
 * @date: 2020/6/19
 */
@Component
public interface DBOperationMapper {
    /**
     * 创建表
     *
     * @param sql sql
     */
    void createTable(@Param("sql") String sql);

    /**
     * 查询表是否存在
     *
     * @param tableName 表名
     * @param schema    schema
     * @return 是否
     */
    boolean selectTableExist(@Param("tableName") String tableName, @Param("schema") String schema);

    /**
     * 删除表
     *
     * @param tableName 表名
     * @param schema    schema
     */
    void dropTable(@Param("tableName") String tableName, @Param("schema") String schema);

}
