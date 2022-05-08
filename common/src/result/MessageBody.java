package result;

/**
 * @Describe: 消息体
 * @Author: tyf
 * @CreateTime: 2022/5/4
 **/
public class MessageBody {
    /**
     * GET_CARS: 获取车辆信息
     * POST_CARS:租赁汽车
     * LOGIN: 登陆功能
     */
    private String type;

    private String id;

    private String message;


    public MessageBody(String type) {
        this(type, "", "");
    }

    public MessageBody(String type, String id, String message) {
        this.type = type;
        this.id = id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "MessageBody{" +
                "type='" + type + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
