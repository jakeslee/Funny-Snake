package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;
import Game.Snake.Controller.EventProcessAdapter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class Main {
    public static void main(String[] args) {
        try {
            Config.PWD = new File(".").getCanonicalPath();
            System.setProperty("user.dir", new File(".").getCanonicalPath());
            System.out.println(Config.PWD + "用户的当前工作目录:/n"+System.getProperty("user.dir"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                displayJFrame();
            }
        });
    }

    public static void displayJFrame() {

        GameFrame gameFrame = new GameFrame();
        gameFrame.setIconImage(new ImageIcon(Config.buildPath("res/icon.png")).getImage());
        try {
            Image image = ImageIO.read(new File(Config.buildPath("res/start-img.jpg")));
            SplashWindow splash = new SplashWindow(image, gameFrame,
                    3000);
            splash.setCloseEvent(new EventProcessAdapter() {
                @Override
                public void eventProcessing() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    gameFrame.setVisible(true);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
