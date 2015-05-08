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
    private Image imageBackgroundStart = null;
    private Image imageBackgroundDefault = null;
    private Image imageSnakeHead = null;
    private Image imageSnakeBody = null;
    private Image imageSnakeTurn = null;
    private Image imageSnakeTail = null;
    private Image imageFood = null;

    private Image viewBuffer = null;

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
        snake = Factory.createSnake();

        /*
        * 蛇死亡事件处理
        * */
        snake.setDeathListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {
                //停止碰撞测试
                collideWatcher.stop();

                //发送停止信号
                updateEventListener.updateEvent(true);
            }
        });

        /*
        * 处理刷新事件
        *
        * 1. 重绘视口
        * 2. 触发数据更新事件
        * */
        snake.setOnRefreshListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                System.out.println("repaint");
                repaint();

                //响应数据更新事件
                String statusBar = "蛇身长度: " + getSnake().getDrawableArea().rectangles.size();
                updateEventListener.updateEvent(statusBar);
            }
        });
        collideWatcher.add((Collidedable) snake);
        collideWatcher.start();

        food = Factory.createFood();

        /*
        * 食物被吃响应
        *
        * 产生新的食物
        * */
        food.setEatenListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {
                food = Factory.createFood();
                food.setEatenListener(this);
                collideWatcher.set(Food.class.getName(), food);
            }
        });
        collideWatcher.add((Collidedable) food);

        /*
        * 配置更新事件处理
        * */
        Config.addUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                //BACKGROUND_PATH_START
                if (Config.BACKGROUND_PATH_START != null && !Config.BACKGROUND_PATH_START.equals("")) {
                    try {
                        imageBackgroundStart = ImageIO.read(new File(Config.BACKGROUND_PATH_START));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageBackgroundStart = null;
                }

                //BACKGROUND_PATH_DEFAULT
                if (Config.BACKGROUND_PATH_DEFAULT != null && !Config.BACKGROUND_PATH_DEFAULT.equals("")) {
                    try {
                        imageBackgroundDefault = ImageIO.read(new File(Config.BACKGROUND_PATH_DEFAULT));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageBackgroundDefault = null;
                }

                //BACKGROUND_PATH
                if (Config.BACKGROUND_PATH != null && !Config.BACKGROUND_PATH.equals("")) {
                    try {
                        imageBackground = ImageIO.read(new File(Config.BACKGROUND_PATH));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageBackground = imageBackgroundDefault;
                }

                //FOOD_IMG
                if (Config.FOOD_IMG != null) {
                    try {
                        imageFood = ImageIO.read(new File(Config.FOOD_IMG));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    imageFood = null;

                //SNAKE_HEAD_IMG, SNAKE_BODY_IMG, SNAKE_TURN_IMG, SNAKE_TAIL_IMG
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

        /*
        * 按键事件处理
        * */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        ((Snake) getSnake()).turnUp();
                        break;
                    case KeyEvent.VK_DOWN:
                        ((Snake) getSnake()).turnDown();
                        break;
                    case KeyEvent.VK_RIGHT:
                        ((Snake) getSnake()).turnRight();
                        break;
                    case KeyEvent.VK_LEFT:
                        ((Snake) getSnake()).turnLeft();
                        break;
                }
                super.keyPressed(e);
            }
        });

        /*
        * 窗体配置
        * */
        setSize(Config.VIEW_SIZE);

    }

    /*
    * 开始游戏处理函数
    * */
    public void startGame() {
        ((Snake)getSnake()).start();
        starting = true;
    }

    /*
    * 停止游戏处理函数
    * */
    public void stopGame() {
        ((Snake)getSnake()).stop();
        starting = false;
    }

    /*
    * 视口绘制函数
    *
    * 参数: graphics  用于绘制的画布
    * */
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
        java.util.List<Rectangle> snake_rect = getSnake().getDrawableArea().rectangles;
        String method = null;
        Image IMG = null;
        for (Rectangle r : snake_rect) {
            if (getSnake().getDrawableArea().paintMethd == null)
                method = null;
            else
                method = getSnake().getDrawableArea().paintMethd.get(r);

            //绘制对象名称 （SNAKE_HEAD, SNAKE_BODY, SNAKE_TAIL, SNAKE_TURN, FOOD或图片路径）
            //                          null 标识采用默认值(SNAKE_DEFAULT)+
            IMG = getImageByMethod(method);
            if (IMG != null) {
                graphics.drawImage(IMG, (int)r.getX(), (int)r.getY(), this);
                continue;
            }
            graphics.draw(makeCircle(r));
        }

        //绘制食物
        Rectangle food_rect = getFood().getDrawableArea().rectangles.get(0);
        if (getFood().getDrawableArea().paintMethd == null)
            method = null;
        else
            method = getFood().getDrawableArea().paintMethd.get(food_rect);
        IMG = getImageByMethod(method);
        if (IMG == null) {
            Color c = graphics.getColor();
            graphics.setColor(Color.yellow);
            graphics.draw(makeCircle(food_rect));
            graphics.setColor(c);
        }else {
            graphics.drawImage(IMG, (int)food_rect.getX(), (int)food_rect.getY(), this);
        }
    }

    /*
    * 通过绘制方法获取对应图片
    *
    * 参数: method    绘制方法
    *
    * 返回值: 返回对应的图片，null将使用默认方式绘制
    * */
    public Image getImageByMethod(String method) {
        Image IMG = null;
        if (method != null) {
            switch (method) {
                case "SNAKE_HEAD":
                    IMG = imageSnakeHead;
                    break;
                case "SNAKE_BODY":
                    IMG = imageSnakeBody;
                    break;
                case "SNAKE_TAIL":
                    IMG = imageSnakeTail;
                    break;
                case "SNAKE_TURN":
                    IMG = imageSnakeTurn;
                    break;
                case "FOOD":
                    IMG = imageFood;
                    break;
                default:
                    try {
                        IMG = ImageIO.read(new File(method));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
            if (IMG != null) {
                return IMG;
            }
        }
        return null;
    }

    /*
    * 产生一个圆
    *
    * 参数: r     圆的参数
    *
    * 返回值: 返回圆对象
    * */
    public Ellipse2D makeCircle(Rectangle r) {
        double x = r.getX() + r.getWidth() / 2;
        double y = r.getY() + r.getHeight() / 2;

        Ellipse2D e = new Ellipse2D.Double();
        e.setFrameFromCenter(x, y, x + r.getWidth() / 2, y + r.getWidth() / 2);
        return e;
    }

    /*
    * 获取蛇的可绘制对象
    *
    * 可以转换为其它接口(Snake, Collidedable)
    * */
    private Drawable getSnake() {
        return (Drawable)collideWatcher.get(Snake.class.getName());
    }

    /*
    * 获取食物的可绘制对象
    *
    * 可以转换为其它接口(Food, Collidedable)
    * */
    private Drawable getFood() {
        return (Drawable)collideWatcher.get(Food.class.getName());
    }

    /*
    * 设置更新事件响应
    * */
    public void setUpdateEventListener(EventProcessListener updateEventListener) {
        this.updateEventListener = updateEventListener;
    }

    @Override
    public void update(Graphics g) {
        if (viewBuffer == null) {
            viewBuffer = this.createImage(getWidth(), getHeight());
        }
        Graphics graphics = viewBuffer.getGraphics();


        g.drawImage(viewBuffer, 0, 0, null);
        paint(graphics);
        System.out.println("Paint_update");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!starting) {
            if (imageBackgroundStart == null){
                Color c = g.getColor();
                g.setColor(Config.BACKGROUD_COLOR);
                g.fillRect(0, 0, this.getWidth(), this.getHeight());
                g.setColor(c);
            }else
                g.drawImage(imageBackgroundStart, 0, 0, this.getWidth(), this.getHeight(), this);
        }else {
            paintObject((Graphics2D) g);
        }
        System.out.println("paintComponent");
    }
}
