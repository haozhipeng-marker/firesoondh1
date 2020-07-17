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
@Table(name = "FSDH.DATA_ACCESS_ASSOCIATE")
public class DataAccessAssociateDO {
    /**
     * 数据人接入id
     */
    @Id
    @Column(name = "DATA_ACCESS_ID")
    private String dataAccessId;

    /**
     * ds项目id
     */
    @Column(name = "PROJECT_ID")
    private Integer projectId;
}