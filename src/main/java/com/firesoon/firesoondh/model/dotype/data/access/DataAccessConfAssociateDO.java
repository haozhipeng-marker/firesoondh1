package com.firesoon.firesoondh.model.dotype.data.access;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Yuan
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "FSDH.DATA_ACCESS_CONF_ASSOCIATE")
public class DataAccessConfAssociateDO {
    /**
     * 数据接入配置id
     */
    @Id
    @Column(name = "DATA_ACCESS_CONF_ID")
    private String dataAccessConfId;

    /**
     * ds工作流id
     */
    @Column(name = "PROCESS_ID")
    private Integer processId;

    /**
     * ds资源id
     */
    @Column(name = "RESOURCES_ID")
    private Integer resourcesId;
}