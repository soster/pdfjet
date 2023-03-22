/**
 *  FontStream1.java
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
import java.util.*;


class FontStream1 {

    protected static void register(
            PDF pdf,
            Font font,
            InputStream inputStream) throws Exception {
        getFontData(font, inputStream);
        // System.out.println(font.fontUnderlineThickness);

        embedFontFile(pdf, font, inputStream);
        addFontDescriptorObject(pdf, font);
        addCIDFontDictionaryObject(pdf, font);
        addToUnicodeCMapObject(pdf, font);

        // Type0 Font Dictionary
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type0\n");
        pdf.append("/BaseFont /");
        pdf.append(font.name);
        pdf.append('\n');
        pdf.append("/Encoding /Identity-H\n");
        pdf.append("/DescendantFonts [");
        pdf.append(font.cidFontDictObjNumber);
        pdf.append(" 0 R]\n");
        pdf.append("/ToUnicode ");
        pdf.append(font.toUnicodeCMapObjNumber);
        pdf.append(" 0 R\n");
        pdf.append(">>\n");
        pdf.endobj();
        font.objNumber = pdf.getObjNumber();
        pdf.fonts.add(font);
    }


    private static void embedFontFile(
            PDF pdf, Font font, InputStream inputStream) throws Exception {
        // Check if the font file is already embedded
        for (Font f : pdf.fonts) {
            if (f.fileObjNumber != 0 && f.name.equals(font.name)) {
                font.fileObjNumber = f.fileObjNumber;
                return;
            }
        }

        int metadataObjNumber = pdf.addMetadataObject(font.info, true);

        pdf.newobj();
        pdf.append("<<\n");

        pdf.append("/Metadata ");
        pdf.append(metadataObjNumber);
        pdf.append(" 0 R\n");

        if (font.cff) {
            pdf.append("/Subtype /CIDFontType0C\n");
        }
        pdf.append("/Filter /FlateDecode\n");
        pdf.append("/Length ");
        pdf.append(font.compressedSize);
        pdf.append("\n");

        if (!font.cff) {
            pdf.append("/Length1 ");
            pdf.append(font.uncompressedSize);
            pdf.append('\n');
        }

        pdf.append(">>\n");
        pdf.append("stream\n");
        byte[] buf = new byte[4096];
        int len;
        while ((len = inputStream.read(buf, 0, buf.length)) > 0) {
            pdf.append(buf, 0, len);
        }
        inputStream.close();
        pdf.append("\nendstream\n");
        pdf.endobj();

        font.fileObjNumber = pdf.getObjNumber();
    }


    private static void addFontDescriptorObject(PDF pdf, Font font) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.fontDescriptorObjNumber != 0 && f.name.equals(font.name)) {
                font.fontDescriptorObjNumber = f.fontDescriptorObjNumber;
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /FontDescriptor\n");
        pdf.append("/FontName /");
        pdf.append(font.name);
        pdf.append('\n');
        if (font.cff) {
            pdf.append("/FontFile3 ");
        }
        else {
            pdf.append("/FontFile2 ");
        }
        pdf.append(font.fileObjNumber);
        pdf.append(" 0 R\n");
        pdf.append("/Flags 32\n");
        pdf.append("/FontBBox [");
        pdf.append(font.bBoxLLx);
        pdf.append(' ');
        pdf.append(font.bBoxLLy);
        pdf.append(' ');
        pdf.append(font.bBoxURx);
        pdf.append(' ');
        pdf.append(font.bBoxURy);
        pdf.append("]\n");
        pdf.append("/Ascent ");
        pdf.append(font.fontAscent);
        pdf.append('\n');
        pdf.append("/Descent ");
        pdf.append(font.fontDescent);
        pdf.append('\n');
        pdf.append("/ItalicAngle 0\n");
        pdf.append("/CapHeight ");
        pdf.append(font.capHeight);
        pdf.append('\n');
        pdf.append("/StemV 79\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.fontDescriptorObjNumber = pdf.getObjNumber();
    }


    private static void addToUnicodeCMapObject(PDF pdf, Font font) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.toUnicodeCMapObjNumber != 0 && f.name.equals(font.name)) {
                font.toUnicodeCMapObjNumber = f.toUnicodeCMapObjNumber;
                return;
            }
        }

        StringBuilder sb = new StringBuilder();

        sb.append("/CIDInit /ProcSet findresource begin\n");
        sb.append("12 dict begin\n");
        sb.append("begincmap\n");
        sb.append("/CIDSystemInfo <</Registry (Adobe) /Ordering (Identity) /Supplement 0>> def\n");
        sb.append("/CMapName /Adobe-Identity def\n");
        sb.append("/CMapType 2 def\n");

        sb.append("1 begincodespacerange\n");
        sb.append("<0000> <FFFF>\n");
        sb.append("endcodespacerange\n");

        List<String> list = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (int cid = 0; cid <= 0xffff; cid++) {
            int gid = font.unicodeToGID[cid];
            if (gid > 0) {
                buf.append('<');
                buf.append(toHexString(gid));
                buf.append("> <");
                buf.append(toHexString(cid));
                buf.append(">\n");
                list.add(buf.toString());
                buf.setLength(0);
                if (list.size() == 100) {
                    writeListToBuffer(sb, list);
                }
            }
        }
        if (list.size() > 0) {
            writeListToBuffer(sb, list);
        }

        sb.append("endcmap\n");
        sb.append("CMapName currentdict /CMap defineresource pop\n");
        sb.append("end\nend");

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Length ");
        pdf.append(sb.length());
        pdf.append("\n");
        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(sb.toString());
        pdf.append("\nendstream\n");
        pdf.endobj();

        font.toUnicodeCMapObjNumber = pdf.getObjNumber();
    }


    private static void addCIDFontDictionaryObject(PDF pdf, Font font) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.cidFontDictObjNumber != 0 && f.name.equals(font.name)) {
                font.cidFontDictObjNumber = f.cidFontDictObjNumber;
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        if (font.cff) {
            pdf.append("/Subtype /CIDFontType0\n");
        }
        else {
            pdf.append("/Subtype /CIDFontType2\n");
        }
        pdf.append("/BaseFont /");
        pdf.append(font.name);
        pdf.append('\n');
        pdf.append("/CIDSystemInfo <</Registry (Adobe) /Ordering (Identity) /Supplement 0>>\n");
        pdf.append("/FontDescriptor ");
        pdf.append(font.fontDescriptorObjNumber);
        pdf.append(" 0 R\n");

        final float k = 1000.0f / Float.valueOf(font.unitsPerEm);
        pdf.append("/DW ");
        pdf.append(Math.round(k * Float.valueOf(font.advanceWidth[0])));
        pdf.append('\n');

        pdf.append("/W [0[\n");
        for (int i = 0; i < font.advanceWidth.length; i++) {
            pdf.append(Math.round(k * Float.valueOf(font.advanceWidth[i])));
            pdf.append(' ');
        }
        pdf.append("]]\n");

        pdf.append("/CIDToGIDMap /Identity\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.cidFontDictObjNumber = pdf.getObjNumber();
    }


    protected static String toHexString(int code) {
        String str = Integer.toHexString(code);
        if (str.length() == 1) {
            return "000" + str;
        }
        else if (str.length() == 2) {
            return "00" + str;
        }
        else if (str.length() == 3) {
            return "0" + str;
        }
        return str;
    }


    protected static void writeListToBuffer(StringBuilder sb, List<String> list) {
        sb.append(list.size());
        sb.append(" beginbfchar\n");
        for (String str : list) {
            sb.append(str);
        }
        sb.append("endbfchar\n");
        list.clear();
    }


    private static int getInt16(InputStream stream) throws Exception {
        return stream.read() << 8 | stream.read();
    }


    private static int getInt24(InputStream stream) throws Exception {
        return stream.read() << 16 |
                stream.read() << 8 | stream.read();
    }


    private static int getInt32(InputStream stream) throws Exception {
        return stream.read() << 24 | stream.read() << 16 |
                stream.read() << 8 | stream.read();
    }


    protected static void getFontData(Font font, InputStream inputStream) throws Exception {
        int len = inputStream.read();
        byte[] fontName = new byte[len];
        inputStream.read(fontName, 0, len);
        font.name = new String(fontName, "UTF-8");

        len = getInt24(inputStream);
        byte[] fontInfo = new byte[len];
        inputStream.read(fontInfo, 0, len);
        font.info = new String(fontInfo, "UTF-8");

        byte[] buf = new byte[getInt32(inputStream)];
        inputStream.read(buf, 0, buf.length);
        ByteArrayInputStream stream =
                new ByteArrayInputStream(Decompressor.inflate(buf));

        font.unitsPerEm = getInt32(stream);
        font.bBoxLLx = getInt32(stream);
        font.bBoxLLy = getInt32(stream);
        font.bBoxURx = getInt32(stream);
        font.bBoxURy = getInt32(stream);
        font.fontAscent = getInt32(stream);
        font.fontDescent = getInt32(stream);
        font.firstChar = getInt32(stream);
        font.lastChar = getInt32(stream);
        font.capHeight = getInt32(stream);
        font.fontUnderlinePosition = getInt32(stream);
        font.fontUnderlineThickness = getInt32(stream);

        len = getInt32(stream);
        font.advanceWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.advanceWidth[i] = getInt16(stream);
        }

        len = getInt32(stream);
        font.glyphWidth = new int[len];
        for (int i = 0; i < len; i++) {
            font.glyphWidth[i] = getInt16(stream);
        }

        len = getInt32(stream);
        font.unicodeToGID = new int[len];
        for (int i = 0; i < len; i++) {
            font.unicodeToGID[i] = getInt16(stream);
        }

        font.cff = inputStream.read() == 'Y';
        font.uncompressedSize = getInt32(inputStream);
        font.compressedSize = getInt32(inputStream);
    }

}   // End of FontStream1.java
