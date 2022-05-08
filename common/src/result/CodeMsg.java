package result;

/**
 * @Describe: 类描述
 * @Author: tyf
 * @CreateTime: 2022/5/4
 **/
public class CodeMsg {

    public static final CodeMsg NOT_FOUNT_OPTION = new CodeMsg(1001, "用户指令未识别！");
    public static final CodeMsg LOGIN_SUCCESS = new CodeMsg(4000, "登陆成功！");
    public static final CodeMsg LOGIN_NO_USER = new CodeMsg(4001, "用户验证未成功！");
    public static final CodeMsg LOGIN_PASSWORD_WRONG = new CodeMsg(4002, "用户验证未成功！");
    public static final CodeMsg CAR_BOOK_SUCCESS = new CodeMsg(2000, "车辆租赁成功！");
    public static final CodeMsg CAR_NOT_FOUNT = new CodeMsg(2001, "符合条件用户车辆未找到");
    public static final CodeMsg CAR_NO_STOCK = new CodeMsg(2002, "该车辆已经没有租赁余额");
    public static final CodeMsg REMOTE_FAILED = new CodeMsg(5000, "内部出现错误，请联系管理员");

    public int code;
    public String message;

    public CodeMsg(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
