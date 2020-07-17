package com.firesoon.firesoondh.model.dtotype.dsagent;

import com.alibaba.fastjson.JSONObject;
import lombok.*;

/**
 * @author Yuan
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class DataSourceDetailDTO {
    private Object principal;
    private String note;
    private String database;
    private String password;
    private JSONObject other;
    private String port;
    private String name;
    private String host;
    private String type;
    private String userName;
}
