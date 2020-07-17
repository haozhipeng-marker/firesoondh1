package com.firesoon.firesoondh.services.data.access;

import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import com.firesoon.firesoondh.model.dtotype.data.access.SynchronizationConfUpdateReqDTO;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/17
 */
public interface SynchronizationConfService {

    /**
     * 创建同步配置
     *
     * @param dataAccessId 数据接入id
     * @param dataSourceId 数据源id
     * @param tableName    表名
     * @return 配置id
     */
    String synchronizationConfAdd(String dataAccessId, Integer dataSourceId, String tableName);


    /**
     * 删除配置通过 数据接入配置id
     *
     * @param dataAccessConfId 数据接入配置id
     */
    void synchronizationConfDelete(String dataAccessConfId);

    /**
     * 查询数据接入配置
     *
     * @param dataAccessConfId 配置id
     * @return 结果
     */
    DataAccessConfDO dataAccessConfDetail(String dataAccessConfId);

    /**
     * 更新数据接入配置
     *
     * @param synchronizationConfUpdateReqDTO 相关信息
     */
    void synchronizationConfUpdate(SynchronizationConfUpdateReqDTO synchronizationConfUpdateReqDTO);
}
