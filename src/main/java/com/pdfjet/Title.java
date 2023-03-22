/**
 *  Title.java
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


/**
 * Please see Example_51 and Example_52
 *
 */
public class Title implements Drawable {
    public TextLine prefix;
    public TextLine textLine;


    public Title(Font font, String title, float x, float y) {
        this.prefix = new TextLine(font);
        this.prefix.setLocation(x, y);
        this.textLine = new TextLine(font, title);
        this.textLine.setLocation(x, y);
    }


    public Title setPrefix(String text) {
        prefix.setText(text);
        return this;
    }


    public Title setOffset(float offset) {
        textLine.setLocation(textLine.x + offset, textLine.y);
        return this;
    }

    public void setPosition(float x, float y) {
        prefix.setLocation(x, y);
        textLine.setLocation(x, y);
    }

    public void setPosition(double x, double y) {
        setPosition(x, y);
    }


    public Title setLocation(float x, float y) {
        textLine.setLocation(x, y);
        return this;
    }

    public Title setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    public float[] drawOn(Page page) throws Exception {
        if (!prefix.equals("")) {
            prefix.drawOn(page);
        }
        return textLine.drawOn(page);
    }

}
