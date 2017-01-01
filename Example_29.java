import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_29.java
 */
public class Example_29 {

    public Example_29() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_29.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);
    
        Font font = new Font(pdf, CoreFont.HELVETICA);
        font.setSize(16f);
    
        Paragraph p = new Paragraph();
        p.add(new TextLine(font, "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla elementum interdum elit, quis vehicula urna interdum quis. Phasellus gravida ligula quam, nec blandit nulla. Sed posuere, lorem eget feugiat placerat, ipsum nulla euismod nisi, in semper mi nibh sed elit. Mauris libero est, sodales dignissim congue sed, pulvinar non ipsum. Sed risus nisi, ultrices nec eleifend at, viverra sed neque. Integer vehicula massa non arcu viverra ullamcorper. Ut id tellus id ante mattis commodo. Donec dignissim aliquam tortor, eu pharetra ipsum ullamcorper in. Vivamus ultrices imperdiet iaculis."));
    
        TextColumn column = new TextColumn();
        column.setLocation(10f, 0f);
        column.setSize(540f, 0f);
        // column.SetLineBetweenParagraphs(true);
        column.setLineBetweenParagraphs(false);
        column.addParagraph(p);
    
        Dimension dim0 = column.getSize();
        Point point1 = column.drawOn(page);
        Point point2 = column.drawOn(page, false);
        Dimension dim1 = column.getSize();
        Dimension dim2 = column.getSize();
        Dimension dim3 = column.getSize();
/*
        System.out.println("height0: " + dim0.getHeight());
        System.out.println("point1.x: " + point1.getX() + "    point1.y " + point1.getY());
        System.out.println("point2.x: " + point2.getX() + "    point2.y " + point2.getY());
        System.out.println("height1: " + dim1.getHeight());
        System.out.println("height2: " + dim2.getHeight());
        System.out.println("height3: " + dim3.getHeight());
        System.out.println();
*/
        column.removeLastParagraph();
        column.setLocation(10f, point2.getY() + 10f);
        p = new Paragraph();
        p.add(new TextLine(font, "Peter Blood, bachelor of medicine and several other things besides, smoked a pipe and tended the geraniums boxed on the sill of his window above Water Lane in the town of Bridgewater."));
        column.addParagraph(p);

        Dimension dim4 = column.getSize();
        Point point = column.drawOn(page);  // Draw the updated text column
/*
        System.out.println("point.x: " + point.getX() + "    point.y " + point.getY());
        System.out.println("height4: " + dim4.getHeight());
*/
        Box box = new Box();
        box.setLocation(point.getX(), point.getY() + 10f);
        box.setSize(540f, 25f);
        box.setLineWidth(2f);
        box.setColor(Color.darkblue);
        box.drawOn(page);
    
        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_29();
        }
        catch (Exception e) {
            System.out.println(e);
        }
    }

}   // End of Example_29.java
