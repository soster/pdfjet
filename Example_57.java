import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_57.java
 *
 */
public class Example_57 {

    public Example_57() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_57.pdf")),
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
        f2.setSize(9f);
        f2.setItalic(true);

        StringBuilder buf = new StringBuilder();
        BufferedReader reader =
                new BufferedReader(new FileReader("data/calculus.txt"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line);
            buf.append("\n");
        }
        reader.close();

        float w = 530f;
        float h = 13f;

        Page page = new Page(pdf, Letter.PORTRAIT);

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
        fields.add(new Field(  w/2, new String[] {"Email", "jsmith12345@gmail.ca"}));
        fields.add(new Field(   0f, new String[] {"Other Information", buf.toString()}, true));

        new Form(fields)
                .setLabelFont(f1)
                .setLabelFontSize(7f)
                .setValueFont(f2)
                .setValueFontSize(9f)
                .setLocation(50f, 60f)
                .setRowLength(w)
                .setRowHeight(h)
                .drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_57();
    }

}   // End of Example_57.java
