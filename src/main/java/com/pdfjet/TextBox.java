/**
 *  TextBox.java
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
 * A box containing line-wrapped text.
 *
 * <p>
 * Defaults:
 * x = 0f
 * y = 0f
 * width = 300f
 * height = 0f
 * alignment = Align.LEFT
 * valign = Align.TOP
 * spacing = 0f
 * margin = 0f
 * </p>
 *
 * This class was originally developed by Ronald Bourret.
 * It was completely rewritten in 2013 by Eugene Dragoev.
 */
public class TextBox implements Drawable {
    protected Font font;
    protected Font fallbackFont;
    protected String text;
    protected float x;
    protected float y;
    protected float width = 300f;
    protected float height = 0f;
    protected float spacing = 0f;
    protected float margin = 0f;
    protected float lineWidth = 0f;

    private int background = Color.transparent;
    private int pen = Color.black;
    private int brush = Color.black;
    private int valign = Align.TOP;
    private Map<String, Integer> colors = null;
    // TextBox properties
    // Future use:
    // bits 0 to 15
    // Border:
    // bit 16 - top
    // bit 17 - bottom
    // bit 18 - left
    // bit 19 - right
    // Text Alignment:
    // bit 20
    // bit 21
    // Text Decoration:
    // bit 22 - underline
    // bit 23 - strikeout
    // Future use:
    // bits 24 to 31
    private int properties = 0x00000001;
    private String language = "en-US";
    private String altDescription = "";
    private String uri = null;
    private String key = null;
    private String uriLanguage = null;
    private String uriActualText = null;
    private String uriAltDescription = null;
    private Direction textDirection = Direction.LEFT_TO_RIGHT;

    /**
     * Creates a text box and sets the font.
     *
     * @param font the font.
     */
    public TextBox(Font font) {
        this.font = font;
    }

    /**
     * Creates a text box and sets the font.
     *
     * @param text the text.
     * @param font the font.
     */
    public TextBox(Font font, String text) {
        this.font = font;
        this.text = text;
    }

    /**
     * Creates a text box and sets the font and the text.
     *
     * @param font   the font.
     * @param text   the text.
     * @param width  the width.
     * @param height the height.
     */
    public TextBox(Font font, String text, double width, double height) {
        this(font, text, (float) width, (float) height);
    }

    /**
     * Creates a text box and sets the font and the text.
     *
     * @param font   the font.
     * @param text   the text.
     * @param width  the width.
     * @param height the height.
     */
    public TextBox(Font font, String text, float width, float height) {
        this.font = font;
        this.text = text;
        this.width = width;
        this.height = height;
    }

    /**
     * Sets the font for this text box.
     *
     * @param font the font.
     */
    public void setFont(Font font) {
        this.font = font;
    }

    /**
     * Returns the font used by this text box.
     *
     * @return the font.
     */
    public Font getFont() {
        return font;
    }

    /**
     * Sets the text box text.
     *
     * @param text the text box text.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Returns the text box text.
     *
     * @return the text box text.
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the position where this text box will be drawn on the page.
     *
     * @param x the x coordinate of the top left corner of the text box.
     * @param y the y coordinate of the top left corner of the text box.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     * Sets the position where this text box will be drawn on the page.
     *
     * @param x the x coordinate of the top left corner of the text box.
     * @param y the y coordinate of the top left corner of the text box.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }

    /**
     * Sets the location where this text box will be drawn on the page.
     *
     * @param x the x coordinate of the top left corner of the text box.
     * @param y the y coordinate of the top left corner of the text box.
     * @return this TextBox object.
     */
    public TextBox setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    /**
     * Gets the location where this text box will be drawn on the page.
     *
     * @return the float array of of x and y.
     */
    public float[] getLocation() {
        return new float[] {this.x, this.y};
    }

    /**
     * Sets the location where this text box will be drawn on the page.
     *
     * @param x the x coordinate of the top left corner of the text box.
     * @param y the y coordinate of the top left corner of the text box.
     * @return this TextBox object.
     */
    public TextBox setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }

    /**
     * Sets the width of this text box.
     *
     * @param width the specified width.
     */
    public void setWidth(double width) {
        this.width = (float) width;
    }

    /**
     * Sets the width of this text box.
     *
     * @param width the specified width.
     */
    public void setWidth(float width) {
        this.width = width;
    }

    /**
     * Returns the text box width.
     *
     * @return the text box width.
     */
    public float getWidth() {
        return width;
    }

    /**
     * Sets the height of this text box.
     *
     * @param height the specified height.
     */
    public void setHeight(double height) {
        this.height = (float) height;
    }

    /**
     * Sets the height of this text box.
     *
     * @param height the specified height.
     */
    public void setHeight(float height) {
        this.height = height;
    }

    /**
     * Returns the text box height.
     *
     * @return the text box height.
     */
    public float getHeight() {
        return height;
    }

    /**
     * Sets the margin of this text box.
     *
     * @param margin the margin between the text and the box
     */
    public void setMargin(double margin) {
        this.margin = (float) margin;
    }

    /**
     * Sets the margin of this text box.
     *
     * @param margin the margin between the text and the box
     */
    public void setMargin(float margin) {
        this.margin = margin;
    }

    /**
     * Returns the text box margin.
     *
     * @return the margin between the text and the box
     */
    public float getMargin() {
        return margin;
    }

    /**
     * Sets the border line width.
     *
     * @param lineWidth double
     */
    public void setLineWidth(double lineWidth) {
        this.lineWidth = (float) lineWidth;
    }

    /**
     * Sets the border line width.
     *
     * @param lineWidth float
     */
    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    /**
     * Returns the border line width.
     *
     * @return float the line width.
     */
    public float getLineWidth() {
        return lineWidth;
    }

    /**
     * Sets the spacing between lines of text.
     *
     * @param spacing the spacing
     */
    public void setSpacing(double spacing) {
        this.spacing = (float) spacing;
    }

    /**
     * Sets the spacing between lines of text.
     *
     * @param spacing the spacing
     */
    public void setSpacing(float spacing) {
        this.spacing = spacing;
    }

    /**
     * Returns the spacing between lines of text.
     *
     * @return float the spacing.
     */
    public float getSpacing() {
        return spacing;
    }

    /**
     * Sets the background to the specified color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBgColor(int color) {
        this.background = color;
    }

    /**
     * Sets the background to the specified color.
     *
     * @param color the color specified as array of integer values from 0x00 to
     *              0xFF.
     */
    public void setBgColor(int[] color) {
        this.background = color[0] << 16 | color[1] << 8 | color[2];
    }

    /**
     * Sets the background to the specified color.
     *
     * @param color the color specified as array of double values from 0.0 to 1.0.
     */
    public void setBgColor(double[] color) {
        setBgColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
    }

    /**
     * Returns the background color.
     *
     * @return int the color as 0xRRGGBB integer.
     */
    public int getBgColor() {
        return this.background;
    }

    /**
     * Sets the pen and brush colors to the specified color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int color) {
        this.pen = color;
        this.brush = color;
    }

    /**
     * Sets the pen and brush colors to the specified color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setFgColor(int[] color) {
        this.pen = color[0] << 16 | color[1] << 8 | color[2];
        this.brush = pen;
    }

    /**
     * Sets the foreground pen and brush colors to the specified color.
     *
     * @param color the color specified as an array of double values from 0.0 to
     *              1.0.
     */
    public void setFgColor(double[] color) {
        setPenColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
        setBrushColor(pen);
    }

    /**
     * Sets the pen color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setPenColor(int color) {
        this.pen = color;
    }

    /**
     * Sets the pen color.
     *
     * @param color the color specified as an array of int values from 0x00 to 0xFF.
     */
    public void setPenColor(int[] color) {
        this.pen = color[0] << 16 | color[1] << 8 | color[2];
    }

    /**
     * Sets the pen color.
     *
     * @param color the color specified as an array of double values from 0.0 to 1.0.
     */
    public void setPenColor(double[] color) {
        setPenColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
    }

    /**
     * Returns the pen color as 0xRRGGBB integer.
     *
     * @return int the pen color.
     */
    public int getPenColor() {
        return this.pen;
    }

    /**
     * Sets the brush color.
     *
     * @param color the color specified as 0xRRGGBB integer.
     */
    public void setBrushColor(int color) {
        this.brush = color;
    }

    /**
     * Sets the brush color.
     *
     * @param color the color specified as an array of int values from 0x00 to 0xFF.
     */
    public void setBrushColor(int[] color) {
        this.brush = color[0] << 16 | color[1] << 8 | color[2];
    }

    /**
     * Sets the brush color.
     *
     * @param color the color specified as an array of double values from 0.0 to 1.0.
     */
    public void setBrushColor(double[] color) {
        setBrushColor(new int[] { (int) color[0], (int) color[1], (int) color[2] });
    }

    /**
     * Returns the brush color.
     *
     * @return int the brush color specified as 0xRRGGBB integer.
     */
    public int getBrushColor() {
        return this.brush;
    }

    /**
     * Sets the TextBox border properties.
     *
     * @param border the border properties.
     */
    public void setBorder(int border) {
        this.properties |= border;
    }

    /**
     * Returns the text box specific border value.
     *
     * @param border the border property.
     * @return boolean the specific border value.
     */
    public boolean getBorder(int border) {
        if (border == Border.NONE) {
            if (((properties >> 16) & 0xF) == 0x0) {
                return true;
            }
        } else if (border == Border.TOP) {
            if (((properties >> 16) & 0x1) == 0x1) {
                return true;
            }
        } else if (border == Border.BOTTOM) {
            if (((properties >> 16) & 0x2) == 0x2) {
                return true;
            }
        } else if (border == Border.LEFT) {
            if (((properties >> 16) & 0x4) == 0x4) {
                return true;
            }
        } else if (border == Border.RIGHT) {
            if (((properties >> 16) & 0x8) == 0x8) {
                return true;
            }
        } else if (border == Border.ALL) {
            if (((properties >> 16) & 0xF) == 0xF) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the TextBox borders on and off.
     *
     * @param borders the borders flag.
     */
    public void setBorders(boolean borders) {
        if (borders) {
            setBorder(Border.ALL);
        } else {
            setBorder(Border.NONE);
        }
    }

    /**
     * Sets the cell text alignment.
     *
     * @param alignment the alignment code.
     *                  Supported values: Align.LEFT, Align.RIGHT and Align.CENTER.
     */
    public void setTextAlignment(int alignment) {
        this.properties &= 0x00CFFFFF;
        this.properties |= (alignment & 0x00300000);
    }

    /**
     * Returns the text alignment.
     *
     * @return alignment the alignment code. Supported values: Align.LEFT,
     *         Align.RIGHT and Align.CENTER.
     */
    public int getTextAlignment() {
        return (this.properties & 0x00300000);
    }

    /**
     * Sets the underline variable.
     * If the value of the underline variable is 'true' - the text is underlined.
     *
     * @param underline the underline flag.
     */
    public void setUnderline(boolean underline) {
        if (underline) {
            this.properties |= 0x00400000;
        } else {
            this.properties &= 0x00BFFFFF;
        }
    }

    /**
     * Whether the text will be underlined.
     *
     * @return whether the text will be underlined
     */
    public boolean getUnderline() {
        return (properties & 0x00400000) != 0;
    }

    /**
     * Sets the srikeout flag.
     * In the flag is true - draw strikeout line through the text.
     *
     * @param strikeout the strikeout flag.
     */
    public void setStrikeout(boolean strikeout) {
        if (strikeout) {
            this.properties |= 0x00800000;
        } else {
            this.properties &= 0x007FFFFF;
        }
    }

    /**
     * Returns the strikeout flag.
     *
     * @return boolean the strikeout flag.
     */
    public boolean getStrikeout() {
        return (properties & 0x00800000) != 0;
    }

    public void setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
    }

    public Font getFallbackFont() {
        return this.fallbackFont;
    }

    /**
     * Sets the vertical alignment of the text in this TextBox.
     *
     * @param valign - valid values are Align.TOP, Align.BOTTOM and Align.CENTER
     */
    public void setVerticalAlignment(int valign) {
        this.valign = valign;
    }

    public int getVerticalAlignment() {
        return this.valign;
    }

    public void setTextColors(Map<String, Integer> colors) {
        this.colors = colors;
    }

    public Map<String, Integer> getTextColors() {
        return this.colors;
    }

    public TextBox setLanguage(String language) {
        this.language = language;
        return this;
    }

    public String getLanguage() {
        return this.language;
    }

    /**
     * Sets the alternate description of this text line.
     *
     * @param altDescription the alternate description of the text line.
     * @return this TextBox.
     */
    public TextBox setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }

    public String getAltDescription() {
        return altDescription;
    }

    private void drawBorders(Page page) {
        page.addArtifactBMC();
        page.setPenColor(pen);
        page.setPenWidth(lineWidth);
        if (getBorder(Border.ALL)) {
            page.drawRect(x, y, width, height);
        } else {
            if (getBorder(Border.TOP)) {
                page.moveTo(x, y);
                page.lineTo(x + width, y);
                page.strokePath();
            }
            if (getBorder(Border.BOTTOM)) {
                page.moveTo(x, y + height);
                page.lineTo(x + width, y + height);
                page.strokePath();
            }
            if (getBorder(Border.LEFT)) {
                page.moveTo(x, y);
                page.lineTo(x, y + height);
                page.strokePath();
            }
            if (getBorder(Border.RIGHT)) {
                page.moveTo(x + width, y);
                page.lineTo(x + width, y + height);
                page.strokePath();
            }
        }
        page.addEMC();
    }

    // Preserves the leading spaces and tabs
    private StringBuilder getStringBuilder(String line) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char ch = line.charAt(i);
            if (ch == ' ') {
                buf.append(ch);
            } else if (ch == '\t') {
                buf.append("    ");
            } else {
                break;
            }
        }
        return buf;
    }

    private String[] getTextLines() {
        List<String> list = new ArrayList<String>();
        float textAreaWidth;
        if (textDirection == Direction.LEFT_TO_RIGHT) {
            textAreaWidth = width - 2*margin;
        } else {
            textAreaWidth = height - 2*margin;
        }
        String[] lines = text.split("\\r?\\n", -1);
        for (String line : lines) {
            if (font.stringWidth(fallbackFont, line) <= textAreaWidth) {
                list.add(line);
            } else {
                StringBuilder buf1 = getStringBuilder(line); // Preserves the indentation
                String[] tokens = line.trim().split("\\s+"); // Do not remove the trim()!
                for (String token : tokens) {
                    if (font.stringWidth(fallbackFont, token) > textAreaWidth) {
                        // We have very long token, so we have to split it
                        StringBuilder buf2 = new StringBuilder();
                        for (int i = 0; i < token.length(); i++) {
                            char ch = token.charAt(i);
                            if (font.stringWidth(fallbackFont, buf2.toString() + ch) > textAreaWidth) {
                                list.add(buf2.toString());
                                buf2.setLength(0);
                            }
                            buf2.append(ch);
                        }
                        if (buf2.length() > 0) {
                            buf1.append(buf2.toString() + " ");
                        }
                    } else {
                        if (font.stringWidth(fallbackFont, buf1.toString() + token) > textAreaWidth) {
                            list.add(buf1.toString());
                            buf1.setLength(0);
                        }
                        buf1.append(token + " ");
                    }
                }
                if (buf1.length() > 0) {
                    String str = buf1.toString().trim();
                    if (font.stringWidth(fallbackFont, str) <= textAreaWidth) {
                        list.add(str);
                    } else {
                        // We have very long token, so we have to split it
                        StringBuilder buf2 = new StringBuilder();
                        for (int i = 0; i < str.length(); i++) {
                            char ch = str.charAt(i);
                            if (font.stringWidth(fallbackFont, buf2.toString() + ch) > textAreaWidth) {
                                list.add(buf2.toString());
                                buf2.setLength(0);
                            }
                            buf2.append(ch);
                        }
                        if (buf2.length() > 0) {
                            list.add(buf2.toString());
                        }
                    }
                }
            }
        }
        int index = list.size() - 1;
        if (list.size() > 0 && list.get(index).trim().length() == 0) {
            // Remove the last line if it is blank
            list.remove(index);
        }
        return list.toArray(new String[] {});
    }

    /**
     * Draws this text box on the specified page.
     *
     * @param page the Page where the TextBox is to be drawn.
     * @return x and y coordinates of the bottom right corner of this component.
     */
    public float[] drawOn(Page page) {
        String[] lines = getTextLines();
        float leading = font.ascent + font.descent + spacing;
        if (height > 0f) { // TextBox with fixed height
            if ((lines.length*leading - spacing) > (height - 2*margin)) {
                List<String> list = new ArrayList<String>();
                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    if (((i + 1)*leading - spacing) > (height - 2*margin)) {
                        break;
                    }
                    list.add(line);
                }
                if (list.size() > 0) {  // At least one line must fit in the text box
                    String lastLine = list.get(list.size() - 1);
                    if (lastLine.length() > 3) {
                        lastLine = lastLine.substring(0, lastLine.length() - 3);
                    }
                    list.set(list.size() - 1, lastLine + "...");
                    lines = list.toArray(new String[] {});
                }
            }
            if (page != null) {
                if (getBgColor() != Color.transparent) {
                    page.setBrushColor(background);
                    page.addArtifactBMC();
                    page.fillRect(x, y, width, height);
                    page.addEMC();
                }
                page.setPenColor(this.pen);
                page.setBrushColor(this.brush);
                page.setPenWidth(this.font.underlineThickness);
            }
            float xText = x + margin;
            float yText = y + margin + font.ascent;
            if (textDirection == Direction.LEFT_TO_RIGHT) {
                if (valign == Align.TOP) {
                    yText = y + margin + font.ascent;
                } else if (valign == Align.BOTTOM) {
                    yText = (y + height) - (Float.valueOf(lines.length)*leading + margin);
                    yText += font.ascent;
                } else if (valign == Align.CENTER) {
                    yText = y + (height - Float.valueOf(lines.length)*leading)/2;
                    yText += font.ascent;
                }
            } else {
                yText = x + margin + font.ascent;
            }
            for (String line : lines) {
                if (textDirection == Direction.LEFT_TO_RIGHT) {
                    if (getTextAlignment() == Align.LEFT) {
                        xText = x + margin;
                    } else if (getTextAlignment() == Align.RIGHT) {
                        xText = (x + width) - (font.stringWidth(fallbackFont, line) + margin);
                    } else if (getTextAlignment() == Align.CENTER) {
                        xText = x + (width - font.stringWidth(fallbackFont, line))/2;
                    }
                } else {
                    xText = y + margin;
                }
                if (page != null) {
                    drawText(page, font, fallbackFont, line, xText, yText, brush, colors);
                }
                if (textDirection == Direction.LEFT_TO_RIGHT ||
                        textDirection == Direction.BOTTOM_TO_TOP) {
                    yText += leading;
                } else {
                    yText -= leading;
                }
            }
        } else { // TextBox that expands to fit the content
            if (page != null) {
                if (getBgColor() != Color.transparent) {
                    page.setBrushColor(background);
                    page.addArtifactBMC();
                    page.fillRect(x, y, width, (lines.length * leading - spacing) + 2*margin);
                    page.addEMC();
                }
                page.setPenColor(this.pen);
                page.setBrushColor(this.brush);
                page.setPenWidth(this.font.underlineThickness);
            }
            float xText = x + margin;
            float yText = y + margin + font.ascent;
            for (String line : lines) {
                if (textDirection == Direction.LEFT_TO_RIGHT) {
                    if (getTextAlignment() == Align.LEFT) {
                        xText = x + margin;
                    } else if (getTextAlignment() == Align.RIGHT) {
                        xText = (x + width) - (font.stringWidth(fallbackFont, line) + margin);
                    } else if (getTextAlignment() == Align.CENTER) {
                        xText = x + (width - font.stringWidth(fallbackFont, line))/2;
                    }
                } else {
                    xText = x + margin;
                }
                if (page != null) {
                    drawText(page, font, fallbackFont, line, xText, yText, brush, colors);
                }
                if (textDirection == Direction.LEFT_TO_RIGHT ||
                        textDirection == Direction.BOTTOM_TO_TOP) {
                    yText += leading;
                } else {
                    yText -= leading;
                }
            }
            height = ((yText - y) - (font.ascent + spacing)) + margin;
        }
        if (page != null) {
            drawBorders(page);
            if (textDirection == Direction.LEFT_TO_RIGHT && (uri != null || key != null)) {
                page.addAnnotation(new Annotation(
                        uri,
                        key,    // The destination name
                        x,
                        y,
                        x + width,
                        y + height,
                        uriLanguage,
                        uriActualText,
                        uriAltDescription));
            }
            page.setTextDirection(0);
        }
        return new float[] { x + width, y + height };
    }

    private void drawText(
            Page page,
            Font font,
            Font fallbackFont,
            String text,
            float xText,
            float yText,
            int color,
            Map<String, Integer> colors) {
        page.addBMC(StructElem.P, language, text, altDescription);
        if (textDirection == Direction.LEFT_TO_RIGHT) {
            page.drawString(font, fallbackFont, text, xText, yText, color, colors);
        } else if (textDirection == Direction.BOTTOM_TO_TOP) {
            page.setTextDirection(90);
            page.drawString(font, fallbackFont, text, yText, xText + height, color, colors);
        } else if (textDirection == Direction.TOP_TO_BOTTOM) {
            page.setTextDirection(270);
            page.drawString(font, fallbackFont, text,
                    (yText + width) - (margin + 2*font.ascent), xText, color, colors);
        }
        page.addEMC();
        if (textDirection == Direction.LEFT_TO_RIGHT) {
            float lineLength = font.stringWidth(fallbackFont, text);
            if (getUnderline()) {
                float yAdjust = font.underlinePosition;
                page.addArtifactBMC();
                page.moveTo(xText, yText + yAdjust);
                page.lineTo(xText + lineLength, yText + yAdjust);
                page.strokePath();
                page.addEMC();
            }
            if (getStrikeout()) {
                float yAdjust = font.bodyHeight / 4;
                page.addArtifactBMC();
                page.moveTo(xText, yText - yAdjust);
                page.lineTo(xText + lineLength, yText - yAdjust);
                page.strokePath();
                page.addEMC();
            }
        }
    }

    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI
     */
    public void setURIAction(String uri) {
        this.uri = uri;
    }

    public void setTextDirection(Direction textDirection) {
        this.textDirection = textDirection;
    }
} // End of TextBox.java
