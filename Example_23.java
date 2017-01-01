import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_23.java
 *
 */
public class Example_23 {

    public Example_23() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_23.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(7f);

        Font f2 = new Font(pdf, CoreFont.HELVETICA);
        f2.setSize(7f);

        List<List<Cell>> tableData = new ArrayList<List<Cell>>();

        List<Cell> row = new ArrayList<Cell>();
        Cell cell = new Cell(f1, "Hello");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "World");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "Next Column");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        cell = new Cell(f1, "Last Column");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);

        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "This is a test:");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        cell = new Cell(f2,
"Here we are going\nto test\nthe wrapCellText method. We will create a table and place it near the bottom of the page. When we draw this table the text will wrap around the column edge and stay within the column. All the white space characters will be stripped and replaced with a single space between the words. So - let's  see how this is working: If you are using this functionality to draw text - make the border invisible. This white space should be replaced with a single space.");
        cell.setColSpan(2);
        // cell.setColSpan(3);
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        // cell.setTextAlignment(Align.RIGHT);
        // cell.setTextAlignment(Align.CENTER);
        row.add(cell);
        row.add(new Cell(f2, "Test 123"));
        cell = new Cell(f2, "Test 456");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        cell = new Cell(f2, "Another row. Make sure that this line of text will be wrappped around correctly too.");
        row.add(cell);
        row.add(new Cell(f2, "Yahoo!"));
        row.add(new Cell(f2, "Test 789"));
        row.add(new Cell(f2, "Test 000"));
        tableData.add(row);

        Table table = new Table();
        table.setData(tableData, Table.DATA_HAS_1_HEADER_ROWS);
        
        table.setLocation(70f, 730f);
        table.setColumnWidth(0, 100f);
        table.setColumnWidth(1, 100f);
        table.wrapAroundCellText();
        // table.setNoCellBorders();

        int numOfPages = table.getNumberOfPages(page);
        while (true) {
            table.drawOn(page);
            if (!table.hasMoreData()) break;
            page = new Page(pdf, Letter.PORTRAIT);
            table.setLocation(70f, 30f);
        }

        pdf.close();
    }
    

    public static void main(String[] args) {
        try {
            new Example_23();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}   // End of Example_23.java
