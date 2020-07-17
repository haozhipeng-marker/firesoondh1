package com.firesoon.firesoondh.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 缓存配置
 * @author: Yz
 * @date: 2020/6/12
 */
@Component
public class CacheConts {

    /**
     * 缓存项最大数量
     */

    public static long GUAVA_CACHE_SIZE;
    /**
     * 缓存时间：小时
     */

    public static long GUAVA_CACHE_HOURS;

    @Value("${cacheSize}")
    public void setGuavaCacheSize(long guavaCacheSize) {
        GUAVA_CACHE_SIZE = guavaCacheSize;
    }

    @Value("${cacheHours}")
    public void setGuavaCacheHours(long guavaCacheHours) {
        GUAVA_CACHE_HOURS = guavaCacheHours;
    }
}
