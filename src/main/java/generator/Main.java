package generator;

import generator.transformations.HeartTransformation;
import generator.transformations.LinearTransformation;
import generator.transformations.SinusoidalTransformation;
import generator.transformations.SphereTransformation;

import java.util.List;

public class Main {
    public static void main(String[] args) {
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
                24,
                1,
                true
        );

        ImageUtils.save(fractal, "png");
    }
}