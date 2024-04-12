package generator.transformations;

import generator.Point;
import generator.Transformation;

public class SinusoidalTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        return new Point(Math.sin(point.x()), Math.sin(point.y()));
    }

}
