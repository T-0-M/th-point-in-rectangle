package com.properclever.pir.util;

import com.properclever.pir.domain.Point;
import com.properclever.pir.domain.Shape;

public interface PointInRectangleCheckable {
    boolean checkPointInRectangle(Shape rectangle, Point testPoint);
}
