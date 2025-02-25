package com.properclever.pir.util;

import com.properclever.pir.solution.BasicSolution;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;


public class TestBasicSolution {
    @ParameterizedTest(name = "{index}: {3}")
    @DisplayName("Testing our basic cases")
    @MethodSource("argsForTestBasicSolution")
    public void testBasicSolution(String shape, String point, boolean expectedResult, String desc) {
        System.out.println("\n" + desc);
        boolean result = new BasicSolution(shape, point).solve();
        Assertions.assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> argsForTestBasicSolution() {
        return Stream.of(
                Arguments.of("[[1,1], [2,7], [10,7], [9,1]]", "[8,2]", false, "Parallelogram"),
                Arguments.of("[[-1,-1], [-1,1], [1,1], [1,-1], [-1,-1]]", "[0,0]", true, "Orthogonal Square"),
                Arguments.of("[[1,1], [10,7], [10,1], [1,7]]", "[2,6]", false, "Bowtie"),
                Arguments.of("[[1,1], [1,3], [3,3], [5,3], [5,2], [5,1]]", "[4,2]", true, "Orthogonal Rectangle"),
                Arguments.of("[[0,0], [3,0], [3,10], [0,10]]", "[2,10]", false, "Point not strictly inside")
        );
    }
}
