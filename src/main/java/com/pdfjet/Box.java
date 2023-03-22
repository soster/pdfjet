/**
 *  Box.java
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
 *  Used to create rectangular boxes on a page.
 *  Also used to for layout purposes. See the placeIn method in the Image and TextLine classes.
 *
 */
public class Box implements Drawable {

    protected float x;
    protected float y;

    private float w;
    private float h;

    private int color = Color.black;

    private float width = 0.3f;
    private String pattern = "[] 0";
    private boolean fillShape = false;

    protected String uri = null;
    protected String key = null;

    private String language = null;
    private String actualText = Single.space;
    private String altDescription = Single.space;

    /**
     *  The default constructor.
     *
     */
    public Box() {
    }


    /**
     *  Creates a box object.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     *  @param w the width of this box.
     *  @param h the height of this box.
     */
    public Box(double x, double y, double w, double h) {
        this.x = (float) x;
        this.y = (float) y;
        this.w = (float) w;
        this.h = (float) h;
    }


    /**
     *  Creates a box object.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     *  @param w the width of this box.
     *  @param h the height of this box.
     */
    public Box(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /**
     *  Sets the position of this box on the page.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     *  Sets the position of this box on the page.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location of this box on the page.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     *  @return this Box object.
     */
    public Box setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    /**
     *  Sets the location of this box on the page.
     *
     *  @param x the x coordinate of the top left corner of this box when drawn on the page.
     *  @param y the y coordinate of the top left corner of this box when drawn on the page.
     *  @return this Box object.
     */
    public Box setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Sets the size of this box.
     *
     *  @param w the width of this box.
     *  @param h the height of this box.
     */
    public void setSize(double w, double h) {
        this.w = (float) w;
        this.h = (float) h;
    }


    /**
     *  Sets the size of this box.
     *
     *  @param w the width of this box.
     *  @param h the height of this box.
     */
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }


    /**
     *  Sets the color for this box.
     *
     *  @param color the color specified as an integer.
     */
    public void setColor(int color) {
        this.color = color;
    }


    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     */
    public void setLineWidth(double width) {
        this.width = (float) width;
    }


    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     */
    public void setLineWidth(float width) {
        this.width = width;
    }


    /**
     *  Sets the URI for the "click box" action.
     *
     *  @param uri the URI
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }


    /**
     *  Sets the destination key for the action.
     *
     *  @param key the destination name.
     */
    public void setGoToAction(String key) {
        this.key = key;
    }


    /**
     *  Sets the alternate description of this box.
     *
     *  @param altDescription the alternate description of the box.
     *  @return this Box.
     */
    public Box setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    /**
     *  Sets the actual text for this box.
     *
     *  @param actualText the actual text for the box.
     *  @return this Box.
     */
    public Box setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }


    /**
     *  The line dash pattern controls the pattern of dashes and gaps used to stroke paths.
     *  It is specified by a dash array and a dash phase.
     *  The elements of the dash array are positive numbers that specify the lengths of
     *  alternating dashes and gaps.
     *  The dash phase specifies the distance into the dash pattern at which to start the dash.
     *  The elements of both the dash array and the dash phase are expressed in user space units.
     *  <pre>
     *  Examples of line dash patterns:
     *
     *      "[Array] Phase"     Appearance          Description
     *      _______________     _________________   ____________________________________
     *
     *      "[] 0"              -----------------   Solid line
     *      "[3] 0"             ---   ---   ---     3 units on, 3 units off, ...
     *      "[2] 1"             -  --  --  --  --   1 on, 2 off, 2 on, 2 off, ...
     *      "[2 1] 0"           -- -- -- -- -- --   2 on, 1 off, 2 on, 1 off, ...
     *      "[3 5] 6"             ---     ---       2 off, 3 on, 5 off, 3 on, 5 off, ...
     *      "[2 3] 11"          -   --   --   --    1 on, 3 off, 2 on, 3 off, 2 on, ...
     *  </pre>
     *
     *  @param pattern the line dash pattern.
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    /**
     *  Sets the private fillShape variable.
     *  If the value of fillShape is true - the box is filled with the current brush color.
     *
     *  @param fillShape the value used to set the private fillShape variable.
     */
    public void setFillShape(boolean fillShape) {
        this.fillShape = fillShape;
    }


    /**
     *  Places this box in the another box.
     *
     *  @param box the other box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     */
    public void placeIn(Box box, double xOffset, double yOffset) {
        placeIn(box, (float) xOffset, (float) yOffset);
    }


    /**
     *  Places this box in the another box.
     *
     *  @param box the other box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     */
    public void placeIn(Box box, float xOffset, float yOffset) {
        this.x = box.x + xOffset;
        this.y = box.y + yOffset;
    }


    /**
     *  Scales this box by the spacified factor.
     *
     *  @param factor the factor used to scale the box.
     */
    public void scaleBy(double factor) {
        scaleBy((float) factor);
    }


    /**
     *  Scales this box by the spacified factor.
     *
     *  @param factor the factor used to scale the box.
     */
    public void scaleBy(float factor) {
        this.x *= factor;
        this.y *= factor;
    }


    /**
     *  Draws this box on the specified page.
     *
     *  @param page the page to draw this box on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        page.addBMC(StructElem.P, language, actualText, altDescription);
        page.setPenWidth(width);
        page.setLinePattern(pattern);
        if (fillShape) {
            page.setBrushColor(color);
        }
        else {
            page.setPenColor(color);
        }
        page.moveTo(x, y);
        page.lineTo(x + w, y);
        page.lineTo(x + w, y + h);
        page.lineTo(x, y + h);
        if (fillShape) {
            page.fillPath();
        }
        else {
            page.closePath();
        }
        page.addEMC();

        if (uri != null || key != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    y,
                    x + w,
                    y + h,
                    language,
                    actualText,
                    altDescription));
        }

        return new float[] {x + w, y + h + width};
    }

}   // End of Box.java
