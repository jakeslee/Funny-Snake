package Game.Snake.Controller;

import java.awt.*;
import java.util.List;

/**
 * Created by jakes on 15/5/6.
 */
public interface Collidedable {
    /*
    * 获取物体的区域
    * */
    List<Rectangle> getRectangles();

    /*
    * 获取唯一识别码
    * */
    String getIdentification();

    /*
    * 碰撞事件通知
    * */
    void collideWith(String identification);
}
