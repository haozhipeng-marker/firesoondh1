package com.firesoon.firesoondh.services.dsagent.upload.impl;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.exception.BusinessException;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.dsagent.FileDTO;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import com.firesoon.firesoondh.services.dsagent.upload.AgentUploadService;
import com.firesoon.firesoondh.utils.ConvertUtil;
import com.firesoon.firesoondh.utils.DsAgentUtil;
import com.firesoon.firesoondh.utils.RestTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * @description:
 * @author: Yz
 * @date: 2020/6/18
 */
@Slf4j
@Component
public class AgentUploadServiceImpl implements AgentUploadService {


    @Autowired
    private RestTemplateUtil restTemplate;
    @Autowired
    private DsAgentUtil dsAgentUtil;

    @Override
    public void deleteResource(Integer id) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_FILE_DELETE,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(4) {{
                    put("id", id);
                }},
                JSONObject.class);
        Res<String> agentRes = dsAgentUtil.responseEntityDispose(res, String.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_FILE_DELETE, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
    }

    @Override
    public boolean verifyName(String fullName) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_FILE_VERIFY,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(2) {{
                    put("fullName", fullName);
                    put("type", "FILE");
                }},
                JSONObject.class);
        Res<String> agentRes = dsAgentUtil.responseEntityDispose(res, String.class);

        if (DsConts.RESOURCE_ALREADY_EXIST.equals(agentRes.getCode())) {
            return false;
        }
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_FILE_VERIFY, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return true;
    }

    @Override
    public Integer addDir(Integer parentDirId, String dirName, String currentDir) {
        if (parentDirId == null) {
            parentDirId = -1;
            currentDir = "/";
        }
        Integer finalParentDirId = parentDirId;
        String finalCurrentDir = currentDir;
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_CREATE_DIR,
                HttpMethod.POST,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_FORM_URLENCODED,
                new HashMap<String, Object>(4) {{
                    put("name", dirName);
                    put("pid", finalParentDirId);
                    put("currentDir", finalCurrentDir);
                    put("type", "FILE");
                }},
                JSONObject.class);
        Res<Integer> agentRes = dsAgentUtil.responseEntityDispose(res, Integer.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_CREATE_DIR, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public Integer addFile(Integer parentDirId, String fileName, File file, String currentDir) {
        //判断文件是否存在
        if (!verifyName(currentDir.concat(fileName))) {
            log.error("文件创建校验失败 fullName={}", currentDir.concat(fileName));
            throw new BusinessException(ResErro.ERRO_517);
        }

        if (parentDirId == null) {
            parentDirId = -1;
            currentDir = "/";
        }
        Integer finalParentDirId = parentDirId;
        String finalCurrentDir = currentDir;
        FileSystemResource resource = new FileSystemResource(file);
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_CREATE_FILE,
                HttpMethod.POST,
                dsAgentUtil.getSessionId(),
                MediaType.MULTIPART_FORM_DATA,
                new HashMap<String, Object>(8) {{
                    put("name", fileName);
                    put("pid", finalParentDirId);
                    put("currentDir", finalCurrentDir);
                    put("type", "FILE");
                    put("file", resource);
                    put("description", "");
                }},
                JSONObject.class);
        Res<Integer> agentRes = dsAgentUtil.responseEntityDispose(res, Integer.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_CREATE_FILE, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public FileDTO selectResource(Integer resourceId) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_SELECT_RESOURCE_BY_ID,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(1) {{
                    put("id", resourceId);
                }},
                JSONObject.class);
        Res<FileDTO> agentRes = dsAgentUtil.responseEntityDispose(res, FileDTO.class);

        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_SELECT_RESOURCE_BY_ID, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public FileDTO selectResource(String resourceFullName) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_SELECT_RESOURCE_BY_NAME,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(2) {{
                    put("fullName", resourceFullName);
                    put("type", "FILE");
                }},
                JSONObject.class);
        Res<FileDTO> agentRes = dsAgentUtil.responseEntityDispose(res, FileDTO.class);

        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_SELECT_RESOURCE_BY_NAME, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return agentRes.getData();
    }

    @Override
    public List<FileDTO> selectDir(Integer dirId) {
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.UPLOAD_DIR_SELECT,
                HttpMethod.GET,
                dsAgentUtil.getSessionId(),
                MediaType.APPLICATION_JSON,
                new HashMap<String, Object>(4) {{
                    put("id", dirId);
                    put("type", "FILE");
                    put("pageNo", 1);
                    put("pageSize", 50);
                }},
                JSONObject.class);
        Res<List> agentRes = dsAgentUtil.responseEntityDispose(res, List.class);
        if (agentRes.getCode() != HttpStatus.OK.value()) {
            log.error("请求ds接口错误 地址为{} 错误为{}", DsConts.UPLOAD_DIR_SELECT, agentRes.toString());
            throw new BusinessException(ResErro.ERRO_513);
        }
        return ConvertUtil.convert(agentRes.getData(), FileDTO.class);
    }
}
