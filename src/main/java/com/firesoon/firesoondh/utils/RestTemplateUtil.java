package com.firesoon.firesoondh.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: restTemplate增强
 * @author: Yz
 * @date: 2020/6/4
 */
@Component
public class RestTemplateUtil extends RestTemplate {
    @Autowired
    private RestTemplate restTemplate;

    public <T> ResponseEntity<T> exchangeGetOrPost(String url, HttpMethod httpMethod, Map<String, Object> cookies, MediaType mediaType, Map<String, Object> params, Class<T> tClass) {
        HttpHeaders headers = new HttpHeaders();
        //添加cookie
        if (!ObjectUtils.isEmpty(cookies)) {
            List<String> cookiesTemps = new ArrayList<>();
            for (Map.Entry<String, Object> entry : cookies.entrySet()) {
                cookiesTemps.add(entry.getKey() + "=" + entry.getValue());
            }
            headers.put(HttpHeaders.COOKIE, cookiesTemps);
        }

        headers.setContentType(mediaType);
        //get添加链接上的参数
        if (httpMethod.compareTo(HttpMethod.GET) == 0) {
            if (url.contains("?")) {
                url = url.concat("&");
            } else {
                url = url.concat("?");
            }
            if (!ObjectUtils.isEmpty(params)) {
                StringBuilder urlBuilder = new StringBuilder(url);
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    urlBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                }
                url = urlBuilder.toString();
            }
        }
        //添加body参数
        MultiValueMap<String, Object> requestMap = new LinkedMultiValueMap<String, Object>();
        if (!ObjectUtils.isEmpty(params)) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                requestMap.add(entry.getKey(), entry.getValue());
            }
        }


        HttpEntity<?> requestEntity;
        if (mediaType.includes(MediaType.APPLICATION_JSON)) {
            requestEntity = new HttpEntity<>(JSONObject.toJSON(requestMap), headers);
        } else {
            requestEntity = new HttpEntity<>(requestMap, headers);
        }


        return restTemplate.exchange(url, httpMethod, requestEntity, tClass);
    }
}
