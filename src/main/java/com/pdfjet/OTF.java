/**
 *  OTF.java
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
import java.util.zip.*;

/**
 * This class parses and extracts the data from TTF and OTF font files.
 */
public class OTF {
    String fontName;
    String fontInfo;
    ByteArrayOutputStream baos;
    int unitsPerEm;
    short bBoxLLx;
    short bBoxLLy;
    short bBoxURx;
    short bBoxURy;
    short ascent;
    short descent;
    int[] advanceWidth;
    int firstChar;
    int lastChar;
    short capHeight;
    int[] glyphWidth;
    long postVersion;
    long italicAngle;
    short underlinePosition;
    short underlineThickness;
    byte[] buf;
    boolean cff = false;
    int cffOff;
    int cffLen;
    int[] unicodeToGID = new int[0x10000];
    int index = 0;

    /**
     * Creates OTF object
     *
     * @param stream the input stream
     * @throws Exception if there is a problem
     */
    public OTF(InputStream stream) throws Exception {
        buf = Contents.getFromStream(stream);

        // Extract OTF metadata
        long version = readUInt32();
        if (version == 0x00010000L ||   // Win OTF
            version == 0x74727565L ||   // Mac TTF
            version == 0x4F54544FL) {   // CFF OTF
            // We should be able to read this font
        } else {
            throw new Exception(
                    "OTF version == " + version + " is not supported.");
        }

        int numOfTables   = readUInt16();
        int searchRange   = readUInt16();
        int entrySelector = readUInt16();
        int rangeShift    = readUInt16();

        FontTable cmapTable = null;
        for (int i = 0; i < numOfTables; i++) {
            byte[] name = new byte[4];
            for (int j = 0; j < 4; j++) {
                name[j] = readByte();
            }
            FontTable table = new FontTable();
            table.name     = new String(name);
            table.checkSum = readUInt32();
            table.offset = (int) readUInt32();
            table.length = (int) readUInt32();

            int k = index;  // Save the current index
            if      (table.name.equals("head")) { head(table); }
            else if (table.name.equals("hhea")) { hhea(table); }
            else if (table.name.equals("OS/2")) { OS_2(table); }
            else if (table.name.equals("name")) { name(table); }
            else if (table.name.equals("hmtx")) { hmtx(table); }
            else if (table.name.equals("post")) { post(table); }
            else if (table.name.equals("CFF ")) { CFF_(table); }
            else if (table.name.equals("cmap")) { cmapTable = table; }
            index = k;      // Restore the index
        }

        // This table must be processed last
        cmap(cmapTable);
        baos = new ByteArrayOutputStream();
        DeflaterOutputStream dos =
                new DeflaterOutputStream(baos, new Deflater(Deflater.BEST_SPEED));
        if (cff) {
            dos.write(buf, cffOff, cffLen);
        } else {
            dos.write(buf, 0, buf.length);
        }
        dos.finish();
    }

    private void head(FontTable table) {
        index = table.offset + 16;
        int flags = readUInt16();
        unitsPerEm = readUInt16();
        index += 16;
        bBoxLLx = readInt16();
        bBoxLLy = readInt16();
        bBoxURx = readInt16();
        bBoxURy = readInt16();
    }

    private void hhea(FontTable table) {
        index = table.offset + 4;
        ascent  = readInt16();
        descent = readInt16();
        index += 26;
        advanceWidth = new int[readUInt16()];
    }

    private void OS_2(FontTable table) {
        index = table.offset + 64;
        firstChar = readUInt16();
        lastChar  = readUInt16();
        index += 20;
        capHeight = readInt16();
    }

    private void name(FontTable table) throws IOException {
        index = table.offset;
        int format = readUInt16();
        int count  = readUInt16();
        int stringOffset = readUInt16();
        StringBuilder macFontInfo = new StringBuilder();
        StringBuilder winFontInfo = new StringBuilder();

        for (int r = 0; r < count; r++) {
            int platformID = readUInt16();
            int encodingID = readUInt16();
            int languageID = readUInt16();
            int nameID = readUInt16();
            int length = readUInt16();
            int offset = readUInt16();

            if (platformID == 1 && encodingID == 0 && languageID == 0) {
                // Macintosh
                String str = new String(
                        buf, table.offset + stringOffset + offset, length, "UTF-8");
                if (nameID == 6) {
                    fontName = str;
                } else {
                    macFontInfo.append(str);
                    macFontInfo.append('\n');
                }
            } else if (platformID == 3 && encodingID == 1 && languageID == 0x409) {
                // Windows
                String str = new String(
                        buf, table.offset + stringOffset + offset, length, "UTF-16");
                if (nameID == 6) {
                    fontName = new String(str.getBytes("UTF-8"));
                } else {
                    winFontInfo.append(str);
                    winFontInfo.append('\n');
                }
            }
        }
        fontInfo = (winFontInfo != null) ? winFontInfo.toString() : macFontInfo.toString();
    }

    private void cmap(FontTable table) throws Exception {
        index = table.offset;
        int tableOffset = index;
        index += 2;
        int numRecords = readUInt16();

        // Process the encoding records
        boolean format4subtable = false;
        int subtableOffset = 0;
        for (int i = 0; i < numRecords; i++) {
            int platformID = readUInt16();
            int encodingID = readUInt16();
            subtableOffset = (int) readUInt32();
            if (platformID == 3 && encodingID == 1) {
                format4subtable = true;
                break;
            }
        }
        if (!format4subtable) {
            throw new Exception("Format 4 subtable not found in this font.");
        }

        index = tableOffset + subtableOffset;

        int format   = readUInt16();
        int tableLen = readUInt16();
        int language = readUInt16();
        int segCount = readUInt16() / 2;

        index += 6; // Skip to the endCount[]
        int[] endCount = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            endCount[i] = readUInt16();
        }

        index += 2; // Skip the reservedPad
        int[] startCount = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            startCount[i] = readUInt16();
        }

        short[] idDelta = new short[segCount];
        for (int i = 0; i < segCount; i++) {
            idDelta[i] = (short) readUInt16();
        }

        int[] idRangeOffset = new int[segCount];
        for (int i = 0; i < segCount; i++) {
            idRangeOffset[i] = readUInt16();
        }

        int[] glyphIdArray = new int[(tableLen - (16 + 8*segCount)) / 2];
        for (int i = 0; i < glyphIdArray.length; i++) {
            glyphIdArray[i] = readUInt16();
        }

        glyphWidth = new int[lastChar + 1];
        Arrays.fill(glyphWidth, advanceWidth[0]);

        for (int ch = firstChar; ch <= lastChar; ch++) {
            int seg = getSegmentFor(ch, startCount, endCount, segCount);
            if (seg != -1) {
                int gid;
                int offset = idRangeOffset[seg];
                if (offset == 0) {
                    gid = (idDelta[seg] + ch) % 65536;
                } else {
                    offset /= 2;
                    offset -= segCount - seg;
                    gid = glyphIdArray[offset + (ch - startCount[seg])];
                    if (gid != 0) {
                        gid += idDelta[seg] % 65536;
                    }
                }
                if (gid < advanceWidth.length) {
                    glyphWidth[ch] = advanceWidth[gid];
                }
                unicodeToGID[ch] = gid;
            }
        }
    }

    private void hmtx(FontTable table) {
        index = table.offset;
        for (int j = 0; j < advanceWidth.length; j++) {
            advanceWidth[j] = readUInt16();
            index += 2;
        }
    }

    private void post(FontTable table) {
        index = table.offset;
        postVersion = readUInt32();
        italicAngle = readUInt32();
        underlinePosition  = (short) readUInt16();
        underlineThickness = (short) readUInt16();
    }

    private void CFF_(FontTable table) {
        this.cff = true;
        this.cffOff = table.offset;
        this.cffLen = table.length;
    }

    private int getSegmentFor(
            int ch, int[] startCount, int[] endCount, int segCount) {
        int segment = -1;
        for (int i = 0; i < segCount; i++) {
            if (ch <= endCount[i] && ch >= startCount[i]) {
                segment = i;
                break;
            }
        }
        return segment;
    }

    private byte readByte() {
        return buf[index++];
    }

    private short readInt16() {
        short val = 0;
        val |= (buf[index++] <<  8) & 0xFF00;
        val |= (buf[index++])       & 0x00FF;
        return val;
    }

    private int readUInt16() {
        int val = 0;
        val |= (buf[index++] <<  8) & 0x0000FF00;
        val |= (buf[index++])       & 0x000000FF;
        return val;
    }

    private long readUInt32() {
        long val = 0L;
        val |= (buf[index++] << 24) & 0xFF000000L;
        val |= (buf[index++] << 16) & 0x00FF0000L;
        val |= (buf[index++] <<  8) & 0x0000FF00L;
        val |= (buf[index++])       & 0x000000FFL;
        return val;
    }
}   // End of OTF.java

class FontTable {
    protected String name;
    protected long checkSum;
    protected int offset;
    protected int length;
}
