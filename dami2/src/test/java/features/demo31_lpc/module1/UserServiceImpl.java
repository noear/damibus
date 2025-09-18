package features.demo31_lpc.module1;

/**
 * 通过约定保持与 UserService 相同的接口定义（或者实现 UserService 接口）
 * */
public class UserServiceImpl {
    public void onCreated(Long userId, String name) {
        System.err.println("onCreated: userId=" + userId + ", name=" + name);
    }

    public Long getUserId(String name) {
        return 99L;
    }
}
