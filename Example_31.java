import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_31.java
 *
 */
public class Example_31 {

    public Example_31() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_31.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        String path = "fonts/Android/";
        Font f1 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSansDevanagari-Regular.ttf"));
        f1.setSize(15f);

        Font f2 = new Font(pdf,
                getClass().getResourceAsStream(path + "DroidSans.ttf"));
        f2.setSize(15f);

        StringBuilder buf = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream("data/marathi.txt"), "UTF-8"));
        String line = null;
        while ((line = reader.readLine()) != null) {
            buf.append(line + "\n");
        }
        reader.close();

        TextBox textBox = new TextBox(f1, buf.toString(), 500f, 300f);
        textBox.setFallbackFont(f2);
        textBox.setLocation(50f, 50f);
        textBox.setNoBorders();
        textBox.drawOn(page);

        String str = "असम के बाद UP में भी CM कैंडिडेट का ऐलान करेगी BJP?";
        TextLine textLine = new TextLine(f1, str);
        textLine.setFallbackFont(f2);
        textLine.setLocation(50f, 200f);
        textLine.drawOn(page);


        page.setPenColor(Color.blue);
        page.setBrushColor(Color.blue);
        page.fillRect(100f, 300f, 200f, 200f);

        GraphicsState gs = new GraphicsState();
        //  The current stroking alpha constant
        gs.set_CA(0.5f);
        //  The current nonstroking alpha constant
        gs.set_ca(0.5f);
        page.setGraphicsState(gs);

        page.setPenColor(Color.green);
        page.setBrushColor(Color.green);
        page.fillRect(150f, 350f, 200f, 200f);

        page.setPenColor(Color.red);
        page.setBrushColor(Color.red);
        page.fillRect(200, 400, 200f, 200f);

        // Reset the parameters to the default values
        page.setGraphicsState(new GraphicsState());

        page.setPenColor(Color.orange);
        page.setBrushColor(Color.orange);
        page.fillRect(250, 450, 200f, 200f);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_31();
    }

}   // End of Example_31.java
