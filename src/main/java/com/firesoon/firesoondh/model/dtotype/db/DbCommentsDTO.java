package com.firesoon.firesoondh.model.dtotype.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据库注释
 * @author: Yz
 * @date: 2020/6/11
 */
@Data
public class DbCommentsDTO {
    /**
     * 名称
     */
    private String name;
    /**
     * 注释
     */
    private String comments;
}
