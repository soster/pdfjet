/**
 *  OptimizeOTF.java
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
import java.util.zip.*;

/**
 * This program optimizes .otf and .ttf fonts by converting them to
 * .otf.stream and .ttf.stream fonts that can be embedded much faster in PDF
 * that the original fonts.
 */
public class OptimizeOTF {
    private static boolean useZopfli = true;

    /**
     * Converts font TTF or OTF file to .ttf.stream .otf.stream
     *
     * @param fileName the file name
     * @throws Exception if the font file is not found
     */
    public static void convertFontFile(String fileName) throws Exception {
        BufferedOutputStream fos =
                new BufferedOutputStream(new FileOutputStream(fileName + ".stream"));

        OTF otf = new OTF(new FileInputStream(fileName));
        byte[] name = otf.fontName.getBytes("UTF8");
        fos.write(name.length);
        fos.write(name);

        byte[] info = otf.fontInfo.getBytes("UTF8");
        writeInt24(info.length, fos);
        fos.write(info);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(32768);
        writeInt32(otf.unitsPerEm, baos);
        writeInt32(otf.bBoxLLx, baos);
        writeInt32(otf.bBoxLLy, baos);
        writeInt32(otf.bBoxURx, baos);
        writeInt32(otf.bBoxURy, baos);
        writeInt32(otf.ascent, baos);
        writeInt32(otf.descent, baos);
        writeInt32(otf.firstChar, baos);
        writeInt32(otf.lastChar, baos);
        writeInt32(otf.capHeight, baos);
        writeInt32(otf.underlinePosition, baos);
        writeInt32(otf.underlineThickness, baos);

        writeInt32(otf.advanceWidth.length, baos);
        for (int i = 0; i < otf.advanceWidth.length; i++) {
            writeInt16(otf.advanceWidth[i], baos);
        }

        writeInt32(otf.glyphWidth.length, baos);
        for (int i = 0; i < otf.glyphWidth.length; i++) {
            writeInt16(otf.glyphWidth[i], baos);
        }

        writeInt32(otf.unicodeToGID.length, baos);
        for (int i = 0; i < otf.unicodeToGID.length; i++) {
            writeInt16(otf.unicodeToGID[i], baos);
        }

        byte[] buf1 = baos.toByteArray();
        if (OptimizeOTF.useZopfli) {
            compressWithZopfli(fileName, fos, buf1, false);
        } else {
            ByteArrayOutputStream buf2 = new ByteArrayOutputStream(0xFFFF);
            DeflaterOutputStream dos1 =
                    new DeflaterOutputStream(
                            buf2, new Deflater(Deflater.BEST_COMPRESSION));
            dos1.write(buf1, 0, buf1.length);
            dos1.finish();
            writeInt32(buf2.size(), fos);
            buf2.writeTo(fos);
        }

        byte[] buf3 = otf.buf;
        if (otf.cff == true) {
            fos.write('Y');
            buf3 = new byte[otf.cffLen];
            for (int i = 0; i < otf.cffLen; i++) {
                buf3[i] = otf.buf[otf.cffOff + i];
            }
        } else {
            fos.write('N');
        }

        if (OptimizeOTF.useZopfli) {
            compressWithZopfli(fileName, fos, buf3, true);
        } else {
            ByteArrayOutputStream buf4 = new ByteArrayOutputStream(0xFFFF);
            DeflaterOutputStream dos =
                    new DeflaterOutputStream(buf4,
                            new Deflater(Deflater.BEST_COMPRESSION));
            dos.write(buf3, 0, buf3.length);
            dos.finish();
            writeInt32(buf3.length, fos);   // Uncompressed font size
            writeInt32(buf4.size(), fos);   // Compressed font size
            buf4.writeTo(fos);
        }
        fos.close();
    }

    private static void compressWithZopfli(
            String fileName,
            BufferedOutputStream fos,
            byte[] buf3,
            boolean uncompressed) throws IOException {
        BufferedOutputStream fos4 =
                new BufferedOutputStream(new FileOutputStream(fileName + ".tmp"));
        fos4.write(buf3, 0, buf3.length);
        fos4.close();
        final List<String> command = new ArrayList<String>();
        command.add("util/zopfli/zopfli");
        command.add("-c");
        command.add("--zlib");
        command.add("--i100");
        command.add(fileName + ".tmp");
        final Process process = new ProcessBuilder(command).start();
        final InputStream input = process.getInputStream();
        final byte[] buf = new byte[4096];
        ByteArrayOutputStream buf5 = new ByteArrayOutputStream(0xFFFF);
        int len;
        while ((len = input.read(buf)) != -1) {
            buf5.write(buf, 0, len);
        }
        if (uncompressed) {
            writeInt32(buf3.length, fos);   // Uncompressed font size
        }
        writeInt32(buf5.size(), fos);       // Compressed font size
        buf5.writeTo(fos);
        new File(fileName + ".tmp").delete();
    }

    private static void writeInt16(int i, OutputStream stream) throws IOException {
        stream.write((i >>  8) & 0xff);
        stream.write((i >>  0) & 0xff);
    }

    private static void writeInt24(int i, OutputStream stream) throws IOException {
        stream.write((i >> 16) & 0xff);
        stream.write((i >>  8) & 0xff);
        stream.write((i >>  0) & 0xff);
    }

    private static void writeInt32(int i, OutputStream stream) throws IOException {
        stream.write((i >> 24) & 0xff);
        stream.write((i >> 16) & 0xff);
        stream.write((i >>  8) & 0xff);
        stream.write((i >>  0) & 0xff);
    }

    /**
     * Entry point for the OptimizeOTF converter
     *
     * @param args the arguments
     * @throws Exception if there is a problem
     */
    public static void main(String[] args) throws Exception {
        File file = new File(args[0]);
        if (file.isDirectory()) {
            String path = file.getPath();
            String[] list = file.list();
            for (String fileName : list) {
                if (fileName.endsWith(".ttf") || fileName.endsWith(".otf")) {
                    System.out.println("Reading: " + fileName);
                    convertFontFile(path + File.separator + fileName);
                    System.out.println("Writing: " + fileName + ".stream");
                }
            }
        } else {
            System.out.println("Reading: " + args[0]);
            convertFontFile(args[0]);
            System.out.println("Writing: " + args[0] + ".stream");
        }
    }
}
