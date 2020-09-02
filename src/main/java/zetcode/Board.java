// Java Snake game source code - http://zetcode.com/javagames/snake/
// Available under 2-Clause BSD License - https://opensource.org/licenses/BSD-2-Clause

package zetcode;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Board extends JPanel implements ActionListener {

    // Defining the constants (variables that won't change)

    // Determine the size of the 'board'/game window
    private final int B_WIDTH = 350;
    private final int B_HEIGHT = 350;

    // The size of the 'apple' image as well as the size of the snake's body dots ('dot.png')
    private final int DOT_SIZE = 10;

    // Defines the maximum number of possible dots on the board (900 = (300 * 300) / (10 * 10))
                                                           // or 900 = (B_WIDTH * B_HEIGHT) / DOT SIZE * DOT SIZE)
    private final int ALL_DOTS = 900;

    // Calculates a random position for each apple spawn
    private final int RAND_POS = 29;

    // Determine the 'speed' of the game
    private int DELAY = 100;

    // These two arrays store the x and y coordinates of all joints of a snake.
    private final int x[] = new int[ALL_DOTS];
    private final int y[] = new int[ALL_DOTS];

    private int dots;
    private int apple_x;
    private int apple_y;

    private boolean leftDirection = false;
    private boolean rightDirection = true;
    private boolean upDirection = false;
    private boolean downDirection = false;
    private boolean inGame = true;

    private Timer timer;
    private Image ball;
    private Image apple;
    private Image head;

    public Board() {
        initBoard();
    }

    private void initBoard() {
        addKeyListener(new TAdapter());
        setBackground(Color.black);
        setFocusable(true);
        setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
        loadImages();
        initGame();
    }

    // The 'ImageIcon' class is used for displaying PNG images
    private void loadImages() {
        ImageIcon iid = new ImageIcon("src/resources/smoke.png");
        ball = iid.getImage();

        ImageIcon iia = new ImageIcon("src/resources/cow.png");
        apple = iia.getImage();

        ImageIcon iih = new ImageIcon("src/resources/ufo.png");
        head = iih.getImage();
    }

    // Create the snake, randomly spawn an apple on the board, and start the timer
    private void initGame() {
        dots = 3; // Begin game with snake having 3 total 'dots' (2 body 'dots' plus the head 'dot')

        for (int z = 0; z < dots; z++) { // Randomly spawn an apple on the board
            x[z] = 50 - z * 10;
            y[z] = 50;
        }
        locateApple();

        timer = new Timer(DELAY, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }

    private void doDrawing(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int z = 0; z < dots; z++) {
                if (z == 0) {
                    g.drawImage(head, x[z], y[z], this);
                } else {
                    g.drawImage(ball, x[z], y[z], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameOver(g);
        }
    }

    // Details for 'Game Over' screen window
    private void gameOver(Graphics g) {
        String msg = "You crashed...Game Over";
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics metr = getFontMetrics(small);
        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(msg, (B_WIDTH - metr.stringWidth(msg)) / 2, B_HEIGHT / 2);
    }

    private void checkApple() {
        // If the apple collides with the head...
        if ((x[0] == apple_x) && (y[0] == apple_y)) {
            // Increase the number of joints ('dots') of the snake.
            dots++;
            // Then call the locateApple() method which randomly positions a new apple object on the board.
            locateApple();
        }
    }

 // The key algorithm of the game:
     // We control the head of the snake.
     // We change its direction with the cursor keys.
     // The rest of the joints move one position up the chain.
     // The second joint moves where the first was,
     // the third joint where the second was, etc.
    private void move() {
        // Moves the joints up the chain.
        for (int z = dots; z > 0; z--) {
            x[z] = x[(z - 1)];
            y[z] = y[(z - 1)];
        }
        // Moves the head to the left.
        if (leftDirection) {
            x[0] -= DOT_SIZE;
        }
        // Moves the head to the right.
        if (rightDirection) {
            x[0] += DOT_SIZE;
        }
        // Moves the head up.
        if (upDirection) {
            y[0] -= DOT_SIZE;
        }
        // Moves the head down.
        if (downDirection) {
            y[0] += DOT_SIZE;
        }
    }

    // Determine if the snake has hit itself or one of the walls.
    private void checkCollision() {
        for (int z = dots; z > 0; z--) {
            // If the snake hits one of its joints with its head, the game is over.
            if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z])) {
                inGame = false;
                break;
            }
        }
        // Also, it is game over if the snake hits the bottom of the board.
        if (y[0] >= B_HEIGHT) {
            inGame = false;
        }
        if (y[0] < 0) {
            inGame = false;
        }
        if (x[0] >= B_WIDTH) {
            inGame = false;
        }
        if (x[0] < 0) {
            inGame = false;
        }
        if (!inGame) {
            timer.stop();
        }
    }

    private void locateApple() {
        int r = (int) (Math.random() * RAND_POS);
        apple_x = ((r * DOT_SIZE));

        r = (int) (Math.random() * RAND_POS);
        apple_y = ((r * DOT_SIZE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if ((key == KeyEvent.VK_LEFT) && (!rightDirection)) {
                leftDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_RIGHT) && (!leftDirection)) {
                rightDirection = true;
                upDirection = false;
                downDirection = false;
            }
            if ((key == KeyEvent.VK_UP) && (!downDirection)) {
                upDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
            if ((key == KeyEvent.VK_DOWN) && (!upDirection)) {
                downDirection = true;
                rightDirection = false;
                leftDirection = false;
            }
        }
    }
}
