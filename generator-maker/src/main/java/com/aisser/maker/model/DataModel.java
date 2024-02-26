package com.aisser.maker.model;

import lombok.Data;

/**
 * @author AissEr
 * @created by AissEr on 2024/2/25-20:43
 */
@Data
public class DataModel {
    /**
     * 是否有循环
     */
    private boolean loop;

    /**
     * 作者信息
     */
    private String author = "AissEr";

    /**
     * 输出内容
     */
    private String outputText = "Sum = ";

}
