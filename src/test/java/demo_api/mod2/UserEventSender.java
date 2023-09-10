package demo_api.mod2;

import org.noear.dami.api.marker.Sender;

/**
 * 提示：需要要支持类隔离的环境，请使用基本类型做为参数与返回
 * */
public interface UserEventSender extends Sender {
    long created(long userId, String name);
    void updated(long userId, String name);
}
