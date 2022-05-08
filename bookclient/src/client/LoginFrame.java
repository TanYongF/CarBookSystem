package client;

import cn.hutool.json.JSONUtil;
import result.CodeMsg;
import result.MessageBody;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @Describe: 登陆窗口
 * @Author: tyf
 * @CreateTime: 2022/5/6
 **/
public class LoginFrame extends MainFrame {

    JLabel userNameLabel = new JLabel("姓名");
    JLabel passWordLabel = new JLabel("密码");
    JTextField usernameField = new JTextField(20);
    JTextField passwordField = new JPasswordField(20);
    JButton button = new JButton("登陆");

    public LoginFrame() {
        //添加控件
        JPanel jp1 = new JPanel();
        JPanel jp2 = new JPanel();
        JPanel jp3 = new JPanel();
        jp2.add(userNameLabel);
        jp2.add(usernameField);
        jp1.add(passWordLabel);
        jp1.add(passwordField);
        jp3.add(jScrollPane);

        this.add(jp2);
        this.add(jp1);
        this.add(button);
        this.add(jp3);

        button.addActionListener(e -> {
            String userName = usernameField.getText();
            String password = passwordField.getText();
            if (userName == null || userName.isEmpty() || password == null || password.isEmpty()) {
                log.append("输入错误！请重新输入！");
            } else {
                login(userName, password);
            }
        });
        log.append("请输入账号密码登陆");

    }

    public static void main(String[] args) {
    }

    /**
     * 登陆功能
     * @param userNameValue
     * @param passwordValue
     */
    private void login(String userNameValue, String passwordValue) {
        //输入密码
        String token = userNameValue + " " + passwordValue;
        MessageBody loginBody = new MessageBody("LOGIN", "-1", token);
        try {
            out.writeUTF(JSONUtil.toJsonStr(loginBody));
            out.flush();
            CodeMsg codeMsg = JSONUtil.toBean(in.readUTF(), CodeMsg.class);
            if (codeMsg.code == CodeMsg.LOGIN_SUCCESS.code) {
                log.append("服务器提示： " + codeMsg.message);
                Class<BookFrame> bookFrameClazz = BookFrame.class;
                BookFrame bookFrame = bookFrameClazz.getConstructor().newInstance();
                setVisible(false);
            } else {
                usernameField.setText("");
                passwordField.setText("");
                log.append(codeMsg.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 增加Log日志框换行
     * @param num
     */
    private void LineFeed(int num) {
        String emptyString = "";
        for (int i = 0; i < num; i++) emptyString += " ";
        this.add(new JLabel(emptyString));
    }
}
