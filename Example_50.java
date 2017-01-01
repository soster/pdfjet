import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_50.java
 *
 */
public class Example_50 {

    public Example_50() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_50.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(10f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        // Columns x coordinates
        float x1 = 75f;
        float x2 = 325f;

        float y1 = 75f;

        // Width of the second column:
        float w2 = 200f;

        Image image1 = new Image(
                pdf,
                getClass().getResourceAsStream("images/fruit.jpg"),
                ImageType.JPG);
        image1.setLocation(x1, y1);
        image1.scaleBy(0.75f);
        image1.drawOn(page);


        TextBlock textBlock = new TextBlock(f1);
        textBlock.setText("Donec a urna ac ipsum fringilla ultricies non vel diam. Morbi vitae lacus ac elit luctus dignissim. Quisque rutrum egestas facilisis. Curabitur tempus, tortor ac fringilla fringilla, libero elit gravida sem, vel aliquam leo nibh sed libero. Proin pretium, augue quis eleifend hendrerit, leo libero auctor magna,\n\nvitae porttitor lorem urna eget urna. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut tincidunt venenatis odio in dignissim. Ut cursus egestas eros, ac blandit nisi ullamcorper a.");
        textBlock.setLocation(x2, y1);
        textBlock.setWidth(w2);
        // textBlock.setTextAlignment(Align.LEFT);
        // textBlock.setTextAlignment(Align.RIGHT);
        // textBlock.setTextAlignment(Align.CENTER);

        // We can find out the height of the block before we draw it!
        float y2 = y1 + textBlock.getHeight();

        // Diagnostics Code:
        // Line line = new Line(x2, y2, x2 + w2, y2);
        // line.setWidth(0.2f);
        // line.drawOn(page);

        y2 += 10f;   // Add a bit of space between the rows.

        textBlock.drawOn(page);


        // Draw the second row image and text:
        Image image2 = new Image(
                pdf,
                getClass().getResourceAsStream("images/eu-map.png"),
                ImageType.PNG);
        image2.setLocation(x1, y2);
        image2.scaleBy(0.5f);
        image2.drawOn(page);

        textBlock = new TextBlock(f1);
        textBlock.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla elementum interdum elit, quis vehicula urna interdum quis. Phasellus gravida ligula quam, nec blandit nulla. Sed posuere, lorem eget feugiat placerat, ipsum nulla euismod nisi, in semper mi nibh sed elit. Mauris libero est, sodales dignissim congue sed, pulvinar non ipsum. Sed risus nisi, ultrices nec eleifend at, viverra sed neque. Integer vehicula massa non arcu viverra ullamcorper. Ut id tellus id ante mattis commodo. Donec dignissim aliquam tortor, eu pharetra ipsum ullamcorper in. Vivamus ultrices imperdiet iaculis.\n\n");
        textBlock.setWidth(w2);
        textBlock.setLocation(x2, y2);
        textBlock.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_50();
    }

}   // End of Example_50.java
