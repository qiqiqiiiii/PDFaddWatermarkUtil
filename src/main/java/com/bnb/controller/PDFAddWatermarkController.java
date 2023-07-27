package com.bnb.controller;

import com.bnb.config.KeyConfig;
import com.bnb.config.WatermarkConfig;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Scanner;

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
                        outputFile.mkdirs();
                    }
                    pdfAddWaterMark(inputFile.getPath(),
                            outputFile.getPath() + "\\" + fileName,
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
                            outputFile.mkdirs();
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

        KeyConfig keyConfig = new KeyConfig();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入PDF文件夹或PDF文件地址：");
        keyConfig.setInputFilePath(scanner.next());
        System.out.println("输入生产文件地址：");
        keyConfig.setOutputFilePath(scanner.next());
        System.out.println("输入水印文字（使用”-“分隔）：");
        String[] contents = scanner.next().split("-");
        keyConfig.setWatermarkContents(contents);

        WatermarkConfig watermarkConfig = new WatermarkConfig();
        System.out.println("输入水印高度（50）");
        watermarkConfig.setWatermarkHeight(scanner.nextInt());
        System.out.println("输入水印宽度（50）");
        watermarkConfig.setWatermarkWeight(scanner.nextInt());
        System.out.println("输入水印间隔距离（范围：100-1000，推荐250）");
        watermarkConfig.setWatermarkInterval(scanner.nextInt());
        System.out.println("输入水印填充透明度（范围：0.0-1.0，推荐0.2）");
        watermarkConfig.setFillOpacity(scanner.nextFloat());
        System.out.println("输入水印轮廓不透明度(0.4)");
        watermarkConfig.setStrokeOpacity(scanner.nextFloat());
        System.out.println("输入水印字体大小(范围10-100，推荐30)");
        watermarkConfig.setSize(scanner.nextFloat());
        System.out.println("输入水印旋转角度(0-360)");
        watermarkConfig.setRotation(scanner.nextFloat());
        pdfAddWatermarkBatch(keyConfig, watermarkConfig);
    }
}