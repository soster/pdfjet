/**
 *  Chart.java
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
import java.text.*;


/**
 *  Used to create XY chart objects and draw them on a page.
 *
 *  Please see Example_09.
 */
public class Chart implements Drawable {

    private float w = 300f;
    private float h = 200f;

    private float x1;
    private float y1;

    private float x2;
    private float y2;

    private float x3;
    private float y3;

    private float x4;
    private float y4;

    private float x5;
    private float y5;

    private float x6;
    private float y6;

    private float x7;
    private float y7;

    private float x8;
    private float y8;

    private float xMax = Float.MIN_VALUE;
    private float xMin = Float.MAX_VALUE;

    private float yMax = Float.MIN_VALUE;
    private float yMin = Float.MAX_VALUE;

    private int xAxisGridLines = 0;
    private int yAxisGridLines = 0;

    private String title = "";
    private String xAxisTitle = "";
    private String yAxisTitle = "";

    private boolean drawXAxisLines = true;
    private boolean drawYAxisLines = true;

    private boolean drawXAxisLabels = true;
    private boolean drawYAxisLabels = true;

    private boolean xyChart = true;

    private float hGridLineWidth;
    private float vGridLineWidth;

    private String hGridLinePattern = "[1 1] 0";
    private String vGridLinePattern = "[1 1] 0";

    private float chartBorderWidth = 0.3f;
    private float innerBorderWidth = 0.3f;

    private NumberFormat nf = null;
    private int minFractionDigits = 2;
    private int maxFractionDigits = 2;

    private Font f1;
    private Font f2;

    private List<List<Point>> chartData = null;


    /**
     *  Create a XY chart object.
     *
     *  @param f1 the font used for the chart title.
     *  @param f2 the font used for the X and Y axis titles.
     */
    public Chart(Font f1, Font f2) {
        this.f1 = f1;
        this.f2 = f2;
        nf = NumberFormat.getInstance();
    }


    /**
     *  Sets the title of the chart.
     *
     *  @param title the title text.
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     *  Sets the title for the X axis.
     *
     *  @param title the X axis title.
     */
    public void setXAxisTitle(String title) {
        this.xAxisTitle = title;
    }


    /**
     *  Sets the title for the Y axis.
     *
     *  @param title the Y axis title.
     */
    public void setYAxisTitle(String title) {
        this.yAxisTitle = title;
    }


    /**
     *  Sets the data that will be used to draw this chart.
     *
     *  @param chartData the data.
     */
    public void setData(List<List<Point>> chartData) {
        this.chartData = chartData;
    }


    /**
     *  Returns the chart data.
     *
     *  @return the chart data.
     */
    public List<List<Point>> getData() {
        return chartData;
    }


    /**
     *  Sets the position of this chart on the page.
     *
     *  @param x the x coordinate of the top left corner of this chart when drawn on the page.
     *  @param y the y coordinate of the top left corner of this chart when drawn on the page.
     */
    public void setPosition(double x, double y) {
        setLocation((float) x, (float) y);
    }


    /**
     *  Sets the position of this chart on the page.
     *
     *  @param x the x coordinate of the top left corner of this chart when drawn on the page.
     *  @param y the y coordinate of the top left corner of this chart when drawn on the page.
     */
    public void setPosition(float x, float y) {
        setLocation(x, y);
    }


    /**
     *  Sets the location of this chart on the page.
     *
     *  @param x the x coordinate of the top left corner of this chart when drawn on the page.
     *  @param y the y coordinate of the top left corner of this chart when drawn on the page.
     *  @return this Chart object.
     */
    public Chart setLocation(double x, double y) {
        return setLocation((float) x, (float) y);
    }


    /**
     *  Sets the location of this chart on the page.
     *
     *  @param x the x coordinate of the top left corner of this chart when drawn on the page.
     *  @param y the y coordinate of the top left corner of this chart when drawn on the page.
     *  @return this Chart object.
     */
    public Chart setLocation(float x, float y) {
        this.x1 = x;
        this.y1 = y;
        return this;
    }


    /**
     *  Sets the size of this chart.
     *
     *  @param w the width of this chart.
     *  @param h the height of this chart.
     */
    public void setSize(double w, double h) {
        setSize((float) w, (float) h);
    }


    /**
     *  Sets the size of this chart.
     *
     *  @param w the width of this chart.
     *  @param h the height of this chart.
     */
    public void setSize(float w, float h) {
        this.w = w;
        this.h = h;
    }


    /**
     *  Sets the minimum number of fractions digits do display for the X and Y axis labels.
     *
     *  @param minFractionDigits the minimum number of fraction digits.
     */
    public void setMinimumFractionDigits(int minFractionDigits) {
        this.minFractionDigits = minFractionDigits;
    }


    /**
     *  Sets the maximum number of fractions digits do display for the X and Y axis labels.
     *
     *  @param maxFractionDigits the maximum number of fraction digits.
     */
    public void setMaximumFractionDigits(int maxFractionDigits) {
        this.maxFractionDigits = maxFractionDigits;
    }


    /**
     *  Calculates the slope of a trend line given a list of points.
     *  See Example_09.
     *
     *  @param points the list of points.
     *  @return the slope float value.
     */
    public float slope(List<Point> points) {
        return (covar(points) / devsq(points) * (points.size() - 1));
    }


    /**
     *  Calculates the intercept of a trend line given a list of points.
     *  See Example_09.
     *
     *  @param points the list of points.
     *  @param slope the slope.
     *  @return the intercept float value.
     */
    public float intercept(List<Point> points, double slope) {
        return intercept(points, (float) slope);
    }


    /**
     *  Calculates the intercept of a trend line given a list of points.
     *  See Example_09.
     *
     *  @param points the list of points.
     *  @param slope the slope.
     *  @return the intercept float value.
     */
    public float intercept(List<Point> points, float slope) {
        float[] _mean = mean(points);
        return (_mean[1] - slope * _mean[0]);
    }


    public void setDrawXAxisLines(boolean drawXAxisLines) {
        this.drawXAxisLines = drawXAxisLines;
    }


    public void setDrawYAxisLines(boolean drawYAxisLines) {
        this.drawYAxisLines = drawYAxisLines;
    }


    public void setDrawXAxisLabels(boolean drawXAxisLabels) {
        this.drawXAxisLabels = drawXAxisLabels;
    }


    public void setDrawYAxisLabels(boolean drawYAxisLabels) {
        this.drawYAxisLabels = drawYAxisLabels;
    }


    public void setXYChart(boolean xyChart) {
        this.xyChart = xyChart;
    }


    /**
     *  Draws this chart on the specified page.
     *
     *  @param page the page to draw this chart on.
     */
    public float[] drawOn(Page page) throws Exception {
        nf.setMinimumFractionDigits(minFractionDigits);
        nf.setMaximumFractionDigits(maxFractionDigits);

        x2 = x1 + w;
        y2 = y1;

        x3 = x2;
        y3 = y1 + h;

        x4 = x1;
        y4 = y3;

        setXAxisMinAndMaxChartValues();
        setYAxisMinAndMaxChartValues();
        roundXAxisMinAndMaxValues();
        roundYAxisMinAndMaxValues();

        // Draw chart title
        page.drawString(
                f1,
                title,
                x1 + ((w - f1.stringWidth(title)) / 2),
                y1 + 1.5f * f1.bodyHeight);

        float topMargin = 2.5f * f1.bodyHeight;
        float leftMargin = getLongestAxisYLabelWidth() + 2f * f2.bodyHeight;
        float rightMargin = 2f * f2.bodyHeight;
        float bottomMargin = 2.5f * f2.bodyHeight;

        x5 = x1 + leftMargin;
        y5 = y1 + topMargin;

        x6 = x2 - rightMargin;
        y6 = y5;

        x7 = x6;
        y7 = y3 - bottomMargin;

        x8 = x5;
        y8 = y7;

        drawChartBorder(page);
        drawInnerBorder(page);

        if (drawXAxisLines) {
            drawHorizontalGridLines(page);
        }
        if (drawYAxisLines) {
            drawVerticalGridLines(page);
        }
        if (drawXAxisLabels) {
            drawXAxisLabels(page);
        }
        if (drawYAxisLabels) {
            drawYAxisLabels(page);
        }

        // Translate the point coordinates
        for (int i = 0; i < chartData.size(); i++) {
            List<Point> points = chartData.get(i);
            for (int j = 0; j < points.size(); j++) {
                Point point = points.get(j);
                if (xyChart) {
                    point.x = x5 + (point.x - xMin) * (x6 - x5) / (xMax - xMin);
                    point.y = y8 - (point.y - yMin) * (y8 - y5) / (yMax - yMin);
                    point.lineWidth *= (x6 - x5) / w;
                }
                else {
                    point.x = x5 + point.x * (x6 - x5) / w;
                    point.y = y8 - (point.y - yMin) * (y8 - y5) / (yMax - yMin);
                }
                if (point.getURIAction() != null) {
                    page.addAnnotation(new Annotation(
                            point.getURIAction(),
                            null,
                            point.x - point.r,
                            point.y - point.r,
                            point.x + point.r,
                            point.y + point.r,
                            null,
                            null,
                            null));
                }
            }
        }

        drawPathsAndPoints(page, chartData);

        // Draw the Y axis title
        page.setBrushColor(Color.black);
        page.setTextDirection(90);
        page.drawString(
                f1,
                yAxisTitle,
                x1 + f1.bodyHeight,
                y8 - ((y8 - y5) - f1.stringWidth(yAxisTitle)) / 2);

        // Draw the X axis title
        page.setTextDirection(0);
        page.drawString(
                f1,
                xAxisTitle,
                x5 + ((x6 - x5) - f1.stringWidth(xAxisTitle)) / 2,
                y4 - f1.bodyHeight / 2);

        page.setDefaultLineWidth();
        page.setDefaultLinePattern();
        page.setPenColor(Color.black);

        return new float[] {this.x1 + this.w, this.y1 + this.h};
    }


    private float getLongestAxisYLabelWidth() {
        float minLabelWidth = f2.stringWidth(nf.format(yMin) + "0");
        float maxLabelWidth = f2.stringWidth(nf.format(yMax) + "0");
        if (maxLabelWidth > minLabelWidth) {
            return maxLabelWidth;
        }
        return minLabelWidth;
    }


    private void setXAxisMinAndMaxChartValues() {
        if (xAxisGridLines != 0) {
            return;
        }
        for (List<Point> points : chartData) {
            for (Point point : points) {
                if (point.x < xMin) {
                    xMin = point.x;
                }
                if (point.x > xMax) {
                    xMax = point.x;
                }
            }
        }
    }


    private void setYAxisMinAndMaxChartValues() {
        if (yAxisGridLines != 0) {
            return;
        }
        for (List<Point> points : chartData) {
            for (Point point : points) {
                if (point.y < yMin) {
                    yMin = point.y;
                }
                if (point.y > yMax) {
                    yMax = point.y;
                }
            }
        }
    }


    private void roundXAxisMinAndMaxValues() {
        if (xAxisGridLines != 0) {
            return;
        }
        Round round = roundMaxAndMinValues(xMax, xMin);
        xMax = round.maxValue;
        xMin = round.minValue;
        xAxisGridLines = round.numOfGridLines;
    }


    private void roundYAxisMinAndMaxValues() {
        if (yAxisGridLines != 0) {
            return;
        }
        Round round = roundMaxAndMinValues(yMax, yMin);
        yMax = round.maxValue;
        yMin = round.minValue;
        yAxisGridLines = round.numOfGridLines;
    }


    private void drawChartBorder(Page page) {
        page.setPenWidth(chartBorderWidth);
        page.setPenColor(Color.black);
        page.moveTo(x1, y1);
        page.lineTo(x2, y2);
        page.lineTo(x3, y3);
        page.lineTo(x4, y4);
        page.closePath();
        page.strokePath();
    }


    private void drawInnerBorder(Page page) {
        page.setPenWidth(innerBorderWidth);
        page.setPenColor(Color.black);
        page.moveTo(x5, y5);
        page.lineTo(x6, y6);
        page.lineTo(x7, y7);
        page.lineTo(x8, y8);
        page.closePath();
        page.strokePath();
    }


    private void drawHorizontalGridLines(Page page) {
        page.setPenWidth(hGridLineWidth);
        page.setPenColor(Color.black);
        page.setLinePattern(hGridLinePattern);
        float x = x8;
        float y = y8;
        float step = (y8 - y5) / yAxisGridLines;
        for (int i = 0; i < yAxisGridLines; i++) {
            page.drawLine(x, y, x6, y);
            y -= step;
        }
    }


    private void drawVerticalGridLines(Page page) {
        page.setPenWidth(vGridLineWidth);
        page.setPenColor(Color.black);
        page.setLinePattern(vGridLinePattern);
        float x = x5;
        float y = y5;
        float step = (x6 - x5) / xAxisGridLines;
        for (int i = 0; i < xAxisGridLines; i++) {
            page.drawLine(x, y, x, y8);
            x += step;
        }
    }


    private void drawXAxisLabels(Page page) {
        float x = x5;
        float y = y8 + f2.bodyHeight;
        float step = (x6 - x5) / xAxisGridLines;
        page.setBrushColor(Color.black);
        for (int i = 0; i < (xAxisGridLines + 1); i++) {
            String label = nf.format(xMin + ((xMax - xMin) / xAxisGridLines) * i);
            page.drawString(f2, label, x - (f2.stringWidth(label) / 2), y);
            x += step;
        }
    }


    private void drawYAxisLabels(Page page) {
        float x = x5 - getLongestAxisYLabelWidth();
        float y = y8 + f2.ascent / 3;
        float step = (y8 - y5) / yAxisGridLines;
        page.setBrushColor(Color.black);
        for (int i = 0; i < (yAxisGridLines + 1); i++) {
            String label = nf.format(yMin + ((yMax - yMin) / yAxisGridLines) * i);
            page.drawString(f2, label, x, y);
            y -= step;
        }
    }


    private void drawPathsAndPoints(
            Page page, List<List<Point>> chartData) throws Exception {
        for (int i = 0; i < chartData.size(); i++) {
            List<Point> points = chartData.get(i);
            Point point = points.get(0);
            if (point.drawPath) {
                page.setPenColor(point.color);
                page.setPenWidth(point.lineWidth);
                page.setLinePattern(point.linePattern);
                page.drawPath(points, Operation.STROKE);
                if (point.getText() != null) {
                    page.setBrushColor(point.getTextColor());
                    page.setTextDirection(point.getTextDirection());
                    page.drawString(f2, point.getText(), point.x, point.y);
                }
            }
            for (int j = 0; j < points.size(); j++) {
                point = points.get(j);
                if (point.getShape() != Point.INVISIBLE) {
                    page.setPenWidth(point.lineWidth);
                    page.setLinePattern(point.linePattern);
                    page.setPenColor(point.color);
                    page.setBrushColor(point.color);
                    page.drawPoint(point);
                }
            }
        }
    }


    private Round roundMaxAndMinValues(float maxValue, float minValue) {

        int maxExponent = (int) Math.floor(Math.log(maxValue) / Math.log(10));
        maxValue *= (float) Math.pow(10, -maxExponent);

        if      (maxValue > 9.00f) { maxValue = 10.0f; }
        else if (maxValue > 8.00f) { maxValue = 9.00f; }
        else if (maxValue > 7.00f) { maxValue = 8.00f; }
        else if (maxValue > 6.00f) { maxValue = 7.00f; }
        else if (maxValue > 5.00f) { maxValue = 6.00f; }
        else if (maxValue > 4.00f) { maxValue = 5.00f; }
        else if (maxValue > 3.50f) { maxValue = 4.00f; }
        else if (maxValue > 3.00f) { maxValue = 3.50f; }
        else if (maxValue > 2.50f) { maxValue = 3.00f; }
        else if (maxValue > 2.00f) { maxValue = 2.50f; }
        else if (maxValue > 1.75f) { maxValue = 2.00f; }
        else if (maxValue > 1.50f) { maxValue = 1.75f; }
        else if (maxValue > 1.25f) { maxValue = 1.50f; }
        else if (maxValue > 1.00f) { maxValue = 1.25f; }
        else                       { maxValue = 1.00f; }

        Round round = new Round();

        if      (maxValue == 10.0f) { round.numOfGridLines = 10; }
        else if (maxValue == 9.00f) { round.numOfGridLines =  9; }
        else if (maxValue == 8.00f) { round.numOfGridLines =  8; }
        else if (maxValue == 7.00f) { round.numOfGridLines =  7; }
        else if (maxValue == 6.00f) { round.numOfGridLines =  6; }
        else if (maxValue == 5.00f) { round.numOfGridLines =  5; }
        else if (maxValue == 4.00f) { round.numOfGridLines =  8; }
        else if (maxValue == 3.50f) { round.numOfGridLines =  7; }
        else if (maxValue == 3.00f) { round.numOfGridLines =  6; }
        else if (maxValue == 2.50f) { round.numOfGridLines =  5; }
        else if (maxValue == 2.00f) { round.numOfGridLines =  8; }
        else if (maxValue == 1.75f) { round.numOfGridLines =  7; }
        else if (maxValue == 1.50f) { round.numOfGridLines =  6; }
        else if (maxValue == 1.25f) { round.numOfGridLines =  5; }
        else if (maxValue == 1.00f) { round.numOfGridLines = 10; }

        round.maxValue = maxValue * ((float) Math.pow(10, maxExponent));
        float step = round.maxValue / round.numOfGridLines;
        float temp = round.maxValue;
        round.numOfGridLines = 0;
        while (true) {
            round.numOfGridLines++;
            temp -= step;
            if (temp <= minValue) {
                round.minValue = temp;
                break;
            }
        }

        return round;
    }


    private float[] mean(List<Point> points) {
        float[] _mean = new float[2];
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            _mean[0] += point.x;
            _mean[1] += point.y;
        }
        _mean[0] /= points.size() - 1;
        _mean[1] /= points.size() - 1;
        return _mean;
    }


    private float covar(List<Point> points) {
        float covariance = 0f;
        float[] _mean = mean(points);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            covariance += (point.x - _mean[0]) * (point.y - _mean[1]);
        }
        return (covariance / (points.size() - 1));
    }


    /**
     * devsq() returns the sum of squares of deviations.
     *
     */
    private float devsq(List<Point> points) {
        float _devsq = 0f;
        float[] _mean = mean(points);
        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            _devsq += Math.pow((point.x - _mean[0]), 2);
        }
        return _devsq;
    }


    /**
     *  Sets xMin and xMax for the X axis and the number of X grid lines.
     *
     *  @param xMin for the X axis.
     *  @param xMax for the X axis.
     *  @param xAxisGridLines the number of X axis grid lines.
     */
    public void setXAxisMinMax(float xMin, float xMax, int xAxisGridLines) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.xAxisGridLines = xAxisGridLines;
    }


    /**
     *  Sets yMin and yMax for the Y axis and the number of Y grid lines.
     *
     *  @param yMin for the Y axis.
     *  @param yMax for the Y axis.
     *  @param yAxisGridLines the number of Y axis grid lines.
     */
    public void setYAxisMinMax(float yMin, float yMax, int yAxisGridLines) {
        this.yMin = yMin;
        this.yMax = yMax;
        this.yAxisGridLines = yAxisGridLines;
    }

}   // End of Chart.java
