package com.aisser.maker.template;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.aisser.maker.meta.Meta;
import com.aisser.maker.meta.emuns.FileGenerateTypeEnum;
import com.aisser.maker.meta.emuns.FileTypeEnum;
import com.aisser.maker.template.enums.FileFilterRangeEnum;
import com.aisser.maker.template.enums.FileFilterRuleEnum;
import com.aisser.maker.template.model.FileFilterConfig;
import com.aisser.maker.template.model.TemplateMakerFileConfig;
import com.aisser.maker.template.model.TemplateMakerModelConfig;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/2-17:36
 */
public class TemplateMaker {

    private static Long makeTemplate(Meta newMeta ,
                                     String originProjectPath,
                                     TemplateMakerFileConfig templateMakerFileConfig,
                                     TemplateMakerModelConfig templateMakerModelConfig,
                                     Long id){
        if(id == null){
            id = IdUtil.getSnowflakeNextId();
        }

        // 复制目录
        String projectPath = System.getProperty("user.dir");
        String tempDirPath = projectPath + File.separator + ".temp";
        String templatePath = tempDirPath + File.separator + id;
        if(!FileUtil.exist(templatePath)){
            FileUtil.mkdir(templatePath);
            FileUtil.copy(originProjectPath,templatePath,true);
        }

        // 一、输入信息
        // 1.输入项目基本信息
        String sourceRootPath = templatePath + File.separator + FileUtil.getLastPathEle(Paths.get(originProjectPath)).toString();
        sourceRootPath = sourceRootPath.replaceAll("\\\\","/");
        List<TemplateMakerFileConfig.FileInfoConfig> fileInfoConfigList = templateMakerFileConfig.getFiles();

        // 二、生成文件模板
        List<Meta.FileConfig.FileInfo> newFileInfoList = new ArrayList<>();
        for (TemplateMakerFileConfig.FileInfoConfig fileInfoConfig : fileInfoConfigList) {
            String inputFilePath = fileInfoConfig.getPath();

            // 如果是相对路径，将其转换为绝对路径
            if(!inputFilePath.startsWith(sourceRootPath)){
                inputFilePath = sourceRootPath + File.separator + inputFilePath;
            }

            List<File> files = FileFilter.doFilter(inputFilePath, fileInfoConfig.getFilterConfigs());
            for (File file : files) {
                Meta.FileConfig.FileInfo fileInfo = makeFileTemplate(templateMakerModelConfig, sourceRootPath, file);
                newFileInfoList.add(fileInfo);
            }
        }

        // 如果是文件组
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = templateMakerFileConfig.getFileGroupConfig();
        if(fileGroupConfig != null){
            String condition = fileGroupConfig.getCondition();
            String groupKey = fileGroupConfig.getGroupKey();
            String groupName = fileGroupConfig.getGroupName();

            // 新增分量配置
            Meta.FileConfig.FileInfo groupFileInfo = new Meta.FileConfig.FileInfo();
            groupFileInfo.setType(FileTypeEnum.GROUP.getValue());
            groupFileInfo.setCondition(condition);
            groupFileInfo.setGroupKey(groupKey);
            groupFileInfo.setGroupName(groupName);
            // 文件全放到一个分组内
            groupFileInfo.setFiles(newFileInfoList);
            newFileInfoList = new ArrayList<>();
            newFileInfoList.add(groupFileInfo);
        }

        // 处理模型信息
        List<TemplateMakerModelConfig.ModelInfoConfig> models = templateMakerModelConfig.getModels();
        // 转换为配置接收的ModelInfo对象
        List<Meta.ModelConfig.ModelInfo> inputModelInfoList = models.stream().map(modelInfoConfig -> {
            Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
            BeanUtil.copyProperties(modelInfoConfig, modelInfo);
            return modelInfo;
        }).collect(Collectors.toList());

        // 本次新增的模型配置列表
        List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>();
        // 如果是模型组
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        if(modelGroupConfig != null){
            String condition = modelGroupConfig.getCondition();
            String groupKey = modelGroupConfig.getGroupKey();
            String groupName = modelGroupConfig.getGroupName();
            Meta.ModelConfig.ModelInfo groupModelInfo = new Meta.ModelConfig.ModelInfo();
            groupModelInfo.setGroupKey(groupKey);
            groupModelInfo.setGroupName(groupName);
            groupModelInfo.setCondition(condition);

            // 模型放在一个分组内
            groupModelInfo.setModels(inputModelInfoList);
            newModelInfoList.add(groupModelInfo);
        }else {
            newModelInfoList.addAll(inputModelInfoList);
        }

        // 三、生成配置文件
        String metaOutputPath = sourceRootPath + File.separator + "meta.json";
        if(FileUtil.exist(metaOutputPath)){
            Meta oldMeta = JSONUtil.toBean(FileUtil.readUtf8String(metaOutputPath),Meta.class);
            BeanUtil.copyProperties(newMeta, oldMeta, CopyOptions.create().ignoreNullValue());
            newMeta = oldMeta;

            // 追加配置参数
            List<Meta.FileConfig.FileInfo> fileInfoList = newMeta.getFileConfig().getFiles();
            fileInfoList.addAll(newFileInfoList);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = newMeta.getModelConfig().getModels();
            modelInfoList.addAll(newModelInfoList);

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
            fileInfoList.addAll(newFileInfoList);

            Meta.ModelConfig modelConfig = new Meta.ModelConfig();
            newMeta.setModelConfig(modelConfig);
            List<Meta.ModelConfig.ModelInfo> modelInfoList = new ArrayList<>();
            modelConfig.setModels(modelInfoList);
            modelInfoList.addAll(newModelInfoList);
        }
        // 2. 输出元信息文件
        FileUtil.writeUtf8String(JSONUtil.toJsonPrettyStr(newMeta),metaOutputPath);

        return id;
    }

    private static List<Meta.ModelConfig.ModelInfo> distanceModel(List<Meta.ModelConfig.ModelInfo> modelInfoList) {
        // 策略：同分组内模型 merge，不同分组保留

        // 1. 有分组的，以组为单位划分
        Map<String, List<Meta.ModelConfig.ModelInfo>> groupKeyModelInfoListMap = modelInfoList
                .stream()
                .filter(modelInfo -> StrUtil.isNotBlank(modelInfo.getGroupKey()))
                .collect(
                        Collectors.groupingBy(Meta.ModelConfig.ModelInfo::getGroupKey)
                );


        // 2. 同组内的模型配置合并
        // 保存每个组对应的合并后的对象 map
        Map<String, Meta.ModelConfig.ModelInfo> groupKeyMergedModelInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.ModelConfig.ModelInfo>> entry : groupKeyModelInfoListMap.entrySet()) {
            List<Meta.ModelConfig.ModelInfo> tempModelInfoList = entry.getValue();
            List<Meta.ModelConfig.ModelInfo> newModelInfoList = new ArrayList<>(tempModelInfoList.stream()
                    .flatMap(modelInfo -> modelInfo.getModels().stream())
                    .collect(
                            Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                    ).values());

            // 使用新的 group 配置
            Meta.ModelConfig.ModelInfo newModelInfo = CollUtil.getLast(tempModelInfoList);
            newModelInfo.setModels(newModelInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedModelInfoMap.put(groupKey, newModelInfo);
        }

        // 3. 将模型分组添加到结果列表
        List<Meta.ModelConfig.ModelInfo> resultList = new ArrayList<>(groupKeyMergedModelInfoMap.values());

        // 4. 将未分组的模型添加到结果列表
        List<Meta.ModelConfig.ModelInfo> noGroupModelInfoList = modelInfoList.stream().filter(modelInfo -> StrUtil.isBlank(modelInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupModelInfoList.stream()
                .collect(
                        Collectors.toMap(Meta.ModelConfig.ModelInfo::getFieldName, o -> o, (e, r) -> r)
                ).values()));
        return resultList;

    }

    private static List<Meta.FileConfig.FileInfo> distinctFiles(List<Meta.FileConfig.FileInfo> fileInfoList) {
        // 1.有分组的，以组为单位划分
        Map<String, List<Meta.FileConfig.FileInfo>> groupKeyFileInfoListMap = fileInfoList
                .stream()
                .filter(fileInfo -> StrUtil.isNotBlank(fileInfo.getGroupKey()))
                .collect(Collectors.groupingBy(Meta.FileConfig.FileInfo::getGroupKey));

        // 2. 同组内的文件配置合并
        // 保存每个组对应的合并后的map
        Map<String, Meta.FileConfig.FileInfo> groupKeyMergedFileInfoMap = new HashMap<>();
        for (Map.Entry<String, List<Meta.FileConfig.FileInfo>> entry : groupKeyFileInfoListMap.entrySet()) {
            List<Meta.FileConfig.FileInfo> tempFileInfoList = entry.getValue();
            List<Meta.FileConfig.FileInfo> newFIleInfoList = new ArrayList<>(tempFileInfoList.stream()
                    .flatMap(fileInfo -> fileInfo.getFiles().stream())
                    .collect(
                            Collectors.toMap(Meta.FileConfig.FileInfo::getInputPath, o -> o, (e,r ) -> r)
                    ).values());
            // 使用新的group配置
            Meta.FileConfig.FileInfo newFileInfo = CollUtil.getLast(tempFileInfoList);
            newFileInfo.setFiles(newFIleInfoList);
            String groupKey = entry.getKey();
            groupKeyMergedFileInfoMap.put(groupKey,newFileInfo);
        }

        // 3. 将文件分组添加到结果列表
        List<Meta.FileConfig.FileInfo> resultList = new ArrayList<>(groupKeyMergedFileInfoMap.values());

        // 4.将未分组的文件添加到结果列表
        List<Meta.FileConfig.FileInfo> noGroupFileInfoList = fileInfoList.stream()
                .filter(fileInfo -> StrUtil.isBlank(fileInfo.getGroupKey()))
                .collect(Collectors.toList());
        resultList.addAll(new ArrayList<>(noGroupFileInfoList.stream()
                .collect(Collectors.toMap(
                        Meta.FileConfig.FileInfo::getInputPath, o -> o, (e,r) -> r
                )).values()));

        return resultList;
    }

    private static Meta.FileConfig.FileInfo makeFileTemplate(TemplateMakerModelConfig templateMakerModelConfig, String sourceRootPath, File inputFile){
        // 要挖坑的文件绝对路径（用于制作模板）
        // 注意win系统需要对路径进行转义
        String fileInputAbsolutePath = inputFile.getAbsolutePath().replace("\\\\","/");
        String fileOutputAbsolutePath = fileInputAbsolutePath + ".ftl";

        // 文件输入输出相对路径（用于生成配置）
        String fileInputPath = fileInputAbsolutePath.replace(sourceRootPath+"/","");
        String fileOutputPath = fileInputPath + ".ftl";

        // 使用字符串替换，生成模板文件
        String fileContent;
        // 如果已有模板文件，说明是第一次制作，则在模板基础上再挖坑
        if(FileUtil.exist(fileOutputAbsolutePath)){
            fileContent = FileUtil.readUtf8String(fileOutputAbsolutePath);
        }
        else {
            fileContent = FileUtil.readUtf8String(fileInputAbsolutePath);
        }

        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = templateMakerModelConfig.getModelGroupConfig();
        String newFileContent = fileContent;
        String replacement;

        for (TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig : templateMakerModelConfig.getModels()) {
            if(modelGroupConfig == null){
                replacement = String.format("${%s}",modelInfoConfig.getFieldName());
            }
            else {
                String groupKey = modelGroupConfig.getGroupKey();
                replacement = String.format("${%s.%s}",groupKey,modelInfoConfig.getFieldName());
            }
            newFileContent = StrUtil.replace(newFileContent, modelInfoConfig.getReplaceText(), replacement);
        }

        // 文件配置信息
        Meta.FileConfig.FileInfo fileInfo = new Meta.FileConfig.FileInfo();
        fileInfo.setInputPath(fileInputPath);
        fileInfo.setOutputPath(fileOutputPath);
        fileInfo.setType(FileTypeEnum.FILE.getValue());

        // 和源文件一致，没有挖坑，则为静态生成
        if(newFileContent.equals(fileContent)){
            // 输出路径 = 输入路径
            fileInfo.setOutputPath(fileInputPath);
            fileInfo.setGenerateType(FileGenerateTypeEnum.STATIC.getValue());
        }else {
            fileInfo.setGenerateType(FileGenerateTypeEnum.DYNAMIC.getValue());
            FileUtil.writeUtf8String(newFileContent, fileOutputAbsolutePath);
        }
        return fileInfo;
    }

    public static void main(String[] args) {
        Meta meta = new Meta();
        meta.setName("springboot-init");
        meta.setDescription("SpringBoot初始化项目通用模板");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "generator-demo-project/springboot-init";


        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig.setReplaceText("BaseResponse");
        modelInfoConfig.setFieldName("className");
        modelInfoConfig.setType("String");
        templateMakerModelConfig.setModels(Arrays.asList(modelInfoConfig));

//        // 模型组配置
//        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
//        modelGroupConfig.setGroupKey("mysql");
//        modelGroupConfig.setGroupName("数据库配置");
//        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);
//
//        // 模型配置
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig1.setFieldName("url");
//        modelInfoConfig1.setType("String");
//        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
//        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");
//
//        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
//        modelInfoConfig2.setFieldName("username");
//        modelInfoConfig2.setType("String");
//        modelInfoConfig2.setDefaultValue("root");
//        modelInfoConfig2.setReplaceText("root");
//
//        List<TemplateMakerModelConfig.ModelInfoConfig> list = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
//        templateMakerModelConfig.setModels(list);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig1 = new TemplateMakerFileConfig.FileInfoConfig();
        List<FileFilterConfig> fileFilterConfigList = new ArrayList<>();
        fileFilterConfigList.add(FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_NAME.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("Base")
                .build());
        fileInfoConfig1.setPath("src/main/java/com/aisser/springbootinit/common");
        fileInfoConfig1.setFilterConfigs(fileFilterConfigList);

//        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
//        fileInfoConfig2.setPath("src/main/resources/application.yml");

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig1));

        Meta.ModelConfig.ModelInfo modelInfo = new Meta.ModelConfig.ModelInfo();
        modelInfo.setFieldName("className");
        modelInfo.setType("String");

        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);

        Long id = makeTemplate(meta,originProjectPath,templateMakerFileConfig,templateMakerModelConfig,1764276837696102400L);
        System.out.println(id);

    }

}
