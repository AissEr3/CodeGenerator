package com.aisser.maker.template.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/3-16:15
 */
@Data
public class TemplateMakerFileConfig {
    private List<FileInfoConfig> files;

    @Data
    @NoArgsConstructor
    public static class FileInfoConfig{

        private String path;

        private List<FileFilterConfig> filterConfigs;
    }

}
