package com.aisser.maker.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
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
    public void testMakeTemplateByJsonFile(){
        String templateMakerJsonPath = "templateMaker.json";
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(FileUtil.readUtf8String(templateMakerJsonPath), TemplateMakerConfig.class);
        Long id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }

    @Test
    public void makeSpringBootTemplate(){
        String rootPath = "examples/generator-web-backend/";
        String configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker.json");
        TemplateMakerConfig templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        Long id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker1.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker2.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker3.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker4.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker5.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker6.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);

        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker7.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);
        configStr = ResourceUtil.readUtf8Str(rootPath + "templateMaker8.json");
        templateMakerConfig = JSONUtil.toBean(configStr,TemplateMakerConfig.class);
        id = TemplateMaker.makeTemplate(templateMakerConfig);
        System.out.println(id);
    }

}