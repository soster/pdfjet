/**
 *  Font.java
 *
Copyright (c) 2014, Innovatics Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.pdfjet;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;


/**
 *  Used to create font objects.
 *  The font objects must added to the PDF before they can be used to draw text.
 *
 */
public class Font {

    protected String name;
    protected int objNumber;

    // The object number of the embedded font file
    protected int fileObjNumber = -1;

    // Font attributes
    protected int unitsPerEm = 1000;
    protected float size = 12.0f;
    protected float ascent;
    protected float descent;
    protected float capHeight;
    protected float body_height;

    // Font metrics
    protected int[][] metrics = null;

    // Don't change the following default values!
    protected boolean isStandard = true;
    protected boolean kernPairs = false;
    protected boolean isComposite = false;
    protected int firstChar = 32;
    protected int lastChar = 255;
    protected boolean skew15 = false;
    protected boolean isCJK = false;

    // Font bounding box
    protected float bBoxLLx;
    protected float bBoxLLy;
    protected float bBoxURx;
    protected float bBoxURy;
    protected float underlinePosition;
    protected float underlineThickness;

    protected int compressed_size;
    protected int uncompressed_size;

    protected int[] advanceWidth = null;
    protected int[] glyphWidth = null;
    protected int[] unicodeToGID;

    protected String fontID;

    private int fontDescriptorObjNumber = -1;
    private int cMapObjNumber = -1;
    private int cidFontDictObjNumber = -1;
    private int toUnicodeCMapObjNumber = -1;
    private int widthsArrayObjNumber = -1;
    private int encodingObjNumber = -1;
    private int codePage = CodePage.CP1252;
    private int fontUnderlinePosition = 0;
    private int fontUnderlineThickness = 0;


    /**
     *  Constructor for the 14 standard fonts.
     *  Creates a font object and adds it to the PDF.
     *
     *  <pre>
     *  Examples:
     *      Font font1 = new Font(pdf, CoreFont.HELVETICA);
     *      Font font2 = new Font(pdf, CoreFont.TIMES_ITALIC);
     *      Font font3 = new Font(pdf, CoreFont.ZAPF_DINGBATS);
     *      ...
     *  </pre>
     *
     *  @param pdf the PDF to add this font to.
     *  @param coreFont the core font. Must be one the names defined in the CoreFont class.
     */
    public Font(PDF pdf, CoreFont coreFont) throws Exception {
        StandardFont font = StandardFont.getInstance(coreFont);
        this.name = font.name;
        this.bBoxLLx = font.bBoxLLx;
        this.bBoxLLy = font.bBoxLLy;
        this.bBoxURx = font.bBoxURx;
        this.bBoxURy = font.bBoxURy;
        this.metrics = font.metrics;
        this.ascent = bBoxURy * size / unitsPerEm;
        this.descent = bBoxLLy * size / unitsPerEm;
        this.body_height = ascent - descent;
        this.fontUnderlinePosition = font.underlinePosition;
        this.fontUnderlineThickness = font.underlineThickness;
        this.underlineThickness = fontUnderlineThickness * size / unitsPerEm;
        this.underlinePosition = fontUnderlinePosition * size / -unitsPerEm + underlineThickness / 2.0f;

        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type1\n");
        pdf.append("/BaseFont /");
        pdf.append(this.name);
        pdf.append('\n');
        if (!this.name.equals("Symbol") && !this.name.equals("ZapfDingbats")) {
            pdf.append("/Encoding /WinAnsiEncoding\n");
        }
        pdf.append(">>\n");
        pdf.endobj();
        objNumber = pdf.objNumber;

        pdf.fonts.add(this);
    }

    // Used by PDFobj
    protected Font(CoreFont coreFont) {
        StandardFont font = StandardFont.getInstance(coreFont);
        this.name = font.name;
        this.bBoxLLx = font.bBoxLLx;
        this.bBoxLLy = font.bBoxLLy;
        this.bBoxURx = font.bBoxURx;
        this.bBoxURy = font.bBoxURy;
        this.metrics = font.metrics;
        this.ascent = bBoxURy * size / unitsPerEm;
        this.descent = bBoxLLy * size / unitsPerEm;
        this.body_height = ascent - descent;
        this.fontUnderlinePosition = font.underlinePosition;
        this.fontUnderlineThickness = font.underlineThickness;
        this.underlineThickness = fontUnderlineThickness * size / unitsPerEm;
        this.underlinePosition = fontUnderlinePosition * size / -unitsPerEm + underlineThickness / 2.0f;
    }


    /**
     *  Constructor for CJK - Chinese, Japanese and Korean fonts.
     *  Please see Example_04.
     *
     *  @param pdf the PDF to add this font to.
     *  @param fontName the font name. Please see Example_04.
     *  @param codePage the code page. Must be: CodePage.UNICODE
     */
    public Font(PDF pdf, String fontName, int codePage) throws Exception {
        this.name = fontName;
        this.codePage = codePage;
        isCJK = true;
        isStandard = false;
        isComposite = true;

        firstChar = 0x0020;
        lastChar = 0xFFEE;

        // Font Descriptor
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /FontDescriptor\n");
        pdf.append("/FontName /");
        pdf.append(fontName);
        pdf.append('\n');
        pdf.append("/Flags 4\n");
        pdf.append("/FontBBox [0 0 0 0]\n");
        pdf.append(">>\n");
        pdf.endobj();

        // CIDFont Dictionary
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /CIDFontType0\n");
        pdf.append("/BaseFont /");
        pdf.append(fontName);
        pdf.append('\n');
        pdf.append("/FontDescriptor ");
        pdf.append(pdf.objNumber - 1);
        pdf.append(" 0 R\n");
        pdf.append("/CIDSystemInfo <<\n");
        pdf.append("/Registry (Adobe)\n");
        if (fontName.startsWith("AdobeMingStd")) {
            pdf.append("/Ordering (CNS1)\n");
            pdf.append("/Supplement 4\n");
        } else if (fontName.startsWith("AdobeSongStd")
                || fontName.startsWith("STHeitiSC")) {
            pdf.append("/Ordering (GB1)\n");
            pdf.append("/Supplement 4\n");
        } else if (fontName.startsWith("KozMinPro")) {
            pdf.append("/Ordering (Japan1)\n");
            pdf.append("/Supplement 4\n");
        } else if (fontName.startsWith("AdobeMyungjoStd")) {
            pdf.append("/Ordering (Korea1)\n");
            pdf.append("/Supplement 1\n");
        } else {
            throw new Exception("Unsupported font: " + fontName);
        }
        pdf.append(">>\n");
        pdf.append(">>\n");
        pdf.endobj();

        // Type0 Font Dictionary
        pdf.newobj();
        pdf.append("<<\n");
        pdf.append("/Type /Font\n");
        pdf.append("/Subtype /Type0\n");
        pdf.append("/BaseFont /");
        if (fontName.startsWith("AdobeMingStd")) {
            pdf.append(fontName + "-UniCNS-UTF16-H\n");
            pdf.append("/Encoding /UniCNS-UTF16-H\n");
        } else if (fontName.startsWith("AdobeSongStd")
                || fontName.startsWith("STHeitiSC")) {
            pdf.append(fontName + "-UniGB-UTF16-H\n");
            pdf.append("/Encoding /UniGB-UTF16-H\n");
        } else if (fontName.startsWith("KozMinPro")) {
            pdf.append(fontName + "-UniJIS-UCS2-H\n");
            pdf.append("/Encoding /UniJIS-UCS2-H\n");
        } else if (fontName.startsWith("AdobeMyungjoStd")) {
            pdf.append(fontName + "-UniKS-UCS2-H\n");
            pdf.append("/Encoding /UniKS-UCS2-H\n");
        } else {
            throw new Exception("Unsupported font: " + fontName);
        }
        pdf.append("/DescendantFonts [");
        pdf.append(pdf.objNumber - 1);
        pdf.append(" 0 R]\n");
        pdf.append(">>\n");
        pdf.endobj();
        objNumber = pdf.objNumber;

        ascent = size;
        descent = -ascent/4;
        body_height = ascent - descent;

        pdf.fonts.add(this);
    }


    // Constructor for the DejaVuLGCSerif.ttf font.
    public Font(PDF pdf, InputStream inputStream) throws Exception {
        this.isStandard = false;
        this.isComposite = true;
        this.codePage = CodePage.UNICODE;

        FastFont.register(pdf, this, inputStream);

        this.ascent = bBoxURy * size / unitsPerEm;
        this.descent = bBoxLLy * size / unitsPerEm;
        this.body_height = ascent - descent;
        this.underlineThickness = fontUnderlineThickness * size / unitsPerEm;
        this.underlinePosition = fontUnderlinePosition * size / -unitsPerEm + underlineThickness / 2f;

        pdf.fonts.add(this);
    }


    protected int getFontDescriptorObjNumber() {
        return fontDescriptorObjNumber;
    }


    protected int getCMapObjNumber() {
        return cMapObjNumber;
    }


    protected int getCidFontDictObjNumber() {
        return cidFontDictObjNumber;
    }


    protected int getToUnicodeCMapObjNumber() {
        return toUnicodeCMapObjNumber;
    }


    protected int getWidthsArrayObjNumber() {
        return widthsArrayObjNumber;
    }


    protected int getEncodingObjNumber() {
        return encodingObjNumber;
    }


    protected void setFontDescriptorObjNumber(int fontDescriptorObjNumber) {
        this.fontDescriptorObjNumber = fontDescriptorObjNumber;
    }


    protected void setCMapObjNumber(int cMapObjNumber) {
        this.cMapObjNumber = cMapObjNumber;
    }


    protected void setCidFontDictObjNumber(int cidFontDictObjNumber) {
        this.cidFontDictObjNumber = cidFontDictObjNumber;
    }


    protected void setToUnicodeCMapObjNumber(int toUnicodeCMapObjNumber) {
        this.toUnicodeCMapObjNumber = toUnicodeCMapObjNumber;
    }


    protected void setWidthsArrayObjNumber(int widthsArrayObjNumber) {
        this.widthsArrayObjNumber = widthsArrayObjNumber;
    }


    protected void setEncodingObjNumber(int encodingObjNumber) {
        this.encodingObjNumber = encodingObjNumber;
    }


    /**
     *  Sets the size of this font.
     *
     *  @param fontSize specifies the size of this font.
     */
    public void setSize(double fontSize) {
        setSize((float) fontSize);
    }


    /**
     *  Sets the size of this font.
     *
     *  @param fontSize specifies the size of this font.
     */
    public void setSize(float fontSize) {
        size = fontSize;
        if (isCJK) {
            ascent = size;
            descent = -ascent/4;
            return;
        }
        this.ascent = bBoxURy * size / unitsPerEm;
        this.descent = bBoxLLy * size / unitsPerEm;
        this.body_height = ascent - descent;
        this.underlineThickness = fontUnderlineThickness * size / unitsPerEm;
        this.underlinePosition = fontUnderlinePosition * size / -unitsPerEm + underlineThickness / 2.0f;
    }


    /**
     *  Returns the current font size.
     *
     *  @return the current size of the font.
     */
    public float getSize() {
        return size;
    }


    /**
     *  Sets the kerning for the selected font to 'true' or 'false' depending on the passed value of kernPairs parameter.
     *  The kerning is implemented only for the 14 standard fonts.
     *
     *  @param kernPairs if 'true' the kerning for this font is enabled.
     */
    public void setKernPairs(boolean kernPairs) {
        this.kernPairs = kernPairs;
    }


    /**
     *  Returns the width of the specified string when drawn on the page with this font using the current font size.
     *
     *  @param str the specified string.
     *
     *  @return the width of the string when draw on the page with this font using the current selected size.
     */
    public float stringWidth(String str) {
        if (str == null) {
            return 0.0f;
        }

        if (isCJK) {
            return str.length() * ascent;
        }

        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            int c1 = str.charAt(i);
            if (isStandard) {
                if (c1 < firstChar || c1 > lastChar) {
                    c1 = mapUnicodeChar(c1);
                }
                c1 -= 32;
                width += metrics[c1][1];
    
                if (kernPairs && i < (str.length() - 1)) {
                    int c2 = str.charAt(i + 1);
                    if (c2 < firstChar || c2 > lastChar) {
                        c2 = 32;
                    }
                    for (int j = 2; j < metrics[c1].length; j += 2) {
                        if (metrics[c1][j] == c2) {
                            width += metrics[c1][j + 1];
                            break;
                        }
                    }
                }
            }
            else {
                if (c1 < firstChar || c1 > lastChar) {
                    width += advanceWidth[0];
                } else {
                    width += nonStandardFontGlyphWidth(c1);
                }
            }
        }

        return width * size / unitsPerEm;
    }


    private int nonStandardFontGlyphWidth(int c1) {
        int width = 0;

        if (isComposite) {
            width = glyphWidth[c1];
        } else {
            if (c1 < 127) {
                width = glyphWidth[c1];
            } else {
                if (codePage == 0) {
                    width = glyphWidth[CP1250.codes[c1 - 127]];
                } else if (codePage == 1) {
                    width = glyphWidth[CP1251.codes[c1 - 127]];
                } else if (codePage == 2) {
                    width = glyphWidth[CP1252.codes[c1 - 127]];
                } else if (codePage == 3) {
                    width = glyphWidth[CP1253.codes[c1 - 127]];
                } else if (codePage == 4) {
                    width = glyphWidth[CP1254.codes[c1 - 127]];
                } else if (codePage == 7) {
                    width = glyphWidth[CP1257.codes[c1 - 127]];
                }
            }
        }

        return width;
    }


    /**
     *  Returns the ascent of this font.
     *
     *  @return the ascent of the font.
     */
    public float getAscent() {
        return ascent;
    }


    /**
     *  Returns the descent of this font.
     *
     *  @return the descent of the font.
     */
    public float getDescent() {
        return -descent;
    }


    /**
     *  Returns the height of this font.
     *
     *  @return the height of the font.
     */
    public float getHeight() {
        return ascent - descent;
    }


    /**
     *  Returns the height of the body of the font.
     *
     *  @return float the height of the body of the font.
     */
    public float getBodyHeight() {
        return body_height;
    }


    /**
     *  Returns the number of characters from the specified string that will fit within the specified width.
     *
     *  @param str the specified string.
     *  @param width the specified width.
     *
     *  @return the number of characters that will fit.
     */
    public int getFitChars(String str, double width) {
        return getFitChars(str, (float) width);
    }


    /**
     *  Returns the number of characters from the specified string that will fit within the specified width.
     *
     *  @param str the specified string.
     *  @param width the specified width.
     *
     *  @return the number of characters that will fit.
     */
    public int getFitChars(String str, float width) {

        float w = width * unitsPerEm / size;

        if (isCJK) {
            return (int) (w / ascent);
        }

        if (isStandard) {
            return getStandardFontFitChars(str, w);
        }

        int i;
        for (i = 0; i < str.length(); i++) {

            int c1 = str.charAt(i);

            if (c1 < firstChar || c1 > lastChar) {
                w -= advanceWidth[0];
            }
            else {
                w -= nonStandardFontGlyphWidth(c1);
            }

            if (w < 0) break;
        }

        return i;
    }


    private int getStandardFontFitChars(String str, float width) {
        float w = width;

        int i = 0;
        while (i < str.length()) {

            int c1 = str.charAt(i);

            if (c1 < firstChar || c1 > lastChar) {
                c1 = 32;
            }

            c1 -= 32;
            w -= metrics[c1][1];

            if (w < 0) {
                return i;
            }

            if (kernPairs && i < (str.length() - 1)) {
                int c2 = str.charAt(i + 1);
                if (c2 < firstChar || c2 > lastChar) {
                    c2 = 32;
                }
    
                for (int j = 2; j < metrics[c1].length; j += 2) {
                    if (metrics[c1][j] == c2) {
                        w -= metrics[c1][j + 1];
                        if (w < 0) {
                            return i;
                        }
                        break;
                    }
                }
            }

            i += 1;
        }

        return i;
    }


    protected int mapUnicodeChar(int c1) {

        int[] codes = null;

        if (codePage == CodePage.CP1250) {
            codes = CP1250.codes;
        }
        else if (codePage == CodePage.CP1251) {
            codes = CP1251.codes;
        }
        else if (codePage == CodePage.CP1252) {
            codes = CP1252.codes;
        }
        else if (codePage == CodePage.CP1253) {
            codes = CP1253.codes;
        }
        else if (codePage == CodePage.CP1254) {
            codes = CP1254.codes;
        }
        else if (codePage == CodePage.CP1257) {
            codes = CP1257.codes;
        }

        if (codes != null) {
            for (int i = 0; i < codes.length; i++) {
                if (codes[i] == c1) {
                    return 127 + i;
                }
            }
        }

        return 0x0020;
    }


    /**
     * Sets the skew15 private variable.
     * When the variable is set to 'true' all glyphs in the font are skewed on 15 degrees.
     * This makes a regular font look like an italic type font.
     * Use this method when you don't have real italic font in the font family,
     * or when you want to generate smaller PDF files.
     * For example you could embed only the Regular and Bold fonts and synthesize the RegularItalic and BoldItalic.
     * 
     * @param skew15 the skew flag.
     */
    public void setItalic(boolean skew15) {
        this.skew15 = skew15;
    }


    /**
     * Returns the width of a string drawn using two fonts.
     * 
     * @param font2 the fallback font.
     * @param str the string.
     * @return the width.
     */
    public float stringWidth(Font font2, String str) {
        float width = 0f;

        boolean usingFont1 = true;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int ch = str.charAt(i);
            if ((isCJK && ch >= 0x4E00 && ch <= 0x9FCC)
                    || (!isCJK && unicodeToGID[ch] != 0)) {
                if (!usingFont1) {
                    width += font2.stringWidth(buf.toString());
                    buf = new StringBuilder();
                    usingFont1 = true;
                }
            }
            else {
                if (usingFont1) {
                    width += stringWidth(buf.toString());
                    buf = new StringBuilder();
                    usingFont1 = false;
                }
            }
            buf.append((char) ch);
        }

        if (usingFont1) {
            width += stringWidth(buf.toString());
        }
        else {
            width += font2.stringWidth(buf.toString());
        }

        return width;
    }

}   // End of Font.java
