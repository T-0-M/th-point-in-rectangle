package com.properclever.pir.util;

import com.properclever.pir.domain.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class TestPointStringParser {

    // Tests for parsePoint()

    @Test
    public void testParsePoint_valid() {
        // Valid input with extra spaces.
        Point point = PointStringParser.parsePoint("[ 3.5 , -2.0 ]");
        assertEquals(3.5, point.x(), 0.0001);
        assertEquals(-2.0, point.y(), 0.0001);
    }

    @Test
    public void testParsePoint_invalidFormat_noBrackets() {
        // Input missing the square brackets.
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePoint("3.5, -2.0")
        );
        assertTrue(exception.getMessage().contains("Input does not match expected point format"));
    }

    @Test
    public void testParsePoint_invalidFormat_letters() {
        // Input with non-numeric characters.
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePoint("[a, b]")
        );
        assertTrue(exception.getMessage().contains("Input does not match expected point format"));
    }

    @Test
    public void testParsePoint_nullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePoint(null)
        );
        assertTrue(exception.getMessage().contains("Input string is null or empty"));
    }

    @Test
    public void testParsePoint_emptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePoint("   ")
        );
        assertTrue(exception.getMessage().contains("Input string is null or empty"));
    }

    // Tests for parsePointArray()

    @Test
    public void testParsePointArray_valid() {
        String input = "[[0,0], [1,1], [2.5, -3.5]]";
        List<Point> points = PointStringParser.parsePointArray(input);
        assertEquals(3, points.size());

        // Verify each point
        Point p0 = points.getFirst();
        assertEquals(0.0, p0.x(), 0.0001);
        assertEquals(0.0, p0.y(), 0.0001);

        Point p1 = points.get(1);
        assertEquals(1.0, p1.x(), 0.0001);
        assertEquals(1.0, p1.y(), 0.0001);

        Point p2 = points.get(2);
        assertEquals(2.5, p2.x(), 0.0001);
        assertEquals(-3.5, p2.y(), 0.0001);
    }

    @Test
    public void testParsePointArray_invalidSeparator() {
        // Missing comma between coordinate pairs
        String input = "[[0,0] [1,1]]";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray(input)
        );
        assertTrue(exception.getMessage().contains("Invalid separator between coordinate pairs"));
    }

    @Test
    public void testParsePointArray_pointInsteadOfList() {
        // Giving a point instead of an array of points
        String input = "[0,0]";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray(input)
        );
        assertTrue(exception.getMessage().contains("No valid list found"));
    }

    @Test
    public void testParsePointArray_noValidPairs() {
        // Empty array (no coordinate pairs)
        String input = "[]";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray(input)
        );
        assertTrue(exception.getMessage().contains("No valid coordinate pairs found"));
    }

    @Test
    public void testParsePointArray_trailingCharacters() {
        // Extra characters after valid pairs
        String input = "[[0,0], [1,1]] extra";
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray(input)
        );
        // The error message could be from the outer bracket check or the trailing characters check
        assertTrue(
                exception.getMessage().contains("Input must start with '[' and end with ']'") ||
                        exception.getMessage().contains("Extra characters found after the last coordinate pair")
        );
    }


    @Test
    public void testParsePointArray_nullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray(null)
        );
        assertTrue(exception.getMessage().contains("Input string is null or empty"));
    }

    @Test
    public void testParsePointArray_emptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                PointStringParser.parsePointArray("   ")
        );
        assertTrue(exception.getMessage().contains("Input string is null or empty"));
    }
}