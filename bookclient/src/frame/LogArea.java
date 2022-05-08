package frame;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Describe: 自定义输入框，日志框
 * @Author: tyf
 * @CreateTime: 2022/5/6
 **/
public class LogArea extends JTextArea {

    public LogArea() {
        super("", 20, 35);
    }

    @Override
    public void append(String str) {
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd-HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        super.append(String.format("【%s】:%s\n", sdf.format(date), str));
    }

}
