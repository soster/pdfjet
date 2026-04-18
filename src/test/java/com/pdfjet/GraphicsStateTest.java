package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class GraphicsStateTest {

    @Test
    public void testAlphaGetSet() {
        GraphicsState gs = new GraphicsState();
        gs.setAlphaStroking(0.5f);
        gs.setAlphaNonStroking(0.75f);
        assertEquals(0.5f, gs.getAlphaStroking(), 0.001f);
        assertEquals(0.75f, gs.getAlphaNonStroking(), 0.001f);
    }

    @Test
    public void testDefaultAlphaIsOpaque() {
        GraphicsState gs = new GraphicsState();
        assertEquals(1.0f, gs.getAlphaStroking(), 0.001f);
        assertEquals(1.0f, gs.getAlphaNonStroking(), 0.001f);
    }

    @Test
    public void testTransparencyAppliedToDrawing() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        GraphicsState gs = new GraphicsState();
        gs.setAlphaNonStroking(0.4f);
        page.setGraphicsState(gs);

        Box box = new Box();
        box.setLocation(100f, 100f);
        box.setSize(100f, 100f);
        box.setFillShape(true);
        box.setColor(Color.blue);
        box.drawOn(page);

        page.setGraphicsState(new GraphicsState());
        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
    }

}
