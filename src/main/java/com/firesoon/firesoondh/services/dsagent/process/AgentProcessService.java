package com.firesoon.firesoondh.services.dsagent.process;

import com.firesoon.firesoondh.model.dtotype.dsagent.ProcessDefinitionDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.ProcessDefinitionInfoDTO;

/**
 * @description: ds工作流接口代理
 * @author: Yz
 * @date: 2020/6/15
 */
public interface AgentProcessService {
    /**
     * 保存工作流定义
     *
     * @param processDefinitionDTO 定义信息
     * @return 工作流定义id
     */
    Integer saveProcess(ProcessDefinitionDTO processDefinitionDTO);

    /**
     * 工作流名称校验
     *
     * @param projectName 项目名称
     * @param name        工作流名称
     * @return 是否校验成功
     */
    boolean saveProcessNameValidate(String projectName, String name);

    /**
     * 删除工作流定义
     *
     * @param processDefinitionId 工作流定义id
     * @param projectName         项目名称
     */
    void deleteProcess(Integer processDefinitionId, String projectName);


    /**
     * 查询工作流定义
     *
     * @param processDefinitionId 工作流定义id
     * @param projectName         项目名称
     * @return 工作流实体
     */
    ProcessDefinitionInfoDTO selectProcess(Integer processDefinitionId, String projectName);
}
