import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_16.java
 *
 */
public class Example_16 {

    public Example_16() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_16.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(14f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Box bg_flag = new Box();
        bg_flag.setLocation(50f, 50f);
        bg_flag.setSize(200f, 120f);
        bg_flag.setColor(Color.black);
        bg_flag.setLineWidth(1.5f);
        bg_flag.drawOn(page);

        float stripe_width = 120f / 3;

        Line white_stripe = new Line(0f, 0f, 200f, 0f);
        white_stripe.setWidth(stripe_width);
        white_stripe.setColor(Color.white);
        white_stripe.placeIn(bg_flag, 0f, stripe_width / 2);
        white_stripe.drawOn(page);

        Line green_stripe = new Line(0f, 0f, 200f, 0f);
        green_stripe.setWidth(stripe_width);
        green_stripe.setColor(0x00966e);
        green_stripe.placeIn(bg_flag, 0f, (3 * stripe_width) / 2);
        green_stripe.drawOn(page);

        Line red_stripe = new Line(0f, 0f, 200f, 0f);
        red_stripe.setWidth(stripe_width);
        red_stripe.setColor(0xd62512);
        red_stripe.placeIn(bg_flag, 0f, (5 * stripe_width) / 2);
        red_stripe.drawOn(page);

        Map<String, Integer> colors = new HashMap<String, Integer>();
        colors.put("Lorem", Color.blue);
        colors.put("ipsum", Color.red);
        colors.put("dolor", Color.green);
        colors.put("ullamcorper", Color.gray);

        StringBuilder buf = new StringBuilder();
        buf.append("    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla elementum interdum elit, quis vehicula urna interdum quis. Phasellus gravida ligula quam, nec blandit nulla. Sed posuere, lorem eget feugiat placerat, ipsum nulla euismod nisi, in semper mi nibh sed elit. Mauris libero est, sodales dignissim congue sed, pulvinar non ipsum. Sed risus nisi, ultrices nec eleifend at, viverra sed neque. Integer vehicula massa non arcu viverra ullamcorper. Ut id tellus id ante mattis commodo. Donec dignissim aliquam tortor, eu pharetra ipsum ullamcorper in. Vivamus ultrices imperdiet iaculis.\n\n");

        buf.append("    Donec a urna ac ipsum fringilla ultricies non vel diam. Morbi vitae lacus ac elit luctus dignissim. Quisque rutrum egestas facilisis. Curabitur tempus, tortor ac fringilla fringilla, libero elit gravida sem, vel aliquam leo nibh sed libero. Proin pretium, augue quis eleifend hendrerit, leo libero auctor magna, vitae porttitor lorem urna eget urna. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut tincidunt venenatis odio in dignissim. Ut cursus egestas eros, ac blandit nisi ullamcorper a.");

        TextBox textBox = new TextBox(f1, buf.toString());
        textBox.setLocation(50f, 200f);
        textBox.setWidth(400f);

        // If no height is specified the height will be calculated based on the text.
        // textBox.setHeight(363f);
        // textBox.setHeight(362f);
        // textBox.setVerticalAlignment(Align.TOP);
        // textBox.setVerticalAlignment(Align.BOTTOM);
        // textBox.setVerticalAlignment(Align.CENTER);

        textBox.setTextColors(colors);

        // Find x and y without actually drawing the text box.
        // float[] xy = textBox.drawOn(page, false);
        float[] xy = textBox.drawOn(page);

        Box box = new Box();
        box.setLocation(xy[0], xy[1]);
        box.setSize(20f, 20f);
        box.drawOn(page);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_16();
    }

}   // End of Example_16.java
