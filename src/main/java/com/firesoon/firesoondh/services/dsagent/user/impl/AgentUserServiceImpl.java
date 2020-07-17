package com.firesoon.firesoondh.services.dsagent.user.impl;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.dsagent.UserDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.dsagent.user.AgentUserService;
import com.firesoon.firesoondh.utils.DsAgentUtil;
import com.firesoon.firesoondh.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


/**
 * @description:
 * @author: Yz
 * @date: 2020/6/12
 */
@Slf4j
@Service
public class AgentUserServiceImpl implements AgentUserService {
    @Autowired
    private RestTemplateUtil restTemplate;

    @Autowired
    private DsAgentUtil dsAgentUtil;

    @Override
    public UserDTO getUserInfo() {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.GET_USER_INFO,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                null,
                JSONObject.class);
        Res<UserDTO> agentRes = dsAgentUtil.responseEntityDispose(res, UserDTO.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.GET_USER_INFO, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }
}
