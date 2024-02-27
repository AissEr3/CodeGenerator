package com.aisser.maker.generator;

import cn.hutool.core.io.FileUtil;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/28-9:11
 */
public class ScriptGenerator {

    /**
     * 生成脚本的生成器
     * @param outputPath
     * @param jarPath
     */
    public static void doGenerator(String outputPath, String jarPath){
        StringBuilder builder = new StringBuilder();
        // Linux
//        String linuxPrefix = "#!/bin/bash";
//        String linuxSuffix = "\"$@\"";
//        builder.append(linuxPrefix).append("\n");
//        builder.append(String.format("java -jar %s ",jarPath));
//        builder.append(linuxSuffix).append("\n");
//        FileUtil.writeBytes(builder.toString().getBytes(StandardCharsets.UTF_8),outputPath);


        // Windows
        builder = new StringBuilder();
        String windowsPrefix = "@echo off";
        String windowsSuffix = "%*";

        builder.append(windowsPrefix).append("\n");
        builder.append(String.format("java -jar %s ",jarPath));
        builder.append(windowsSuffix).append("\n");

        FileUtil.writeBytes(builder.toString().getBytes(StandardCharsets.UTF_8),outputPath);

//         Linux需要添加权限
        try{
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath),permissions);
        }catch(Exception e){

        }
    }
}
