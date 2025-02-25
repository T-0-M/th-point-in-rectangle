package com.properclever.pir.solution;

import com.properclever.pir.domain.GeneralRectangle;
import com.properclever.pir.domain.Shape;


public class GeneralSolution extends AbstractSolution {

    public GeneralSolution(String shapeStr, String pointStr) {
        super(shapeStr, pointStr);
    }

    @Override
    protected Boolean doSolve() {
        Shape shape;
        try {
            // validity of GeneralRectangle is determined in its constructor
            shape = new GeneralRectangle(inputPoints);
        } catch (IllegalArgumentException _) {
            // inputPoints were not a valid GeneralRectangle
            return false;
        }
        // we have a valid GeneralRectangle!
        // check if the test point (parsed in the base class) lies strictly inside the rectangle.
        return shape.contains(testPoint);
    }
}