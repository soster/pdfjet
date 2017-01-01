import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_43.java
 *
 */
public class Example_43 {

    public Example_43() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_43.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA);

        Page page = new Page(pdf, Letter.PORTRAIT);

        List<Paragraph> paragraphs = new ArrayList<Paragraph>();
        Paragraph p1 = new Paragraph();
        TextLine tl1 = new TextLine(f2,
"The Swiss Confederation was founded in 1291 as a defensive alliance among three cantons. In succeeding years, other localities joined the original three. The Swiss Confederation secured its independence from the Holy Roman Empire in 1499. Switzerland's sovereignty and neutrality have long been honored by the major European powers, and the country was not involved in either of the two World Wars. The political and economic integration of Europe over the past half century, as well as Switzerland's role in many UN and international organizations, has strengthened Switzerland's ties with its neighbors. However, the country did not officially become a UN member until 2002.");
        p1.add(tl1);

        Paragraph p2 = new Paragraph();
        TextLine tl2 = new TextLine(f2,
"Even so, unemployment has remained at less than half the EU average.");
        p2.add(tl2);

        paragraphs.add(p1);
        paragraphs.add(p2);

        Text text = new Text(paragraphs);
        text.setLocation(50f, 50f);
        text.setWidth(500f);
        float[] xy = text.drawOn(page);
// System.out.println("x == " + xy[0]);
// System.out.println("y == " + xy[1]);

        List<float[]> points = text.getBeginParagraphPoints();
        for (float[] point : points) {
// System.out.println("x_begin == " + point[0]);
// System.out.println("y_begin == " + point[1]);
        }

        points = text.getEndParagraphPoints();
        for (float[] point : points) {
// System.out.println("x_end == " + point[0]);
// System.out.println("y_end == " + point[1]);
            page.drawRect(point[0], point[1], 15f, 15f);
        }

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_43();
    }

}   // End of Example_43.java
