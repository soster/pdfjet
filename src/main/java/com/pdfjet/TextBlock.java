/**
 *  TextBlock.java
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
 *  Class for creating blocks of text.
 *
 */
public class TextBlock implements Drawable {

    protected Font font;
    protected Font fallbackFont = null;
    protected String text = null;

    private float spaceBetweenLines;
    private int textAlign = Align.LEFT;

    private float x;
    private float y;
    private float w = 300f;
    private float h = 200f;

    private int background = Color.white;
    private int brush = Color.black;
    private boolean drawBorder;

    private String uri = null;
    private String key = null;
    private String uriLanguage = null;
    private String uriActualText = null;
    private String uriAltDescription = null;


    /**
     *  Creates a text block.
     *
     *  @param font the text font.
     */
    public TextBlock(Font font) {
        this.font = font;
        this.spaceBetweenLines = this.font.descent;
    }


    public TextBlock(Font font, String text) {
        this.font = font;
        this.text = text;
        this.spaceBetweenLines = this.font.descent;
    }


    /**
     *  Sets the fallback font.
     *
     *  @param fallbackFont the fallback font.
     *  @return the TextBlock object.
     */
    public TextBlock setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
        return this;
    }


    /**
     *  Sets the block text.
     *
     *  @param text the block text.
     *  @return the TextBlock object.
     */
    public TextBlock setText(String text) {
        this.text = text;
        return this;
    }


    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location where this text block will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text block.
     *  @param y the y coordinate of the top left corner of the text block.
     *  @return the TextBlock object.
     */
    public TextBlock setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

     /**
     *  Sets the location where this text block will be drawn on the page.
     *
     *  @param x the x coordinate of the top left corner of the text block.
     *  @param y the y coordinate of the top left corner of the text block.
     *  @return the TextBlock object.
     */
    public TextBlock setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Sets the width of this text block.
     *
     *  @param width the specified width.
     *  @return the TextBlock object.
     */
    public TextBlock setWidth(float width) {
        this.w = width;
        return this;
    }


    /**
     *  Returns the text block width.
     *
     *  @return the text block width.
     */
    public float getWidth() {
        return this.w;
    }


    /**
     *  Sets the height of this text block.
     *
     *  @param height the specified height.
     *  @return the TextBlock object.
     */
    public TextBlock setHeight(float height) {
        this.h = height;
        return this;
    }


    /**
     *  Returns the text block height.
     *
     *  @return the text block height.
     *  @throws Exception  If an input or output exception occurred
     */
    public float getHeight() throws Exception {
        return drawOn(null)[1];
    }


    /**
     *  Sets the space between two lines of text.
     *
     *  @param spaceBetweenLines the space between two lines.
     *  @return the TextBlock object.
     */
    public TextBlock setSpaceBetweenLines(float spaceBetweenLines) {
        this.spaceBetweenLines = spaceBetweenLines;
        return this;
    }


    /**
     *  Returns the space between two lines of text.
     *
     *  @return float the space.
     */
    public float getSpaceBetweenLines() {
        return spaceBetweenLines;
    }


    /**
     *  Sets the text alignment.
     *
     *  @param textAlign the alignment parameter.
     *  @return this TextBlock object.
     *  Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public TextBlock setTextAlignment(int textAlign) {
        this.textAlign = textAlign;
        return this;
    }


    /**
     *  Returns the text alignment.
     *
     *  @return the alignment code.
     */
    public int getTextAlignment() {
        return this.textAlign;
    }


    /**
     *  Sets the background to the specified color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     *  @return the TextBlock object.
     */
    public TextBlock setBgColor(int color) {
        this.background = color;
        return this;
    }


    /**
     *  Returns the background color.
     *
     * @return int the color as 0xRRGGBB integer.
     */
    public int getBgColor() {
        return this.background;
    }


    /**
     *  Sets the brush color.
     *
     *  @param color the color specified as 0xRRGGBB integer.
     *  @return the TextBlock object.
     */
    public TextBlock setBrushColor(int color) {
        this.brush = color;
        return this;
    }


    /**
     * Returns the brush color.
     *
     * @return int the brush color specified as 0xRRGGBB integer.
     */
    public int getBrushColor() {
        return this.brush;
    }


    public void setDrawBorder(boolean drawBorder) {
        this.drawBorder = drawBorder;
    }


    // Is the text Chinese, Japanese or Korean?
    private boolean isCJK(String text) {
        int cjk = 0;
        int other = 0;
        for (char ch : text.toCharArray()) {
            if (ch >= 0x4E00 && ch <= 0x9FFF ||     // Unified CJK
                ch >= 0xAC00 && ch <= 0xD7AF ||     // Hangul (Korean)
                ch >= 0x30A0 && ch <= 0x30FF ||     // Katakana (Japanese)
                ch >= 0x3040 && ch <= 0x309F) {     // Hiragana (Japanese)
                cjk += 1;
            }
            else {
                other += 1;
            }
        }
        return cjk > other;
    }


    /**
     *  Draws this text block on the specified page.
     *
     *  @param page the page to draw this text block on.
     *  @return the TextBlock object.
     */
    public float[] drawOn(Page page) throws Exception {
        if (page != null) {
            if (getBgColor() != Color.white) {
                page.setBrushColor(this.background);
                page.fillRect(x, y, w, h);
            }
            page.setBrushColor(this.brush);
        }
        return drawText(page);
    }


    private float[] drawText(Page page) throws Exception {
        List<String> list = new ArrayList<String>();
        String[] lines = text.split("\\r?\\n", -1);
        for (String line : lines) {
            if (isCJK(line)) {
                StringBuilder buf = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    Character ch = line.charAt(i);
                    if (font.stringWidth(fallbackFont, buf.toString() + ch) < this.w) {
                        buf.append(ch);
                    }
                    else {
                        list.add(buf.toString());
                        buf.setLength(0);
                        buf.append(ch);
                    }
                }
                String str = buf.toString().trim();
                if (!str.equals("")) {
                    list.add(str);
                }
            }
            else {
                if (font.stringWidth(fallbackFont, line) < this.w) {
                    list.add(line.trim());
                }
                else {
                    StringBuilder buf = new StringBuilder();
                    String[] tokens = TextUtils.splitTextIntoTokens(line, font, fallbackFont, this.w);
                    for (String token : tokens) {
                        if (font.stringWidth(fallbackFont, (buf.toString() + " " + token).trim()) < this.w) {
                            buf.append(" ");
                            buf.append(token);
                        }
                        else {
                            list.add(buf.toString().trim());
                            buf.setLength(0);
                            buf.append(token);
                        }
                    }
                    String str = buf.toString().trim();
                    if (!str.equals("")) {
                        list.add(str);
                    }
                }
            }
        }
        lines = list.toArray(new String[] {});

        float xText;
        float yText = y + font.getAscent();
        for (int i = 0; i < lines.length; i++) {
            if (textAlign == Align.LEFT) {
                xText = x;
            }
            else if (textAlign == Align.RIGHT) {
                xText = (x + this.w) - (font.stringWidth(fallbackFont, lines[i]));
            }
            else if (textAlign == Align.CENTER) {
                xText = x + (this.w - font.stringWidth(fallbackFont, lines[i]))/2;
            }
            else {
                throw new Exception("Invalid text alignment option.");
            }
            if (page != null) {
                page.drawString(font, fallbackFont, lines[i], xText, yText);
            }
            if (i < (lines.length - 1)) {
                yText += font.bodyHeight + spaceBetweenLines;
            }
        }

        this.h = (yText - y) + font.descent;
        if (page != null && drawBorder) {
            Box box = new Box();
            box.setLocation(x, y);
            box.setSize(w, h);
            box.drawOn(page);
        }

        if (page != null && (uri != null || key != null)) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    y,
                    x + w,
                    y + h,
                    uriLanguage,
                    uriActualText,
                    uriAltDescription));
        }

        return new float[] {this.x + this.w, this.y + this.h};
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI
     *  @return this TextBlock.
     */
    public TextBlock setURIAction(String uri) {
        this.uri = uri;
        return this;
    }

}   // End of TextBlock.java
