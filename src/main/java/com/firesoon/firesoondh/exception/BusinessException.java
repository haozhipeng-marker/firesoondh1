package com.firesoon.firesoondh.exception;

import com.firesoon.firesoondh.model.enumtype.ResErro;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 自定义业务异常
 * @author: Yz
 * @date: 2020/6/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BusinessException extends RuntimeException {

    private ResErro resErro;

    public BusinessException() {
        super();
    }

    public BusinessException(ResErro resErro) {
        super(resErro.getMsg());
        this.resErro = resErro;
    }
}
