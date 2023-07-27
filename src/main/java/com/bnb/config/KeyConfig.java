package com.bnb.config;

import lombok.Data;

@Data
public class KeyConfig {
    /**
     * 添加水印PDF文件夹地址
     */
//    @Value(("${inputFilePath}"))
    private String inputFilePath;

    /**
     * 保存地址
     */
//    @Value(("${outputFilePath}"))
    private String outputFilePath;

    /**
     * 水印文字
     */
    private String[] watermarkContents;
}
