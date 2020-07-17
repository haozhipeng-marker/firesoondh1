package com.firesoon.firesoondh.utils;


import com.firesoon.firesoondh.constant.TemplateConts;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.Map;

/**
 * @description: 文件生成
 * @author: Yz
 * @date: 2020/3/5
 */
public class FreemarkerUtil {

    /**
     * @param templateFileName 模板名称
     * @param datas            数据
     * @param newFileName      生成新文件的名称
     */
    public static void generateFile(String templateFileName, Map<String, Object> datas, String newFileName) {
        try {
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_0);

            // 设置默认编码
            configuration.setDefaultEncoding("UTF-8");

            // 设置模板所在文件夹
            configuration.setClassForTemplateLoading(FreemarkerUtil.class, TemplateConts.TEMPLATE_PATH);
            // 生成模板对象
            Template template = configuration.getTemplate(templateFileName);
            // 将datas写入模板并返回
            StringWriter stringWriter = new StringWriter();
            template.process(datas, stringWriter);
            stringWriter.flush();
            String temp = stringWriter.getBuffer().toString();
            new File(newFileName).mkdirs();
            if (new File(newFileName).exists()) {
                boolean delete = new File(newFileName).delete();
            }

            FileOutputStream newFile = new FileOutputStream(newFileName);
            newFile.write(temp.getBytes());
            newFile.close();


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
