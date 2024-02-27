package ${basePackage}.cli.command;

import cn.hutool.core.bean.BeanUtil;
import ${basePackage}.generator.MainGenerator;
import ${basePackage}.model.DataModel;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

/**
 * @author ${author}
 * @created by ${author}
 */
@Data
@Command(name = "generate", description = "代码生成", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {

<#list modelConfig.models as modelInfo>
    /**
    * ${modelInfo.description}
    */
    @Option(names = {<#if modelInfo.abbr??>"-${modelInfo.abbr}"</#if>, "--${modelInfo.fieldName}"}, <#if modelInfo.description??>description = "${modelInfo.description}"</#if>, arity = "0..1", interactive = true, echo = true)
    private ${modelInfo.type} ${modelInfo.fieldName};

</#list>

    @Override
    public Integer call() throws Exception {
        DataModel mainTemplateConfig = new DataModel();
        BeanUtil.copyProperties(this,mainTemplateConfig);
        System.out.println("配置信息：" + mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);

        return 0;
    }

}
