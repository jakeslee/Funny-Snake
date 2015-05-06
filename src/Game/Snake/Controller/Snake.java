package Game.Snake.Controller;

import java.util.List;

/**
 * Created by jakes on 15/5/6.
 */
public interface Snake {

    void startMoving();

    void turnLeft();
    void turnRight();
    void turnUp();
    void turnDown();

    boolean isDead();

    List<SnakeNode> getSnakeNodes();    //取得蛇身体所有节点，用以绘制
}

class SnakeNode {
    int x;
    int y;
}
