package com.firesoon.firesoondh.model.dtotype.db;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据库列信息
 * @author: Yz
 * @date: 2020/6/11
 */
@Data
public class DbColumnDTO {
    /**
     * 列名
     */
    private String name;
    /**
     * 类型
     */
    private String type;
    /**
     * 长度
     */
    private Integer length;
    /**
     * 是否为空
     */
    private Integer isNull;
    /**
     * 注释
     */
    private String comments;
    /**
     * 是否是主键 1是 0不是
     */
    private Integer isPrimaryKey;
    /**
     * 默认值
     */
    private String defaultValue;
}
