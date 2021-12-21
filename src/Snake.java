import javafx.scene.paint.Color;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Snake {

    private final PixelPainter PAINTER;
    private final int Y_MAX;
    private final int Y_MIN;
    private final int X_MAX;
    private final int X_MIN;

    private final Queue<Coords> BODY;
    private Coords head;
    private int length;
    private boolean isAlive;

    Direction dir;
    Direction tempDir;

    public Snake(PixelPainter painter, int pixel_yMax, int pixel_yMin, int pixel_xMax, int pixel_xMin) {
        this.PAINTER = painter;
        this.Y_MAX = pixel_yMax;
        this.Y_MIN = pixel_yMin;
        this.X_MAX = pixel_xMax;
        this.X_MIN = pixel_xMin;

        this.BODY = new LinkedList<>();
        init();
    }

    public void reset() {
        BODY.clear();
        init();
    }

    public void init() {
        head = new Coords((X_MAX - X_MIN) / 2, (Y_MAX - Y_MIN) / 2);
        BODY.add(head);
        length = 1;
        isAlive = true;
        tempDir = Direction.NONE;
    }

    public void update() {
        if (isAlive) {
            // update direction
            dir = tempDir;

            Coords newHead = switch (dir) {
                case UP -> head.getAbove();
                case DOWN -> head.getBelow();
                case LEFT -> head.getLeft();
                case RIGHT -> head.getRight();
                case NONE -> null;
            };

            if (newHead == null)
                return; // skip update if nowhere to move

            // check for collision with wall
            // or self
            if (newHead.x() > X_MAX || newHead.x() < X_MIN || newHead.y() > Y_MAX || newHead.y() < Y_MIN
                || BODY.contains(newHead)) {
                isAlive = false;
            } else {
                BODY.add(newHead);
                head = newHead;

                while (BODY.size() > length) {
                    BODY.remove();
                }
            }
        }
    }

    public void draw() {
        for (Coords coords : BODY) {
            PAINTER.paint(coords, Color.GREEN);
        }
    }

    public boolean hasEaten(Coords coords) {
        return head.equals(coords);
    }

    public void grow() {
        length++;
    }

    public boolean isDead() {
        return !isAlive;
    }

    public void setDir(Direction dir) {
        if (!(this.dir == Direction.UP && dir == Direction.DOWN) &&
            !(this.dir == Direction.DOWN && dir == Direction.UP) &&
            !(this.dir == Direction.LEFT && dir == Direction.RIGHT) &&
            !(this.dir == Direction.RIGHT && dir == Direction.LEFT)) {
            this.tempDir = dir;
        }
    }

    public List<Coords> getBody() {
        return new LinkedList<>(BODY);
    }

    public enum Direction {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        NONE,
    }
}
