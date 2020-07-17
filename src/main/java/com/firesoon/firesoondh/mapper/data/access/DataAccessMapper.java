package com.firesoon.firesoondh.mapper.data.access;


import com.firesoon.firesoondh.model.dotype.data.access.DataAccessDO;
import com.firesoon.firesoondh.model.dtotype.BaseListSearchDTO;
import com.firesoon.firesoondh.model.votype.data.access.DataAccessVO;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Yuan
 */
@Component
public interface DataAccessMapper extends Mapper<DataAccessDO> {
    /**
     * 数据接入树状信息
     *
     * @param baseListSearchDTO 分页信息
     * @return data
     */
    List<DataAccessVO> dataAccessTreeList(BaseListSearchDTO baseListSearchDTO);
}