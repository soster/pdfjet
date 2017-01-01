import java.io.*;
import java.util.*;

import com.pdfjet.*;


/**
 *  Example_39.java
 *
 */
public class Example_39 {

    public Example_39() throws Exception {

        PDF pdf = new PDF(
                new BufferedOutputStream(
                        new FileOutputStream("Example_39.pdf")));

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
        chart.setTitle("Horizontal Bar Chart Example");
        chart.setXAxisTitle("");
        chart.setYAxisTitle("");
        chart.setData(getData());
        chart.setDrawYAxisLabels(false);

        chart.drawOn(page);

        pdf.close();
    }


    public List<List<Point>> getData() throws Exception {
        List<List<Point>> chartData = new ArrayList<List<Point>>();

        List<Point> path1 = new ArrayList<Point>();

        Point point = new Point();
        point.setStartOfPath();
        point.setX(0f);
        point.setY(45f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.blue);
        point.setLineWidth(20f);
        point.setText(" Horizontal");
        point.setTextColor(Color.white);
        path1.add(point);

        point = new Point();
        point.setX(35f);
        point.setY(45f);
        point.setShape(Point.INVISIBLE);
        path1.add(point);


        List<Point> path2 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(0f);
        point.setY(35f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.gold);
        point.setLineWidth(20f);
        point.setText(" Bar");
        point.setTextColor(Color.black);
        path2.add(point);

        point = new Point();
        point.setX(22f);
        point.setY(35f);
        point.setShape(Point.INVISIBLE);
        path2.add(point);


        List<Point> path3 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(0f);
        point.setY(25f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.green);
        point.setLineWidth(20f);
        point.setText(" Chart");
        point.setTextColor(Color.white);
        path3.add(point);

        point = new Point();
        point.setX(30f);
        point.setY(25f);
        point.setShape(Point.INVISIBLE);
        path3.add(point);


        List<Point> path4 = new ArrayList<Point>();

        point = new Point();
        point.setStartOfPath();
        point.setX(0f);
        point.setY(15f);
        point.setShape(Point.INVISIBLE);
        point.setColor(Color.red);
        point.setLineWidth(20f);
        point.setText(" Example");
        point.setTextColor(Color.white);
        path4.add(point);

        point = new Point();
        point.setX(47f);
        point.setY(15f);
        point.setShape(Point.INVISIBLE);
        path4.add(point);

        chartData.add(path1);
        chartData.add(path2);
        chartData.add(path3);
        chartData.add(path4);

        return chartData;
    }


    public static void main(String[] args) throws Exception {
        new Example_39();
    }

}   // End of Example_39.java
