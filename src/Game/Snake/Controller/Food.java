package Game.Snake.Controller;

import java.awt.*;

/**
 * Created by jakes on 15/5/6.
 */
public interface Food {
    /*
    * 创建食物
    *
    * 注意: 不能与蛇身重叠
    *
    * 返回: 新创建的食物
    * */

    Food createFood();

    /*
    * 是否被吃掉
    *
    * 返回: 吃掉返回true，否则返回false
    * */
    boolean isEaten(Rectangle head);

}
