package com.aisser.web.model.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.aisser.web.meta.Meta;
import com.aisser.web.model.entity.Generator;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/5-16:30
 */
@Data
public class GeneratorVO implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String description;

    /**
     * 基础包
     */
    private String basePackage;

    /**
     * 版本
     */
    private String version;

    /**
     * 作者
     */
    private String author;

    /**
     * 标签列表 ( json数组 )
     */
    private List<String> tags;

    /**
     * 图片
     */
    private String picture;

    /**
     * 文件配置 ( json字符串 )
     */
    private Meta.FileConfig fileConfig;

    /**
     * 模型配置 ( json字符串 )
     */
    private Meta.ModelConfig modelConfig;

    /**
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 状态
     */
    private Integer status;

    /**
     *  创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private UserVO userVO;

    private static final long serialVersionUID = -1356740592820679632L;

    /**
     * 包装类转换对象
     * @param generatorVO
     * @return
     */
    public static Generator voToObj(GeneratorVO generatorVO){
        if(generatorVO == null){
            return null;
        }
        Generator generator = new Generator();
        BeanUtil.copyProperties(generatorVO,generator);
        List<String> tagList = generatorVO.getTags();
        generator.setTags(JSONUtil.toJsonStr(tagList));
        Meta.ModelConfig modelConfig = generatorVO.getModelConfig();
        generator.setModelConfig(JSONUtil.toJsonStr(modelConfig));
        Meta.FileConfig fileConfig = generatorVO.getFileConfig();
        generator.setFileConfig(JSONUtil.toJsonStr(fileConfig));
        return generator;
    }

    public static GeneratorVO objToVo(Generator generator){
        if(generator == null){
            return null;
        }
        GeneratorVO generatorVO = new GeneratorVO();
        BeanUtil.copyProperties(generator,generatorVO);
        generatorVO.setTags(JSONUtil.toList(generator.getTags(),String.class));
        generatorVO.setModelConfig(JSONUtil.toBean(generator.getModelConfig(),Meta.ModelConfig.class));
        generatorVO.setFileConfig(JSONUtil.toBean(generator.getFileConfig(),Meta.FileConfig.class));

        return generatorVO;
    }
}
