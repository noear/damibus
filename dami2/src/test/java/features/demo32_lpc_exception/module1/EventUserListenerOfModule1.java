package features.demo32_lpc_exception.module1;

/**
 * 通过约定保持与 Sender 相同的接口定义（或者实现 UserEventSender 接口）
 * */
public class EventUserListenerOfModule1 {
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
        throw new RuntimeException("测试异常");
    }
}
