import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_20.java
 *
 *  This example program will print out all the fonts that are not embedded in the PDF file we read.
 */
class Example_20 {

    public Example_20(String fileName) throws Exception {

        try {

            PDF pdf = new PDF();

            BufferedInputStream bis =
                    new BufferedInputStream(
                            new FileInputStream(fileName));

            Map<Integer, PDFobj> objects = pdf.read(bis);
            bis.close();

            for (PDFobj obj : objects.values()) {
/*
Uncomment to print out all the objects in a textual form:
                List<String> dict = obj.getDict();
                for (int i = 0; i < dict.size(); i++) {
                    System.out.println(dict.get(i));
                }
                System.out.println();
*/
                String type = obj.getValue("/Type");
                if (type.equals("/Font")
                        && obj.getValue("/Subtype").equals("/Type0") == false
                        && obj.getValue("/FontDescriptor").equals("")) {

                    System.out.println("Non-EmbeddedFont -> "
                            + obj.getValue("/BaseFont").substring(1));
                }
                else if (type.equals("/FontDescriptor")) {
                    String fontFile = obj.getValue("/FontFile");
                    if (fontFile.equals("")) {
                        fontFile = obj.getValue("/FontFile2");
                    }
                    if (fontFile.equals("")) {
                        fontFile = obj.getValue("/FontFile3");
                    }

                    if (fontFile.equals("")) {
                        System.out.println("Non-Embedded Font -> "
                                + obj.getValue("/FontName").substring(1));
                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        if (args.length > 0) {
            try {
                new Example_20(args[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}   // End of Example_20.java
