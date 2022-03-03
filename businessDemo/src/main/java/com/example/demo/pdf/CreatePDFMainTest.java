package com.example.demo.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

/**
 * https://www.cnblogs.com/liaojie970/p/7132475.html
 */
public class CreatePDFMainTest {

    public static void main(String[] args) throws Exception {
        Document document = new Document(PageSize.A4);
        //第二步，创建Writer实例
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("hello.pdf"));
        // 设置密码为："World"
        writer.setEncryption("Hello".getBytes(), "World".getBytes(),
                PdfWriter.ALLOW_SCREENREADERS,
                PdfWriter.STANDARD_ENCRYPTION_128);

        //创建中文字体
        BaseFont bfchinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        Font fontChinese = new Font(bfchinese, 12, Font.NORMAL);

        //第三步，打开文档
        document.open();

        //第四步，写入内容
        Paragraph paragraph = new Paragraph("hello world", fontChinese);
        document.add(paragraph);
        //第五步，关闭文档
        document.close();
    }
}