package generator;

import generator.transformations.HeartTransformation;
import generator.transformations.LinearTransformation;
import generator.transformations.SinusoidalTransformation;
import generator.transformations.SphereTransformation;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GeneratorTest {
    @Test
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
    void generator_shouldGenerateFractalFlameWithConcurrency() {
        ConcurrentRender render = new ConcurrentRender();
        List<Transformation> variations = List.of(new HeartTransformation(), new SinusoidalTransformation(),
                new LinearTransformation(), new SphereTransformation()
        );

        FractalImage fractal = render.render(
                variations,
                1920*4,
                1080*4,
                300000,
                500,
                10,
                1,
                true
        );

        ImageUtils.save(fractal, "png");
    }
}
