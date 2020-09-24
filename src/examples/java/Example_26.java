import java.io.*;

import com.pdfjet.*;


/**
 *  Example_26.java
 *
 */
public class Example_26 {

    public Example_26() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_26.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(10f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        float x = 70f;
        float y = 50f;

        new CheckBox(f1, "Hello")
                .setLocation(x, y += 30f)
                .setCheckmark(Color.blue)
                .check(Mark.CHECK)
                .drawOn(page);

        new CheckBox(f1, "World!")
                .setLocation(x, y += 30f)
                .setCheckmark(Color.blue)
                .setURIAction("http://pdfjet.com")
                .check(Mark.CHECK)
                .drawOn(page);

        new CheckBox(f1, "This is a test.")
                .setLocation(x, y += 30f)
                .setURIAction("http://pdfjet.com")
                .drawOn(page);

        new RadioButton(f1, "Hello, World!")
                .setLocation(x, y += 30f)
                .select(true)
                .drawOn(page);

        float[] xy = (new RadioButton(f1, "Yes"))
                .setLocation(x + 100f, 80f)
                .setURIAction("http://pdfjet.com")
                .select(true)
                .drawOn(page);

        xy = (new RadioButton(f1, "No"))
                .setLocation(xy[0], 80f)
                .drawOn(page);

        xy = (new CheckBox(f1, "Hello"))
                .setLocation(xy[0], 80f)
                .setCheckmark(Color.blue)
                .check(Mark.X)
                .drawOn(page);

        xy = (new CheckBox(f1, "Yahoo")
                .setLocation(xy[0], 80f)
                .setCheckmark(Color.blue)
                .check(Mark.CHECK)
                .drawOn(page));

        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);

        new TextLine(f1, "This is a test!")
                .setLocation(170f, 110f)
                .drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_26();
    }

}   // End of Example_26.java
