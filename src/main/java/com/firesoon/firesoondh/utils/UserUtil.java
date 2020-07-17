package com.firesoon.firesoondh.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @description: 用户相关接口
 * @author: Yz
 * @date: 2020/6/18
 */

public class UserUtil {


    public static String getSessionId() {

        //获取request
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert servletRequestAttributes != null;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        Cookie[] cookies = request.getCookies();
        String sessionId = "";
        //遍历cookie获取用户登录后的sessionId
        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), "sessionId")) {
                sessionId = cookie.getValue();
                break;
            }
        }
        return sessionId;
    }

}
