package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;
import Game.Snake.Controller.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by jakes on 15/5/6.
 */
class GameScreen extends JPanel {
    /*
    * 显示资源
    * */
    private Image imageBackground = null;
    private Image imageSnakeHead = null;
    private Image imageSnakeBody = null;
    private Image imageSnakeTurn = null;
    private Image imageSnakeTail = null;

    private Snake snake;
    private Food food;
    private CollideWatcher collideWatcher;

    /*
    * LOGIC CONTROL
    * */
    private boolean starting = false;

    /*
    * Event Emit
    * */
    private EventProcessListener updateEventListener = null;

    public GameScreen() {
        collideWatcher = new CollideWatcher();
        snake = new SnakeImpl();
        snake.setDeathListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {

            }
        });
        snake.setOnRefreshListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {

            }
        });
        collideWatcher.add((Collidedable) snake);


        food = new FoodImpl();
        food.setEatenListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {
                food = new FoodImpl();
                collideWatcher.set(Food.class.getName(), food);
            }
        });
        collideWatcher.add((Collidedable) food);


        setSize(Config.VIEW_SIZE);

        Config.addUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                if (Config.BACKGROUND_PATH != null && Config.BACKGROUND_PATH.equals("")) {
                    try {
                        imageBackground = ImageIO.read(new File(Config.BACKGROUND_PATH));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageBackground = null;
                }

                if (Config.SNAKE_HEAD_IMG != null) {
                    try {
                        imageSnakeHead = ImageIO.read(new File(Config.SNAKE_HEAD_IMG));
                        imageSnakeBody = ImageIO.read(new File(Config.SNAKE_BODY_IMG));
                        imageSnakeTurn = ImageIO.read(new File(Config.SNAKE_TURN_IMG));
                        imageSnakeTail = ImageIO.read(new File(Config.SNAKE_TAIL_IMG));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageSnakeHead = null;
                    imageSnakeBody = null;
                    imageSnakeTurn = null;
                    imageSnakeTail = null;
                }
            }
        });


        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

            }
        });

    }

    public void startGame() {
        getSnake().start();
        starting = true;
    }

    public void stopGame() {

        starting = false;
    }

    public void paintObject(Graphics2D graphics) {
        //绘制背景
        int bg_width = Config.VIEW_SIZE.width;
        int bg_height = Config.VIEW_SIZE.height;
        if (imageBackground != null) {
            graphics.drawImage(imageBackground, 0, 0, bg_width, bg_height, this);
        }else {
            graphics.setColor(Color.black);
            graphics.fillRect(0, 0, bg_width, bg_height);
            graphics.setColor(Color.white);
        }

        //绘制蛇身
        java.util.List<Rectangle> snake_rect = getSnake().getSnakeNodes();
        if (Config.SNAKE_HEAD_IMG == null || Config.SNAKE_BODY_IMG  == null ||
                Config.SNAKE_TAIL_IMG  == null || Config.SNAKE_TURN_IMG  == null) {
            for (Rectangle rect : snake_rect) {
                graphics.draw(makeCircle(rect));
            }
        }else {
            for (int i = 0; i < snake_rect.size(); i++) {
                if (i == 0)
                    graphics.drawImage(imageSnakeHead, (int)snake_rect.get(i).getX(), (int)snake_rect.get(i).getY(),
                            (int)snake_rect.get(i).getWidth(), (int)snake_rect.get(i).getHeight(), this);
                else if (i == snake_rect.size() - 1)
                    graphics.drawImage(imageSnakeTail, (int)snake_rect.get(i).getX(), (int)snake_rect.get(i).getY(),
                            (int)snake_rect.get(i).getWidth(), (int)snake_rect.get(i).getHeight(), this);
                else
                    graphics.drawImage(imageSnakeBody, (int)snake_rect.get(i).getX(), (int)snake_rect.get(i).getY(),
                            (int)snake_rect.get(i).getWidth(), (int)snake_rect.get(i).getHeight(), this);
            }
        }

        //绘制食物
        Rectangle food_rect = getFood().getRect();
        Color c = graphics.getColor();
        graphics.setColor(Color.yellow);
        graphics.draw(makeCircle(food_rect));
        graphics.setColor(c);
    }

    public Ellipse2D makeCircle(Rectangle r) {
        double x = r.getX() + r.getWidth() / 2;
        double y = r.getY() + r.getHeight() / 2;

        Ellipse2D e = new Ellipse2D.Double();
        e.setFrameFromCenter(x, y, x + r.getWidth() / 2, y + r.getWidth() / 2);
        return e;
    }

    public void setUpdateEventListener(EventProcessListener updateEventListener) {
        this.updateEventListener = updateEventListener;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!starting) {
            try {
                imageBackground = ImageIO.read(new File(Config.BACKGROUND_PATH_DEFAULT));
                if (imageBackground == null){
                    System.out.println("end-hahha");
                }
                g.drawImage(imageBackground, 0, 0, this.getWidth(), this.getHeight(), this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private Snake getSnake() {
        return (Snake)collideWatcher.get(Snake.class.getName());
    }

    private Food getFood() {
        return (Food)collideWatcher.get(Food.class.getName());
    }
}
