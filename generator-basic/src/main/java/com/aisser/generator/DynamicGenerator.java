package com.aisser.generator;

import com.aisser.model.MainTemplateConfig;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-20:49
 */
public class DynamicGenerator {
    public static void main(String[] args) throws IOException, TemplateException {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputPath = projectPath + File.separator + "src/main/java/com/aisser/MainTemplate.java";
        MainTemplateConfig data = new MainTemplateConfig();
        data.setLoop(true);
        data.setOutputText("sum is ");
        data.setAuthor("AissEr");
        doGenerator(inputPath,outputPath,data);
    }

    public static void doGenerator(String inputPath, String outputPath, Object data) throws IOException, TemplateException {
        // 1. 配置基本信息，指定要替换的文件夹
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("utf-8");

        // 2. 获取对应的模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        // 3. 生成替换后的文件
        Writer out = new FileWriter(outputPath);
        template.process(data,out);

        out.close();
    }
}
