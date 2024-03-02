package com.aisser.maker.template;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import com.aisser.maker.template.enums.FileFilterRangeEnum;
import com.aisser.maker.template.enums.FileFilterRuleEnum;
import com.aisser.maker.template.model.FileFilterConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author AissEr
 * @created by AissEr on 2024/3/3-16:27
 */
public class FileFilter {

    /**
     * 对某个文件夹进行过滤
     * @param filePath
     * @param fileFilterConfigList
     * @return
     */
    public static List<File> doFilter(String filePath, List<FileFilterConfig> fileFilterConfigList){
        List<File> files = FileUtil.loopFiles(filePath);
        return files.stream()
                .filter(file -> doSingleFileFilter(fileFilterConfigList,file))
                .collect(Collectors.toList());
    }

    /**
     * 过滤单个文件
     * @param fileFilterConfigs
     * @param file
     * @return
     */
    public static boolean doSingleFileFilter(List<FileFilterConfig> fileFilterConfigs, File file){
        String fileName = file.getName();
        String fileContent = FileUtil.readUtf8String(file);

        // 保存匹配结果
        boolean result = true;

        if(CollUtil.isEmpty(fileFilterConfigs)){
            return true;
        }
        for (FileFilterConfig fileFilterConfig : fileFilterConfigs) {
            // 获取该过滤规则的范围，规则，值
            String range = fileFilterConfig.getRange();
            String rule = fileFilterConfig.getRule();
            String value = fileFilterConfig.getValue();

            // 获取匹配范围
            FileFilterRangeEnum fileFilterRange = FileFilterRangeEnum.getEnumByValue(range);
            if(fileFilterRange == null){
                continue;
            }

            // 根据匹配范围，指定对应需匹配的内容
            String content = fileName;
            switch(fileFilterRange){
                case FILE_NAME:
                    content = fileName;
                    break;
                case FILE_CONTENT:
                    content = fileContent;
                    break;
            }

            // 获取匹配规则，对对应的匹配内容进行验证
            FileFilterRuleEnum fileFilterRule = FileFilterRuleEnum.getEnumByValue(rule);
            if(fileFilterRule == null){
                continue;
            }
            switch (fileFilterRule){
                case CONTAINS:
                    result = content.contains(value);
                    break;
                case STARTS_WITH:
                    result = content.startsWith(value);
                    break;
                case END_WITH:
                    result = content.endsWith(value);
                    break;
                case REGEX:
                    result = content.matches(value);
                    break;
                case EQUALS:
                    result = content.equals(value);
                    break;
            }

            if(!result){
                return false;
            }

        }

        return true;
    }
}
