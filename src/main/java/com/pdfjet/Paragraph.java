/**
 *  Paragraph.java
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
 *  Used to create paragraph objects.
 *  See the TextColumn class for more information.
 *
 */
public class Paragraph {
    public float xText;
    public float yText;
    public float x1;
    public float y1;
    public float x2;
    public float y2;
    protected List<TextLine> lines = null;
    protected int alignment = Align.LEFT;

    /**
     *  Constructor for creating paragraph objects.
     *
     */
    public Paragraph() {
        lines = new ArrayList<TextLine>();
    }

    public Paragraph(TextLine text) {
        lines = new ArrayList<TextLine>();
        lines.add(text);
    }

    /**
     *  Adds a text line to this paragraph.
     *
     *  @param text the text line to add to this paragraph.
     *  @return this paragraph.
     */
    public Paragraph add(TextLine text) {
        lines.add(text);
        return this;
    }

    /**
     *  Sets the alignment of the text in this paragraph.
     *
     *  @param alignment the alignment code.
     *  @return this paragraph.
     *
     *  <pre>Supported values: Align.LEFT, Align.RIGHT, Align.CENTER and Align.JUSTIFY.</pre>
     */
    public Paragraph setAlignment(int alignment) {
        this.alignment = alignment;
        return this;
    }

    public List<TextLine> getTextLines() {
        return lines;
    }

    public boolean startsWith(String token) {
        return lines.get(0).getText().startsWith(token);
    }

    public void setColor(int color) {
        for (TextLine line : lines) {
            line.setColor(color);
        }
    }

    public void setColorMap(Map<String, Integer> colorMap) {
        for (TextLine line : lines) {
            line.setColorMap(colorMap);
        }
    }
}   // End of Paragraph.java
