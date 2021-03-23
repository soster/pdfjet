/**
 *  Point.java
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


/**
 *  Used to create point objects with different shapes and draw them on a page.
 *  Please note: When we are mentioning (x, y) coordinates of a point - we are talking about the coordinates of the center of the point.
 *
 *  Please see Example_05.
 */
public class Point implements Drawable {

    public static final int INVISIBLE = -1;
    public static final int CIRCLE = 0;
    public static final int DIAMOND = 1;
    public static final int BOX = 2;
    public static final int PLUS = 3;
    public static final int H_DASH = 4;
    public static final int V_DASH = 5;
    public static final int MULTIPLY = 6;
    public static final int STAR = 7;
    public static final int X_MARK = 8;
    public static final int UP_ARROW = 9;
    public static final int DOWN_ARROW = 10;
    public static final int LEFT_ARROW = 11;
    public static final int RIGHT_ARROW = 12;

    public static final boolean CONTROL_POINT = true;

    protected float x;
    protected float y;
    protected float r = 2f;
    protected int shape = Point.CIRCLE;
    protected int color = Color.black;
    protected int align = Align.RIGHT;
    protected float lineWidth = 0.3f;
    protected String linePattern = "[] 0";
    protected boolean fillShape = false;
    protected boolean isControlPoint = false;
    protected boolean drawPath = false;

    private String text;
    private int textColor;
    private int textDirection;
    private String uri;
    private float xBox;
    private float yBox;

    /**
     *  The default constructor.
     */
    public Point() {
    }


    /**
     *  Constructor for creating point objects.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public Point(double x, double y) {
        this((float) x, (float) y);
    }


    /**
     *  Constructor for creating point objects.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public Point(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     *  Constructor for creating point objects.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     *  @param isControlPoint true if this point is one of the points specifying a curve.
     */
    public Point(double x, double y, boolean isControlPoint) {
        this((float) x, (float) y, isControlPoint);
    }


    /**
     *  Constructor for creating point objects.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     *  @param isControlPoint true if this point is one of the points specifying a curve.
     */
    public Point(float x, float y, boolean isControlPoint) {
        this.x = x;
        this.y = y;
        this.isControlPoint = isControlPoint;
    }


    /**
     *  Sets the position (x, y) of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Sets the position (x, y) of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }

    public void setXY(float x, float y) {
        setLocation(x, y);
    }

    public void setXY(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location (x, y) of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     *  @return the location of the point.
     */
    public Point setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     *  Sets the location (x, y) of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     *  @param y the y coordinate of this point when drawn on the page.
     *  @return the location of the point.
     */
    public Point setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Sets the x coordinate of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     */
    public void setX(double x) {
        this.x = (float) x;
    }


    /**
     *  Sets the x coordinate of this point.
     *
     *  @param x the x coordinate of this point when drawn on the page.
     */
    public void setX(float x) {
        this.x = x;
    }


    /**
     *  Returns the x coordinate of this point.
     *
     *  @return the x coordinate of this point.
     */
    public float getX() {
        return x;
    }


    /**
     *  Sets the y coordinate of this point.
     *
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public void setY(double y) {
        this.y = (float) y;
    }


    /**
     *  Sets the y coordinate of this point.
     *
     *  @param y the y coordinate of this point when drawn on the page.
     */
    public void setY(float y) {
        this.y = y;
    }


    /**
     *  Returns the y coordinate of this point.
     *
     *  @return the y coordinate of this point.
     */
    public float getY() {
        return y;
    }


    /**
     *  Sets the radius of this point.
     *
     *  @param r the radius.
     */
    public void setRadius(double r) {
        this.r = (float) r;
    }


    /**
     *  Sets the radius of this point.
     *
     *  @param r the radius.
     */
    public void setRadius(float r) {
        this.r = r;
    }


    /**
     *  Returns the radius of this point.
     *
     *  @return the radius of this point.
     */
    public float getRadius() {
        return r;
    }


    /**
     *  Sets the shape of this point.
     *
     *  @param shape the shape of this point. Supported values:
     *  <pre>
     *  Point.INVISIBLE
     *  Point.CIRCLE
     *  Point.DIAMOND
     *  Point.BOX
     *  Point.PLUS
     *  Point.H_DASH
     *  Point.V_DASH
     *  Point.MULTIPLY
     *  Point.STAR
     *  Point.X_MARK
     *  Point.UP_ARROW
     *  Point.DOWN_ARROW
     *  Point.LEFT_ARROW
     *  Point.RIGHT_ARROW
     *  </pre>
     */
    public void setShape(int shape) {
        this.shape = shape;
    }


    /**
     *  Returns the point shape code value.
     *
     *  @return the shape code value.
     */
    public int getShape() {
        return shape;
    }


    /**
     *  Sets the private fillShape variable.
     *
     *  @param fillShape if true - fill the point with the specified brush color.
     */
    public void setFillShape(boolean fillShape) {
        this.fillShape = fillShape;
    }


    /**
     *  Returns the value of the fillShape private variable.
     *
     *  @return the value of the private fillShape variable.
     */
    public boolean getFillShape() {
        return this.fillShape;
    }


    /**
     *  Sets the pen color for this point.
     *
     *  @param color the color specified as an integer.
     *  @return the point.
     */
    public Point setColor(int color) {
        this.color = color;
        return this;
    }


    /**
     *  Returns the point color as an integer.
     *
     *  @return the color.
     */
    public int getColor() {
        return this.color;
    }


    /**
     *  Sets the width of the lines of this point.
     *
     *  @param lineWidth the line width.
     */
    public void setLineWidth(double lineWidth) {
        this.lineWidth = (float) lineWidth;
    }


    /**
     *  Sets the width of the lines of this point.
     *
     *  @param lineWidth the line width.
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }


    /**
     *  Returns the width of the lines used to draw this point.
     *
     *  @return the width of the lines used to draw this point.
     */
    public float getLineWidth() {
        return lineWidth;
    }


    /**
     *
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
     *  @param linePattern the line dash pattern.
     */
    public void setLinePattern(String linePattern) {
        this.linePattern = linePattern;
    }


    /**
     *  Returns the line dash pattern.
     *
     *  @return the line dash pattern.
     */
    public String getLinePattern() {
        return linePattern;
    }


    /**
     *  Sets this point as the start of a path that will be drawn on the chart.
     *
     *  @return the point.
     */
    public Point setDrawPath() {
        this.drawPath = true;
        return this;
    }


    /**
     *  Sets the URI for the "click point" action.
     *
     *  @param uri the URI
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }


    /**
     *  Returns the URI for the "click point" action.
     *
     *  @return the URI for the "click point" action.
     */
    public String getURIAction() {
        return uri;
    }


    /**
     *  Sets the point text.
     *
     *  @param text the text.
     */
    public void setText(String text) {
        this.text = text;
    }


    /**
     *  Returns the text associated with this point.
     *
     *  @return the text.
     */
    public String getText() {
        return this.text;
    }


    /**
     *  Sets the point's text color.
     *
     *  @param textColor the text color.
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }


    /**
     *  Returns the point's text color.
     *
     *  @return the text color.
     */
    public int getTextColor() {
        return this.textColor;
    }


    /**
     *  Sets the point's text direction.
     *
     *  @param textDirection the text direction.
     */
    public void setTextDirection(int textDirection) {
        this.textDirection = textDirection;
    }


    /**
     *  Returns the point's text direction.
     *
     *  @return the text direction.
     */
    public int getTextDirection() {
        return this.textDirection;
    }


    /**
     *  Sets the point alignment inside table cell.
     *
     *  @param align the alignment value.
     */
    public void setAlignment(int align) {
        this.align = align;
    }


    /**
     *  Returns the point alignment.
     *
     *  @return align the alignment value.
     */
    public int getAlignment() {
        return this.align;
    }


    /**
     *  Places this point in the specified box at position (0f, 0f).
     *
     *  @param box the specified box.
     */
    public void placeIn(Box box) {
        placeIn(box, 0f, 0f);
    }


    /**
     *  Places this point in the specified box.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     */
    public void placeIn(
            Box box,
            double xOffset,
            double yOffset) {
        placeIn(box, (float) xOffset, (float) yOffset);
    }


    /**
     *  Places this point in the specified box.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     */
    public void placeIn(
            Box box,
            float xOffset,
            float yOffset) {
        xBox = box.x + xOffset;
        yBox = box.y + yOffset;
    }


    /**
     *  Draws this point on the specified page.
     *
     *  @param page the page to draw this point on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        page.setPenWidth(lineWidth);
        page.setLinePattern(linePattern);

        if (fillShape) {
            page.setBrushColor(color);
        }
        else {
            page.setPenColor(color);
        }

        x += xBox;
        y += yBox;
        page.drawPoint(this);
        x -= xBox;
        y -= yBox;

        return new float[] {x + xBox + r, y + yBox + r};
    }

}   // End of Point.java
