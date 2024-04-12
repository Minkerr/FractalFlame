package generator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ConcurrentRender implements Render {
    private final int nThreads = 8;
    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }

    private double random(double min, double max) {
        return ThreadLocalRandom.current().nextDouble(min, max);
    }

    private List<TransformationCoefficientSet> generateTransformations(int numberOfTransformations) {
        List<TransformationCoefficientSet> coeff = new ArrayList<>();
        for (int i = 0; i < numberOfTransformations; i++) {
            coeff.add(new TransformationCoefficientSet());
        }
        return coeff;
    }

    private FractalImage initCanvas(int width, int height) {
        Pixel[][] data = new Pixel[width][height];
        for (int i = 0; i < width; i++) {
            Arrays.fill(data[i], new Pixel(0, 0, 0, 0));
        }
        return new FractalImage(data, width, height);
    }

    @Override
    @SuppressWarnings({"ParameterNumber", "LambdaBodyLength"})
    public FractalImage render(
            List<Transformation> variations,
            int width,
            int height,
            int samples,
            int iterPerSample,
            int numberOfTransformations,
            int symmetry,
            boolean correction
    ) {
        FractalImage fractal = initCanvas(width, height);
        List<TransformationCoefficientSet> coeff = generateTransformations(numberOfTransformations);
        double yMin = -1;
        double yMax = 1;
        double xMin = (double) -width / height;
        double xMax = (double) width / height;
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        CountDownLatch countDownLatch = new CountDownLatch(nThreads);

        Runnable task = () -> {
            for (int num = 0; num < samples / nThreads; ++num) {
                Point pw = new Point(random(xMin, xMax), random(yMin, yMax));
                for (int step = 0; step < iterPerSample; ++step) {
                    int i = random(0, numberOfTransformations);
                    double x = coeff.get(i).a() * pw.x() + coeff.get(i).b() * pw.y() + coeff.get(i).c();
                    double y = coeff.get(i).d() * pw.x() + coeff.get(i).e() * pw.y() + coeff.get(i).f();

                    Transformation variation = variations.get(random(0, variations.size()));
                    pw = variation.apply(new Point(x, y));

                    double theta2 = 0.0;
                    for (int s = 0; s < symmetry; theta2 += Math.PI * 2 / symmetry, ++s) {
                        if (symmetry > 1) {
                            var pwr = pw.rotate(theta2);
                            x = pwr.x();
                            y = pwr.y();
                        }
                        if (xMin < x && x < xMax && yMin < y && y < yMax) {
                            int x1 = width - (int) (((xMax - x) / (xMax - xMin)) * width);
                            int y1 = height - (int) (((yMax - y) / (yMax - yMin)) * height);
                            if (y1 < height && x1 < width) {
                                var pixel = fractal.pixel(x1, y1);
                                synchronized (pixel) {
                                    if (pixel.hitCount() == 0) {
                                        int red = coeff.get(i).red();
                                        int green = coeff.get(i).green();
                                        int blue = coeff.get(i).blue();
                                        fractal.data()[x1][y1] = new Pixel(red, green, blue, 1);
                                    } else {
                                        int red = (fractal.data()[x1][y1].r() + coeff.get(i).red()) / 2;
                                        int green = (fractal.data()[x1][y1].g() + coeff.get(i).green()) / 2;
                                        int blue = (fractal.data()[x1][y1].b() + coeff.get(i).blue()) / 2;
                                        int hits = fractal.data()[x1][y1].hitCount();
                                        fractal.data()[x1][y1] = new Pixel(red, green, blue, hits + 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            countDownLatch.countDown();
        };

        List<Runnable> tasks = Stream.generate(() -> task).limit(nThreads).toList();
        for (var t : tasks) {
            executor.submit(t);
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            executor.close();
            throw new RuntimeException(e);
        }

        executor.shutdown();
        if (correction) {
            GammaCorrection gammaCorrection = new GammaCorrection();
            gammaCorrection.process(fractal);
        }
        return fractal;
    }
}
