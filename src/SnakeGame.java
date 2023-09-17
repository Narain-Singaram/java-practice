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
    private boolean inTitleScreen = true; // Title screen flag
    private JButton beginButton; // Begin button
    private int score = 0;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        addKeyListener(this);

        // Create and configure the Begin button
        beginButton = new JButton("Begin");
        beginButton.setFont(new Font("SansSerif", Font.PLAIN, 20));
        beginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inTitleScreen) {
                    // Start the game when the Begin button is clicked from the title screen
                    inTitleScreen = false;
                    startGame();
                    beginButton.setVisible(false); // Hide the button during gameplay
                }
            }
        });

        // Add the Begin button to the panel
        add(beginButton);
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void startGame() {
        snake.clear();
        snake.add(new Point(2 * UNIT_SIZE, 2 * UNIT_SIZE)); // Starting position of the snake
        spawnFood();
        score = 0;
        direction = 'R';
        running = true;
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
            score += 10;
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

        if (inTitleScreen) {
            // Title screen
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.BOLD, 40));
            g.drawString("Snake Game", WIDTH / 2 - 120, HEIGHT / 2 - 40);

            // Show the Begin button
            beginButton.setBounds(WIDTH / 2 - 50, HEIGHT / 2 + 20, 100, 40);
            beginButton.setVisible(true);
            add(beginButton);
        } else if (running) {
            // Draw food
            g.setColor(Color.red);
            g.fillRect(food.x, food.y, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < snake.size(); i++) {
                if (i == 0) {
                    // Snake head with eyes
                    g.setColor(Color.green);
                    g.fillRect(snake.get(i).x, snake.get(i).y, UNIT_SIZE, UNIT_SIZE);
                    g.setColor(Color.black);
                    g.fillRect(snake.get(i).x + 4, snake.get(i).y + 4, 6, 6); // Left eye
                    g.fillRect(snake.get(i).x + 10, snake.get(i).y + 4, 6, 6); // Right eye
                } else {
                    g.setColor(Color.green);
                    g.fillRect(snake.get(i).x, snake.get(i).y, UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw score
            g.setColor(Color.white);
            g.setFont(new Font("SansSerif", Font.PLAIN, 20));
            g.drawString("Score: " + score, 10, 20);
        } else {
            // Game over
            g.setColor(Color.red);
            g.setFont(new Font("SansSerif", Font.BOLD, 40));
            g.drawString("Game Over", WIDTH / 2 - 110, HEIGHT / 2 - 40);
            g.setFont(new Font("SansSerif", Font.PLAIN, 20));
            g.drawString("Final Score: " + score, WIDTH / 2 - 70, HEIGHT / 2 + 20);
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

        if (inTitleScreen) {
            // Start the game when the Begin button is clicked from the title screen
            if (keyCode == KeyEvent.VK_ENTER) {
                inTitleScreen = false;
                startGame();
                beginButton.setVisible(false); // Hide the button during gameplay
            }
        } else if (running) {
            // Control the snake during the game
            if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) && direction != 'D') {
                direction = 'U';
            } else if ((keyCode == KeyEvent.VK_S || keyCode == KeyEvent.VK_DOWN) && direction != 'U') {
                direction = 'D';
            } else if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) && direction != 'R') {
                direction = 'L';
            } else if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) && direction != 'L') {
                direction = 'R';
            }
        }
    }

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
