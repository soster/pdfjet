package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class TableTest extends PDFTestBase {

    @Test
    public void testTableDrawOnReturnsPositionBelowTable() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        PDF pdf = new PDF(os);
        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(7f);
        f2.setSize(7f);
        Page page = new Page(pdf, A4.PORTRAIT);

        Table table = new Table();
        List<List<Cell>> tableData = new ArrayList<List<Cell>>();
        for (int i = 0; i < 5; i++) {
            List<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < 3; j++) {
                Cell cell = new Cell(i == 0 ? f1 : f2);
                cell.setTopPadding(5f);
                cell.setBottomPadding(5f);
                cell.setLeftPadding(5f);
                cell.setRightPadding(5f);
                cell.setText("R" + i + "C" + j);
                if (i == 0) cell.setBgColor(Color.deepskyblue);
                row.add(cell);
            }
            tableData.add(row);
        }
        table.setData(tableData, 1);
        table.setCellBordersWidth(0.2f);
        table.setLocation(50f, 30f);
        float[] pos = table.drawOn(page);
        pdf.complete();

        assertTrue("Table width must be positive", table.getWidth() > 0f);
        // Y cursor must be below the table start y=30
        assertTrue("Y cursor advanced past table start y=30", pos[1] > 30f);
    }

    @Test
    public void testTableGetCellAt() throws Exception {
        PDF pdf = writablePDF();
        Font font = new Font(pdf, CoreFont.HELVETICA);

        Table table = new Table();
        List<List<Cell>> tableData = new ArrayList<List<Cell>>();
        for (int i = 0; i < 3; i++) {
            List<Cell> row = new ArrayList<Cell>();
            for (int j = 0; j < 3; j++) {
                row.add(new Cell(font, "Cell " + i + "," + j));
            }
            tableData.add(row);
        }
        table.setData(tableData);

        Cell cell = table.getCellAt(1, 2);
        assertNotNull(cell);
        assertEquals("Cell 1,2", cell.getText());
    }

    @Test
    public void testTableWithMoreRowsProducesLargerPDF() throws Exception {
        ByteArrayOutputStream fewRows = new ByteArrayOutputStream();
        PDF pdf1 = new PDF(fewRows);
        Font font1 = new Font(pdf1, CoreFont.HELVETICA);
        Page page1 = new Page(pdf1, A4.PORTRAIT);
        Table t1 = new Table();
        List<List<Cell>> data1 = new ArrayList<List<Cell>>();
        for (int i = 0; i < 3; i++) {
            List<Cell> row = new ArrayList<Cell>();
            row.add(new Cell(font1, "Row " + i));
            data1.add(row);
        }
        t1.setData(data1);
        t1.setLocation(50f, 30f);
        t1.drawOn(page1);
        pdf1.complete();

        ByteArrayOutputStream manyRows = new ByteArrayOutputStream();
        PDF pdf2 = new PDF(manyRows);
        Font font2 = new Font(pdf2, CoreFont.HELVETICA);
        Page page2 = new Page(pdf2, A4.PORTRAIT);
        Table t2 = new Table();
        List<List<Cell>> data2 = new ArrayList<List<Cell>>();
        for (int i = 0; i < 30; i++) {
            List<Cell> row = new ArrayList<Cell>();
            row.add(new Cell(font2, "Row " + i));
            data2.add(row);
        }
        t2.setData(data2);
        t2.setLocation(50f, 30f);
        t2.drawOn(page2);
        pdf2.complete();

        assertTrue("More rows produce larger PDF",
                manyRows.toByteArray().length > fewRows.toByteArray().length);
    }

}
