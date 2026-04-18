package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class CompositeTextLineTest extends PDFTestBase {

    @Test
    public void testSubscriptAndSuperscriptEffects() throws Exception {
        PDF pdf = writablePDF();
        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);

        TextLine sub = new TextLine(f2, "2");
        sub.setTextEffect(Effect.SUBSCRIPT);
        assertEquals(Effect.SUBSCRIPT, sub.getTextEffect());

        TextLine sup = new TextLine(f2, "2-");
        sup.setTextEffect(Effect.SUPERSCRIPT);
        assertEquals(Effect.SUPERSCRIPT, sup.getTextEffect());
    }

    @Test
    public void testH2OComposite() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);

        Page page = new Page(pdf, Letter.PORTRAIT);

        CompositeTextLine composite = new CompositeTextLine(50f, 50f);
        composite.setFontSize(14f);

        TextLine h = new TextLine(f1, "H");
        TextLine two = new TextLine(f2, "2");
        TextLine o = new TextLine(f1, "O");
        two.setTextEffect(Effect.SUBSCRIPT);

        composite.addComponent(h);
        composite.addComponent(two);
        composite.addComponent(o);

        assertEquals(3, composite.getNumberOfTextLines());
        assertTrue("Composite width must be positive", composite.getWidth() > 0f);

        float[] xy = composite.drawOn(page);
        assertNotNull(xy);
        assertEquals(2, xy.length);

        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
    }

    @Test
    public void testGetMinMaxBoundsWithMixedEffects() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);

        Page page = new Page(pdf, Letter.PORTRAIT);

        CompositeTextLine composite = new CompositeTextLine(50f, 100f);
        composite.setFontSize(14f);
        TextLine text1 = new TextLine(f1, "SO");
        TextLine text2 = new TextLine(f2, "4");
        TextLine text3 = new TextLine(f2, "2-");
        text2.setTextEffect(Effect.SUBSCRIPT);
        text3.setTextEffect(Effect.SUPERSCRIPT);
        composite.addComponent(text1);
        composite.addComponent(text2);
        composite.addComponent(text3);

        composite.drawOn(page);
        pdf.complete();

        float[] minMax = composite.getMinMax();
        assertNotNull(minMax);
        assertEquals(2, minMax.length);
        assertTrue("minMax[0] must be less than minMax[1]", minMax[0] < minMax[1]);
    }

}
