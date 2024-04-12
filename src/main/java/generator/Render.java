package generator;

import java.util.List;

@FunctionalInterface
public interface Render {
    @SuppressWarnings("ParameterNumber")
    FractalImage render(
            List<Transformation> variations,
            int wight,
            int height,
            int samples,
            int iterPerSample,
            int numberOfTransformations,
            int symmetry,
            boolean correction
    );
}