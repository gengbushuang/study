package com;

import com.graph.bean.Point;

public class Util {

    public static float piecewiseResolve(Point[] points, float y) {
        float x = 0.0F;
        int index = -1;
        for (int i = 0; i < points.length - 1; i++) {
            if (Math.abs(y - points[i].getY()) < 1e-6) {
                return points[i].getX();
            }
            if (y >= points[i].getY() && points[i].getY() <= points[i + 1].getY()) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            return 1.0F;
        }

        float k = (points[index + 1].getY() - points[index].getY()) / (points[index + 1].getX() - points[index].getX());
        float b = points[index].getY() - k * points[index].getX();
        x = (y - b) / k;
        if (x < points[index].getX()) {
            return points[index].getX();
        }
        if (x > points[index + 1].getX()) {
            return points[index + 1].getX();
        }
        return x;
    }

}
