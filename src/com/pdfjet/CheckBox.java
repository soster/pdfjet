/**
 *  CheckBox.java
 *
Copyright (c) 2014, Innovatics Inc.
All rights reserved.

Portions provided by Shirley C. Christenson
Shirley Christenson Consulting

Redistribution and use in source and binary forms, with or without modification,
are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice,
      this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above copyright notice,
      this list of conditions and the following disclaimer in the documentation
      and / or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package com.pdfjet;


/**
 *  Creates a CheckBox, which can be set checked or unchecked.
 *  Default is checked, with a blue check mark.
 *  Default box is black, default font size is 14.0f.
 */
public class CheckBox {

    private boolean boxChecked = true;
    private float x;
    private float y;
    private float w = 12.0f;
    private float h = 12.0f;
    private int checkColor = Color.blue;
    private int boxColor = Color.black;
    private float penWidth = 0.3f;
    private float checkWidth = 3.0f;
    private int mark = 1;
    private Font font = null;
    private String text = null;
    private String uri = null;


    /**
     *  Creates a CheckBox with blue check mark.
     *
     */
    public CheckBox() {
    }


    public CheckBox(Font font, String text) {
        this.font = font;
        this.text = text;
        this.boxChecked = false;
    }


    /**
     *  Creates a CheckBox.
     *
     *  @param boxChecked boolean - true or false. Default is true. 
     *  @param checkColor the color of the check mark. Default is blue.
     */
    public CheckBox(boolean boxChecked, int checkColor) {
        this.boxChecked = boxChecked;
        this.checkColor = checkColor;
    }


    /**
     *  Creates a CheckBox.
     *
     *  @param boxChecked boolean - If true box is checked. If false no check mark.
     *  Use default green check mark. 
     */
    public CheckBox(boolean boxChecked) {
        this.boxChecked = boxChecked;
    }


    /**
     *  Sets the color of the check mark.
     *
     *  @param checkColor the check mark color specified as an 0xRRGGBB integer.
     */
    public void setCheckColor(int checkColor) {
        this.checkColor = checkColor;
    }


    /**
     *  Sets the color of the check box.
     *
     *  @param boxColor the check box color specified as an 0xRRGGBB integer.
     */
    public void setBoxColor(int boxColor) {
        this.boxColor = boxColor;
    }


    /**
     *  Set the x,y position on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setPosition(double x, double y) {
    	setPosition((float) x, (float) y);
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
     *  Set the x,y location on the Page.
     *
     *  @param x the x coordinate on the Page.
     *  @param y the y coordinate on the Page.
     */
    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }


    /**
     *  Set the size of the CheckBox.
     *
     *  @param size the size of the CheckBox.
     */
    public void setSize(double size) {
    	setSize((float) size);
    }


    /**
     *  Set the size of the CheckBox.
     *
     *  @param size the size of the CheckBox.
     */
    public void setSize(float size) {
        this.h = size;
        this.w = size;
        this.checkWidth = size / 4.0f;
        this.penWidth = size / 40.0f;
    }

    
    /**
     *  Sets the type of check mark.
     *
     *  @param mark the type of check mark.
     *  1 = check (the default)
     *  2 = X
     *  
     */
    public void setMarkType(int mark) {
    	if (mark > 0 && mark < 3) {
    		this.mark = mark;
    	}
    }


    /**
     *  Gets the height of the CheckBox.
     *
     */
    public float getHeight() {
        return this.h;
    }


    /**
     *  Gets the width of the CheckBox.
     *
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Get the x coordinate of the upper left corner.
     *
     */
    public float getX() {
        return this.x;
    }


    /**
     *  Get the y coordinate of the upper left corner.
     *
     */
    public float getY() {
        return this.y;
    }


    public void setChecked(boolean boxChecked) {
        this.boxChecked = boxChecked;
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI.
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }


    /**
     *  Draws this CheckBox on the specified Page.
     *
     *  @param page the Page where the CheckBox is to be drawn.
     */
    public void drawOn(Page page) throws Exception {

        page.setPenWidth(penWidth);
        page.moveTo(x, y);
        page.lineTo(x + w, y);
        page.lineTo(x + w, y + h);
        page.lineTo(x, y + h);
        page.closePath();
        page.setPenColor(boxColor);
        page.strokePath();
        
        if (this.boxChecked) {
        	page.setPenWidth(checkWidth);
        	if (mark == 1) {
                // Draw check mark
        		page.moveTo(x + checkWidth/2, y + h/2);
        		page.lineTo(x + w/3, (y + h) - checkWidth/2);
        		page.lineTo(x + w - checkWidth/2, y + checkWidth/2);
        	}
        	else {
                // Draw 'X' mark
        		page.moveTo(x + checkWidth/2, y + checkWidth/2);
        		page.lineTo((x + w) - checkWidth/2, (y + h) - checkWidth/2);
        		page.moveTo((x + w) - checkWidth/2, y + checkWidth/2);
        		page.lineTo(x + checkWidth/2, (y + h) - checkWidth/2);
        	}
        	page.setPenColor(checkColor);
        	page.setLineCapStyle(Cap.ROUND);
        	page.strokePath();
        }

        if (font != null && text != null) {
            float x_text = x + 5f*w/4f;
            float y_text = (page.height - y) - 7f*h/8f;
            page.drawString(font, text, x_text, y + h);
            if (uri != null) {
                // Please note: The font descent is a negative number.
                page.annots.add(new Annotation(
                        uri,
                        text,   // The destination name
                        x_text,
                        y_text + font.descent,
                        x_text + font.stringWidth(text),
                        y_text + font.ascent));
            }
        }

        page.setPenWidth(0f);
        page.setPenColor(Color.black);
    }

}   // End of CheckBox.java
