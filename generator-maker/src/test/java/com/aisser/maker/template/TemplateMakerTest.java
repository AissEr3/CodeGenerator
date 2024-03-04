package com.aisser.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONUtil;
import com.aisser.maker.meta.Meta;
import com.aisser.maker.template.enums.FileFilterRangeEnum;
import com.aisser.maker.template.enums.FileFilterRuleEnum;
import com.aisser.maker.template.model.FileFilterConfig;
import com.aisser.maker.template.model.TemplateMakerFileConfig;
import com.aisser.maker.template.model.TemplateMakerModelConfig;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/4-15:43
 */
public class TemplateMakerTest {

    @Test
    public void testMakeTemplate(){
        Meta meta = new Meta();
        meta.setName("springboot-init");
        meta.setDescription("SpringBoot初始化项目通用模板");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "generator-demo-project/springboot-init";

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();

        // 模型组配置
        TemplateMakerModelConfig.ModelGroupConfig modelGroupConfig = new TemplateMakerModelConfig.ModelGroupConfig();
        modelGroupConfig.setGroupKey("mysql");
        modelGroupConfig.setGroupName("数据库配置");
        templateMakerModelConfig.setModelGroupConfig(modelGroupConfig);

        // 模型配置
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig1 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig1.setFieldName("url");
        modelInfoConfig1.setType("String");
        modelInfoConfig1.setDefaultValue("jdbc:mysql://localhost:3306/my_db");
        modelInfoConfig1.setReplaceText("jdbc:mysql://localhost:3306/my_db");

        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig2 = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig2.setFieldName("username");
        modelInfoConfig2.setType("String");
        modelInfoConfig2.setDefaultValue("root");
        modelInfoConfig2.setReplaceText("root");

        List<TemplateMakerModelConfig.ModelInfoConfig> list = Arrays.asList(modelInfoConfig1, modelInfoConfig2);
        templateMakerModelConfig.setModels(list);

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath("src/main/resources/application.yml");

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig2));

        // 分组配置
        TemplateMakerFileConfig.FileGroupConfig fileGroupConfig = new TemplateMakerFileConfig.FileGroupConfig();
        fileGroupConfig.setCondition("outputText");
        fileGroupConfig.setGroupKey("test");
        fileGroupConfig.setGroupName("测试分组");
        templateMakerFileConfig.setFileGroupConfig(fileGroupConfig);


        Long id = TemplateMaker.makeTemplate(meta,originProjectPath,templateMakerFileConfig,templateMakerModelConfig,1764276837696102400L);
        System.out.println(id);
    }

    @Test
    public void testMakeTemplate2(){
        Meta meta = new Meta();
        meta.setName("springboot-init");
        meta.setDescription("SpringBoot初始化项目通用模板");

        String projectPath = System.getProperty("user.dir");
        String originProjectPath = new File(projectPath).getParent() + File.separator + "generator-demo-project/springboot-init";

        TemplateMakerFileConfig.FileInfoConfig fileInfoConfig2 = new TemplateMakerFileConfig.FileInfoConfig();
        fileInfoConfig2.setPath("./");
        fileInfoConfig2.setFilterConfigs(Arrays.asList(FileFilterConfig.builder()
                .range(FileFilterRangeEnum.FILE_CONTENT.getValue())
                .rule(FileFilterRuleEnum.CONTAINS.getValue())
                .value("BaseResponse")
                .build()));

        TemplateMakerFileConfig templateMakerFileConfig = new TemplateMakerFileConfig();
        templateMakerFileConfig.setFiles(Arrays.asList(fileInfoConfig2));

        // 模型参数配置
        TemplateMakerModelConfig templateMakerModelConfig = new TemplateMakerModelConfig();
        TemplateMakerModelConfig.ModelInfoConfig modelInfoConfig = new TemplateMakerModelConfig.ModelInfoConfig();
        modelInfoConfig.setFieldName("className");
        modelInfoConfig.setReplaceText("BaseResponse");
        modelInfoConfig.setType("String");
        templateMakerModelConfig.setModels(Arrays.asList(modelInfoConfig));

        Long id = TemplateMaker.makeTemplate(meta,originProjectPath,templateMakerFileConfig,templateMakerModelConfig,1764276837696102400L);
        System.out.println(id);
    }

    @Test
    public void testMakeTemplateByJsonFile(){
        String templateMakerJsonPath = "templateMaker.json";
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(FileUtil.readUtf8String(templateMakerJsonPath), TemplateMakerConfig.class);
        Long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }

}