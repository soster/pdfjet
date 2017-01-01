package com.pdfjet;


public class Title {
    public TextLine prefix = null;
    public TextLine text = null;

    public Title(Font paramFont, String paramString, float paramFloat1, float paramFloat2) {
        this.prefix = new TextLine(paramFont);
        this.text = new TextLine(paramFont, paramString);
        this.prefix.setLocation(paramFloat1, paramFloat2);
        this.text.setLocation(paramFloat1, paramFloat2);
    }

    public Title setPrefix(String paramString) {
        this.prefix.setText(paramString);
        return this;
    }

    public Title setOffset(float paramFloat) {
        this.text.setLocation(this.text.x + paramFloat, this.text.y);
        return this;
    }

    public void drawOn(Page paramPage) throws Exception {
        if (!this.prefix.equals("")) {
            this.prefix.drawOn(paramPage);
        }
        this.text.drawOn(paramPage);
    }
}


