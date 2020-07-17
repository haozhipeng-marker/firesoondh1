package com.firesoon.firesoondh.model.votype.data.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yuan
 */
@Data
@ApiModel
public class DataAccessConfVO {

    @ApiModelProperty("dataAccessConfId")
    private String id;
    @ApiModelProperty("源表")
    private String originTable;

    @ApiModelProperty("目标表")
    private String targetTable;

    @ApiModelProperty("是否全量 1是 0不是")
    private Integer isFull;

    @ApiModelProperty("增量类型 1merge 2insert")
    private Integer addType;

    @ApiModelProperty("主键(增量类型为merge时必须)")
    private String primaryKey;

    @ApiModelProperty("每个batch获取数据大小")
    private Integer fetchSize;

    @ApiModelProperty("前置处理SQL")
    private String preSql;

    @ApiModelProperty("数据过滤SQL")
    private String filterSql;
}