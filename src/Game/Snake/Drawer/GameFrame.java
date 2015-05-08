package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;
import Game.Snake.Controller.EventProcessAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class GameFrame extends JFrame {
    private GameScreen gameScreen = null;
    private boolean starting = false;
    private JLabel status = null;

    public GameFrame() throws HeadlessException {
        super("贪吃蛇 " + Config.VERSION);
        setSize(Config.SCREEN_SIZE);
        gameScreen = new GameScreen();
        gameScreen.setUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                status.setText((String)data);
            }
        });

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton jButtonStart = new JButton("开始游戏");
        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (starting) {
                    gameScreen.stopGame();
                } else {
                    gameScreen.startGame();
                }
                starting = !starting;
            }
        });
        JButton jButtonExit = new JButton("退出游戏");
        jButtonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        status = new JLabel("蛇身长度: 5");

        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));

        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(status);
        controlPanel.add(Box.createGlue());
        controlPanel.add(jButtonStart);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(jButtonExit);

        add(controlPanel, BorderLayout.NORTH);
        add(gameScreen);

        Config.update();
    }
}

