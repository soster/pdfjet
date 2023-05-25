/**
 *  GraphicsState.java
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
 *  The graphics state class
 *
 */
public class GraphicsState {
    // Default values
    private float CA = 1f;
    private float ca = 1f;

    /**
     * Set the alpha stroking color
     *
     * @param CA the alpha stroking color
     */
    public void setAlphaStroking(float CA) {
        if (CA >= 0f && CA <= 1f) {
            this.CA = CA;
        }
    }

    /**
     * Get the stroking alpha color
     *
     * @return the stroking alpha color
     */
    public float getAlphaStroking() {
        return this.CA;
    }

    /**
     * Set the non stroking alpha color
     *
     * @param ca the non stroking alpha color
     */
    public void setAlphaNonStroking(float ca) {
        if (ca >= 0f && ca <= 1f) {
            this.ca = ca;
        }
    }

    /**
     * Get the non stroking alpha color
     *
     * @return the non stroking alpha color
     */
    public float getAlphaNonStroking() {
        return this.ca;
    }
}
