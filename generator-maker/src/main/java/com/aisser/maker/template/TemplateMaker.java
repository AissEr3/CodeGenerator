package com.aisser.maker.template;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aisser.maker.meta.Meta;
import com.aisser.maker.meta.MetaManager;
import com.aisser.maker.meta.emuns.FileGenerateTypeEnum;
import com.aisser.maker.meta.emuns.FileTypeEnum;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/2-17:36
 */
public class TemplateMaker {

    private static Long makeTemplate(Meta newMeta ,String originProjectPath, String inputFIlePath, Meta.ModelConfig.ModelInfo modelInfo, String searchStr, Long id){
        if(id == null){
            id = IdUtil.getSnowflakeNextId();
        }

        // 复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if(!FileUtil.exist(templatePath)){
            FileUtil.mkdir(templatePath);
        }
        FileUtil.copy(originProjectPath,templatePath,true);

        // 一、输入信息
        // 1.输入项目基本信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        String fileInputPath = inputFIlePath;
        String fileOutputPath = fileInputPath + ".ftl";

        // 二、使用字符串替换，生成模板文件
        String fileInputAbsolutePath = sourceRootPath + File.separator + fileInputPath;
        String fileOutputAbsolutePath = sourceRootPath + File.separator + fileOutputPath;
        String fileContent = null;
        // 判断是否已有模板文件
        if(FileUtil.exist(fileInputPath)){
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        }
        else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }
        String replacement = String.format("${%s}",modelInfo.getFieldName());
        String newFileContent = StrUtil.replace(fileContent,searchStr,replacement);

        //输出模板文件
        FileUtil.writeUtf8String(newFileContent,fileOutputAbsolutePath);

        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());
        fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        if(FileUtil.exist(metaOutputPath)){
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath),Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

            // 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.add(fileInfo);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.add(modelInfo);

            // 配置去重
            newMeta.getFileConfig().setFiles(distinctFiles(fileInfoList));
            newMeta.getModelConfig().setModels(distanceModel(modelInfoList));
        }
        else {
            // 1.构造配置参数
            Meta.FileConfig fileConfig = new Meta.FileConfig();
            newMeta.setFileConfig(fileConfig);
            fileConfig.setSourceRootPath(sourceRootPath);
            List<Meta.FileConfig.FileInfo> fileInfoList = new ArrayList<>();
            fileConfig.setFiles(fileInfoList);
            fileInfoList.add(fileInfo);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.add(modelInfo);
        }
        // 2. 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta),metaOutputPath);

        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> distanceModel(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        return new ArrayList<>(
                modelInfoList.stream()
                        .collect(Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e,r) -> r)
                        ).values()
        );
    }

    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        return new ArrayList<>(
                fileInfoList.stream()
                        .collect(
                                Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e,r) -> r)
                        ).values()
        );
    }

    public static void main(String[] args) {
        Meta meta = new Meta();
        meta.setName("acm-template-pro-generator");
        meta.setDescription("ACM 示例模板生成器");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "generator-demo-project/acm-template";
        String inputFilePath = "src/com/aisser/acm/MainTemplate.java";

//        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
//        modelInfo.setFieldName("outputText");
//        modelInfo.setType("String");
//        modelInfo.setDefaultValue("sum =");
//
//        String searchStr = "sum =";

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        String searchStr = "MainTemplate";

        Long id = makeTemplate(meta,originProjectPath,inputFilePath,modelInfo,searchStr,null);
        System.out.println(id);

    }

}
