import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Stack;

/**
 * Created by Soul.专情 on 2018/3/23.
 */
/*游戏区并包含游戏的功能及其运行*/
class SokobanPanel extends JPanel implements KeyListener {
    private static final int  STATE_UP= 10;
    private static final int  STATE_DOWN= 20;
    private static final int  STATE_LEFT= 30;
    private static final int  STATE_RIGHT= 40;
    int level = 1;//游戏关卡
    private int maxLevels = 11;//游戏地图最大数量
    private int[][] map;//二维地图数组
    private int manX, manY, mapRow, mapCol;//人所在位置row，col， 和地图row， col数量
    private Image[] images;//地图小构建图片
    private static final int ICON_SIZE = 48;//游戏构建尺寸大小
    private Stack<Integer> stack = new Stack();//存放移动情况

    SokobanPanel() {
        setBounds(15, 50, 600, 600);
        setForeground(Color.WHITE);
        addKeyListener(this);
        setImages(12);
        setVisible(true);
    }

    //获得上一步方向， 以便悔棋， 默认方向为向下， 用数字5表示
    private static int directions(int state) {
        int dir = 5;
        switch (state) {
            case 10:
                dir = 8;
                break;
            case 20:
                dir = 5;
                break;
            case 30:
                dir = 6;
                break;
            case 40:
                dir = 7;
                break;
        }
        return dir;
    }

    //从目录文件获得游戏相关构件图片
    private void setImages(int size) {
        images = new Image[size];
        for (int i = 0; i < 11; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage("pic\\" + i + ".png");
        }
        images[11] = Toolkit.getDefaultToolkit().getImage("pic\\b.png");
    }

    //画出地图
    void Sokoban(int level) {
        ReadMap levelMap = new ReadMap(level);
        map = levelMap.getMap();
        manX = levelMap.getmanX();
        manY = levelMap.getmanY();
        mapRow = levelMap.getRow();
        mapCol = levelMap.getCol();
        repaint();
    }

    //得到最大关卡数
    int getMaxLevels() {
        return maxLevels;
    }

    //在游戏去画出游戏图
    public void paint(Graphics g) {
        g.drawImage(images[11], 0, 0, this);
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapCol; j++) {
                if (map[i][j] != 0)
                    g.drawImage(images[map[i][j]], j * ICON_SIZE, i * ICON_SIZE, this);
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    //游戏操作其中W，S, A, D分别代表上下左右， 另外方向键也可以操作
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                ;
            case KeyEvent.VK_W:
                moveUp();
                break;
            case KeyEvent.VK_DOWN:
                ;
            case KeyEvent.VK_S:
                moveDown();
                break;
            case KeyEvent.VK_LEFT:
                ;
            case KeyEvent.VK_A:
                moveLeft();
                break;
            case KeyEvent.VK_RIGHT:
                ;
            case KeyEvent.VK_D:
                moveRight();
                break;
        }
        if (isWin()) {
            if (level == maxLevels) {
                JOptionPane.showMessageDialog(this, "The last level");
            } else {
                level++;
                Sokoban(level);
            }
            stack.removeAllElements();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    //判断移动记录是否为空
    boolean isStackEmpty() {
        return stack.isEmpty();
    }

    //删除移动记录，在直接跳关和重新开始时应用
    void remove() {
        stack.removeAllElements();
    }

    //向上移动
    private void moveUp() {
        move(manX - 1, manY, manX - 2, manY, STATE_UP, 8);
    }

    //向下移动
    private void moveDown() {
        move(manX + 1, manY, manX + 2, manY, STATE_DOWN, 5);
    }

    //向左移动
    private void moveLeft() {
        move(manX, manY - 1, manX, manY - 2, STATE_LEFT, 6);
    }

    //向右移动
    private void moveRight() {
        move(manX, manY + 1, manX, manY + 2, STATE_RIGHT, 7);
    }

    //悔棋逻辑， 分为上一步不动， 不推箱子移动， 推箱子移动，状态码与移动时记录一致， 有十二种
    private void back(int state, int prevPosX, int prevPosY, int nextPosX, int nextPosY) {
        if (state % 10 == 0) {
            if (map[manX][manY] == 10)
                map[manX][manY] = 4;
            else
                map[manX][manY] = 2;


        } else {
            if (map[nextPosX][nextPosY] == 9)
                map[nextPosX][nextPosY] = 4;
            else
                map[nextPosX][nextPosY] = 2;
            if (map[manX][manY] == 10)
                map[manX][manY] = 9;
            else map[manX][manY] = 3;
        }
        if (map[prevPosX][prevPosY] == 4)
            map[prevPosX][prevPosY] = 10;
        else {
            if (!stack.isEmpty())
                map[prevPosX][prevPosY] = directions(stack.peek());
            else
                map[prevPosX][prevPosY] = 5;
        }
        repaint();
        manX = prevPosX;
        manY = prevPosY;
    }

    //判断是否箱子都在点上，若是则进入下一关
    private Boolean isWin() {
        boolean win = false;
        out:
        for (int i = 0; i < mapRow; i++) {
            for (int j = 0; j < mapCol; j++) {
                if (map[i][j] == 4 || map[i][j] == 9) {
                    if (map[i][j] == 9)
                        win = true;
                    else {
                        win = false;
                        break out;
                    }
                }
            }
        }
        return win;
    }

    //移动逻辑， 分为不推箱子移动，对应上下左右状态码为10， 20， 30， 40； 推箱子移动，分别状态码加1； 不动分别状态码加2//
    /*                                   |--墙
    *                                    |--箱子
    *                            |--箱子--
    *                            |       |--点
    *                    NO   |--        |--空地
    *                         |  |--墙
    * 下一位置是否有箱子或墙存在--
    *                         |  |--空地
    *                    YES  |--
    *                            |--空点
    * */
    private void move(int nextPosX1, int nextPosY1, int nextPosX2, int nextPosY2, int state, int direction) {
        if (map[nextPosX1][nextPosY1] == 2) {
            if (map[manX][manY] == 10)
                map[manX][manY] = 4;
            else
                map[manX][manY] = 2;
            map[nextPosX1][nextPosY1] = direction;
            repaint();
            manX = nextPosX1;
            manY = nextPosY1;
            stack.push(state);
        } else if (map[nextPosX1][nextPosY1] == 4) {
            if (map[manX][manY] == 10)
                map[manX][manY] = 4;
            else
                map[manX][manY] = 2;
            map[nextPosX1][nextPosY1] = 10;
            repaint();
            manX = nextPosX1;
            manY = nextPosY1;
            stack.push(state);
        } else {
            if (map[nextPosX1][nextPosY1] != 1) {
                if (map[nextPosX2][nextPosY2] == 2 || map[nextPosX2][nextPosY2] == 4) {
                    if (map[manX][manY] == 10)
                        map[manX][manY] = 4;
                    else map[manX][manY] = 2;
                    if (map[nextPosX1][nextPosY1] == 9)
                        map[nextPosX1][nextPosY1] = 10;
                    else
                        map[nextPosX1][nextPosY1] = direction;
                    if (map[nextPosX2][nextPosY2] == 2)
                        map[nextPosX2][nextPosY2] = 3;
                    else
                        map[nextPosX2][nextPosY2] = 9;
                    repaint();
                    manX = nextPosX1;
                    manY = nextPosY1;
                    stack.push(state + 1);
                }
            } else
                stack.push(state + 2);
        }
    }

    //悔棋操作
    void undo() {
        int state = stack.pop();
        switch (state) {
            case 12:
                ;
            case 22:
                ;
            case 32:
                ;
            case 42:
                break;
            case 10:
                ;
            case 11:
                back(state, manX + 1, manY, manX - 1, manY);
                break;
            case 20:
                ;
            case 21:
                back(state, manX - 1, manY, manX + 1, manY);
                break;
            case 30:
                ;
            case 31:
                back(state, manX, manY + 1, manX, manY - 1);
                break;
            case 40:
            case 41:
                back(state, manX, manY - 1, manX, manY + 1);
                break;
        }
    }
}