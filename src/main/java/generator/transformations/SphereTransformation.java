package generator.transformations;

import generator.Point;
import generator.Transformation;

public class SphereTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        double x = point.x();
        double y = point.y();
        return new Point(
                x / (x * x + y * y),
                y / (x * x + y * y)
        );
    }
}
