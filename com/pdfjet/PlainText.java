package com.pdfjet;

import java.util.ArrayList;
import java.util.List;

/**
 *  PlainText.java
 *
 Copyright (c) 2015, Innovatics Inc.
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

/**
 *  Please see Example_45
 */
public class PlainText
        implements Drawable {
    private Font font;
    private String[] textLines;
    private float fontSize;
    private float x;
    private float y;
    private float w = 500.0F;
    private float leading;
    private int backgroundColor = 16777215;
    private int borderColor = 16777215;
    private int textColor = 0;
    private List<float[]> endOfLinePoints = null;

    private String language = null;
    private String altDescription = null;
    private String actualText = null;

    public PlainText(Font font, String[] textLines) {
        this.font = font;
        this.fontSize = font.getSize();
        this.textLines = textLines;
        this.endOfLinePoints = new ArrayList();
        StringBuilder localStringBuilder = new StringBuilder();
        for (String str : textLines) {
            localStringBuilder.append(str);
            localStringBuilder.append(' ');
        }
        this.altDescription = localStringBuilder.toString();
        this.actualText = localStringBuilder.toString();
    }

    public PlainText setFontSize(float fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public PlainText setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public PlainText setWidth(float width) {
        this.w = width;
        return this;
    }

    public PlainText setLeading(float leading) {
        this.leading = leading;
        return this;
    }

    public PlainText setBackgroundColor(int color) {
        this.backgroundColor = color;
        return this;
    }

    public PlainText setBorderColor(int color) {
        this.borderColor = color;
        return this;
    }

    public PlainText setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public List<float[]> getEndOfLinePoints() {
        return this.endOfLinePoints;
    }

    /**
     *  Draws this PlainText on the specified page.
     *
     *  @param page the page to draw this PlainText on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page)
            throws Exception {
        float originalSize = this.font.getSize();
        this.font.setSize(this.fontSize);
        float y_text = this.y + this.font.getAscent();

        page.addBMC("Span", this.language, " ", " ");
        page.setBrushColor(this.backgroundColor);
        this.leading = this.font.getBodyHeight();
        float f3 = this.font.getBodyHeight() * this.textLines.length;
        page.fillRect(this.x, this.y, this.w, f3);
        page.setPenColor(this.borderColor);
        page.setPenWidth(0.0F);
        page.drawRect(this.x, this.y, this.w, f3);
        page.addEMC();

        page.addBMC("Span", this.language, this.altDescription, this.actualText);
        page.setTextStart();
        page.setTextFont(this.font);
        page.setBrushColor(this.textColor);
        page.setTextLeading(this.leading);
        page.setTextLocation(this.x, y_text);
        for (String str : this.textLines) {
            if (this.font.skew15) {
                setTextSkew(page, 0.26F, this.x, y_text);
            }
            page.println(str);
            this.endOfLinePoints.add(new float[]{this.x + this.font.stringWidth(str), y_text});
            y_text += this.leading;
        }
        page.setTextEnd();
        page.addEMC();

        this.font.setSize(originalSize);

        return new float[]{this.x + this.w, this.y + f3};
    }

    private void setTextSkew(Page page, float skew, float x, float y)
            throws Exception {
        page.append(1.0F);
        page.append(' ');
        page.append(0.0F);
        page.append(' ');
        page.append(skew);
        page.append(' ');
        page.append(1.0F);
        page.append(' ');
        page.append(x);
        page.append(' ');
        page.append(page.height - y);
        page.append(" Tm\n");
    }
}


