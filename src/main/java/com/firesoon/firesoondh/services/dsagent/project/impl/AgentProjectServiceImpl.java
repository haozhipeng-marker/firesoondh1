package com.firesoon.firesoondh.services.dsagent.project.impl;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.dsagent.ProjectDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.dsagent.project.AgentProjectService;
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
 * @date: 2020/6/15
 */
@Service
@Slf4j
public class AgentProjectServiceImpl implements AgentProjectService {

    @Autowired
    private RestTemplateUtil restTemplate;
    @Autowired
    private DsAgentUtil dsAgentUtil;

    @Override
    public Integer create(String projectName, String description) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.CREATE_PROJECT,
                HttpMethod.POST,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_FORM_URLENCODED,
                new HashMap<String, Object>(2) {{
                    put("projectName", projectName);
                    put("description", description);
                }},
                JSONObject.class);
        Res<Integer> agentRes = dsAgentUtil.responseEntityDispose(res, Integer.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.CREATE_PROJECT, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public List<ProjectDTO> getAllList() {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.ALL_PROJECT_LIST,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                null,
                JSONObject.class);
        Res<List> agentRes = dsAgentUtil.responseEntityDispose(res, List.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.ALL_PROJECT_LIST, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return ConvertUtil.convert(agentRes.getData(), ProjectDTO.class);
    }

    @Override
    public ProjectDTO getById(Integer id) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.SELECT_PROJECT_ID,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("projectId", id);
                }},
                JSONObject.class);
        Res<ProjectDTO> agentRes = dsAgentUtil.responseEntityDispose(res, ProjectDTO.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.SELECT_PROJECT_ID, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public void delete(Integer id) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.DELETE_PROJECT,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("projectId", id);
                }},
                JSONObject.class);
        Res<String> agentRes = dsAgentUtil.responseEntityDispose(res, String.class);
        if (DsConts.PROJECT_IS_USING.equals(agentRes.getCode())) {
            log.error("ds删除项目失败 项目使用中 project={} res={}", id, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_518);
        }

        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.DELETE_PROJECT, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
    }
}
