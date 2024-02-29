package ${basePackage}.generator;

import ${basePackage}.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class MainGenerator {

<#macro generateFile fileInfo indent>
    ${indent}inputPath = new File(inputRootPath,"${fileInfo.inputPath}").getAbsolutePath();
    ${indent}outputPath = new File(outputRootPath, "${fileInfo.outputPath}").getAbsolutePath();
    <#if fileInfo.generateType == "static">
    ${indent}StaticGenerator.copyFilesByHutool(inputPath,outputPath);
    <#else>
    ${indent}DynamicGenerator.doGenerate(inputPath, outputPath, model);
    </#if>
</#macro>

    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(DataModel model) throws TemplateException, IOException {
        String inputRootPath = "${fileConfig.inputRootPath}";
        String outputRootPath = "${fileConfig.outputRootPath}";

        String inputPath;
        String outputPath;

    <#list modelConfig.models as modelInfo>
        <#-- 有分组 -->
        <#if modelInfo.groupKey??>
            <#list modelInfo.models as subModelInfo>
                ${subModelInfo.type} ${subModelInfo.fieldName} = model.${modelInfo.groupKey}.${subModelInfo.fieldName};
            </#list>
        <#else>
            ${modelInfo.type} ${modelInfo.fieldName} = model.${modelInfo.fieldName};
        </#if>
    </#list>

<#list fileConfig.files as fileInfo>
    <#if fileInfo.groupKey??>
        <#if fileInfo.condition??>
            if(${fileInfo.condition}){
                <#list fileInfo.files as fileInfo>
                <@generateFile fileInfo=fileInfo indent="        "></@generateFile>
                </#list>
            }
        <#else >
        <@generateFile fileInfo=fileInfo indent=""></@generateFile>
        </#if>
    <#else>
        <#if fileInfo.condition??>
            if(${fileInfo.condition}){
            <@generateFile fileInfo=fileInfo indent="      "></@generateFile>
            }
        <#else >
        <@generateFile fileInfo=fileInfo indent="       "></@generateFile>
        </#if>
    </#if>
</#list>
    }
}