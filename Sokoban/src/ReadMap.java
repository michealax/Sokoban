import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Soul.专情 on 2018/3/23.
 */
public class ReadMap {
    private int level, manX, manY, mapRow, mapCol;
    private FileReader filereader;
    private BufferedReader bufferedreader;
    private String lines = "";
    private int[][] map;

    //读取地图
    ReadMap(int k) {
        level = k;

        //读取相应关卡的地图
        try {
            File f = new File("maps\\" + level + ".map");
            filereader = new FileReader(f);
            bufferedreader = new BufferedReader(filereader);
        } catch (IOException e) {
            System.err.println();
        }
        try {
            String s;
            s = bufferedreader.readLine();
            mapRow = Integer.parseInt((s.trim().split("\\s+"))[1]);//读取地图的Row
            mapCol = Integer.parseInt((s.trim().split("\\s+"))[0]);//读取地图的Col

            //将地图每一行的字符串连接
            while ((s = bufferedreader.readLine()) != null) {
                lines = lines + s;
            }
        } catch (IOException e) {
            System.err.println();
        }

        //初始化地图int类型二维数组
        map = new int[mapRow][mapCol];

        byte[] d = lines.getBytes();
        int len = lines.length();
        int[] x = new int[len];
        int c = 0;
        for (int i = 0; i < len; i++)
            x[i] = d[i] - 48;

        //将每一个数字存在二位地图数组中，并返回人物所在位置，其中初始化中人物被数字5所代替
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapCol; j++) {
                map[i][j] = x[c];
                if (map[i][j] == 5) {
                    manX = i;
                    manY = j;
                }
                c++;
            }
        }
    }

    //得到地图
    int[][] getMap() {
        return map;
    }

    //得到人所在row
    int getmanX() {
        return manX;
    }

    //得到人所在col
    int getmanY() {
        return manY;
    }

    //得到地图的row
    int getRow() {
        return mapRow;
    }

    //得到地图的col
    int getCol() {
        return mapCol;
    }


}
