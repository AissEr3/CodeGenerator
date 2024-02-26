package com.aisser.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-19:41
 */
public class StaticFileGenerator {

    /**
     * 拷贝文件
     * @param inputPath
     * @param outputPath
     */
    public static void copyFilesByHuTools(String inputPath, String outputPath){
        FileUtil.copy(inputPath,outputPath,false);
    }
}
