import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_48.java
 *
 *  This example program will extract and write out to file all JPEG images embedded in PDF.
 */
class Example_48 {

    public Example_48(String fileName) throws Exception {

        try {
            PDF pdf = new PDF();

            BufferedInputStream bis =
                    new BufferedInputStream(new FileInputStream(fileName));
            Map<Integer, PDFobj> objects = pdf.read(bis);
            bis.close();

            int imageNumber = 1;
            for (PDFobj obj : objects.values()) {
                if (obj.getValue("/Type").equals("/XObject") &&
                        obj.getValue("/Subtype").equals("/Image") &&
                        obj.getValue("/Filter").equals("/DCTDecode")) {
                    FileOutputStream fos =
                            new FileOutputStream("image" + imageNumber + ".jpg");
                    fos.write(obj.getData());
                    fos.close();
                    imageNumber++;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) throws Exception {
        if (args.length > 0) {
            new Example_48(args[0]);
        }
    }

}   // End of Example_48.java
