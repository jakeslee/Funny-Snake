package Game.Snake.Drawer;


import Game.Snake.Configuration.Config;
import Game.Snake.Controller.EventProcessListener;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

/**
 * 几乎所有时髦的应用都有一个欢迎屏幕。欢迎屏幕既是宣传产品的方法之一，
 * 而且在长时间的应用启动过程中，欢迎屏幕还用来表示应用正在准备过程中。
 */

/**
 * 本例子实现一个欢迎屏幕，常用作应用软件的启动画面。
 */
public class SplashWindow extends JWindow {
    private EventProcessListener closeEvent;
    /**
     * 构造函数
     *
     * @param filename
     *            欢迎屏幕所用的图片
     * @param frame
     *            欢迎屏幕所属的窗体
     * @param waitTime
     *            欢迎屏幕显示的事件
     */
    public SplashWindow(Image image, JFrame frame, int waitTime) {
        super(frame);
        StartPanel startPanel = new StartPanel(image, waitTime);


        //setSize(800, 575);

        // 建立一个标签，标签中显示图片。
        //
        // 将标签放在欢迎屏幕中间
        getContentPane().add(startPanel, BorderLayout.CENTER);
        pack();
        // 将欢迎屏幕放在屏幕中间
        setLocationRelativeTo(null);

        // 增加一个鼠标事件处理器，如果用户用鼠标点击了欢迎屏幕，则关闭。
/*        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                closeEvent.eventProcessing();
                setVisible(false);
                dispose();
            }
        });*/

        final int pause = waitTime;
        /**
         * Swing线程在同一时刻仅能被一个线程所访问。一般来说，这个线程是事件派发线程（event-dispatching thread）。
         * 如果需要从事件处理（event-handling）或绘制代码以外的地方访问UI，
         * 那么可以使用SwingUtilities类的invokeLater()或invokeAndWait()方法。
         */
        // 关闭欢迎屏幕的线程
        final Runnable closerRunner = new Runnable() {
            public void run() {
                closeEvent.eventProcessing();
                setVisible(false);
                dispose();
            }
        };


        // 等待关闭欢迎屏幕的线程
        Runnable waitRunner = new Runnable() {
            public void run() {
                try {
                    // 当显示了waitTime后，尝试关闭欢迎屏幕
                    //timer.start();
                    Thread.sleep(pause);
                    SwingUtilities.invokeAndWait(closerRunner);
                    // SwingUtilities.invokeLater(closerRunner);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        setVisible(true);
        // 启动等待关闭欢迎屏幕的线程
        Thread splashThread = new Thread(waitRunner, "SplashThread");
        splashThread.start();
    }

    public void setCloseEvent(EventProcessListener closeEvent) {
        this.closeEvent = closeEvent;
    }
}

class StartPanel extends JPanel {
    private Image img = null;
    int delay = 0;
    int percent = 0;

    public StartPanel(Image img, int delay) {
        super();
        this.delay = delay;
        this.img = img;

        setPreferredSize(new Dimension(img.getWidth(null), img.getHeight(null)));


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (percent < 100)
                    percent += 3;
                else
                    percent = 100;
                StartPanel.this.repaint();
            }
        }, 1000, delay/55);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.gray);
        g.drawImage(img, 0, 0, this);
        g.drawString("当前版本号: " + Config.VERSION, 10, 20);
        g.drawString("正在加载... " + percent + "%", 10, getHeight() - 10);
    }
}

