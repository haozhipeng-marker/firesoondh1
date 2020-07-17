package com.firesoon.firesoondh.model;

import com.firesoon.firesoondh.model.enumtype.ResErro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Yuan
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class Res<T> {
    private Integer code;
    private String msg;
    private T data;

    public Res() {
        this.setCode(200);
    }

    public static <T> Res<T> succ() {
        return new Res<T>().setMsg("请求成功");
    }

    public static <T> Res<T> succ(T data) {
        return new Res<>(200, "请求成功", data);
    }

    public static <T> Res<T> fail(ResErro resErro, T t) {
        return new Res<T>().setCode(resErro.getCode()).setMsg(resErro.getMsg());
    }
}
