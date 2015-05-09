package Game.Snake.Configuration;

import Game.Snake.Controller.CollideWatcher;
import Game.Snake.Controller.EventProcessListener;
import Game.Snake.Controller.Wall;
import Game.Snake.Drawer.Map;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jakes, lhwarthas on 15/5/6.
 */
public class Config {
    private static List<EventProcessListener> eventListenerList = new ArrayList<>();

    /*
    * 增加更新事件监听器
    *
    * 参数: eventProcessListener  响应更新事件的监听器
    * */
    public static void addUpdateEventListener(EventProcessListener eventProcessListener) {
        eventListenerList.add(eventProcessListener);
    }

    /*
    * 触发Config更新事件
    * */
    public static void update() {
        for (EventProcessListener e : eventListenerList)
            e.updateEvent(null);
    }

    public static void loadConfig() {
        String content = readFile("config.json");
        if (!content.equals("")) {
            JSONObject jsonObject = new JSONObject(content);
            if (jsonObject.keySet().contains("SNAKE_BODY_WIDTH"))
                SNAKE_BODY_WIDTH = jsonObject.getInt("SNAKE_BODY_WIDTH");

            if (jsonObject.keySet().contains("DEFAULT_MAP"))
                DEFAULT_MAP = jsonObject.getString("DEFAULT_MAP");

            if (jsonObject.keySet().contains("MAP_DIRECTORY"))
                MAP_DIRECTORY = jsonObject.getString("MAP_DIRECTORY");

            if (jsonObject.keySet().contains("CURRENT_MAP"))
                CURRENT_MAP = jsonObject.getString("DEFAULT_MAP");

            if (jsonObject.keySet().contains("DEFAULT_SHAPE"))
                DEFAULT_SHAPE = jsonObject.getString("DEFAULT_SHAPE");

            if (jsonObject.keySet().contains("SPEED_LEVELS")) {
                LEVELS.clear();
                JSONObject levels = jsonObject.getJSONObject("SPEED_LEVELS");
                for (String key : levels.keySet())
                    LEVELS.put(levels.getInt(key), key);
            }

            loadMaps();
        }
    }

    public static void loadMaps() {
        MAPS.clear();
        File path = new File(MAP_DIRECTORY);
        if (path.isDirectory()) {
            File[] files = path.listFiles();
            for (File item : files) {
                if (item.isDirectory()) {
                    String mapInfo = readFile(item.getPath() + File.separator + "info.json");
                    if (!mapInfo.equals("")) {
                        String mapName = item.getName();
                        JSONObject jsonObject = new JSONObject(mapInfo);
                        Map map = new Map();

                        /*
                        * WALL
                        * */
                        Wall wall = new Wall();
                        JSONObject wallObject = jsonObject.getJSONObject("WALL");

                        wall.setMode(wallObject.getBoolean("MODE"));
                        if (wallObject.getBoolean("MODE")) {
                            wall.setImg(wallObject.getString("IMG"));
                        }else {
                            JSONArray jsonArray = wallObject.getJSONArray("IMG");
                            wall.setPaint(makeRGBA(jsonArray.getInt(0), jsonArray.getInt(1),
                                    jsonArray.getInt(3), jsonArray.getDouble(3)));
                        }

                        JSONArray rectObjects = wallObject.getJSONArray("RECT");
                        List<Rectangle> rectangles = new ArrayList<>();
                        for (int i = 0; i < rectObjects.length(); i++) {
                            JSONArray rect = rectObjects.getJSONArray(i);
                            int x = rect.getInt(0);
                            int y = rect.getInt(1);
                            int width = rect.getInt(2);
                            int height = rect.getInt(3);
                            if (x < 0)
                                x = 0 - x * Config.SNAKE_BODY_WIDTH;
                            if (y < 0)
                                y = 0 - y * Config.SNAKE_BODY_WIDTH;
                            if (width < 0)
                                width = 0 - width * Config.SNAKE_BODY_WIDTH;
                            if (height < 0)
                                height = 0 - height * Config.SNAKE_BODY_WIDTH;
                            rectangles.add(new Rectangle(x, y, width, height));
                        }
                        wall.setRectangles(rectangles);
                        map.wall = wall;

                        /*
                        * SNAKE
                        * */
                        map.snake = jsonObject.getJSONObject("SNAKE");

                        /*
                        * FOOD
                        * */
                        map.food = jsonObject.getJSONObject("FOOD");

                        /*
                        * VIEW
                        * */
                        map.view = jsonObject.getJSONObject("VIEW");

                        MAPS.put(mapName, map);
                    }
                }
            }
        }
    }

    public static void applyMap(String mapName) {
        CURRENT_MAP = mapName;
        Map map = MAPS.get(mapName);
        SNAKE_BODY_WIDTH = map.view.getInt("LHW");

        //SNAKE
        JSONArray startPosition = map.snake.getJSONArray("INIT");
        int startX = startPosition.getInt(0);
        int startY = startPosition.getInt(1);
        if (startX < 0)
            startX = 0 - startX * SNAKE_BODY_WIDTH;
        if (startY < 0)
            startY = 0 - startY * SNAKE_BODY_WIDTH;
        START_POSITION = new Point(startX, startY);
        if (map.snake.getBoolean("MODE")) {
            SNAKE_HEAD_IMG = MAP_DIRECTORY + mapName + File.separator + map.snake.getString("HEAD");
            SNAKE_TURN_IMG = MAP_DIRECTORY + mapName + File.separator + map.snake.getString("TURN");
            SNAKE_BODY_IMG = MAP_DIRECTORY + mapName + File.separator + map.snake.getString("BODY");
            SNAKE_TAIL_IMG = MAP_DIRECTORY + mapName + File.separator + map.snake.getString("TAIL");
        }else {
            SNAKE_HEAD_IMG = null;
            SNAKE_TURN_IMG = null;
            SNAKE_BODY_IMG = null;
            SNAKE_TAIL_IMG = null;
        }

        //FOOD
        if (map.food.getBoolean("MODE")) {
            FOOD_IMG = MAP_DIRECTORY + mapName + File.separator + map.food.getString("IMG");
        }else {
            FOOD_IMG = null;
        }

        //WALL
        if (map.wall.isMode()) {
            WALL_IMG = MAP_DIRECTORY + mapName + File.separator + map.wall.getImg();
        }else {
            WALL_IMG = null;
        }

        //VIEW
        if (map.view.getBoolean("MODE")) {
            BACKGROUND_PATH = MAP_DIRECTORY + mapName + File.separator + map.view.getString("IMG");
        }else {
            BACKGROUND_PATH = null;
        }

        JSONArray colorArray = map.view.getJSONArray("BG_COLOR");
        BACKGROUD_COLOR = makeRGBA(colorArray.getInt(0),colorArray.getInt(1),
                colorArray.getInt(2), colorArray.getDouble(3));
        colorArray = map.view.getJSONArray("FG_COLOR");
        FOREGROUD_COLOR = makeRGBA(colorArray.getInt(0),colorArray.getInt(1),
                colorArray.getInt(2), colorArray.getDouble(3));

        JSONArray viewRect = map.view.getJSONArray("RECT");
        int view_width = viewRect.getInt(0);
        int view_height = viewRect.getInt(1);
        if (view_width < 0)
            view_width = 0 - view_width * SNAKE_BODY_WIDTH;
        if (view_height < 0)
            view_height = 0 - view_height * SNAKE_BODY_WIDTH;
        VIEW_SIZE = new Dimension(view_width, view_height);

        WALL = map.wall;

        update();
    }

    public static Color makeRGBA(int r, int g, int b, double a) {
        return new Color(r, g, b, (int)a * 255);
    }

    public static void writeFile(String filePath, String sets)
            throws IOException {
        FileWriter fw = new FileWriter(filePath);
        PrintWriter out = new PrintWriter(fw);
        out.write(sets);
        out.println();
        fw.close();
        out.close();
    }

    public static String readFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String str = "";
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            while ((tempString = reader.readLine()) != null) {
                str += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return str;
    }

    //Constant
    /*
    * 软件版本
    * */
    public static final String VERSION = Version.Version.toUpperCase();

    //Variable
    /*
    * 蛇移动速度
    *
    * 也是碰撞检测的速度
    * 单位: ms
    * */
    public static int SNAKE_SPEED = 200;

    /*
    * 蛇身体每节长度
    * */
    public static int SNAKE_BODY_WIDTH = 15;

    /*
    * 蛇身体节数
    * */
    public static int SNAKE_LENGTH = 5;

    /*
    * 窗口大小
    * */
    public static Dimension SCREEN_SIZE = new Dimension(796, 600);

    /*
    * 控制面板的区域
    * */
    public static int CONTROL_BAR_HEIGHT = 30;

    /*
    * 绘制视口的区域
    * */
    public static Dimension VIEW_SIZE = new Dimension(SCREEN_SIZE.width, SCREEN_SIZE.height - CONTROL_BAR_HEIGHT);

    /*
    * 背景色
    * */
    public static Color BACKGROUD_COLOR = Color.black;

    /*
    * 前景色
    * */
    public static Color FOREGROUD_COLOR = Color.white;

    /*
    * 初始背景图片路径
    * */
    public static String BACKGROUND_PATH_START = "res/bg-1.jpg";

    /*
    * 默认背景图片路径
    * */
    public static String BACKGROUND_PATH_DEFAULT = null;

    /*
    * 背景图片路径
    * */
    public static String BACKGROUND_PATH = null;

    /*
    * 默认图片路径
    * */
    public static String SNAKE_DEFAULT_IMG = null;

    /*
    * 蛇头图片路径
    * */
    public static String SNAKE_HEAD_IMG = null;

    /*
    * 蛇身图片路径
    * */
    public static String SNAKE_BODY_IMG = null;

    /*
    * 蛇转向图片路径
    * */
    public static String SNAKE_TURN_IMG = null;

    /*
    * 蛇尾图片路径
    * */
    public static String SNAKE_TAIL_IMG = null;

    /*
    * 食物图片路径
    * */
    public static String FOOD_IMG = null;

    /*
    * 墙图片路径
    * */
    public static String WALL_IMG = null;

    /*
    * 墙对象
    * */
    public static Wall WALL = null;

    /*
    * 设置蛇头起始位置
    * */
    public static Point START_POSITION = new Point(300,300);

    /*
    * 默认绘制形状
    * */
    public static String DEFAULT_SHAPE = "FILL-RECTANGLE";

    /*
    * 默认地图
    * */
    public static String DEFAULT_MAP = null;

    /*
    * 当前地图
    * */
    public static String CURRENT_MAP = null;

    /*
    * 地图路径
    * */
    public static String MAP_DIRECTORY = "res/";

    /*
    * 地图列表
    * */
    public static java.util.Map<String, Map> MAPS = new HashMap<>();

    /*
    * 速度等级
    * */
    public static java.util.Map<Integer, String> LEVELS = new HashMap<>();
}
