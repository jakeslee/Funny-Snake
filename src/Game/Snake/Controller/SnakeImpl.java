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
    private Timer timer = null;
    private SnakeNode head = null;
    private List<Rectangle> snakeRect = null;
    private List<SnakeNode> snakeNodes = new ArrayList<SnakeNode>();
    private EventProcessListener death = null;
    private EventProcessListener refresh = null;

    public SnakeImpl() {}

    public SnakeImpl(CollideWatcher watcher) {
        SnakeNode snakeNode = null;
        for (int i = 0; i < Config.SNAKE_LENGTH; i++) {
            snakeNode = new SnakeNode(CollideWatcher.generateRectangle(
                    Config.START_POSITION.x - i * Config.SNAKE_BODY_WIDTH, Config.START_POSITION.y),
                    Snake.DIRECTION_RIGHT, Snake.DIRECTION_RIGHT);
            snakeNodes.add(snakeNode);
        }
        head = snakeNodes.get(0);

        timer = new Timer(Config.SNAKE_SPEED, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SnakeImpl.this.snakeMoving(head.nextDirection, head);
                head.lastDirection = head.nextDirection;
                SnakeNode node = null;
                for (int i = 1; i < snakeNodes.size(); i++) {
                    node = snakeNodes.get(i);
                    SnakeImpl.this.snakeMoving(node.nextDirection, node);
                    node.lastDirection = node.nextDirection;
                    node.nextDirection = snakeNodes.get(i - 1).lastDirection;
                }
                //refresh.eventProcessing();

                refresh.updateEvent(null);

            }
        });
    }

    public List<Rectangle> getSnakeRect(){
        List<Rectangle> snakeRect = new ArrayList<Rectangle>();
        for(int i = 0; i < snakeNodes.size(); ++i){
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
        if (identification.equals(Food.class.getName())){
            SnakeNode last = snakeNodes.get(snakeNodes.size() - 1);
            Rectangle rect = CollideWatcher.generateRectangle(last.body.x, last.body.y);
            SnakeNode s =  new SnakeNode(rect, last.lastDirection, last.lastDirection);
            System.out.println(s.body.x + "," + s.body.y);
            snakeMoving((byte)~last.lastDirection, s);
            System.out.println(s.body.x + "," + s.body.y);

            System.out.println(last.lastDirection + "," + last.nextDirection);
            System.out.println(s.lastDirection + "," + s.nextDirection);


            snakeNodes.add(s);
            //蛇增加
            //snakeNodes = Collections.reverse(snakeNodes);
            //snakeNodes.add(new SnakeNode())
            refresh.eventProcessing();
        }else if (identification.equals(Snake.class.getName())){
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

    @Override
    public void turnLeft() {
        if (head.nextDirection != DIRECTION_RIGHT){
            head.nextDirection = DIRECTION_LEFT;
        }
    }

    @Override
    public void turnRight() {
        if (head.nextDirection != DIRECTION_LEFT){
            head.nextDirection = DIRECTION_RIGHT;
        }
    }

    @Override
    public void turnUp() {
        if (head.nextDirection != DIRECTION_DOWN){
            head.nextDirection = DIRECTION_UP;
        }
    }

    @Override
    public void turnDown() {
        if (head.nextDirection != DIRECTION_UP){
            head.nextDirection = DIRECTION_DOWN;
        }
    }

    @Override
    public void setDeathListener(EventProcessListener eventProcessListener) {
        death = eventProcessListener;
    }

    @Override
    public void setOnRefreshListener(EventProcessListener eventProcessListener) {
        refresh = eventProcessListener;
    }

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

    public SnakeNode CreateSnakeNode(char lastDirection) {
        SnakeNode node = new SnakeNode();
        SnakeNode last = snakeNodes.get(snakeNodes.size());

        return node;
    }

    @Override
    public DrawableRect getDrawableArea() {
        DrawableRect drawableRect = new DrawableRect();
        drawableRect.rectangles = getSnakeRect();

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

    public SnakeNode() {
    }

    public SnakeNode(Rectangle body, byte lastDirection, byte nextDirection) {
        this.body = body;
        this.lastDirection = lastDirection;
        this.nextDirection = nextDirection;
    }
}
