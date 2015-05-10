package Game.Snake.Drawer;

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
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                displayJFrame();
            }
        });
    }

    public static void displayJFrame() {
        GameFrame gameFrame = new GameFrame();
        gameFrame.setIconImage(new ImageIcon("res/icon.png").getImage());
        try {
            Image image = ImageIO.read(new File("res/start-img.jpg"));
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
