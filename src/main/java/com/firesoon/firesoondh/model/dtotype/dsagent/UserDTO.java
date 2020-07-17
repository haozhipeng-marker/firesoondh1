package com.firesoon.firesoondh.model.dtotype.dsagent;


import lombok.Data;

import java.util.Date;

/**
 * @author Yuan
 */
@Data
public class UserDTO {
    private String userPassword;
    private String updateTime;
    private Object tenantCode;
    private String userName;
    private String queueName;
    private String tenantName;
    private String phone;
    private Date createTime;
    private Integer tenantId;
    private Integer id;
    private String userType;
    private Object alertGroup;
    private String email;
    private String queue;
}
