package week3;

import cn.hutool.json.JSONUtil;
import result.CodeMsg;
import result.MessageBody;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * @Describe: 并发测试
 * @Author: tyf
 * @CreateTime: 2022/5/5
 **/
public class Test {

    public static final String SERVER_ADDRESS = "127.0.0.1";
    public static final int SERVER_PORT = 9091;


    public static void main(String[] args) {

        for (int i = 0; i < 40; i++) {
            new Thread(() -> {
                try {
                    System.out.println("准备连接到主机：" + SERVER_ADDRESS + " ，端口号：" + SERVER_PORT);
                    Socket client = new Socket(SERVER_ADDRESS, SERVER_PORT);
                    System.out.println(client.getLocalPort());
//                    client.bind(new InetSocketAddress(18000+ finalI));
//                    client.connect(new InetSocketAddress(SERVER_ADDRESS, SERVER_PORT));
                    System.out.println("远程主机地址链接成功！地址" + client.getRemoteSocketAddress());
                    DataOutputStream out = new DataOutputStream(client.getOutputStream());
                    DataInputStream in = new DataInputStream(client.getInputStream());
                    //登陆验证
                    System.out.println("请输入账号密码登陆");
                    while (true) {
                        //输入密码
                        String token = "tan 1234";
                        MessageBody loginBody = new MessageBody("LOGIN", "-1", token);
                        out.writeUTF(JSONUtil.toJsonStr(loginBody));
                        out.flush();
                        CodeMsg codeMsg = JSONUtil.toBean(in.readUTF(), CodeMsg.class);
                        if (codeMsg.code == CodeMsg.LOGIN_SUCCESS.code) {
                            System.out.println("服务器提示： " + codeMsg.message);
                            break;
                        } else {
                            System.out.println(codeMsg);
                        }
                    }
                    test2(out);
                    System.out.println(Thread.currentThread().getName() + ":" + in.readUTF());
                    Thread.sleep(1000 * 1000);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }, "Thread-" + i).start();
        }
    }

    static void test1(DataOutputStream out) throws IOException {
        MessageBody get_cars = new MessageBody("GET_CARS");
        out.writeUTF(JSONUtil.toJsonStr(get_cars));
    }

    static void test2(DataOutputStream out) throws IOException {
        MessageBody post_cars = new MessageBody("POST_CARS", "1", "");
        out.writeUTF(JSONUtil.toJsonStr(post_cars));
    }
}
