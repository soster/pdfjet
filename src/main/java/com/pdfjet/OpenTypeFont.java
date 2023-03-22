/**
 *  OpenTypeFont.java
 *
Copyright 2023 Innovatics Inc.
*/
package com.pdfjet;

import java.io.*;
import java.util.*;


class OpenTypeFont {
    protected static void register(
            PDF pdf, Font font, InputStream inputStream) throws Exception {
        OTF otf = new OTF(inputStream);

        font.name = otf.fontName;
        font.firstChar = otf.firstChar;
        font.lastChar = otf.lastChar;
        font.unicodeToGID = otf.unicodeToGID;
        font.unitsPerEm = otf.unitsPerEm;
        font.bBoxLLx = otf.bBoxLLx;
        font.bBoxLLy = otf.bBoxLLy;
        font.bBoxURx = otf.bBoxURx;
        font.bBoxURy = otf.bBoxURy;
        font.advanceWidth = otf.advanceWidth;
        font.glyphWidth = otf.glyphWidth;
        font.fontAscent = otf.ascent;
        font.fontDescent = otf.descent;
        font.fontUnderlinePosition = otf.underlinePosition;
        font.fontUnderlineThickness = otf.underlineThickness;
        font.setSize(font.size);

        embedFontFile(pdf, font, otf);
        addFontDescriptorObject(pdf, font, otf);
        addCIDFontDictionaryObject(pdf, font, otf);
        addToUnicodeCMapObject(pdf, font, otf);

        // Type0 Font Dictionary
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type0\n");
        pdf.append("/BaseFont /");
        pdf.append(otf.fontName);
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


    private static void embedFontFile(PDF pdf, Font font, OTF otf) throws Exception {
        // Check if the font file is already embedded
        for (Font f : pdf.fonts) {
            if (f.fileObjNumber != 0 && f.name.equals(otf.fontName)) {
                font.fileObjNumber = f.fileObjNumber;
                return;
            }
        }

        int metadataObjNumber = pdf.addMetadataObject(otf.fontInfo, true);

        pdf.newobj();
        pdf.append("<<\n");
        if (otf.cff) {
            pdf.append("/Subtype /CIDFontType0C\n");
        }
        pdf.append("/Filter /FlateDecode\n");

        pdf.append("/Length ");
        pdf.append(otf.baos.size());    // The compressed size
        pdf.append("\n");

        if (!otf.cff) {
            pdf.append("/Length1 ");
            pdf.append(otf.buf.length); // The uncompressed size
            pdf.append('\n');
        }

        if (metadataObjNumber != -1) {
            pdf.append("/Metadata ");
            pdf.append(metadataObjNumber);
            pdf.append(" 0 R\n");
        }

        pdf.append(">>\n");
        pdf.append("stream\n");
        pdf.append(otf.baos);
        pdf.append("\nendstream\n");
        pdf.endobj();

        font.fileObjNumber = pdf.getObjNumber();
    }


    private static void addFontDescriptorObject(
            PDF pdf, Font font, OTF otf) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.fontDescriptorObjNumber != 0 && f.name.equals(otf.fontName)) {
                font.fontDescriptorObjNumber = f.fontDescriptorObjNumber;
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /FontDescriptor\n");
        pdf.append("/FontName /");
        pdf.append(otf.fontName);
        pdf.append('\n');
        if (otf.cff) {
            pdf.append("/FontFile3 ");
        }
        else {
            pdf.append("/FontFile2 ");
        }
        pdf.append(font.fileObjNumber);
        pdf.append(" 0 R\n");
        pdf.append("/Flags 32\n");
        pdf.append("/FontBBox [");
        pdf.append(otf.bBoxLLx);
        pdf.append(' ');
        pdf.append(otf.bBoxLLy);
        pdf.append(' ');
        pdf.append(otf.bBoxURx);
        pdf.append(' ');
        pdf.append(otf.bBoxURy);
        pdf.append("]\n");
        pdf.append("/Ascent ");
        pdf.append(otf.ascent);
        pdf.append('\n');
        pdf.append("/Descent ");
        pdf.append(otf.descent);
        pdf.append('\n');
        pdf.append("/ItalicAngle 0\n");
        pdf.append("/CapHeight ");
        pdf.append(otf.capHeight);
        pdf.append('\n');
        pdf.append("/StemV 79\n");
        pdf.append(">>\n");
        pdf.endobj();

        font.fontDescriptorObjNumber = pdf.getObjNumber();
    }


    private static void addToUnicodeCMapObject(
            PDF pdf,
            Font font,
            OTF otf) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.toUnicodeCMapObjNumber != 0 && f.name.equals(otf.fontName)) {
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
            int gid = otf.unicodeToGID[cid];
            if (gid > 0) {
                buf.append('<');
                buf.append(toHexString(gid));
                buf.append("> <");
                buf.append(toHexString(cid));
                buf.append(">\n");
                list.add(buf.toString());
                buf.setLength(0);
                if (list.size() == 100) {
                    writeListToBuffer(list, sb);
                }
            }
        }
        if (list.size() > 0) {
            writeListToBuffer(list, sb);
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


    private static void addCIDFontDictionaryObject(
            PDF pdf,
            Font font,
            OTF otf) throws Exception {
        for (Font f : pdf.fonts) {
            if (f.cidFontDictObjNumber != 0 && f.name.equals(otf.fontName)) {
                font.cidFontDictObjNumber = f.cidFontDictObjNumber;
                return;
            }
        }

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        if (otf.cff) {
            pdf.append("/Subtype /CIDFontType0\n");
        }
        else {
            pdf.append("/Subtype /CIDFontType2\n");
        }
        pdf.append("/BaseFont /");
        pdf.append(otf.fontName);
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


    private static String toHexString(int code) {
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


    private static void writeListToBuffer(List<String> list, StringBuilder sb) {
        sb.append(list.size());
        sb.append(" beginbfchar\n");
        for (String str : list) {
            sb.append(str);
        }
        sb.append("endbfchar\n");
        list.clear();
    }

}   // End of OpenTypeFont.java
