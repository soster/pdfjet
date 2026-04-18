package com.pdfjet;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class TableTest extends PDFTestBase {

    @Test
    public void testTableCreationAndDrawing() throws Exception {
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
        table.drawOn(page);

        pdf.complete();
        assertTrue(os.toByteArray().length > 0);
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

}
