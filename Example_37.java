import java.io.*;

import com.pdfjet.*;


/**
 *  Example_37.java
 *
 */
public class Example_37 {

    public Example_37() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_37.pdf")));

        Page page = new Page(pdf, A4.PORTRAIT);

        page.setBrushColor(Color.slategray);
        // page.setBrushColor(Color.yellow);
        page.fillRect(90f, 40f, 340f, 240f);

        Image image1 = new Image(
                pdf,
                getClass().getResourceAsStream("images/indexed-color-with-alpha.png"),
                ImageType.PNG);
        image1.setLocation(90f, 40f);
        image1.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_37();
    }

}   // End of Example_37.java
