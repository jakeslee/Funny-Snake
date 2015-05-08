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
    * 用于数据更新的常量，Map<int, Object>
    *
    * CURRENT_LENGTH    当前长度
    *
    * DIRECTION_UP      方向为上
    *
    * DIRECTION_DOWN    方向为下
    *
    * DIRECTION_LEFT    方向为左
    *
    * DIRECTION_RIGHT   方向为右
    * */
    int CURRENT_LENGTH = 0;
    char DIRECTION_UP = 'U';
    char DIRECTION_DOWN = 'D';
    char DIRECTION_LEFT = 'L';
    char DIRECTION_RIGHT = 'R';
}

