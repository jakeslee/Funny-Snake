package Game.Snake.Drawer;

import Game.Snake.Configuration.Config;
import Game.Snake.Controller.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.File;
import java.io.IOException;
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
    private Image imageSnakeDefault = null;
    private Image imageSnakeHead = null;
    private Image imageSnakeBody = null;
    private Image imageSnakeTurn = null;
    private Image imageSnakeTail = null;
    private Image imageFood = null;
    private Image imageWall = null;

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
        createSnake();

        /*
        * 配置更新事件处理
        * */
        Config.addUpdateEventListener(new EventProcessAdapter() {
            @Override
            public void updateEvent(Object data) {
                setSize(Config.VIEW_SIZE);

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

                //WALL_IMG
                if (Config.WALL_IMG != null) {
                    try {
                        imageWall = ImageIO.read(new File(Config.WALL_IMG));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else
                    imageWall = null;

                //WALL OBJ
                if (Config.WALL == null) {
                    collideWatcher.remove(Wall.class.getName());
                } else {
                    collideWatcher.add(Config.WALL);
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

                //SNAKE_DEFAULT
                if (Config.SNAKE_DEFAULT_IMG != null && !Config.SNAKE_DEFAULT_IMG.equals("")) {
                    try {
                        imageSnakeDefault = ImageIO.read(new File(Config.SNAKE_DEFAULT_IMG));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    imageSnakeDefault = null;
                }

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

                createSnake();
                collideWatcher.reloadTimer();
            }
        });

        /*
        * 按键事件处理
        * */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
            }

            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if (starting) {
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
                    }
                } catch (NullPointerException e1) {
                    e1.printStackTrace();
                }
                super.keyPressed(e);
            }
        });
    }

    /*
    * 创建Snake
    * */
    private void createSnake() {
        snake = Factory.createSnake(collideWatcher);

        /*
        * 蛇死亡事件处理
        * */
        snake.setDeathListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {
                stopGame();
                //停止碰撞测试
                collideWatcher.stop();

                //重新创建一个新的Snake
                createSnake();
                createFood();

                //发送停止信号
                updateEventListener.updateEvent(true);
                repaint();
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
                repaint();

                //响应数据更新事件
                updateEventListener.updateEvent(getSnake().getDrawableArea().rectangles.size());
            }
        });
        collideWatcher.add((Collidedable) snake);
    }

    /*
    * 创建Food
    * */
    private void createFood() {
        food = Factory.createFood(collideWatcher);

        /*
        * 食物被吃响应
        *
        * 产生新的食物
        * */
        food.setEatenListener(new EventProcessAdapter() {
            @Override
            public void eventProcessing() {
                food = Factory.createFood(collideWatcher);
                food.setEatenListener(this);
                collideWatcher.set(Food.class.getName(), food);
            }
        });
        collideWatcher.add((Collidedable) food);
    }

    /*
    * 开始游戏处理函数
    * */
    public void startGame() {
        createFood();
        ((Snake)getSnake()).start();
        starting = true;
        collideWatcher.start();
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
            graphics.setColor(Config.BACKGROUD_COLOR);
            graphics.fillRect(0, 0, bg_width, bg_height);
            graphics.setColor(Config.FOREGROUD_COLOR);
        }


        String method = null;
        Image IMG = null;
        for (Object o : collideWatcher.values()) {
            Drawable drawable = (Drawable)o;

            for (Rectangle r : drawable.getDrawableArea().rectangles) {
                Color toPaint = null;
                if (drawable.getDrawableArea().paintMethd != null) {
                    method = (String)drawable.getDrawableArea().paintMethd.get(r);
                    IMG = getImageByMethod(method);
                    if (IMG == null)
                        toPaint = (Color)drawable.getDrawableArea().paintMethd.get(null);
                }else {
                    IMG = null;
                }

                if (IMG == null) {
                    Color c = graphics.getColor();

                    if (toPaint == null)
                        toPaint = Config.FOREGROUD_COLOR;

                    graphics.setColor(toPaint);
                    graphics.fillRect(r.x, r.y, r.width, r.height);
                    graphics.setColor(c);
                }else {
                    if (r.width > Config.SNAKE_BODY_WIDTH || r.height > Config.SNAKE_BODY_WIDTH) {
                        for (int i = 0; i < r.width / Config.SNAKE_BODY_WIDTH; i++) {
                            for (int j = 0; j < r.height / Config.SNAKE_BODY_WIDTH; j++) {
                                graphics.drawImage(IMG, (int) r.x + i * Config.SNAKE_BODY_WIDTH,
                                        (int) r.y + j * Config.SNAKE_BODY_WIDTH,
                                        Config.SNAKE_BODY_WIDTH, Config.SNAKE_BODY_WIDTH, this);
                            }
                        }
                    }else
                        graphics.drawImage(IMG, (int) r.getX(), (int) r.getY(),
                                Config.SNAKE_BODY_WIDTH, Config.SNAKE_BODY_WIDTH, this);
                }
            }
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
                case "WALL":
                    IMG = imageWall;
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
    //public Ellipse2D makeDrawArea(Rectangle r) {
    public Ellipse2D makeDrawArea(Rectangle r) {
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
    }
}
