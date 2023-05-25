/**
 *  OptimizePNG.java
 *
Copyright 2023 Innovatics Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.pdfjet;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.InflaterInputStream;

/**
 * This program optimizes .png images by converting them to
 * .png.stream images that can be embedded much faster in PDFs
 * that the original images.
 */
public class OptimizePNG {
    /**
     * Converts font TTF or OTF file to .ttf.stream .otf.stream
     *
     * @param args the arguments
     * @throws Exception if the image file is not found
     */
    public static void main(String[] args) throws Exception {
        FileInputStream fis = new FileInputStream(args[0]);
        PNGImage png = new PNGImage(fis);
        byte[] image = png.getData();
        byte[] alpha = png.getAlpha();
        int w = png.getWidth();
        int h = png.getHeight();
        int c = png.getColorType();
        fis.close();

        ByteArrayOutputStream inflatedImage = new ByteArrayOutputStream();
        InflaterInputStream iis =
                new InflaterInputStream(new ByteArrayInputStream(image));
        int ch = 0;
        while ((ch = iis.read()) != -1) {
            inflatedImage.write(ch);
        }
        iis.close();

        ByteArrayOutputStream inflatedAlpha = new ByteArrayOutputStream();
        if (alpha != null) {
            iis = new InflaterInputStream(new ByteArrayInputStream(alpha));
            ch = 0;
            while ((ch = iis.read()) != -1) {
                inflatedAlpha.write(ch);
            }
            iis.close();
        }

        byte[] deflatedImage =
                compressWithZopfli(inflatedImage.toByteArray(), args[0]);
        byte[] deflatedAlpha = null;
        if (alpha != null) {
            deflatedAlpha = compressWithZopfli(inflatedAlpha.toByteArray(), args[0]);
        }

        BufferedOutputStream bos =
                new BufferedOutputStream(
                        new FileOutputStream(args[0] + ".stream"));
        writeInt32(w, bos); // Width
        writeInt32(h, bos); // Height
        bos.write(c);       // Color Space
        if (deflatedAlpha != null) {
            bos.write(1);
            writeInt32(deflatedAlpha.length, bos);
            bos.write(deflatedAlpha);
        } else {
            bos.write(0);
        }
        writeInt32(deflatedImage.length, bos);
        bos.write(deflatedImage);
        bos.flush();
        bos.close();
    }

    private static byte[] compressWithZopfli(
            byte[] buffer, String fileName) throws IOException {
        BufferedOutputStream bos =
                new BufferedOutputStream(
                        new FileOutputStream(fileName + ".tmp"));
        bos.write(buffer, 0, buffer.length);
        bos.close();

        final List<String> command = new ArrayList<String>();
        command.add("util/zopfli/zopfli");
        command.add("-c");
        command.add("--zlib");
        command.add("--i100");
        command.add(fileName + ".tmp");
        final Process process = new ProcessBuilder(command).start();
        final InputStream input = process.getInputStream();

        ByteArrayOutputStream baos = new ByteArrayOutputStream(0xFFFF);
        final byte[] buf = new byte[4096];
        int len;
        while ((len = input.read(buf)) != -1) {
            baos.write(buf, 0, len);
        }
        input.close();
        new File(fileName + ".tmp").delete();
        return baos.toByteArray();
    }

    private static void writeInt32(int i, OutputStream os) throws IOException {
        os.write((i >> 24) & 0xff);
        os.write((i >> 16) & 0xff);
        os.write((i >>  8) & 0xff);
        os.write((i >>  0) & 0xff);
    }
}   // End of OptimizePNG.java
