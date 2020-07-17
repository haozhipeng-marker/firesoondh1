package com.firesoon.firesoondh.model.votype.data.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据源接入
 * @author: Yz
 * @date: 2020/6/28
 */
@Data
@ApiModel
public class DataSourceAccessVO {
    @ApiModelProperty("数据源名称")
    private String name;
    @ApiModelProperty("数据源id")
    private Integer id;
    @ApiModelProperty("是否使用 true使用 false未使用")
    private Boolean disabled;
}
