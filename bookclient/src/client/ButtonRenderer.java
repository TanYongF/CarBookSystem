package client;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @Describe: 负责表格中的渲染器
 * @Author: tyf
 * @CreateTime: 2022/5/7
 **/
public class ButtonRenderer extends JButton implements TableCellRenderer {
    public JComponent getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus, int row, int column) {
        //value 源于editor
        String text = "租赁该车";
        long stock = (long) table.getValueAt(row, column - 1);
        if(stock <= 0){
            setBackground(Color.gray);
        }else if(stock <= 10){
            setBackground(Color.YELLOW);
        }else{
            setBackground(Color.GREEN);
        }
        //按钮文字
        setText(text);
        //单元格提示
        setToolTipText(text);
        //背景色
        setBackground(Color.BLUE);
        //前景色
        setForeground(Color.WHITE);
        return this;
    }
}