/**
 *  CompositeTextLine.java
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
 *  This class was designed and implemented by Jon T. Swanson, Ph.D.
 *
 *  Refactored and integrated into the project by Eugene Dragoev - 2nd June 2012.
 *  Used to create composite text line objects.
 */
public class CompositeTextLine implements Drawable {

    private static final int X = 0;
    private static final int Y = 1;

    private List<TextLine> textLines = new ArrayList<TextLine>();

    private float[] position = new float[2];
    private float[] current  = new float[2];

    // Subscript and Superscript size factors
    private float subscriptSizeFactor    = 0.583f;
    private float superscriptSizeFactor  = 0.583f;

    // Subscript and Superscript positions in relation to the base font
    private float superscriptPosition = 0.350f;
    private float subscriptPosition   = 0.141f;

    private float fontSize = 0f;


    public CompositeTextLine(float x, float y) {
        position[X] = x;
        position[Y] = y;
        current[X]  = x;
        current[Y]  = y;
    }


    /**
     *  Sets the font size.
     *
     *  @param fontSize the font size.
     */
    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }


    /**
     *  Gets the font size.
     *
     *  @return fontSize the font size.
     */
    public float getFontSize() {
        return fontSize;
    }


    /**
     *  Sets the superscript factor for this composite text line.
     *
     *  @param superscript the superscript size factor.
     */
    public void setSuperscriptFactor(float superscript) {
        this.superscriptSizeFactor = superscript;
    }


    /**
     *  Gets the superscript factor for this text line.
     *
     *  @return superscript the superscript size factor.
     */
    public float getSuperscriptFactor() {
        return superscriptSizeFactor;
    }


    /**
     *  Sets the subscript factor for this composite text line.
     *
     *  @param subscript the subscript size factor.
     */
    public void setSubscriptFactor(float subscript) {
        this.subscriptSizeFactor = subscript;
    }


    /**
     *  Gets the subscript factor for this text line.
     *
     *  @return subscript the subscript size factor.
     */
    public float getSubscriptFactor() {
        return subscriptSizeFactor;
    }


    /**
     *  Sets the superscript position for this composite text line.
     *
     *  @param superscriptPosition the superscript position.
     */
    public void setSuperscriptPosition(float superscriptPosition) {
        this.superscriptPosition = superscriptPosition;
    }


    /**
     *  Gets the superscript position for this text line.
     *
     *  @return superscriptPosition the superscript position.
     */
    public float getSuperscriptPosition() {
        return superscriptPosition;
    }


    /**
     *  Sets the subscript position for this composite text line.
     *
     *  @param subscriptPosition the subscript position.
     */
    public void setSubscriptPosition(float subscriptPosition) {
        this.subscriptPosition = subscriptPosition;
    }


    /**
     *  Gets the subscript position for this text line.
     *
     *  @return subscriptPosition the subscript position.
     */
    public float getSubscriptPosition() {
        return subscriptPosition;
    }


    /**
     *  Add a new text line.
     *
     *  Find the current font, current size and effects (normal, super or subscript)
     *  Set the position of the component to the starting stored as current position
     *  Set the size and offset based on effects
     *  Set the new current position
     *
     *  @param component the component.
     */
    public void addComponent(TextLine component) {
        if (component.getTextEffect() == Effect.SUPERSCRIPT) {
            if (fontSize > 0f) {
                component.font.setSize(fontSize * superscriptSizeFactor);
            }
            component.setLocation(
                    current[X],
                    current[Y] - fontSize * superscriptPosition);
        }
        else if (component.getTextEffect() == Effect.SUBSCRIPT) {
            if (fontSize > 0f) {
                component.font.setSize(fontSize * subscriptSizeFactor);
            }
            component.setLocation(
                    current[X],
                    current[Y] + fontSize * subscriptPosition);
        }
        else {
            if (fontSize > 0f) {
                component.getFont().setSize(fontSize);
            }
            component.setLocation(current[X], current[Y]);
        }
        current[X] += component.getWidth();
        textLines.add(component);
    }


    /**
     *  Loop through all the text lines and reset their position based on
     *  the new position set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

     /**
     *  Loop through all the text lines and reset their position based on
     *  the new position set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
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
     *  Loop through all the text lines and reset their location based on
     *  the new location set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     *  @return the CompositeTextLine object.
     */
    public CompositeTextLine setLocation(float x, float y) {
        position[X] = x;
        position[Y] = y;
        current[X]  = x;
        current[Y]  = y;

        if (textLines == null || textLines.size() == 0) {
            return this;
        }

        for (TextLine component : textLines) {
            if (component.getTextEffect() == Effect.SUPERSCRIPT) {
                component.setLocation(
                        current[X],
                        current[Y] - fontSize * superscriptPosition);
            }
            else if (component.getTextEffect() == Effect.SUBSCRIPT) {
                component.setLocation(
                        current[X],
                        current[Y] + fontSize * subscriptPosition);
            }
            else {
                component.setLocation(current[X], current[Y]);
            }
            current[X] += component.getWidth();
        }
        return this;
    }

    /**
     *  Loop through all the text lines and reset their location based on
     *  the new location set here.
     *
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     *  @return the CompositeTextLine object.
     */
    public CompositeTextLine setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Return the position of this composite text line.
     *
     *  @return the position of this composite text line.
     */
    public float[] getPosition() {
        return position;
    }


    /**
     *  Return the nth entry in the TextLine array.
     *
     *  @param index the index of the nth element.
     *  @return the text line at the specified index.
     */
    public TextLine getTextLine(int index) {
        if (textLines == null || textLines.size() == 0) {
            return null;
        }
        if (index < 0 || index > textLines.size() - 1) {
            return null;
        }
        return textLines.get(index);
    }


    /**
     *  Returns the number of text lines.
     *
     *  @return the number of text lines.
     */
    public int getNumberOfTextLines() {
       return textLines.size();
    }


    /**
     *  Returns the vertical coordinates of the top left and bottom right corners
     *  of the bounding box of this composite text line.
     *
     *  @return the an array containing the vertical coordinates.
     */
    public float[] getMinMax() {
        float min = position[Y];
        float max = position[Y];
        float cur;

        for (TextLine component : textLines) {
            if (component.getTextEffect() == Effect.SUPERSCRIPT) {
                cur = (position[Y] - component.font.ascent) - fontSize * superscriptPosition;
                if (cur < min)
                    min = cur;
            }
            else if (component.getTextEffect() == Effect.SUBSCRIPT) {
                cur = (position[Y] + component.font.descent) + fontSize * subscriptPosition;
                if (cur > max)
                    max = cur;
            }
            else {
                cur = position[Y] - component.font.ascent;
                if (cur < min)
                    min = cur;
                cur = position[Y] + component.font.descent;
                if (cur > max)
                    max = cur;
            }
        }

        return new float[] {min, max};
    }


    /**
     *  Returns the height of this CompositeTextLine.
     *
     *  @return the height.
     */
    public float getHeight() {
        float[] yy = getMinMax();
        return yy[1] - yy[0];
    }


    /**
     *  Returns the width of this CompositeTextLine.
     *
     *  @return the width.
     */
    public float getWidth() {
        return (current[X] - position[X]);
    }


    /**
     *  Draws this line on the specified page.
     *
     *  @param page the page to draw this line on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        float xMax = 0f;
        float yMax = 0f;
        // Loop through all the text lines and draw them on the page
        for (TextLine textLine : textLines) {
            float[] xy = textLine.drawOn(page);
            xMax = Math.max(xMax, xy[0]);
            yMax = Math.max(yMax, xy[1]);
        }
        return new float[] {xMax, yMax};
    }

}
