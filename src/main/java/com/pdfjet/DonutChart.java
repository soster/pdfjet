/**
 *  DonutChart.java
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

public class DonutChart {
    Font f1;
    Font f2;
    Float xc;
    Float yc;
    Float r1;
    Float r2;
    List<Slice> slices;
    boolean isDonutChart = true;
    
    public DonutChart(Font f1, Font f2, boolean isDonutChart) {
        this.f1 = f1;
        this.f2 = f2;
        this.isDonutChart = isDonutChart;
        this.slices = new ArrayList<Slice>();
    }

    public void setLocation(Float xc, Float yc) {
        this.xc = xc;
        this.yc = yc;
    }

    public void setR1AndR2(Float r1, Float r2) {
        this.r1 = r1;
        this.r2 = r2;
    }

    public void addSlice(Slice slice) {
        this.slices.add(slice);
    }

    private List<Float[]> getControlPoints(
            Float xc, Float yc,
            Float x0, Float y0,
            Float x3, Float y3) {
        List<Float[]> points = new ArrayList<Float[]>();

        Float ax = x0 - xc;
        Float ay = y0 - yc;
        Float bx = x3 - xc;
        Float by = y3 - yc;
        Float q1 = ax*ax + ay*ay;
        Float q2 = q1 + ax*bx + ay*by;
        Float k2 = 4f/3f * (((float) Math.sqrt(2f*q1*q2)) - q2) / (ax*by - ay*bx);

        // Control points coordinates
        Float x1 = xc + ax - k2*ay;
        Float y1 = yc + ay + k2*ax;
        Float x2 = xc + bx + k2*by;
        Float y2 = yc + by - k2*bx;

        points.add(new Float[] {x0, y0});
        points.add(new Float[] {x1, y1});
        points.add(new Float[] {x2, y2});
        points.add(new Float[] {x3, y3});

        return points;
    }

    private Float[] getPoint(Float xc, Float yc, Float radius, Float angle) {
        Float x = xc + radius*((float) Math.cos(angle*Math.PI/180.0));
        Float y = yc + radius*((float) Math.sin(angle*Math.PI/180.0));
        return new Float[] {x, y};
    }

    private float drawSlice(
            Page page,
            int fillColor,
            Float xc, Float yc,
            Float r1, Float r2,     // r1 > r2
            Float a1, Float a2) {   // a1 > a2
        page.setBrushColor(fillColor);

        Float angle1 = a1 - 90f;
        Float angle2 = a2 - 90f;

        List<Float[]> points1 = new ArrayList<Float[]>();
        List<Float[]> points2 = new ArrayList<Float[]>();
        while (true) {
            if ((angle2 - angle1) <= 90f) {
                Float[] p0 = getPoint(xc, yc, r1, angle1);          // Start point
                Float[] p3 = getPoint(xc, yc, r1, angle2);          // End point
                points1.addAll(getControlPoints(xc, yc, p0[0], p0[1], p3[0], p3[1]));
                p0 = getPoint(xc, yc, r2, angle1);                  // Start point
                p3 = getPoint(xc, yc, r2, angle2);                  // End point
                points2.addAll(getControlPoints(xc, yc, p0[0], p0[1], p3[0], p3[1]));
                break;
            } else {
                Float[] p0 = getPoint(xc, yc, r1, angle1);
                Float[] p3 = getPoint(xc, yc, r1, angle1 + 90f);
                points1.addAll(getControlPoints(xc, yc, p0[0], p0[1], p3[0], p3[1]));
                p0 = getPoint(xc, yc, r2, angle1);
                p3 = getPoint(xc, yc, r2, angle1 + 90f);
                points2.addAll(getControlPoints(xc, yc, p0[0], p0[1], p3[0], p3[1]));
                angle1 += 90f;
            }
        }
        Collections.reverse(points2);

        page.moveTo(points1.get(0)[0], points1.get(0)[1]);
        for (int i = 0; i <= (points1.size() - 4); i += 4) {
            page.curveTo(
                    points1.get(i + 1)[0], points1.get(i + 1)[1],
                    points1.get(i + 2)[0], points1.get(i + 2)[1],
                    points1.get(i + 3)[0], points1.get(i + 3)[1]);
        }
        page.lineTo(points2.get(0)[0], points2.get(0)[1]);
        for (int i = 0; i <= (points2.size() - 4); i += 4) {
            page.curveTo(
                    points2.get(i + 1)[0], points2.get(i + 1)[1],
                    points2.get(i + 2)[0], points2.get(i + 2)[1],
                    points2.get(i + 3)[0], points2.get(i + 3)[1]);
        }
        page.fillPath();

        return a2;
    }

    public void drawOn(Page page) throws Exception {
        float angle = 0f;
        for (Slice slice : slices) {
            angle = drawSlice(
                    page, slice.color,
                    xc, yc,
                    r1, r2,
                    angle, angle + slice.angle);
        }
    }
}
