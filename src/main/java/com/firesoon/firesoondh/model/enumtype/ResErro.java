package com.firesoon.firesoondh.model.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;


/**
 * 返回错误定义
 *
 * @author Yuan
 */
@Getter
@AllArgsConstructor
public enum ResErro {
    /**
     *
     */
    ERRO_500(500, "服务器异常"),
    ERRO_511(511, "请求参数缺失"),
    ERRO_512(512, "未登录"),
    ERRO_513(513, "代理ds接口请求错误"),
    ERRO_514(514, "数据内容格式错误"),
    ERRO_515(515, "数据范围异常"),
    ERRO_516(516, "暂无该条数据记录"),
    ERRO_517(517, "名称重复"),
    ERRO_518(518, "内有数据使用中"),
    ERRO_519(519, "工作流已经上线请先下线"),
    ERRO_520(520, "该数据源已存在");

    private final Integer code;
    private final String msg;

}
