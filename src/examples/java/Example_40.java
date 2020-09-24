import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_40.java
 *
 */
public class Example_40 {

    public Example_40() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_40.pdf")));

        Page page = new Page(pdf, Letter.PORTRAIT);

        Font f1 = new Font(pdf, CoreFont.HELVETICA_BOLD);
        Font f2 = new Font(pdf, CoreFont.HELVETICA_BOLD);

        f1.setItalic(true);
        f2.setItalic(true);

        f1.setSize(10f);
        f2.setSize(8f);

        Chart chart = new Chart(f1, f2);
        chart.setLocation(70f, 50f);
        chart.setSize(500f, 300f);
        chart.setTitle("Vertical Bar Chart Example");
        chart.setXAxisTitle("Bar Chart");
        chart.setYAxisTitle("Vertical");
        chart.setData(getData());
        chart.setDrawXAxisLabels(false);

        chart.drawOn(page);

        pdf.close();
    }


    public List<List<Point>> getData() throws Exception {
        List<List<Point>> chartData = new ArrayList<List<Point>>();

        List<Point> path1 = new ArrayList<Point>();

        Point point = new Point();
        point.setStartOfPath();
        point.setX(15f);
        point.setY(0f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.blue);
        point.setLineWidth(25f);
        point.setText(" Vertical");
        point.setTextColor(Color.white);
        point.setTextDirection(90);
        path1.add(point);

        point = new Point();
        point.setX(15f);
        point.setY(45f);
        point.setShape(Point.INVISIBLE);
        path1.add(point);


        List<Point> path2 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(25f);
        point.setY(0f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.green);
        point.setLineWidth(25f);
        point.setText(" Bar");
        point.setTextColor(Color.white);
        point.setTextDirection(90);
        path2.add(point);

        point = new Point();
        point.setX(25f);
        point.setY(20f);
        point.setShape(Point.INVISIBLE);
        path2.add(point);


        List<Point> path3 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(35f);
        point.setY(0f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.red);
        point.setLineWidth(25f);
        point.setText(" Chart");
        point.setTextColor(Color.white);
        point.setTextDirection(90);
        path3.add(point);

        point = new Point();
        point.setX(35f);
        point.setY(31);
        point.setShape(Point.INVISIBLE);
        path3.add(point);


        List<Point> path4 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(45f);
        point.setY(0f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.gold);
        point.setLineWidth(25f);
        point.setText(" Example");
        point.setTextColor(Color.black);
        point.setTextDirection(90);
        path4.add(point);

        point = new Point();
        point.setX(45f);
        point.setY(73);
        point.setShape(Point.INVISIBLE);
        path4.add(point);

        chartData.add(path1);
        chartData.add(path2);
        chartData.add(path3);
        chartData.add(path4);

        return chartData;
    }


    public static void main(String[] args) throws Exception {
        new Example_40();
    }

}   // End of Example_40.java
