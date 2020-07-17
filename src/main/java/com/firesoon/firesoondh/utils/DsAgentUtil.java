package com.firesoon.firesoondh.utils;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.model.Res;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: dolphinscheduler 代理通用工具
 * @author: Yz
 * @date: 2020/6/4
 */
@Component
public class DsAgentUtil {
    private static String DS_RES_CODE = "code";
    private static String DS_RES_DATA = "data";
    private static String DS_RES_MSG = "msg";
    private static String DS_RES_TOTAL_LIST = "totalList";


    public Map<String, Object> getSessionId() {
        Map<String, Object> temp = new HashMap<>(1);
        temp.put("sessionId", UserUtil.getSessionId());
        return temp;
    }


    public <T> Res<T> responseEntityDispose(ResponseEntity<JSONObject> responseEntity, Class<T> tClass) {
        Res<T> res = new Res<>();
        JSONObject resContent = responseEntity.getBody();

        //内容体是否为空
        if (ObjectUtils.isEmpty(resContent)) {
            return res.setCode(responseEntity.getStatusCodeValue());
        }
        //http 是否200
        if (responseEntity.getStatusCodeValue() != HttpStatus.OK.value()
                && responseEntity.getStatusCodeValue() != HttpStatus.CREATED.value()) {
            return res.setCode(responseEntity.getStatusCodeValue()).setMsg(resContent.getString("message"));
        }
        //数据体中的code是否为 0
        if (resContent.getInteger(DS_RES_CODE) == 0) {
            if (StringUtils.isNotBlank(resContent.getString(DS_RES_DATA)) && resContent.getString(DS_RES_DATA).contains(DS_RES_TOTAL_LIST)) {
                return new Res<T>().setData(resContent.getJSONObject(DS_RES_DATA).getObject(DS_RES_TOTAL_LIST, tClass));
            }
            return new Res<T>().setData(resContent.getObject(DS_RES_DATA, tClass));
        }

        return new Res<T>(resContent.getInteger(DS_RES_CODE), resContent.getString(DS_RES_MSG), resContent.getObject(DS_RES_DATA, tClass));

    }


}
