package com.bnb.config;

import lombok.Data;

@Data
public class WatermarkConfig {
    /**
     * 水印的高度
     */
//    @Value(("${watermark.watermarkHeight}"))
    private Integer watermarkHeight;

    /**
     * 水印的宽度
     */
//    @Value(("${watermark.watermarkWeight}"))
    private Integer watermarkWeight;

    /**
     * 水印间隔距离
     */
//    @Value(("${watermark.watermarkInterval}"))
    private Integer watermarkInterval;

    /**
     * 填充透明度
     */
//    @Value(("${watermark.waterMarkInterval}"))
    private Float fillOpacity;

    /**
     * 轮廓不透明度
     */

    private Float strokeOpacity;
    /**
     * 字体大小
     */
    private Float size;
    /**
     * 水印旋转角度
     */
    private Float rotation;
}
