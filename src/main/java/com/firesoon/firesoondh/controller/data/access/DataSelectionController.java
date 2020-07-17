package com.firesoon.firesoondh.controller.data.access;

import com.firesoon.firesoondh.controller.BaseController;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dtotype.BaseListSearchDTO;
import com.firesoon.firesoondh.model.dtotype.data.access.DataSelectionOperationReqDTO;
import com.firesoon.firesoondh.model.votype.data.access.DataAccessVO;
import com.firesoon.firesoondh.model.votype.data.access.DataSourceAccessVO;
import com.firesoon.firesoondh.services.data.access.DataSelectionService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.PageSerializable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 数据源选择以及同步表选择
 * @author: Yz
 * @date: 2020/6/16
 */
@Api(tags = "数据接入")
@RestController
@RequestMapping("/dataAccess/selection")
public class DataSelectionController extends BaseController {

    @Autowired
    private DataSelectionService dataSelectionService;


    @ApiOperation("数据源列表")
    @PostMapping("/dataSourceList")
    public Res<List<DataSourceAccessVO>> dataSourceList() {
        return Res.succ(dataSelectionService.dataSourceList());
    }


    @ApiOperation("数据接入列表")
    @PostMapping("/list")
    public Res<PageSerializable<DataAccessVO>> dataAccessList(@RequestBody BaseListSearchDTO baseListSearchDTO) {
        List<DataAccessVO> res = dataSelectionService.dataAccessTreeList(baseListSearchDTO);
        return Res.succ(new PageInfo<>(res));
    }


    @ApiOperation("新增数据接入")
    @PostMapping("/add")
    public Res<String> add(@RequestBody DataSelectionOperationReqDTO dataSelectionOperationReqDTO) {
        String id = dataSelectionService.dataSelectionAdd(dataSelectionOperationReqDTO);
        return Res.succ(id);
    }

    @ApiOperation("更新数据接入")
    @PostMapping("/update")
    public Res<Integer> update(@RequestBody DataSelectionOperationReqDTO dataSelectionOperationReqDTO) {
        dataSelectionService.dataSelectionUpdate(dataSelectionOperationReqDTO);
        return Res.succ();
    }

    @ApiOperation("删除数据接入")
    @PostMapping("/delete/{dataAccessId}")
    public Res<Integer> delete(@ApiParam("数据接入id") @PathVariable String dataAccessId) {
        dataSelectionService.dataSelectionDelete(dataAccessId);
        return Res.succ();
    }


}
