import java.io.*;

import com.pdfjet.*;


/**
 *  Example_12.java
 *
 */
public class Example_12 {

    public static void main(String[] args) throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_12.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA);

        Page page = new Page(pdf, Letter.PORTRAIT);

        StringBuilder buf = new StringBuilder();
        BufferedReader reader =
                new BufferedReader(new FileReader("Example_12.java"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            // Both CR and LF are required by the scanner!
            buf.append((char) 13);
            buf.append((char) 10);
        }
        reader.close();

        BarCode2D code2D = new BarCode2D(buf.toString());
        code2D.setLocation(100f, 60f);
        float[] xy = code2D.drawOn(page);
/*
        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);
*/
        TextLine text = new TextLine(f1,
                "PDF417 barcode containing the program that created it.");
        text.setLocation(100f, 40f);
        text.drawOn(page);

        pdf.close();
    }

}   // End of Example_12.java
