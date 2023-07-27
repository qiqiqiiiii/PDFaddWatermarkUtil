package com.bnb.controller;

import com.bnb.config.KeyConfig;
import com.bnb.config.WatermarkConfig;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Description: PDF增加水印工具类
 */
public class PDFAddWatermarkController {

    /**
     * 给PDF添加水印
     *
     * @param inputFilePath    原文件路径+名称,例如D:\\pdf\\test.pdf
     * @param outputFilePath   添加水印后输出文件保存的路径+名称
     * @param waterMarkContent 添加水印的内容
     */
    public static void pdfAddWaterMark(String inputFilePath, String outputFilePath, String waterMarkContent, WatermarkConfig watermarkConfig) {
        try {
            // 读取PDF文件流
            PdfReader pdfReader = new PdfReader(inputFilePath);

            // 创建PDF文件的模板，可以对模板的内容修改，重新生成新PDF文件
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(outputFilePath));

            // 设置水印字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            // 设置PDF内容的Graphic State 图形状态
            PdfGState pdfGraPhicState = new PdfGState();
            // 填充透明度
            pdfGraPhicState.setFillOpacity(watermarkConfig.getFillOpacity());
            // 轮廓不透明度
            pdfGraPhicState.setStrokeOpacity(watermarkConfig.getStrokeOpacity());

            // PDF页数
            int pdfPageNum = pdfReader.getNumberOfPages() + 1;

            // PDF文件内容字节
            PdfContentByte pdfContent;

            // PDF页面矩形区域
            Rectangle pageRectangle;

            for (int i = 1; i < pdfPageNum; i++) {
                // 获取当前页面矩形区域
                pageRectangle = pdfReader.getPageSizeWithRotation(i);
                // 获取当前页内容，getOverContent表示之后会在页面内容的上方加水印
                pdfContent = pdfStamper.getOverContent(i);

                // 获取当前页内容，getOverContent表示之后会在页面内容的下方加水印
                // pdfContent = pdfStamper.getUnderContent(i);

                pdfContent.saveState();
                // 设置水印透明度
                pdfContent.setGState(pdfGraPhicState);

                // 开启写入文本
                pdfContent.beginText();
                // 设置字体
                pdfContent.setFontAndSize(baseFont, watermarkConfig.getSize());

                // 在高度和宽度维度每隔waterMarkInterval距离添加一个水印
                for (int height = watermarkConfig.getWatermarkHeight(); height < pageRectangle.getHeight(); height = height + watermarkConfig.getWatermarkInterval()) {
                    for (int width = watermarkConfig.getWatermarkWeight(); width < pageRectangle.getWidth() + watermarkConfig.getWatermarkWeight();
                         width = width + watermarkConfig.getWatermarkInterval()) {
                        // 添加水印文字并旋转30度角
                        pdfContent.showTextAligned(Element.ALIGN_LEFT, waterMarkContent, width - watermarkConfig.getWatermarkWeight(),
                                height - watermarkConfig.getWatermarkHeight(), watermarkConfig.getRotation());
                    }
                }
                // 停止写入文本
                pdfContent.endText();
            }
            pdfStamper.close();
            pdfReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void pdfAddWatermarkBatch(KeyConfig keyConfig, WatermarkConfig watermarkConfig) {
        try {
            String[] watermarkContents = keyConfig.getWatermarkContents();
            File inputFile = new File(keyConfig.getInputFilePath());
            if (!inputFile.isDirectory()) { //如果是一个文件
                String fileName = inputFile.getName();
                for (String watermarkContent : watermarkContents) {
                    File outputFile = new File(keyConfig.getOutputFilePath() + "\\" + watermarkContent);
                    if (!outputFile.exists()) {
                        outputFile.mkdir();
                    }
                    pdfAddWaterMark(keyConfig.getInputFilePath(),
                            outputFile.getPath() + fileName,
                            watermarkContent, watermarkConfig);
                }
            } else {
                File[] files = inputFile.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        break;
                    }
                    String fileName = file.getName();
                    for (String watermarkContent : watermarkContents) {
                        File outputFile = new File(keyConfig.getOutputFilePath() + "\\" + watermarkContent);
                        if (!outputFile.exists()) {
                            outputFile.mkdir();
                        }
                        pdfAddWaterMark(file.getPath(),
                                outputFile.getPath() + "\\" + fileName,
                                watermarkContent, watermarkConfig);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String[] strs = new String[]{"银行", "ABCD", "XYZ","信托","仅供内部使用"};
        KeyConfig keyConfig = new KeyConfig();
        keyConfig.setInputFilePath("D:\\test");
        keyConfig.setOutputFilePath("D:\\test\\result");
        keyConfig.setWatermarkContents(strs);

        WatermarkConfig watermarkConfig = new WatermarkConfig();
        watermarkConfig.setWatermarkHeight(50);
        watermarkConfig.setWatermarkWeight(100);
        watermarkConfig.setWatermarkInterval(150);
        watermarkConfig.setFillOpacity(0.2f);
        watermarkConfig.setStrokeOpacity(0.4f);
        watermarkConfig.setSize(30f);
        watermarkConfig.setRotation(30f);
        pdfAddWatermarkBatch(keyConfig, watermarkConfig);
    }
}