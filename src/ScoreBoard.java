import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class ScoreBoard {
    private final GraphicsContext GC;
    private int score;

    private final int xPos;
    private final int yPos;

    public ScoreBoard(GraphicsContext gc, int xPos, int yPos) {
        this.GC = gc;
        this.score = 0;

        this.xPos = xPos;
        this.yPos = yPos;
    }

    public void draw() {
        Font old_font = GC.getFont();
        GC.setFill(Color.WHITE);
        GC.setFont(new Font("Consolas", 15));
        GC.fillText(String.format("Score: %d", score), xPos, yPos);
        GC.setFont(old_font);
    }

    public void reset() {
        score = 0;
    }

    public void increaseScore() {
        score += 100;
    }
}
