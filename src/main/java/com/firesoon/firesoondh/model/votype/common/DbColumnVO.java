package com.firesoon.firesoondh.model.votype.common;

import com.firesoon.firesoondh.model.dtotype.db.DbColumnDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 列
 * @author: Yz
 * @date: 2020/6/30
 */

@Data
public class DbColumnVO  {
    /**
     * 列名
     */
    @ApiModelProperty("列名")
    private String name;
    /**
     * 类型
     */
    @ApiModelProperty("类型")
    private String type;
    /**
     * 长度
     */
    @ApiModelProperty("长度")
    private Integer length;
    /**
     * 是否为空
     */
    @ApiModelProperty("是否为空")
    private Integer isNull;
    /**
     * 注释
     */
    @ApiModelProperty("注释")
    private String comments;
    /**
     * 是否是主键 1是 0不是
     */
    @ApiModelProperty("是否是主键 1是 0不是")
    private Integer isPrimaryKey;
    /**
     * 默认值
     */
    @ApiModelProperty("默认值")
    private String defaultValue;
}
