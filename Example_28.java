import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_28.java
 *
 */
public class Example_28 {

    public Example_28() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_28.pdf")));

        String path = "fonts/Android/";
/*
        Font f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans.ttf"));

        Font f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSansFallback.ttf"));
*/
        Font f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans.ttf.stream"),
                Font.STREAM);

        Font f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSansFallback.ttf.stream"),
                Font.STREAM);

        f1.setSize(11f);
        f2.setSize(11f);

        Page page = new Page(pdf, Letter.LANDSCAPE);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("data/report.csv"), "UTF-8"));

        float y = 50f;
        String str = null;
        while ((str = reader.readLine()) != null) {
            new TextLine(f1, str)
                    .setFallbackFont(f2)
                    .setLocation(50f, y += 20f)
                    .drawOn(page);
        }
        reader.close();

        float x = 50f;
        y = 300f;

        TextLine text = new TextLine(f2);
        StringBuilder buf = new StringBuilder();
        for (int i = 0x2701; i < 0x27bf; i++) {
            if (i % 16 == 0) {
                text.setText(buf.toString());
                text.setLocation(x, y += 24f);
                text.drawOn(page);
                buf = new StringBuilder();
            }
            buf.append((char) i);
        }

        f2.setSize(24f);
        TextLine line2 = new TextLine(f2, "\u54C1\u5851");
        line2.setLocation(630f, 570f);
        line2.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_28();
    }

}   // End of Example_28.java
