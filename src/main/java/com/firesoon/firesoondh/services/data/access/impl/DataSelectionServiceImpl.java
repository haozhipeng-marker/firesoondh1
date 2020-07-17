package com.firesoon.firesoondh.services.data.access.impl;

import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.mapper.data.access.DataAccessAssociateMapper;
import com.firesoon.firesoondh.mapper.data.access.DataAccessConfMapper;
import com.firesoon.firesoondh.mapper.data.access.DataAccessMapper;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessAssociateDO;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessDO;
import com.firesoon.firesoondh.model.dtotype.BaseListSearchDTO;
import com.firesoon.firesoondh.model.dtotype.data.access.DataSelectionOperationReqDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDetailDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.UserDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.model.votype.data.access.DataAccessVO;
import com.firesoon.firesoondh.model.votype.data.access.DataSourceAccessVO;
import com.firesoon.firesoondh.services.data.access.DataSelectionService;
import com.firesoon.firesoondh.services.data.access.SynchronizationConfService;
import com.firesoon.firesoondh.services.dsagent.datasource.AgentDataSourceService;
import com.firesoon.firesoondh.services.dsagent.project.AgentProjectService;
import com.firesoon.firesoondh.services.dsagent.user.AgentUserService;
import com.firesoon.firesoondh.utils.ConvertUtil;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/17
 */
@Slf4j
@Service
public class DataSelectionServiceImpl implements DataSelectionService {

    @Autowired
    private DataAccessMapper dataAccessMapper;
    @Autowired
    private DataAccessAssociateMapper dataAccessAssociateMapper;
    @Autowired
    private SynchronizationConfService synchronizationConfService;
    @Autowired
    private AgentDataSourceService agentDataSourceService;
    @Autowired
    private AgentProjectService agentProjectService;
    @Autowired
    private AgentUserService userService;
    @Autowired
    private DataAccessConfMapper dataAccessConfMapper;

    @Override
    public List<DataAccessVO> dataAccessTreeList(BaseListSearchDTO baseListSearchDTO) {

        PageHelper.startPage(baseListSearchDTO.getPageNum(), baseListSearchDTO.getPageSize());
        return dataAccessMapper.dataAccessTreeList(baseListSearchDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String dataSelectionAdd(DataSelectionOperationReqDTO dataSelectionOperationReqDTO) {
        //判断是否已经存在该数据源
        if (!CollectionUtils.isEmpty(
                dataAccessMapper.select(DataAccessDO.builder()
                        .dataSourceId(dataSelectionOperationReqDTO.getDataSourceId()).build()))) {
            throw new BusinessException(ResErro.ERRO_520);
        }
        UserDTO userInfo = userService.getUserInfo();
        //数据源信息查询
        DataSourceDetailDTO dataSourceDetail = agentDataSourceService.getDataSourceDetails(dataSelectionOperationReqDTO.getDataSourceId());
        //dataAccess入库
        String dataAccessId = UUID.randomUUID().toString();
        dataAccessMapper.insertSelective(DataAccessDO.builder()
                .accessDesc(dataSelectionOperationReqDTO.getAccessDesc())
                .accessName(dataSelectionOperationReqDTO.getAccessName())
                .dataSourceId(dataSelectionOperationReqDTO.getDataSourceId())
                .createTime(new Date())
                .creatorId(userInfo.getId())
                .dataSourceName(dataSourceDetail.getName())
                .dataSourceType(dataSourceDetail.getType())
                .id(dataAccessId).build());
        //创建项目
        Integer projectId = agentProjectService.create(dataSourceDetail.getName() + "数据接入"
                , dataSelectionOperationReqDTO.getAccessName());
        //绑定项目与dataAccess关联
        dataAccessAssociateMapper.insert(DataAccessAssociateDO.builder()
                .dataAccessId(dataAccessId)
                .projectId(projectId)
                .build());
        //创建dataAccessConf

        dataSelectionOperationReqDTO.getTableName().forEach(tableName -> synchronizationConfService.synchronizationConfAdd(dataAccessId, dataSelectionOperationReqDTO.getDataSourceId(), tableName));

        return dataAccessId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dataSelectionUpdate(DataSelectionOperationReqDTO dataSelectionOperationReqDTO) {
        String dataAccessId = dataSelectionOperationReqDTO.getDataAccessId();
        //更新数据接入
        dataAccessMapper.updateByPrimaryKeySelective(DataAccessDO.builder()
                .id(dataAccessId)
                .accessDesc(dataSelectionOperationReqDTO.getAccessDesc())
                .updateTime(new Date())
                .build());

        List<DataAccessConfDO> orgConfList = dataAccessConfMapper.select(DataAccessConfDO.builder()
                .dataAccessId(dataAccessId)
                .build());


        //当为空时特殊判断
        if (CollectionUtils.isEmpty(dataSelectionOperationReqDTO.getTableName())) {
            if (!CollectionUtils.isEmpty(orgConfList)) {
                orgConfList.forEach(dataAccessConfDO -> synchronizationConfService.synchronizationConfDelete(dataAccessConfDO.getId()));
            }
            return;
        }
        Map<String, String> newTableNameList = dataSelectionOperationReqDTO.getTableName().stream().collect(Collectors.toMap(Function.identity(), Function.identity()));
        Map<String, String> orgTableNameList = orgConfList.stream().collect(Collectors.toMap(DataAccessConfDO::getOriginTable, DataAccessConfDO::getOriginTable));

        //需要删除的
        orgConfList.stream()
                .filter(dataAccessConfDO -> !ObjectUtils.allNotNull(newTableNameList.get(dataAccessConfDO.getOriginTable())))
                .forEach(dataAccessConfDO -> synchronizationConfService.synchronizationConfDelete(dataAccessConfDO.getId()));

        //需要添加的
        dataSelectionOperationReqDTO.getTableName().stream()
                .filter(tableName -> !ObjectUtils.allNotNull(orgTableNameList.get(tableName)))
                .forEach(tableName -> synchronizationConfService.synchronizationConfAdd(dataAccessId, dataSelectionOperationReqDTO.getDataSourceId(), tableName));

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void dataSelectionDelete(String dataAccessId) {

        //删除数据接入
        dataAccessMapper.deleteByPrimaryKey(dataAccessId);
        //删除数据配置
        dataAccessConfMapper.select(DataAccessConfDO.builder()
                .dataAccessId(dataAccessId)
                .build())
                .forEach(dataAccessConfDO -> synchronizationConfService.synchronizationConfDelete(dataAccessConfDO.getId()));
        //删除项目
        DataAccessAssociateDO dataAccessAssociate = dataAccessAssociateMapper.selectByPrimaryKey(dataAccessId);
        agentProjectService.delete(dataAccessAssociate.getProjectId());
        //删除关联
        dataAccessAssociateMapper.deleteByPrimaryKey(dataAccessId);
    }

    @Override
    public List<DataSourceAccessVO> dataSourceList() {
        List<DataSourceAccessVO> res = new ArrayList<>();


        //所有已经接入的数据源
        List<Integer> userDataSourceId = dataAccessMapper.selectAll().stream()
                .map(DataAccessDO::getDataSourceId)
                .collect(Collectors.toList());
        //对所有的数据源进行 是否使用判断
        agentDataSourceService.getDataSourceList().forEach(dataSourceDTO -> {
            DataSourceAccessVO dataSourceAccessVO = ConvertUtil.convert(dataSourceDTO, DataSourceAccessVO.class);
            if (userDataSourceId.contains(dataSourceDTO.getId())) {
                dataSourceAccessVO.setDisabled(true);
            } else {
                dataSourceAccessVO.setDisabled(false);
            }
            res.add(dataSourceAccessVO);
        });

        return res;
    }


}
