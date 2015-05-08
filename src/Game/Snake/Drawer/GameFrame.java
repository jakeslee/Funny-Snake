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
        JButton jButtonStart = new JButton("开始游戏");
        setSize(Config.SCREEN_SIZE);
        gameScreen = new GameScreen();
        gameScreen.setUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                if (data instanceof String)
                    status.setText((String)data);
                else if (data instanceof Boolean) {
                    jButtonStart.setText("开始游戏");
                    starting = false;
                }
            }
        });
        gameScreen.setFocusable(true);
        gameScreen.requestFocusInWindow();

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jButtonStart.setFocusable(false);
        jButtonStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (starting) {
                    jButtonStart.setText("开始游戏");
                    gameScreen.stopGame();
                } else {
                    jButtonStart.setText("停止");
                    gameScreen.startGame();
                }
                starting = !starting;
            }
        });
        JButton jButtonExit = new JButton("退出游戏");
        jButtonExit.setFocusable(false);
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

