package com.firesoon.firesoondh.services.dsagent.upload;

import com.firesoon.firesoondh.model.dtotype.dsagent.FileDTO;

import java.io.File;
import java.util.List;

/**
 * @description: ds资源中心接口代理
 * @author: Yz
 * @date: 2020/6/15
 */
public interface AgentUploadService {
    /**
     * 删除文件
     *
     * @param id 文件id
     */
    void deleteResource(Integer id);

    /**
     * 校验文件名是否可建
     *
     * @param fullName 文件名地址全路径
     * @return 是否
     */
    boolean verifyName(String fullName);

    /**
     * 上传文件夹
     *
     * @param parentDirId 父级文件夹id （不填默认根目录）
     * @param dirName     文件夹名称
     * @param currentDir  全路径
     * @return 文件id
     */
    Integer addDir(Integer parentDirId, String dirName, String currentDir);

    /**
     * 上传文件
     *
     * @param parentDirId 父级文件夹id （不填默认根目录）
     * @param fileName    文件名
     * @param file        文件
     * @param currentDir  全路径
     * @return 文件id
     */
    Integer addFile(Integer parentDirId, String fileName, File file, String currentDir);

    /**
     * 查询某个资源
     *
     * @param resourceId 资源id
     * @return 文件
     */
    FileDTO selectResource(Integer resourceId);


    /**
     * 查询某个资源
     *
     * @param resourceFullName 资源全路径名称
     * @return 文件
     */
    FileDTO selectResource(String resourceFullName);

    /**
     * 查某个目录下所有文件
     *
     * @param dirId 资源id
     * @return 文件列表
     */
    List<FileDTO> selectDir(Integer dirId);


}
