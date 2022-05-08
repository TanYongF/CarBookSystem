package server;

import pojo.Car;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static config.ServerConfig.*;

/**
 * 服务器主程序，负责初始化数据以及接收用户请求并链接
 */
public class BookServer extends Thread {

    public static ConcurrentHashMap<String, String> session = new ConcurrentHashMap<>();
    final JTextArea text = new JTextArea();
    private HashMap<String, String> userMap = new HashMap<>();
    private ConcurrentHashMap<Long, Car> carsMap = new ConcurrentHashMap<>();
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    private ServerSocket serverSocket;

    public BookServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(0);
    }

    /**
     * 服务器启动方法
     */
    public void run() {
        //创建端口
        createWindow();
        //初始化
        init();
        //循环等待客户端连接
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                threadPool.submit(new ServerThread(socket, userMap, carsMap, text));
            } catch (SocketTimeoutException s) {
                text.append("Socket timed out!\n");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * 初始化服务器具体方法
     * 具体流程：
     * 1. 加载用户配置文件
     * 2. 加载车辆配置文件
     * 3. 打印 Banner
     */
    private void init() {
        text.append(String.format("Car book system server starting at %s\n", serverSocket.getLocalSocketAddress()));
        try {
            parseUserDetails();
            parseCarsDetails();
            printBanner();
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

        text.append("服务端启动完成!\n");
        text.append("等待远程连接，端口号为：" + serverSocket.getLocalPort() + "...\n");
    }



    /**
     * 读取用户配置文件, 具体是 USER_FILE 路径下读入， 将其使用 Map 来保存
     */
    private void parseUserDetails() throws IOException {
        text.append("正在加载用户配置文件...\n");
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(USER_FILE), "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] userDetail = line.split(",");
                String username = userDetail[0];
                String password = userDetail[1];
                userMap.put(username, password);
            }
        } catch (IOException e) {
            System.err.println("文件格式错误！\n");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 解析车辆配置文件，具体是 UCAR_FILE 路径下读入， 将其装载至内存，以 Map 来保存
     */
    private void parseCarsDetails() throws IOException {
        String line;
        text.append("正在加载车辆配置文件...\n");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(CAR_FILE), "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                Car car = new Car();
                car.setId(Long.parseLong(details[0]));
                car.setName(details[1]);
                car.setCarId(details[2]);
                car.setPrice(Long.parseLong(details[3]));
                car.setStock(Long.parseLong(details[4]));
                car.setType(Integer.parseInt(details[5]));
                carsMap.put(car.getId(), car);
            }
        } catch (IOException e) {
            System.err.println("文件格式错误！\n");
            e.printStackTrace();
            throw e;
        }
    }


    /**
     * 服务器启动，打印 banner 图标
     */
    private void printBanner() {
        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(BANNER_FILE), "utf-8"))) {
            while ((line = reader.readLine()) != null) {
                text.append(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("banner打印错误！\n");
            e.printStackTrace();
        }
    }

    /**
     * 渲染窗口方法
     */
    private void createWindow() {
        JFrame frame = new JFrame("汽车租赁系统【服务端】");
        frame.getContentPane().add(new JScrollPane(text));    // 加入滚动条
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(1);
            }
        });
        frame.setVisible(true);
        frame.setSize(700, 700);
        frame.setLocation(300, 200);
    }

}