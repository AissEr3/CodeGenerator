package com.aisser.maker.template;

import com.aisser.maker.meta.Meta;
import com.aisser.maker.template.model.TemplateMakerFileConfig;
import com.aisser.maker.template.model.TemplateMakerModelConfig;
import com.aisser.maker.template.model.TemplateMakerOutputConfig;
import lombok.Data;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/4-16:13
 */
@Data
public class TemplateMakerConfig {
    private Long id;

    private Meta meta = new Meta();

    private String originProjectPath;

    private TemplateMakerFileConfig fileConfig = new TemplateMakerFileConfig();

    private TemplateMakerModelConfig modelConfig = new TemplateMakerModelConfig();

    private TemplateMakerOutputConfig outputConfig = new TemplateMakerOutputConfig();
}
