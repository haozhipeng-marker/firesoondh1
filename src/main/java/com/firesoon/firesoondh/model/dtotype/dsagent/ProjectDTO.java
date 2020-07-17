package com.firesoon.firesoondh.model.dtotype.dsagent;

import lombok.Data;

import java.util.Date;

/**
 * @author Yuan
 */
@Data
public class ProjectDTO {
    private Integer perm;
    private Date createTime;
    private Integer defCount;
    private String name;
    private String description;
    private Integer instRunningCount;
    private Date updateTime;
    private Integer id;
    private String userName;
    private Integer userId;
}
