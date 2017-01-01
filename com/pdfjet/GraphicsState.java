package com.pdfjet;


public class GraphicsState {

    // Default values
    private float CA = 1f;
    private float ca = 1f;

    public void set_CA(float paramFloat) {
        if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {
            this.CA = paramFloat;
        }
    }

    public float get_CA() {
        return this.CA;
    }

    public void set_ca(float paramFloat) {
        if ((paramFloat >= 0.0F) && (paramFloat <= 1.0F)) {
            this.ca = paramFloat;
        }
    }

    public float get_ca() {
        return this.ca;
    }
}


