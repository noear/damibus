package org.noear.dami.bus;

import org.noear.dami.exception.DamiException;

/**
 * @author noear
 * @since 1.0
 */
public class AssertUtil {
    /**
     * 断言主题是否为空
     *
     * @param topic
     */
    public static void assertTopic(final String topic) {
        if (topic == null || topic.isEmpty()) {
            throw new DamiException("The topic cannot be empty");
        }
    }
}
