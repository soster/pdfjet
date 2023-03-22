/**
 *  Page.java
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

import java.io.*;
import java.util.*;


/**
 *  Used to create PDF page objects.
 *
 *  Please note:
 *  <pre>
 *  The coordinate (0f, 0f) is the top left corner of the page.
 *  The size of the pages are represented in points.
 *  1 point is 1/72 inches.
 *  </pre>
 *
 */
public class Page {

    protected PDF pdf;
    protected PDFobj pageObj;
    protected int objNumber;
    protected ByteArrayOutputStream buf;
    protected float[] tm = new float[] {1f, 0f, 0f, 1f};
    protected int renderingMode = 0;
    protected float width;
    protected float height;
    protected List<Integer> contents;
    protected List<Annotation> annots;
    protected List<Destination> destinations;
    protected float[] cropBox = null;
    protected float[] bleedBox = null;
    protected float[] trimBox = null;
    protected float[] artBox = null;
    protected List<StructElem> structures = new ArrayList<StructElem>();
    private float[] pen = {0f, 0f, 0f};
    private float[] brush = {0f, 0f, 0f};
    private float[] penCMYK = {0f, 0f, 0f, 1f};
    private float[] brushCMYK = {0f, 0f, 0f, 1f};
    private float penWidth = -1.0f;
    private CapStyle lineCapStyle = CapStyle.BUTT;
    private JoinStyle lineJoinStyle = JoinStyle.MITER;
    private String linePattern = "[] 0";
    private Font font;
    private final List<State> savedStates = new ArrayList<State>();
    private int mcid = 0;

    protected float savedHeight = Float.MAX_VALUE;
    boolean heightTransformed = false;

    /*
     * From Android's Matrix object:
     */
    public static final int MSCALE_X = 0;
    public static final int MSKEW_X  = 1;
    public static final int MTRANS_X = 2;
    public static final int MSKEW_Y  = 3;
    public static final int MSCALE_Y = 4;
    public static final int MTRANS_Y = 5;


    /**
     *  Creates page object and add it to the PDF document.
     *
     *  Please note:
     *  <pre>
     *  The coordinate (0f, 0f) is the top left corner of the page.
     *  The size of the pages are represented in points.
     *  1 point is 1/72 inches.
     *  </pre>
     *
     *  @param pdf the pdf object.
     *  @param pageSize the page size of this page.
     *  @throws Exception  If an input or output
     *                     exception occurred
     */
    public Page(PDF pdf, float[] pageSize) throws Exception {
        this(pdf, pageSize, true);
    }


    /**
     *  Creates page object and add it to the PDF document.
     *
     *  Please note:
     *  <pre>
     *  The coordinate (0f, 0f) is the top left corner of the page.
     *  The size of the pages are represented in points.
     *  1 point is 1/72 inches.
     *  </pre>
     *
     *  @param pdf the pdf object.
     *  @param pageSize the page size of this page.
     *  @param addPageToPDF boolean flag.
     *  @throws Exception  If an input or output exception occurred
     */
    public Page(PDF pdf, float[] pageSize, boolean addPageToPDF) throws Exception {
        this.pdf = pdf;
        contents = new ArrayList<Integer>();
        annots = new ArrayList<Annotation>();
        destinations = new ArrayList<Destination>();
        width = pageSize[0];
        height = pageSize[1];
        buf = new ByteArrayOutputStream(8192);
        if (addPageToPDF) {
            pdf.addPage(this);
        }
    }


    public Page(PDF pdf, PDFobj pageObj) {
        this.pdf = pdf;
        this.pageObj = pageObj;
        width = pageObj.getPageSize()[0];
        height = pageObj.getPageSize()[1];
        buf = new ByteArrayOutputStream(8192);
        append("q\n");
        if (pageObj.gsNumber != -1) {
            append("/GS");
            append(pageObj.gsNumber + 1);
            append(" gs\n");
        }
    }


    public Font addResource(CoreFont coreFont, List<PDFobj> objects) {
        return pageObj.addResource(coreFont, objects);
    }


    public void addResource(Image image, List<PDFobj> objects) {
        pageObj.addResource(image, objects);
    }


    public void addResource(Font font, List<PDFobj> objects) {
        pageObj.addResource(font, objects);
    }


    public void complete(List<PDFobj> objects) {
        append("Q\n");
        pageObj.addContent(getContent(), objects);
    }


    public byte[] getContent() {
        return buf.toByteArray();
    }


    /**
     *  Adds destination to this page.
     *
     *  @param name The destination name.
     *  @param yPosition The vertical position of the destination on this page.
     *
     *  @return the destination.
     */
    public Destination addDestination(String name, float yPosition) {
        Destination dest = new Destination(name, height - yPosition);
        destinations.add(dest);
        return dest;
    }


    /**
     *  Returns the width of this page.
     *
     *  @return the width of the page.
     */
    public float getWidth() {
        return width;
    }


    /**
     *  Returns the height of this page.
     *
     *  @return the height of the page.
     */
    public float getHeight() {
        return height;
    }


    /**
     *  Draws a line on the page, using the current color, between the points (x1, y1) and (x2, y2).
     *
     *  @param x1 the first point's x coordinate.
     *  @param y1 the first point's y coordinate.
     *  @param x2 the second point's x coordinate.
     *  @param y2 the second point's y coordinate.
     */
    public void drawLine(
            double x1,
            double y1,
            double x2,
            double y2) {
        drawLine((float) x1, (float) y1, (float) x2, (float) y2);
    }


    /**
     *  Draws a line on the page, using the current color, between the points (x1, y1) and (x2, y2).
     *
     *  @param x1 the first point's x coordinate.
     *  @param y1 the first point's y coordinate.
     *  @param x2 the second point's x coordinate.
     *  @param y2 the second point's y coordinate.
     */
    public void drawLine(
            float x1,
            float y1,
            float x2,
            float y2) {
        moveTo(x1, y1);
        lineTo(x2, y2);
        strokePath();
    }


    public void drawString(
            Font font1,
            Font font2,
            String str,
            float x,
            float y) {
        drawString(font1, font2, str, x, y, null);
    }

    /**
     *  Draws the text given by the specified string,
     *  using the specified main font and the current brush color.
     *  If the main font is missing some glyphs - the fallback font is used.
     *  The baseline of the leftmost character is at position (x, y) on the page.
     *
     *  @param font the main font.
     *  @param fallbackFont the fallback font.
     *  @param str the string to be drawn.
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     *  @param colors map used to highlight specific words.
     */
    public void drawString(
            Font font,
            Font fallbackFont,
            String str,
            float x,
            float y,
            Map<String, Integer> colors) {
        if (font.isCoreFont || font.isCJK || fallbackFont == null || fallbackFont.isCoreFont || fallbackFont.isCJK) {
            drawString(font, str, x, y, colors);
        }
        else {
            Font activeFont = font;
            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                int ch = str.charAt(i);
                if (activeFont.unicodeToGID[ch] == 0) {
                    drawString(activeFont, buf.toString(), x, y, colors);
                    x += activeFont.stringWidth(buf.toString());
                    buf.setLength(0);
                    // Switch the active font
                    if (activeFont == font) {
                        activeFont = fallbackFont;
                    }
                    else {
                        activeFont = font;
                    }
                }
                buf.append((char) ch);
            }
            drawString(activeFont, buf.toString(), x, y, colors);
        }
    }


    /**
     *  Draws the text given by the specified string,
     *  using the specified font and the current brush color.
     *  The baseline of the leftmost character is at position (x, y) on the page.
     *
     *  @param font the font to use.
     *  @param str the string to be drawn.
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     */
    public void drawString(
            Font font,
            String str,
            double x,
            double y) {
        drawString(font, str, (float) x, (float) y);
    }


    public void drawString(
            Font font,
            String str,
            float x,
            float y) {
        drawString(font, str, x, y, null);
    }

    /**
     *  Draws the text given by the specified string,
     *  using the specified font and the current brush color.
     *  The baseline of the leftmost character is at position (x, y) on the page.
     *
     *  @param font the font to use.
     *  @param str the string to be drawn.
     *  @param x the x coordinate.
     *  @param y the y coordinate.
     *  @param colors map used to highlight specific words.
     */
    public void drawString(
            Font font,
            String str,
            float x,
            float y,
            Map<String, Integer> colors) {
        if (str == null || str.equals("")) {
            return;
        }

        append("BT\n");

        if (font.fontID == null) {
            setTextFont(font);
        }
        else {
            append('/');
            append(font.fontID);
            append(' ');
            append(font.size);
            append(" Tf\n");
        }

        if (renderingMode != 0) {
            append(renderingMode);
            append(" Tr\n");
        }

        float skew = 0f;
        if (font.skew15 &&
                tm[0] == 1f &&
                tm[1] == 0f &&
                tm[2] == 0f &&
                tm[3] == 1f) {
            skew = 0.26f;
        }

        append(tm[0]);
        append(' ');
        append(tm[1]);
        append(' ');
        append(tm[2] + skew);
        append(' ');
        append(tm[3]);
        append(' ');
        append(x);
        append(' ');
        append(height - y);
        append(" Tm\n");

        if (colors == null) {
            append("[<");
            drawString(font, str);
            append(">] TJ\n");
        }
        else {
            drawColoredString(font, str, colors);
        }

        append("ET\n");
    }


    private void drawString(Font font, String str) {
        if (font.isCoreFont) {
            drawAsciiString(font, str);
        }
        else {
            drawUnicodeString(font, str);
        }
    }


    private void drawAsciiString(Font font, String str) {
        for (int i = 0; i < str.length(); i++) {
            int c1 = str.charAt(i);
            if (c1 < font.firstChar || c1 > font.lastChar) {
                append(String.format("%02X", 0x20));
                return;
            }
            append(String.format("%02X", c1));
            if (font.isCoreFont && font.kernPairs && i < (str.length() - 1)) {
                c1 -= 32;
                int c2 = str.charAt(i + 1);
                if (c2 < font.firstChar || c2 > font.lastChar) {
                    c2 = 32;
                }
                for (int j = 2; j < font.metrics[c1].length; j += 2) {
                    if (font.metrics[c1][j] == c2) {
                        append(">");
                        append(-font.metrics[c1][j + 1]);
                        append("<");
                        break;
                    }
                }
            }
        }
    }


    private void drawUnicodeString(Font font, String str) {
        if (font.isCJK) {
            for (int i = 0; i < str.length(); i++) {
                int c1 = str.charAt(i);
                if (c1 == 0xFEFF) {     // BOM marker
                    continue;
                }
                if (c1 < font.firstChar || c1 > font.lastChar) {
                    append(String.format("%04X", 0x0020));
                }
                else {
                    append(String.format("%04X", c1));
                }
            }
        }
        else {
            for (int i = 0; i < str.length(); i++) {
                int c1 = str.charAt(i);
                if (c1 == 0xFEFF) {     // BOM marker
                    continue;
                }
                if (c1 < font.firstChar || c1 > font.lastChar) {
                    append(String.format("%04X", font.unicodeToGID[0x0020]));
                }
                else {
                    append(String.format("%04X", font.unicodeToGID[c1]));
                }
            }
        }
    }


    /**
     *  Sets the graphics state. Please see Example_31.
     *
     *  @param gs the graphics state to use.
     */
    public void setGraphicsState(GraphicsState gs) {
        StringBuilder buf = new StringBuilder();
        buf.append("/CA ");
        buf.append(gs.getAlphaStroking());
        buf.append(" ");
        buf.append("/ca ");
        buf.append(gs.getAlphaNonStroking());
        String state = buf.toString();
        Integer n = pdf.states.get(state);
        if (n == null) {
            n = pdf.states.size() + 1;
            pdf.states.put(state, n);
        }
        append("/GS");
        append(n);
        append(" gs\n");
    }


    /**
     * Sets the color for stroking operations.
     * The pen color is used when drawing lines and splines.
     *
     * @param r the red component is float value from 0.0 to 1.0.
     * @param g the green component is float value from 0.0 to 1.0.
     * @param b the blue component is float value from 0.0 to 1.0.
     */
    public void setPenColor(double r, double g, double b) {
        setPenColor((float) r, (float) g, (float) b);
    }


    /**
     * Sets the color for stroking operations.
     * The pen color is used when drawing lines and splines.
     *
     * @param r the red component is float value from 0.0f to 1.0f.
     * @param g the green component is float value from 0.0f to 1.0f.
     * @param b the blue component is float value from 0.0f to 1.0f.
     */
    public void setPenColor(float r, float g, float b) {
        if (pen[0] != r || pen[1] != g || pen[2] != b) {
            setColor(r, g, b);
            append(" RG\n");
            pen[0] = r;
            pen[1] = g;
            pen[2] = b;
        }
    }


    /**
     * Sets the color for stroking operations using CMYK.
     * The pen color is used when drawing lines and splines.
     *
     * @param c the cyan component is float value from 0.0f to 1.0f.
     * @param m the magenta component is float value from 0.0f to 1.0f.
     * @param y the yellow component is float value from 0.0f to 1.0f.
     * @param k the black component is float value from 0.0f to 1.0f.
     */
    public void setPenColorCMYK(float c, float m, float y, float k) {
        if (penCMYK[0] != c || penCMYK[1] != m || penCMYK[2] != y || penCMYK[3] != k) {
            setColorCMYK(c, m, y, k);
            append(" K\n");
            penCMYK[0] = c;
            penCMYK[1] = m;
            penCMYK[2] = y;
            penCMYK[3] = k;
        }
    }


    /**
     * Sets the color for brush operations.
     * This is the color used when drawing regular text and filling shapes.
     *
     * @param r the red component is float value from 0.0 to 1.0.
     * @param g the green component is float value from 0.0 to 1.0.
     * @param b the blue component is float value from 0.0 to 1.0.
     */
    public void setBrushColor(double r, double g, double b) {
        setBrushColor((float) r, (float) g, (float) b);
    }


    /**
     * Sets the color for brush operations.
     * This is the color used when drawing regular text and filling shapes.
     *
     * @param r the red component is float value from 0.0f to 1.0f.
     * @param g the green component is float value from 0.0f to 1.0f.
     * @param b the blue component is float value from 0.0f to 1.0f.
     */
    public void setBrushColor(float r, float g, float b) {
        if (brush[0] != r || brush[1] != g || brush[2] != b) {
            setColor(r, g, b);
            append(" rg\n");
            brush[0] = r;
            brush[1] = g;
            brush[2] = b;
        }
    }


    /**
     * Sets the color for brush operations using CMYK.
     * This is the color used when drawing regular text and filling shapes.
     *
     * @param c the cyan component is float value from 0.0f to 1.0f.
     * @param m the magenta component is float value from 0.0f to 1.0f.
     * @param y the yellow component is float value from 0.0f to 1.0f.
     * @param k the black component is float value from 0.0f to 1.0f.
     */
    public void setBrushColorCMYK(float c, float m, float y, float k) {
        if (brushCMYK[0] != c || brushCMYK[1] != m || brushCMYK[2] != y || brushCMYK[3] != k) {
            setColorCMYK(c, m, y, k);
            append(" k\n");
            brushCMYK[0] = c;
            brushCMYK[1] = m;
            brushCMYK[2] = y;
            brushCMYK[3] = k;
        }
    }


    /**
     * Sets the color for brush operations.
     *
     * @param color the color.
     */
    public void setBrushColor(float[] color) {
        setBrushColor(color[0], color[1], color[2]);
    }


    /**
     * Returns the brush color.
     *
     * @return the brush color.
     */
    public float[] getBrushColor() {
        return brush;
    }


    private void setColor(float r, float g, float b) {
        append(r);
        append(' ');
        append(g);
        append(' ');
        append(b);
    }


    private void setColorCMYK(float c, float m, float y, float k) {
        append(c);
        append(' ');
        append(m);
        append(' ');
        append(y);
        append(' ');
        append(k);
    }


    /**
     * Sets the pen color.
     *
     * @param color the color. See the Color class for predefined values or define your own using 0x00RRGGBB packed integers.
     */
    public void setPenColor(int color) {
        float r = ((color >> 16) & 0xff)/255f;
        float g = ((color >>  8) & 0xff)/255f;
        float b = ((color)       & 0xff)/255f;
        setPenColor(r, g, b);
    }


    /**
     * Sets the brush color.
     *
     * @param color the color. See the Color class for predefined values or define your own using 0x00RRGGBB packed integers.
     */
    public void setBrushColor(int color) {
        float r = ((color >> 16) & 0xff)/255f;
        float g = ((color >>  8) & 0xff)/255f;
        float b = ((color)       & 0xff)/255f;
        setBrushColor(r, g, b);
    }


    /**
     *  Sets the line width to the default.
     *  The default is the finest line width.
     */
    public void setDefaultLineWidth() {
        if (penWidth != 0f) {
            penWidth = 0f;
            append(penWidth);
            append(" w\n");
        }
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
    public void setLinePattern(String pattern) {
        if (!pattern.equals(linePattern)) {
            linePattern = pattern;
            append(linePattern);
            append(" d\n");
        }
    }


    /**
     *  Sets the default line dash pattern - solid line.
     */
    public void setDefaultLinePattern() {
        append("[] 0");
        append(" d\n");
    }


    /**
     *  Sets the pen width that will be used to draw lines and splines on this page.
     *
     *  @param width the pen width.
     */
    public void setPenWidth(double width) {
        setPenWidth((float) width);
    }


    /**
     *  Sets the pen width that will be used to draw lines and splines on this page.
     *
     *  @param width the pen width.
     */
    public void setPenWidth(float width) {
        if (penWidth != width) {
            penWidth = width;
            append(penWidth);
            append(" w\n");
        }
    }


    /**
     *  Sets the current line cap style.
     *
     *  @param style the cap style of the current line.
     *  Supported values: CapStyle.BUTT, CapStyle.ROUND and CapStyle.PROJECTING_SQUARE
     */
    public void setLineCapStyle(CapStyle style) {
        if (lineCapStyle != style) {
            lineCapStyle = style;
            append(lineCapStyle.ordinal());
            append(" J\n");
        }
    }


    /**
     *  Sets the line join style.
     *
     *  @param style the line join style code. Supported values: JoinStyle.MITER, JoinStyle.ROUND and JoinStyle.BEVEL
     */
    public void setLineJoinStyle(JoinStyle style) {
        if (lineJoinStyle != style) {
            lineJoinStyle = style;
            append(lineJoinStyle.ordinal());
            append(" j\n");
        }
    }


    /**
     *  Moves the pen to the point with coordinates (x, y) on the page.
     *
     *  @param x the x coordinate of new pen position.
     *  @param y the y coordinate of new pen position.
     */
    public void moveTo(double x, double y) {
        moveTo((float) x, (float) y);
    }


    /**
     *  Moves the pen to the point with coordinates (x, y) on the page.
     *
     *  @param x the x coordinate of new pen position.
     *  @param y the y coordinate of new pen position.
     */
    public void moveTo(float x, float y) {
        append(x);
        append(' ');
        append(height - y);
        append(" m\n");
    }


    /**
     *  Draws a line from the current pen position to the point with coordinates (x, y),
     *  using the current pen width and stroke color.
     *  Make sure you call strokePath(), closePath() or fillPath() after the last call to this method.
     *  @param x the x coordinate of the new pen position.
     *  @param y the y coordinate of the new pen position.
     */
    public void lineTo(double x, double y) {
        lineTo((float) x, (float) y);
    }


    /**
     *  Draws a line from the current pen position to the point with coordinates (x, y),
     *  using the current pen width and stroke color.
     *  Make sure you call strokePath(), closePath() or fillPath() after the last call to this method.
     *  @param x the x coordinate of the new pen position.
     *  @param y the y coordinate of the new pen position.
     */
    public void lineTo(float x, float y) {
        append(x);
        append(' ');
        append(height - y);
        append(" l\n");
    }


    /**
     *  Draws the path using the current pen color.
     */
    public void strokePath() {
        append("S\n");
    }


    /**
     *  Closes the path and draws it using the current pen color.
     */
    public void closePath() {
        append("s\n");
    }


    /**
     *  Closes and fills the path with the current brush color.
     */
    public void fillPath() {
        append("f\n");
    }


    /**
     *  Draws the outline of the specified rectangle on the page.
     *  The left and right edges of the rectangle are at x and x + w.
     *  The top and bottom edges are at y and y + h.
     *  The rectangle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the rectangle to be drawn.
     *  @param y the y coordinate of the rectangle to be drawn.
     *  @param w the width of the rectangle to be drawn.
     *  @param h the height of the rectangle to be drawn.
     */
    public void drawRect(double x, double y, double w, double h) {
        drawRect((float) x, (float) y, (float) w, (float) h);
    }


    /**
     *  Draws the outline of the specified rectangle on the page.
     *  The left and right edges of the rectangle are at x and x + w.
     *  The top and bottom edges are at y and y + h.
     *  The rectangle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the rectangle to be drawn.
     *  @param y the y coordinate of the rectangle to be drawn.
     *  @param w the width of the rectangle to be drawn.
     *  @param h the height of the rectangle to be drawn.
     */
    public void drawRect(float x, float y, float w, float h) {
        moveTo(x, y);
        lineTo(x+w, y);
        lineTo(x+w, y+h);
        lineTo(x, y+h);
        closePath();
    }


    /**
     *  Fills the specified rectangle on the page.
     *  The left and right edges of the rectangle are at x and x + w.
     *  The top and bottom edges are at y and y + h.
     *  The rectangle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the rectangle to be drawn.
     *  @param y the y coordinate of the rectangle to be drawn.
     *  @param w the width of the rectangle to be drawn.
     *  @param h the height of the rectangle to be drawn.
     */
    public void fillRect(double x, double y, double w, double h) {
        fillRect((float) x, (float) y, (float) w, (float) h);
    }


    /**
     *  Fills the specified rectangle on the page.
     *  The left and right edges of the rectangle are at x and x + w.
     *  The top and bottom edges are at y and y + h.
     *  The rectangle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the rectangle to be drawn.
     *  @param y the y coordinate of the rectangle to be drawn.
     *  @param w the width of the rectangle to be drawn.
     *  @param h the height of the rectangle to be drawn.
     */
    public void fillRect(float x, float y, float w, float h) {
        moveTo(x, y);
        lineTo(x+w, y);
        lineTo(x+w, y+h);
        lineTo(x, y+h);
        fillPath();
    }


    /**
     *  Draws or fills the specified path using the current pen or brush.
     *
     *  @param path the path.
     *  @param operation specifies 'stroke' or 'fill' operation.
     *  @throws Exception  If an input or output exception occurred
     */
    public void drawPath(
            List<Point> path, char operation) throws Exception {
        if (path.size() < 2) {
            throw new Exception(
                    "The Path object must contain at least 2 points");
        }
        Point point = path.get(0);
        moveTo(point.x, point.y);
        boolean curve = false;
        for (int i = 1; i < path.size(); i++) {
            point = path.get(i);
            if (point.isControlPoint) {
                curve = true;
                append(point);
            }
            else {
                if (curve) {
                    curve = false;
                    append(point);
                    append("c\n");
                }
                else {
                    lineTo(point.x, point.y);
                }
            }
        }
        append(operation);
        append('\n');
    }


    /**
     *  Draws a circle on the page.
     *
     *  The outline of the circle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the center of the circle to be drawn.
     *  @param y the y coordinate of the center of the circle to be drawn.
     *  @param r the radius of the circle to be drawn.
     */
    public void drawCircle(
            double x,
            double y,
            double r) {
        drawEllipse((float) x, (float) y, (float) r, (float) r, Operation.STROKE);
    }


    /**
     *  Draws a circle on the page.
     *
     *  The outline of the circle is drawn using the current pen color.
     *
     *  @param x the x coordinate of the center of the circle to be drawn.
     *  @param y the y coordinate of the center of the circle to be drawn.
     *  @param r the radius of the circle to be drawn.
     */
    public void drawCircle(
            float x,
            float y,
            float r) {
        drawEllipse(x, y, r, r, Operation.STROKE);
    }


    /**
     *  Draws the specified circle on the page and fills it with the current brush color.
     *
     *  @param x the x coordinate of the center of the circle to be drawn.
     *  @param y the y coordinate of the center of the circle to be drawn.
     *  @param r the radius of the circle to be drawn.
     *  @param operation must be Operation.STROKE, Operation.CLOSE or Operation.FILL.
     */
    public void drawCircle(
            double x,
            double y,
            double r,
            char operation) {
        drawEllipse((float) x, (float) y, (float) r, (float) r, operation);
    }


    /**
     *  Draws the specified circle on the page and fills it with the current brush color.
     *
     *  @param x the x coordinate of the center of the circle to be drawn.
     *  @param y the y coordinate of the center of the circle to be drawn.
     *  @param r the radius of the circle to be drawn.
     *  @param operation must be Operation.STROKE, Operation.CLOSE or Operation.FILL.
     */
    public void drawCircle(
            float x,
            float y,
            float r,
            char operation) {
        drawEllipse(x, y, r, r, operation);
    }


    /**
     *  Draws an ellipse on the page using the current pen color.
     *
     *  @param x the x coordinate of the center of the ellipse to be drawn.
     *  @param y the y coordinate of the center of the ellipse to be drawn.
     *  @param r1 the horizontal radius of the ellipse to be drawn.
     *  @param r2 the vertical radius of the ellipse to be drawn.
     */
    public void drawEllipse(
            double x,
            double y,
            double r1,
            double r2) {
        drawEllipse((float) x, (float) y, (float) r1, (float) r2, Operation.STROKE);
    }


    /**
     *  Draws an ellipse on the page using the current pen color.
     *
     *  @param x the x coordinate of the center of the ellipse to be drawn.
     *  @param y the y coordinate of the center of the ellipse to be drawn.
     *  @param r1 the horizontal radius of the ellipse to be drawn.
     *  @param r2 the vertical radius of the ellipse to be drawn.
     */
    public void drawEllipse(
            float x,
            float y,
            float r1,
            float r2) {
        drawEllipse(x, y, r1, r2, Operation.STROKE);
    }


    /**
     *  Fills an ellipse on the page using the current pen color.
     *
     *  @param x the x coordinate of the center of the ellipse to be drawn.
     *  @param y the y coordinate of the center of the ellipse to be drawn.
     *  @param r1 the horizontal radius of the ellipse to be drawn.
     *  @param r2 the vertical radius of the ellipse to be drawn.
     */
    public void fillEllipse(
            double x,
            double y,
            double r1,
            double r2) {
        drawEllipse((float) x, (float) y, (float) r1, (float) r2, Operation.FILL);
    }


    /**
     *  Fills an ellipse on the page using the current pen color.
     *
     *  @param x the x coordinate of the center of the ellipse to be drawn.
     *  @param y the y coordinate of the center of the ellipse to be drawn.
     *  @param r1 the horizontal radius of the ellipse to be drawn.
     *  @param r2 the vertical radius of the ellipse to be drawn.
     */
    public void fillEllipse(
            float x,
            float y,
            float r1,
            float r2) {
        drawEllipse(x, y, r1, r2, Operation.FILL);
    }


    /**
     *  Draws an ellipse on the page and fills it using the current brush color.
     *
     *  @param x the x coordinate of the center of the ellipse to be drawn.
     *  @param y the y coordinate of the center of the ellipse to be drawn.
     *  @param r1 the horizontal radius of the ellipse to be drawn.
     *  @param r2 the vertical radius of the ellipse to be drawn.
     *  @param operation the operation.
     */
    private void drawEllipse(
            float x,
            float y,
            float r1,
            float r2,
            char operation) {
        // The best 4-spline magic number
        float m4 = 0.551784f;

        // Starting point
        moveTo(x, y - r2);

        appendPointXY(x + m4*r1, y - r2);
        appendPointXY(x + r1, y - m4*r2);
        appendPointXY(x + r1, y);
        append("c\n");

        appendPointXY(x + r1, y + m4*r2);
        appendPointXY(x + m4*r1, y + r2);
        appendPointXY(x, y + r2);
        append("c\n");

        appendPointXY(x - m4*r1, y + r2);
        appendPointXY(x - r1, y + m4*r2);
        appendPointXY(x - r1, y);
        append("c\n");

        appendPointXY(x - r1, y - m4*r2);
        appendPointXY(x - m4*r1, y - r2);
        appendPointXY(x, y - r2);
        append("c\n");

        append(operation);
        append('\n');
    }


    /**
     *  Draws a point on the page using the current pen color.
     *
     *  @param p the point.
     *  @throws Exception  If an input or output exception occurred
     */
    public void drawPoint(Point p) throws Exception {
        if (p.shape != Point.INVISIBLE)  {
            List<Point> list;
            if (p.shape == Point.CIRCLE) {
                if (p.fillShape) {
                    drawCircle(p.x, p.y, p.r, 'f');
                }
                else {
                    drawCircle(p.x, p.y, p.r, 'S');
                }
            }
            else if (p.shape == Point.DIAMOND) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y));
                list.add(new Point(p.x, p.y + p.r));
                list.add(new Point(p.x - p.r, p.y));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.BOX) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x - p.r, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y + p.r));
                list.add(new Point(p.x - p.r, p.y + p.r));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.PLUS) {
                drawLine(p.x - p.r, p.y, p.x + p.r, p.y);
                drawLine(p.x, p.y - p.r, p.x, p.y + p.r);
            }
            else if (p.shape == Point.UP_ARROW) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y + p.r));
                list.add(new Point(p.x - p.r, p.y + p.r));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.DOWN_ARROW) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x - p.r, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y - p.r));
                list.add(new Point(p.x, p.y + p.r));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.LEFT_ARROW) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x + p.r, p.y + p.r));
                list.add(new Point(p.x - p.r, p.y));
                list.add(new Point(p.x + p.r, p.y - p.r));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.RIGHT_ARROW) {
                list = new ArrayList<Point>();
                list.add(new Point(p.x - p.r, p.y - p.r));
                list.add(new Point(p.x + p.r, p.y));
                list.add(new Point(p.x - p.r, p.y + p.r));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
            else if (p.shape == Point.H_DASH) {
                drawLine(p.x - p.r, p.y, p.x + p.r, p.y);
            }
            else if (p.shape == Point.V_DASH) {
                drawLine(p.x, p.y - p.r, p.x, p.y + p.r);
            }
            else if (p.shape == Point.X_MARK) {
                drawLine(p.x - p.r, p.y - p.r, p.x + p.r, p.y + p.r);
                drawLine(p.x - p.r, p.y + p.r, p.x + p.r, p.y - p.r);
            }
            else if (p.shape == Point.MULTIPLY) {
                drawLine(p.x - p.r, p.y - p.r, p.x + p.r, p.y + p.r);
                drawLine(p.x - p.r, p.y + p.r, p.x + p.r, p.y - p.r);
                drawLine(p.x - p.r, p.y, p.x + p.r, p.y);
                drawLine(p.x, p.y - p.r, p.x, p.y + p.r);
            }
            else if (p.shape == Point.STAR) {
                float angle = (float) Math.PI / 10;
                float sin18 = (float) Math.sin(angle);
                float cos18 = (float) Math.cos(angle);
                float a = p.r * cos18;
                float b = p.r * sin18;
                float c = 2 * a * sin18;
                float d = 2 * a * cos18 - p.r;
                list = new ArrayList<Point>();
                list.add(new Point(p.x, p.y - p.r));
                list.add(new Point(p.x + c, p.y + d));
                list.add(new Point(p.x - a, p.y - b));
                list.add(new Point(p.x + a, p.y - b));
                list.add(new Point(p.x - c, p.y + d));
                if (p.fillShape) {
                    drawPath(list, 'f');
                }
                else {
                    drawPath(list, 's');
                }
            }
        }
    }


    /**
     *  Sets the text rendering mode.
     *
     *  @param mode the rendering mode.
     *  @throws Exception  If an input or output exception occurred
     */
    public void setTextRenderingMode(int mode) throws Exception {
        if (mode >= 0 && mode <= 7) {
            this.renderingMode = mode;
        }
        else {
            throw new Exception("Invalid text rendering mode: " + mode);
        }
    }


    /**
     *  Sets the text direction.
     *
     *  @param degrees the angle.
     */
    public void setTextDirection(int degrees) {
        if (degrees > 360) degrees %= 360;
        if (degrees == 0) {
            tm = new float[] { 1f,  0f,  0f,  1f};
        }
        else if (degrees == 90) {
            tm = new float[] { 0f,  1f, -1f,  0f};
        }
        else if (degrees == 180) {
            tm = new float[] {-1f,  0f,  0f, -1f};
        }
        else if (degrees == 270) {
            tm = new float[] { 0f, -1f,  1f,  0f};
        }
        else if (degrees == 360) {
            tm = new float[] { 1f,  0f,  0f,  1f};
        }
        else {
            float sinOfAngle = (float) Math.sin(degrees * (Math.PI / 180));
            float cosOfAngle = (float) Math.cos(degrees * (Math.PI / 180));
            tm = new float[] {cosOfAngle, sinOfAngle, -sinOfAngle, cosOfAngle};
        }
    }


    /**
     *  Draws a cubic bezier curve starting from the current point to the end point p3
     *
     *  @param x1 first control point x
     *  @param y1 first control point y
     *  @param x2 second control point x
     *  @param y2 second control point y
     *  @param x3 end point x
     *  @param y3 end point y
     */
    public void curveTo(
        float x1, float y1, float x2, float y2, float x3, float y3) {
        append(x1);
        append(' ');
        append(height - y1);
        append(' ');
        append(x2);
        append(' ');
        append(height - y2);
        append(' ');
        append(x3);
        append(' ');
        append(height - y3);
        append(' ');
        append("c\n");
    }


    /**
     *  Draws a bezier curve starting from the current point.
     *  <strong>Please note:</strong> You must call the fillPath,
     *  closePath or strokePath method after the last bezierCurveTo call.
     *  <p><i>Author:</i> <strong>Pieter Libin</strong>, pieter@emweb.be</p>
     *
     *  @param p1 first control point
     *  @param p2 second control point
     *  @param p3 end point
     */
    public void bezierCurveTo(Point p1, Point p2, Point p3) {
        append(p1);
        append(p2);
        append(p3);
        append("c\n");
    }


    /**
     *  Sets the start of text block.
     *  Please see Example_32. This method must have matching call to setTextEnd().
     */
    public void setTextStart() {
        append("BT\n");
    }


    /**
     *  Sets the text location.
     *  Please see Example_32.
     *
     *  @param x the x coordinate of new text location.
     *  @param y the y coordinate of new text location.
     */
    public void setTextLocation(float x, float y) {
        append(x);
        append(' ');
        append(height - y);
        append(" Td\n");
    }


    public void setTextBegin(float x, float y) {
        append("BT\n");
        append(x);
        append(' ');
        append(height - y);
        append(" Td\n");
    }


    /**
     *  Sets the text leading.
     *  Please see Example_32.
     *
     *  @param leading the leading.
     */
    public void setTextLeading(float leading) {
        append(leading);
        append(" TL\n");
    }


    public void setCharSpacing(float spacing) {
        append(spacing);
        append(" Tc\n");
    }


    public void setWordSpacing(float spacing) {
        append(spacing);
        append(" Tw\n");
    }


    public void setTextScaling(float scaling) {
        append(scaling);
        append(" Tz\n");
    }


    public void setTextRise(float rise) {
        append(rise);
        append(" Ts\n");
    }


    public void setTextFont(Font font) {
        this.font = font;
        append("/F");
        append(font.objNumber);
        append(' ');
        append(font.size);
        append(" Tf\n");
    }


    /**
     *  Prints a line of text and moves to the next line.
     *  Please see Example_32.
     *  @param str the line of text that is printed
     */
    public void println(String str) {
        print(str);
        println();
    }


    /**
     *  Prints a line of text.
     *  Please see Example_32.
     *  @param str the line of text that is printed
     */
    public void print(String str) {
        append("[<");
        drawString(font, str);
        append(">] TJ\n");
    }


    /**
     *  Move to the next line.
     *  Please see Example_32.
     */
    public void println() {
        append("T*\n");
    }


    /**
     *  Sets the end of text block.
     *  Please see Example_32.
     */
    public void setTextEnd() {
        append("ET\n");
    }


    // Code provided by:
    // Dominique Andre Gunia <contact@dgunia.de>
    // <<
    public void drawRectRoundCorners(
            float x, float y, float w, float h, float r1, float r2, char operation)
        throws Exception {

        // The best 4-spline magic number
        float m4 = 0.551784f;

        List<Point> list = new ArrayList<Point>();

        // Starting point
        list.add(new Point(x + w - r1, y));
        list.add(new Point(x + w - r1 + m4*r1, y, Point.CONTROL_POINT));
        list.add(new Point(x + w, y + r2 - m4*r2, Point.CONTROL_POINT));
        list.add(new Point(x + w, y + r2));

        list.add(new Point(x + w, y + h - r2));
        list.add(new Point(x + w, y + h - r2 + m4*r2, Point.CONTROL_POINT));
        list.add(new Point(x + w - m4*r1, y + h, Point.CONTROL_POINT));
        list.add(new Point(x + w - r1, y + h));

        list.add(new Point(x + r1, y + h));
        list.add(new Point(x + r1 - m4*r1, y + h, Point.CONTROL_POINT));
        list.add(new Point(x, y + h - m4*r2, Point.CONTROL_POINT));
        list.add(new Point(x, y + h - r2));

        list.add(new Point(x, y + r2));
        list.add(new Point(x, y + r2 - m4*r2, Point.CONTROL_POINT));
        list.add(new Point(x + m4*r1, y, Point.CONTROL_POINT));
        list.add(new Point(x + r1, y));
        list.add(new Point(x + w - r1, y));

        drawPath(list, operation);
    }


    /**
     *  Clips the path.
     */
    public void clipPath() {
        append("W\n");
        append("n\n");  // Close the path without painting it.
    }


    public void clipRect(float x, float y, float w, float h) {
        moveTo(x, y);
        lineTo(x + w, y);
        lineTo(x + w, y + h);
        lineTo(x, y + h);
        clipPath();
    }


    public void save() {
        append("q\n");
        savedStates.add(new State(
                pen, brush, penWidth, lineCapStyle, lineJoinStyle, linePattern));
        savedHeight = height;
    }


    public void restore() {
        append("Q\n");
        if (savedStates.size() > 0) {
            State savedState = savedStates.remove(savedStates.size() - 1);
            pen = savedState.getPen();
            brush = savedState.getBrush();
            penWidth = savedState.getPenWidth();
            lineCapStyle = savedState.getLineCapStyle();
            lineJoinStyle = savedState.getLineJoinStyle();
            linePattern = savedState.getLinePattern();
        }
        if (savedHeight != Float.MAX_VALUE) {
            height = savedHeight;
            savedHeight = Float.MAX_VALUE;
        }
        heightTransformed = false;
    }
    // <<


    /**
     * Sets the page CropBox.
     * See page 77 of the PDF32000_2008.pdf specification.
     *
     * @param upperLeftX the top left X coordinate of the CropBox.
     * @param upperLeftY the top left Y coordinate of the CropBox.
     * @param lowerRightX the bottom right X coordinate of the CropBox.
     * @param lowerRightY the bottom right Y coordinate of the CropBox.
     */
    public void setCropBox(
            float upperLeftX, float upperLeftY, float lowerRightX, float lowerRightY) {
        this.cropBox = new float[] {upperLeftX, upperLeftY, lowerRightX, lowerRightY};
    }


    /**
     * Sets the page BleedBox.
     * See page 77 of the PDF32000_2008.pdf specification.
     *
     * @param upperLeftX the top left X coordinate of the BleedBox.
     * @param upperLeftY the top left Y coordinate of the BleedBox.
     * @param lowerRightX the bottom right X coordinate of the BleedBox.
     * @param lowerRightY the bottom right Y coordinate of the BleedBox.
     */
    public void setBleedBox(
            float upperLeftX, float upperLeftY, float lowerRightX, float lowerRightY) {
        this.bleedBox = new float[] {upperLeftX, upperLeftY, lowerRightX, lowerRightY};
    }


    /**
     * Sets the page TrimBox.
     * See page 77 of the PDF32000_2008.pdf specification.
     *
     * @param upperLeftX the top left X coordinate of the TrimBox.
     * @param upperLeftY the top left Y coordinate of the TrimBox.
     * @param lowerRightX the bottom right X coordinate of the TrimBox.
     * @param lowerRightY the bottom right Y coordinate of the TrimBox.
     */
    public void setTrimBox(
            float upperLeftX, float upperLeftY, float lowerRightX, float lowerRightY) {
        this.trimBox = new float[] {upperLeftX, upperLeftY, lowerRightX, lowerRightY};
    }


    /**
     * Sets the page ArtBox.
     * See page 77 of the PDF32000_2008.pdf specification.
     *
     * @param upperLeftX the top left X coordinate of the ArtBox.
     * @param upperLeftY the top left Y coordinate of the ArtBox.
     * @param lowerRightX the bottom right X coordinate of the ArtBox.
     * @param lowerRightY the bottom right Y coordinate of the ArtBox.
     */
    public void setArtBox(
            float upperLeftX, float upperLeftY, float lowerRightX, float lowerRightY) {
        this.artBox = new float[] {upperLeftX, upperLeftY, lowerRightX, lowerRightY};
    }


    private void appendPointXY(float x, float y) {
        append(x);
        append(' ');
        append(height - y);
        append(' ');
    }


    private void append(Point point) {
        append(point.x);
        append(' ');
        append(height - point.y);
        append(' ');
    }


    protected void append(String str) {
        int len = str.length();
        for (int i = 0; i < len; i++) {
            buf.write((byte) str.charAt(i));
        }
    }


    protected void append(int num) {
        append(Integer.toString(num));
    }


    protected void append(float val) {
        append(PDF.df.format(val));
    }


    protected void append(char ch) {
        buf.write((byte) ch);
    }


    protected void append(byte b) {
        buf.write(b);
    }


    /**
     *  Appends the specified array of bytes to the page.
     *  @param buffer the array of bytes that is appended.
     *  @throws IOException  If an input or output exception occurred
     */
    public void append(byte[] buffer) throws IOException {
        buf.write(buffer);
    }


    private void drawWord(
            Font font, StringBuilder buf, Map<String, Integer> colors) {
        String str = buf.toString();
        if (str.length() > 0) {
            if (colors.containsKey(str)) {
                setBrushColor(colors.get(str));
            }
            else {
                setBrushColor(Color.black);
            }
        }
        append("[<");
        drawString(font, str);
        append(">] TJ\n");
        buf.setLength(0);
    }


    protected void drawColoredString(
            Font font,
            String str,
            Map<String, Integer> colors) {
        StringBuilder buf1 = new StringBuilder();
        StringBuilder buf2 = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isLetterOrDigit(ch)) {
                drawWord(font, buf2, colors);
                buf1.append(ch);
            }
            else {
                drawWord(font, buf1, colors);
                buf2.append(ch);
            }
        }
        drawWord(font, buf1, colors);
        drawWord(font, buf2, colors);
    }


    protected void setStructElementsPageObjNumber(int pageObjNumber) {
        for (StructElem element : structures) {
            element.pageObjNumber = pageObjNumber;
        }
    }


    public void addBMC(
            String structure,
            String actualText,
            String altDescription) {
        addBMC(structure, null, actualText, altDescription);
    }


    public void addBMC(
            String structure,
            String language,
            String actualText,
            String altDescription) {
        if (pdf.compliance == Compliance.PDF_UA) {
            StructElem element = new StructElem();
            element.structure = structure;
            element.mcid = mcid;
            element.language = language;
            element.actualText = actualText;
            element.altDescription = altDescription;
            structures.add(element);

            append("/");
            append(structure);
            append(" <</MCID ");
            append(mcid++);
            append(">>\n");
            append("BDC\n");
        }
    }


    public void addEMC() {
        if (pdf.compliance == Compliance.PDF_UA) {
            append("EMC\n");
        }
    }


    protected void addAnnotation(Annotation annotation) {
        annotation.y1 = this.height - annotation.y1;
        annotation.y2 = this.height - annotation.y2;
        annots.add(annotation);
        if (pdf.compliance == Compliance.PDF_UA) {
            StructElem element = new StructElem();
            element.structure = StructElem.LINK;
            element.language = annotation.language;
            element.actualText = annotation.actualText;
            element.altDescription = annotation.altDescription;
            element.annotation = annotation;
            structures.add(element);
        }
    }


    protected void beginTransform(
            float x, float y, float xScale, float yScale) {
        append("q\n");

        append(xScale);
        append(" 0 0 ");
        append(yScale);
        append(' ');
        append(x);
        append(' ');
        append(y);
        append(" cm\n");

        append(xScale);
        append(" 0 0 ");
        append(yScale);
        append(' ');
        append(x);
        append(' ');
        append(y);
        append(" Tm\n");
    }


    protected void endTransform() {
        append("Q\n");
    }


    public void drawContents(
            byte[] content,
            float h,    // The height of the graphics object in points.
            float x,
            float y,
            float xScale,
            float yScale) throws Exception {
        beginTransform(x, (this.height - yScale * h) - y, xScale, yScale);
        append(content);
        endTransform();
    }


    public void drawString(
            Font font, String str, float x, float y, float dx) {
        float x1 = x;
        for (int i = 0; i < str.length(); i++) {
            drawString(font, str.substring(i, i + 1), x1, y);
            x1 += dx;
        }
    }


    public void addWatermark(
            Font font, String text) throws Exception {
        float hypotenuse = (float)
                Math.sqrt(this.height * this.height + this.width * this.width);
        float stringWidth = font.stringWidth(text);
        float offset = (hypotenuse - stringWidth) / 2f;
        double angle = Math.atan(this.height / this.width);
        TextLine watermark = new TextLine(font);
        watermark.setColor(Color.lightgrey);
        watermark.setText(text);
        watermark.setLocation(
                (float) (offset * Math.cos(angle)),
                (this.height - (float) (offset * Math.sin(angle))));
        watermark.setTextDirection((int) (angle * (180.0 / Math.PI)));
        watermark.drawOn(this);
    }


    public void invertYAxis() {
        append("1 0 0 -1 0 ");
        append(this.height);
        append(" cm\n");
    }


    /**
     * Rotates the content drawn to the page. Useful if the orientation of the pdf differs.
     * @param deg Multiple of 90 degrees.
     * @param width
     * @param height
     * @throws Exception
     */
    public void rotatePage(int deg, float width, float height) throws Exception {

        float translateX = 0;
        float translateY = 0;
        float skewx = 0;
        float skewy = 0;
        float scalex = 1;
        float scaley = 1;
        switch (deg) {
            case 90:
                translateX = height;
                scalex = 0;
                skewx = -1;
                scaley = 1;
                skewy = 0;
                break;
            case 270:
                translateY = width;
                scalex = 0;
                skewx = 1;
                skewy = -1;
                scaley = 0;
                break;
            case 180:
                translateX = width;
                translateY = height;
                scalex = -1;
                skewx = 0;
                skewy = 0;
                scaley = -1;
                break;
            default:
                return;
        }


        append(scalex);
        append(" ");
        append(skewx);
        append(" ");
        append(skewy);
        append(" ");
        append(scaley);
        append(" 0 0 cm\n");

        append("1 0 0 1 ");
        append(translateX);
        append(" ");
        append(-translateY);
        append(" cm\n");



    }

    /**
     * Transformation matrix.
     * Use save before, restore afterwards!
     * 9 value array like generated by androids Matrix.getValues()
     * @param values the 9 value array generated by Matrix.getValues()
     */
    public void transform(float[] values) {
        float scalex = values[MSCALE_X];
        float scaley = values[MSCALE_Y];
        float transx = values[MTRANS_X];
        float transy = values[MTRANS_Y];

        if (!heightTransformed && scaley != 0) {
            // Because with the height the y coordinate in the pdf coordinate system gets calculated:
            height = height / scaley;
            heightTransformed = true;
        }

        append(scalex);
        append(" ");
        append(values[MSKEW_X]);
        append(" ");
        append(values[MSKEW_Y]);
        append(" ");
        append(scaley);
        append(" ");

        if (Math.asin(values[MSKEW_Y]) != 0f) {
            transx -= values[MSKEW_Y] * height / scaley;
        }

        append(transx);
        append(" ");
        append(-transy);
        append(" cm\n");

    }


    public float[] addHeader(TextLine textLine) throws Exception {
        return addHeader(textLine, 1.5f*textLine.font.ascent);
    }


    public float[] addHeader(TextLine textLine, float offset) throws Exception {
        textLine.setLocation((getWidth() - textLine.getWidth())/2, offset);
        float[] xy = textLine.drawOn(this);
        xy[1] += font.descent;
        return xy;
    }


    public float[] addFooter(TextLine textLine) throws Exception {
        return addFooter(textLine, textLine.font.ascent);
    }


    public float[] addFooter(TextLine textLine, float offset) throws Exception {
        textLine.setLocation((getWidth() - textLine.getWidth())/2, getHeight() - offset);
        return textLine.drawOn(this);
    }

}   // End of Page.java
