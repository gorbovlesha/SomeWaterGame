import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame {

    private static GameWindow gameWindow;
    private static long lastFrameTime;
    private static Image background;
    private static Image gameOver;
    private static Image water;
    private static float dropLeft = 200;
    private static float dropTop = -100;
    private static float dropV = 2;
    private static int score = 0;


    public static void main(String[] args) throws IOException {
        background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));
        gameOver= ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        water = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));

        gameWindow = new GameWindow();
        gameWindow.setDefaultCloseOperation(gameWindow.EXIT_ON_CLOSE);
        gameWindow.setLocation(200,100);
        gameWindow.setSize(906, 478);
        gameWindow.setResizable(false);
        lastFrameTime = System.nanoTime(); //возвращает текущее время в наносекундах
        GameField gameField = new GameField();
        gameField.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                float dropRight = dropLeft + water.getWidth(null);
                float dropBottom = dropTop + water.getHeight(null);
                boolean isDrop = x>=dropLeft && x<=dropRight && y>=dropTop && y<=dropBottom;
                if(isDrop) {
                    dropTop = -100;
                    dropLeft = (int) (Math.random() * (gameField.getWidth() - water.getWidth(null)));
                    dropV = dropV + 0.1f;
                    score++;
                    gameWindow.setTitle("Score: " + score);
                }


            }
        });
        gameWindow.add(gameField);
        gameWindow.setVisible(true);
    }
    private static void onRepaint(Graphics g) {
        long currentTime = System.nanoTime();
        float deltaTime = (currentTime -  lastFrameTime) * 0.000000001f;
        lastFrameTime = currentTime;

        dropTop = dropTop + dropV +deltaTime;
        g.drawImage(background, 0,0, null);  //without changes
        g.drawImage(water,(int) dropLeft,(int) dropTop,null);
        if (dropTop>gameWindow.getHeight()) {
            g.drawImage(gameOver, 280,120,null);
        }

    }
    private static class GameField extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
