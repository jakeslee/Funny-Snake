package Game.Snake.Controller;

/**
 * Created by jakes on 15/5/7.
 */
public class Factory {
    /*
    * 构造Snake工厂函数
    * */
    public static Snake createSnake(CollideWatcher watcher) {
      return new SnakeImpl(watcher);
    }

    /*
    * 构造Snake工厂函数
    * */
    public static Food createFood(CollideWatcher watcher) {
        return new FoodImpl(watcher);
    }
}
