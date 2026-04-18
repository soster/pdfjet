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
    public void testTransparencyRegistersExtGStateResource() throws Exception {
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

        String content = new String(os.toByteArray(), "ISO-8859-1");
        assertTrue("PDF must contain ExtGState resource for transparency",
                content.contains("/ExtGState"));
    }

    @Test
    public void testTransparentPDFDiffersFromOpaqueOne() throws Exception {
        ByteArrayOutputStream opaque = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(opaque);
        Page page1 = new Page(pdf1, Letter.PORTRAIT);
        Box box1 = new Box();
        box1.setLocation(100f, 100f);
        box1.setSize(100f, 100f);
        box1.setFillShape(true);
        box1.setColor(Color.blue);
        box1.drawOn(page1);
        pdf1.complete();

        ByteArrayOutputStream transparent = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(transparent);
        Page page2 = new Page(pdf2, Letter.PORTRAIT);
        GraphicsState gs = new GraphicsState();
        gs.setAlphaNonStroking(0.4f);
        page2.setGraphicsState(gs);
        Box box2 = new Box();
        box2.setLocation(100f, 100f);
        box2.setSize(100f, 100f);
        box2.setFillShape(true);
        box2.setColor(Color.blue);
        box2.drawOn(page2);
        page2.setGraphicsState(new GraphicsState());
        pdf2.complete();

        assertNotEquals("Transparent PDF should differ in size from opaque PDF",
                opaque.toByteArray().length, transparent.toByteArray().length);
    }

}
