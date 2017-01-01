import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.pdfjet.*;


/**
 *  Example_19.java
 *
 */
class Example_19 {

    public Example_19(String fileNumber, String fileName) throws Exception {

        PDF pdf = new PDF(new BufferedOutputStream(
                new FileOutputStream("Example_19_" + fileNumber + ".pdf")));

        BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream(fileName));
        Map<Integer, PDFobj> objects = pdf.read(bis);
        bis.close();

        List<PDFobj> pages = pdf.getPageObjects(objects);

        Page page = new Page(pdf, objects, pages.get(0));
        Font f1 = page.addFontResource(CoreFont.HELVETICA).setSize(14f);
        Font f2 = page.addFontResource(CoreFont.COURIER).setSize(14f);
        page.setPenColor(Color.blue);
        page.setPenWidth(1.5f);
        page.drawRect(250f, 210f, 50f, 50f);
        page.setBrushColor(Color.green);
        page.drawString(f1, "Hello, World!", 300f, 300f);
        page.setBrushColor(Color.blue);
        page.drawString(f2, "This is a test.", 300f, 350f);
        page.complete();

        pdf.addObjects(objects);

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_19("00", "Example_07.pdf");
    }

}   // End of Example_19.java
