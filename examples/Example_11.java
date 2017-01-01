import java.io.*;

import com.pdfjet.*;


/**
 *  Example_11.java
 *
 */
public class Example_11 {

    public Example_11() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_11.pdf")));

        // Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f1 = new Font(pdf,
                new BufferedInputStream(getClass().getResourceAsStream(
                        "fonts/Android/DroidSans.ttf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        BarCode code = new BarCode(BarCode.CODE128, "Hello, World!");
        code.setLocation(170f, 70f);
        code.setModuleLength(0.75f);
        code.setFont(f1);
        float[] xy = code.drawOn(page);
/*
        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);
*/
        code = new BarCode(BarCode.CODE128, "G86513JVW0C");
        code.setLocation(170f, 170f);
        code.setModuleLength(0.75f);
        code.setDirection(BarCode.TOP_TO_BOTTOM);
        code.setFont(f1);
        xy = code.drawOn(page);

        code = new BarCode(BarCode.CODE39, "WIKIPEDIA");
        code.setLocation(270f, 370f);
        code.setModuleLength(0.75f);
        code.setFont(f1);
        xy = code.drawOn(page);

        code = new BarCode(BarCode.CODE39, "CODE39");
        code.setLocation(400f, 70f);
        code.setModuleLength(0.75f);
        code.setDirection(BarCode.TOP_TO_BOTTOM);
        code.setFont(f1);
        xy = code.drawOn(page);

        code = new BarCode(BarCode.CODE39, "CODE39");
        code.setLocation(450f, 70f);
        code.setModuleLength(0.75f);
        code.setDirection(BarCode.BOTTOM_TO_TOP);
        code.setFont(f1);
        xy = code.drawOn(page);

        code = new BarCode(BarCode.UPC, "712345678904");
        code.setLocation(450f, 270f);
        code.setModuleLength(0.75f);
        code.setDirection(BarCode.BOTTOM_TO_TOP);
        code.setFont(f1);
        xy = code.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_11();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_11.java
