package org.noear.dami.bus.impl;

import org.noear.dami.bus.IdGenerator;

import java.util.UUID;

/**
 * Id 生成器 guid 实现
 *
 * @author noear
 * @since 1.0
 */
public class IdGeneratorGuid implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
