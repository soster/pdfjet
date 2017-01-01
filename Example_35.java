import java.io.*;

import com.pdfjet.*;
import java.lang.Character.UnicodeBlock;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

import java.util.HashSet;

import com.pdfjet.A4;
import com.pdfjet.CodePage;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.TextLine;


/**
 *  Example_35.java
 *
 */
public class Example_35 {

    public Example_35() throws Exception {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("data/chinese-english.txt"), "UTF-8"));
        String text = reader.readLine();


        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_35.pdf")));

        Page page = new Page(pdf, A4.PORTRAIT);

        Font mainFont = new Font(pdf, "AdobeMingStd");
        Font fallbackFont = new Font(
                pdf,
                new FileInputStream("fonts/Android/Roboto-Regular.ttf"));

        TextLine textLine = new TextLine(mainFont);
        textLine.setText(text);
        textLine.setLocation(50f, 50f);
        textLine.drawOn(page);

        textLine = new TextLine(mainFont);
        textLine.setFallbackFont(fallbackFont);
        textLine.setText(text);
        textLine.setLocation(50f, 80f);
        textLine.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_35();
    }

}   // End of Example_35.java
