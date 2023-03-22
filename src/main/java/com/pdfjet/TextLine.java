/**
 *  TextLine.java
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
 *  Used to create text line objects.
 *
 */
public class TextLine implements Drawable {

    protected float x;
    protected float y;

    protected Font font;
    protected Font fallbackFont;
    protected String text;
    protected boolean trailingSpace = true;

    private String uri;
    private String key;

    private boolean underline = false;
    private boolean strikeout = false;

    private int degrees = 0;
    private int color = Color.black;

    private float xBox;
    private float yBox;

    private int textEffect = Effect.NORMAL;
    private float verticalOffset = 0f;

    private String language = null;
    private String altDescription = null;

    private String uriLanguage = null;
    private String uriActualText = null;
    private String uriAltDescription = null;

    private String structureType = StructElem.P;


    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     */
    public TextLine(Font font) {
        this.font = font;
    }


    /**
     *  Constructor for creating text line objects.
     *
     *  @param font the font to use.
     *  @param text the text.
     */
    public TextLine(Font font, String text) {
        this.font = font;
        this.text = text;
        this.altDescription = text;
    }


    /**
     *  Sets the text.
     *
     *  @param text the text.
     *  @return this TextLine.
     */
    public TextLine setText(String text) {
        this.text = text;
        this.altDescription = text;
        return this;
    }


    /**
     *  Returns the text.
     *
     *  @return the text.
     */
    public String getText() {
        return text;
    }

    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     *  Sets the position where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }

    /**
     *  Sets the location where this text line will be drawn on the page.
     *
     *  @param x the x coordinate of the text line.
     *  @param y the y coordinate of the text line.
     *  @return this TextLine.
     */
    public TextLine setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public TextLine setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    public float advance(float leading) {
        this.y += leading;
        return this.y;
    }


    /**
     *  Sets the font to use for this text line.
     *
     *  @param font the font to use.
     *  @return this TextLine.
     */
    public TextLine setFont(Font font) {
        this.font = font;
        return this;
    }


    /**
     *  Gets the font to use for this text line.
     *
     *  @return font the font to use.
     */
    public Font getFont() {
        return font;
    }


    /**
     *  Sets the font size to use for this text line.
     *
     *  @param fontSize the fontSize to use.
     *  @return this TextLine.
     */
    public TextLine setFontSize(float fontSize) {
        this.font.setSize(fontSize);
        return this;
    }


    /**
     *  Sets the fallback font.
     *
     *  @param fallbackFont the fallback font.
     *  @return this TextLine.
     */
    public TextLine setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
        return this;
    }


    /**
     *  Sets the fallback font size to use for this text line.
     *
     *  @param fallbackFontSize the fallback font size.
     *  @return this TextLine.
     */
    public TextLine setFallbackFontSize(float fallbackFontSize) {
        this.fallbackFont.setSize(fallbackFontSize);
        return this;
    }


    /**
     *  Returns the fallback font.
     *
     *  @return the fallback font.
     */
    public Font getFallbackFont() {
        return this.fallbackFont;
    }


    /**
     *  Sets the color for this text line.
     *
     *  @param color the color is specified as an integer.
     *  @return this TextLine.
     */
    public TextLine setColor(int color) {
        this.color = color;
        return this;
    }


    /**
     *  Sets the pen color.
     *  See the Color class for predefined values or define your own using 0x00RRGGBB packed integers.
     *
     *  @param color the color.
     *  @return this TextLine.
     */
    public TextLine setColor(int[] color) {
        this.color = color[0] << 16 | color[1] << 8 | color[2];
        return this;
    }


    /**
     *  Returns the text line color.
     *
     *  @return the text line color.
     */
    public int getColor() {
        return this.color;
    }


    /**
     * Returns the y coordinate of the destination.
     *
     * @return the y coordinate of the destination.
     */
    public float getDestinationY() {
        return y - font.getSize();
    }


    /**
     *  Returns the width of this TextLine.
     *
     *  @return the width.
     */
    public float getWidth() {
        return font.stringWidth(fallbackFont, text);
    }


    public float getStringWidth(String text) {
        return font.stringWidth(fallbackFont, text);
    }


    /**
     *  Returns the height of this TextLine.
     *
     *  @return the height.
     */
    public float getHeight() {
        return font.getHeight();
    }


    /**
     *  Sets the URI for the "click text line" action.
     *
     *  @param uri the URI
     *  @return this TextLine.
     */
    public TextLine setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    /**
     *  Returns the action URI.
     *
     *  @return the action URI.
     */
    public String getURIAction() {
        return this.uri;
    }


    /**
     *  Sets the destination key for the action.
     *
     *  @param key the destination name.
     *  @return this TextLine.
     */
    public TextLine setGoToAction(String key) {
        this.key = key;
        return this;
    }


    /**
     * Returns the GoTo action string.
     *
     * @return the GoTo action string.
     */
    public String getGoToAction() {
        return this.key;
    }


    /**
     *  Sets the underline variable.
     *  If the value of the underline variable is 'true' - the text is underlined.
     *
     *  @param underline the underline flag.
     *  @return this TextLine.
     */
    public TextLine setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }


    /**
     * Returns the underline flag.
     *
     * @return the underline flag.
     */
    public boolean getUnderline() {
        return this.underline;
    }


    /**
     *  Sets the strike variable.
     *  If the value of the strike variable is 'true' - a strike line is drawn through the text.
     *
     *  @param strikeout the strikeout flag.
     *  @return this TextLine.
     */
    public TextLine setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
        return this;
    }


    /**
     *  Returns the strikeout flag.
     *
     *  @return the strikeout flag.
     */
    public boolean getStrikeout() {
        return this.strikeout;
    }


    /**
     *  Sets the direction in which to draw the text.
     *
     *  @param degrees the number of degrees.
     *  @return this TextLine.
     */
    public TextLine setTextDirection(int degrees) {
        this.degrees = degrees;
        return this;
    }


    /**
     * Returns the text direction.
     *
     * @return the text direction.
     */
    public int getTextDirection() {
        return degrees;
    }


    /**
     *  Sets the text effect.
     *
     *  @param textEffect Effect.NORMAL, Effect.SUBSCRIPT or Effect.SUPERSCRIPT.
     *  @return this TextLine.
     */
    public TextLine setTextEffect(int textEffect) {
        this.textEffect = textEffect;
        return this;
    }


    /**
     *  Returns the text effect.
     *
     *  @return the text effect.
     */
    public int getTextEffect() {
        return textEffect;
    }


    /**
     *  Sets the vertical offset of the text.
     *
     *  @param verticalOffset the vertical offset.
     *  @return this TextLine.
     */
    public TextLine setVerticalOffset(float verticalOffset) {
        this.verticalOffset = verticalOffset;
        return this;
    }


    /**
     *  Returns the vertical text offset.
     *
     *  @return the vertical text offset.
     */
    public float getVerticalOffset() {
        return verticalOffset;
    }


    /**
     *  Sets the trailing space after this text line when used in paragraph.
     *
     *  @param trailingSpace the trailing space.
     *  @return this TextLine.
     */
    public TextLine setTrailingSpace(boolean trailingSpace) {
        this.trailingSpace = trailingSpace;
        return this;
    }


    /**
     *  Returns the trailing space.
     *
     *  @return the trailing space.
     */
    public boolean getTrailingSpace() {
        return trailingSpace;
    }


    public TextLine setLanguage(String language) {
        this.language = language;
        return this;
    }


    public String getLanguage() {
        return this.language;
    }


    /**
     *  Sets the alternate description of this text line.
     *
     *  @param altDescription the alternate description of the text line.
     *  @return this TextLine.
     */
    public TextLine setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }


    public String getAltDescription() {
        return altDescription;
    }


    public TextLine setURILanguage(String uriLanguage) {
        this.uriLanguage = uriLanguage;
        return this;
    }


    public TextLine setURIAltDescription(String uriAltDescription) {
        this.uriAltDescription = uriAltDescription;
        return this;
    }


    public TextLine setURIActualText(String uriActualText) {
        this.uriActualText = uriActualText;
        return this;
    }


    public TextLine setStructureType(String structureType) {
        this.structureType = structureType;
        return this;
    }


    /**
     *  Places this text line in the specified box.
     *
     *  @param box the specified box.
     *  @return this TextLine.
     */
    public TextLine placeIn(Box box) {
        placeIn(box, 0f, 0f);
        return this;
    }


    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     *  @return this TextLine.
     */
    public TextLine placeIn(
            Box box,
            double xOffset,
            double yOffset) {
        return placeIn(box, (float) xOffset, (float) yOffset);
    }


    /**
     *  Places this text line in the box at the specified offset.
     *
     *  @param box the specified box.
     *  @param xOffset the x offset from the top left corner of the box.
     *  @param yOffset the y offset from the top left corner of the box.
     *  @return this TextLine.
     */
    public TextLine placeIn(
            Box box,
            float xOffset,
            float yOffset) {
        xBox = box.x + xOffset;
        yBox = box.y + yOffset;
        return this;
    }


    /**
     *  Draws this text line on the specified page.
     *
     *  @param page the page to draw this text line on.
     *  @return float[] with the coordinates of the bottom right corner.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        if (page == null || text == null || text.equals("")) {
            return new float[] {x, y};
        }

        page.setTextDirection(degrees);

        x += xBox;
        y += yBox;

        page.setBrushColor(color);
        page.addBMC(structureType, language, text, altDescription);
        page.drawString(font, fallbackFont, text, x, y);
        page.addEMC();

        double radians = Math.PI * degrees / 180.0;
        if (underline) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(fallbackFont, text);
            double xAdjust = font.underlinePosition * Math.sin(radians) + verticalOffset;
            double yAdjust = font.underlinePosition * Math.cos(radians) + verticalOffset;
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(structureType, language, text, "Underlined text: " + text);
            page.moveTo(x + xAdjust, y + yAdjust);
            page.lineTo(x2 + xAdjust, y2 + yAdjust);
            page.strokePath();
            page.addEMC();
        }

        if (strikeout) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(fallbackFont, text);
            double xAdjust = (font.bodyHeight / 4.0) * Math.sin(radians);
            double yAdjust = (font.bodyHeight / 4.0) * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(structureType, language, text, "Strikethrough text: " + text);
            page.moveTo(x - xAdjust, y - yAdjust);
            page.lineTo(x2 - xAdjust, y2 - yAdjust);
            page.strokePath();
            page.addEMC();
        }

        if (uri != null || key != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    y - font.ascent,
                    x + font.stringWidth(fallbackFont, text),
                    y + font.descent,
                    uriLanguage,
                    uriActualText,
                    uriAltDescription));
        }
        page.setTextDirection(0);

        float len = font.stringWidth(fallbackFont, text);
        double xMax = Math.max(x, x + len*Math.cos(radians));
        double yMax = Math.max(y, y - len*Math.sin(radians));

        return new float[] {(float) xMax, (float) yMax};
    }

}   // End of TextLine.java
