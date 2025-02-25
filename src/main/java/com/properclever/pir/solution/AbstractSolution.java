package com.properclever.pir.solution;

import com.properclever.pir.util.PointStringParser;
import com.properclever.pir.domain.Point;
import java.util.List;

/**
 * This class handles the incidental task of converting input strings into the appropriate
 * domain objects (i.e. a list of Points for the shape and a single Point for the test).
 * The string parsing itself is not central to the problem's logic, it's just a means of getting
 * the data into our model. By performing this work in the base class, we keep the subclasses
 * focused solely on the core logic of determining whether the point lies inside the rectangle.
 * <p>
 * Subclasses must implement doSolve() to provide specific solution logic.
 */
public abstract class AbstractSolution implements Solvable<Boolean> {

    protected final List<Point> inputPoints;
    protected final Point testPoint;

    /**
     * Constructs an instance by parsing the provided input strings.
     *
     * @param shapeStr a string representing an array of coordinate pairs
     *                 (e.g. "[[1,1], [1,3], [3,3], [3,1]]")
     * @param pointStr a string representing a single coordinate pair
     *                 (e.g. "[2,2]")
     * @throws IllegalArgumentException if the input strings are invalid.
     */
    public AbstractSolution(final String shapeStr, final String pointStr) {
        this.inputPoints = PointStringParser.parsePointArray(shapeStr);
        this.testPoint = PointStringParser.parsePoint(pointStr);
    }

    /**
     * Executes the solution using the pre-configured inputs.
     * This final method guarantees a consistent entry point for all solution implementations
     * and delegates the actual problem-solving to the abstract {@code doSolve()} method.
     *
     * @return the Boolean result of solving the problem.
     */
    @Override
    public final Boolean solve() {
        return doSolve();
    }

    /**
     * Subclasses must implement this method to provide the specific logic for determining
     * whether the test point is inside the rectangle defined by the input points.
     *
     * @return the result of the solution.
     */
    protected abstract Boolean doSolve();
}