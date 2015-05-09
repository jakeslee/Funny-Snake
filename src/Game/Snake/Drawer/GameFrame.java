package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;
import Game.Snake.Controller.EventProcessAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class GameFrame extends JFrame {
    private GameScreen gameScreen = null;
    private boolean starting = false;
    private JLabel status = null;

    public GameFrame() throws HeadlessException {
        super("贪吃蛇 " + Config.VERSION);
        Config.addUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                Config.SCREEN_SIZE.height = Config.VIEW_SIZE.height + Config.CONTROL_BAR_HEIGHT;
                Config.SCREEN_SIZE.width = Config.VIEW_SIZE.width;
            }
        });
        Config.loadConfig();
        if (Config.CURRENT_MAP == null && Config.DEFAULT_MAP != null) {
            Config.applyMap(Config.DEFAULT_MAP);
        }else
            Config.applyMap(Config.CURRENT_MAP);

        JButton jButtonStart = new JButton("开始游戏");
        JButton setting = new JButton("设置");

        gameScreen = new GameScreen();
        gameScreen.setUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                if (data instanceof String)
                    status.setText((String) data  + "  |  当前速度等级: " + Config.LEVELS.get(Config.SNAKE_SPEED));
                else if (data instanceof Boolean) {
                    jButtonStart.setText("开始游戏");
                    starting = false;
                }
            }
        });
        gameScreen.setFocusable(true);
        gameScreen.requestFocusInWindow();

        setting.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Setting settingDlg = new Setting(GameFrame.this);
                settingDlg.setVisible(true);
            }
        });



        setting.setFocusable(false);
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

        status = new JLabel("蛇身长度: 5  |  当前速度等级: 普通");

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        setContentPane(mainPanel);
        JPanel controlPanel = new JPanel();
        controlPanel.setBounds(0, 0, Config.SCREEN_SIZE.width, Config.CONTROL_BAR_HEIGHT);
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.X_AXIS));
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(status);
        controlPanel.add(Box.createGlue());

        controlPanel.add(jButtonStart);
        controlPanel.add(setting);
        controlPanel.add(jButtonExit);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getY() > Config.CONTROL_BAR_HEIGHT + GameFrame.this.getInsets().top) {
                    jButtonStart.setVisible(false);
                    jButtonExit.setVisible(false);
                    setting.setVisible(false);
                } else {
                    jButtonStart.setVisible(true);
                    jButtonExit.setVisible(true);
                    setting.setVisible(true);
                }
            }
        });


        gameScreen.setBounds(0, Config.CONTROL_BAR_HEIGHT, Config.VIEW_SIZE.width, Config.VIEW_SIZE.height);
        mainPanel.add(controlPanel, BorderLayout.NORTH);
        mainPanel.add(gameScreen);
        mainPanel.setPreferredSize(Config.SCREEN_SIZE);
        pack();

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Config.update();
    }
}

class Setting extends JDialog {
    public Setting(Frame owner) {
        super(owner);
        setTitle("设置");
        setModal(true);
        setSize(300, 230);
        setLocationRelativeTo(owner);
        JPanel content = new JPanel();
        setContentPane(content);
        setLayout(null);

        JPanel setMap = new JPanel();
        setMap.setBorder(BorderFactory.createTitledBorder("设置当前地图"));

        Vector<String> maps = new Vector<>(Config.MAPS.keySet());

        JComboBox mapList = new JComboBox(maps);
        setMap.add(new JLabel("选择地图："));
        setMap.add(mapList);
        setMap.setBounds(10, 10, getWidth() - 20, 70);

        JPanel setSpeed = new JPanel();
        setSpeed.setBorder(BorderFactory.createTitledBorder("设置游戏等级"));
        Vector<String> levels = new Vector<>(Config.LEVELS.values());
        setSpeed.add(new JLabel("选择玩家等级："));
        JComboBox levelCom = new JComboBox(levels);
        setSpeed.add(levelCom);
        setSpeed.setBounds(10, 90, getWidth() - 20, 70);

        JButton apply = new JButton("应用");
        apply.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLevel = (String)levelCom.getSelectedItem();
                for (int speed : Config.LEVELS.keySet()) {
                    if (Config.LEVELS.get(speed).equals(selectedLevel)) {
                        Config.SNAKE_SPEED = speed;
                        break;
                    }
                }
                Config.applyMap((String) mapList.getSelectedItem());
            }
        });
        JButton cancel = new JButton("取消");
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Setting.this.setVisible(false);
            }
        });
        apply.setBounds(150, 170, 70, 25);
        cancel.setBounds(220, 170, 70, 25);

        content.add(setMap);
        content.add(setSpeed);
        content.add(apply);
        content.add(cancel);
    }
}

