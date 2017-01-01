import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_47.java
 *
 */
public class Example_47 {

    public Example_47() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_47.pdf")),
                        Compliance.PDF_UA);

        Font f1 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);
        f1.setSize(14f);

        Font f2 = new Font(
                pdf,
                getClass().getResourceAsStream(
                        "fonts/DejaVu/ttf/DejaVuLGCSerif.ttf.stream"),
                Font.STREAM);
        f2.setSize(14f);
        f2.setItalic(true);

        Page page = new Page(pdf, Letter.PORTRAIT);

        List<Paragraph> paragraphs = new ArrayList<Paragraph>();

        Paragraph paragraph = new Paragraph()
                .add(new TextLine(f1,
"The centres also offer free one-on-one consultations with business advisors who can review your business plan and make recommendations to improve it. The small business centres offer practical resources, from step-by-step info on setting up your business to sample business plans to a range of business-related articles and books in our resource libraries."))
                .add(new TextLine(f2,
"This text is blue color and is written using italic font.")
                        .setColor(Color.blue));

        paragraphs.add(paragraph);

        float height = 82f;

        Line line = new Line(70f, 150f, 70f, 150f + height);
        line.drawOn(page);

        TextFrame frame = new TextFrame(paragraphs);
        frame.setLocation(70f, 150f);
        frame.setWidth(500f);
        frame.setHeight(height);
        frame = frame.drawOn(page);

        if (frame.getParagraphs() != null) {
            frame.setLocation(70f, 450f);
            frame.setWidth(500f);
            frame.setHeight(90f);
            frame = frame.drawOn(page);
        }

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_47();
    }

}   // End of Example_47.java
