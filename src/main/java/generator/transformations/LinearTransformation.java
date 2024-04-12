package generator.transformations;

import generator.Point;
import generator.Transformation;

public class LinearTransformation implements Transformation {
    @Override
    public Point apply(Point point) {
        return point;
    }
}
