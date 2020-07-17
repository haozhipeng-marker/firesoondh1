package com.firesoon.firesoondh.services.dsagent.datasource;

import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDetailDTO;

import java.util.List;

/**
 * @description: 数据源接口代理
 * @author: Yz
 * @date: 2020/6/16
 */
public interface AgentDataSourceService {
    /**
     * 获取数据源连接详情
     *
     * @param dateSourceId 数据源id
     * @return 数据源信息
     */
    DataSourceDetailDTO getDataSourceDetails(Integer dateSourceId);


    /**
     * 获取获取数据源列表
     *
     * @return 数据源信息
     */
    List<DataSourceDTO> getDataSourceList();
}
