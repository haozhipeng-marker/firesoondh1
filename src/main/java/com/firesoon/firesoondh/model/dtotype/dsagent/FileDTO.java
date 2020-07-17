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
public class FileDTO {
    private String fileName;
    private Integer size;
    private String createTime;
    private String alias;
    private String fullName;
    private Object description;
    private Integer pid;
    private String updateTime;
    private Integer id;
    private String type;
    private Integer userId;
    private Boolean directory;
}
