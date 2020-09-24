package com.pdfjet;


public class GraphicsState {

    // Default values
    private float CA = 1f;
    private float ca = 1f;

    public void setAlphaStroking(float CA) {
        if (CA >= 0f && CA <= 1f) {
            this.CA = CA;
        }
    }

    public float getAlphaStroking() {
        return this.CA;
    }

    public void setAlphaNonStroking(float ca) {
        if (ca >= 0f && ca <= 1f) {
            this.ca = ca;
        }
    }

    public float getAlphaNonStroking() {
        return this.ca;
    }

}
