package com.firesoon.firesoondh.controller.common;

import com.firesoon.firesoondh.controller.BaseController;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.data.info.ColumnListReqDTO;
import com.firesoon.firesoondh.model.dtotype.db.DateSourceDTO;
import com.firesoon.firesoondh.model.dtotype.db.DbColumnDTO;
import com.firesoon.firesoondh.model.dtotype.db.DbCommentsDTO;
import com.firesoon.firesoondh.model.dtotype.dsagent.DataSourceDetailDTO;
import com.firesoon.firesoondh.model.enumtype.DataSourceEnum;
import com.firesoon.firesoondh.model.votype.common.DbColumnVO;
import com.firesoon.firesoondh.model.votype.common.DbTableVO;
import com.firesoon.firesoondh.services.dsagent.datasource.AgentDataSourceService;
import com.firesoon.firesoondh.utils.ConvertUtil;
import com.firesoon.firesoondh.utils.db.DataSourceUtil;
import com.firesoon.firesoondh.utils.db.operation.DataSourceOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 获取数据源中的一些信息
 * @author: Yz
 * @date: 2020/6/16
 */
@Api(tags = "通用获取数据源下的信息")
@RestController
@RequestMapping("/common/dataInfo")
public class DataInfoController extends BaseController {
    @Autowired
    private AgentDataSourceService agentDataSourceService;

    @ApiOperation("获取数据源下的所有表")
    @PostMapping("/tableList")
    public Res<List<DbTableVO>> tableList(@RequestParam Integer dataSourceId) {
        //获取数据源信息
        DataSourceDetailDTO dataSourceDetailDTO = agentDataSourceService.getDataSourceDetails(dataSourceId);
        //获取连接类型
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf(dataSourceDetailDTO.getType().toUpperCase());
        //获取url
        String url = String.format(dataSourceEnum.getUrlTemplate(), dataSourceDetailDTO.getHost(),
                dataSourceDetailDTO.getPort(), dataSourceDetailDTO.getDatabase());
        //获取动态连接
        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTemplate(
                new DateSourceDTO(
                        dataSourceDetailDTO.getPassword(),
                        dataSourceDetailDTO.getUserName(), url, dataSourceEnum));
        DataSourceOperation dataSourceOperation = dataSourceEnum.getDataSourceOperation();

        List<? extends DbCommentsDTO> dbCommentsDTOList = dataSourceOperation.getTables(jdbcTemplate);

        return Res.succ(ConvertUtil.convert(dbCommentsDTOList,DbTableVO.class));
    }

    @ApiOperation("获取数据源下的某表的字段信息")
    @PostMapping("/columnList")
    public Res<List<DbColumnVO>> columnList(@ApiParam("表及数据源信息") @RequestBody ColumnListReqDTO columnListReqDTO) {

        //获取数据源信息
        DataSourceDetailDTO dataSourceDetailDTO = agentDataSourceService.getDataSourceDetails(columnListReqDTO.getDataSourceId());
        //获取连接类型
        DataSourceEnum dataSourceEnum = DataSourceEnum.valueOf(dataSourceDetailDTO.getType().toUpperCase());
        //获取url
        String url = String.format(dataSourceEnum.getUrlTemplate(), dataSourceDetailDTO.getHost(),
                dataSourceDetailDTO.getPort(), dataSourceDetailDTO.getDatabase());
        //获取动态连接
        JdbcTemplate jdbcTemplate = DataSourceUtil.getJdbcTemplate(
                new DateSourceDTO(
                        dataSourceDetailDTO.getPassword(),
                        dataSourceDetailDTO.getUserName(), url, dataSourceEnum));
        DataSourceOperation dataSourceOperation = dataSourceEnum.getDataSourceOperation();

        List<? extends DbColumnDTO> oracleColumnDTO = dataSourceOperation.getColumns(jdbcTemplate, columnListReqDTO.getTableName());

        return Res.succ(ConvertUtil.convert(oracleColumnDTO,DbColumnVO.class));
    }

}
