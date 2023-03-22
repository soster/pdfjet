/**
 *  Font.java
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

import java.io.InputStream;
import java.util.List;


/**
 *  Used to create font objects.
 *  The font objects must added to the PDF before they can be used to draw text.
 */
public class Font {
    /** Chinese (Traditional) font */
    public static final String AdobeMingStd_Light = "AdobeMingStd-Light";

    /** Chinese (Simplified) font */
    public static final String STHeitiSC_Light = "STHeitiSC-Light";

    /** Japanese font */
    public static final String KozMinProVI_Regular = "KozMinProVI-Regular";

    /** Korean font */
    public static final String AdobeMyungjoStd_Medium = "AdobeMyungjoStd-Medium";

    /** Is this a stream font? */
    public static final boolean STREAM = true;

    protected String name;
    protected String info;
    protected int objNumber;
    protected String fontID;

    // The object number of the embedded font file
    protected int fileObjNumber;
    protected int fontDescriptorObjNumber;
    protected int cidFontDictObjNumber;
    protected int toUnicodeCMapObjNumber;

    // Font attributes
    protected int unitsPerEm = 1000;    // The default for core fonts.
    protected int fontAscent;
    protected int fontDescent;
    protected int bBoxLLx;
    protected int bBoxLLy;
    protected int bBoxURx;
    protected int bBoxURy;
    protected int firstChar = 32;       // The default for core fonts.
    protected int lastChar = 255;       // The default for core fonts.
    protected int capHeight;
    protected int fontUnderlinePosition;
    protected int fontUnderlineThickness;
    protected int[] advanceWidth;
    protected int[] glyphWidth;
    protected int[] unicodeToGID;
    protected boolean cff;
    protected int compressedSize;
    protected int uncompressedSize;
    protected int[][] metrics;          // Only used for core fonts.

    // Don't change the following default values!
    protected float size = 12.0f;
    protected boolean isCoreFont = false;
    protected boolean isCJK = false;
    protected boolean skew15 = false;
    protected boolean kernPairs = false;

    // These attributes depend on the font size.
    protected float ascent;
    protected float descent;
    protected float bodyHeight;
    protected float underlinePosition;
    protected float underlineThickness;


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
     *  @throws Exception  If an input or output exception occurred
     */
    public Font(PDF pdf, CoreFont coreFont) throws Exception {
        StandardFont font = new StandardFont(coreFont);
        this.isCoreFont = true;
        this.name = font.name;
        this.bBoxLLx = font.bBoxLLx;
        this.bBoxLLy = font.bBoxLLy;
        this.bBoxURx = font.bBoxURx;
        this.bBoxURy = font.bBoxURy;
        this.metrics = font.metrics;
        this.fontUnderlinePosition = font.underlinePosition;
        this.fontUnderlineThickness = font.underlineThickness;
        this.fontAscent = font.bBoxURy;
        this.fontDescent = font.bBoxLLy;
        setSize(size);

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
        objNumber = pdf.getObjNumber();

        pdf.fonts.add(this);
    }


    // Used by PDFobj
    protected Font(CoreFont coreFont) {
        StandardFont font = new StandardFont(coreFont);
        this.isCoreFont = true;
        this.name = font.name;
        this.bBoxLLx = font.bBoxLLx;
        this.bBoxLLy = font.bBoxLLy;
        this.bBoxURx = font.bBoxURx;
        this.bBoxURy = font.bBoxURy;
        this.metrics = font.metrics;
        this.fontUnderlinePosition = font.underlinePosition;
        this.fontUnderlineThickness = font.underlineThickness;
        this.fontAscent = font.bBoxURy;
        this.fontDescent = font.bBoxLLy;
        setSize(size);
    }


    /**
     *  Constructor for CJK - Chinese, Japanese and Korean fonts.
     *  Please see Example_04.
     *
     *  @param pdf the PDF to add this font to.
     *  @param fontName the font name. Please see Example_04.
     *  @throws Exception  If an input or output exception occurred
     */
    public Font(PDF pdf, String fontName) throws Exception {
        this.name = fontName;
        this.isCJK = true;
        this.firstChar = 0x0020;
        this.lastChar = 0xFFEE;
        this.ascent = this.size;
        this.descent = this.size/4;
        this.bodyHeight = this.ascent + this.descent;

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
        pdf.append(pdf.getObjNumber() - 1);
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
        pdf.append(pdf.getObjNumber() - 1);
        pdf.append(" 0 R]\n");
        pdf.append(">>\n");
        pdf.endobj();
        objNumber = pdf.getObjNumber();

        pdf.fonts.add(this);
    }


    /**
     * Constructor for .ttf.stream fonts
     * 
     * @param pdf the PDF
     * @param inputStream the input stream
     * @param flag the flag ...
     * @throws Exception if the font is not found
     */
    public Font(PDF pdf, InputStream inputStream, boolean flag) throws Exception {
        FontStream1.register(pdf, this, inputStream);
        this.setSize(size);
    }


    /**
     * Constructor for .ttf.stream fonts
     * 
     * @param objects the list of objects
     * @param inputStream the input stream
     * @param flag the flag ...
     * @throws Exception is the font is not found
     */
    public Font(List<PDFobj> objects, InputStream inputStream, boolean flag) throws Exception {
        FontStream2.register(objects, this, inputStream);
        setSize(size);
    }


    /**
     *  Constructor for OpenType and TrueType fonts.
     *
     *  @param pdf the PDF object that requires this font.
     *  @param inputStream the input stream to read this font from.
     *  @throws Exception  If an input or output exception occurred
     */
    public Font(PDF pdf, InputStream inputStream) throws Exception {
        OpenTypeFont.register(pdf, this, inputStream);
        setSize(size);
    }


    /**
     *  Sets the size of this font.
     *
     *  @param fontSize specifies the size of this font.
     *  @return the font.
     */
    public Font setSize(double fontSize) {
        return setSize((float) fontSize);
    }


    /**
     *  Sets the size of this font.
     *
     *  @param fontSize specifies the size of this font.
     *  @return the font.
     */
    public Font setSize(float fontSize) {
        this.size = fontSize;
        if (isCJK) {
            this.ascent = size;
            this.descent = ascent/4;
            this.bodyHeight = this.ascent + this.descent;
            return this;
        }
        this.ascent = Float.valueOf(fontAscent) * size / Float.valueOf(unitsPerEm);
        this.descent = -Float.valueOf(fontDescent) * size / Float.valueOf(unitsPerEm);
        this.bodyHeight = this.ascent + this.descent;
        this.underlineThickness = (Float.valueOf(fontUnderlineThickness) * size / Float.valueOf(unitsPerEm));
        this.underlinePosition = -(Float.valueOf(fontUnderlinePosition) * size / Float.valueOf(unitsPerEm)) + underlineThickness / 2.0f;
        return this;
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
            return 0f;
        }

        if (isCJK) {
            return str.length() * ascent;
        }

        int width = 0;
        for (int i = 0; i < str.length(); i++) {
            int c1 = str.charAt(i);
            if (isCoreFont) {
                if (c1 < firstChar || c1 > lastChar) {
                    c1 = 0x20;
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
                    width += glyphWidth[c1];
                }
            }
        }

        return width * size / unitsPerEm;
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
        return descent;
    }


    /**
     *  Returns the height of this font.
     *
     *  @return the height of the font.
     */
    public float getHeight() {
        return ascent + descent;
    }


    /**
     *  Returns the height of the body of the font.
     *
     *  @return float the height of the body of the font.
     */
    public float getBodyHeight() {
        return bodyHeight;
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

        if (isCoreFont) {
            return getCoreFontFitChars(str, w);
        }

        int i;
        for (i = 0; i < str.length(); i++) {
            int c1 = str.charAt(i);

            if (c1 < firstChar || c1 > lastChar) {
                w -= advanceWidth[0];
            }
            else {
                w -= glyphWidth[c1];
            }

            if (w < 0) break;
        }

        return i;
    }


    private int getCoreFontFitChars(String str, float width) {
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
     * @param fallbackFont the fallback font.
     * @param str the string.
     * @return the width.
     */
    public float stringWidth(Font fallbackFont, String str) {
        float width = 0f;

        if (this.isCoreFont || this.isCJK || fallbackFont == null || fallbackFont.isCoreFont || fallbackFont.isCJK) {
            return stringWidth(str);
        }

        Font activeFont = this;
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            int ch = str.charAt(i);
            if (activeFont.unicodeToGID[ch] == 0) {
                width += activeFont.stringWidth(buf.toString());
                buf.setLength(0);
                // Switch the active font
                if (activeFont == this) {
                    activeFont = fallbackFont;
                }
                else {
                    activeFont = this;
                }
            }
            buf.append((char) ch);
        }
        width += activeFont.stringWidth(buf.toString());

        return width;
    }

}   // End of Font.java
