/**
 *  Barcode.java
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

import java.util.*;

/**
 *  Used to create one dimentional barcodes - UPC, Code 39 and Code 128.
 *
 *  Please see Example_11.
 */
public class Barcode implements Drawable {
    public static final int UPC = 0;
    public static final int CODE128 = 1;
    public static final int CODE39 = 2;

    public static final int LEFT_TO_RIGHT = 0;
    public static final int TOP_TO_BOTTOM = 1;
    public static final int BOTTOM_TO_TOP = 2;

    private int barcodeType = 0;
    private String text = null;
    private float x1 = 0.0f;
    private float y1 = 0.0f;
    private float m1 = 0.75f;   // Module length
    private float barHeightFactor = 50.0f;
    private int direction = LEFT_TO_RIGHT;
    private Font font = null;

    private int[] tableA = {3211,2221,2122,1411,1132,1231,1114,1312,1213,3112};
    private Map<Character, String> tableB = new HashMap<Character, String>();

    /**
     *  The constructor.
     *
     *  @param barcodeType the type of the barcode.
     *  @param text the content text of the barcode.
     */
    public Barcode(int barcodeType, String text) {
        this.barcodeType = barcodeType;
        this.text = text;

        tableB.put('*', "bWbwBwBwb");
        tableB.put('-', "bWbwbwBwB");
        tableB.put('$', "bWbWbWbwb");
        tableB.put('%', "bwbWbWbWb");
        tableB.put(' ', "bWBwbwBwb");
        tableB.put('.', "BWbwbwBwb");
        tableB.put('/', "bWbWbwbWb");
        tableB.put('+', "bWbwbWbWb");
        tableB.put('0', "bwbWBwBwb");
        tableB.put('1', "BwbWbwbwB");
        tableB.put('2', "bwBWbwbwB");
        tableB.put('3', "BwBWbwbwb");
        tableB.put('4', "bwbWBwbwB");
        tableB.put('5', "BwbWBwbwb");
        tableB.put('6', "bwBWBwbwb");
        tableB.put('7', "bwbWbwBwB");
        tableB.put('8', "BwbWbwBwb");
        tableB.put('9', "bwBWbwBwb");
        tableB.put('A', "BwbwbWbwB");
        tableB.put('B', "bwBwbWbwB");
        tableB.put('C', "BwBwbWbwb");
        tableB.put('D', "bwbwBWbwB");
        tableB.put('E', "BwbwBWbwb");
        tableB.put('F', "bwBwBWbwb");
        tableB.put('G', "bwbwbWBwB");
        tableB.put('H', "BwbwbWBwb");
        tableB.put('I', "bwBwbWBwb");
        tableB.put('J', "bwbwBWBwb");
        tableB.put('K', "BwbwbwbWB");
        tableB.put('L', "bwBwbwbWB");
        tableB.put('M', "BwBwbwbWb");
        tableB.put('N', "bwbwBwbWB");
        tableB.put('O', "BwbwBwbWb");
        tableB.put('P', "bwBwBwbWb");
        tableB.put('Q', "bwbwbwBWB");
        tableB.put('R', "BwbwbwBWb");
        tableB.put('S', "bwBwbwBWb");
        tableB.put('T', "bwbwBwBWb");
        tableB.put('U', "BWbwbwbwB");
        tableB.put('V', "bWBwbwbwB");
        tableB.put('W', "BWBwbwbwb");
        tableB.put('X', "bWbwBwbwB");
        tableB.put('Y', "BWbwBwbwb");
        tableB.put('Z', "bWBwBwbwb");
    }

    /**
     *  Sets the position where this barcode will be drawn on the page.
     *
     *  @param x1 the x coordinate of the top left corner of the barcode.
     *  @param y1 the y coordinate of the top left corner of the barcode.
     */
    public void setPosition(float x1, float y1) {
        setLocation(x1, y1);
    }

    /**
     *  Sets the position where this barcode will be drawn on the page.
     *
     *  @param x1 the x coordinate of the top left corner of the barcode.
     *  @param y1 the y coordinate of the top left corner of the barcode.
     */
    public void setPosition(double x1, double y1) {
        setLocation(x1, y1);
    }

    /**
     *  Sets the location where this barcode will be drawn on the page.
     *
     *  @param x1 the x coordinate of the top left corner of the barcode.
     *  @param y1 the y coordinate of the top left corner of the barcode.
     *  @return this Barcode object.
     */
    public Barcode setLocation(float x1, float y1) {
        this.x1 = x1;
        this.y1 = y1;
        return this;
    }

    /**
     *  Sets the location where this barcode will be drawn on the page.
     *
     *  @param x1 the x coordinate of the top left corner of the barcode.
     *  @param y1 the y coordinate of the top left corner of the barcode.
     *  @return this Barcode object.
     */
    public Barcode setLocation(double x1, double y1) {
        return setLocation((float) x1, (float) y1);
    }

    /**
     *  Sets the module length of this barcode.
     *  The default value is 0.75
     *
     *  @param moduleLength the specified module length.
     */
    public void setModuleLength(double moduleLength) {
        this.m1 = (float) moduleLength;
    }

    /**
     *  Sets the module length of this barcode.
     *  The default value is 0.75
     *
     *  @param moduleLength the specified module length.
     */
    public void setModuleLength(float moduleLength) {
        this.m1 = moduleLength;
    }

    /**
     *  Sets the bar height factor.
     *  The height of the bars is the moduleLength * barHeightFactor
     *  The default value is 50.0
     *
     *  @param barHeightFactor the specified bar height factor.
     */
    public void setBarHeightFactor(double barHeightFactor) {
        this.barHeightFactor = (float) barHeightFactor;
    }

    /**
     *  Sets the bar height factor.
     *  The height of the bars is the moduleLength * barHeightFactor
     *  The default value is 50.0f
     *
     *  @param barHeightFactor the specified bar height factor.
     */
    public void setBarHeightFactor(float barHeightFactor) {
        this.barHeightFactor = barHeightFactor;
    }

    /**
     *  Sets the drawing direction for this font.
     *
     *  @param direction the specified direction.
     */
    public void setDirection(int direction) {
        this.direction = direction;
    }

    /**
     *  Sets the font to be used with this barcode.
     *
     *  @param font the specified font.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     *  Draws this barcode on the specified page.
     *
     *  @param page the specified page.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        if (barcodeType == Barcode.UPC) {
            return drawCodeUPC(page, x1, y1);
        } else if (barcodeType == Barcode.CODE128) {
            return drawCode128(page, x1, y1);
        } else if (barcodeType == Barcode.CODE39) {
            return drawCode39(page, x1, y1);
        } else {
            throw new Exception("Unsupported Barcode Type.");
        }
    }

    protected float[] drawOnPageAtLocation(Page page, float x1, float y1) throws Exception {
        if (barcodeType == Barcode.UPC) {
            return drawCodeUPC(page, x1, y1);
        } else if (barcodeType == Barcode.CODE128) {
            return drawCode128(page, x1, y1);
        } else if (barcodeType == Barcode.CODE39) {
            return drawCode39(page, x1, y1);
        } else {
            throw new Exception("Unsupported Barcode Type.");
        }
    }

    private float[] drawCodeUPC(Page page, float x1, float y1) throws Exception {
        float x = x1;
        float y = y1;
        float h = m1 * barHeightFactor; // Barcode height when drawn horizontally

        // Calculate the check digit:
        // 1. Add the digits in the odd-numbered positions (first, third, fifth, etc.)
        // together and multiply by three.
        // 2. Add the digits in the even-numbered positions (second, fourth, sixth, etc.)
        // to the result.
        // 3. Subtract the result modulo 10 from ten.
        // 4. The answer modulo 10 is the check digit.
        int sum = 0;
        for (int i = 0; i < 11; i += 2) {
            sum += text.charAt(i) - 48;
        }
        sum *= 3;
        for (int i = 1; i < 11; i += 2) {
            sum += text.charAt(i) - 48;
        }
        int reminder = sum % 10;
        int checkDigit = (10 - reminder) % 10;
        text += Integer.toString(checkDigit);

        x = drawEGuard(page, x, y, m1, h + 8);
        for (int i = 0; i < 6; i++) {
            int digit = text.charAt(i) - 0x30;
            // page.drawString(Integer.toString(digit), x + 1, y + h + 12);
            String symbol = Integer.toString(tableA[digit]);
            for (int j = 0; j < symbol.length(); j++) {
                int n = symbol.charAt(j) - 0x30;
                if (j%2 != 0) {
                    drawVertBar(page, x, y, n*m1, h);
                }
                x += n*m1;
            }
        }
        x = drawMGuard(page, x, y, m1, h + 8);
        for (int i = 6; i < 12; i++) {
            int digit = text.charAt(i) - 0x30;
            // page.drawString(Integer.toString(digit), x + 1, y + h + 12);
            String symbol = Integer.toString(tableA[digit]);
            for (int j = 0; j < symbol.length(); j++) {
                int n = symbol.charAt(j) - 0x30;
                if (j%2 == 0) {
                    drawVertBar(page, x, y, n*m1, h);
                }
                x += n*m1;
            }
        }
        x = drawEGuard(page, x, y, m1, h + 8);

        float[] xy = new float[] {x, y};
        if (font != null) {
            String label =
                    text.charAt(0) +
                    "  " +
                    text.charAt(1) +
                    text.charAt(2) +
                    text.charAt(3) +
                    text.charAt(4) +
                    text.charAt(5) +
                    "   " +
                    text.charAt(6) +
                    text.charAt(7) +
                    text.charAt(8) +
                    text.charAt(9) +
                    text.charAt(10) +
                    "  " +
                    text.charAt(11);
            float fontSize = font.getSize();
            font.setSize(10f);

            TextLine textLine = new TextLine(font, label);
            textLine.setLocation(
                    x1 + ((x - x1) - font.stringWidth(label))/2,
                    y1 + h + font.bodyHeight);
            xy = textLine.drawOn(page);
            xy[0] = Math.max(x, xy[0]);
            xy[1] = Math.max(y, xy[1]);

            font.setSize(fontSize);
            return new float[] {xy[0], xy[1] + font.descent};
        }

        return new float[] {xy[0], xy[1]};
    }

    private float drawEGuard(
            Page page,
            float x,
            float y,
            float m1,
            float h) {
        if (page != null) {
            // 101
            page.addArtifactBMC();
            drawBar(page, x + (0.5f * m1), y, m1, h);
            drawBar(page, x + (2.5f * m1), y, m1, h);
            page.addEMC();
        }
        return (x + (3.0f * m1));
    }

    private float drawMGuard(
            Page page,
            float x,
            float y,
            float m1,
            float h) {
        if (page != null) {
            // 01010
            page.addArtifactBMC();
            drawBar(page, x + (1.5f * m1), y, m1, h);
            drawBar(page, x + (3.5f * m1), y, m1, h);
            page.addEMC();
        }
        return (x + (5.0f * m1));
    }

    private void drawBar(
            Page page,
            float x,
            float y,
            float m1,  // Single bar width
            float h) {
        if (page != null) {
            page.setPenWidth(m1);
            page.moveTo(x, y);
            page.lineTo(x, y + h);
            page.strokePath();
        }
    }

    private float[] drawCode128(Page page, float x1, float y1) throws Exception {
        float x = x1;
        float y = y1;

        float w = m1;
        float h = m1;

        if (direction == TOP_TO_BOTTOM) {
            w *= barHeightFactor;
        } else if (direction == LEFT_TO_RIGHT) {
            h *= barHeightFactor;
        }

        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < text.length(); i++) {
            char symchar = text.charAt(i);
            if (symchar < 32) {
                list.add(GS1_128.SHIFT);
                list.add(symchar + 64);
            } else if (symchar < 128) {
                list.add(symchar - 32);
            } else if (symchar < 256) {
                list.add(GS1_128.FNC_4);
                list.add(symchar - 160);    // 128 + 32
            } else {
                // list.add(31);            // '?'
                list.add(256);              // This will generate an exception.
            }
            if (list.size() == 48) {
                // Maximum number of data characters is 48
                break;
            }
        }

        StringBuilder buf = new StringBuilder();
        int checkDigit = GS1_128.START_B;
        buf.append((char) checkDigit);
        for (int i = 0; i < list.size(); i++) {
            int codeword = list.get(i);
            buf.append((char) codeword);
            checkDigit += codeword * (i + 1);
        }
        checkDigit %= GS1_128.START_A;
        buf.append((char) checkDigit);
        buf.append((char) GS1_128.STOP);

        for (int i = 0; i < buf.length(); i++) {
            int si = buf.charAt(i);
            String symbol = Integer.toString(GS1_128.TABLE[si]);
            for (int j = 0; j < symbol.length(); j++) {
                int n = symbol.charAt(j) - 0x30;
                if (j%2 == 0) {
                    if (direction == LEFT_TO_RIGHT) {
                        drawVertBar(page, x, y, n * m1, h);
                    } else if (direction == TOP_TO_BOTTOM) {
                        drawHorzBar(page, x, y, n * m1, w);
                    }
                }
                if (direction == LEFT_TO_RIGHT) {
                    x += n * m1;
                } else if (direction == TOP_TO_BOTTOM) {
                    y += n * m1;
                }
            }
        }

        float[] xy = new float[] {x, y};
        if (font != null) {
            if (direction == LEFT_TO_RIGHT) {
                TextLine textLine = new TextLine(font, text);
                textLine.setLocation(
                        x1 + ((x - x1) - font.stringWidth(text))/2,
                        y1 + h + font.bodyHeight);
                xy = textLine.drawOn(page);
                xy[0] = Math.max(x, xy[0]);
                return new float[] {xy[0], xy[1] + font.descent};
            } else if (direction == TOP_TO_BOTTOM) {
                TextLine textLine = new TextLine(font, text);
                textLine.setLocation(
                        x + w + font.bodyHeight,
                        y - ((y - y1) - font.stringWidth(text))/2);
                textLine.setTextDirection(90);
                xy = textLine.drawOn(page);
                xy[1] = Math.max(y, xy[1]);
            }
        }

        return xy;
    }

    private float[] drawCode39(Page page, float x1, float y1) throws Exception {
        text = "*" + text + "*";
        float x = x1;
        float y = y1;
        float w = m1 * barHeightFactor; // Barcode width when drawn vertically
        float h = m1 * barHeightFactor; // Barcode height when drawn horizontally

        float[] xy = new float[] {0f, 0f};
        if (direction == LEFT_TO_RIGHT) {
            for (int i = 0; i < text.length(); i++) {
                String code = tableB.get(text.charAt(i));
                if (code == null) {
                    throw new Exception("The input string '" + text +
                            "' contains characters that are invalid in a Code39 barcode.");
                }
                for (int j = 0; j < 9; j++) {
                    char ch = code.charAt(j);
                    if (ch == 'w') {
                        x += m1;
                    } else if (ch == 'W') {
                        x += m1 * 3;
                    } else if (ch == 'b') {
                        drawVertBar(page, x, y, m1, h);
                        x += m1;
                    } else if (ch == 'B') {
                        drawVertBar(page, x, y, m1 * 3, h);
                        x += m1 * 3;
                    }
                }
                x += m1;
            }

            if (font != null) {
                TextLine textLine = new TextLine(font, text);
                textLine.setLocation(
                        x1 + ((x - x1) - font.stringWidth(text))/2,
                        y1 + h + font.bodyHeight);
                xy = textLine.drawOn(page);
                xy[0] = Math.max(x, xy[0]);
            }
        } else if (direction == TOP_TO_BOTTOM) {
            for (int i = 0; i < text.length(); i++) {
                String code = tableB.get(text.charAt(i));
                if (code == null) {
                    throw new Exception("The input string '" + text +
                            "' contains characters that are invalid in a Code39 barcode.");
                }
                for (int j = 0; j < 9; j++) {
                    char ch = code.charAt(j);
                    if (ch == 'w') {
                        y += m1;
                    } else if (ch == 'W') {
                        y += 3 * m1;
                    } else if (ch == 'b') {
                        drawHorzBar(page, x, y, m1, h);
                        y += m1;
                    } else if (ch == 'B') {
                        drawHorzBar(page, x, y, 3 * m1, h);
                        y += 3 * m1;
                    }
                }
                y += m1;
            }

            if (font != null) {
                TextLine textLine = new TextLine(font, text);
                textLine.setLocation(
                        x - font.bodyHeight,
                        y1 + ((y - y1) - font.stringWidth(text))/2);
                textLine.setTextDirection(270);
                xy = textLine.drawOn(page);
                xy[0] = Math.max(x, xy[0]) + w;
                xy[1] = Math.max(y, xy[1]);
            }
        } else if (direction == BOTTOM_TO_TOP) {
            float height = 0.0f;

            for (int i = 0; i < text.length(); i++) {
                String code = tableB.get(text.charAt(i));
                if (code == null) {
                    throw new Exception("The input string '" + text +
                            "' contains characters that are invalid in a Code39 barcode.");
                }
                for (int j = 0; j < 9; j++) {
                    char ch = code.charAt(j);
                    if (ch == 'w' || ch == 'b') {
                        height += m1;
                    } else if (ch == 'W' || ch == 'B') {
                        height += 3 * m1;
                    }
                }
                height += m1;
            }

            y += height - m1;
            for (int i = 0; i < text.length(); i++) {
                String code = tableB.get(text.charAt(i));
                for (int j = 0; j < 9; j++) {
                    char ch = code.charAt(j);
                    if (ch == 'w') {
                        y -= m1;
                    } else if (ch == 'W') {
                        y -= 3 * m1;
                    } else if (ch == 'b') {
                        drawHorzBar2(page, x, y, m1, h);
                        y -= m1;
                    } else if (ch == 'B') {
                        drawHorzBar2(page, x, y, 3 * m1, h);
                        y -= 3 * m1;
                    }
                }
                y -= m1;
            }

            if (font != null) {
                y = y1 + (height - m1);
                TextLine textLine = new TextLine(font, text);
                textLine.setLocation(
                        x + w + font.bodyHeight,
                        y - ((y - y1) - font.stringWidth(text))/2);
                textLine.setTextDirection(90);
                xy = textLine.drawOn(page);
                xy[1] = Math.max(y, xy[1]);
                return new float[] {xy[0], xy[1] + font.descent};
            }
        }

        return new float[] {xy[0], xy[1]};
    }

    private void drawVertBar(
            Page page,
            float x,
            float y,
            float m1,   // Module length
            float h) throws Exception {
        if (page != null) {
            page.addArtifactBMC();
            page.setPenWidth(m1);
            page.moveTo(x + m1 / 2, y);
            page.lineTo(x + m1 / 2, y + h);
            page.strokePath();
            page.addEMC();
        }
    }

    private void drawHorzBar(
            Page page,
            float x,
            float y,
            float m1,   // Module length
            float w) {
        if (page != null) {
            page.addArtifactBMC();
            page.setPenWidth(m1);
            page.moveTo(x, y + m1 / 2);
            page.lineTo(x + w, y + m1 / 2);
            page.strokePath();
            page.addEMC();
        }
    }

    private void drawHorzBar2(
            Page page,
            float x,
            float y,
            float m1,   // Module length
            float w) throws Exception {
        if (page != null) {
            page.addArtifactBMC();
            page.setPenWidth(m1);
            page.moveTo(x, y - m1 / 2);
            page.lineTo(x + w, y - m1 / 2);
            page.strokePath();
            page.addEMC();
        }
    }

    public float getHeight() {
        if (font == null) {
            return m1 * barHeightFactor;
        }
        return m1 * barHeightFactor + font.getHeight();
    }
}   // End of Barcode.java
