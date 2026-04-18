package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;

public class TextLayoutTest extends PDFTestBase {

    @Test
    public void testTextColumnDrawOnReturnsPositionBelowContent() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font f1 = new Font(pdf, CoreFont.HELVETICA);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(10f);
        f2.setSize(14f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        TextColumn column = new TextColumn();
        column.setSpaceBetweenLines(5.0f);
        column.setSpaceBetweenParagraphs(10.0f);

        Paragraph p1 = new Paragraph();
        p1.setAlignment(Align.CENTER);
        p1.add(new TextLine(f2, "Title Paragraph"));

        Paragraph p2 = new Paragraph();
        p2.setAlignment(Align.JUSTIFY);
        p2.add(new TextLine(f1,
                "This is a test paragraph with justified text alignment. "
                + "It contains enough text to wrap across multiple lines when rendered."));

        Paragraph p3 = new Paragraph();
        p3.setAlignment(Align.RIGHT);
        TextLine source = new TextLine(f1, "Source: Test Data");
        source.setColor(Color.blue);
        p3.add(source);

        column.addParagraph(p1);
        column.addParagraph(p2);
        column.addParagraph(p3);
        column.setLocation(90f, 100f);
        column.setSize(400f, 500f);

        float[] endPoint = column.drawOn(page);
        pdf.complete();

        assertEquals(2, endPoint.length);
        // Y cursor must advance below the starting y=100
        assertTrue("Y cursor advanced past column start y=100", endPoint[1] > 100f);
        // Y cursor must stay within the column height (100 + 500 = 600)
        assertTrue("Y cursor stays within column bounds", endPoint[1] <= 600f);
    }

    @Test
    public void testTextBoxContent() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextBox tb = new TextBox(font);
        tb.setText("Hello TextBox");
        assertEquals("Hello TextBox", tb.getText());
    }

    @Test
    public void testTextBoxAlignment() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);
        TextBox tb = new TextBox(font);
        tb.setTextAlignment(Align.CENTER);
        assertEquals(Align.CENTER, tb.getTextAlignment());
        tb.setTextAlignment(Align.RIGHT);
        assertEquals(Align.RIGHT, tb.getTextAlignment());
    }

    @Test
    public void testOptionalContentGroupRegistersOCGResource() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font font = new Font(pdf, CoreFont.HELVETICA);
        Page page = new Page(pdf, Letter.PORTRAIT);

        TextBox tb = new TextBox(font);
        tb.setText("Visible Layer Text");
        tb.setLocation(100f, 100f);

        OptionalContentGroup group = new OptionalContentGroup("Layer1");
        group.add(tb);
        group.setVisible(true);
        group.setPrintable(true);
        group.drawOn(page);

        Line line = new Line(100f, 200f, 300f, 200f);
        line.setColor(Color.green);

        OptionalContentGroup hiddenGroup = new OptionalContentGroup("Hidden");
        hiddenGroup.add(line);
        hiddenGroup.drawOn(page);

        pdf.complete();

        String content = new String(os.toByteArray(), "ISO-8859-1");
        assertTrue("PDF references OCG type", content.contains("/OCG"));
        assertTrue("Layer name 'Layer1' present in PDF", content.contains("Layer1"));
        assertTrue("Layer name 'Hidden' present in PDF", content.contains("Hidden"));
    }

    @Test
    public void testChartDrawOnReturnsBottomRightCorner() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(10f);
        f2.setSize(8f);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Chart chart = new Chart(f1, f2);
        chart.setLocation(70f, 50f);
        chart.setSize(400f, 250f);
        chart.setTitle("Unit Test Chart");
        chart.setXAxisTitle("X");
        chart.setYAxisTitle("Y");

        java.util.List<java.util.List<Point>> chartData = new java.util.ArrayList<java.util.List<Point>>();
        java.util.List<Point> path = new java.util.ArrayList<Point>();
        Point p = new Point(10f, 0f);
        p.setShape(Point.INVISIBLE);
        p.setColor(Color.blue);
        p.setLineWidth(15f);
        path.add(p);
        p = new Point(10f, 30f);
        p.setShape(Point.INVISIBLE);
        path.add(p);
        chartData.add(path);

        chart.setData(chartData);
        float[] pos = chart.drawOn(page);
        pdf.complete();

        // drawOn returns {x1 + w, y1 + h} = {70 + 400, 50 + 250}
        assertEquals(470f, pos[0], 0.001f);
        assertEquals(300f, pos[1], 0.001f);
    }

}
