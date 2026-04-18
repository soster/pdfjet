package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class PDFJetTest {

    @Test
    public void testBasicPDFCreation() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Font font = new Font(pdf, CoreFont.HELVETICA);
        page.setTextFont(font);
        page.drawString(font, "Hello PDFjet", 100f, 100f);

        pdf.complete();
        assertTrue("PDF output should not be empty", os.toByteArray().length > 0);
    }

    @Test
    public void testPageDimensions() throws Exception {
        PDF pdf = new PDF();
        Page page = new Page(pdf, A4.PORTRAIT);
        assertEquals(595f, page.getWidth(), 0.001f);
        assertEquals(842f, page.getHeight(), 0.001f);
    }

    @Test
    public void testColorConstants() {
        assertEquals(0x000000, Color.black);
        assertEquals(0xffffff, Color.white);
    }

    @Test
    public void testBoxAndLineDrawing() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Box box = new Box();
        box.setLocation(100f, 100f);
        box.setSize(50f, 50f);
        box.setColor(Color.black);
        box.drawOn(page);

        Line line = new Line(100f, 100f, 150f, 150f);
        line.setColor(Color.red);
        line.drawOn(page);

        pdf.complete();
        assertTrue("PDF output should not be empty", os.toByteArray().length > 0);
    }

    @Test
    public void testPathDrawing() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Path path = new Path();
        path.add(new Point(10f, 10f));
        path.add(new Point(20f, 20f));
        path.add(new Point(30f, 10f));
        path.setColor(Color.blue);
        path.drawOn(page);

        pdf.complete();
        assertTrue("PDF output should not be empty", os.toByteArray().length > 0);
    }

    @Test
    public void testPointDrawing() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Point point = new Point(100f, 100f);
        point.setShape(Point.STAR);
        point.setRadius(5f);
        point.setColor(Color.black);
        point.drawOn(page);

        pdf.complete();
        assertTrue("PDF output should not be empty", os.toByteArray().length > 0);
    }

}
