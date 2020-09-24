import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_15.java
 *
 */
public class Example_15 {

    public Example_15() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_15.pdf")));

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA);
        f1.setSize(7f);
        f2.setSize(7f);

        Font f3 = new Font(pdf, CoreFont.HELVETICA);
        Font f4 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f5 = new Font(pdf, CoreFont.HELVETICA);

        Page page = new Page(pdf, A4.PORTRAIT);

        Table table = new Table();

        // REPLACED:
        // table.setCellMargin(10f);

        List<List<Cell>> tableData = new ArrayList<List<Cell>>();

        List<Cell> row = null;
        Cell cell = null;

        for (int i = 0; i < 60; i++) {
            row = new ArrayList<Cell>();
            for (int j = 0; j < 5; j++) {
                if (i == 0) {
                    cell = new Cell(f1);
                }
                else {
                    cell = new Cell(f2);
                }
                //  cell.setNoBorders();

                // WITH:
                cell.setTopPadding(10f);
                cell.setBottomPadding(10f);
                cell.setLeftPadding(10f);
                cell.setRightPadding(10f);

                cell.setText("Hello " + i + " " + j);

                CompositeTextLine composite = new CompositeTextLine(0f, 0f);
                composite.setFontSize(7f);
                TextLine line1 = new TextLine(f3, "H");
                TextLine line2 = new TextLine(f4, "2");
                TextLine line3 = new TextLine(f5, "O");
                line2.setTextEffect(Effect.SUBSCRIPT);
                composite.addComponent(line1);
                composite.addComponent(line2);
                composite.addComponent(line3);

                if (i == 0 || j == 0) {
                    cell.setCompositeTextLine(composite);
                    cell.setBgColor(Color.deepskyblue);
                }
                else {
                    cell.setBgColor(Color.dodgerblue);
                }
                cell.setPenColor(Color.lightgray);
                cell.setBrushColor(Color.black);
                row.add(cell);
            }
            tableData.add(row);
        }

        table.setData(tableData, Table.DATA_HAS_2_HEADER_ROWS);

        table.setCellBordersWidth(0.2f);
        table.setLocation(70f, 30f);
        table.autoAdjustColumnWidths();

        while (true) {
            Point point = table.drawOn(page);
            TextLine text = new TextLine(f1, "Hello, World.");
            text.setLocation(point.getX() + table.getWidth(), point.getY());
            text.drawOn(page);

            if (!table.hasMoreData()) {
                break;
            }
            page = new Page(pdf, A4.PORTRAIT);
        }

        pdf.close();
    }


    public static void main(String[] args) {
        try {
            new Example_15();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_15.java
