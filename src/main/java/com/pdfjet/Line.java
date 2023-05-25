/**
 *  Line.java
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
 *  Used to create line objects.
 *
 *  Please see Example_01.
 */
public class Line implements Drawable {
    private float x1;
    private float y1;
    private float x2;
    private float y2;

    private float xBox;
    private float yBox;

    private int color = Color.black;
    private float width = 0.3f;
    private String pattern = "[] 0";
    private CapStyle capStyle = CapStyle.BUTT;

    private String language = null;
    private String altDescription = Single.space;
    private String actualText = Single.space;

    /**
     *  The default constructor.
     *
     *
     */
    public Line() {
    }

    /**
     *  Create a line object.
     *
     *  @param x1 the x coordinate of the start point.
     *  @param y1 the y coordinate of the start point.
     *  @param x2 the x coordinate of the end point.
     *  @param y2 the y coordinate of the end point.
     */
    public Line(double x1, double y1, double x2, double y2) {
        this.x1 = (float) x1;
        this.y1 = (float) y1;
        this.x2 = (float) x2;
        this.y2 = (float) y2;
    }

    /**
     *  Create a line object.
     *
     *  @param x1 the x coordinate of the start point.
     *  @param y1 the y coordinate of the start point.
     *  @param x2 the x coordinate of the end point.
     *  @param y2 the y coordinate of the end point.
     */
    public Line(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
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
     *  @return this Line object.
     */
    public Line setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setStartPoint(double x, double y) {
        this.x1 = (float) x;
        this.y1 = (float) y;
        return this;
    }

    public void setPosition(float x, float y) {
        setStartPoint(x, y);
    }

    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setStartPoint(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }

    /**
     *  Sets the x and y coordinates of the start point.
     *
     *  @param x the x coordinate of the start point.
     *  @param y the y coordinate of the start point.
     *  @return this Line object.
     */
    public Line setPointA(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }

    /**
     *  Returns the start point of this line.
     *
     *  @return Point the point.
     */
    public Point getStartPoint() {
        return new Point(x1, y1);
    }

    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the y coordinate of the end point.
     *  @return this Line object.
     */
    public Line setEndPoint(double x, double y) {
        this.x2 = (float) x;
        this.y2 = (float) y;
        return this;
    }

    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the t coordinate of the end point.
     *  @return this Line object.
     */
    public Line setEndPoint(float x, float y) {
        this.x2 = x;
        this.y2 = y;
        return this;
    }

    /**
     *  Sets the x and y coordinates of the end point.
     *
     *  @param x the x coordinate of the end point.
     *  @param y the t coordinate of the end point.
     *  @return this Line object.
     */
    public Line setPointB(float x, float y) {
        this.x2 = x;
        this.y2 = y;
        return this;
    }

    /**
     *  Returns the end point of this line.
     *
     *  @return Point the point.
     */
    public Point getEndPoint() {
        return new Point(x2, y2);
    }

    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     *  @return this Line object.
     */
    public Line setWidth(double width) {
        this.width = (float) width;
        return this;
    }

    /**
     *  Sets the width of this line.
     *
     *  @param width the width.
     *  @return this Line object.
     */
    public Line setWidth(float width) {
        this.width = width;
        return this;
    }

    /**
     *  Sets the color for this line.
     *
     *  @param color the color specified as an integer.
     *  @return this Line object.
     */
    public Line setColor(int color) {
        this.color = color;
        return this;
    }

    /**
     *  Sets the line cap style.
     *
     *  @param style the cap style of the current line.
     *  Supported values: CapStyle.BUTT, CapStyle.ROUND and CapStyle.PROJECTING_SQUARE
     *  @return this Line object.
     */
    public Line setCapStyle(CapStyle style) {
        this.capStyle = style;
        return this;
    }

    /**
     *  Returns the line cap style.
     *
     *  @return the cap style.
     */
    public CapStyle getCapStyle() {
        return capStyle;
    }

    /**
     *  Sets the alternate description of this line.
     *
     *  @param altDescription the alternate description of the line.
     *  @return this Line.
     */
    public Line setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }

    /**
     *  Sets the actual text for this line.
     *
     *  @param actualText the actual text for the line.
     *  @return this Line.
     */
    public Line setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }

    /**
     *  Places this line in the specified box at position (0.0f, 0.0f).
     *
     *  @param box the specified box.
     *  @return this Line object.
     */
    public Line placeIn(Box box) {
        return placeIn(box, 0f, 0f);
    }

    /**
     *  Places this line in the specified box.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     *  @return this Line object.
     */
    public Line placeIn(Box box, double xOffset, double yOffset) {
        placeIn(box, (float) xOffset, (float) yOffset);
        return this;
    }

    /**
     *  Places this line in the specified box.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     *  @return this Line object.
     */
    public Line placeIn(Box box, float xOffset, float yOffset) {
        xBox = box.x + xOffset;
        yBox = box.y + yOffset;
        return this;
    }

    /**
     *  Scales this line by the spacified factor.
     *
     *  @param factor the factor used to scale the line.
     *  @return this Line object.
     *  @throws Exception If an input or output exception occurred
     */
    public Line scaleBy(double factor) throws Exception {
        return scaleBy((float) factor);
    }

    /**
     *  Scales this line by the spacified factor.
     *
     *  @param factor the factor used to scale the line.
     *  @return this Line object.
     */
    public Line scaleBy(float factor) {
        this.x1 *= factor;
        this.x2 *= factor;
        this.y1 *= factor;
        this.y2 *= factor;
        return this;
    }

    /**
     *  Draws this line on the specified page.
     *
     *  @param page the page to draw this line on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        page.setPenColor(color);
        page.setPenWidth(width);
        page.setLineCapStyle(capStyle);
        page.setLinePattern(pattern);
        page.addBMC(StructElem.P, language, actualText, altDescription);
        page.drawLine(
                x1 + xBox,
                y1 + yBox,
                x2 + xBox,
                y2 + yBox);
        page.addEMC();

        float xMax = Math.max(x1 + xBox, x2 + xBox);
        float yMax = Math.max(y1 + yBox, y2 + yBox);
        return new float[] {xMax, yMax};
    }
}   // End of Line.java
