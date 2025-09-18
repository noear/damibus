/*
 * Copyright 2023～ noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.dami2.solon;

import org.noear.dami2.Dami;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.solon.annotation.DamiTopic;
import org.noear.solon.Solon;
import org.noear.solon.core.BeanBuilder;
import org.noear.solon.core.BeanWrap;

/**
 * TopicMapping 构建器
 *
 * @author noear
 * @since 1.0
 */
public class DamiTopicBeanBuilder implements BeanBuilder<DamiTopic> {
    @Override
    public void doBuild(Class<?> clz, BeanWrap bw, DamiTopic anno) throws Throwable {
        if (clz.isInterface()) {
            Object raw = Dami.lpc().createConsumer(anno.value(), clz);
            bw.context().wrapAndPut(clz, raw);
        } else {
            //增加代理支持
            bw.context().beanExtractOrProxy(bw);

            if (EventListener.class.isAssignableFrom(clz)) {
                Dami.bus().listen(anno.value(), anno.index(), bw.raw());
            } else {
                Dami.lpc().registerService(anno.value(), anno.index(), bw.raw());
            }

            lifecycleWrap(bw, anno.value());
        }
    }

    /**
     * 包装生命周期
     */
    private void lifecycleWrap(BeanWrap bw, String topicMapping) {
        if (Solon.context() != bw.context()) {
            //如果不是根容器，则停止时自动注销
            ListenerLifecycleWrap lifecycleWrap = ListenerLifecycleWrap.getOf(bw.context());

            lifecycleWrap.add(topicMapping, bw.raw());
        }
    }
}
