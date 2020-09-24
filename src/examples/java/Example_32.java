import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_32.java
 *
 */
public class Example_32 {

    private Font f1;
    private float x = 50f;
    private float y = 50f;
    private float leading = 14f;

    public Example_32() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_32.pdf")));

        f1 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(10f);

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("Example_02.java"), "UTF-8"));

        String line = reader.readLine();
        Page page = null;
        while (line != null) {
            if (page == null) {
                y = 50f;
                page = newPage(pdf);
            }
            page.println(line);
            y += leading;
            if (y > (Letter.PORTRAIT[1] - 20f)) {
                page.setTextEnd();
                page = null;
            }
            line = reader.readLine();
        }
        if (page != null) {
            page.setTextEnd();
        }
        reader.close();

        pdf.close();
    }

    private Page newPage(PDF pdf) throws Exception {
        Page page = new Page(pdf, Letter.PORTRAIT);
        page.setTextStart();
        page.setTextFont(f1);
        page.setTextLocation(x, y);
        page.setTextLeading(leading);
        return page;
    }

    public static void main(String[] args) throws Exception {
        new Example_32();
    }

}   // End of Example_32.java
