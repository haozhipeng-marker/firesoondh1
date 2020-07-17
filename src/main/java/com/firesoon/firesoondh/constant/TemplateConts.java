package com.firesoon.firesoondh.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description: 模板生成配置
 * @author: Yz
 * @date: 2020/6/12
 */
@Component
public class TemplateConts {
    /**
     * 模板文件夹
     */
    public static String TEMPLATE_PATH;
    /**
     * 生成文件目录
     */
    public static String GENERATE_FILE_PATH;

    public static String SPARK_CONF_TEMPLATE_NAME = "conf.template";

    @Value("${templatePath}")
    public void setTemplatePath(String templatePath) {
        TEMPLATE_PATH = templatePath;
    }

    @Value("${generateFilePath}")
    public void setGenerateFilePath(String generateFilePath) {
        GENERATE_FILE_PATH = generateFilePath;
    }
}
