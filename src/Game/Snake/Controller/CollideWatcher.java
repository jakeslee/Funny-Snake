package Game.Snake.Controller;

import Game.Snake.Configuration.Config;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;


/**
 * Created by jakes on 15/5/6.
 */
public class CollideWatcher {
    Map<String, Object> watchers = new HashMap<>();
    Timer timer = null;

    /*
    * 构造函数
    *
    * 初始化计时器
    * */
    public CollideWatcher() {
        timer = new Timer(Config.SNAKE_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (String lhs : watchers.keySet()) {
                    for (String rhs : watchers.keySet()) {
                        if (lhs.equals(rhs))
                            continue;
                        CollideWatcher.this.scan(lhs, rhs);
                    }
                }
            }
        });
    }

    /*
    * 更新时钟周期
    * */
    public void reloadTimer() {
        timer.setDelay(Config.SNAKE_SPEED);
    }

    /*
    * 启动碰撞监视
    * */
    public void start(){
        timer.start();
    }

    /*
    * 停止碰撞监视
    * */
    public void stop(){
        timer.stop();
    }

    /*
    * 添加监视的对象
    *
    * 参数: collidedable 监视的对象
    * */
    public void add(Collidedable collidedable) {
        set(collidedable.getIdentification(), collidedable);
    }

    /*
    * 设置监视对象
    *
    * 参数:   identification 对象的唯一标识符
    *        object         对象
    * */
    public void set(String identification, Object object) {
        if (object != null)
            watchers.put(identification, object);
        else {
            throw new NullPointerException();
        }
    }

    /*
    * 取得对应的监视对象
    *
    * 参数: identification    对象的ID
    *
    * 返回值: 对应的对象
    * */
    public Object get(String identification) {
        return watchers.get(identification);
    }

    /*
    * 移除监视对象
    *
    * 参数: identification    对象的ID
    * */
    public Object remove(String identification) {
        return watchers.remove(identification);
    }

    public Collection<Object> values() {
        return watchers.values();
    }

    /*
    * 判断与已存在的元素是否碰撞
    *
    * 参数: rectangle 用于碰撞比较的对象区域
    *
    * 返回值: 如果有碰撞则返回true，否则为false
    * */
    public boolean isCollidedWithExistence(Rectangle rectangle) {
        for (Object o : watchers.values()) {
            for (Rectangle r : ((Collidedable)o).getRectangles()) {
                if (isCollided(r, rectangle)) {
                    return true;
                }
            }
        }
        return false;
    }

    //Private Method
    /*
    * 扫描两个对象是否相交
    *
    * 参数: lhs   对象的唯一识别码
    *       rhs   对象的唯一识别码
    *
    * 返回值: 对象相交则返回true，并通知两个对象
    * */
    private boolean scan(String lhs, String rhs) {
        Collidedable collidedable1 = (Collidedable)watchers.get(lhs);
        Collidedable collidedable2 = (Collidedable)watchers.get(rhs);
        for (Rectangle rect1 : collidedable1.getRectangles()){
            for (Rectangle rect2 : collidedable2.getRectangles()){
                if (isCollided(rect1, rect2)){
                    collidedable1.collideWith(collidedable2.getIdentification());//通知两个对象
                    collidedable2.collideWith(collidedable1.getIdentification());
                    return true;
                }
            }
        }
        return false;
    }

    /*
    * 生成Rectangle
    *
    * 参数: X, Y  区域坐标
    *
    * 返回值: 产生的Rectangle
    * */
    public static Rectangle generateRectangle(int X, int Y) {
        return new Rectangle(X, Y, Config.SNAKE_BODY_WIDTH, Config.SNAKE_BODY_WIDTH);
    }

    /*
    * 判断两个矩形是否相交
    *
    * 参数: lhs, rhs  用于判断的矩形
    *
    * 返回值: 相交则返回真
    * */
    public static boolean isCollided(Rectangle lhs, Rectangle rhs) {
        Rectangle lhs_s = new Rectangle(lhs.x + 2, lhs.y + 2, lhs.width - 4, lhs.height - 4);
        Rectangle rhs_s = new Rectangle(rhs.x + 2, rhs.y + 2, rhs.width - 4, rhs.height - 4);
        return lhs_s.intersects(rhs_s);
    }

}

