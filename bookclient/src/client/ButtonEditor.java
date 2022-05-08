package client;

/**
 * @Describe:
 * @Author: tyf
 * @CreateTime: 2022/5/7
 **/
import service.CarService;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import java.io.IOException;

/**
 * 表格中按钮中的按钮事件
 */
public class ButtonEditor extends DefaultCellEditor {
    protected JButton button;//represent the cellEditorComponent
    private long carId;//保存cellEditorValue
    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(e -> {
            try {
                CarService.bookCars(carId);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JOptionPane.showMessageDialog(button, "编号为"+ carId + "的车租赁成功");
            //刷新渲染器
            fireEditingStopped();
        });
    }
    @Override
    public JComponent getTableCellEditorComponent(JTable table, Object value,
                                                  boolean isSelected, int row, int column) {
        //value 源于单元格数值
        carId = (long)table.getValueAt(row, 0);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return carId;
    }


}