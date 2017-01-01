import java.io.*;

import com.pdfjet.*;


/**
 *  Example_10.java
 *
 */
public class Example_10 {

    public Example_10() throws Exception {

        int rotate = 0;
        // int rotate = 90;
        // int rotate = 270;

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_10.pdf")));

        pdf.setTitle("Using TextColumn and Paragraph classes");
        pdf.setSubject("Examples");
        pdf.setAuthor("Innovatics Inc.");

        String fileName = "/images/sz-map.png";
        Image image1 = new Image(pdf, new BufferedInputStream(
                getClass().getResourceAsStream(fileName)), ImageType.PNG);

        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(10f);

        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f2.setSize(14f);

        Font f3 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f3.setSize(12f);

        Font f4 = new Font(pdf, CoreFont.HELVETICA_OBLIQUE);
        f4.setSize(10f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        image1.setLocation(90f, 35f);
        image1.scaleBy(0.75f);
        image1.drawOn(page);

        TextColumn column = new TextColumn(rotate);
        column.setSpaceBetweenLines(5.0f);
        column.setSpaceBetweenParagraphs(10.0f);

        Paragraph p1 = new Paragraph();
        p1.setAlignment(Align.CENTER);
        p1.add(new TextLine(f2, "Switzerland"));

        Paragraph p2 = new Paragraph();
        p2.add(new TextLine(f2, "Introduction"));

        Paragraph p3 = new Paragraph();
        // p3.setAlignment(Align.LEFT);
        // p3.setAlignment(Align.RIGHT);
        p3.setAlignment(Align.JUSTIFY);
        TextLine text = new TextLine(f1,
"The Swiss Confederation was founded in 1291 as a defensive alliance among three cantons. In succeeding years, other localities joined the original three. The Swiss Confederation secured its independence from the Holy Roman Empire in 1499. Switzerland's sovereignty and neutrality have long been honored by the major European powers, and the country was not involved in either of the two World Wars. The political and economic integration of Europe over the past half century, as well as Switzerland's role in many UN and international organizations, has strengthened Switzerland's ties with its neighbors. However, the country did not officially become a UN member until 2002.");
        p3.add(text);

        text = new TextLine(f1,
"Switzerland remains active in many UN and international organizations but retains a strong commitment to neutrality.");
        text.setColor(Color.red);
        p3.add(text);

        Paragraph p4 = new Paragraph();
        p4.add(new TextLine(f3, "Economy"));

        Paragraph p5 = new Paragraph();
        p5.setAlignment(Align.JUSTIFY);
        text = new TextLine(f1,
"Switzerland is a peaceful, prosperous, and stable modern market economy with low unemployment, a highly skilled labor force, and a per capita GDP larger than that of the big Western European economies. The Swiss in recent years have brought their economic practices largely into conformity with the EU's to enhance their international competitiveness. Switzerland remains a safehaven for investors, because it has maintained a degree of bank secrecy and has kept up the franc's long-term external value. Reflecting the anemic economic conditions of Europe, GDP growth stagnated during the 2001-03 period, improved during 2004-05 to 1.8% annually and to 2.9% in 2006.");
        p5.add(text);

        text = new TextLine(f4,
"Even so, unemployment has remained at less than half the EU average.");
        text.setColor(Color.blue);
        p5.add(text);

        Paragraph p6 = new Paragraph();
        p6.setAlignment(Align.RIGHT);

        text = new TextLine(f1,
"Source: The world fact book.");
        text.setColor(Color.blue);
        text.setURIAction("https://www.cia.gov/library/publications/the-world-factbook/geos/sz.html");
        p6.add(text);

        column.addParagraph(p1);
        column.addParagraph(p2);
        column.addParagraph(p3);
        column.addParagraph(p4);
        column.addParagraph(p5);
        column.addParagraph(p6);

        if (rotate == 0) {
            column.setLocation(90f, 300f);
        }
        else if (rotate == 90) {
            column.setLocation(90f, 780f);
        }
        else if (rotate == 270) {
            column.setLocation(550f, 310f);
        }

        float columnWidth = 470f; 
        column.setSize(columnWidth, 100f);
        Point point = column.drawOn(page);

        if (rotate == 0) {
            Line line = new Line(
                    point.getX(),
                    point.getY(),
                    point.getX() + columnWidth,
                    point.getY());
            line.drawOn(page);
        }

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_10();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_10.java
