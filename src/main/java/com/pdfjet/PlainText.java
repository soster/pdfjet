/**
 *  PlainText.java
 *
Copyright 2020 Innovatics Inc.

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
public class PlainText implements Drawable {

    private Font font;
    private String[] textLines;
    private float fontSize;
    private float x;
    private float y;
    private float w = 500f;
    private float leading;
    private int backgroundColor = Color.white;
    private int borderColor = Color.white;
    private int textColor = Color.black;
    private String language;
    private String actualText;
    private String altDescription;


    public PlainText(Font font, String[] textLines) {
        this.font = font;
        this.fontSize = font.getSize();
        this.textLines = textLines;
        StringBuilder buf = new StringBuilder();
        for (String str : textLines) {
            buf.append(str);
            buf.append(' ');
        }
        this.actualText = buf.toString();
        this.altDescription = buf.toString();
    }


    public PlainText setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }


    public void setPosition(double x, double y) {
        setLocation((float) x, (float) y);
    }


    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    public PlainText setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    public PlainText setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public PlainText setWidth(float w) {
        this.w = w;
        return this;
    }


    public PlainText setLeading(float leading) {
        this.leading = leading;
        return this;
    }


    public PlainText setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }


    public PlainText setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        return this;
    }


    public PlainText setTextColor(int textColor) {
        this.textColor = textColor;
        return this;
    }


    /**
     *  Draws this PlainText on the specified page.
     *
     *  @param page the page to draw this PlainText on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        float originalSize = font.getSize();
        font.setSize(fontSize);
        float yText = y + font.getAscent();

        page.addBMC(StructElem.SPAN, language, Single.space, Single.space);
        page.setBrushColor(backgroundColor);
        leading = font.getBodyHeight();
        float h = font.getBodyHeight() * textLines.length;
        page.fillRect(x, y, w, h);
        page.setPenColor(borderColor);
        page.setPenWidth(0f);
        page.drawRect(x, y, w, h);
        page.addEMC();

        page.addBMC(StructElem.SPAN, language, actualText, altDescription);
        page.setTextStart();
        page.setTextFont(font);
        page.setBrushColor(textColor);
        page.setTextLeading(leading);
        page.setTextLocation(x, yText);
        for (String str : textLines) {
            if (font.skew15) {
                setTextSkew(page, 0.26f, x, yText);
            }
            page.println(str);
            yText += leading;
        }
        page.setTextEnd();
        page.addEMC();

        font.setSize(originalSize);

        return new float[] { x + w, y + h };
    }


    private void setTextSkew(Page page, float skew, float x, float y) {
        page.append(1f);
        page.append(' ');
        page.append(0f);
        page.append(' ');
        page.append(skew);
        page.append(' ');
        page.append(1f);
        page.append(' ');
        page.append(x);
        page.append(' ');
        page.append(page.height - y);
        page.append(" Tm\n");
    }

}   // End of PlainText.java
