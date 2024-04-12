package generator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;

public final class ImageUtils {
    private ImageUtils() {
    }

    @SuppressWarnings("MultipleStringLiterals")
    public static void save(FractalImage image, String format) {
        BufferedImage bufferedImage = new BufferedImage(image.width(), image.height(), BufferedImage.TYPE_INT_BGR);
        String actualFormat = format;
        if (!format.equals("jpg") && !format.equals("bmp")) {
            actualFormat = "png"; // png is default format
        }

        for (int x = 0; x < image.width(); x++) {
            for (int y = 0; y < image.height(); y++) {
                bufferedImage.setRGB(
                        x,
                        y,
                        new Color(
                                image.pixel(x, y).r(),
                                image.pixel(x, y).g(),
                                image.pixel(x, y).b()
                        ).getRGB()
                );
            }
        }

        try {
            Path path = Path.of("src" + File.separator + "main" + File.separator + "java" + File.separator
                    + "generator" + File.separator + "images" + File.separator + "output."
                    + actualFormat);

            int fileNumber = 1;
            while (Files.exists(path)) {
                path = Path.of("src" + File.separator + "main" + File.separator + "java" + File.separator
                        + "generator" + File.separator + "images" + File.separator + "output" + fileNumber + "."
                        + actualFormat);
                fileNumber++;
            }

            File file = Files.createFile(path).toFile();
            ImageIO.write(bufferedImage, actualFormat, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
