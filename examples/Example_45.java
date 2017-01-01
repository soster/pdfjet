import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_45.java
 *
 */
public class Example_45 {

    public Example_45() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_45.pdf")),
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

        TextLine text = new TextLine(f1);
        text.setLocation(70f, 70f);
        text.setText("Hasta la vista!");
        text.setLanguage("es-MX");
        text.setStrikeout(true);
        text.setUnderline(true);
        text.setURIAction("http://pdfjet.com");
        text.drawOn(page);

        text = new TextLine(f1);
        text.setLocation(70f, 90f);
        text.setText("416-335-7718");
        text.setURIAction("http://pdfjet.com");
        text.drawOn(page);

        text = new TextLine(f1);
        text.setLocation(70f, 120f);
        text.setText("2014-11-25");
        text.drawOn(page);

        List<Paragraph> paragraphs = new ArrayList<Paragraph>();

        Paragraph paragraph = new Paragraph()
                .add(new TextLine(f1,
"The centres also offer free one-on-one consultations with business advisors who can review your business plan and make recommendations to improve it. The small business centres offer practical resources, from step-by-step info on setting up your business to sample business plans to a range of business-related articles and books in our resource libraries."))
                .add(new TextLine(f2,
"This text is blue color and is written using italic font.")
                        .setColor(Color.blue));

        paragraphs.add(paragraph);

        Text textArea = new Text(paragraphs);
        textArea.setLocation(70f, 150f);
        textArea.setWidth(500f);
        textArea.drawOn(page);

        float[] xy = (new PlainText(f2, new String[] {
"The Fibonacci sequence is named after Fibonacci.",
"His 1202 book Liber Abaci introduced the sequence to Western European mathematics,",
"although the sequence had been described earlier in Indian mathematics.",
"By modern convention, the sequence begins either with F0 = 0 or with F1 = 1.",
"The Liber Abaci began the sequence with F1 = 1, without an initial 0.",
"",
"Fibonacci numbers are closely related to Lucas numbers in that they are a complementary pair",
"of Lucas sequences. They are intimately connected with the golden ratio;",
"for example, the closest rational approximations to the ratio are 2/1, 3/2, 5/3, 8/5, ... .",
"Applications include computer algorithms such as the Fibonacci search technique and the",
"Fibonacci heap data structure, and graphs called Fibonacci cubes used for interconnecting",
"parallel and distributed systems. They also appear in biological settings, such as branching",
"in trees, phyllotaxis (the arrangement of leaves on a stem), the fruit sprouts of a pineapple,",
"the flowering of an artichoke, an uncurling fern and the arrangement of a pine cone.",
                })
                .setLocation(70f, 370f)
                .setFontSize(10f)
                .drawOn(page));

        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);

        page = new Page(pdf, Letter.PORTRAIT);

        text = new TextLine(f1);
        text.setLocation(70f, 120f);
        text.setText("416-877-1395");
        text.drawOn(page);

        Line line = new Line(70f, 150f, 300f, 150f);
        line.setWidth(1f);
        line.setColor(Color.oldgloryred);
        line.setAltDescription("This is a red line.");
        line.setActualText("This is a red line.");
        line.drawOn(page);

        box = new Box();
        box.setLineWidth(1f);
        box.setLocation(70f, 200f);
        box.setSize(100f, 100f);
        box.setColor(Color.oldgloryblue);
        box.setAltDescription("This is a blue box.");
        box.setActualText("This is a blue box.");
        box.drawOn(page);

        page.addBMC("Span", "This is a test", "This is a test");
        page.drawString(f1, "This is a test", 75f, 230f);
        page.addEMC();

        Image image = new Image(
                pdf,
                getClass().getResourceAsStream("images/fruit.jpg"),
                ImageType.JPG);
        image.setLocation(70f, 310f);
        image.scaleBy(0.5f);
        image.setAltDescription("This is an image of a strawberry.");
        image.setActualText("This is an image of a strawberry.");
        image.drawOn(page);

        float w = 530f;
        float h = 13f;

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field(   0f, new String[] {"Company", "Smart Widget Designs"}));
        fields.add(new Field(   0f, new String[] {"Street Number", "120"}));
        fields.add(new Field(  w/8, new String[] {"Street Name", "Oak"}));
        fields.add(new Field(5*w/8, new String[] {"Street Type", "Street"}));
        fields.add(new Field(6*w/8, new String[] {"Direction", "West"}));
        fields.add(new Field(7*w/8, new String[] {"Suite/Floor/Apt.", "8W"})
                .setAltDescription("Suite/Floor/Apartment")
                .setActualText("Suite/Floor/Apartment"));
        fields.add(new Field(   0f, new String[] {"City/Town", "Toronto"}));
        fields.add(new Field(  w/2, new String[] {"Province", "Ontario"}));
        fields.add(new Field(7*w/8, new String[] {"Postal Code", "M5M 2N2"}));
        fields.add(new Field(   0f, new String[] {"Telephone Number", "(416) 331-2245"}));
        fields.add(new Field(  w/4, new String[] {"Fax (if applicable)", "(416) 124-9879"}));
        fields.add(new Field(  w/2, new String[] {"Email","jsmith12345@gmail.ca"}));
        fields.add(new Field(   0f, new String[] {
                "Other Information","We don't work on weekends.", "Please send us an Email."}));

        new Form(fields)
                .setLabelFont(f1)
                .setLabelFontSize(7f)
                .setValueFont(f2)
                .setValueFontSize(9f)
                .setLocation(70f, 490f)
                .setRowLength(w)
                .setRowHeight(h)
                .drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_45();
    }

}   // End of Example_45.java
