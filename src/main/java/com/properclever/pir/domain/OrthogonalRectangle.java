package com.properclever.pir.domain;

import com.properclever.pir.util.OrthogonalGeomTools;

import java.util.List;

public class OrthogonalRectangle implements Shape {
    public final Point topLeft;
    public final Point topRight;
    public final Point bottomRight;
    public final Point bottomLeft;
    private final OrthogonalGeomTools tools = new OrthogonalGeomTools();

    public OrthogonalRectangle(List<Point> points) {
        if (!tools.isValidRectangle(points)) {
            // if the shape is not a valid rectangle, return false immediately.
            throw new IllegalArgumentException("Points do not form a valid orthogonal rectangle");
        }
        // since we know the shape is an orthogonal rectangle, the bounding box must also be valid
        BoundingBox bbox = tools.getValidBoundingBox(points);

        // Assign rectangle corners from BoundingBox
        this.bottomLeft = bbox.minPoint();
        this.bottomRight = new Point(bbox.maxPoint().x(), bbox.minPoint().y());
        this.topRight = bbox.maxPoint();
        this.topLeft = new Point(bbox.minPoint().x(), bbox.maxPoint().y());
    }

    @Override
    public List<Point> getCoordinates() {
        return List.of(bottomLeft, topLeft, topRight, bottomRight, bottomLeft);
    }

    @Override
    public boolean contains(Point testPoint) {
        return tools.checkPointInRectangle(this, testPoint);
    }
}