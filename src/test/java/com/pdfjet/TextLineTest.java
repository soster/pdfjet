package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TextLineTest extends PDFTestBase {

    @Test
    public void testTextLineContentAndFont() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextLine line = new TextLine(font, "Hello World");
        assertEquals("Hello World", line.getText());
        assertEquals(font, line.getFont());
    }

    @Test
    public void testTextLineColor() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextLine line = new TextLine(font, "test");
        line.setColor(Color.red);
        assertEquals(Color.red, line.getColor());
    }

    @Test
    public void testTextLineUnderlineAndStrikeout() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextLine line = new TextLine(font, "test");

        line.setUnderline(true);
        assertTrue(line.getUnderline());
        line.setUnderline(false);
        assertFalse(line.getUnderline());

        line.setStrikeout(true);
        assertTrue(line.getStrikeout());
        line.setStrikeout(false);
        assertFalse(line.getStrikeout());
    }

    @Test
    public void testTextLineTextDirection() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextLine line = new TextLine(font, "test");
        for (int dir : new int[]{0, 45, 90, 135, 180, 270, 315}) {
            line.setTextDirection(dir);
            assertEquals(dir, line.getTextDirection());
        }
    }

    @Test
    public void testTextLineURIAction() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextLine line = new TextLine(font, "Click me");
        line.setURIAction("https://example.com");
        assertEquals("https://example.com", line.getURIAction());
    }

    @Test
    public void testTextLineDrawOnReturnsPositionAfterText() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font font = new Font(pdf, CoreFont.HELVETICA);
        font.setSize(12f);
        TextLine line = new TextLine(font, "Hello, World!");
        line.setLocation(50f, 50f);
        Page page = new Page(pdf, Letter.PORTRAIT);
        float[] pos = line.drawOn(page);
        pdf.complete();

        assertTrue("TextLine width should be positive", line.getWidth() > 0f);
        // drawOn returns position after the text; x should advance past start
        assertTrue("X cursor advanced past starting x=50", pos[0] > 50f);
    }

    @Test
    public void testRotatedTextProducesLargerPDFThanNoText() throws Exception {
        ByteArrayOutputStream noText = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(noText);
        new Page(pdf1, Letter.PORTRAIT);
        pdf1.complete();

        ByteArrayOutputStream withText = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(withText);
        Font font = new Font(pdf2, CoreFont.HELVETICA_BOLD);
        font.setItalic(true);
        Page page = new Page(pdf2, Letter.PORTRAIT);
        TextLine text = new TextLine(font);
        text.setLocation(300f, 300f);
        for (int i = 0; i < 360; i += 45) {
            text.setTextDirection(i);
            text.setUnderline(true);
            text.setText("Hello " + i + " degrees");
            text.drawOn(page);
        }
        pdf2.complete();

        assertTrue("PDF with rotated text should be larger than empty page",
                withText.toByteArray().length > noText.toByteArray().length);

        String content = new String(withText.toByteArray(), "ISO-8859-1");
        assertTrue("Helvetica-Bold referenced in PDF", content.contains("/Helvetica-Bold"));
    }

}
