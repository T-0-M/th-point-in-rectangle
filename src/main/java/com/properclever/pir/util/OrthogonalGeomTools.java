package com.properclever.pir.util;

import com.properclever.pir.domain.*;

import java.util.*;

public class OrthogonalGeomTools implements PointInRectangleCheckable {

    private static boolean isCyclicRotation(List<Point> a, List<Point> b) {
        // check if list 'a' is a cyclic rotation of list 'b'
        if (a.size() != b.size()) {
            return false;
        }
        int n = a.size();
        for (int shift = 0; shift < n; shift++) {
            boolean match = true;
            for (int i = 0; i < n; i++) {
                if (!a.get(i).equals(b.get((i + shift) % n))) {
                    match = false;
                    break;
                }
            }
            if (match) {
                return true;
            }
        }
        return false;
    }

    private boolean validateUniquePoints(List<Point> points) {
        // check that the points list is non-null and contains at least 4 unique points
        if (points == null || points.isEmpty()) {
            System.out.println("Invalid rectangle: Points list is null or empty.");
            return false;
        }
        Set<Point> uniquePoints = new HashSet<>(points);
        if (uniquePoints.size() < 4) {
            System.out.println("Invalid rectangle: Only " + uniquePoints.size() + " unique point(s).");
            return false;
        }
        return true;
    }

    public BoundingBox getValidBoundingBox(List<Point> points) {
        // get (an orthogonal) bounding box for the points; return null if the shape is degenerate
        BoundingBox bbox = new UnknownShape(points).getBoundingBox();
        if (bbox.dimensions() != 2) {
            System.out.println("Invalid rectangle: Shape coordinate(s) in a line or at one point.");
            return null;
        }
        return bbox;
    }

    private List<Point> createClosedPoints(List<Point> points) {
        // ensure we have a closed list, add extra point if necessary,
        // so we can analyse all lines
        List<Point> closedPoints = new ArrayList<>(points);
        if (!points.getFirst().equals(points.getLast())) {
            closedPoints.add(points.getFirst());
        }
        return closedPoints;
    }

    private boolean validateSegments(List<Point> closedPoints, BoundingBox bbox) {
        // check each segment is axis-aligned and every point is on the bounding box
        for (int i = 0; i < closedPoints.size() - 1; i++) {
            Point current = closedPoints.get(i);
            Point next = closedPoints.get(i + 1);
            double dx = next.x() - current.x();
            double dy = next.y() - current.y();
            if (dx != 0 && dy != 0) {
                System.out.println("Invalid rectangle: Found a segment that is not horizontal or vertical.");
                return false;
            }
            if (!(current.x() == bbox.minPoint().x() || current.x() == bbox.maxPoint().x() || current.y() == bbox.minPoint().y() || current.y() == bbox.maxPoint().y())) {
                System.out.println("Invalid rectangle: Found a point that's not on the bounding box.");
                return false;
            }
        }
        return true;
    }

    private List<Point> extractVertices(List<Point> closedPoints) {
        // collapse consecutive collinear segments to extract "turning"/corner vertices.
        // return null if backtracking is detected
        List<Point> vertices = new ArrayList<>();
        vertices.add(closedPoints.get(0));
        if (closedPoints.size() < 2) {
            return vertices;
        }
        double initialDx = closedPoints.get(1).x() - closedPoints.get(0).x();
        double initialDy = closedPoints.get(1).y() - closedPoints.get(0).y();
        String prevDir = (Math.abs(initialDx) > 0 ? "H" : "V");
        int prevSign = (prevDir.equals("H") ? (initialDx > 0 ? 1 : -1) : (initialDy > 0 ? 1 : -1));

        for (int i = 1; i < closedPoints.size() - 1; i++) {
            Point current = closedPoints.get(i);
            Point next = closedPoints.get(i + 1);
            double dx = next.x() - current.x();
            double dy = next.y() - current.y();
            String currDir = (Math.abs(dx) > 0 ? "H" : "V");
            int currSign = (currDir.equals("H") ? (dx > 0 ? 1 : -1) : (dy > 0 ? 1 : -1));

            if (currDir.equals(prevDir)) {
                // Detect backtracking.
                if (currSign != prevSign) {
                    System.out.printf("Invalid rectangle: Backtracking detected at point %s%n", current);
                    return null;
                }
            } else {
                if (!vertices.getLast().equals(current)) {
                    vertices.add(current);
                }
                prevDir = currDir;
                prevSign = currSign;
            }
        }
        // Remove duplicate if first and last vertex are the same.
        if (vertices.size() > 1 && vertices.getFirst().equals(vertices.getLast())) {
            vertices.removeLast();
        }
        return vertices;
    }

    private boolean validateVertexCount(List<Point> vertices) {
        // check that there are exactly 4 "turning" vertices
        if (vertices == null || vertices.size() != 4) {
            System.out.printf("Invalid rectangle: %d turning vertices.%n", vertices == null ? 0 : vertices.size());
            return false;
        }
        return true;
    }

    private boolean validateVertexOrder(List<Point> vertices, BoundingBox bbox) {
        // check vertices are arranged in a valid cyclic (clockwise or anticlockwise) order.
        List<Point> expectedPointsClockwise = bbox.getBoundingPoints();
        List<Point> expectedPointsAnticlockwise = new ArrayList<>(expectedPointsClockwise);
        Collections.reverse(expectedPointsAnticlockwise);
        boolean isCyclic = isCyclicRotation(vertices, expectedPointsClockwise) || isCyclicRotation(vertices, expectedPointsAnticlockwise);
        if (!isCyclic) {
            System.out.println("Invalid rectangle: vertices are not arranged in a valid clockwise/anticlockwise order.");
            return false;
        }
        return true;
    }

    public boolean isValidRectangle(List<Point> points) {
        // systematically apply rules to weed out invalid rectangles with early exits
        // 1. check there are at least 4 unique points
        if (!validateUniquePoints(points)) {
            return false;
        }
        // 2. get the bounding box of all the points
        BoundingBox bbox = getValidBoundingBox(points);
        if (bbox == null) {
            return false;
        }
        // 3. close the point loop if not already (last == first)
        List<Point> closedPoints = createClosedPoints(points);
        // 4. check all segments are horizontal/vertical and lie on bbox
        if (!validateSegments(closedPoints, bbox)) {
            return false;
        }
        // 5. detect mid-edge backtracking (return null) and collapse edges
        List<Point> vertices = extractVertices(closedPoints);
        // 6. check backtracking then check we now have 4 (corner) vertices
        if (vertices == null || !validateVertexCount(vertices)) {
            return false;
        }
        // 7. ensure corner vertices are in the correct order
        if (!validateVertexOrder(vertices, bbox)) {
            // bowtie!
            return false;
        }
        System.out.println("Shape is a valid orthogonal rectangle!");
        return true;
    }

    @Override
    public boolean checkPointInRectangle(Shape rectangle, Point testPoint) {
        OrthogonalRectangle orthRect = (OrthogonalRectangle) rectangle;
        double x = testPoint.x();
        double y = testPoint.y();
        // a point on the boundary not valid so must use strictly less-than/greater-than comparisons
        return x > orthRect.bottomLeft.x() && x < orthRect.bottomRight.x() && y > orthRect.bottomLeft.y() && y < orthRect.topLeft.y();
    }
}