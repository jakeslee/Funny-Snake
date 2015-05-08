package Game.Snake.Configuration;

import Game.Snake.Controller.EventProcessListener;

import java.awt.*;
import java.awt.geom.Point2D;
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
    public static final String VERSION = Version.Version.toUpperCase();

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
    public static int SNAKE_BODY_WIDTH = 15;

    /*
    * 蛇身体节数
    * */
    public static int SNAKE_LENGTH = 5;

    /*
    * 窗口大小
    * */
    public static Dimension SCREEN_SIZE = new Dimension(796, 600);

    /*
    * 控制面板的区域
    * */
    public static int CONTROL_BAR_HEIGHT = 30;

    /*
    * 绘制视口的区域
    * */
    public static Dimension VIEW_SIZE = new Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height - CONTROL_BAR_HEIGHT);

    /*
    * 背景色
    * */
    public static Color BACKGROUD_COLOR = Color.black;

    /*
    * 前景色
    * */
    public static Color FOREGROUD_COLOR = Color.white;

    /*
    * 初始背景图片路径
    * */
    public static String BACKGROUND_PATH_START = "res/bg-1.jpg";

    /*
    * 默认背景图片路径
    * */
    public static String BACKGROUND_PATH_DEFAULT = null;

    /*
    * 背景图片路径
    * */
    public static String BACKGROUND_PATH = null;

    /*
    * 默认图片路径
    * */
    public static String SNAKE_DEFAULT_IMG = null;

    /*
    * 蛇头图片路径
    * */
    public static String SNAKE_HEAD_IMG = null;

    /*
    * 蛇身图片路径
    * */
    public static String SNAKE_BODY_IMG = null;

    /*
    * 蛇转向图片路径
    * */
    public static String SNAKE_TURN_IMG = null;

    /*
    * 蛇尾图片路径
    * */
    public static String SNAKE_TAIL_IMG = null;

    /*
    * 食物图片路径
    * */
    public static String FOOD_IMG = null;

    /*
    * 设置蛇头起始位置
    * */
    public static Point START_POSITION = new Point(300,300);
}
