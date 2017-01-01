import java.io.*;

import com.pdfjet.*;


/**
 *  Example_21.java
 *
 */
public class Example_21 {

    public Example_21() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(new FileOutputStream("Example_21.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA);

        Page page = new Page(pdf, Letter.PORTRAIT);

        TextLine text = new TextLine(f1, "QR codes");
        text.setLocation(100.0f, 30.0f);
        text.drawOn(page);

        // Please note:
        // The higher the error correction level - the shorter the string that you can encode.
        QRCode qr = new QRCode(
                "http://d-project.googlecode.com/svn/trunk/misc/qrcode/as3/src/sample",
                ErrorCorrectLevel.L);   // Low
        qr.setModuleLength(3f);
        // qr.setLocation(100f, 100f);
        qr.setLocation(0.0001f, 100f);
        qr.drawOn(page);

        qr = new QRCode(
                "http://d-project.googlecode.com/svn/trunk/misc/qrcode",
                ErrorCorrectLevel.M);   // Medium
        qr.setLocation(300f, 100f);
        qr.drawOn(page);

        qr = new QRCode(
                "http://www.d-project.com/qrcode/index.html",
                ErrorCorrectLevel.Q);   // High
        qr.setLocation(100f, 300f);
        qr.drawOn(page);

        qr = new QRCode(
                "http://www.d-project.com",
                ErrorCorrectLevel.H);   // Very High
        qr.setLocation(300f, 300f);
        float[] xy = qr.drawOn(page);
/*
        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);
*/
        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        long t0 = System.currentTimeMillis();
        new Example_21();
        long t1 = System.currentTimeMillis();
        // System.out.println(t1 - t0);
    }

}   // End of Example_21.java
