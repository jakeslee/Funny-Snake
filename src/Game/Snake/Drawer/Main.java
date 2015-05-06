package Game.Snake.Drawer;

import javax.swing.*;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                displayJFrame();
            }
        });
    }

    public static void displayJFrame() {
        GameFrame gameFrame = new GameFrame();
        gameFrame.setVisible(true);
    }
}
