import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

import com.pdfjet.A4;
import com.pdfjet.Align;
import com.pdfjet.Border;
import com.pdfjet.Cell;
import com.pdfjet.CoreFont;
import com.pdfjet.Font;
import com.pdfjet.PDF;
import com.pdfjet.Page;
import com.pdfjet.Table;


public class Example_38 {

    private PDF pdf;
    private Font font;
    private Page a4Landscape;
    private float mm;


    public Example_38() throws Exception {
        BufferedOutputStream bos =
                new BufferedOutputStream(new FileOutputStream("Example_38.pdf"));

        pdf = new PDF(bos);
        font = new Font(pdf, CoreFont.COURIER);
        a4Landscape = createA4LandscapePage();
        mm = a4Landscape.getHeight()/210f;
        renderTableOnPage();

        pdf.close();
    }

    
    public void renderTableOnPage() throws Exception {
        Table tb = new Table();
        tb.setData(createTableData());
        tb.setBottomMargin(mm*10);
        tb.setPosition(mm*10, mm*10);
        tb.getNumberOfPages(a4Landscape);
        tb.drawOn(a4Landscape);
        while (tb.hasMoreData()) {
            a4Landscape=createA4LandscapePage();
            tb.drawOn(a4Landscape);
        }       
    }


    private Page createA4LandscapePage() throws Exception {
        return new Page(pdf, A4.LANDSCAPE);
    }


    /**
     * This will return a 10x10 matrix. The HTML-Like table will be like:
     * <table border="solid">
     * <tr>
     * <td colspan="2" rowspan="2">2x2</td>
     * <td colspan="2">2x1</td>
     * <td colspan="2">2x1</td>
     * <td colspan="2">2x1</td>
     * <td colspan="2">2x1</td>
     * </tr>
     * <tr>
     * <td colspan="2" rowspan="2">2x2</td>
     * <td>1x1</td>
     * <td colspan="5">5x1</td>
     * </tr>
     * <tr>
     * <td rowspan="2">1x2</td>
     * <td>1x1</td>
     * <td colspan="2" rowspan="2">2x2</td>
     * <td rowspan="2">1x2</td>
     * <td colspan="3">3x1</td>
     * </tr>
     * <tr>
     * <td>1x1</td>
     * <td rowspan="3">1x3</td>
     * <td>1x1</td>
     * <td colspan="2">2x1</td>
     * <td rowspan="2">1x2</td>
     * </tr>
     * <tr>
     * <td rowspan="2">1x2</td>
     * <td>1x1</td>
     * <td colspan="2">2x1</td>
     * <td colspan="4" rowspan="4">4x4</td>
     * </tr>
     * <tr>
     * <td>1x1</td>
     * <td rowspan="3">1x3</td>
     * <td rowspan="3">1x3</td>
     * <td rowspan="3">1x3</td>
     * </tr>
     * <tr>
     * <td rowspan="2">1x2</td>
     * <td>1x1</td>
     * <td rowspan="4">1x4</td>
     * </tr>
     * <tr>
     * <td>1x1</td>
     * </tr>
     * <tr>
     * <td rowspan="2">1x2</td>
     * <td>1x1</td>
     * <td colspan="2">2x1</td>
     * <td colspan="2" rowspan="2">2x2</td>
     * <td rowspan="2">1x2</td>
     * <td>1x1</td>
     * <td>1x1</td>
     * </tr>
     * <tr>
     * <td>1x1</td>
     * <td>1x1</td>
     * <td>1x1</td>
     * <td>1x1</td>
     * <td>1x1</td>
     * </tr>
     * </table>
     * 
     * @return
     * @throws Exception
     */
    private List<List<Cell>> createTableData() throws Exception {

        List<List<Cell>> rows = new LinkedList<List<Cell>>();

        for (int i = 0; i < 10; i++) {
            List<Cell> row = new LinkedList<Cell>();
            switch (i) {
            case 0:
                row.add(getCell(font, 2, "2x2", true, false));
                row.add(getCell(font, 1,    "", true, false));
                row.add(getCell(font, 2, "2x1", true, true));
                row.add(getCell(font, 1,    "", true, false));
                row.add(getCell(font, 2, "2x1", true, true));
                row.add(getCell(font, 1,    "", true, false));
                row.add(getCell(font, 2, "2x1", true, true));
                row.add(getCell(font, 1,    "", true, false));
                row.add(getCell(font, 2, "2x1", true, true));
                row.add(getCell(font, 1,    "", true, false));
                break;
            case 1:
                row.add(getCell(font, 2,   "^", false, true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 2, "2x2", true,  false));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 5, "5x1", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                break;
            case 2:
                row.add(getCell(font, 1, "1x2", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 2,   "^", false, true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 2, "2x2", true,  false));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 3, "3x1", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1, "1x1", true,  true));
                break;
            case 3:
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1, "1x3", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 2,   "^", false, true));
                row.add(getCell(font, 1,    "", true,  false));
                row.add(getCell(font, 1, "1x1", false, true));
                row.add(getCell(font, 2, "2x1", true,  true));
                row.add(getCell(font, 1,    "", true,  false));
                row.add(getCell(font, 1, "1x2", true,  false));
                break;
            case 4:
                row.add(getCell(font, 1, "1x2", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1,   "^", false, false));
                row.add(getCell(font, 2, "2x1", true,  true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 4, "4x4", true,  false));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,   "^", false, true));
                break;
            case 5:
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x3", true,  false));
                row.add(getCell(font, 1, "1x3", true,  false));
                row.add(getCell(font, 4,   "^", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1, "1x3", true,  false));
                break;
            case 6:
                row.add(getCell(font, 1, "1x2", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1, "1x4", true,  false));
                row.add(getCell(font, 1,   "^", false, false));
                row.add(getCell(font, 1,   "^", false, false));
                row.add(getCell(font, 4,   "^", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1,    "", false, false));
                row.add(getCell(font, 1,   "^", false, false));
                break;
            case 7:
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1,   "^", false, false));
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 4,   "^", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,   "^", false, true));
                break;
            case 8:
                row.add(getCell(font, 1, "1x2", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1,   "^", false, false));
                row.add(getCell(font, 2, "2x1", true,  true));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 2, "2x2", true,  false));
                row.add(getCell(font, 1,    "", true,  true));
                row.add(getCell(font, 1, "1x2", true,  false));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1, "1x1", true,  true));
                break;
            case 9:
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 1, "1x1", true,  true));
                row.add(getCell(font, 2,   "^", false, true));
                row.add(getCell(font, 1,    "", false, true));
                row.add(getCell(font, 1,   "^", false, true));
                row.add(getCell(font, 1, "1x1", false, true));
                row.add(getCell(font, 1, "1x1", false, true));
                break;
            }
            rows.add(row);
        }       

        return rows;
    }


    private Cell getCell(
            Font font,
            int colSpan,
            String text,
            boolean topBorder,
            boolean bottomBoarder) throws Exception {
        Cell cell = new Cell(font);
        cell.setColSpan(colSpan);
        cell.setWidth(50f);
        cell.setText(text);
        cell.setBorder(Border.TOP, topBorder);
        cell.setBorder(Border.BOTTOM, bottomBoarder);
        cell.setTextAlignment(Align.CENTER);
        return cell;
    }
    

    public static void main(String[] args) throws Exception {
        new Example_38();
    }

}
