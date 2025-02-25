package com.properclever.pir.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnknownShape implements Shape {
    private final List<Point> coordinates;

    private BoundingBox cachedBoundingBox;

    public UnknownShape(List<Point> points) {
        if (points == null || points.isEmpty()) {
            throw new IllegalArgumentException("Points list cannot be null or empty");
        }
        // defensive copy
        this.coordinates = new ArrayList<>(points);
    }

    @Override
    public List<Point> getCoordinates() {
        return Collections.unmodifiableList(coordinates);
    }

    @Override
    public boolean contains(Point testPoint) {
        return false;
    }

    @Override
    public BoundingBox getBoundingBox() {
        if (cachedBoundingBox == null) {
            // use default impl from interface
            cachedBoundingBox = Shape.super.getBoundingBox();
        }
        return cachedBoundingBox;
    }
}