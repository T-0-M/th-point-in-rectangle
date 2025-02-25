package com.properclever.pir.util;

import com.properclever.pir.domain.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PointStringParser {

    // Reusable regex pattern for matching a single coordinate pair.
    private static final Pattern COORDINATE_PAIR_PATTERN =
            Pattern.compile("\\[\\s*([-+]?\\d*\\.?\\d+)\\s*,\\s*([-+]?\\d*\\.?\\d+)\\s*]");

    /**
     * Parses a string representing an array of coordinate pairs (e.g. [[-1,-1], [-1,1], [1,1], [1,-1]])
     * into a list of Point objects.
     *
     * @param input the string to parse.
     * @return a List of Point objects in the order they appear.
     * @throws IllegalArgumentException if the input does not match the expected format.
     */
    public static List<Point> parsePointArray(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string is null or empty.");
        }
        String trimmed = input.trim();
        if (!trimmed.startsWith("[") || !trimmed.endsWith("]")) {
            throw new IllegalArgumentException("Input must start with '[' and end with ']'.");
        }

        // Remove the outermost brackets.
        String inner = trimmed.substring(1, trimmed.length() - 1).trim();
        if (inner.isEmpty()) {
            throw new IllegalArgumentException("No valid coordinate pairs found.");
        }
        if (!inner.startsWith("[") || !inner.endsWith("]")) {
            throw new IllegalArgumentException("No valid list found.");
        }
        Matcher matcher = COORDINATE_PAIR_PATTERN.matcher(inner);
        List<Point> points = new ArrayList<>();
        int lastIndex = 0;
        while (matcher.find()) {
            // For coordinate pairs after the first, the separator must be a comma.
            if (lastIndex != 0) {
                String gap = inner.substring(lastIndex, matcher.start()).trim();
                if (!gap.equals(",")) {
                    throw new IllegalArgumentException("Invalid separator between coordinate pairs: '" + gap + "'");
                }
            }
            points.add(parsePoint(matcher.group(0)));
            lastIndex = matcher.end();
        }
        // Check for any trailing text after the last valid pair.
        String trailing = inner.substring(lastIndex).trim();
        if (!trailing.isEmpty()) {
            throw new IllegalArgumentException("Extra characters found after the last coordinate pair.");
        }
        String result = points.stream()
                .map(Point::toString)
                .collect(Collectors.joining(", "));
//        System.out.println("Parsed point array list: [" + result + "]");
        return points;
    }

    /**
     * Parses a string representing a single coordinate pair (e.g. [0,0] or [4.5, -8.7])
     * into a Point object.
     *
     * @param input the string to parse.
     * @return a Point object.
     * @throws IllegalArgumentException if the input does not match the expected format.
     */
    public static Point parsePoint(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new IllegalArgumentException("Input string is null or empty.");
        }
        String trimmed = input.trim();
        Matcher matcher = COORDINATE_PAIR_PATTERN.matcher(trimmed);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input does not match expected point format: " + input);
        }
        try {
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(2));
            Point result = new Point(x, y);
//            System.out.printf("Parsed point: [%s]%n", result);
            return result;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid number format in coordinate pair.", e);
        }
    }
}