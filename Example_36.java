import java.io.*;

import com.pdfjet.*;


/**
 *  Example_36.java
 *
 */
public class Example_36 {

    public Example_36() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_36.pdf")));

        Page page1 = new Page(pdf, A4.PORTRAIT, false);

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
        text.drawOn(page1);

        image1.setLocation(90f, 40f);
        image1.drawOn(page1);

        text.setText(
                "JPG image file embedded once and drawn 3 times");
        text.setLocation(90f, 550f);
        text.drawOn(page1);

        image2.setLocation(90f, 560f);
        image2.scaleBy(0.5f);
        image2.drawOn(page1);

        image2.setLocation(260f, 560f);
        image2.scaleBy(0.5f);
        image2.setRotateCW90(true);
        image2.drawOn(page1);

        image2.setLocation(350f, 560f);
        image2.setRotateCW90(false);
        image2.scaleBy(0.5f);
        image2.drawOn(page1);

        image3.setLocation(390f, 630f);
        image3.scaleBy(0.5f);
        image3.drawOn(page1);

        Page page2 = new Page(pdf, A4.PORTRAIT, false);
        image1.drawOn(page2);

        text.setText("Hello, World!!");
        text.setLocation(90f, 800f);
        text.drawOn(page2);

        text.setText(
                "The map on the right is an embedded BMP image");
        text.setUnderline(true);
        text.setStrikeout(true);
        text.setTextDirection(15);
        text.setLocation(90f, 800f);
        text.drawOn(page1);

        pdf.addPage(page2);
        pdf.addPage(page1);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_36();
    }

}   // End of Example_36.java
