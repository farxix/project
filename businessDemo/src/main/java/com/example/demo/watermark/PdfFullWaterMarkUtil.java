package com.example.demo.watermark;

import java.awt.FontMetrics;
import java.io.FileOutputStream;
import javax.swing.JLabel;
import com.itextpdf.text.Element;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 给图像添加水印
 *
 * @since 2021-10-29
 */
public class PdfFullWaterMarkUtil {

    public static void main(String[] args) {
        waterMark("C:\\Users\\farxix\\Pictures\\pdf.pdf", "C:\\Users\\farxix\\Pictures\\pdf-copy.pdf", "复印无效");
    }


    private static int interval = -5;

    public static void waterMark(String inputFile,
                                 String outputFile, String waterMarkName) {
        try {
            PdfReader reader = new PdfReader(inputFile);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(
                    outputFile));

            BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);

            Rectangle pageRect = null;
            PdfGState gs = new PdfGState();
            gs.setFillOpacity(0.3f);
            gs.setStrokeOpacity(0.4f);
            int total = reader.getNumberOfPages() + 1;

            JLabel label = new JLabel();
            FontMetrics metrics;
            int textH = 0;
            int textW = 0;
            label.setText(waterMarkName);
            metrics = label.getFontMetrics(label.getFont());
            textH = metrics.getHeight();
            textW = metrics.stringWidth(label.getText());

            PdfContentByte under;
            for (int i = 1; i < total; i++) {
                pageRect = reader.getPageSizeWithRotation(i);
                under = stamper.getOverContent(i);
                under.saveState();
                under.setGState(gs);
                under.beginText();
                under.setFontAndSize(base, 20);

                //水印文字成30度角倾斜
                //你可以随心所欲的改你自己想要的角度
                for (int height = interval + textH; height < pageRect.getHeight();
                     height = height + textH * 3) {
                    for (int width = interval + textW;
                         width < pageRect.getWidth() + textW;
                         width = width + textW * 2) {
                        under.showTextAligned(Element.ALIGN_LEFT, waterMarkName, width - textW,
                                height - textH, 30);
                    }
                }
                // 添加水印文字
                under.endText();
            }
            stamper.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}