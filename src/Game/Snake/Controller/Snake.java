package Game.Snake.Controller;

import java.awt.*;
import java.util.List;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public interface Snake {
    /*
    * 让蛇开始移动
    * */
    void start();

    /*
    * 让蛇停止移动
    * */
    void stop();

    /*
    * 让蛇左转
    *
    * 如果蛇没有开始移动，则自动移动；不能向相反方向移动
    * */
    void turnLeft();

    /*
    * 让蛇右转
    *
    * 如果蛇没有开始移动，则自动移动；不能向相反方向移动
    * */
    void turnRight();

    /*
    * 让蛇上转
    *
    * 如果蛇没有开始移动，则自动移动；不能向相反方向移动
    * */
    void turnUp();

    /*
    * 让蛇下转
    *
    * 如果蛇没有开始移动，则自动移动；不能向相反方向移动
    * */
    void turnDown();

    /*
    * 设置蛇死亡的监听器
    *
    * 参数: eventProcessListener  蛇死亡的监听器
    * */
    void setDeathListener(EventProcessListener eventProcessListener);

    /*
    * 刷新事件通知
    *
    * 参数: eventProcessListener  响应刷新事件的监听器
    * */
    void setOnRefreshListener(EventProcessListener eventProcessListener);

    /*
    * 取得蛇身体所有节点，用以绘制
    * */
    List<Rectangle> getSnakeNodes();
}

