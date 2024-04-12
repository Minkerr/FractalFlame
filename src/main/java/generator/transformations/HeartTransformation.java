package generator.transformations;

import generator.Point;
import generator.Transformation;

public class HeartTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        return new Point(
                Math.sqrt(x * x + y * y) * Math.sin(Math.sqrt(x * x + y * y) * Math.atan(y / x)),
                -Math.sqrt(x * x + y * y) * Math.cos(Math.sqrt(x * x + y * y) * Math.atan(y / x))
        );
    }
}