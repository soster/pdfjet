package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class ShapeDrawingTest {

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
        assertTrue(os.toByteArray().length > 0);
    }

    @Test
    public void testBoxFilledDrawing() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Box box = new Box();
        box.setLocation(50f, 50f);
        box.setSize(200f, 120f);
        box.setColor(Color.black);
        box.setLineWidth(1.5f);
        box.setFillShape(true);
        box.drawOn(page);

        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
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
        assertTrue(os.toByteArray().length > 0);
    }

    @Test
    public void testPathWithBezierControlPoints() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        Path path = new Path();
        path.add(new Point(13.0f, 0.0f));
        path.add(new Point(15.5f, 4.5f));
        path.add(new Point(18.0f, 3.5f));
        path.add(new Point(15.5f, 13.5f, Point.CONTROL_POINT));
        path.add(new Point(20.5f, 13.0f, Point.CONTROL_POINT));
        path.add(new Point(21.0f, 19.0f));
        path.setColor(Color.red);
        path.setFillShape(true);
        path.drawOn(page);

        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
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
        assertTrue(os.toByteArray().length > 0);
    }

    @Test
    public void testAllPointShapes() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Page page = new Page(pdf, Letter.PORTRAIT);

        int[] shapes = {
            Point.CIRCLE, Point.DIAMOND, Point.BOX,
            Point.PLUS, Point.STAR, Point.X_MARK
        };
        float x = 50f;
        for (int shape : shapes) {
            Point p = new Point(x, 100f);
            p.setShape(shape);
            p.setRadius(8f);
            p.setColor(Color.blue);
            p.drawOn(page);
            x += 40f;
        }
        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
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
