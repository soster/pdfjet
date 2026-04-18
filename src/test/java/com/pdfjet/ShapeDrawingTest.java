package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class ShapeDrawingTest {

    @Test
    public void testBoxDrawOnReturnsPositionAfterBox() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Box box = new Box();
        box.setLocation(100f, 100f);
        box.setSize(50f, 50f);
        box.setColor(Color.black);
        float[] pos = box.drawOn(page);

        // drawOn returns {x + w, y + h + lineWidth}; default lineWidth=0
        assertEquals(150f, pos[0], 0.001f);
        assertEquals(150f, pos[1], 0.001f);

        pdf.complete();
    }

    @Test
    public void testLineDrawOnReturnsEndPoint() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Line line = new Line(100f, 100f, 150f, 150f);
        line.setColor(Color.red);
        float[] pos = line.drawOn(page);

        assertEquals(150f, pos[0], 0.001f);
        assertEquals(150f, pos[1], 0.001f);

        pdf.complete();
    }

    @Test
    public void testFilledBoxProducesDifferentOutputThanOutlineBox() throws Exception {
        ByteArrayOutputStream outline = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(outline);
        Page page1 = new Page(pdf1, Letter.PORTRAIT);
        Box box1 = new Box();
        box1.setLocation(50f, 50f);
        box1.setSize(100f, 100f);
        box1.setColor(Color.black);
        box1.drawOn(page1);
        pdf1.complete();

        ByteArrayOutputStream filled = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(filled);
        Page page2 = new Page(pdf2, Letter.PORTRAIT);
        Box box2 = new Box();
        box2.setLocation(50f, 50f);
        box2.setSize(100f, 100f);
        box2.setColor(Color.black);
        box2.setFillShape(true);
        box2.drawOn(page2);
        pdf2.complete();

        assertFalse("Filled and outline box PDFs should have different content",
                java.util.Arrays.equals(outline.toByteArray(), filled.toByteArray()));
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
        float[] pos = path.drawOn(page);

        assertNotNull(pos);
        assertEquals(2, pos.length);

        pdf.complete();
    }

    @Test
    public void testBezierPathLargerThanStraightPath() throws Exception {
        ByteArrayOutputStream straight = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(straight);
        Page page1 = new Page(pdf1, Letter.PORTRAIT);
        Path p1 = new Path();
        p1.add(new Point(10f, 10f));
        p1.add(new Point(30f, 30f));
        p1.setColor(Color.red);
        p1.drawOn(page1);
        pdf1.complete();

        ByteArrayOutputStream bezier = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(bezier);
        Page page2 = new Page(pdf2, Letter.PORTRAIT);
        Path p2 = new Path();
        p2.add(new Point(13.0f, 0.0f));
        p2.add(new Point(15.5f, 4.5f));
        p2.add(new Point(18.0f, 3.5f));
        p2.add(new Point(15.5f, 13.5f, Point.CONTROL_POINT));
        p2.add(new Point(20.5f, 13.0f, Point.CONTROL_POINT));
        p2.add(new Point(21.0f, 19.0f));
        p2.setColor(Color.red);
        p2.setFillShape(true);
        p2.drawOn(page2);
        pdf2.complete();

        assertTrue("Bezier path with more points produces larger PDF",
                bezier.toByteArray().length > straight.toByteArray().length);
    }

    @Test
    public void testPointDrawOnReturnsPosition() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Point point = new Point(100f, 100f);
        point.setShape(Point.STAR);
        point.setRadius(5f);
        point.setColor(Color.black);
        float[] pos = point.drawOn(page);

        assertNotNull(pos);
        assertEquals(2, pos.length);

        pdf.complete();
    }

    @Test
    public void testMoreShapesProduceLargerPDF() throws Exception {
        ByteArrayOutputStream oneShape = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(oneShape);
        Page page1 = new Page(pdf1, Letter.PORTRAIT);
        new Point(50f, 100f).drawOn(page1);
        pdf1.complete();

        ByteArrayOutputStream sixShapes = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(sixShapes);
        Page page2 = new Page(pdf2, Letter.PORTRAIT);
        int[] shapes = { Point.CIRCLE, Point.DIAMOND, Point.BOX, Point.PLUS, Point.STAR, Point.X_MARK };
        float x = 50f;
        for (int shape : shapes) {
            Point p = new Point(x, 100f);
            p.setShape(shape);
            p.setRadius(8f);
            p.setColor(Color.blue);
            p.drawOn(page2);
            x += 40f;
        }
        pdf2.complete();

        assertTrue("Six shapes produce larger PDF than one shape",
                sixShapes.toByteArray().length > oneShape.toByteArray().length);
    }

    @Test
    public void testColorConstants() {
        assertEquals(0x000000, Color.black);
        assertEquals(0xffffff, Color.white);
        assertEquals(0xff0000, Color.red);
        assertEquals(0x0000ff, Color.blue);
        assertEquals(0x00ff00, Color.lime);
    }

}
