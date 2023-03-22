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

import java.util.*;


/**
 *  Used to create table objects and draw them on a page.
 *
 *  Please see Example_08.
 */
public class Table {

    public static final int DATA_HAS_0_HEADER_ROWS = 0;
    public static final int DATA_HAS_1_HEADER_ROWS = 1;
    public static final int DATA_HAS_2_HEADER_ROWS = 2;
    public static final int DATA_HAS_3_HEADER_ROWS = 3;
    public static final int DATA_HAS_4_HEADER_ROWS = 4;
    public static final int DATA_HAS_5_HEADER_ROWS = 5;
    public static final int DATA_HAS_6_HEADER_ROWS = 6;
    public static final int DATA_HAS_7_HEADER_ROWS = 7;
    public static final int DATA_HAS_8_HEADER_ROWS = 8;
    public static final int DATA_HAS_9_HEADER_ROWS = 9;

    private int rendered = 0;
    private int numOfPages;

    private List<List<Cell>> tableData;
    private int numOfHeaderRows = 0;

    private float x1;
    private float y1;
    private float y1FirstPage;
    private float rightMargin;
    private float bottomMargin = 30f;


    /**
     *  Create a table object.
     *
     */
    public Table() {
        tableData = new ArrayList<List<Cell>>();
    }

    /**
     *  Sets the position (x, y) of the top left corner of this table on the page.
     *
     *  @param x the x coordinate of the top left point of the table.
     *  @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }

     /**
     *  Sets the position (x, y) of the top left corner of this table on the page.
     *
     *  @param x the x coordinate of the top left point of the table.
     *  @param y the y coordinate of the top left point of the table.
     */
    public void setPosition(double x, double y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location (x, y) of the top left corner of this table on the page.
     *
     *  @param x the x coordinate of the top left point of the table.
     *  @param y the y coordinate of the top left point of the table.
     */
    public void setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
    }

    /**
     *  Sets the location (x, y) of the top left corner of this table on the page.
     *
     *  @param x the x coordinate of the top left point of the table.
     *  @param y the y coordinate of the top left point of the table.
     */
    public void setLocation(double x, double y) {
        setLocation((float) x, (float) y);
    }


    /**
     *  Sets the bottom margin for this table.
     *
     *  @param bottomMargin the margin.
     */
    public void setBottomMargin(double bottomMargin) {
        this.bottomMargin = (float) bottomMargin;
    }


    /**
     *  Sets the bottom margin for this table.
     *
     *  @param bottomMargin the margin.
     */
    public void setBottomMargin(float bottomMargin) {
        this.bottomMargin = bottomMargin;
    }


    /**
     *  Sets the table data.
     *
     *  The table data is a perfect grid of cells.
     *  All cell should be an unique object and you can not reuse blank cell objects.
     *  Even if one or more cells have colspan bigger than zero the number of cells in the row will not change.
     *
     *  @param tableData the table data.
     */
    public void setData(List<List<Cell>> tableData) {
        // TODO: Do we need this method?
        this.tableData = tableData;
        this.numOfHeaderRows = 0;
        this.rendered = 0;
        addCellsToCompleteTheGrid(tableData);
    }


    /**
     *  Sets the table data and specifies the number of header rows in this data.
     *
     *  @param tableData the table data.
     *  @param numOfHeaderRows the number of header rows in this data.
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
     *  Sets the alignment of the numbers to the right.
     */
    public void rightAlignNumbers() {
        for (int i = numOfHeaderRows; i < tableData.size(); i++) {
            List<Cell> row = tableData.get(i);
            for (Cell cell : row) {
                if (cell.text != null) {
                    String str = cell.text;
                    int len = str.length();
                    boolean isNumber = true;
                    int j = 0;
                    while (j < len) {
                        char ch = str.charAt(j++);
                        if (!Character.isDigit(ch)
                                && ch != '('
                                && ch != ')'
                                && ch != '-'
                                && ch != '.'
                                && ch != ','
                                && ch != '\'') {
                            isNumber = false;
                        }
                    }
                    if (isNumber) {
                        cell.setTextAlignment(Align.RIGHT);
                    }
                }
            }
        }
    }


    /**
     *  Removes the horizontal lines between the rows from index1 to index2.
     *  @param index1 the index of the first specified row.
     *  @param index2 the index of the second specified row.
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
     *  Sets the text alignment in the specified column.
     *
     *  @param index the index of the specified column.
     *  @param alignment the specified alignment. Supported values: Align.LEFT, Align.RIGHT, Align.CENTER and Align.JUSTIFY.
     */
    public void setTextAlignInColumn(int index, int alignment) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setTextAlignment(alignment);
                if (cell.textBlock != null) {
                    cell.textBlock.setTextAlignment(alignment);
                }
            }
        }
    }


    /**
     *  Sets the color of the text in the specified column.
     *
     *  @param index the index of the specified column.
     *  @param color the color specified as an integer.
     */
    public void setTextColorInColumn(int index, int color) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setBrushColor(color);
                if (cell.textBlock != null) {
                    cell.textBlock.setBrushColor(color);
                }
            }
        }
    }


    /**
     *  Sets the font for the specified column.
     *
     *  @param index the column index.
     *  @param font the font.
     */
    public void setFontInColumn(int index, Font font) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.font = font;
                if (cell.textBlock != null) {
                    cell.textBlock.font = font;
                }
            }
        }
    }


    /**
     *  Sets the color of the text in the specified row.
     *
     *  @param index the index of the specified row.
     *  @param color the color specified as an integer.
     */
    public void setTextColorInRow(int index, int color) {
        List<Cell> row = tableData.get(index);
        for (Cell cell : row) {
            cell.setBrushColor(color);
            if (cell.textBlock != null) {
                cell.textBlock.setBrushColor(color);
            }
        }
    }


    /**
     *  Sets the font for the specified row.
     *
     *  @param index the row index.
     *  @param font the font.
     */
    public void setFontInRow(int index, Font font) {
        List<Cell> row = tableData.get(index);
        for (Cell cell : row) {
            cell.font = font;
            if (cell.textBlock != null) {
                cell.textBlock.font = font;
            }
        }
    }


    /**
     *  Sets the width of the column with the specified index.
     *
     *  @param index the index of specified column.
     *  @param width the specified width.
     */
    public void setColumnWidth(int index, float width) {
        for (List<Cell> row : tableData) {
            if (index < row.size()) {
                Cell cell = row.get(index);
                cell.setWidth(width);
                if (cell.textBlock != null) {
                    cell.textBlock.setWidth(width - (cell.leftPadding + cell.rightPadding));
                }
            }
        }
    }


    /**
     *  Returns the column width of the column at the specified index.
     *
     *  @param index the index of the column.
     *  @return the width of the column.
     */
    public float getColumnWidth(int index) {
        return getCellAtRowColumn(0, index).getWidth();
    }


    /**
     *  Returns the cell at the specified row and column.
     *
     *  @param row the specified row.
     *  @param col the specified column.
     *
     *  @return the cell at the specified row and column.
     */
    public Cell getCellAt(int row, int col) {
        if (row >= 0) {
            return tableData.get(row).get(col);
        }
        return tableData.get(tableData.size() + row).get(col);
    }


    /**
     *  Returns the cell at the specified row and column.
     *
     *  @param row the specified row.
     *  @param col the specified column.
     *
     *  @return the cell at the specified row and column.
     */
    public Cell getCellAtRowColumn(int row, int col) {
        return getCellAt(row, col);
    }


    /**
     *  Returns a list of cells for the specified row.
     *
     *  @param index the index of the specified row.
     *
     *  @return the list of cells.
     */
    public List<Cell> getRow(int index) {
        return tableData.get(index);
    }


    public List<Cell> getRowAtIndex(int index) {
        return getRow(index);
    }


    /**
     *  Returns a list of cells for the specified column.
     *
     *  @param index the index of the specified column.
     *
     *  @return the list of cells.
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
     *  Returns the total number of pages that are required to draw this table on.
     *
     *  @param page the type of pages we are drawing this table on.
     *
     *  @return the number of pages.
     *  @throws Exception  If an input or output exception occurred
     */
    public int getNumberOfPages(Page page) throws Exception {
        numOfPages = 1;
        while (hasMoreData()) {
            drawOn(null);
        }
        resetRenderedPagesCount();
        return numOfPages;
    }


    /**
     *  Draws this table on the specified page.
     *
     *  @param page the page to draw this table on.
     *
     *  @return Point the point on the page where to draw the next component.
     *  @throws Exception  If an input or output exception occurred
     */
    public float[] drawOn(Page page) throws Exception {
        return drawTableRows(page, drawHeaderRows(page, 0));
    }


    public float[] drawOn(PDF pdf, List<Page> pages, float[] pageSize) throws Exception {
        float[] xy = null;
        int pageNumber = 1;
        while (this.hasMoreData()) {
            Page page = new Page(pdf, pageSize, false);
            pages.add(page);
            xy = drawTableRows(page, drawHeaderRows(page, pageNumber));
            pageNumber++;
        }
        // Allow the table to be drawn again later:
        resetRenderedPagesCount();
        return xy;
    }


    private float[] drawHeaderRows(Page page, int pageNumber) throws Exception {
        float x = x1;
        float y = (pageNumber == 1) ? y1FirstPage : y1;

        float cellH;
        for (int i = 0; i < numOfHeaderRows; i++) {
            List<Cell> dataRow = tableData.get(i);
            cellH = getMaxCellHeight(dataRow);
            for (int j = 0; j < dataRow.size(); j++) {
                Cell cell = dataRow.get(j);
                float cellW = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int k = 1; k < colspan; k++) {
                    cellW += dataRow.get(++j).width;
                }
                if (page != null) {
                    page.setBrushColor(cell.getBrushColor());
                    cell.paint(page, x, y, cellW, cellH);
                }
                x += cellW;
            }
            x = x1;
            y += cellH;
        }

        return new float[] {x, y};
    }


    private float[] drawTableRows(Page page, float[] parameter) throws Exception {
        float x = parameter[0];
        float y = parameter[1];

        float cellH;
        for (int i = rendered; i < tableData.size(); i++) {
            List<Cell> dataRow = tableData.get(i);
            cellH = getMaxCellHeight(dataRow);
            for (int j = 0; j < dataRow.size(); j++) {
                Cell cell = dataRow.get(j);
                float cellW = cell.getWidth();
                int colspan = cell.getColSpan();
                for (int k = 1; k < colspan; k++) {
                    cellW += dataRow.get(++j).getWidth();
                }
                if (page != null) {
                    page.setBrushColor(cell.getBrushColor());
                    cell.paint(page, x, y, cellW, cellH);
                }
                x += cellW;
            }
            x = x1;
            y += cellH;

            // Consider the height of the next row when checking if we must go to a new page
            if (i < (tableData.size() - 1)) {
                List<Cell> nextRow = tableData.get(i + 1);
                for (Cell cell : nextRow) {
                    float cellHeight = cell.getHeight();
                    if (cellHeight > cellH) {
                        cellH = cellHeight;
                    }
                }
            }

            if (page != null && (y + cellH) > (page.height - bottomMargin)) {
                if (i == tableData.size() - 1) {
                    rendered = -1;
                }
                else {
                    rendered = i + 1;
                    numOfPages++;
                }
                return new float[] {x, y};
            }
        }
        rendered = -1;

        return new float[] {x, y};
    }


    private float getMaxCellHeight(List<Cell> row) {
        float maxCellHeight = 0f;
        for (Cell cell : row) {
            if (cell.getHeight() > maxCellHeight) {
                maxCellHeight = cell.getHeight();
            }
        }
        return maxCellHeight;
    }


    /**
     *  Returns true if the table contains more data that needs to be drawn on a page.
     *
     *  @return whether the table has more data to be drawn on a page.
     */
    public boolean hasMoreData() {
        return rendered != -1;
    }


    /**
     *  Returns the width of this table when drawn on a page.
     *
     *  @return the width of this table.
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
     *  Returns the number of data rows that have been rendered so far.
     *
     *  @return the number of data rows that have been rendered so far.
     */
    public int getRowsRendered() {
        return rendered == -1 ? rendered : rendered - numOfHeaderRows;
    }


    /**
     *  Just calls the wrapAroundCellText method.
     */
    @Deprecated
    public void wrapAroundCellText2() {
        wrapAroundCellText();
    }


    /**
     *  Wraps around the text in all cells so it fits the column width.
     *  This method should be called after all calls to setColumnWidth and autoAdjustColumnWidths.
     */
    public void wrapAroundCellText() {
        List<List<Cell>> tableData2 = new ArrayList<List<Cell>>();

        for (List<Cell> row : tableData) {
            for (int i = 0; i < row.size(); i++) {
                Cell cell = row.get(i);
                int colspan = cell.getColSpan();
                for (int n = 1; n < colspan; n++) {
                    Cell next = row.get(i + n);
                    cell.setWidth(cell.getWidth() + next.getWidth());
                    next.setWidth(0f);
                }
            }
        }

        // Adjust the number of header rows automatically!
        numOfHeaderRows = getNumHeaderRows();
        rendered = numOfHeaderRows;

        addExtraTableRows(tableData2);

        for (int i = 0; i < tableData2.size(); i++) {
            List<Cell> row = tableData2.get(i);
            for (int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                if (cell.text != null) {
                    int n = 0;
                    float effectiveWidth = cell.width - (cell.leftPadding + cell.rightPadding);
                    String[] tokens = TextUtils.splitTextIntoTokens(
                            cell.text, cell.font, cell.fallbackFont, effectiveWidth);
                    StringBuilder buf = new StringBuilder();
                    for (String token : tokens) {
                        if (cell.font.stringWidth(cell.fallbackFont,
                                (buf.toString() + " " + token).trim()) > effectiveWidth) {
                            tableData2.get(i + n).get(j).setText(buf.toString().trim());
                            buf = new StringBuilder(token);
                            n++;
                        }
                        else {
                            buf.append(" ");
                            buf.append(token);
                        }
                    }
                    tableData2.get(i + n).get(j).setText(buf.toString().trim());
                }
                else {
                    tableData2.get(i).get(j).setCompositeTextLine(cell.getCompositeTextLine());
                }
            }
        }

        tableData = tableData2;
    }


    private void addExtraTableRows(List<List<Cell>> tableData2) {
        for (List<Cell> row : tableData) {
            int maxNumVerCells = 0;
            for (Cell cell : row) {
                int numVerCells = cell.getNumVerCells();
                if (numVerCells > maxNumVerCells) {
                    maxNumVerCells = numVerCells;
                }
            }

            for (int i = 0; i < maxNumVerCells; i++) {
                List<Cell> row2 = new ArrayList<Cell>();
                for (Cell cell : row) {
                    Cell cell2 = new Cell(cell.getFont(), "");
                    cell2.setFallbackFont(cell.getFallbackFont());
                    cell2.setWidth(cell.getWidth());
                    if (i == 0) {
                        cell2.setPoint(cell.getPoint());
                        cell2.setTopPadding(cell.topPadding);
                    }
                    if (i == (maxNumVerCells - 1)) {
                        cell2.setBottomPadding(cell.bottomPadding);
                    }
                    cell2.setLeftPadding(cell.leftPadding);
                    cell2.setRightPadding(cell.rightPadding);
                    cell2.setLineWidth(cell.lineWidth);
                    cell2.setBgColor(cell.getBgColor());
                    cell2.setPenColor(cell.getPenColor());
                    cell2.setBrushColor(cell.getBrushColor());
                    cell2.setProperties(cell.getProperties());
                    cell2.setVerTextAlignment(cell.getVerTextAlignment());
                    if (i == 0) {
                        if (cell.image != null) {
                            cell2.setImage(cell.getImage());
                        }
                        if (cell.getCompositeTextLine() != null) {
                            cell2.setCompositeTextLine(cell.getCompositeTextLine());
                        }
                        else {
                            cell2.setText(cell.getText());
                        }
                        if (maxNumVerCells > 1) {
                            cell2.setBorder(Border.BOTTOM, false);
                        }
                    }
                    else  {
                        cell2.setBorder(Border.TOP, false);
                        if (i < (maxNumVerCells - 1)) {
                            cell2.setBorder(Border.BOTTOM, false);
                        }
                    }
                    row2.add(cell2);
                }
                tableData2.add(row2);
            }
        }
    }


    private int getNumHeaderRows() {
        int numberOfHeaderRows = 0;
        for (int i = 0; i < this.numOfHeaderRows; i++) {
            List<Cell> row = tableData.get(i);
            int maxNumVerCells = 0;
            for (Cell cell : row) {
                int numVerCells = cell.getNumVerCells();
                if (numVerCells > maxNumVerCells) {
                    maxNumVerCells = numVerCells;
                }
            }
            numberOfHeaderRows += maxNumVerCells;
        }
        return numberOfHeaderRows;
    }


    /**
     *  Sets all table cells borders to <strong>false</strong>.
     *
     */
    public void setNoCellBorders() {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setNoBorders();
            }
        }
    }


    /**
     *  Sets the color of the cell border lines.
     *
     *  @param color the color of the cell border lines.
     */
    public void setCellBordersColor(int color) {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setPenColor(color);
            }
        }
    }


    /**
     *  Sets the width of the cell border lines.
     *
     *  @param width the width of the border lines.
     */
    public void setCellBordersWidth(float width) {
        for (List<Cell> row : tableData) {
            for (Cell cell : row) {
                cell.setLineWidth(width);
            }
        }
    }


    /**
     * Resets the rendered pages count.
     * Call this method if you have to draw this table more than one time.
     */
    public void resetRenderedPagesCount() {
        this.rendered = numOfHeaderRows;
    }


    /**
     * This method removes borders that have the same color and overlap 100%.
     * The result is improved onscreen rendering of thin border lines by some PDF viewers.
     */
    public void mergeOverlaidBorders() {
        for (int i = 0; i < tableData.size(); i++) {
            List<Cell> currentRow = tableData.get(i);
            for (int j = 0; j < currentRow.size(); j++) {
                Cell currentCell = currentRow.get(j);
                if (j < currentRow.size() - 1) {
                    Cell cellAtRight = currentRow.get(j + 1);
                    if (cellAtRight.getBorder(Border.LEFT) &&
                            currentCell.getPenColor() == cellAtRight.getPenColor() &&
                            currentCell.getLineWidth() == cellAtRight.getLineWidth() &&
                            (currentCell.getColSpan() + j) < (currentRow.size() - 1)) {
                        currentCell.setBorder(Border.RIGHT, false);
                    }
                }
                if (i < tableData.size() - 1) {
                    List<Cell> nextRow = tableData.get(i + 1);
                    Cell cellBelow = nextRow.get(j);
                    if (cellBelow.getBorder(Border.TOP) &&
                            currentCell.getPenColor() == cellBelow.getPenColor() &&
                            currentCell.getLineWidth() == cellBelow.getLineWidth()) {
                        currentCell.setBorder(Border.BOTTOM, false);
                    }
                }
            }
        }
    }


    /**
     *  Auto adjusts the widths of all columns so that they are just wide enough to hold the text without truncation.
     */
    public void autoAdjustColumnWidths() {
        float[] maxColWidths = new float[tableData.get(0).size()];

        for (int i = 0; i < numOfHeaderRows; i++) {
            for (int j = 0; j < maxColWidths.length; j++) {
                Cell cell = tableData.get(i).get(j);
                float textWidth = cell.font.stringWidth(cell.fallbackFont, cell.text);
                textWidth += cell.leftPadding + cell.rightPadding;
                if (textWidth > maxColWidths[j]) {
                    maxColWidths[j] = textWidth;
                }
            }
        }

        for (int i = numOfHeaderRows; i < tableData.size(); i++) {
            for (int j = 0; j < maxColWidths.length; j++) {
                Cell cell = tableData.get(i).get(j);
                if (cell.getColSpan() > 1) {
                    continue;
                }
                if (cell.text != null) {
                    float textWidth = cell.font.stringWidth(cell.fallbackFont, cell.text);
                    textWidth += cell.leftPadding + cell.rightPadding;
                    if (textWidth > maxColWidths[j]) {
                        maxColWidths[j] = textWidth;
                    }
                }
                if (cell.image != null) {
                    float imageWidth = cell.image.getWidth() + cell.leftPadding + cell.rightPadding;
                    if (imageWidth > maxColWidths[j]) {
                        maxColWidths[j] = imageWidth;
                    }
                }
                if (cell.barCode != null) {
                    try {
                        float barcodeWidth = cell.barCode.drawOn(null)[0] + cell.leftPadding + cell.rightPadding;
                        if (barcodeWidth > maxColWidths[j]) {
                            maxColWidths[j] = barcodeWidth;
                        }
                    } catch (Exception e) {
                    }
                }
                if (cell.textBlock != null) {
                    String[] tokens = cell.textBlock.text.split("\\s+");
                    for (String token : tokens) {
                        float tokenWidth = cell.textBlock.font.stringWidth(cell.textBlock.fallbackFont, token);
                        tokenWidth += cell.leftPadding + cell.rightPadding;
                        if (tokenWidth > maxColWidths[j]) {
                            maxColWidths[j] = tokenWidth;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < tableData.size(); i++) {
            List<Cell> row = tableData.get(i);
            for (int j = 0; j < row.size(); j++) {
                Cell cell = row.get(j);
                cell.setWidth(maxColWidths[j] + 0.1f);
            }
        }

        autoResizeColumnsWithColspanBiggerThanOne();
    }


    private boolean isTextColumn(int index) {
        for (int i = numOfHeaderRows; i < tableData.size(); i++) {
            List<Cell> dataRow = tableData.get(i);
            if (dataRow.get(index).image != null || dataRow.get(index).barCode != null) {
                return false;
            }
        }
        return true;
    }


    public void fitToPage(float[] pageSize) {
        autoAdjustColumnWidths();

        float tableWidth = (pageSize[0] - this.x1) - rightMargin;
        float textColumnWidths = 0f;
        float otherColumnWidths = 0f;
        List<Cell> row = tableData.get(0);
        for (int i = 0; i < row.size(); i++) {
            Cell cell = row.get(i);
            if (isTextColumn(i)) {
                textColumnWidths += cell.getWidth();
            }
            else {
                otherColumnWidths += cell.getWidth();
            }
        }

        float adjusted;
        if ((tableWidth - otherColumnWidths) > textColumnWidths) {
            adjusted = textColumnWidths + ((tableWidth - otherColumnWidths) - textColumnWidths);
        }
        else {
            adjusted = textColumnWidths - (textColumnWidths - (tableWidth - otherColumnWidths));
        }
        float factor = adjusted / textColumnWidths;
        for (int i = 0; i < row.size(); i++) {
            if (isTextColumn(i)) {
                setColumnWidth(i, getColumnWidth(i) * factor);
            }
        }

        autoResizeColumnsWithColspanBiggerThanOne();
        mergeOverlaidBorders();
    }


    private void autoResizeColumnsWithColspanBiggerThanOne() {
        for (int i = 0; i < tableData.size(); i++) {
            List<Cell> dataRow = tableData.get(i);
            for (int j = 0; j < dataRow.size(); j++) {
                Cell cell = dataRow.get(j);
                int colspan = cell.getColSpan();
                if (colspan > 1) {
                    if (cell.textBlock != null) {
                        float sumOfWidths = cell.getWidth();
                        for (int k = 1; k < colspan; k++) {
                            sumOfWidths += dataRow.get(++j).getWidth();
                        }
                        cell.textBlock.setWidth(sumOfWidths - (cell.leftPadding + cell.rightPadding));
                    }
                }
            }
        }
    }


    public void setRightMargin(float rightMargin) {
        this.rightMargin = rightMargin;
    }


    public void setFirstPageTopMargin(float topMargin) {
        this.y1FirstPage = y1 + topMargin;
    }


    public static void addToRow(List<Cell> row, Cell cell) {
        row.add(cell);
        for (int i = 1; i < cell.getColSpan(); i++) {
            row.add(new Cell(cell.getFont(), ""));
        }
    }

}   // End of Table.java
