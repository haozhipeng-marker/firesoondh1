package com.firesoon.firesoondh.model.dtotype.data.info;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 获取数据源下的某表的字段信息入参
 * @author: Yz
 * @date: 2020/6/16
 */
@Data
@ApiModel
public class ColumnListReqDTO {
    @ApiModelProperty("数据源id")
    private Integer dataSourceId;
    @ApiModelProperty("表名")
    private String tableName;
}
