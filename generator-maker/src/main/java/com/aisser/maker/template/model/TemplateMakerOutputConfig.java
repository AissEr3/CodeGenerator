package com.aisser.maker.template.model;

import lombok.Data;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/4-17:36
 */
@Data
public class TemplateMakerOutputConfig {

    // 从未分组文件中移除组内的同名文件
    private boolean removeGroupFilesFromRoot = true;
}
