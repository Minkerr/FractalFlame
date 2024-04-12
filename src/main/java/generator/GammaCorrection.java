package generator;

public class GammaCorrection implements ImageProcessor {
    private final double gamma = 2.2;

    @Override
    public void process(FractalImage image) {
        double max = 0.0;
        int height = image.height();
        int width = image.width();
        double[][] normal = new double[width][height];

        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                if (image.data()[row][col] == null) {
                    image.data()[row][col] = new Pixel(0, 0, 0, 0);
                }
                if (image.data()[row][col].hitCount() != 0) {
                    normal[row][col] = Math.log10(image.data()[row][col].hitCount());
                    if (normal[row][col] > max) {
                        max = normal[row][col];
                    }
                }
            }
        }
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                normal[row][col] /= max;
                int r = (int) (image.data()[row][col].r() * Math.pow(normal[row][col], (1.0 / gamma)));
                int g = (int) (image.data()[row][col].g() * Math.pow(normal[row][col], (1.0 / gamma)));
                int b = (int) (image.data()[row][col].b() * Math.pow(normal[row][col], (1.0 / gamma)));
                image.data()[row][col] = new Pixel(r, g, b, 0);
            }
        }
    }
}
