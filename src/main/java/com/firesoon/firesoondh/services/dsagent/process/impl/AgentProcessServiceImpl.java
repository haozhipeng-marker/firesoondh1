package com.firesoon.firesoondh.services.dsagent.process.impl;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.dsagent.ProcessDefinitionDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.ProcessDefinitionInfoDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.dsagent.process.AgentProcessService;
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

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/15
 */
@Slf4j
@Service
public class AgentProcessServiceImpl implements AgentProcessService {

    @Autowired
    private RestTemplateUtil restTemplate;
    @Autowired
    private DsAgentUtil dsAgentUtil;


    @Override
    public Integer saveProcess(ProcessDefinitionDTO processDefinitionDTO) {
        //名称校验
        if (!saveProcessNameValidate(processDefinitionDTO.getProjectName(), processDefinitionDTO.getName())) {
            log.error("工作流保存名称校验失败 projectName={},name={}", processDefinitionDTO.getProjectName(), processDefinitionDTO.getName());
            throw new BusinessException(ResErro.ERRO_517);
        }
        //保存
        String url = String.format(DsConts.PROCESS_SAVE, processDefinitionDTO.getProjectName());
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(url, HttpMethod.POST,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_FORM_URLENCODED,
                new HashMap<String, Object>(8) {{
                    put("processDefinitionJson", JSONObject.toJSONString(processDefinitionDTO.getProcessDefinitionJson()));
                    put("name", processDefinitionDTO.getName());
                    put("description", processDefinitionDTO.getDescription());
                    put("locations", JSONObject.toJSONString(processDefinitionDTO.getLocations()));
                    put("connects", JSONObject.toJSONString(processDefinitionDTO.getConnects()));
                }},
                JSONObject.class);
        Res<Integer> agentRes = dsAgentUtil.responseEntityDispose(res, Integer.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", url, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public boolean saveProcessNameValidate(String projectName, String name) {
        String url = String.format(DsConts.PROCESS_VERIFY_NAME, projectName);
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(url, HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("name", name);
                }},
                JSONObject.class);
        Res<String> agentRes = dsAgentUtil.responseEntityDispose(res, String.class);


        if (DsConts.PROCESS_ALREADY_EXIST.equals(agentRes.getCode())) {
            //存在该名称
            return false;
        }

        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", url, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }


        return true;

    }

    @Override
    public void deleteProcess(Integer processDefinitionId, String projectName) {
        //保存
        String url = String.format(DsConts.PROCESS_DELETE, projectName);
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(url, HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("processDefinitionId", processDefinitionId);

                }},
                JSONObject.class);
        Res<String> agentRes = dsAgentUtil.responseEntityDispose(res, String.class);

        if (DsConts.PROCESS_IS_USING.equals(agentRes.getCode())) {
            log.error("ds工作流项目失败 工作流使用中 process={} project={} res={}", processDefinitionId, projectName, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_518);
        }


        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", url, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
    }

    @Override
    public ProcessDefinitionInfoDTO selectProcess(Integer processDefinitionId, String projectName) {
        //保存
        String url = String.format(DsConts.PROCESS_SELECT_ID, projectName);
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(url, HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("processId", processDefinitionId);

                }},
                JSONObject.class);
        Res<ProcessDefinitionInfoDTO> agentRes = dsAgentUtil.responseEntityDispose(res, ProcessDefinitionInfoDTO.class);

        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", url, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }
}
