package com.aisser.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.aisser.maker.generator.JarGenerator;
import com.aisser.maker.generator.ScriptGenerator;
import com.aisser.maker.generator.file.DynamicFileGenerator;
import com.aisser.maker.meta.Meta;
import com.aisser.maker.meta.MetaManger;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/27-15:55
 */
public class MainGenerator {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {
        // 1. 获取json文件中的Meta信息
        Meta meta = MetaManger.getMetaInstance();

        // 2. 指定输出路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();

        // 3. 读取resource目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        // 4. 获取java包的基础路径
        String outputBasePackage = meta.getBasePackage();
        String outputBasePackagePath = StrUtil.join("/",StrUtil.split(outputBasePackage,"."));
        String outputBaseJavaPackagePath = outputPath + File.separator + StrUtil.join(File.separator,"src/main/java".split("/")) + File.separator + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;

        // 生成指定文件信息
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/model/DataModel.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/model/DataModel.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // ConfigCommand.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/cli/command/ConfigCommand.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/cli/command/ConfigCommand.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // GenerateCommand.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/cli/command/GenerateCommand.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/cli/command/GenerateCommand.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // ListCommand.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/cli/command/ListCommand.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/cli/command/ListCommand.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // CommandExecutor.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/cli/CommandExecutor.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/cli/CommandExecutor.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // DynamicGenerator.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/generator/DynamicGenerator.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/generator/DynamicGenerator.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // MainGenerator.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/generator/MainGenerator.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/generator/MainGenerator.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // StaticGenerator.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/generator/StaticGenerator.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/generator/StaticGenerator.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // Main.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/java/Main.java.ftl".split("/"));
        outputFilePath = outputBaseJavaPackagePath + StrUtil.join(File.separator,"/Main.java".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        // pom.java
        inputFilePath = inputResourcePath + StrUtil.join(File.separator,"templates/pom.xml.ftl".split("/"));
        outputFilePath = outputPath + StrUtil.join(File.separator,"/pom.xml".split("/"));
        DynamicFileGenerator.doGenerate(inputFilePath,outputFilePath,meta);

        JarGenerator.doGenerator("C:\\D_commonFiles\\java_project\\CodeGenerator\\generator-maker\\generated\\acm-template-pro-generator");

        String jarName = String.format("%s-%s-jar-with-dependencies.jar",meta.getName(),meta.getVersion());
        String jarPath = "target/" + jarName;
        ScriptGenerator.doGenerator(outputPath+"/generator.bat",jarPath);
    }
}
