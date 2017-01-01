package com.pdfjet;


public class TextLine
        implements Drawable {
    protected float x;


    protected float y;


    protected Font font;


    protected Font fallbackFont;


    protected String str;


    protected boolean trailingSpace = true;

    private String uri;

    private String key;
    private boolean underline = false;
    private boolean strikeout = false;
    private String underlineTTS = "underline";
    private String strikeoutTTS = "strikeout";

    private int degrees = 0;
    private int color = 0;

    private float box_x;

    private float box_y;
    private int textEffect = 0;
    private float verticalOffset = 0.0F;

    private String language = null;
    private String altDescription = null;
    private String actualText = null;

    private String uriLanguage = null;
    private String uriAltDescription = null;
    private String uriActualText = null;


    public TextLine(Font font) {
        this.font = font;
    }


    public TextLine(Font font, String text) {
        this.font = font;
        this.str = text;
        if (this.altDescription == null) {
            this.altDescription = text;
        }
        if (this.actualText == null) {
            this.actualText = text;
        }
    }


    public TextLine setText(String text) {
        this.str = text;
        if (this.altDescription == null) {
            this.altDescription = text;
        }
        if (this.actualText == null) {
            this.actualText = text;
        }
        return this;
    }


    public String getText() {
        return this.str;
    }


    public TextLine setPosition(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    public TextLine setPosition(float x, float y) {
        return setLocation(x, y);
    }


    public TextLine setLocation(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }


    public TextLine setFont(Font font) {
        this.font = font;
        return this;
    }


    public Font getFont() {
        return this.font;
    }


    public TextLine setFontSize(float size) {
        this.font.setSize(size);
        return this;
    }


    public TextLine setFallbackFont(Font fallbackFont) {
        this.fallbackFont = fallbackFont;
        return this;
    }


    public TextLine setFallbackFontSize(float size) {
        this.fallbackFont.setSize(size);
        return this;
    }


    public Font getFallbackFont() {
        return this.fallbackFont;
    }


    public TextLine setColor(int color) {
        this.color = color;
        return this;
    }


    public TextLine setColor(int[] color) {
        this.color = (color[0] << 16 | color[1] << 8 | color[2]);
        return this;
    }


    public int getColor() {
        return this.color;
    }


    public float getDestinationY() {
        return this.y - this.font.getSize();
    }


    public float getY() {
        return getDestinationY();
    }


    public float getWidth() {
        if (this.fallbackFont == null) {
            return this.font.stringWidth(this.str);
        }
        return this.font.stringWidth(this.fallbackFont, this.str);
    }


    public float getHeight() {
        return this.font.getHeight();
    }


    public TextLine setURIAction(String uri) {
        this.uri = uri;
        return this;
    }


    public String getURIAction() {
        return this.uri;
    }


    public TextLine setGoToAction(String paramString) {
        this.key = paramString;
        return this;
    }


    public String getGoToAction() {
        return this.key;
    }


    public TextLine setUnderline(boolean underline) {
        this.underline = underline;
        return this;
    }


    public boolean getUnderline() {
        return this.underline;
    }


    public TextLine setStrikeout(boolean strikeout) {
        this.strikeout = strikeout;
        return this;
    }


    public boolean getStrikeout() {
        return this.strikeout;
    }


    public TextLine setTextDirection(int degrees) {
        this.degrees = degrees;
        return this;
    }


    public int getTextDirection() {
        return this.degrees;
    }


    public TextLine setTextEffect(int paramInt) {
        this.textEffect = paramInt;
        return this;
    }


    public int getTextEffect() {
        return this.textEffect;
    }


    public TextLine setVerticalOffset(float paramFloat) {
        this.verticalOffset = paramFloat;
        return this;
    }


    public float getVerticalOffset() {
        return this.verticalOffset;
    }


    public TextLine setTrailingSpace(boolean trailingSpace) {
        this.trailingSpace = trailingSpace;
        return this;
    }


    public boolean getTrailingSpace() {
        return this.trailingSpace;
    }

    public TextLine setLanguage(String paramString) {
        this.language = paramString;
        return this;
    }

    public String getLanguage() {
        return this.language;
    }


    public TextLine setAltDescription(String altDescription) {
        this.altDescription = altDescription;
        return this;
    }

    public String getAltDescription() {
        return this.altDescription;
    }


    public TextLine setActualText(String actualText) {
        this.actualText = actualText;
        return this;
    }

    public String getActualText() {
        return this.actualText;
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


    public TextLine placeIn(Box box)
            throws Exception {
        placeIn(box, 0.0F, 0.0F);
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
    public TextLine placeIn(Box box, double xOffset, double yOffset)
            throws Exception {
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
    public TextLine placeIn(Box box, float xOffset, float yOffset)
            throws Exception {
        this.box_x = (box.x + xOffset);
        this.box_y = (box.y + yOffset);
        return this;
    }

    /**
     *  Draws this text line on the specified page.
     *
     *  @param page the page to draw this text line on.
     *  @return x and y coordinates of the bottom right corner of this component.
     *  @throws Exception
     */
    public float[] drawOn(Page page)
            throws Exception {
        return drawOn(page, true);
    }

    /**
     *  Draws this text line on the specified page if the draw parameter is true.
     *
     *  @param page the page to draw this text line on.
     *  @param draw if draw is false - no action is performed.
     */
    protected float[] drawOn(Page page, boolean draw)
            throws Exception {
        if (page == null || !draw || str == null || str.equals("")) {
            return new float[] {x, y};
        }

        page.setTextDirection(degrees);

        x += box_x;
        y += box_y;

        page.setBrushColor(color);
        page.addBMC(StructElem.SPAN, language, altDescription, actualText);
        if (fallbackFont == null) {
            page.drawString(font, str, x, y);
        }
        else {
            page.drawString(font, fallbackFont, str, x, y);
        }
        page.addEMC();

        double radians = Math.PI * degrees / 180.0;
        if (underline) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double x_adjust = font.underlinePosition * Math.sin(radians);
            double y_adjust = font.underlinePosition * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(StructElem.SPAN, language, underlineTTS, underlineTTS);
            page.moveTo(x + x_adjust, y + y_adjust);
            page.lineTo(x2 + x_adjust, y2 + y_adjust);
            page.strokePath();
            page.addEMC();
        }

        if (strikeout) {
            page.setPenWidth(font.underlineThickness);
            page.setPenColor(color);
            float lineLength = font.stringWidth(str);
            double x_adjust = ( font.body_height / 4.0 ) * Math.sin(radians);
            double y_adjust = ( font.body_height / 4.0 ) * Math.cos(radians);
            double x2 = x + lineLength * Math.cos(radians);
            double y2 = y - lineLength * Math.sin(radians);
            page.addBMC(StructElem.SPAN, language, strikeoutTTS, strikeoutTTS);
            page.moveTo(x - x_adjust, y - y_adjust);
            page.lineTo(x2 - x_adjust, y2 - y_adjust);
            page.strokePath();
            page.addEMC();
        }

        if (uri != null || key != null) {
            page.addAnnotation(new Annotation(
                    uri,
                    key,    // The destination name
                    x,
                    page.height - (y - font.ascent),
                    x + font.stringWidth(str),
                    page.height - (y - font.descent),
                    uriLanguage,
                    uriAltDescription,
                    uriActualText));
        }
        page.setTextDirection(0);

        float len = font.stringWidth(str);
        double x_max = Math.max((double) x, x + len*Math.cos(radians));
        double y_max = Math.max((double) y, y - len*Math.sin(radians));

        return new float[] {(float) x_max, (float) y_max};
    }
}


