/**
 *  Text.java
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
 *  Please see Example_45
 */
public class Text implements Drawable {

    private final List<Paragraph> paragraphs;
    private final Font font;
    private final Font fallbackFont;
    private float x1;
    private float y1;
    private float xText;
    private float yText;
    private float width;
    private float leading;
    private float paragraphLeading;
    private final List<float[]> beginParagraphPoints;
    private float spaceBetweenTextLines;
    private boolean drawBorder = true;


    public Text(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        this.font = paragraphs.get(0).list.get(0).getFont();
        this.fallbackFont = paragraphs.get(0).list.get(0).getFallbackFont();
        this.leading = font.getBodyHeight();
        this.paragraphLeading = 2*leading;
        this.beginParagraphPoints = new ArrayList<float[]>();
        this.spaceBetweenTextLines = font.stringWidth(fallbackFont, Single.space);
    }


    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    public Text setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }

    public Text setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    public Text setWidth(float width) {
        this.width = width;
        return this;
    }


    public Text setLeading(float leading) {
        this.leading = leading;
        return this;
    }


    public Text setParagraphLeading(float paragraphLeading) {
        this.paragraphLeading = paragraphLeading;
        return this;
    }


    public List<float[]> getBeginParagraphPoints() {
        return this.beginParagraphPoints;
    }


    public Text setSpaceBetweenTextLines(float spaceBetweenTextLines) {
        this.spaceBetweenTextLines = spaceBetweenTextLines;
        return this;
    }


    public float[] drawOn(Page page) throws Exception {
        this.xText = x1;
        this.yText = y1 + font.ascent;
        for (Paragraph paragraph : paragraphs) {
            int numberOfTextLines = paragraph.list.size();
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < numberOfTextLines; i++) {
                TextLine textLine = paragraph.list.get(i);
                buf.append(textLine.getText());
            }
            for (int i = 0; i < numberOfTextLines; i++) {
                TextLine textLine = paragraph.list.get(i);
                if (i == 0) {
                    beginParagraphPoints.add(new float[] {xText, yText});
                }
                float[] xy = drawTextLine(page, xText, yText, textLine);
                xText = xy[0];
                if (textLine.getTrailingSpace()) {
                    xText += spaceBetweenTextLines;
                }
                yText = xy[1];
            }
            xText = x1;
            yText += paragraphLeading;
        }

        float height = ((yText - paragraphLeading) - y1) + font.descent;
        if (page != null && drawBorder) {
            Box box = new Box();
            box.setLocation(x1, y1);
            box.setSize(width, height);
            box.drawOn(page);
        }

        return new float[] {x1 + width, y1 + height};
    }


    public float[] drawTextLine(Page page, float x, float y, TextLine textLine) throws Exception {
        this.xText = x;
        this.yText = y;

        String[] tokens = null;
        if (stringIsCJK(textLine.text)) {
            tokens = tokenizeCJK(textLine, this.width);
        }
        else {
            tokens = textLine.text.split("\\s+");
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < tokens.length; i++) {
            String token = (i == 0) ? tokens[i] : (Single.space + tokens[i]);
            float lineWidth = textLine.font.stringWidth(textLine.fallbackFont, buf.toString());
            float tokenWidth = textLine.font.stringWidth(textLine.fallbackFont, token);
            if ((lineWidth + tokenWidth) < ((this.x1 + this.width) - this.xText)) {
                buf.append(token);
            }
            else {
                if (page != null) {
                    new TextLine(textLine.font, buf.toString())
                            .setFallbackFont(textLine.fallbackFont)
                            .setLocation(xText, yText + textLine.getVerticalOffset())
                            .setColor(textLine.getColor())
                            .setUnderline(textLine.getUnderline())
                            .setStrikeout(textLine.getStrikeout())
                            .setLanguage(textLine.getLanguage())
                            .drawOn(page);
                }
                xText = x1;
                yText += leading;
                buf.setLength(0);
                buf.append(tokens[i]);
            }
        }
        if (page != null) {
            new TextLine(textLine.font, buf.toString())
                    .setFallbackFont(textLine.fallbackFont)
                    .setLocation(xText, yText + textLine.getVerticalOffset())
                    .setColor(textLine.getColor())
                    .setUnderline(textLine.getUnderline())
                    .setStrikeout(textLine.getStrikeout())
                    .setLanguage(textLine.getLanguage())
                    .drawOn(page);
        }

        return new float[] {
                xText + textLine.font.stringWidth(textLine.fallbackFont, buf.toString()),
                yText};
    }


    private boolean stringIsCJK(String str) {
        // CJK Unified Ideographs Range: 4E00–9FD5
        // Hiragana Range: 3040–309F
        // Katakana Range: 30A0–30FF
        // Hangul Jamo Range: 1100–11FF
        int numOfCJK = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if ((ch >= 0x4E00 && ch <= 0x9FD5) ||
                    (ch >= 0x3040 && ch <= 0x309F) ||
                    (ch >= 0x30A0 && ch <= 0x30FF) ||
                    (ch >= 0x1100 && ch <= 0x11FF)) {
                numOfCJK += 1;
            }
        }
        return (numOfCJK > (str.length() / 2));
    }


    private String[] tokenizeCJK(TextLine textLine, float textWidth) {
        List<String> list = new ArrayList<String>();
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < textLine.text.length(); i++) {
            char ch = textLine.text.charAt(i);
            if (textLine.font.stringWidth(textLine.fallbackFont, buf.toString() + ch) < textWidth) {
                buf.append(ch);
            }
            else {
                list.add(buf.toString());
                buf.setLength(0);
                buf.append(ch);
            }
        }
        if (buf.toString().length() > 0) {
            list.add(buf.toString());
        }
        return list.toArray(new String[] {});
    }

}   // End of Text.java
