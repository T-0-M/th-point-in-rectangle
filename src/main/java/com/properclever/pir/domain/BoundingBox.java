package com.properclever.pir.domain;

import java.util.List;
import java.util.ArrayList;

public record BoundingBox(Point minPoint, Point maxPoint) {

    public BoundingBox {
        if (minPoint == null || maxPoint == null) {
            throw new IllegalArgumentException("Points cannot be null");
        }
    }

    /**
     * Returns the number of dimensions:
     * 0 for a point, 1 for a line, and 2 for a rectangle.
     */
    public int dimensions() {
        boolean sameX = Double.compare(minPoint.x(), maxPoint.x()) == 0;
        boolean sameY = Double.compare(minPoint.y(), maxPoint.y()) == 0;
        if (sameX && sameY) {
            return 0; // point
        } else if (sameX || sameY) {
            return 1; // line
        } else {
            return 2; // rectangle
        }
    }

    /**
     * Returns the significant points for this bounding box.
     * - For 0 dimensions, returns a single point.
     * - For 1 dimension, returns the two endpoints.
     * - For 2 dimensions, returns the four corners.
     */
    public List<Point> getBoundingPoints() {
        List<Point> points = new ArrayList<>();
        switch (dimensions()) {
            case 0 -> points.add(minPoint);
            case 1 -> {
                points.add(minPoint);
                points.add(maxPoint);
            }
            case 2 -> {
                points.add(minPoint);
                points.add(new Point(minPoint.x(), maxPoint.y()));
                points.add(maxPoint);
                points.add(new Point(maxPoint.x(), minPoint.y()));
            }
        }
        return points;
    }
}