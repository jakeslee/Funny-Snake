package Game.Snake.Configuration;

import java.awt.*;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class Config {
    /*
    * 软件版本
    * */
    public static final String VERSION = Version.Version;

    /*
    * 蛇移动速度
    *
    * 也是碰撞检测的速度
    * 单位: ms
    * */
    public static final int SNAKE_SPEED = 200;

    /*
    * 蛇身体每节长度
    * */
    public static final int SNAKE_BODY_WIDTH = 6;

    /*
    * 蛇身体节数
    * */
    public static final int SNAKE_LENGTH = 5;

    /*
    * 视口大小
    * */
    public static final Dimension SCREEN_SIZE = new Dimension(800, 600);
}
