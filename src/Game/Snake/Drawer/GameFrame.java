package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;

import javax.swing.*;
import java.awt.*;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class GameFrame extends JFrame {
    GameScreen gameScreen = null;
    public GameFrame() throws HeadlessException {
        super("贪吃蛇 " + Config.VERSION);
        setSize(Config.SCREEN_SIZE);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}

