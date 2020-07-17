package com.firesoon.firesoondh.model.votype.common;

import com.firesoon.firesoondh.model.dtotype.db.DbCommentsDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 表
 * @author: Yz
 * @date: 2020/6/30
 */
@Data
public class DbTableVO   {
    /**
     * 名称
     */
    @ApiModelProperty("表名")
    private String name;
    /**
     * 注释
     */
    @ApiModelProperty("表注释")
    private String comments;
}
