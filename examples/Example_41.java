import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_41.java
 *
 */
public class Example_41 {

    public Example_41() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_41.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(10f);

        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f2.setSize(10f);

        Font f3 = new Font(pdf, CoreFont.HELVETICA_OBLIQUE);
        f3.setSize(10f);

        List<Paragraph> paragraphs = new ArrayList<Paragraph>();

        Paragraph paragraph = new Paragraph()
                .add(new TextLine(f1,
"The small business centres offer practical resources, from step-by-step info on setting up your business to sample business plans to a range of business-related articles and books in our resource libraries.")
                        .setUnderline(true))
                .add(new TextLine(f2,
"This text is bold!")
                        .setColor(Color.blue));

        paragraphs.add(paragraph);

        paragraph = new Paragraph()
                .add(new TextLine(f1,
"The centres also offer free one-on-one consultations with business advisors who can review your business plan and make recommendations to improve it.")
                        .setUnderline(true))
                .add(new TextLine(f3,
"This text is using italic font.")
                        .setColor(Color.green));

        paragraphs.add(paragraph);

        Text text = new Text(paragraphs);
        text.setLocation(70f, 90f);
        text.setWidth(500f);
        // text.setSpaceBetweenTextLines(0f);
        float[] xy = text.drawOn(page);

        List<float[]> beginParagraphPoints = text.getBeginParagraphPoints();
        int paragraphNumber = 1;
        for (int i = 0; i < beginParagraphPoints.size(); i++) {
            float[] point = beginParagraphPoints.get(i);
            new TextLine(f1, String.valueOf(paragraphNumber) + ".")
                    .setLocation(point[0] - 30f, point[1])
                    .drawOn(page);
            paragraphNumber++;
        }

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_41();
    }

}   // End of Example_41.java
