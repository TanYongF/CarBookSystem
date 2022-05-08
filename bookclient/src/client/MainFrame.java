package client;

import frame.LogArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

/**
 * @Describe: 父框架类，主要定义菜单等共用组件
 * @Author: tyf
 * @CreateTime: 2022/5/6
 **/
public class MainFrame extends JFrame {

    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 9091;
    public static Socket client;
    public static DataInputStream in;
    public static DataOutputStream out;
    public static LogArea log = new LogArea();

    public static JScrollPane jScrollPane = new JScrollPane(log);

    //类加载时机建立链接
    static {
        connect();
    }

    public final JMenu menuFile = new JMenu("登陆");
    public final JMenu menuFile1 = new JMenu("汽车租赁查询");
    public JMenuBar menuBar = new JMenuBar();
    public JTextArea text = new JTextArea();
    JLabel label = new JLabel("汽车租赁系统");

    public MainFrame() {
        createWindow();
    }

    public static void connect() {
        try {
            log.append("准备连接到主机：" + SERVER_ADDRESS + " ，端口号：" + SERVER_PORT + "");
            client = new Socket(SERVER_ADDRESS, SERVER_PORT);
            log.append("远程主机地址链接成功！地址" + client.getRemoteSocketAddress() + "");
            out = new DataOutputStream(client.getOutputStream());
            in = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        MainFrame mainFrame = new MainFrame();
//        mainFrame.setVisible(true);
    }

    void createWindow() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER, 200, 10));// 可编辑
        this.add(label, BorderLayout.CENTER);
        Font font = new Font("", 10, 30);
        label.setFont(font);
        label.setOpaque(true);
        label.setBackground(new Color(51, 153, 255, 255));
        JPanel jp3 = new JPanel();
        log.setEditable(false);
        log.setLineWrap(true);
        log.setVisible(true);
        jp3.add(jScrollPane);
        this.add(jp3);

        menuBar.add(menuFile);
        menuBar.add(menuFile1);

        JMenuItem managersItem = new JMenuItem("登陆", new ImageIcon("d:" + File.separator + "icons" + File.separator +
                "new.gif"));
        JMenuItem customersItemCar = new JMenuItem("车辆选择：轿车", new ImageIcon("d:" + File.separator + "icons" + File.separator + "open.gif"));
        JMenuItem customersItemBus = new JMenuItem("车辆选择：客车", new ImageIcon("d:" + File.separator + "icons" + File.separator + "open.gif"));

        menuFile.add(managersItem);
        menuFile1.add(customersItemCar);
        menuFile1.add(customersItemBus);

        managersItem.addActionListener(e -> {
            this.setVisible(false);
            Class loginClazz = LoginFrame.class;
            LoginFrame loginFrame = null;
            try {
                loginFrame = (LoginFrame) loginClazz.getDeclaredConstructor().newInstance();
            } catch (Exception ee) {
                ee.printStackTrace();
            }
            loginFrame.setVisible(true);
        });

        customersItemCar.addActionListener(e -> {
        });

        customersItemBus.addActionListener(e -> {
            text.append("=== 您正处于普通用户登录状态");
        });

        this.setTitle("Welcome To 汽车租赁系统");
        this.setJMenuBar(menuBar);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        this.setSize(500, 600);
        this.setLocationRelativeTo(null);//窗体居中显示
    }



}



