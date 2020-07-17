package com.firesoon.firesoondh.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yz
 * DTO DO转换 使用fastjson相比反射 速度快性能好
 */
@Log
public class ConvertUtil {

    /**
     * 对象实体之间的转换
     *
     * @param object 入参对象
     * @param clazz  需要转换成的数据实体字节码类型
     * @param <T>    需要转换成的实体泛型
     * @return
     */
    public static <T> T convert(Object object, Class<T> clazz) {
        if (null == object) {
            return null;
        }
        try {
            return JSONObject.parseObject(JSONObject.toJSONString(object), clazz);
        } catch (Exception e) {
            log.severe("ConvertTool.convert,[param]:" + "object = [" + object + "], clazz = [" + clazz
                    + "], excepton=[" + e + "]");
            throw e;
        }
    }

    /**
     * 列表数据转换
     *
     * @param list  入参列表
     * @param clazz 需要转换成的数据实体字节码类型
     * @param <T>   需要转换成的实体泛型
     * @return
     */
    public static <T> List<T> convert(List<?> list, Class<T> clazz) {
        if (null == list) {
            return new ArrayList<>();
        }
        try {
            return JSONObject.parseArray(JSONObject.toJSONString(list), clazz);
        } catch (Exception e) {
            log.info("ConvertTool.convert,[param]:" + "list = [" + list + "], clazz = [" + clazz
                    + "], excepton=[" + e + "]");
            throw e;
        }
    }
}