import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PixelPainter {

    private final GraphicsContext GC;
    private final int HEIGHT;
    private final int WIDTH;
    private final int PIXEL_SIZE;
    private final int BORDER_SIZE;

    public PixelPainter(GraphicsContext GC, int pixelHeight, int pixelWidth, int pixelSize, int borderSize) {
        this.GC = GC;
        this.HEIGHT = pixelHeight;
        this.WIDTH = pixelWidth;
        this.PIXEL_SIZE = pixelSize;
        this.BORDER_SIZE = borderSize;
    }

    public void paint(Coords coords, Color color) {
        GC.setFill(color);
        int xStart = coords.x() * PIXEL_SIZE + BORDER_SIZE;
        int yStart = (HEIGHT - coords.y() - 1) * PIXEL_SIZE + BORDER_SIZE;
        int width = PIXEL_SIZE - BORDER_SIZE;
        GC.fillRect(xStart, yStart, width, width);
    }
}
