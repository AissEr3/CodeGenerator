package com.aisser.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/26-16:13
 */
@Command(name = "list", description = "查看文件列表", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable{

    @Override
    public void run() {
        String projectPath = System.getProperty("user.dir");
        File parentFile = new File(projectPath).getParentFile();
        String input = new File(parentFile,"generator-demo-project/acm-template").getPath();
        List<File> files = FileUtil.loopFiles(input);
        for (File file : files) {
            System.out.println(file);
        }
    }

}
