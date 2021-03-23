package com.pdfjet;

import java.util.*;


public class DonutChart {

	Font f1;
    Font f2;
	List<List<Point>> chartData;
	Float xc;
    Float yc;
    Float r1;
    Float r2;
	List<Float> angles;
	List<Integer> colors;
    boolean isDonutChart = true;
    
    public DonutChart(Font f1, Font f2, boolean isDonutChart) {
	    this.f1 = f1;
	    this.f2 = f2;
	    this.isDonutChart = true;
    }

    public void setLocation(Float xc, Float yc) {
        this.xc = xc;
        this.yc = yc;
    }

    public void setR1AndR2(Float r1, Float r2) {
        this.r1 = r1;
        this.r2 = r2;
        if (this.r1 < 1.0) {
            this.isDonutChart = false;
        }
    }
    
    private List<Point> getBezierCurvePoints(Float xc, Float yc, Float r, Float angle1, Float angle2) {
        angle1 *= -1.0f;
        angle2 *= -1.0f;

        // Start point coordinates
        Float x1 = xc + r*((float) (Math.cos(angle1)*(Math.PI/180.0)));
        Float y1 = yc + r*((float) (Math.sin(angle1)*(Math.PI/180.0)));
        // End point coordinates
        Float x4 = xc + r*((float) (Math.cos(angle2)*(Math.PI/180.0)));
        Float y4 = yc + r*((float) (Math.sin(angle2)*(Math.PI/180.0)));
    
        Float ax = x1 - xc;
        Float ay = y1 - yc;
        Float bx = x4 - xc;
        Float by = y4 - yc;
        Float q1 = ax*ax + ay*ay;
        Float q2 = q1 + ax*bx + ay*by;
    
        Float k2 = 4f/3f * (((float) Math.sqrt(2f*q1*q2)) - q2) / (ax*by - ay*bx);
    
        Float x2 = xc + ax - k2*ay;
        Float y2 = yc + ay + k2*ax;
        Float x3 = xc + bx + k2*by;
        Float y3 = yc + by - k2*bx;
    
        List<Point> list = new ArrayList<Point>();
        list.add(new Point(x1, y1));
        list.add(new Point(x2, y2, Point.CONTROL_POINT));
        list.add(new Point(x3, y3, Point.CONTROL_POINT));
        list.add(new Point(x4, y4));
    
        return list;
    }

    // GetArcPoints calculates a list of points for a given arc of a circle
    // @param xc the x-coordinate of the circle's centre.
    // @param yc the y-coordinate of the circle's centre
    // @param r the radius of the circle.
    // @param angle1 the start angle of the arc in degrees.
    // @param angle2 the end angle of the arc in degrees.
    // @param includeOrigin whether the origin should be included in the list (thus creating a pie shape).
    private List<Point> getArcPoints(Float xc, Float yc, Float r, Float angle1, Float angle2, boolean includeOrigin) {
        List<Point> list = new ArrayList<Point>();

        if (includeOrigin) {
            list.add(new Point(xc, yc));
        }

        float startAngle;
        float endAngle;
        if (angle1 <= angle2) {
            startAngle = angle1;
            endAngle = angle1 + 90;
            while (endAngle < angle2) {
                list.addAll(getBezierCurvePoints(xc, yc, r, startAngle, endAngle));
                startAngle += 90;
                endAngle += 90;
            }
            endAngle -= 90;
            list.addAll(getBezierCurvePoints(xc, yc, r, endAngle, angle2));
        }
        else {
            startAngle = angle1;
            endAngle = angle1 - 90;
            while (endAngle > angle2) {
                list.addAll(getBezierCurvePoints(xc, yc, r, startAngle, endAngle));
                startAngle -= 90;
                endAngle -= 90;
            }
            endAngle += 90;
            list.addAll(getBezierCurvePoints(xc, yc, r, endAngle, angle2));
        }

        return list;
    }

    // GetDonutPoints calculates a list of points for a given donut sector of a circle.
    // @param xc the x-coordinate of the circle's centre.
    // @param yc the y-coordinate of the circle's centre.
    // @param r1 the inner radius of the donut.
    // @param r2 the outer radius of the donut.
    // @param angle1 the start angle of the donut sector in degrees.
    // @param angle2 the end angle of the donut sector in degrees.
    private List<Point> getDonutPoints(Float xc, Float yc, Float r1, Float r2, Float angle1, Float angle2) {
        List<Point> list = new ArrayList<Point>();
        list.addAll(getArcPoints(xc, yc, r1, angle1, angle2, false));
        list.addAll(getArcPoints(xc, yc, r2, angle2, angle1, false));
        return list;
    }

    // AddSector -- TODO:
    public void addSector(Float angle, int color) {
        this.angles.add(angle);
        this.colors.add(color);
    }

    // DrawOn draws donut chart on the specified page.
    public void DrawOn(Page page) throws Exception {
        Float startAngle = 0f;
        Float endAngle = 0f;
        int lastColorIndex = 0;
        for (int i = 0; i < angles.size(); i++) {
            endAngle = startAngle + angles.get(i);
            List<Point> list = new ArrayList<Point>();
            if (isDonutChart) {
                list.addAll(getDonutPoints(xc, yc, r1, r2, startAngle, endAngle));
            }
            else {
                list.addAll(getArcPoints(xc, yc, r2, startAngle, endAngle, true));
            }
            // for (Point point : list) {
            // 	point.drawOn(page);
            // }
            page.setBrushColor(colors.get(i));
            page.drawPath(list, Operation.FILL);
            startAngle = endAngle;
            lastColorIndex = i;
        }

        if (endAngle < 360f) {
            endAngle = 360f;
            List<Point> list = new ArrayList<Point>();
            if (isDonutChart) {
                list.addAll(getDonutPoints(xc, yc, r1, r2, startAngle, endAngle));
            }
            else {
                list.addAll(getArcPoints(xc, yc, r2, startAngle, endAngle, true));
            }
            // for (Point point : list) {
            // 	point.drawOn(page);
            // }
            page.setBrushColor(colors.get(lastColorIndex + 1));
            page.drawPath(list, Operation.FILL);
        }
    }

}
