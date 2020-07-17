package com.firesoon.firesoondh.model.dtotype;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @description: 通用列表查询DTO
 * @author: Yz
 * @date: 2020/6/3
 */
@Data
@ApiModel
public class BaseListSearchDTO {

    @ApiModelProperty("当前页 不传全表查询")
    private Integer pageNum;
    @ApiModelProperty("每页条数")
    private Integer pageSize;
    @ApiModelProperty("查询内容")
    private String search;
    @ApiModelProperty("起始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
}
