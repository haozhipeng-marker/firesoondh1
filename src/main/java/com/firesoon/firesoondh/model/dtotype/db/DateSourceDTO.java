package com.firesoon.firesoondh.model.dtotype.db;

import com.firesoon.firesoondh.model.enumtype.DataSourceEnum;
import lombok.*;

/**
 * @description: 数据源信息
 * @author: Yz
 * @date: 2020/6/11
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
@Builder
public class DateSourceDTO {

    private String passWord;
    private String userName;

    private String url;


    private DataSourceEnum dataSourceEnum;
}
