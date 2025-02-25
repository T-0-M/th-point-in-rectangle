package com.properclever.pir.domain;

import java.util.List;

public interface Shape {
    List<Point> getCoordinates();
    boolean contains(Point testPoint);

    default BoundingBox getBoundingBox() {
        List<Point> points = getCoordinates();
        if (points == null || points.isEmpty()) {
            throw new IllegalStateException("Shape must have at least one point");
        }
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;

        for (Point p : points) {
            if (p == null) {
                throw new IllegalArgumentException("Point cannot be null");
            }
            minX = Math.min(minX, p.x());
            minY = Math.min(minY, p.y());
            maxX = Math.max(maxX, p.x());
            maxY = Math.max(maxY, p.y());
        }
        return new BoundingBox(new Point(minX, minY), new Point(maxX, maxY));
    }
}