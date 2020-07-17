package com.firesoon.firesoondh.model.dtotype.dsagent;


import lombok.*;

/**
 * @author Yuan
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class DataSourceDTO {
    private String typeot;
    private Object note;
    private String connectionParams;
    private String createTime;
    private String name;
    private String updateTime;
    private Integer id;
    private Object userName;
    private String type;
    private Integer userId;
}
