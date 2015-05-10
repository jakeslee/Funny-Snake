package Game.Snake.Controller;

import java.awt.*;
import java.awt.List;
import java.util.*;

/**
 * Created by jakes on 15/5/9.
 */
public class Wall implements Drawable, Collidedable {
    private java.util.List<Rectangle> rectangles;
    private Color paint = null;
    private boolean mode = true;
    private String img = null;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Color getPaint() {
        return paint;
    }

    public void setPaint(Color paint) {
        this.paint = paint;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public java.util.List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(java.util.List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    @Override
    public DrawableRect getDrawableArea() {
        DrawableRect drawableRect = new DrawableRect();
        drawableRect.rectangles = rectangles;
        drawableRect.paintMethd = new HashMap<>();
        for (Rectangle r : drawableRect.rectangles) {
            drawableRect.paintMethd.put(r, "WALL");
        }
        drawableRect.paintMethd.put(null, paint);
        drawableRect.meta = Wall.class.getName();
        return drawableRect;
    }

    @Override
    public String getIdentification() {
        return getClass().getName();
    }

    @Override
    public void collideWith(String identification) {
        System.out.println("You dead!");
    }
}
