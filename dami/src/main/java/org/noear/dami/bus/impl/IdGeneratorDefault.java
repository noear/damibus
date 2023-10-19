package org.noear.dami.bus.impl;

import org.noear.dami.bus.IdGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Id 生成器默认器
 *
 * @author noear
 * @since 1.0
 */
public class IdGeneratorDefault implements IdGenerator {
    private long basetime;
    private AtomicLong count = new AtomicLong();

    /**
     * 创建
     */
    @Override
    public String generate() {
        long tmp = System.currentTimeMillis() / 1000;
        if (tmp != basetime) {
            synchronized (count) {
                basetime = tmp;
                count.set(0);
            }
        }

        //1秒内可容1亿
        return String.valueOf(basetime * 1000 * 1000 * 1000 + count.incrementAndGet());
    }
}
