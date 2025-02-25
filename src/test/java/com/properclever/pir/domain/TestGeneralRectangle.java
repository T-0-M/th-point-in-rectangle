package com.properclever.pir.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneralRectangle {

    @Test
    public void testValidRectangleOrderedPoints() {
        // valid
        List<Point> points = List.of(new Point(0, 0), new Point(3, 0), new Point(3, 2), new Point(0, 2));
        GeneralRectangle rectangle = new GeneralRectangle(points);

        // check closed
        List<Point> coords = rectangle.getCoordinates();
        assertEquals(5, coords.size());
        assertEquals(coords.get(0), coords.get(4));
    }

    @Test
    public void testUnorderedPointsShouldBeInvalid() {
        // collapseAndCheckRightAngles() should detect
        List<Point> points = List.of(new Point(3, 2), new Point(0, 0), new Point(3, 0), new Point(0, 2));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GeneralRectangle(points));
        assertTrue(exception.getMessage().contains("Points do not form a valid general rectangle"));
    }

    @Test
    public void testInvalidRectangleNotEnoughPoints() {
        List<Point> points = List.of(new Point(0, 0), new Point(1, 1), new Point(2, 2));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GeneralRectangle(points));
        assertTrue(exception.getMessage().contains("Points do not form a valid general rectangle"));
    }

    @Test
    public void testInvalidRectangleCollinearPoints() {
        List<Point> points = List.of(new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(3, 0));
        Exception exception = assertThrows(IllegalArgumentException.class, () -> new GeneralRectangle(points));
        assertTrue(exception.getMessage().contains("Points do not form a valid general rectangle"));
    }

    @Test
    public void testContainsStrictInsideOnly() {
        // valid
        List<Point> points = List.of(new Point(0, 0), new Point(4, 0), new Point(4, 3), new Point(0, 3));
        GeneralRectangle rectangle = new GeneralRectangle(points);

        Point strictlyInside = new Point(2, 1.5);
        Point onPerimeter = new Point(0, 1);
        Point outside = new Point(-1, 1);

        // check only the strictly inside point is considered contained
        assertTrue(rectangle.contains(strictlyInside), "Point strictly inside should be contained.");
        assertFalse(rectangle.contains(onPerimeter), "Point on perimeter should not be contained.");
        assertFalse(rectangle.contains(outside), "Point outside should not be contained.");
    }
}