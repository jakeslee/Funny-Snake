package Game.Snake.Configuration;

import Game.Snake.Controller.EventProcessListener;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class Config {
    private static List<EventProcessListener> eventListenerList = new ArrayList<>();

    /*
    * 增加更新事件监听器
    *
    * 参数: eventProcessListener  响应更新事件的监听器
    * */
    public static void addUpdateEventListener(EventProcessListener eventProcessListener) {
        eventListenerList.add(eventProcessListener);
    }

    /*
    * 触发Config更新事件
    * */
    public static void update() {
        for (EventProcessListener e : eventListenerList)
            e.updateEvent(null);
    }

    //Constant
    /*
    * 软件版本
    * */
    public static final String VERSION = Version.Version;

    //Variable
    /*
    * 蛇移动速度
    *
    * 也是碰撞检测的速度
    * 单位: ms
    * */
    public static int SNAKE_SPEED = 200;

    /*
    * 蛇身体每节长度
    * */
    public static int SNAKE_BODY_WIDTH = 6;

    /*
    * 蛇身体节数
    * */
    public static int SNAKE_LENGTH = 5;

    /*
    * 窗口大小
    * */
    public static Dimension SCREEN_SIZE = new Dimension(800, 600);

    /*
    * 控制面板的区域
    * */
    public static int CONTROL_BAR_HEIGHT = 29;

    /*
    * 绘制视口的区域
    * */
    public static Dimension VIEW_SIZE = new Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height - CONTROL_BAR_HEIGHT);
}
