package Game.Snake.Controller;

/**
 * Created by jakes on 15/5/7.
 */
public class Factory {
    /*
    * 构造Snake工厂函数
    * */
    public static Snake createSnake() {
      return new SnakeImpl();
    }

    /*
    * 构造Snake工厂函数
    * */
    public static Food createFood() {
        return new FoodImpl();
    }
}
