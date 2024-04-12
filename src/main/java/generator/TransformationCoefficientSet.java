package generator;

import java.util.Random;

public class TransformationCoefficientSet {
    private double a;
    private double b;
    private double c;
    private double d;
    private double e;
    private double f;
    private int red;
    private int green;
    private int blue;

    private final int maxColorValue = 256;

    public TransformationCoefficientSet() {
        this.a = random(-1.0, 1.0);
        this.d = random(-1.0, 1 - Math.sqrt(this.a * this.a));
        this.b = random(-1.0, 1.0);
        this.e = random(-1.0, 1 - Math.sqrt(this.b * this.b));
        while (this.a * this.a + this.b * this.b + this.d * this.d + this.e * this.e
                >= 1 + (this.a * this.e - this.b * this.d) * (this.a * this.e - this.b * this.d)) {
            this.a = random(-1.0, 1.0);
            this.d = random(-1.0, 1 - Math.sqrt(this.a * this.a));
            this.b = random(-1.0, 1.0);
            this.e = random(-1.0, 1 - Math.sqrt(this.b * this.b));
        }
        this.c = random(-1.0, 1.0);
        this.f = random(-1.0, 1.0);
        this.red = random(0, maxColorValue);
        this.green = random(0, maxColorValue);
        this.blue = random(0, maxColorValue);
    }

    public double a() {
        return a;
    }

    public double b() {
        return b;
    }

    public double c() {
        return c;
    }

    public double d() {
        return d;
    }

    public double e() {
        return e;
    }

    public double f() {
        return f;
    }

    public int red() {
        return red;
    }

    public int green() {
        return green;
    }

    public int blue() {
        return blue;
    }

    private int random(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    private double random(double min, double max) {
        Random random = new Random();
        return random.nextDouble(max - min) + min;
    }
}
