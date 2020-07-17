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
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FSDH.DATA_ACCESS")
@Builder
public class DataAccessDO {
    /**
     * 主键id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 数据源id
     */
    @Column(name = "DATA_SOURCE_ID")
    private Integer dataSourceId;

    /**
     * 数据源类型
     */
    @Column(name = "DATA_SOURCE_TYPE")
    private String dataSourceType;

    /**
     * 数据源名称
     */
    @Column(name = "DATA_SOURCE_NAME")
    private String dataSourceName;

    /**
     * 接入名称
     */
    @Column(name = "ACCESS_NAME")
    private String accessName;

    /**
     * 接入备注
     */
    @Column(name = "ACCESS_DESC")
    private String accessDesc;

    /**
     * 创建者
     */
    @Column(name = "CREATOR_ID")
    private Integer creatorId;

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
}