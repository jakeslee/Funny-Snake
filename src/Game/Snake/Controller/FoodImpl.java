package Game.Snake.Controller;

import java.awt.*;
import java.util.*;

/**
 * Created by lhwarthas on 15/5/6.
 */
public class FoodImpl implements Collidedable, Food {
    @Override
    public java.util.List<Rectangle> getRectangles() {
        return null;
    }

    @Override
    public String getIdentification() {
        return "Food";
    }

    @Override
    public void collideWith(String identification) {

    }

    @Override
    public Food createFood() {
        return null;
    }

    @Override
    public boolean isEaten(Rectangle head) {
        return false;
    }

    @Override
    public void setEatenListener(EventProcessListener eatenListener) {

    }
}