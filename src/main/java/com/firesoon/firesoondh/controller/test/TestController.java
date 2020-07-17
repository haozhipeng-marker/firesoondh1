package com.firesoon.firesoondh.controller.test;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.constant.DsConts;
import com.firesoon.firesoondh.controller.BaseController;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.BaseListSearchDTO;
import com.firesoon.firesoondh.model.dtotype.db.DateSourceDTO;
import com.firesoon.firesoondh.model.dtotype.db.DbColumnDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.UserDTO;
import com.firesoon.firesoondh.model.enumtype.DataSourceEnum;
import com.firesoon.firesoondh.utils.DsAgentUtil;
import com.firesoon.firesoondh.utils.RestTemplateUtil;
import com.firesoon.firesoondh.utils.db.DataSourceUtil;
import com.firesoon.firesoondh.utils.db.operation.DataSourceOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 测试
 * @author: Yz
 * @date: 2020/6/3
 */
@Api(tags = "测试")
@RestController
@RequestMapping("/test")
public class TestController extends BaseController {

    @Autowired
    private RestTemplateUtil restTemplate;

    @Autowired
    private DsAgentUtil dsAgentUtil;


    @ApiOperation("测试方法")
    @PostMapping("/aa")
    public Res<List<DbColumnDTO>> test(@ApiParam @RequestBody BaseListSearchDTO baseListSearchDTO) {
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf("ORACLE");

        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTemplate(
                new DateSourceDTO(
                        "cpw", "cpw",
                        String.format(dataSourceEnum.getUrlTemplate(), "172.16.4.101", "1521", "orcl"), dataSourceEnum));
        DataSourceOperation dataSourceOperation = dataSourceEnum.getDataSourceOperation();
        //List<DBCommentsDTO> dbCommentsDTOList= (List<DBCommentsDTO>) dataSourceOperation.getTables(jdbcTemplate);
        List<DbColumnDTO> dbCommentsDTOList = (List<DbColumnDTO>) dataSourceOperation.getColumns(jdbcTemplate, "ZSS_OR_ZZD_DELETE_RECORDS");

        return Res.succ(dbCommentsDTOList);
    }


    @ApiOperation("测试方法")
    @PostMapping("/bb")
    public Res<String> bb(@ApiParam @RequestBody BaseListSearchDTO baseListSearchDTO) {
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf("ORACLE");
        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTemplate(
                new DateSourceDTO(
                        "cpw", "cpw",
                        String.format(dataSourceEnum.getUrlTemplate(), "172.16.4.101", "1521", "orcl"), dataSourceEnum));
        DataSourceOperation dataSourceOperation = dataSourceEnum.getDataSourceOperation();
        String dbCommentsDTOList = dataSourceOperation.getCreateTableSql(jdbcTemplate, "ZSS_OR_ZZD_DELETE_RECORDS");

        return Res.succ(dbCommentsDTOList);
    }

    @ApiOperation("测试方法")
    @PostMapping("/dd")
    public Res<UserDTO> getUser(@ApiParam @RequestBody BaseListSearchDTO baseListSearchDTO) {
        return Res.succ();
    }


    @ApiOperation("测试方法")
    @PostMapping("/projects/list-paging")
    public Res<JSONObject> list(@ApiParam @RequestBody BaseListSearchDTO baseListSearchDTO) {
        Map<String, Object> params = new HashMap<>(2);
        params.put("pageSize", baseListSearchDTO.getPageSize());
        params.put("pageNo", baseListSearchDTO.getPageNum());
        ResponseEntity<JSONObject> res = restTemplate.exchangeGetOrPost(DsConts.PROJECT_LIST_PAG, HttpMethod.GET, dsAgentUtil.getSessionId(), MediaType.APPLICATION_FORM_URLENCODED, params, JSONObject.class);
        return dsAgentUtil.responseEntityDispose(res, JSONObject.class);
    }

}
