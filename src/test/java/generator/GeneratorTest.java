package generator;

import generator.transformations.HeartTransformation;
import generator.transformations.LinearTransformation;
import generator.transformations.SinusoidalTransformation;
import generator.transformations.SphereTransformation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class GeneratorTest {
    @Test
    @Disabled
    void generator_shouldGenerateFractalFlame() {
        SimpleRender render = new SimpleRender();
        List<Transformation> variations = List.of(new HeartTransformation(), new SinusoidalTransformation(),
                new LinearTransformation(), new SphereTransformation()
        );

        FractalImage fractal = render.render(
                variations,
                1920,
                1080,
                300000,
                500,
                10,
                1,
                true
        );

        ImageUtils.save(fractal, "png");
    }

    @Test
    @Disabled
    void generator_shouldGenerateFractalFlameWithConcurrency() {
        ConcurrentRender render = new ConcurrentRender();
        List<Transformation> variations = List.of(new HeartTransformation(), new SinusoidalTransformation(),
                new LinearTransformation(), new SphereTransformation()
        );

        FractalImage fractal = render.render(
                variations,
                1920 * 4,
                1080 * 4,
                300000,
                500,
                10,
                1,
                true
        );

        ImageUtils.save(fractal, "png");
    }

    @Test
    void testForAnalysisOfGenerationSpeed() throws IOException {
        ConcurrentRender concurrentRender = new ConcurrentRender();
        SimpleRender simpleRender = new SimpleRender();
        List<Transformation> variations = List.of(new HeartTransformation(), new SinusoidalTransformation(),
                new LinearTransformation(), new SphereTransformation()
        );
//        FileWriter writer = new FileWriter("src/test/java/generator/log.txt", true);
//        BufferedWriter bufferedWriter = new BufferedWriter(writer);


        for (int points = 1; points <= 3; points++) {
            for (int iter = 3; iter <= 5; iter++) {
                double concurrentSum = 0;
                double simpleSum = 0;
                double concurrentSumSquared = 0;
                double simpleSumSquared = 0;

                for (int i = 0; i < 10; i++){
                    long startTimeSimple = System.currentTimeMillis();
                    simpleRender.render(variations, 1920, 1080,
                            100000 * points, 100 * iter,
                            10, 1, true);
                    long endTimeSimple = System.currentTimeMillis();
                    double durationSimple = (double) (endTimeSimple - startTimeSimple) / 1000;
                    simpleSum += durationSimple;
                    simpleSumSquared += durationSimple * durationSimple;

                    long startTimeConcurrent = System.currentTimeMillis();
                    concurrentRender.render(variations, 1920, 1080,
                            100000 * points, 100 * iter,
                            10, 1, true);
                    long endTimeConcurrent = System.currentTimeMillis();
                    double durationConcurrent = (double) (endTimeConcurrent - startTimeConcurrent) / 1000;
                    concurrentSum += durationConcurrent;
                    concurrentSumSquared += durationConcurrent * durationConcurrent;
                }

                StringBuilder builder = new StringBuilder();
                builder.append("points peaks: ").append(points * 100000);
                builder.append("; iterations: ").append(100 * iter).append("    ");
                BigDecimal simpleE = new BigDecimal(simpleSum / 10);
                simpleE = simpleE.setScale(2, RoundingMode.HALF_UP);
                builder.append(simpleE).append("   ");
                BigDecimal concurrentE = new BigDecimal(concurrentSum / 10);
                concurrentE = concurrentE.setScale(2, RoundingMode.HALF_UP);
                builder.append(concurrentE)
                        .append("\n                                         ");

                BigDecimal simpleD = BigDecimal.valueOf(Math.sqrt((simpleSumSquared / 10)
                        - (simpleSum / 10) * (simpleSum / 10)));
                simpleD = simpleD.setScale(2, RoundingMode.HALF_UP);
                builder.append(simpleD.doubleValue()).append("   ");
                BigDecimal concurrentD = BigDecimal.valueOf(Math.sqrt((concurrentSumSquared / 10)
                        - (concurrentSum / 10) * (concurrentSum / 10)));
                concurrentD = concurrentD.setScale(2, RoundingMode.HALF_UP);
                builder.append(concurrentD.doubleValue()).append("\n");

                Files.write(Paths.get("src/test/java/generator/log.txt"),
                        builder.toString().getBytes(), StandardOpenOption.APPEND);
                //bufferedWriter.write(builder.toString());
            }
        }


    }
}
