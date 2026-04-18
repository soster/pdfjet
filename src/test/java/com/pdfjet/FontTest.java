package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class FontTest extends PDFTestBase {

    @Test
    public void testFontSizeGetSet() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        font.setSize(14f);
        assertEquals(14f, font.getSize(), 0.001f);
        font.setSize(8f);
        assertEquals(8f, font.getSize(), 0.001f);
    }

    @Test
    public void testFontMetricsArePositive() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        font.setSize(12f);
        assertTrue("Ascent must be positive", font.getAscent() > 0f);
        assertTrue("Descent must be positive", font.getDescent() > 0f);
        assertTrue("Height must be positive", font.getHeight() > 0f);
        assertTrue("Height >= ascent + descent",
                font.getHeight() >= font.getAscent() + font.getDescent() - 0.01f);
    }

    @Test
    public void testFontStringWidthIsPositive() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        font.setSize(12f);
        assertTrue("String width must be positive", font.stringWidth("Hello") > 0f);
    }

    @Test
    public void testFontStringWidthScalesWithSize() throws Exception {
        PDF pdf = writablePDF();
        Font small = new Font(pdf, CoreFont.HELVETICA);
        small.setSize(8f);
        Font large = new Font(pdf, CoreFont.HELVETICA);
        large.setSize(24f);
        assertTrue("Larger font produces wider string",
                large.stringWidth("Test") > small.stringWidth("Test"));
    }

    @Test
    public void testAllCoreFontsCanBeCreated() throws Exception {
        PDF pdf = writablePDF();
        CoreFont[] all = {
            CoreFont.HELVETICA, CoreFont.HELVETICA_BOLD,
            CoreFont.HELVETICA_OBLIQUE, CoreFont.HELVETICA_BOLD_OBLIQUE,
            CoreFont.TIMES_ROMAN, CoreFont.TIMES_BOLD,
            CoreFont.TIMES_ITALIC, CoreFont.TIMES_BOLD_ITALIC,
            CoreFont.COURIER, CoreFont.COURIER_BOLD,
            CoreFont.COURIER_OBLIQUE, CoreFont.COURIER_BOLD_OBLIQUE,
            CoreFont.SYMBOL, CoreFont.ZAPF_DINGBATS
        };
        for (CoreFont cf : all) {
            Font f = new Font(pdf, cf);
            assertNotNull("Font should not be null for " + cf, f);
        }
    }

}
