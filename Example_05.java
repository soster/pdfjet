import java.io.*;

import com.pdfjet.*;


/**
 *  Example_05.java
 *
 */
public class Example_05 {

    public Example_05() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_05.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setItalic(true);

        Page page = new Page(pdf, Letter.PORTRAIT);

        TextLine text = new TextLine(f1);
        text.setLocation(300f, 300f);
        for (int i = 0; i < 360; i += 15) {
            text.setTextDirection(i);
            text.setUnderline(true);
            // text.setStrikeLine(true);
            text.setText("             Hello, World -- " + i + " degrees.");
            text.drawOn(page);
        }

        text = new TextLine(f1, "WAVE AWAY");
        text.setLocation(70f, 50f);
        text.drawOn(page);

        f1.setKernPairs(true);
        text = new TextLine(f1, "WAVE AWAY");
        text.setLocation(70f, 70f);
        text.drawOn(page);

        f1.setKernPairs(false);
        text = new TextLine(f1, "WAVE AWAY");
        text.setLocation(70f, 90f);
        text.drawOn(page);

        f1.setSize(8f);
        text = new TextLine(f1, "-- font.setKernPairs(false);");
        text.setLocation(150f, 50f);
        text.drawOn(page);
        text.setLocation(150f, 90f);
        text.drawOn(page);
        text = new TextLine(f1, "-- font.setKernPairs(true);");
        text.setLocation(150f, 70f);
        text.drawOn(page);

        Point point = new Point(300f, 300f);
        point.setShape(Point.CIRCLE);
        point.setFillShape(true);
        point.setColor(Color.blue);
        point.setRadius(37f);
        point.drawOn(page);
        point.setRadius(25f);
        point.setColor(Color.white);
        point.drawOn(page);

        page.setPenWidth(1f);
        page.drawEllipse(300f, 600f, 100f, 50f);

        f1.setSize(14f);        
        String unicode = "\u20AC\u0020\u201A\u0192\u201E\u2026\u2020\u2021\u02C6\u2030\u0160";
        text = new TextLine(f1, unicode);
        text.setLocation(100f, 700f);
        text.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_05();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_05.java
