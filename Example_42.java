import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_42.java
 *
 */
public class Example_42 {

    public Example_42() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_42.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA);

        Page page = new Page(pdf, Letter.PORTRAIT);

        float w = 500f;
        float h = 13f;

        List<Field> fields = new ArrayList<Field>();
        fields.add(new Field(   0f, new String[] {"Company", "Smart Widgets Construction Inc."}));
        fields.add(new Field(   0f, new String[] {"Street Number", "120"}));
        fields.add(new Field(  w/8, new String[] {"Street Name", "Oak"}));
        fields.add(new Field(5*w/8, new String[] {"Street Type", "Street"}));
        fields.add(new Field(6*w/8, new String[] {"Direction", "West"}));
        fields.add(new Field(7*w/8, new String[] {"Suite/Floor/Apt.", "8W"}));
        fields.add(new Field(   0f, new String[] {"City/Town", "Toronto"}));
        fields.add(new Field(  w/2, new String[] {"Province", "Ontario"}));
        fields.add(new Field(7*w/8, new String[] {"Postal Code", "M5M 2N2"}));
        fields.add(new Field(   0f, new String[] {"Telephone Number", "(416) 331-2245"}));
        fields.add(new Field(  w/4, new String[] {"Fax (if applicable)", "(416) 124-9879"}));
        fields.add(new Field(  w/2, new String[] {"Email","jsmith12345@gmail.ca"}));
        fields.add(new Field(   0f, new String[] {"Other Information","", ""}));

        float[] xy = (new Form(fields)
                .setLabelFont(f1)
                .setLabelFontSize(8f)
                .setValueFont(f2)
                .setValueFontSize(10f)
                .setLocation(70f, 90f)
                .setRowLength(w)
                .setRowHeight(h)
                .drawOn(page));
/*
        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);
*/
        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_42();
    }

}   // End of Example_42.java
