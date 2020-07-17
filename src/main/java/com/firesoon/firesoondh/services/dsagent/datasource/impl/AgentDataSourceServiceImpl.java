package com.firesoon.firesoondh.services.dsagent.datasource.impl;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDetailDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.dsagent.datasource.AgentDataSourceService;
import com.firesoon.firesoondh.utils.ConvertUtil;
import com.firesoon.firesoondh.utils.DsAgentUtil;
import com.firesoon.firesoondh.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/16
 */
@Slf4j
@Service
public class AgentDataSourceServiceImpl implements AgentDataSourceService {

    @Autowired
    private RestTemplateUtil restTemplate;
    @Autowired
    private DsAgentUtil dsAgentUtil;


    @Override
    public DataSourceDetailDTO getDataSourceDetails(Integer dateSourceId) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.DATASOURCES_DETAILS,
                HttpMethod.POST,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_FORM_URLENCODED,
                new HashMap<String, Object>(4) {{
                    put("id", dateSourceId);
                }},
                JSONObject.class);
        Res<DataSourceDetailDTO> agentRes = dsAgentUtil.responseEntityDispose(res, DataSourceDetailDTO.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.DATASOURCES_DETAILS, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public List<DataSourceDTO> getDataSourceList() {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.DATASOURCES_LIST,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_FORM_URLENCODED,
                new HashMap<String, Object>(2) {{
                    put("pageNo", 1);
                    put("pageSize", 50);
                }},
                JSONObject.class);
        Res<List> agentRes = dsAgentUtil.responseEntityDispose(res, List.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.DATASOURCES_LIST, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return ConvertUtil.convert(agentRes.getData(), DataSourceDTO.class);
    }
}
