import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_49.java
 *
 */
public class Example_49 {

    public Example_49() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_49.pdf")),
                        Compliance.PDF_UA);

        String path = "fonts/Android/";
        Font f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSerif-Regular.ttf"));

        Font f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSerif-Italic.ttf"));

        f1.setSize(14f);
        f2.setSize(16f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Paragraph paragraph1 = new Paragraph()
                .add(new TextLine(f1, "Hello"))
                .add(new TextLine(f1, "W").setColor(Color.black).setTrailingSpace(false))
                .add(new TextLine(f1, "o").setColor(Color.red).setTrailingSpace(false))
                .add(new TextLine(f1, "r").setColor(Color.green).setTrailingSpace(false))
                .add(new TextLine(f1, "l").setColor(Color.blue).setTrailingSpace(false))
                .add(new TextLine(f1, "d").setColor(Color.black))
                .add(new TextLine(f1, "$").setTrailingSpace(false)
                        .setVerticalOffset(f1.getAscent() - f2.getAscent()))
                .add(new TextLine(f2, "29.95").setColor(Color.blue))
                .setAlignment(Align.RIGHT);

        Paragraph paragraph2 = new Paragraph()
                .add(new TextLine(f1, "Hello"))
                .add(new TextLine(f1, "World"))
                .add(new TextLine(f1, "$"))
                .add(new TextLine(f2, "29.95").setColor(Color.blue))
                .setAlignment(Align.RIGHT);

        TextColumn column = new TextColumn();
        column.addParagraph(paragraph1);
        column.addParagraph(paragraph2);
        column.setLocation(70f, 150f);
        column.setWidth(500f);
        column.drawOn(page);


        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        paragraphs.add(paragraph1);
        paragraphs.add(paragraph2);

        Text text = new Text(paragraphs);
        text.setLocation(70f, 200f);
        text.setWidth(500f);
        text.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_49();
    }

}   // End of Example_49.java
