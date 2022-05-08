package server;

import cn.hutool.json.JSONUtil;
import pojo.Car;
import result.CodeMsg;
import result.MessageBody;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static config.ServerConfig.CAR_FILE;

/**
 * @Describe: 每个线程对应一个用户
 * @Author: tyf
 * @CreateTime: 2022/5/3
 **/
public class ServerThread extends Thread {

    //用来负责并发下单租赁汽车时候的锁。
    private static ReentrantLock lock = new ReentrantLock(true);
    private final JTextArea textArea;
    private Socket socket;
    private HashMap userMap;

    private ConcurrentHashMap<Long, Car> carsMap;

    public ServerThread(Socket socket, HashMap<String, String> userMap, ConcurrentHashMap<Long, Car> cars, JTextArea text) {
        this.socket = socket;
        this.userMap = userMap;
        this.carsMap = cars;
        this.textArea = text;
    }

    /**
     * 具体处理逻辑
     */
    @Override
    public void run() {
        textArea.append(String.format("【客户端 %s : %d 请求连接】\n", socket.getRemoteSocketAddress().toString(),
                socket.getPort()));
        DataInputStream in;
        DataOutputStream out;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            while (true) {
                String message = in.readUTF(), response = "";
                MessageBody msg = JSONUtil.toBean(message, MessageBody.class);
                textArea.append(msg.toString() + "\n");
                switch (msg.getType()) {
                    case "LOGIN":
                        response = login(msg);
                        break;
                    case "GET_CARS":
                        response = getCarsDetails(msg);
                        break;
                    case "POST_CARS":
                        response = postCarsDetails(msg);
                        syncDisk();
                        break;
                    default:
                        response = JSONUtil.toJsonStr(CodeMsg.NOT_FOUNT_OPTION.message);
                }
                out.writeUTF(response);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 数据持久化到磁盘，可以考虑另起一个线程持久化
     */
    private void syncDisk() {
        try (FileWriter writer = new FileWriter(CAR_FILE, Charset.forName("utf-8"))) {
            for (Long key : carsMap.keySet()) {
                Car car = carsMap.get(key);
                String line = String.format("%s,%s,%s,%s,%s,%s\n", car.getId(), car.getName(), car.getCarId(), car.getPrice(),
                        car.getStock(), car.getType());
                writer.write(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 具体租赁汽车接口实现
     * 1. 判断库存
     * 2. 加锁
     * 3. 判断库存，减去库存（双重校验）
     * 4. 解锁
     * @param msg : 客户端传入的消息体
     * @return
     */
    private String postCarsDetails(MessageBody msg) {
        String str = msg.getId();
        if (str == null || str.isEmpty() || !carsMap.containsKey(Long.parseLong(str))) {
            return JSONUtil.toJsonStr(CodeMsg.CAR_NOT_FOUNT);
        }
        Long id = Long.parseLong(str);
        long stock = carsMap.get(id).getStock();

        //判断库存,如果库存为空，那么直接返回。
        if (stock <= 0) {
            return JSONUtil.toJsonStr(CodeMsg.CAR_NO_STOCK);
        }
        //减少库存，使用锁机保证并发情况下不会出现超卖等情况。
        lock.lock();
        try {
            stock = carsMap.get(id).getStock();
            if (stock <= 0) {
                return JSONUtil.toJsonStr(CodeMsg.CAR_NO_STOCK);
            } else {
                carsMap.get(id).setStock(stock - 1);
                textArea.append(socket.getRemoteSocketAddress() + "租赁成功！, 剩余" + carsMap.get(id).getStock() + "\n");
                return JSONUtil.toJsonStr(CodeMsg.CAR_BOOK_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return JSONUtil.toJsonStr(CodeMsg.REMOTE_FAILED);
    }

    /**
     * 获得所有车辆信息
     *
     * @param msg ：用户消息体
     * @return JSON字符串
     */
    private String getCarsDetails(MessageBody msg) {
        String id = msg.getId();
        String str;
        if (id == "" || id.isEmpty()) {
            str = JSONUtil.toJsonStr(carsMap);
        } else {
            if (carsMap.containsKey(Long.getLong(id))) {
                str = JSONUtil.toJsonStr(carsMap.get(Long.getLong(id)));
            } else {
                str = JSONUtil.toJsonStr(CodeMsg.CAR_NOT_FOUNT);
            }
        }
        return str;
    }

    /**
     * 登陆接口
     *
     * @param msg
     */
    public String login(MessageBody msg) {
        String ret;
        //读取客户端输入的信息
        String[] s = msg.getMessage().split(" ");
        String userName = s[0], password = s[1];
        //校验用户
        if (userName.isEmpty() || password.isEmpty() || !userMap.containsKey(userName)) {
            ret = JSONUtil.toJsonStr(CodeMsg.LOGIN_NO_USER);
        } else if (userMap.get(userName).equals(password)) {
            ret = JSONUtil.toJsonStr(CodeMsg.LOGIN_SUCCESS);
        } else {
            ret = JSONUtil.toJsonStr(CodeMsg.LOGIN_PASSWORD_WRONG);
        }
        return ret;
    }

}
