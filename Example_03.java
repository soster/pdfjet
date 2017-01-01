import java.io.*;

import com.pdfjet.*;


/**
 *  Example_03.java
 *
 */
public class Example_03 {

    public Example_03() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_03.pdf")));

        Page page = new Page(pdf, A4.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA);

        Image image1 = new Image(
                pdf,
                getClass().getResourceAsStream("images/eu-map.png"),
                ImageType.PNG);

        Image image2 = new Image(
                pdf,
                getClass().getResourceAsStream("images/fruit.jpg"),
                ImageType.JPG);

        Image image3 = new Image(
                pdf,
                getClass().getResourceAsStream("images/mt-map.bmp"),
                ImageType.BMP);

        TextLine text = new TextLine(f1,
                "The map below is an embedded PNG image");
        text.setLocation(90f, 30f);
        text.drawOn(page);

        image1.setLocation(90f, 40f);
        image1.drawOn(page);

        text.setText(
                "JPG image file embedded once and drawn 3 times");
        text.setLocation(90f, 550f);
        text.drawOn(page);

        image2.setLocation(90f, 560f);
        image2.scaleBy(0.5f);
        image2.drawOn(page);

        image2.setLocation(260f, 560f);
        image2.scaleBy(0.5f);
        image2.setRotate(ClockWise.degrees_90);
        // image2.setRotate(ClockWise.degrees_180);
        // image2.setRotate(ClockWise.degrees_270);
        image2.drawOn(page);

        image2.setLocation(350f, 560f);
        image2.setRotate(ClockWise.degrees_0);
        image2.scaleBy(0.5f);
        image2.drawOn(page);

        text.setText(
                "The map on the right is an embedded BMP image");
        text.setUnderline(true);
        text.setStrikeout(true);
        text.setTextDirection(15);
        text.setLocation(90f, 800f);
        text.drawOn(page);

        image3.setLocation(390f, 630f);
        image3.scaleBy(0.5f);
        image3.drawOn(page);

        Page page2 = new Page(pdf, A4.PORTRAIT);
        float[] xy = image1.drawOn(page2);

        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page2);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            // long t0 = System.currentTimeMillis();
            new Example_03();
            // To open the PDF from a Java program:
            // Runtime.getRuntime().exec(
            //         "rundll32 url.dll,FileProtocolHandler Example_03.pdf");
            // long t1 = System.currentTimeMillis();
            // System.out.println("t1 - t0 == " + (t1 - t0));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_03.java
