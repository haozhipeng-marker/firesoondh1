package com.firesoon.firesoondh.services.dsagent.user;

import com.firesoon.firesoondh.model.dtotype.dsagent.UserDTO;

/**
 * @description: 用户代理接口
 * @author: Yz
 * @date: 2020/6/12
 */
public interface AgentUserService {
    /**
     * 通过sessionId获取用户信息
     *
     * @return
     */
    UserDTO getUserInfo();
}
