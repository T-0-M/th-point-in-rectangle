package com.properclever.pir.util;

import com.properclever.pir.solution.GeneralSolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


public class TestGeneralSolution {
    @ParameterizedTest(name = "{index}: {3}")
    @DisplayName("Testing our (still) basic cases")
    @MethodSource("argsForTestGeneralSolutionWithBasicCases")
    public void testGeneralSolutionWithBasicCases(String shape, String point, boolean expectedResult, String desc) {
        System.out.println("\n" + desc);
        boolean result = new GeneralSolution(shape, point).solve();
        Assertions.assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> argsForTestGeneralSolutionWithBasicCases() {
        return Stream.of(
                Arguments.of("[[1,1], [2,7], [10,7], [9,1]]", "[8,2]", false, "Parallelogram"),
                Arguments.of("[[-1,-1], [-1,1], [1,1], [1,-1], [-1,-1]]", "[0,0]", true, "Orthogonal Square"),
                Arguments.of("[[1,1], [10,7], [10,1], [1,7]]", "[2,6]", false, "Bowtie"),
                Arguments.of("[[1,1], [1,3], [3,3], [5,3], [5,2], [5,1]]", "[4,2]", true, "Orthogonal Rectangle"),
                Arguments.of("[[0,0], [3,0], [3,10], [0,10]]", "[2,10]", false, "Point not strictly inside")
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @DisplayName("Testing extended cases")
    @MethodSource("argsForTestGeneralSolutionWithExtendedCases")
    public void testGeneralSolutionWithExtendedCases(String shape, String point, boolean expectedResult, String desc) {
        System.out.println("\n" + desc);
        boolean result = new GeneralSolution(shape, point).solve();
        Assertions.assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> argsForTestGeneralSolutionWithExtendedCases() {
        return Stream.of(
                Arguments.of("[[0,4], [4,7], [7,3], [3,0]]", "[2,5]", true, "Rotated square and inside"),
                Arguments.of("[[0,4], [4,7], [7,3], [3,0]]", "[1,2]", false, "Rotated square but outside"),
                Arguments.of("[[0,4], [4,7], [7,3], [3,0]]", "[3,0]", false, "Rotated square but on edge"),
                Arguments.of("[[0,4], [8,10], [11,6], [3,0]]", "[5,5]", true, "Rotated rectangle and inside"),
                Arguments.of("[[0,4], [8,10], [11,6], [3,0]]", "[7,2]", false, "Rotated rectangle but outside"),
                Arguments.of("[[0,4], [8,10], [11,6], [3,0]]", "[8,10]", false, "Rotated rectangle but on edge"),
                Arguments.of("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[5,5]", true, "Rotated rectangle w/extra and inside"),
                Arguments.of("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[7,2]", false, "Rotated rectangle w/extra but outside"),
                Arguments.of("[[0,4], [4,7], [8,10], [11,6], [7,3], [3,0]]", "[11,6]", false, "Rotated rectangle w/extra but on edge"),
                Arguments.of("[[3.5, 1.2], [1.9, 3.8], [4.0, 7.9], [8.1, 6.1], [7.4, 2.5]]", "[5,5]", false, "Some pentagon"),
                Arguments.of("[[2,1], [4,1], [5,3], [4,5], [2,5], [1,3]]", "[3,3]", false, "Some hexagon")
        );
    }
}
