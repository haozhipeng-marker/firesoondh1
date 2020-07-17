package com.firesoon.firesoondh.controller.data.access;

import com.firesoon.firesoondh.controller.BaseController;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.dotype.data.access.DataAccessConfDO;
import com.firesoon.firesoondh.model.dtotype.data.access.SynchronizationConfUpdateReqDTO;
import com.firesoon.firesoondh.model.votype.data.access.DataAccessConfVO;
import com.firesoon.firesoondh.services.data.access.SynchronizationConfService;
import com.firesoon.firesoondh.utils.ConvertUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 同步配置字段配置
 * @author: Yz
 * @date: 2020/6/16
 */
@Api(tags = "数据接入")
@RestController
@RequestMapping("/dataAccess/synConf")
public class SynchronizationConfController extends BaseController {

    @Autowired
    private SynchronizationConfService synchronizationConfService;


    @ApiOperation("同步配置详情")
    @PostMapping("/details/{dataAccessConfId}")
    public Res<DataAccessConfVO> details(@ApiParam("数据接入配置id") @PathVariable String dataAccessConfId) {
        DataAccessConfDO dataAccessConf = synchronizationConfService.dataAccessConfDetail(dataAccessConfId);
        return Res.succ(ConvertUtil.convert(dataAccessConf, DataAccessConfVO.class));
    }

    @ApiOperation("同步配置更新")
    @PostMapping("/update")
    public Res<Integer> update(@RequestBody SynchronizationConfUpdateReqDTO synchronizationConfUpdateReqDTO) {
        synchronizationConfService.synchronizationConfUpdate(synchronizationConfUpdateReqDTO);
        return Res.succ();
    }

    @ApiOperation("同步配置删除")
    @PostMapping("/delete/{dataAccessConfId}")
    public Res<Integer> delete(@ApiParam("数据接入配置id") @PathVariable String dataAccessConfId) {
        synchronizationConfService.synchronizationConfDelete(dataAccessConfId);
        return Res.succ();
    }

}
