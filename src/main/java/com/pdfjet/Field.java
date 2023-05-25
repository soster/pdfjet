/**
 *  Field.java
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
 *  Please see Example_45
 */
public class Field {
    float x;
    String[] values;
    String[] actualText;
    String[] altDescription;
    boolean format = false;

    /**
     * Creates a Field that will be used in a Form
     *
     * @param x the horizontal position within the Form
     * @param values the values contained in this field
     */
    public Field(float x, String[] values) {
        this(x, values, false);
    }

    /**
     * Creates a Field that will be used in a Form
     *
     * @param x the horizontal position within the Form
     * @param values the values contained in this field
     * @param format format the value or not ...
     */
    public Field(float x, String[] values, boolean format) {
        this.x = x;
        this.values = values;
        this.format = format;
        if (values != null) {
            this.actualText     = new String[values.length];
            this.altDescription = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                this.actualText[i]     = values[i];
                this.altDescription[i] = values[i];
            }
        }
    }

    /**
     * Sets the alternative description for this field
     *
     * @param altDescription the alternative description
     * @return this field
     */
    public Field setAltDescription(String altDescription) {
        this.altDescription[0] = altDescription;
        return this;
    }

    /**
     * Sets the actual text for the field
     *
     * @param actualText the actual text in the field
     * @return this field
     */
    public Field setActualText(String actualText) {
        this.actualText[0] = actualText;
        return this;
    }
}
