package com.firesoon.firesoondh.model.dtotype.data.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据接入配置更新实体
 * @author: Yz
 * @date: 2020/6/18
 */
@Data
@ApiModel
public class SynchronizationConfUpdateReqDTO {

    @ApiModelProperty("数据接入配置id")
    private String dataAccessConfId;

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
