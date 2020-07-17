package com.firesoon.firesoondh.intercepor;

import com.alibaba.fastjson.JSONObject;
import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @description: 用户登录拦截器
 * @author: Yz
 * @date: 2020/6/12
 */
public class UserLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String sessionId = "";
        if (!ObjectUtils.allNotNull((Object) cookies)) {
            returnRes(response, Res.fail(ResErro.ERRO_512, null));
            return false;
        }


        for (Cookie cookie : cookies) {
            if (StringUtils.equals(cookie.getName(), "sessionId")) {
                sessionId = cookie.getValue();
                break;
            }
        }
        if (!StringUtils.isNotBlank(sessionId)) {
            returnRes(response, Res.fail(ResErro.ERRO_512, null));
            return false;
        }

        response.setHeader("Access-Control-Allow-Credentials", "true");

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
    }

    private void returnRes(HttpServletResponse response, Res<?> res) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        try (PrintWriter writer = response.getWriter()) {
            writer.print(JSONObject.toJSONString(res));

        } catch (IOException e) {
            throw new RuntimeException("拦截器返回异常");
        }
    }


}
