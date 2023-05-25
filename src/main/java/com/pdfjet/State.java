/**
 *  State.java
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

class State {
    private float[] pen;
    private float[] brush;
    private float penWidth;
    private CapStyle lineCapStyle;
    private JoinStyle lineJoinStyle;
    private String linePattern;

    public State(
            float[] pen,
            float[] brush,
            float penWidth,
            CapStyle lineCapStyle,
            JoinStyle lineJoinStyle,
            String linePattern) {
        this.pen = new float[] { pen[0], pen[1], pen[2] };
        this.brush = new float[] { brush[0], brush[1], brush[2] };
        this.penWidth = penWidth;
        this.lineCapStyle = lineCapStyle;
        this.lineJoinStyle = lineJoinStyle;
        this.linePattern = linePattern;
    }

    public float[] getPen() {
        return pen;
    }

    public float[] getBrush() {
        return brush;
    }

    public float getPenWidth() {
        return penWidth;
    }

    public CapStyle getLineCapStyle() {
        return lineCapStyle;
    }

    public JoinStyle getLineJoinStyle() {
        return lineJoinStyle;
    }

    public String getLinePattern() {
        return linePattern;
    }
}   // End of State.java
