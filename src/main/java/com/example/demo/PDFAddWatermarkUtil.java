package com.example.demo;

import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import org.springframework.context.annotation.PropertySource;

import java.io.FileOutputStream;

/**
 * @Description: PDF增加水印工具类
 */
@PropertySource("classpath:watermark.properties")
public class PDFAddWatermarkUtil {

    /**
     * 给PDF添加水印
     *
     * @param inputFilePath    原文件路径+名称,例如D:\\pdf\\test.pdf
     * @param outputFilePath   添加水印后输出文件保存的路径+名称
     * @param waterMarkContent 添加水印的内容
     */
    public static void pdfAddWaterMark(String inputFilePath, String outputFilePath, String waterMarkContent) {
        try {
            // 水印的高和宽
            int waterMarkHeight = 100;
            int watermarkWeight = 100;

            // 水印间隔距离
            int waterMarkInterval = 200;

            // 读取PDF文件流
            PdfReader pdfReader = new PdfReader(inputFilePath);

            // 创建PDF文件的模板，可以对模板的内容修改，重新生成新PDF文件
            PdfStamper pdfStamper = new PdfStamper(pdfReader, new FileOutputStream(outputFilePath));

            // 设置水印字体
            BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            // 设置PDF内容的Graphic State 图形状态
            PdfGState pdfGraPhicState = new PdfGState();
            // 填充透明度
            pdfGraPhicState.setFillOpacity(0.2f);
            // 轮廓不透明度
            pdfGraPhicState.setStrokeOpacity(0.4f);

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
                pdfContent.setFontAndSize(baseFont, 20);

                // 在高度和宽度维度每隔waterMarkInterval距离添加一个水印
                for (int height = waterMarkHeight; height < pageRectangle.getHeight(); height = height + waterMarkInterval) {
                    for (int width = watermarkWeight; width < pageRectangle.getWidth() + watermarkWeight;
                         width = width + waterMarkInterval) {
                        // 添加水印文字并旋转30度角
                        pdfContent.showTextAligned(Element.ALIGN_LEFT, waterMarkContent, width - watermarkWeight,
                                height - waterMarkHeight, 30);
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

    public static void main(String[] args) {
        try {
            // 20230420 10_46_29--33.pdf
            // 20220711 11_04_38--29.pdf
            //"C:\Users\842527323\Desktop\10-10latex.pdf"
            String fileName = "10-10latex";
//            pdfAddWaterMark("C:\\Users\\842527323\\Desktop\\" + fileName + ".pdf",
//                    "C:\\Users\\842527323\\Desktop\\" + fileName + "-" + System.currentTimeMillis() + "-watermark.pdf",
//                    "仅供宁波银行内部使用");

            pdfAddWaterMark("C:\\Users\\842527323\\Desktop\\" + fileName + ".pdf",
                    "C:\\Users\\842527323\\Desktop\\" + fileName + "-watermark.pdf",
                    "仅供宁波银行内部使用");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}