package com.bnb.config;

import lombok.Data;

@Data
public class KeyConfig {
    /**
     * 添加水印PDF文件夹地址
     */
    private String inputFilePath;

    /**
     * 保存地址
     */
    private String outputFilePath;

    /**
     * 水印文字
     */
    private String[] watermarkContents;
}
