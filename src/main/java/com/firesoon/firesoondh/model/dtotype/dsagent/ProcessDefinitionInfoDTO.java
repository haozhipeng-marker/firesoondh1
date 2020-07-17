package com.firesoon.firesoondh.model.dtotype.dsagent;

import lombok.Data;

/**
 * @author Yuan
 */
@Data
public class ProcessDefinitionInfoDTO {
    private String flag;
    private String description;
    private Object receiversCc;
    private Object globalParams;
    private Integer timeout;
    private String connects;
    private Object receivers;
    private Object globalParamList;
    private Integer id;
    private String resourceIds;
    private Object scheduleReleaseState;
    private String modifyBy;
    private Object globalParamMap;
    private String updateTime;
    private String processDefinitionJson;
    private Object userName;
    private Integer version;
    private Integer userId;
    private String createTime;
    private String name;
    private Integer tenantId;
    private String locations;
    private String releaseState;
    private Object projectName;
    private Integer projectId;
}
