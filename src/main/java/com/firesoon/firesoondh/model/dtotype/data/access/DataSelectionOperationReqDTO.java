package com.firesoon.firesoondh.model.dtotype.data.access;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @description: 数据选择操作实体
 * @author: Yz
 * @date: 2020/6/17
 */
@Data
@ApiModel
public class DataSelectionOperationReqDTO {

    @ApiModelProperty("数据接入id")
    private String dataAccessId;


    @ApiModelProperty("数据源id")
    private Integer dataSourceId;


    @ApiModelProperty("接入名称")
    private String accessName;


    @ApiModelProperty("接入备注")
    private String accessDesc;

    @ApiModelProperty("需要同步的表名列表")
    private List<String> tableName;


}
