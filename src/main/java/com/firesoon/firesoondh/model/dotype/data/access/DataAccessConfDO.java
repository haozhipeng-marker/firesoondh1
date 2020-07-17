package com.firesoon.firesoondh.model.dotype.data.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author Yuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FSDH.DATA_ACCESS_CONF")
public class DataAccessConfDO {
    /**
     * 主键id
     */
    @Id
    @Column(name = "ID")
    private String id;
    /**
     * 数据接入id
     */
    @Column(name = "DATA_ACCESS_ID")
    private String dataAccessId;
    /**
     * 源表
     */
    @Column(name = "ORIGIN_TABLE")
    private String originTable;

    /**
     * 目标表
     */
    @Column(name = "TARGET_TABLE")
    private String targetTable;

    /**
     * 是否全量 1是 0不是
     */
    @Column(name = "IS_FULL")
    private Integer isFull;

    /**
     * 增量类型 1merge 2insert
     */
    @Column(name = "ADD_TYPE")
    private Integer addType;

    /**
     * 主键(增量类型为merge时必须)
     */
    @Column(name = "PRIMARY_KEY")
    private String primaryKey;

    /**
     * 每个batch获取数据大小
     */
    @Column(name = "FETCH_SIZE")
    private Integer fetchSize;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    /**
     * 前置处理SQL
     */
    @Column(name = "PRE_SQL")
    private String preSql;

    /**
     * 数据过滤SQL
     */
    @Column(name = "FILTER_SQL")
    private String filterSql;
}