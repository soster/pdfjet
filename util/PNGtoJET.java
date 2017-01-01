package util;

import java.io.*;
import java.util.*;

import com.pdfjet.*;


public class PNGtoJET {

    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream(args[0]);
        PNGImage png = new PNGImage(fis);
        byte[] image = png.getData();
        byte[] alpha = png.getAlpha();
        int w = png.getWidth();
        int h = png.getHeight();
        int c = png.getColorType();
        fis.close();

        String fileName = args[0].substring(0, args[0].lastIndexOf("."));
        FileOutputStream fos = new FileOutputStream(fileName + ".jet");
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        writeInt(w, bos);   // Width
        writeInt(h, bos);   // Height
        bos.write(c);       // Color Space
        if (alpha != null) {
            bos.write(1);
            writeInt(alpha.length, bos);
            bos.write(alpha);
        }
        else {
            bos.write(0);
        }
        writeInt(image.length, bos);
        bos.write(image);
        bos.flush();
        bos.close();
    }


    private static void writeInt(int i, OutputStream os) throws IOException {
        os.write((i >> 24) & 0xff);
        os.write((i >> 16) & 0xff);
        os.write((i >>  8) & 0xff);
        os.write((i >>  0) & 0xff);
    }

}   // End of PNGtoJET.java
