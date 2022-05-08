import server.BookServer;

import java.io.IOException;

import static config.ServerConfig.SERVER_PORT;

/**
 * @Describe: 服务端启动类
 * @Author: tyf
 * @CreateTime: 2022/5/8
 **/
public class ServerStarter {

    public static void main(String[] args) {
        try {
            BookServer server = new BookServer(SERVER_PORT);
            server.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
