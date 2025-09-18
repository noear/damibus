package features.demo31_lpc.module2;

/**
 * 提示：需要要支持类隔离的环境，请使用基本类型做为参数与返回
 * */
public interface UserService {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}
