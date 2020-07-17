package com.firesoon.firesoondh.model.enumtype;

import com.firesoon.firesoondh.utils.db.operation.DataSourceOperation;
import com.firesoon.firesoondh.utils.db.operation.OracleDataSourceOperation;
import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * @description: 数据源类型
 * @author: Yz
 * @date: 2020/6/11
 */
@Getter
@AllArgsConstructor
public enum DataSourceEnum {
    /**
     * oracle数据源
     */
    ORACLE("oracle.jdbc.OracleDriver"
            , "jdbc:oracle:thin:@%s:%s:%s"
            , "select 1 from dual"
            , "com.firesoon.component.read.OracleReader"
            , new OracleDataSourceOperation());

    /**
     * 驱动包名
     */
    private final String className;
    /**
     * url模板
     */
    private final String urlTemplate;
    /**
     * 连接测试语句
     */
    private final String connectionTestQuery;
    /**
     * ds read Actor
     */
    private final String dsReadActor;
    /**
     * 数据操作类型
     */
    private final DataSourceOperation dataSourceOperation;


}
