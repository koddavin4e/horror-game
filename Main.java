import java.util.HashSet;
import java.util.Set;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 700;

    private Rectangle player;
    private Rectangle ground;

    private double playerX = 100;
    private double playerY = 500;
    private double velocityX = 0;
    private double velocityY = 0;

    private static final double MOVE_SPEED = 4;
    private static final double JUMP_POWER = -12;
    private static final double GRAVITY = 0.5;

    private boolean onGround = false;
    private final Set<KeyCode> pressedKeys = new HashSet<>();

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        root.setPrefSize(WIDTH, HEIGHT);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #1b1b1b, #3a3a3a);");

        player = new Rectangle(40, 60, Color.WHITE);
        player.setArcWidth(10);
        player.setArcHeight(10);
        player.setX(playerX);
        player.setY(playerY);

        ground = new Rectangle(0, 620, WIDTH, 80);
        ground.setFill(Color.DARKSLATEGRAY);

        root.getChildren().addAll(ground, player);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> pressedKeys.add(event.getCode()));
        scene.setOnKeyReleased(event -> pressedKeys.remove(event.getCode()));

        AnimationTimer gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameLoop.start();

        stage.setTitle("Metroidvania Prototype");
        stage.setScene(scene);
        stage.show();
    }

    private void update() {
        handleInput();
        applyPhysics();
        checkCollisions();
        updatePlayerPosition();
    }

    private void handleInput() {
        velocityX = 0;
        if (pressedKeys.contains(KeyCode.A)) {
            velocityX = -MOVE_SPEED;
        }
        if (pressedKeys.contains(KeyCode.D)) {
            velocityX = MOVE_SPEED;
        }
        if ((pressedKeys.contains(KeyCode.W) || pressedKeys.contains(KeyCode.SPACE)) && onGround) {
            velocityY = JUMP_POWER;
            onGround = false;
        }
    }

    private void applyPhysics() {
        velocityY += GRAVITY;
    }

    private void checkCollisions() {
        double nextY = playerY + velocityY;
        double bottom = nextY + player.getHeight();
        double groundY = ground.getY();

        if (bottom >= groundY && playerX + player.getWidth() > ground.getX() && playerX < ground.getX() + ground.getWidth()) {
            nextY = groundY - player.getHeight();
            velocityY = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        playerY = nextY;

        double nextX = playerX + velocityX;
        if (nextX < 0) {
            nextX = 0;
        }
        if (nextX + player.getWidth() > WIDTH) {
            nextX = WIDTH - player.getWidth();
        }
        playerX = nextX;
    }

    private void updatePlayerPosition() {
        player.setX(playerX);
        player.setY(playerY);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
