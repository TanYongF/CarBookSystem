package client;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import pojo.Car;
import service.CarService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Describe: 租赁窗口
 * TODO：1. 订单完成后，需要刷新页面 2.订单页面不应该有登陆按钮
 * @Author: tyf
 * @CreateTime: 2022/5/7
 **/
public class BookFrame extends MainFrame {
    public static volatile String carDetails = "";
    public static DefaultTableModel dm = new DefaultTableModel();

    public static JTable table = new JTable(dm);
    public static JScrollPane jTablePane = new JScrollPane(table);

    JButton faf = new JButton("faf");

    public BookFrame() {
        refreshTable();
        setVisible(true);
    }

    public static void main(String[] args) {
        BookFrame bookFrame = new BookFrame();
        bookFrame.setVisible(true);
    }

    public static Vector<Vector<Object>> getMapKeyValue(Map map) {
        Vector<Vector<Object>> ret = new Vector<>();
        if ((map != null) && (!map.isEmpty())) {
            int size = map.size();
            Iterator iterator = map.entrySet().iterator();
            for (int i = 0; i < size; i++) {
                Vector<Object> row = new Vector<>();
                Map.Entry entry = (Map.Entry) iterator.next();
                Car car = JSONUtil.toBean((JSONObject) entry.getValue(), Car.class);
                row.add(car.getId());
                row.add(car.getName());
                row.add(car.getPrice());
                row.add(car.getCarId());
                row.add(car.getStock());
                ret.add(row);
            }
        }
        return ret;
    }

    public static void getCarsView() {
        carDetails = "";
        //获得可租赁汽车清单
        try {
            log.append("正在获得汽车详情中...");
            while (carDetails.isEmpty()) {
                carDetails = CarService.getCars();
            }
        } catch (IOException e) {
            log.append(e.getMessage());
        }
        ConcurrentHashMap carsMap = JSONUtil.toBean(carDetails, ConcurrentHashMap.class);
        Vector<Vector<Object>> data = getMapKeyValue(carsMap);
        Vector<String> head = new Vector<>();
        head.add("编号");
        head.add("型号");
        head.add("价格");
        head.add("车牌");
        head.add("剩余");
        head.add("Button");
        dm.getDataVector().clear();
        dm.setDataVector(data, head);
    }

    private void refreshTable(){
        getContentPane().removeAll();
        getCarsView();
        //添加渲染器
        table.getColumn("Button").setCellRenderer(new ButtonRenderer());
        //添加编辑器
        table.getColumn("Button").setCellEditor(new ButtonEditor(new JCheckBox()));
        add(jTablePane);
        JPanel jp3 = new JPanel();
        jp3.add(jScrollPane);
        add(jp3);
        faf.addActionListener(e -> {
            refreshTable();
        });
        JPanel jPanel = new JPanel();
        jPanel.add(faf);
        add(jPanel);
    }
}
