package Game.Snake.Controller;

import Game.Snake.Configuration.Config;

import java.awt.*;
import java.util.*;

/**
 * Created by jakes,lhwarthas on 15/5/6.
 */
public class FoodImpl implements Collidedable, Food, Drawable {
    private EventProcessListener eventProcessListener = null;

    /*
    * 定义食物座标
    * */
    private int x, y;

    /*
    * 定义食物
    * */
    private Rectangle food = null;
    private CollideWatcher watcher = null;

    /*
    * 绘制方法
    * */
    private Map<Rectangle, Object> paintMethod = new HashMap<>();


    public FoodImpl(CollideWatcher watcher) {
        this.watcher = watcher;
        createLocation();
        food = CollideWatcher.generateRectangle(x,y);
        paintMethod.put(food, "FOOD");
    }

    public FoodImpl() {}

    /*
    * 创建食物座标
    * */
    public void createLocation() {
        Random random = new Random();
        do {
            x = random.nextInt(Config.VIEW_SIZE.width);
            y = random.nextInt(Config.VIEW_SIZE.height);
            y = y - y % Config.SNAKE_BODY_WIDTH;
            x = x - x % Config.SNAKE_BODY_WIDTH;
        }while (watcher.isCollidedWithExistence(CollideWatcher.generateRectangle(x, y)));
    }

    @Override
    public java.util.List<Rectangle> getRectangles() {
        java.util.List<Rectangle> rects = new java.util.ArrayList<Rectangle>();
        Rectangle rect = new Rectangle(x, y, Config.SNAKE_BODY_WIDTH, Config.SNAKE_BODY_WIDTH);
        rects.add(rect);
        return rects;
    }

    @Override
    public String getIdentification() {
        return Food.class.getName();
    }

    @Override
    public void collideWith(String identification) {
        if (identification.equals(Snake.class.getName())) {
            eventProcessListener.eventProcessing();
        }
    }

    @Override
    public boolean isEaten(Rectangle head) {
        if(head.getLocation() == food.getLocation())
        return true;
        else return false;
    }

    @Override
    public void setEatenListener(EventProcessListener eatenListener) {
        eventProcessListener = eatenListener;
    }

    @Override
    public DrawableRect getDrawableArea() {
        DrawableRect drawableRect = new DrawableRect();
        drawableRect.rectangles = getRectangles();
        drawableRect.paintMethd = paintMethod;
        return drawableRect;
    }
}
