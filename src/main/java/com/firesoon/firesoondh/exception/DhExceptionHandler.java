package com.firesoon.firesoondh.exception;

import com.firesoon.firesoondh.model.Res;
import com.firesoon.firesoondh.model.enumtype.ResErro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 全局异常处理
 * @author: Yz
 * @date: 2020/6/12
 */
@Slf4j
@RestControllerAdvice
public class DhExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Res<String> exceptionHandler(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        //后续对于业务异常的处理
        if (e instanceof BusinessException) {
            return Res.fail(((BusinessException) e).getResErro(), null);
        }
        log.error("", e);
        return Res.fail(ResErro.ERRO_500, e.getMessage());

    }
}
