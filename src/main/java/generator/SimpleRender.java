package generator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleRender implements Render {
    private final Random random = new Random();

    private int random(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    private double random(double min, double max) {
        return random.nextDouble(max - min) + min;
    }

    private List<TransformationCoefficientSet> generateTransformations(int numberOfTransformations) {
        List<TransformationCoefficientSet> coeff = new ArrayList<>();
        for (int i = 0; i < numberOfTransformations; i++) {
            coeff.add(new TransformationCoefficientSet());
        }
        return coeff;
    }

    @Override
    @SuppressWarnings("ParameterNumber")
    public FractalImage render(
            List<Transformation> variations,
            int width,
            int height,
            int numberOfPoints,
            int transformationsForPoint,
            int numberOfTransformations,
            int symmetry,
            boolean correction
    ) {
        Pixel[][] data = new Pixel[width][height];
        List<TransformationCoefficientSet> coeff = generateTransformations(numberOfTransformations);
        double yMin = -1;
        double yMax = 1;
        double xMin = (double) -width / height;
        double xMax = (double) width / height;

        for (int num = 0; num < numberOfPoints; ++num) {
            Point pw = new Point(random(xMin, xMax), random(yMin, yMax));

            for (int step = 0; step < transformationsForPoint; ++step) {
                int i = random(0, numberOfTransformations);

                double x = coeff.get(i).a() * pw.x() + coeff.get(i).b() * pw.y() + coeff.get(i).c();
                double y = coeff.get(i).d() * pw.x() + coeff.get(i).e() * pw.y() + coeff.get(i).f();

                double theta = 0.0;
                Transformation variation = variations.get(random(0, variations.size()));
                pw = variation.apply(new Point(x, y));

                for (int s = 0; s < symmetry; theta += Math.PI * 2 / symmetry, ++s) {
                    if (symmetry > 1) {
                        var pwr = pw.rotate(theta);
                        x = pwr.x();
                        y = pwr.y();
                    }

                    if (xMin < x && x < xMax && yMin < y && y < yMax) {
                        int x1 = width - (int) (((xMax - x) / (xMax - xMin)) * width);
                        int y1 = height - (int) (((yMax - y) / (yMax - yMin)) * height);

                        if (y1 < height && x1 < width) {
                            if (data[x1][y1] == null) {
                                int red = coeff.get(i).red();
                                int green = coeff.get(i).green();
                                int blue = coeff.get(i).blue();
                                data[x1][y1] = new Pixel(red, green, blue, 1);
                            } else {
                                int red = (data[x1][y1].r() + coeff.get(i).red()) / 2;
                                int green = (data[x1][y1].g() + coeff.get(i).green()) / 2;
                                int blue = (data[x1][y1].b() + coeff.get(i).blue()) / 2;
                                int hits = data[x1][y1].hitCount();
                                data[x1][y1] = new Pixel(red, green, blue, hits + 1);
                            }
                        }
                    }
                }
            }
        }
        FractalImage fractal = new FractalImage(data, width, height);
        if (correction) {
            GammaCorrection gammaCorrection = new GammaCorrection();
            gammaCorrection.process(fractal);
        }

        return fractal;
    }
}
