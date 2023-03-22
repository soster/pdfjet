/**
 *  CheckBox.java
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
 *  Creates a CheckBox, which can be set checked or unchecked.
 *  By default the check box is unchecked.
 *  Portions provided by Shirley C. Christenson
 *  Shirley Christenson Consulting
 */
public class CheckBox implements Drawable {

    private float x;
    private float y;
    private float w;
    private float h;
    private int boxColor = Color.black;
    private int checkColor = Color.black;
    private float penWidth;
    private float checkWidth;
    private int mark = 0;
    private Font font = null;
    private String label = "";
    private String uri = null;

    private final String language = null;
    private String altDescription = Single.space;
    private String actualText = Single.space;


    /**
     *  Creates a CheckBox with black check mark.
     *
     *  @param font the font to use.
     *  @param label the label to use.
     */
    public CheckBox(Font font, String label) {
        this.font = font;
        this.label = label;
    }


    /**
     *  Sets the font size to use for this text line.
     *
     *  @param fontSize the fontSize to use.
     *  @return this CheckBox.
     */
    public CheckBox setFontSize(float fontSize) {
        this.font.setSize(fontSize);
        return this;
    }


    /**
     *  Sets the color of the check box.
     *
     *  @param boxColor the check box color specified as an 0xRRGGBB integer.
     *  @return this CheckBox.
     */
    public CheckBox setBoxColor(int boxColor) {
        this.boxColor = boxColor;
        return this;
    }


    /**
     *  Sets the color of the check mark.
     *
     *  @param checkColor the check mark color specified as an 0xRRGGBB integer.
     *  @return this CheckBox.
     */
    public CheckBox setCheckmark(int checkColor) {
        this.checkColor = checkColor;
        return this;
    }


    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this CheckBox.
     */
    public CheckBox setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     *  @return this CheckBox.
     */
    public CheckBox setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }



    /**
     *  Gets the height of the CheckBox.
     *
     *  @return the height.
     */
    public float getHeight() {
        return this.h;
    }


    /**
     *  Gets the width of the CheckBox.
     *
     *  @return the width.
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Checks or unchecks this check box. See the Mark class for available options.
     *
     *  @param mark indicates whether the box is checked or not.
     *  @return this CheckBox.
     */
    public CheckBox check(int mark) {
        this.mark = mark;
        return this;
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI.
     *  @return this CheckBox.
     */
    public CheckBox setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    /**
     *  Sets the alternate description of this check box.
     *
     *  @param altDescription the alternate description of the check box.
     *  @return this Checkbox.
     */
    public CheckBox setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this check box.
     *
     *  @param actualText the actual text for the check box.
     *  @return this CheckBox.
     */
    public CheckBox setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  Draws this CheckBox on the specified Page.
     *
     *  @param page the Page where the CheckBox is to be drawn.
     */
    public float[] drawOn(Page page) throws Exception {
        page.addBMC(StructElem.P, language, actualText, altDescription);

        this.w = font.getAscent();
        this.h = this.w;
        this.penWidth = this.w/15;
        this.checkWidth = this.w/5;

        float yBox = y;
        page.setPenWidth(penWidth);
        page.setPenColor(boxColor);
        page.setLinePattern("[] 0");
        page.drawRect(x + this.penWidth, yBox + this.penWidth, w, h);

        if (mark == Mark.CHECK || mark == Mark.X) {
            page.setPenWidth(checkWidth);
            page.setPenColor(checkColor);
            if (mark == Mark.CHECK) {
                // Draw check mark
                page.moveTo(x + checkWidth + penWidth, yBox + h/2 + penWidth);
                page.lineTo((x + w/6 + checkWidth) + penWidth, ((yBox + h) - 4f*checkWidth/3f) + penWidth);
                page.lineTo(((x + w) - checkWidth) + penWidth, (yBox + checkWidth) + penWidth);
                page.strokePath();
            }
            else if (mark == Mark.X) {
                // Draw 'X' mark
                page.moveTo(x + checkWidth + penWidth, yBox + checkWidth + penWidth);
                page.lineTo(((x + w) - checkWidth) + penWidth, ((yBox + h) - checkWidth) + penWidth);
                page.moveTo(((x + w) - checkWidth) + penWidth, (yBox + checkWidth) + penWidth);
                page.lineTo((x + checkWidth) + penWidth, ((yBox + h) - checkWidth) + penWidth);
                page.strokePath();
            }
        }

        if (uri != null) {
            page.setBrushColor(Color.blue);
        }
        page.drawString(font, label, x + 3f*w/2f, y + font.ascent);
        page.setPenWidth(0f);
        page.setPenColor(Color.black);
        page.setBrushColor(Color.black);

        page.addEMC();

        if (uri != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    null,
                    x + 3f*w/2f,
                    y,
                    x + 3f*w/2f + font.stringWidth(label),
                    y + font.bodyHeight,
                    language,
                    actualText,
                    altDescription));
        }

        return new float[] { x + 3f*w + font.stringWidth(label), y + font.bodyHeight };
    }

}   // End of CheckBox.java
