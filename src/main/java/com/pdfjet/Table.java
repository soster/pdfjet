/**
 *  Table.java
 *
Copyright 2023 Innovatics Inc.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.pdfjet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Used to create table objects and draw them on a page.
 *
 * Please see Example_08.
 */
public class Table {
    public static final int WITH_0_HEADER_ROWS = 0;
    public static final int WITH_1_HEADER_ROW  = 1;
    public static final int WITH_2_HEADER_ROWS = 2;
    public static final int WITH_3_HEADER_ROWS = 3;
    public static final int WITH_4_HEADER_ROWS = 4;
    public static final int WITH_5_HEADER_ROWS = 5;
    public static final int WITH_6_HEADER_ROWS = 6;
    public static final int WITH_7_HEADER_ROWS = 7;
    public static final int WITH_8_HEADER_ROWS = 8;
    public static final int WITH_9_HEADER_ROWS = 9;

    private List<List<Cell>> tableData;
    private int numOfHeaderRows = 1;
    private int rendered = 0;
    private float x1;
    private float y1;
    private float y1FirstPage;
    private float bottomMargin;

    /**
     * Create a table object.
     *
     */
    public Table() {
        tableData = new ArrayList<List<Cell>>();
    }

    /**
     * Create a table object.
     *
     */
    public Table(Font f1, Font f2, String fileName) {
        tableData = new ArrayList<List<Cell>>();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(fileName));
            String delimiterRegex = null;
            int numberOfFields = 0;
            int lineNumber = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineNumber == 0) {
                    delimiterRegex = getDelimiterRegex(line);
                    numberOfFields = line.split(delimiterRegex).length;
                }
                List<Cell> row = new ArrayList<Cell>();
                String[] fields = line.split(delimiterRegex);
                for (String field : fields) {
                    if (lineNumber == 0) {
                        Cell cell = new Cell(f1);
                        cell.setTextBox(new TextBox(f1, field));
                        row.add(cell);
                    } else {
                        row.add(new Cell(f2, field));
                    }
                }
                if (row.size() > numberOfFields) {
                    List<Cell> row2 = new ArrayList<Cell>();
                    for (int i = 0; i < numberOfFields; i++) {
                        row2.add(row.get(i));
                    }
                    tableData.add(row2);
                } else if (row.size() < numberOfFields) {
                    int diff = numberOfFields - row.size();
                    for (int i = 0; i < diff; i++) {
                        row.add(new Cell(f2));
                    }
                    tableData.add(row);
                } else {
                    tableData.add(row);
                }
                lineNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    /**
     * Sets the position (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

    /**
     * Sets the position (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }

    /**
     * Sets the location (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
    }

    /**
     * Sets the location (x, y) of the top left corner of this table on the page.
     *
     * @param x the x coordinate of the top left point of the table.
     * @param y the y coordinate of the top left point of the table.
     */
    public void setLocation(double x, double y) {
        setLocation((float) x, (float) y);
    }

    /**
     * Sets the bottom margin for this table.
     *
     * @param bottomMargin the margin.
     */
    public void setBottomMargin(double bottomMargin) {
        this.bottomMargin = (float) bottomMargin;
    }

    /**
     * Sets the bottom margin for this table.
     *
     * @param bottomMargin the margin.
     */
    public void setBottomMargin(float bottomMargin) {
        this.bottomMargin = bottomMargin;
    }

    /**
     * Sets the table data.
     *
     * The table data is a perfect grid of cells.
     * All cell should be an unique object and you can not reuse blank cell objects.
     * Even if one or more cells have colspan bigger than zero the number of cells
     * in the row will not change.
     *
     * @param tableData the table data.
     */
    public void setData(List<List<Cell>> tableData) {
        this.tableData = tableData;
        this.numOfHeaderRows = 0;
        this.rendered = 0;
        addCellsToCompleteTheGrid(tableData);
    }

    /**
     * Sets the table data and specifies the number of header rows in this data.
     *
     * @param tableData       the table data.
     * @param numOfHeaderRows the number of header rows in this data.
     */
    public void setData(List<List<Cell>> tableData, int numOfHeaderRows) {
        this.tableData = tableData;
        this.numOfHeaderRows = numOfHeaderRows;
        this.rendered = numOfHeaderRows;
        addCellsToCompleteTheGrid(tableData);
    }

    private void addCellsToCompleteTheGrid(List<List<Cell>> tableData) {
        // Add the missing cells.
        int numOfColumns = tableData.get(0).size();
        Font font = tableData.get(0).get(0).font;
        for (List<Cell> row : tableData) {
            int diff = numOfColumns - row.size();
            for (int i = 0; i < diff; i++) {
                row.add(new Cell(font, ""));
            }
        }
    }

    /**
     * Sets the alignment of the numbers to the right.
     */
    public void rightAlignNumbers() {
        StringBuilder buf = new StringBuilder();
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                if (cell.text != null) {
                    buf.setLength(0);
                    String str = cell.text;
                    if (str.startsWith("(") && str.endsWith(")")) {
                        str = str.substring(1, str.length() - 1);
                    }
                    for (int i = 0; i < str.length(); i++) {
                        char ch = str.charAt(i);
                        if (ch != '.' && ch != ',' && ch != '\'') {
                            buf.append(ch);
                        }
                    }
                    try {
                        Double.parseDouble(buf.toString());
                        cell.setTextAlignment(Align.RIGHT);
                    } catch (NumberFormatException nfe) {
                    }
                }
            }
        }
    }

    /**
     * Removes the horizontal lines between the rows from index1 to index2.
     *
     * @param index1 the index of the first specified row.
     * @param index2 the index of the second specified row.
     */
    public void removeLineBetweenRows(int index1, int index2) {
        for (int i = index1; i < index2; i++) {
            List<Cell> row = tableData.get(i);
            for (Cell cell : row) {
                cell.setBorder(Border.BOTTOM, false);
            }
            row = tableData.get(i + 1);
            for (Cell cell : row) {
                cell.setBorder(Border.TOP, false);
            }
        }
    }

    /**
     * Sets the text alignment in the specified column.
     *
     * @param index     the index of the specified column.
     * @param alignment the specified alignment. Supported values: Align.LEFT,
     *                  Align.RIGHT, Align.CENTER and Align.JUSTIFY.
     */
    public void setTextAlignInColumn(int index, int alignment) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setTextAlignment(alignment);
                if (cell.textBox != null) {
                    cell.textBox.setTextAlignment(alignment);
                }
            }
        }
    }

    /**
     * Sets the color of the text in the specified column.
     *
     * @param index the index of the specified column.
     * @param color the color specified as an integer.
     */
    public void setTextColorInColumn(int index, int color) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setBrushColor(color);
                if (cell.textBox != null) {
                    cell.textBox.setBrushColor(color);
                }
            }
        }
    }

    /**
     * Sets the font for the specified column.
     *
     * @param index the column index.
     * @param font  the font.
     */
    public void setFontInColumn(int index, Font font) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.font = font;
                if (cell.textBox != null) {
                    cell.textBox.font = font;
                }
            }
        }
    }

    /**
     * Sets the color of the text in the specified row.
     *
     * @param index the index of the specified row.
     * @param color the color specified as an integer.
     */
    public void setTextColorInRow(int index, int color) {
        List<Cell> row = tableData.get(index);
        for (Cell cell : row) {
            cell.setBrushColor(color);
            if (cell.textBox != null) {
                cell.textBox.setBrushColor(color);
            }
        }
    }

    /**
     * Sets the font for the specified row.
     *
     * @param index the row index.
     * @param font  the font.
     */
    public void setFontInRow(int index, Font font) {
        List<Cell> row = tableData.get(index);
        for (Cell cell : row) {
            cell.font = font;
            if (cell.textBox != null) {
                cell.textBox.font = font;
            }
        }
    }

    /**
     * Sets the width of the column with the specified index.
     *
     * @param index the index of specified column.
     * @param width the specified width.
     */
    public void setColumnWidth(int index, float width) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setWidth(width);
                if (cell.textBox != null) {
                    cell.textBox.setWidth(width - (cell.leftPadding + cell.rightPadding));
                }
            }
        }
    }

    /**
     * Returns the column width of the column at the specified index.
     *
     * @param index the index of the column.
     * @return the width of the column.
     */
    public float getColumnWidth(int index) {
        return getCellAtRowColumn(0, index).getWidth();
    }

    /**
     * Returns the cell at the specified row and column.
     *
     * @param row the specified row.
     * @param col the specified column.
     *
     * @return the cell at the specified row and column.
     */
    public Cell getCellAt(int row, int col) {
        if (row >= 0) {
            return tableData.get(row).get(col);
        }
        return tableData.get(tableData.size() + row).get(col);
    }

    /**
     * Returns the cell at the specified row and column.
     *
     * @param row the specified row.
     * @param col the specified column.
     *
     * @return the cell at the specified row and column.
     */
    public Cell getCellAtRowColumn(int row, int col) {
        return getCellAt(row, col);
    }

    /**
     * Returns a list of cells for the specified row.
     *
     * @param index the index of the specified row.
     *
     * @return the list of cells.
     */
    public List<Cell> getRow(int index) {
        return tableData.get(index);
    }

    public List<Cell> getRowAtIndex(int index) {
        return getRow(index);
    }

    /**
     * Returns a list of cells for the specified column.
     *
     * @param index the index of the specified column.
     *
     * @return the list of cells.
     */
    public List<Cell> getColumn(int index) {
        List<Cell> column = new ArrayList<Cell>();
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                column.add(row.get(index));
            }
        }
        return column;
    }

    public List<Cell> getColumnAtIndex(int index) {
        return getColumn(index);
    }

    /**
     * Draws this table on the specified page.
     *
     * @param page the page to draw this table on.
     *
     * @return Point the point on the page where to draw the next component.
     * @throws Exception If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        wrapAroundCellText();
        setRightBorderOnLastColumn();
        setBottomBorderOnLastRow();
        return drawTableRows(page, drawHeaderRows(page, 0));
    }

    public float[] drawOn(PDF pdf, List<Page> pages, float[] pageSize) throws Exception {
        wrapAroundCellText();
        setRightBorderOnLastColumn();
        setBottomBorderOnLastRow();
        float[] xy = null;
        int pageNumber = 1;
        while (hasMoreData()) {
            Page page = new Page(pdf, pageSize, Page.DETACHED);
            pages.add(page);
            xy = drawTableRows(page, drawHeaderRows(page, pageNumber));
            pageNumber++;
        }
        return xy;
    }

    private float[] drawHeaderRows(Page page, int pageNumber) throws Exception {
        float x = x1;
        float y = (pageNumber == 1) ? y1FirstPage : y1;
        for (int i = 0; i < numOfHeaderRows; i++) {
            List<Cell> row = tableData.get(i);
            float h = getMaxCellHeight(row);
            for (int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                float w = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int k = 1; k < colspan; k++) {
                    j++;
                    w += row.get(j).getWidth();
                }
                if (page != null) {
                    page.setBrushColor(cell.getBrushColor());
                    if (i == (numOfHeaderRows - 1)) {
                        cell.setBorder(Border.BOTTOM, true);
                    }
                    cell.drawOn(page, x, y, w, h);
                }
                x += w;
            }
            x = x1;
            y += h;
            rendered++;
        }
        return new float[] {x, y};
    }

    private float[] drawTableRows(Page page, float[] xy) throws Exception {
        float x = xy[0];
        float y = xy[1];
        while (rendered < tableData.size()) {
            List<Cell> row = tableData.get(rendered);
            float h = getMaxCellHeight(row);
            if (page != null && (y + h) > (page.height - bottomMargin)) {
                return new float[] {x, y};
            }
            for (int i = 0; i < row.size(); i++) {
                Cell cell = row.get(i);
                float w = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int j = 1; j < colspan; j++) {
                    i++;
                    w += row.get(i).getWidth();
                }
                if (page != null) {
                    page.setBrushColor(cell.getBrushColor());
                    cell.drawOn(page, x, y, w, h);
                }
                x += w;
            }
            x = x1;
            y += h;
            rendered++;
        }
        rendered = -1; // We are done!
        return new float[] {x, y};
    }

    private float getMaxCellHeight(List<Cell> row) throws Exception {
        float maxCellHeight = 0f;
        for (int i = 0; i < row.size(); i++) {
            Cell cell = row.get(i);
            float totalWidth = getTotalWidth(row, i);
            float cellHeight = cell.getHeight(totalWidth);
            if (cellHeight > maxCellHeight) {
                maxCellHeight = cellHeight;
            }
        }
        return maxCellHeight;
    }

    /**
     * Returns true if the table contains more data that needs to be drawn on a
     * page.
     *
     * @return whether the table has more data to be drawn on a page.
     */
    private boolean hasMoreData() {
        return rendered != -1;
    }

    /**
     * Returns the width of this table when drawn on a page.
     *
     * @return the width of this table.
     */
    public float getWidth() {
        float tableWidth = 0f;
        List<Cell> row = tableData.get(0);
        for (Cell cell : row) {
            tableWidth += cell.getWidth();
        }
        return tableWidth;
    }

    /**
     * Sets all table cells borders.
     * @param borders true or false.
     */
    public void setCellBorders(boolean borders) {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setBorders(borders);
            }
        }
    }

    /**
     * Sets the color of the cell border lines.
     *
     * @param color the color of the cell border lines.
     */
    public void setCellBordersColor(int color) {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setPenColor(color);
            }
        }
    }

    /**
     * Sets the width of the cell border lines.
     *
     * @param width the width of the border lines.
     */
    public void setCellBordersWidth(float width) {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setLineWidth(width);
            }
        }
    }

    public void setFirstPageTopMargin(float topMargin) {
        this.y1FirstPage = y1 + topMargin;
    }

    // Sets the right border on all cells in the last column.
    private void setRightBorderOnLastColumn() {
        for (List<Cell> row : tableData) {
            Cell cell = null;
            int i = 0;
            while (i < row.size()) {
                cell = row.get(i);
                i += cell.getColSpan();
            }
            cell.setBorder(Border.RIGHT, true);
        }
    }

    // Sets the bottom border on all cells in the last row.
    private void setBottomBorderOnLastRow() {
        List<Cell> lastRow = tableData.get(tableData.size() - 1);
        for (Cell cell : lastRow) {
            cell.setBorder(Border.BOTTOM, true);
        }
    }

    /**
     * Auto adjusts the widths of all columns so that they are just wide enough to
     * hold the text without truncation.
     */
    public void setColumnWidths() {
        float[] maxColWidths = new float[tableData.get(0).size()];
        for (List<Cell> row : tableData) {
            for (int i = 0; i < row.size(); i++) {
                Cell cell = row.get(i);
                if (cell.getColSpan() == 1) {
                    if (cell.textBox != null) {
                        String[] tokens = cell.textBox.text.split("\\s+");
                        for (String token : tokens) {
                            float tokenWidth = cell.textBox.font.stringWidth(cell.textBox.fallbackFont, token);
                            tokenWidth += cell.leftPadding + cell.rightPadding;
                            if (tokenWidth > maxColWidths[i]) {
                                maxColWidths[i] = tokenWidth;
                            }
                        }
                    } else if (cell.image != null) {
                        float imageWidth = cell.image.getWidth() + cell.leftPadding + cell.rightPadding;
                        if (imageWidth > maxColWidths[i]) {
                            maxColWidths[i] = imageWidth;
                        }
                    } else if (cell.barcode != null) {
                        try {
                            float barcodeWidth = cell.barcode.drawOn(null)[0] + cell.leftPadding + cell.rightPadding;
                            if (barcodeWidth > maxColWidths[i]) {
                                maxColWidths[i] = barcodeWidth;
                            }
                        } catch (Exception e) {
                        }
                    } else if (cell.text != null) {
                        float textWidth = cell.font.stringWidth(cell.fallbackFont, cell.text);
                        textWidth += cell.leftPadding + cell.rightPadding;
                        if (textWidth > maxColWidths[i]) {
                            maxColWidths[i] = textWidth;
                        }
                    }
                }
            }
        }
        for (List<Cell> row : tableData) {
            for (int i = 0; i < row.size(); i++) {
                row.get(i).setWidth(maxColWidths[i]);
            }
        }
    }

    private List<List<Cell>> addExtraTableRows() {
        List<List<Cell>> tableData2 = new ArrayList<List<Cell>>();
        for (List<Cell> row : tableData) {
            tableData2.add(row);    // Add the original row
            int maxNumVerCells = 0;
            for (int i = 0; i < row.size(); i++) {
                int numVerCells = getNumVerCells(row, i);
                if (numVerCells > maxNumVerCells) {
                    maxNumVerCells = numVerCells;
                }
            }
            for (int i = 1; i < maxNumVerCells; i++) {
                List<Cell> row2 = new ArrayList<Cell>();
                for (Cell cell : row) {
                    Cell cell2 = new Cell(cell.getFont());
                    cell2.setFallbackFont(cell.getFallbackFont());
                    cell2.setWidth(cell.getWidth());
                    cell2.setLeftPadding(cell.leftPadding);
                    cell2.setRightPadding(cell.rightPadding);
                    cell2.setLineWidth(cell.lineWidth);
                    cell2.setBgColor(cell.getBgColor());
                    cell2.setPenColor(cell.getPenColor());
                    cell2.setBrushColor(cell.getBrushColor());
                    cell2.setProperties(cell.getProperties());
                    cell2.setVerTextAlignment(cell.getVerTextAlignment());
                    cell2.setTopPadding(0f);
                    cell2.setBorder(Border.TOP, false);
                    row2.add(cell2);
                }
                tableData2.add(row2);
            }
        }
        return tableData2;
    }

    private float getTotalWidth(List<Cell> row, int index) {
        Cell cell = row.get(index);
        int colspan = cell.getColSpan();
        float cellWidth = 0f;
        for (int i = 0; i < colspan; i++) {
            cellWidth += row.get(index + i).getWidth();
        }
        cellWidth -= (cell.leftPadding + row.get(index + (colspan - 1)).rightPadding);
        return cellWidth;
    }

    /**
     * Wraps around the text in all cells so it fits the column width.
     * This method should be called after all calls to setColumnWidth and
     * autoAdjustColumnWidths.
     */
    protected void wrapAroundCellText() {
        List<List<Cell>> tableData2 = addExtraTableRows();
        for (int i = 0; i < tableData2.size(); i++) {
            List<Cell> row = tableData2.get(i);
            for (int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                if (cell.text != null) {
                    float cellWidth = getTotalWidth(row, j);
                    String[] tokens = cell.text.split("\\s+");
                    int n = 0;
                    StringBuilder buf = new StringBuilder();
                    for (String token : tokens) {
                        if (cell.font.stringWidth(cell.fallbackFont, token) > cellWidth) {
                            if (buf.length() > 0) {
                                buf.append(" ");
                            }
                            for (int k = 0; k < token.length(); k++) {
                                if (cell.font.stringWidth(cell.fallbackFont, buf.toString() + token.charAt(k)) > cellWidth) {
                                    tableData2.get(i + n).get(j).setText(buf.toString());
                                    buf.setLength(0);
                                    n++;
                                }
                                buf.append(token.charAt(k));
                            }
                        } else {
                            if (cell.font.stringWidth(cell.fallbackFont, (buf.toString() + " " + token).trim()) > cellWidth) {
                                tableData2.get(i + n).get(j).setText(buf.toString().trim());
                                buf.setLength(0);
                                buf.append(token);
                                n++;
                            } else {
                                if (buf.length() > 0) {
                                    buf.append(" ");
                                }
                                buf.append(token);
                            }
                        }
                    }
                    tableData2.get(i + n).get(j).setText(buf.toString().trim());
                }
            }
        }
        tableData = tableData2;
    }

    /**
     *  Use this method to find out how many vertically stacked cell are needed after call to wrapAroundCellText.
     *
     *  @return the number of vertical cells needed to wrap around the cell text.
     */
    public int getNumVerCells(List<Cell> row, int index) {
        Cell cell = row.get(index);
        int numOfVerCells = 1;
        if (cell.text == null) {
            return numOfVerCells;
        }
        float cellWidth = getTotalWidth(row, index);
        String[] tokens = cell.text.split("\\s+");
        StringBuilder buf = new StringBuilder();
        for (String token : tokens) {
            if (cell.font.stringWidth(cell.fallbackFont, token) > cellWidth) {
                if (buf.length() > 0) {
                    buf.append(" ");
                }
                for (int k = 0; k < token.length(); k++) {
                    if (cell.font.stringWidth(cell.fallbackFont, (buf.toString() + " " + token.charAt(k)).trim()) > cellWidth) {
                        numOfVerCells++;
                        buf.setLength(0);
                    }
                    buf.append(token.charAt(k));
                }
            } else {
                if (cell.font.stringWidth(cell.fallbackFont, (buf.toString() + " " + token).trim()) > cellWidth) {
                    numOfVerCells++;
                    buf.setLength(0);
                    buf.append(token);
                } else {
                    if (buf.length() > 0) {
                        buf.append(" ");
                    }
                    buf.append(token);
                }
            }
        }
        return numOfVerCells;
    }

    private String getDelimiterRegex(String str) {
        int comma = 0;
        int pipe = 0;
        int tab = 0;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (ch == ',') {
                comma++;
            } else if (ch == '|') {
                pipe++;
            } else if (ch == '\t') {
                tab++;
            }
        }
        if (comma >= pipe) {
            if (comma >= tab) {
                return ",";
            }
            return "\t";
        } else {
            if (pipe >= tab) {
                return "\\|";
            }
            return "\t";
        }
    }
} // End of Table.java
