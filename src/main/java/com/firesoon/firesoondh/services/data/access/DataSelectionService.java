package com.firesoon.firesoondh.services.data.access;

import com.firesoon.firesoondh.model.dtotype.BaseListSearchDTO;
import com.firesoon.firesoondh.model.dtotype.data.access.DataSelectionOperationReqDTO;
import com.firesoon.firesoondh.model.votype.data.access.DataAccessVO;
import com.firesoon.firesoondh.model.votype.data.access.DataSourceAccessVO;

import java.util.List;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/17
 */
public interface DataSelectionService {
    /**
     * 获取树状数据接入信息
     *
     * @param baseListSearchDTO 查询信息
     * @return 数据信息
     */
    List<DataAccessVO> dataAccessTreeList(BaseListSearchDTO baseListSearchDTO);


    /**
     * 数据选择添加
     *
     * @param dataSelectionOperationReqDTO 相关信息
     * @return dataAccessId
     */
    String dataSelectionAdd(DataSelectionOperationReqDTO dataSelectionOperationReqDTO);

    /**
     * 数据选择添加
     *
     * @param dataSelectionOperationReqDTO 相关信息
     */
    void dataSelectionUpdate(DataSelectionOperationReqDTO dataSelectionOperationReqDTO);

    /**
     * 删除数据接入
     *
     * @param dataAccessId 数据接入id
     */
    void dataSelectionDelete(String dataAccessId);

    /**
     * 数据源列表
     *
     * @return
     */
    List<DataSourceAccessVO> dataSourceList();
}
