package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;

public class PageSizeTest {

    @Test
    public void testA4Dimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, A4.PORTRAIT);
        assertEquals(595f, page.getWidth(), 0.001f);
        assertEquals(842f, page.getHeight(), 0.001f);
    }

    @Test
    public void testLetterPortraitDimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, Letter.PORTRAIT);
        assertEquals(612f, page.getWidth(), 0.001f);
        assertEquals(792f, page.getHeight(), 0.001f);
    }

    @Test
    public void testLetterLandscapeDimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, Letter.LANDSCAPE);
        assertEquals(792f, page.getWidth(), 0.001f);
        assertEquals(612f, page.getHeight(), 0.001f);
    }

    @Test
    public void testLegalDimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, Legal.PORTRAIT);
        assertEquals(612f, page.getWidth(), 0.001f);
        assertEquals(1008f, page.getHeight(), 0.001f);
    }

    @Test
    public void testA3Dimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, A3.PORTRAIT);
        assertEquals(842f, page.getWidth(), 0.001f);
        assertEquals(1191f, page.getHeight(), 0.001f);
    }

    @Test
    public void testA5Dimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, A5.PORTRAIT);
        assertEquals(420f, page.getWidth(), 0.001f);
        assertEquals(595f, page.getHeight(), 0.001f);
    }

    @Test
    public void testPortraitIsNarrowerThanLandscape() {
        assertTrue("Portrait width < portrait height",
                Letter.PORTRAIT[0] < Letter.PORTRAIT[1]);
        assertTrue("Landscape width > landscape height",
                Letter.LANDSCAPE[0] > Letter.LANDSCAPE[1]);
    }

}
