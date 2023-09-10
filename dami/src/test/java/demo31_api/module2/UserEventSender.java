package demo31_api.module2;

/**
 * 提示：需要要支持类隔离的环境，请使用基本类型做为参数与返回
 * */
public interface UserEventSender {
    void onCreated(Long userId, String name);
    Long getUserId(String name);
}
