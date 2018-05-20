import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Created by Soul.专情 on 2018/3/23.
 */
/*初始化游戏页面及其构件*/
public class SokobanFrame extends JFrame implements ActionListener, ItemListener {
    private JLabel label;//界面
    private JButton btNext, btPrev, btUndo, btRestart; //游戏所有功能区
    private SokobanPanel panel; //游戏显示区

    SokobanFrame() {
        super("Sokoban");
        setSize(720, 720);
        setVisible(true);
        setResizable(false);
        setLocation(600, 100);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 默认界面退出同时程序运行结束
        Container container = getContentPane();
        container.setLayout(null);
        container.setBackground(Color.lightGray);


        label = new JLabel("Level 1", SwingConstants.CENTER);
        add(label);
        label.setBounds(100, 20, 400, 20);
        label.setForeground(Color.WHITE);

        btPrev = new JButton("Prev");
        btNext = new JButton("Next");
        btUndo = new JButton("Undo");
        btRestart = new JButton("Restart");
        add(btPrev);
        add(btNext);
        add(btUndo);
        add(btRestart);

        btPrev.setBounds(620, 200, 80, 30);
        btPrev.addActionListener(this);
        btNext.setBounds(620, 250, 80, 30);
        btNext.addActionListener(this);
        btUndo.setBounds(620, 300, 80, 30);
        btUndo.addActionListener(this);
        btRestart.setBounds(620, 150, 80, 30);
        btRestart.addActionListener(this);

        panel = new SokobanPanel();
        add(panel);
        panel.Sokoban(panel.level);
        panel.requestFocus();
        validate();
    }

    //按钮及输入时间监听驱动， btRestart为重新开始，btPrev为上一关， btNext为下一关， btUndo为撤销所走
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btRestart) {
            panel.Sokoban(panel.level);
            panel.remove();
            panel.requestFocus();
        } else if (e.getSource() == btPrev) {
            panel.level--;
            if (panel.level < 1) {
                panel.level++;
                JOptionPane.showMessageDialog(this, "已是第一关");
                panel.requestFocus();
            } else {
                panel.Sokoban(panel.level);
                panel.requestFocus();
            }
            panel.remove();
        } else if (e.getSource() == btNext) {
            panel.level++;
            if (panel.level > panel.getMaxLevels()) {
                panel.level--;
                JOptionPane.showMessageDialog(this, "已是最后一关");
                panel.requestFocus();
            } else {
                panel.Sokoban(panel.level);
                panel.requestFocus();
            }

            panel.remove();
        } else if (e.getSource() == btUndo) {
            if (panel.isStackEmpty())
                JOptionPane.showMessageDialog(this, "You have not moved it");
            else {
                panel.undo();
            }
            panel.requestFocus();
        }
        label.setText("Level " + panel.level);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
}
