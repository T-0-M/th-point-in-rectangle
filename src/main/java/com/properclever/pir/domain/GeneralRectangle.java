package com.properclever.pir.domain;

import com.properclever.pir.util.GeneralGeomTools;

import java.util.List;
import java.util.Optional;

import static com.properclever.pir.util.EdgeCollapser.collapseAndCheckRightAngles;

public class GeneralRectangle implements Shape {
    public final Point v0;
    public final Point v1;
    public final Point v2;
    public final Point v3;
    private final GeneralGeomTools tools = new GeneralGeomTools();

    public GeneralRectangle(List<Point> points) {
        Optional<List<Point>> checkedRectanglePoints = collapseAndCheckRightAngles(points);
        if (checkedRectanglePoints.isEmpty()) {
            // if the shape is not a valid rectangle, return false.
            throw new IllegalArgumentException("Points do not form a valid general rectangle");
        }
        List<Point> vertices = tools.extractFourCorners(checkedRectanglePoints.get());
        v0 = vertices.get(0);
        v1 = vertices.get(1);
        v2 = vertices.get(2);
        v3 = vertices.get(3);
    }

    @Override
    public List<Point> getCoordinates() {
        return List.of(v0, v1, v2, v3, v0);
    }

    @Override
    public boolean contains(Point testPoint) {
        return tools.checkPointInRectangle(this, testPoint);
    }
}