package generator;

public record Point(double x, double y) {
    public Point rotate(double angle) {
        double x1 = x;
        double y1 = y;
        double cosAngle = Math.cos(angle);
        double sinAngle = Math.sin(angle);
        double newX = x1 * cosAngle - y1 * sinAngle;
        double newY = x1 * sinAngle + y1 * cosAngle;
        return new Point(newX, newY);
    }
}
