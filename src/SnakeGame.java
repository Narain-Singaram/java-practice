import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private final int WIDTH = 400;
    private final int HEIGHT = 400;
    private final int UNIT_SIZE = 20;
    private final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private final int DELAY = 100;
    private final ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private char direction = 'R';
    private boolean running = false;
    private Timer timer;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);
        startGame();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(0, 0));
        spawnFood();

        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void spawnFood() {
        Random rand = new Random();
        int x = rand.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        int y = rand.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
        food = new Point(x, y);
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();

        switch (direction) {
            case 'U':
                newHead.translate(0, -UNIT_SIZE);
                break;
            case 'D':
                newHead.translate(0, UNIT_SIZE);
                break;
            case 'L':
                newHead.translate(-UNIT_SIZE, 0);
                break;
            case 'R':
                newHead.translate(UNIT_SIZE, 0);
                break;
        }

        snake.add(0, newHead);

        if (newHead.equals(food)) {
            spawnFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= WIDTH || head.y < 0 || head.y >= HEIGHT) {
            running = false;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                running = false;
                break;
            }
        }

        if (!running) {
            timer.stop();
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (running) {
            // Draw food
            g.setColor(Color.red);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (Point p : snake) {
                g.setColor(Color.green);
                g.fillRect(p.x, p.y, UNIT_SIZE, UNIT_SIZE);
            }
        } else {
            // Game over
            g.setColor(Color.red);
            g.setFont(new Font("SansSerif", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 110, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkCollision();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_I) && direction != 'D') {
            direction = 'U';
        } else if ((keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_K) && direction != 'U') {
            direction = 'D';
        } else if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_J) && direction != 'R') {
            direction = 'L';
        } else if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_L) && direction != 'L') {
            direction = 'R';
        }
    }

    //fsdfs

    @Override
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake Game");
        SnakeGame game = new SnakeGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
