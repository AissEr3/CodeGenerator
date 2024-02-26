package com.aisser.maker.generator.file;

import cn.hutool.core.io.FileUtil;
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
public class DynamicFileGenerator {

    /**
     * 生成文件
     *
     * @param inputPath  生成文件路径
     * @param outputPath 输出文件路径
     * @param data       具体的数据
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Object data) throws IOException, TemplateException {
        // 1. 配置基本信息，指定要替换的文件夹
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_32);
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);
        configuration.setDefaultEncoding("utf-8");

        // 2. 获取对应的模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        // 文件不存在，创建文件和目录
        if(!FileUtil.exist(outputPath)){
            FileUtil.touch(outputPath);
        }

        // 3. 生成替换后的文件
        Writer out = new FileWriter(outputPath);
        template.process(data,out);

        out.close();
    }
}
