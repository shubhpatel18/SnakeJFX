import javafx.scene.paint.Color;

import java.util.Random;

public class Food {

    private final PixelPainter PAINTER;
    private final int Y_MAX;
    private final int Y_MIN;
    private final int X_MAX;
    private final int X_MIN;

    private Coords position;

    private final Random rand;

    public Food(PixelPainter painter, int pixel_yMax, int pixel_yMin, int pixel_xMax, int pixel_xMin) {
        this.rand = new Random();

        this.PAINTER = painter;
        this.Y_MAX = pixel_yMax;
        this.Y_MIN = pixel_yMin;
        this.X_MAX = pixel_xMax;
        this.X_MIN = pixel_xMin;

        findNewCoords();
    }

    public void reset() {
        findNewCoords();
    }

    public Coords getCoords() {
        return position;
    }

    public void draw() {
        PAINTER.paint(position, Color.RED);
    }

    private void findNewCoords() {
        int xPos = rand.nextInt(X_MAX - X_MIN) + X_MIN;
        int yPos = rand.nextInt(Y_MAX - Y_MIN) + Y_MIN;
        position = new Coords(xPos, yPos);
    }
}
