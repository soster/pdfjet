import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_53.java
 *
 */
public class Example_53 {

    public Example_53() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_53.pdf")));

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

        cell = new Cell(f1, "CompositeTextLine");
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
                "Here we are going to test the wrapCellText method.\tWe will create a table and place it near the bottom of the page. When we draw this table the text will wrap around the column edge and stay within the column.\nAll the white space characters will be stripped and replaced with a single space between the words.\tSo - let's  see how this is working:\nIf you are using this functionality to draw text - make the border invisible.");
        cell.setColSpan(2);
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        row.add(new Cell(f2));  // We need an empty cell here because the previous cell had colSpan == 2
        cell = new Cell(f2, "Test 456");
        cell.setTopPadding(5f);
        cell.setBottomPadding(5f);
        row.add(cell);
        tableData.add(row);

        row = new ArrayList<Cell>();
        row.add(new Cell(f2,
                "Another row.\n\n\nMake sure that this line of text will be wrapped around correctly too."));
        row.add(new Cell(f2, "Yahoo!"));
        row.add(new Cell(f2, "Test 789"));

        // start Rossen's composite line test
        CompositeTextLine composite = new CompositeTextLine(0f, 0f);
        TextLine line1 = new TextLine(f1, "Composite Text Line");
        TextLine line2 = new TextLine(f1, "Superscript");
        TextLine line3 = new TextLine(f1, "Subscript");
        line2.setTextEffect(Effect.SUPERSCRIPT);
        line3.setTextEffect(Effect.SUBSCRIPT);
        composite.addComponent(line1);
        composite.addComponent(line2);
        composite.addComponent(line3);

        cell = new Cell(f2);
        cell.setCompositeTextLine(composite);
        cell.setBgColor(Color.peachpuff);
        row.add(cell);
        // end Rossen's composite line test

        tableData.add(row);

        Table table = new Table();
        table.setData(tableData, Table.DATA_HAS_1_HEADER_ROWS);
        table.setLocation(70f, 730f);
        table.setColumnWidth(0, 100f);
        table.setColumnWidth(1, 100f);
        table.setColumnWidth(2, 100f);
        table.setColumnWidth(3, 150f);
        table.wrapAroundCellText();
        // table.wrapAroundCellText2();

        int numOfPages = table.getNumberOfPages(page);
        while (true) {
            table.drawOn(page);
            if (!table.hasMoreData()) break;
            page = new Page(pdf, Letter.PORTRAIT);
            table.setLocation(70f, 30f);
        }

        pdf.close();
    }


    public static void main(String[] args) throws Exception {
        new Example_53();
    }

}   // End of Example_53.java
