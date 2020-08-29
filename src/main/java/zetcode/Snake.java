package zetcode;

import java.awt.EventQueue;
import javax.swing.JFrame;

// 'Snake' is the main Java class
public class Snake extends JFrame {
    public Snake() {
        initUI();
    }

    private void initUI() {
        add(new Board());

        // The setResizable() method affects the insets of the JFrame container on some platforms.
        // Therefore, it is important to call it BEFORE the pack() method, otherwise, the collision of the snake's head with the right and bottom borders might not work correctly.
        setResizable(false);
        pack();

        setTitle("Snake"); // Game title to show on JFrame window
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new Snake();
            ex.setVisible(true);
        });
    }
}
