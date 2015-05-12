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
 * Created by Jakes,lhwarthas on 15/5/6.
 */
public class SnakeImpl implements Snake, Collidedable, Drawable {
    /*
    * 设置计时器
    * */
    private Timer timer = null;

    /*
    * 设置蛇头
    * */
    private SnakeNode head = null;

    /*
    * 设置蛇碰撞的区域
    * */
    private List<Rectangle> snakeRect = null;

    /*
    * 描述整个蛇身
    * */
    private List<SnakeNode> snakeNodes = new ArrayList<SnakeNode>();

    /*
    * 设置死亡事件
    * */
    private EventProcessListener death = null;

    /*
    * 绘制方法
    * */
    private Map<Rectangle, Object> paintMethod = new HashMap<>();

    /*
    * 绘制颜色
    * */
    Map<String, Color> colorMap = new HashMap<>();

    /*
    * 设置刷新事件
    * */
    private EventProcessListener refresh = null;

    /*
    * 缓冲方向
    * */
    private byte BUFFER_DIRECTION;

    public SnakeImpl() {}

    public SnakeImpl(CollideWatcher watcher) {
        SnakeNode snakeNode = null;
        /*
        * 实例化蛇的每个节点
        * */
        for (int i = 0; i < Config.SNAKE_LENGTH; i++) {
            snakeNode = new SnakeNode(CollideWatcher.generateRectangle(
                    Config.START_POSITION.x - i * Config.SNAKE_BODY_WIDTH, Config.START_POSITION.y),
                    Snake.DIRECTION_RIGHT, Snake.DIRECTION_RIGHT);
            snakeNodes.add(snakeNode);
        }
        head = snakeNodes.get(0);

        /*
        * 计时器实例化，开始移动
        * */
        timer = new Timer(Config.SNAKE_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakeImpl.this.snakeMoving(head.nextDirection, head);
                checkBound(head);
                head.lastDirection = head.nextDirection;
                SnakeNode node = null;
                for (int i = 1; i < snakeNodes.size(); i++) {
                    node = snakeNodes.get(i);
                    SnakeImpl.this.snakeMoving(node.nextDirection, node);
                    checkBound(node);
                    node.lastDirection = node.nextDirection;
                    node.nextDirection = snakeNodes.get(i - 1).lastDirection;
                }

                switch (snakeNodes.get(0).nextDirection) {
                    case Snake.DIRECTION_UP:
                        paintMethod.put(snakeNodes.get(0).body, "SNAKE_HEAD_UP");
                        break;
                    case Snake.DIRECTION_DOWN:
                        paintMethod.put(snakeNodes.get(0).body, "SNAKE_HEAD_DOWN");
                        break;
                    case Snake.DIRECTION_LEFT:
                        paintMethod.put(snakeNodes.get(0).body, "SNAKE_HEAD_LEFT");
                        break;
                    case Snake.DIRECTION_RIGHT:
                        paintMethod.put(snakeNodes.get(0).body, "SNAKE_HEAD_RIGHT");
                        break;
                }
                colorMap.put("SNAKE_HEAD", Config.SNAKE_HEAD_COLOR);

                for (int i = 1; i < snakeNodes.size(); i++) {
                    if (snakeNodes.get(i).nextDirection != snakeNodes.get(i).lastDirection) {
                        SnakeNode snakeNode1 = snakeNodes.get(i);
                        if ((snakeNode1.nextDirection == Snake.DIRECTION_DOWN &&
                                snakeNode1.lastDirection == Snake.DIRECTION_LEFT) ||
                                (snakeNode1.nextDirection == Snake.DIRECTION_RIGHT &&
                                        snakeNode1.lastDirection == Snake.DIRECTION_UP)) {
                            paintMethod.put(snakeNodes.get(i).body, "SNAKE_TURN_LD");
                            colorMap.put("SNAKE_TURN_LD", Config.SNAKE_TURN_LD_COLOR);
                        }else if ((snakeNode1.nextDirection == Snake.DIRECTION_RIGHT &&
                                snakeNode1.lastDirection == Snake.DIRECTION_DOWN) ||
                                (snakeNode1.nextDirection == Snake.DIRECTION_UP &&
                                        snakeNode1.lastDirection == Snake.DIRECTION_LEFT)) {
                            paintMethod.put(snakeNodes.get(i).body, "SNAKE_TURN_RD");
                            colorMap.put("SNAKE_TURN_RD", Config.SNAKE_TURN_RD_COLOR);
                        }else if ((snakeNode1.nextDirection == Snake.DIRECTION_LEFT &&
                                snakeNode1.lastDirection == Snake.DIRECTION_UP) ||
                                (snakeNode1.nextDirection == Snake.DIRECTION_DOWN &&
                                        snakeNode1.lastDirection == Snake.DIRECTION_RIGHT)) {
                            paintMethod.put(snakeNodes.get(i).body, "SNAKE_TURN_LU");
                            colorMap.put("SNAKE_TURN_LU", Config.SNAKE_TURN_LU_COLOR);
                        }else {
                            paintMethod.put(snakeNodes.get(i).body, "SNAKE_TURN_RU");
                            colorMap.put("SNAKE_TURN_RU", Config.SNAKE_TURN_RU_COLOR);
                        }
                    }else if (i == snakeNodes.size() -1) {
                        switch (~snakeNodes.get(i).nextDirection) {
                            case Snake.DIRECTION_UP:
                                paintMethod.put(snakeNodes.get(i).body, "SNAKE_TAIL_UP");
                                break;
                            case Snake.DIRECTION_DOWN:
                                paintMethod.put(snakeNodes.get(i).body, "SNAKE_TAIL_DOWN");
                                break;
                            case Snake.DIRECTION_LEFT:
                                paintMethod.put(snakeNodes.get(i).body, "SNAKE_TAIL_LEFT");
                                break;
                            case Snake.DIRECTION_RIGHT:
                                paintMethod.put(snakeNodes.get(i).body, "SNAKE_TAIL_RIGHT");
                                break;
                        }
                        colorMap.put("SNAKE_TAIL", Config.SNAKE_TAIL_COLOR);

                    }else {
                        paintMethod.put(snakeNodes.get(i).body, "SNAKE_BODY");
                    }

                    if (CollideWatcher.isCollided(head.body, snakeNodes.get(i).body)) {
                        stop();
                        death.eventProcessing();
                        break;
                    }
                }
                refresh.updateEvent(null);

            }
        });
    }

    /*
    * 检查边界，若越界则从对面一侧出来
    * */
    public void checkBound(SnakeNode node) {
        if (node.body.x + Config.SNAKE_BODY_WIDTH > Config.VIEW_SIZE.width) {
            node.body.x = 0;
        }
        if (node.body.y + Config.SNAKE_BODY_WIDTH > Config.VIEW_SIZE.height) {
            node.body.y = 0;
        }
        if (node.body.x < 0) {
            node.body.x += Config.VIEW_SIZE.width;
            node.body.x -= node.body.x % Config.SNAKE_BODY_WIDTH;
        }
        if (node.body.y < 0) {
            node.body.y += Config.VIEW_SIZE.height;
            node.body.y -= node.body.y % Config.SNAKE_BODY_WIDTH;
        }
    }

    /*
    * 获得蛇节点的区域
    * */
    public List<Rectangle> getSnakeRect() {
        List<Rectangle> snakeRect = new ArrayList<Rectangle>();
        for(int i = 0; i < snakeNodes.size(); ++i) {
            snakeRect.add(snakeNodes.get(i).body);
        }
        return snakeRect;
    }

    @Override
    public java.util.List<Rectangle> getRectangles() {
        return getSnakeRect();
    }

    @Override
    public String getIdentification() {
        return Snake.class.getName();
    }

    @Override
    public void collideWith(String identification) {
        /*
        * 如果蛇和食物碰撞
        * */
        if (identification.equals(Food.class.getName())) {
            SnakeNode last = snakeNodes.get(snakeNodes.size() - 1);
            Rectangle rect = CollideWatcher.generateRectangle(last.body.x, last.body.y);
            SnakeNode s =  new SnakeNode(rect, last.lastDirection, last.lastDirection);
            snakeMoving((byte) ~last.lastDirection, s);

            paintMethod.put(last.body, "SNAKE_BODY");
            paintMethod.put(s.body, "SNAKE_TAIL");

            snakeNodes.add(s);
            refresh.eventProcessing();
        }else if (identification.equals(Snake.class.getName())) {
            this.stop();
            death.eventProcessing();
        }else if (identification.equals(Wall.class.getName())) {
            this.stop();
            death.eventProcessing();
        }
    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
    }

    /*
    * 检测方向是否冲突
    * */
    public void checkDirectionConflict(){
        if (BUFFER_DIRECTION != ~head.lastDirection) {
            head.nextDirection = BUFFER_DIRECTION;
        }
    }

    @Override
    public void turnLeft() {
        if (head.nextDirection != DIRECTION_RIGHT) {
            BUFFER_DIRECTION = DIRECTION_LEFT;
        }
        checkDirectionConflict();
    }

    @Override
    public void turnRight() {
        if (head.nextDirection != DIRECTION_LEFT) {
            BUFFER_DIRECTION = DIRECTION_RIGHT;
        }
        checkDirectionConflict();
    }

    @Override
    public void turnUp() {
        if (head.nextDirection != DIRECTION_DOWN) {
            BUFFER_DIRECTION = DIRECTION_UP;
        }
        checkDirectionConflict();
    }

    @Override
    public void turnDown() {
        if (head.nextDirection != DIRECTION_UP) {
            BUFFER_DIRECTION = DIRECTION_DOWN;
        }
        checkDirectionConflict();
    }

    @Override
    public void setDeathListener(EventProcessListener eventProcessListener) {
        death = eventProcessListener;
    }

    @Override
    public void setOnRefreshListener(EventProcessListener eventProcessListener) {
        refresh = eventProcessListener;
    }

    /*
    * 蛇的移动函数
    * */
    public void snakeMoving(byte direction, SnakeNode node) {
        switch (direction) {
            case DIRECTION_UP :
                node.body.setLocation((int)node.body.getX(), (int)(node.body.getY() - Config.SNAKE_BODY_WIDTH));
                break;
            case DIRECTION_DOWN :
                node.body.setLocation((int)node.body.getX(), (int)(node.body.getY() + Config.SNAKE_BODY_WIDTH));
                break;
            case DIRECTION_LEFT :
                node.body.setLocation((int)(node.body.getX() - Config.SNAKE_BODY_WIDTH), (int)node.body.getY());
                break;
            case DIRECTION_RIGHT :
                node.body.setLocation((int)(node.body.getX() + Config.SNAKE_BODY_WIDTH), (int)node.body.getY());
        }
    }

    @Override
    public DrawableRect getDrawableArea() {
        DrawableRect drawableRect = new DrawableRect();
        drawableRect.rectangles = getSnakeRect();
        drawableRect.paintMethd = paintMethod;
        drawableRect.meta = colorMap;
        return drawableRect;
    }
}

class SnakeNode {
    /*
    * 每节蛇身的区域
    * */
    public Rectangle body;

    /*
    * 蛇节点上次移动的方向
    * */
    public byte lastDirection;

    /*
    * 蛇节点下次移动的方向
    * */
    public byte nextDirection;

    public SnakeNode() {}

    public SnakeNode(Rectangle body, byte lastDirection, byte nextDirection) {
        this.body = body;
        this.lastDirection = lastDirection;
        this.nextDirection = nextDirection;
    }
}
