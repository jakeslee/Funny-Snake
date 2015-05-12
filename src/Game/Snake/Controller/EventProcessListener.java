package Game.Snake.Controller;

/**
 * Created by jakes on 15/5/6.
 */

public interface EventProcessListener {
    /*
    * 事件处理函数
    * */
    void eventProcessing();
    /*
    * 数据更新响应函数
    * */
    void updateEvent(Object data);
}

