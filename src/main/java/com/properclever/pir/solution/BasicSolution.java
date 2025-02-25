package com.properclever.pir.solution;

import com.properclever.pir.domain.OrthogonalRectangle;
import com.properclever.pir.domain.Shape;


public class BasicSolution extends AbstractSolution {

    public BasicSolution(String shapeStr, String pointStr) {
        super(shapeStr, pointStr);
    }

    @Override
    protected Boolean doSolve() {
        Shape shape;
        try {
            // validity of OrthogonalRectangle is determined in its constructor
            shape = new OrthogonalRectangle(inputPoints);
        } catch (IllegalArgumentException _) {
            // inputPoints were not a valid OrthogonalRectangle
            return false;
        }
        // we have a valid OrthogonalRectangle!
        // check if the test point (parsed in the base class) lies strictly inside the rectangle.
        return shape.contains(testPoint);
    }
}