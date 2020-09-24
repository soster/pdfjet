import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_30.java
 */
public class Example_30 {

    public Example_30() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_30.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);
    
        Font font = new Font(pdf, CoreFont.HELVETICA);

        Image image = new Image(
                pdf,
                getClass().getResourceAsStream("images/map407.png"),
                ImageType.PNG);
        image.setLocation(10f, 100f);

        TextLine textLine = new TextLine(font);
        textLine.setText("© OpenStreetMap contributors");
        textLine.setLocation(430f, 655f);
        textLine.drawOn(page);

        textLine = new TextLine(font);
        textLine.setText("http://www.openstreetmap.org/copyright");
        textLine.setURIAction("http://www.openstreetmap.org/copyright");
        textLine.setLocation(380f, 665f);
        textLine.drawOn(page);

        OptionalContentGroup group = new OptionalContentGroup("Map");
        group.add(image);
        group.setVisible(true);
        group.setPrintable(true);
        group.drawOn(page);

        TextBox tb = new TextBox(font);
        tb.setText("Hello Text");
        tb.setLocation(300f, 100f);

        tb = new TextBox(font);
        tb.setText("Hello Blue Layer Text");
        tb.setLocation(300f, 200f);

        Line line = new Line();
        line.setPointA(300f, 250f);
        line.setPointB(500f, 250f);
        line.setWidth(2f);
        line.setColor(Color.blue);

        group = new OptionalContentGroup("Blue");
        group.add(tb);
        group.add(line);
        // group.setVisible(true);
        group.drawOn(page);

        line = new Line();
        line.setPointA(300f, 260f);
        line.setPointB(500f, 260f);
        line.setWidth(2f);
        line.setColor(Color.red);

        image = new Image(
                pdf,
                getClass().getResourceAsStream("images/BARCODE.PNG"),
                ImageType.PNG);
        image.setLocation(10f, 100f);

        group = new OptionalContentGroup("Barcode");
        group.add(image);
        group.add(line);
        // group.setVisible(true);
        group.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_30();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}   // End of Example_30.java
