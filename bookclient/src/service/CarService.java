package service;

import client.MainFrame;
import cn.hutool.json.JSONUtil;
import result.MessageBody;

import java.io.IOException;

/**
 * @Describe: 租车系统业务层
 * @Author: tyf
 * @CreateTime: 2022/5/7
 **/
public class CarService {

    public static void bookCars(long carId) throws IOException {
        MessageBody post_cars = new MessageBody("POST_CARS", String.valueOf(carId), "");
        MainFrame.out.writeUTF(JSONUtil.toJsonStr(post_cars));
    }

    public static String getCars() throws IOException {
        MessageBody get_cars = new MessageBody("GET_CARS");
        MainFrame.out.writeUTF(JSONUtil.toJsonStr(get_cars));
        return MainFrame.in.readUTF();
    }

}
