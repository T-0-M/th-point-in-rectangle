package com.properclever.pir.util;

import com.properclever.pir.domain.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EdgeCollapser {

    public static final double TOLERANCE = 1e-9;
    /**
     * Attempt to collapse collinear segments from the given list of points,
     * ensuring no backtracking and only right angles (or continued collinearity).
     *
     * Returns an Optional containing the collapsed list of points if successful,
     * or Optional.empty() if a backtrack or non-90-degree turn is encountered.
     */
    public static Optional<List<Point>> collapseAndCheckRightAngles(List<Point> points) {
        if (points == null || points.isEmpty()) {
            return Optional.empty();
        }

        // Ensure the shape is "closed" by appending the first point if the last is different
        List<Point> adjusted;
        if (!points.getFirst().equals(points.getLast())) {
            adjusted = new ArrayList<>(points);
            adjusted.add(points.getFirst());
        } else {
            adjusted = points;
        }
        EdgeAccumulator acc = new EdgeAccumulator();

        try {
   
            adjusted.forEach(acc::accept); // accept() might throw InvalidShapeException

            // If we never threw, shape is valid
            return Optional.of(acc.finish());
        } catch (InvalidShapeException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * A custom exception used to short-circuit the stream when invalid geometry is found.
     */
    private static class InvalidShapeException extends RuntimeException {
        InvalidShapeException(String message) {
            super(message);
        }
    }

    private static class EdgeAccumulator {
        private final List<Point> collapsed = new ArrayList<>();

        // We'll keep track of the "current direction" as a 2D vector
        // from the most recent corner to the current point.
        private double directionX = 0.0;
        private double directionY = 0.0;

        // We need at least one point to establish direction.
        // The second point will finalize direction, and so on.

        void accept(Point p) {
            if (collapsed.isEmpty()) {
                // First point
                collapsed.add(p);
            } else if (collapsed.size() == 1) {
                // Second point => initialize direction
                Point prev = collapsed.getFirst();
                directionX = p.x() - prev.x();
                directionY = p.y() - prev.y();
                if (isZeroLength(directionX, directionY)) {
                    // Two identical points => no direction
                    throw new InvalidShapeException("Zero-length segment encountered.");
                }
                collapsed.add(p);
            } else {
                // We have an established direction and at least 2 points
                Point lastCorner = collapsed.getLast();

                double newX = p.x() - lastCorner.x();
                double newY = p.y() - lastCorner.y();

                // Check geometry between (directionX, directionX) and (newX, newY)
                if (areCollinearAndSameDirection(directionX, directionY, newX, newY)) {
                    // in same direction so just swap in the new point to extend the line
                    // don't need to update direction vector
                    collapsed.removeLast();
                    collapsed.add(p);
                }
                else {
                    // not collinear ... either 90 degrees or bad angle
                    double dot = directionX * newX + directionY * newY;
                    double cross = directionX * newY - directionY * newX;

                    if (Math.abs(cross) < TOLERANCE) {
                        // cross == 0 ... collinear (forward or backtrack)
                        if (dot < 0) {
                            throw new InvalidShapeException("Backtracking detected.");
                        } else {
                            // dot > 0 would have been caught in areCollinearAndSameDirection
                            // dot == 0 shouldn't happen if lengths are not zero
                            throw new InvalidShapeException("Unexpected!");
                        }
                    } else {
                        // cross != 0 ... there's a non-zero angle. Check if it's 90 deg: dot = 0
                        if (Math.abs(dot) < TOLERANCE) {
                            // right angle, update direction to the new segment
                            collapsed.add(p);
                            directionX = newX;
                            directionY = newY;
                        } else {
                            // Not 90 deg, invalid
                            throw new InvalidShapeException("Angle not 90 degrees.");
                        }
                    }
                }
            }
        }

        List<Point> finish() {
            return collapsed;
        }

        /**
         * Check if two vectors (dx1,dy1) and (dx2,dy2) are collinear and point in "roughly"
         * the same direction (dot > 0, cross ~ 0).
         */
        private boolean areCollinearAndSameDirection(double dx1, double dy1,
                                                     double dx2, double dy2) {
            double cross = dx1 * dy2 - dy1 * dx2;
            if (Math.abs(cross) > TOLERANCE) {
                return false; // not collinear
            }
            // dot > 0: same direction, dot < 0: reversed
            double dot = dx1 * dx2 + dy1 * dy2;
            return dot > TOLERANCE;
        }

        /** Checks if a direction vector is effectively zero length. */
        private boolean isZeroLength(double dx, double dy) {
            return Math.abs(dx) < TOLERANCE && Math.abs(dy) < TOLERANCE;
        }
    }
}