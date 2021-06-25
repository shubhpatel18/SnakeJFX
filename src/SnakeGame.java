import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Objects;

public class SnakeGame extends Application {

    private final int PIXEL_SIZE = 16;
    private final int BORDER_SIZE = 2;
    private final int WINDOW_PIXEL_WIDTH = 32;
    private final int WINDOW_PIXEL_HEIGHT = 32;
    private final int WINDOW_WIDTH = PIXEL_SIZE * WINDOW_PIXEL_WIDTH;
    private final int WINDOW_HEIGHT = PIXEL_SIZE * WINDOW_PIXEL_HEIGHT;

    private final int TEXT_X = 60;
    private final int TEXT_Y = WINDOW_HEIGHT - 50;
    private final int SCORE_TEXT_OFFSET = 5;

    private final Canvas CANVAS;
    private final GraphicsContext GC;
    private final Timeline TL;

    private boolean gameStarted;
    private boolean gameWaitingForReset;

    private final Snake SNAKE;
    private final Food FOOD;
    private final ScoreBoard SCORE;

    private final int FOOD_EDGE_SAFETY = 2; // how many pixels FOOD must spawn from the edge

    public static void main(String[] args) {
        launch(args);
    }

    public SnakeGame() {
        // set up display and animation elements
        CANVAS = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);
        GC = CANVAS.getGraphicsContext2D();

        // set up PixelPainter to easily create a pixelated game
        final PixelPainter PAINTER = new PixelPainter(GC, WINDOW_PIXEL_HEIGHT, WINDOW_PIXEL_WIDTH, PIXEL_SIZE, BORDER_SIZE);

        // refresh every 16ms (60Hz animation)
        TL = new Timeline(new KeyFrame(Duration.millis(66.66666), e -> run()));

        // set up game elements
        SNAKE = new Snake(PAINTER, WINDOW_PIXEL_HEIGHT - 1, 0, WINDOW_PIXEL_WIDTH - 1, 0);
        FOOD = new Food(PAINTER, WINDOW_PIXEL_HEIGHT - 1 - FOOD_EDGE_SAFETY, FOOD_EDGE_SAFETY, WINDOW_PIXEL_WIDTH - 1 - FOOD_EDGE_SAFETY, FOOD_EDGE_SAFETY, SNAKE.getBody());
        SCORE = new ScoreBoard(GC, SCORE_TEXT_OFFSET, WINDOW_HEIGHT - SCORE_TEXT_OFFSET);
    }

    public void init() {
        GC.setFont(Font.font("Consolas", 25));
        TL.setCycleCount(Timeline.INDEFINITE);
    }

    public void start(Stage stage) {
        // set up window
        stage.setTitle("Snake");
        stage.getIcons().add(new Image(Objects.requireNonNull(Snake.class.getResourceAsStream("icon.png"))));
        stage.setScene(new Scene(new StackPane(CANVAS)));
        stage.setResizable(false);
        stage.show();

        // set key listeners
        stage.getScene().setOnKeyPressed(e -> {
            // any key can start the game
            gameStarted = true;
            switch (e.getCode()) {
                case UP, W -> SNAKE.setDir(Snake.Direction.UP);
                case DOWN, S -> SNAKE.setDir(Snake.Direction.DOWN);
                case LEFT, A -> SNAKE.setDir(Snake.Direction.LEFT);
                case RIGHT, D -> SNAKE.setDir(Snake.Direction.RIGHT);
            }
        });

        // begin game
        initialize();
        TL.play();
    }

    private void initialize() {
        gameStarted = false;
        redraw();
        GC.setFill(Color.YELLOW);
        GC.fillText("   Press Any Key To Play   ", TEXT_X, TEXT_Y);
    }

    private void run() {
        if (gameStarted) {
            if (gameWaitingForReset) {
                reset();
            }
            SNAKE.update(); // update SNAKE location
            redraw(); // redraw screen
            checkCollisions(); // check if SNAKE has eaten the FOOD, or died
        }

    }

    public void promptReset() {
        gameStarted = false;
        gameWaitingForReset = true;
        GC.setFill(Color.YELLOW);
        GC.fillText("Press Any Key To Play Again", TEXT_X, TEXT_Y);
    }

    public void reset() {
        SNAKE.reset();
        FOOD.exclude(SNAKE.getBody());
        FOOD.reset();
        SCORE.reset();
        gameWaitingForReset = false;
    }

    public void checkCollisions() {
        if (SNAKE.hasEaten(FOOD.getCoords())) {
            SNAKE.grow();
            FOOD.exclude(SNAKE.getBody());
            FOOD.reset();
            SCORE.increaseScore();
        } else if (SNAKE.isDead()) {
            promptReset();
        }
    }

    private void redraw() {
        // clear background
        GC.setFill(Color.BLACK);
        GC.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // redraw game
        SNAKE.draw();
        FOOD.draw();
        SCORE.draw();
    }
}
