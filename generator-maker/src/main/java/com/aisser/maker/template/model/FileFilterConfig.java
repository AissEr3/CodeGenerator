package com.aisser.maker.template.model;

import lombok.Builder;
import lombok.Data;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/3-16:12
 */
@Data
@Builder
public class FileFilterConfig {

    /**
     * 过滤范围
     */
    private String range;

    /**
     * 过滤规则
     */
    private String rule;

    /**
     * 过滤值
     */
    private String value;
}
