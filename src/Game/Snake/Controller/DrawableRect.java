package Game.Snake.Controller;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * Created by jakes on 15/5/7.
 */
public class DrawableRect {
    /*
    * 绘制区域
    * */
    public List<Rectangle> rectangles;

    /*
    * 绘制方法，一般是图片元
    *
    * 可以为null，则采取默认绘制方式：第一个Rectangle为头，最后一个为尾，自动判断转角
    *
    * key: 绘制区域     value:  绘制对象名称 （SNAKE_HEAD, SNAKE_BODY, SNAKE_TAIL, SNAKE_TURN, FOOD或图片路径）
    *                          null (SNAKE_DEFAULT)
    *     null          绘制颜色
    * */
    public Map<Rectangle, Object> paintMethd = null;

    /*
    * 其它附加元信息
    *
    * Map<String, Object>
    *               String              Value Type      Comment
    * Snake可以添加 转角处的(TURN_RECT)    Rectangle
    *              节长(SNAKE_LENGTH)       Integer   通常不用设置，rectangles.size()
    *
    *              PAINT_COLOR          Color
    *              ...
    * Map<String, Color>    绘制方法，绘制颜色
    * */
    public Object meta = null;
}
