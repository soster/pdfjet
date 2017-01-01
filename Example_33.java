import java.io.*;

import com.pdfjet.*;


/**
 *  Example_33.java
 *
 */
public class Example_33 {

    public Example_33() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_33.pdf")));

        Page page = new Page(pdf, A4.PORTRAIT);

        Image image = new Image(
                pdf,
                getClass().getResourceAsStream("images/photoshop.jpg"),
                ImageType.JPG);
        image.setLocation(10f, 10f);
        image.scaleBy(0.25f);
        image.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_33();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_33.java
