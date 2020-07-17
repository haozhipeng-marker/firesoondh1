package com.firesoon.firesoondh.services.dsagent.project;

import com.firesoon.firesoondh.model.dtotype.dsagent.ProjectDTO;

import java.util.List;

/**
 * @description: ds项目接口代理
 * @author: Yz
 * @date: 2020/6/15
 */

public interface AgentProjectService {
    /**
     * 创建项目
     *
     * @param projectName 项目名称
     * @param description 备注
     * @return 项目id
     */
    Integer create(String projectName, String description);

    /**
     * 获取所有的项目列表
     *
     * @return 项目列表
     */
    List<ProjectDTO> getAllList();

    /**
     * 通过项目id查询项目
     *
     * @param id 项目id
     * @return 项目实体
     */
    ProjectDTO getById(Integer id);

    /**
     * 删除
     *
     * @param id 项目id
     */
    void delete(Integer id);
}
