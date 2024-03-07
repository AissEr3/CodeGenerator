package com.aisser.web.model.dto.generator;

import com.aisser.web.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/5-16:24
 */
@Data
public class GeneratorQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * id
     */
    private Long notId;

    /**
     * 搜索词
     */
    private String searchText;

    /**
     * 标签列表
     */
    private List<String> tags;

    /**
     * 至少有一个标签
     */
    private List<String> orTags;

    /**
     * 创建用户 id
     */
    private Long userId;

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
     * 代码生成器产物路径
     */
    private String distPath;

    /**
     * 状态
     */
    private Integer status;


    private static final long serialVersionUID = 5349347740707436320L;
}
