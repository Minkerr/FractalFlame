package generator;

public record FractalImage(Pixel[][] data, int width, int height) {
    Pixel pixel(int x, int y) {
        return data[x][y];
    }
}
