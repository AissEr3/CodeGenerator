package com.aisser.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.aisser.generator.MainGenerator;
import com.aisser.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine;
import picocli.CommandLine.Option;
import picocli.CommandLine.Command;

import java.util.concurrent.Callable;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-15:49
 */
@Data
@Command(name = "generate", description = "代码生成", mixinStandardHelpOptions = true)
public class GenerateCommand implements Callable<Integer> {
    /**
     * 是否有循环
     */
    @Option(names = {"-l", "--loop"}, description = "是否循环", arity = "0..1", interactive = true, echo = true)
    private boolean loop;

    /**
     * 作者信息
     */
    @Option(names = {"-a", "--author"}, description = "作者名称", arity = "0..1", interactive = true, echo = true)
    private String author = "AissEr";

    /**
     * 输出内容
     */
    @Option(names = {"-s", "--sum"}, description = "输出文本格式", arity = "0..1", interactive = true, echo = true)
    private String outputText = "Sum = ";

    @Override
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this,mainTemplateConfig);
        System.out.println("配置信息：" + mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);

        return 0;
    }

}
