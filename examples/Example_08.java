import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_08.java
 *
 */
public class Example_08 {

    public Example_08() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_08.pdf")), Compliance.PDF_A_1B);

        Page page = new Page(pdf, Letter.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        f1.setSize(7f);

        Font f2 = new Font(pdf, CoreFont.HELVETICA);
        f2.setSize(7f);

        Font f3 = new Font(pdf, CoreFont.HELVETICA_BOLD_OBLIQUE);
        f3.setSize(7f);

        Image image1 = new Image(
                pdf,
                getClass().getResourceAsStream("images/fruit.jpg"),
                ImageType.JPG);
        image1.scaleBy(0.25f);

        Table table = new Table();
        List<List<Cell>> tableData = getData(
        		"data/world-communications.txt", "|", Table.DATA_HAS_2_HEADER_ROWS, f1, f2);
        tableData.get(2).get(3).setImage(image1);
        tableData.get(2).get(2).setURIAction("http://pdfjet.com");

        // tableData.get(2).get(3).setVerTextAlignment(Align.CENTER);

        table.setData(tableData, Table.DATA_HAS_2_HEADER_ROWS);
        // table.setCellBordersWidth(1.2f);
        table.setCellBordersWidth(0.2f);
        table.setLocation(70f, 30f);
        table.setTextColorInRow(6, Color.blue);
        table.setTextColorInRow(39, Color.red);
        table.setFontInRow(26, f3);
        table.removeLineBetweenRows(0, 1);  
        table.autoAdjustColumnWidths();
        table.setColumnWidth(0, 120f);
        table.rightAlignNumbers();
        table.wrapAroundCellText();

        int numOfPages = table.getNumberOfPages(page);
        while (true) {
            Point point = table.drawOn(page);
            // TO DO: Draw "Page 1 of N" here
            if (!table.hasMoreData()) {
                // Allow the table to be drawn again later:
                table.resetRenderedPagesCount();
                break;
            }
            page = new Page(pdf, Letter.PORTRAIT);
            table.setLocation(70f, 30f);
        }

        pdf.close();
    }
    
    
    public List<List<Cell>> getData(
            String fileName,
            String delimiter,
            int numOfHeaderRows,
            Font f1,
            Font f2) throws Exception {

        List<List<Cell>> tableData = new ArrayList<List<Cell>>();

        int currentRow = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = reader.readLine()) != null) {
            List<Cell> row = new ArrayList<Cell>();
            String[] cols = null;
            if (delimiter.equals("|")) {
                cols = line.split("\\|", -1);
            }
            else if (delimiter.equals("\t")) {
                cols = line.split("\t", -1);
            }
            else {
                throw new Exception(
                		"Only pipes and tabs can be used as delimiters");
            }
            for (int i = 0; i < cols.length; i++) {
                String text = cols[i].trim();
                Cell cell = null;
                if (currentRow < numOfHeaderRows) {
                    cell = new Cell(f1, text);
                }
                else {
                    cell = new Cell(f2, text);
                }
                cell.setTopPadding(2f);
                cell.setBottomPadding(2f);
                cell.setLeftPadding(2f);
                cell.setRightPadding(2f);
                row.add(cell);
            }
            tableData.add(row);
            currentRow++;
        }
        reader.close();

        appendMissingCells(tableData, f2);
        
        return tableData;
    }
    

    private void appendMissingCells(List<List<Cell>> tableData, Font f2) {
        List<Cell> firstRow = tableData.get(0);
        int numOfColumns = firstRow.size();
        for (int i = 0; i < tableData.size(); i++) {
            List<Cell> dataRow = tableData.get(i);
            int dataRowColumns = dataRow.size();
            if (dataRowColumns < numOfColumns) {
                for (int j = 0; j < (numOfColumns - dataRowColumns); j++) {
                    dataRow.add(new Cell(f2));
                }
                dataRow.get(dataRowColumns - 1).setColSpan((numOfColumns - dataRowColumns) + 1);
            }
        }
    }


    public static void main(String[] args) throws Exception {
        long time0 = System.currentTimeMillis();
        new Example_08();
        long time1 = System.currentTimeMillis();
        if (args.length > 0 && args[0].equals("time")) {
            System.out.println(time1 - time0);
        }
    }

}   // End of Example_08.java
