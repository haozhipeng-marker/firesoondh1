package com.firesoon.firesoondh.model.votype.data.access;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/17
 */
@Data
@ApiModel
public class DataAccessVO {
    @ApiModelProperty("数据接入id")
    private String id;
    @ApiModelProperty("数据接入名称")
    private String name;
    @ApiModelProperty("数据源名称")
    private String dataSourceName;
    @ApiModelProperty("数据源id")
    private Integer dataSourceId;
    @ApiModelProperty("数据源类型")
    private String dataSourceType;
    @ApiModelProperty("描述")
    private String desc;
    @ApiModelProperty("创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @ApiModelProperty("更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @ApiModelProperty("同步表列表")
    List<Table> tableList;

    @Data
    @ApiModel
    public static class Table {
        @ApiModelProperty("数据接入配置id")
        private String id;
        @ApiModelProperty("表名")
        private String name;
        @ApiModelProperty("同步类型")
        private String synType;
        @ApiModelProperty("创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;
        @ApiModelProperty("更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
