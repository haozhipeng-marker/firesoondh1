package com.firesoon.firesoondh.mapper.data.access;


import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Yuan
 */
@Component
public interface DataAccessConfMapper extends Mapper<DataAccessConfDO> {
    /**
     * 通过数据接入id查询数据接入配置
     *
     * @param dataAccessId 数据接入id
     * @return list
     */
    List<DataAccessConfDO> selectByDataAccessId(String dataAccessId);
}