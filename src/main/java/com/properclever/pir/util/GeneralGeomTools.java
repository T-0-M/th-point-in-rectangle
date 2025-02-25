package com.properclever.pir.util;

import com.properclever.pir.domain.Point;
import com.properclever.pir.domain.Shape;

import java.util.List;

public class GeneralGeomTools implements PointInRectangleCheckable {

    private static boolean isStrictlyLeft(Point a, Point b, Point p) {
        // cross product of B and P from A: (b-a) x (p-a)
        double cross = (b.x() - a.x()) * (p.y() - a.y()) - (b.y() - a.y()) * (p.x() - a.x());
        // if cross product is 0, the points are collinear
        // if < 0, it's on the wrong side of the line
        return cross > 0;
    }

    private static boolean isStrictlyRight(Point a, Point b, Point p) {
        // swap handedness, flip the vectors around (A swap B)
        return isStrictlyLeft(b, a, p);
    }

    public List<Point> extractFourCorners(List<Point> rectPoints) {
        // We expect a closed rectangle!
        // It will be 5 points if closed at a corner, or 6 if closed somewhere on an edge
        if (rectPoints.size() < 5 || rectPoints.size() > 6) {
            throw new IllegalArgumentException("Expected final shape size of 5 or 6, but got %d".formatted(rectPoints.size()));
        }

        // Basic check that it's closed: first == last
        Point first = rectPoints.getFirst();
        Point last = rectPoints.getLast();
        if (!first.equals(last)) {
            throw new IllegalArgumentException("Shape is not closed (first != last).");
        }

        // if we have 5 points: [C0, C1, C2, C3, C0]
        //                       --  --  --  --
        // corners are indices 0..3
        if (rectPoints.size() == 5) {
            return List.of(rectPoints.get(0), rectPoints.get(1), rectPoints.get(2), rectPoints.get(3));
        }

        // or if we have 6 points: [S, C1, C2, C3, C4, S]
        //                             --  --  --  --
        // corners are indices 1..4
        return List.of(rectPoints.get(1), rectPoints.get(2), rectPoints.get(3), rectPoints.get(4));
    }

    @Override
    public boolean checkPointInRectangle(Shape rectangle, Point testPoint) {
        // we will use our vector friend, the cross product, on each line of the rectangle
        // and the test point.

        List<Point> vertices = rectangle.getCoordinates();
        Point v0 = vertices.get(0);
        Point v1 = vertices.get(1);
        Point v2 = vertices.get(2);
        Point v3 = vertices.get(3);

        // Check that the point is on the same side of each edge using cross product
        // it depends whether the points are going clockwise or anticlockwise
        // as to whether we want the test point always on the left or the right.
        return (
                isStrictlyLeft(v0, v1, testPoint)
                        && isStrictlyLeft(v1, v2, testPoint)
                        && isStrictlyLeft(v2, v3, testPoint)
                        && isStrictlyLeft(v3, v0, testPoint)
        ) || (
                isStrictlyRight(v0, v1, testPoint)
                        && isStrictlyRight(v1, v2, testPoint)
                        && isStrictlyRight(v2, v3, testPoint)
                        && isStrictlyRight(v3, v0, testPoint)
        );
    }
}
