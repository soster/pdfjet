import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_09.java
 *
 */
public class Example_09 {

    public Example_09() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_09.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f3 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f4 = new Font(pdf, CoreFont.HELVETICA);

        f1.setItalic(true);
        f2.setItalic(true);
        f3.setItalic(true);
        f4.setItalic(true);

        f1.setSize(10f);
        f2.setSize(8f);
        f3.setSize(7f);
        f4.setSize(7f);

        Chart chart = new Chart(f1, f2);
        chart.setLocation(70f, 50f);
        chart.setSize(500f, 300f);
        chart.setTitle("World View - Communications");
        chart.setXAxisTitle("Cell phones per capita");
        chart.setYAxisTitle("Internet users % of the population");
        chart.setData(getData("data/world-communications.txt", "|"));
        addTrendLine(chart);
        chart.drawOn(page);

        addTableToChart(page, chart, f3, f4);

        pdf.close();
    }


    public void addTrendLine(Chart chart) {
        List<Point> points = chart.getData().get(0);

        float m = chart.slope(points);
        float b = chart.intercept(points, m);

        List<Point> trendline = new ArrayList<Point>();
        float x = 0.0f;
        float y = m * x + b;
        Point p1 = new Point(x, y);
        p1.setStartOfPath();
        p1.setColor(Color.blue);
        p1.setShape(Point.INVISIBLE);

        x = 1.5f;
        y = m * x + b;
        Point p2 = new Point(x, y);
        p2.setShape(Point.INVISIBLE);

        trendline.add(p1);
        trendline.add(p2);

        chart.getData().add(trendline);
    }


    public void addTableToChart(
            Page page, Chart chart, Font f3, Font f4) throws Exception {
        Table table = new Table();
        List<List<Cell>> tableData = new ArrayList<List<Cell>>();
        List<Point> points = chart.getData().get(0);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            if (point.getShape() != Point.CIRCLE) {
                List<Cell> tableRow = new ArrayList<Cell>();

                point.setRadius(2f);
                point.setFillShape(true);
                point.setAlignment(Align.LEFT);

                Cell cell = new Cell(f4);
                cell.setPoint(point);
                cell.setText("");

                tableRow.add(cell);

                cell = new Cell(f4);
                cell.setText(point.getText());
                tableRow.add(cell);

                cell = new Cell(f4);
                cell.setText(point.getURIAction());
                tableRow.add(cell);

                tableData.add(tableRow);
            }
        }
        table.setData(tableData);
        table.autoAdjustColumnWidths();
        table.setCellBordersWidth(0.2f);
        table.setLocation(70f, 360f);
        table.setColumnWidth(0, 9f);
        table.drawOn(page);
    }


    public List<List<Point>> getData(
            String fileName,
            String delimiter) throws Exception {
        List<List<Point>> chartData = new ArrayList<List<Point>>();

        BufferedReader reader =
                new BufferedReader(new FileReader(fileName));
        List<Point> points = new ArrayList<Point>();
        String line = null;
        while ((line = reader.readLine()) != null) {
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

            Point point = new Point();
            try {
                double population =
                        Double.valueOf(cols[1].replace(",", ""));
                point.setText(cols[0].trim());
                String country_name = point.getText();
                country_name = country_name.replace(" ", "_");
                country_name = country_name.replace("'", "_");
                country_name = country_name.replace(",", "_");
                country_name = country_name.replace("(", "_");
                country_name = country_name.replace(")", "_");
                point.setURIAction("http://pdfjet.com/country/" + country_name + ".txt");
                point.setX((float) (Double.valueOf(cols[5].replace(",", "")) / population));
                point.setY((float) (Double.valueOf(cols[7].replace(",", "")) / population * 100));
                point.setRadius(2f);

                if (point.getX() > 1.25f) {
                    point.setShape(Point.RIGHT_ARROW);
                    point.setColor(Color.black);
                }
                if (point.getY() > 80f) {
                    point.setShape(Point.UP_ARROW);
                    point.setColor(Color.blue);
                }
                if (point.getText().equals("France")) {
                    point.setShape(Point.MULTIPLY);
                    point.setColor(Color.black);
                }
                if (point.getText().equals("Canada")) {
                    point.setShape(Point.BOX);
                    point.setColor(Color.darkolivegreen);
                }
                if (point.getText().equals("United States")) {
                    point.setShape(Point.STAR);
                    point.setColor(Color.red);
                }

                points.add(point);
            } catch (Exception e) {
            }
        }
        reader.close();
        chartData.add(points);

        return chartData;
    }


    public static void main(String[] args) throws Exception {
        new Example_09();
    }

}   // End of Example_09.java
