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
import org.noear.dami2.bus.Event;
import org.noear.dami2.bus.EventListener;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.bean.LifecycleBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 监听器的生命周期包装
 *
 * @author noear
 * @since 1.0
 */
public class ListenerLifecycleWrap implements LifecycleBean {
    List<ListenerRecord> listenerRecords = new ArrayList<>();

    public void add(String topicMapping, Object listener) {
        listenerRecords.add(new ListenerRecord(topicMapping, listener));
    }

    @Override
    public void start() throws Throwable {

    }

    @Override
    public void stop() throws Throwable {
        //停止时自动注销
        for (ListenerRecord r1 : listenerRecords) {
            if (r1.getListenerObj() instanceof EventListener) {
                Dami.bus().unlisten(r1.getTopicMapping(), (EventListener<Event<Object>>) r1.getListenerObj());
            } else {
                Dami.lpc().unregisterService(r1.getTopicMapping(), r1.getListenerObj());
            }
        }
    }

    /**
     * 获取实例
     */
    public static ListenerLifecycleWrap getOf(AppContext context) {
        ListenerLifecycleWrap lifecycleWrap = context.attachGet(ListenerLifecycleWrap.class);

        if (lifecycleWrap == null) {
            lifecycleWrap = new ListenerLifecycleWrap();
            context.attachSet(ListenerLifecycleWrap.class, lifecycleWrap);
            context.lifecycle(lifecycleWrap);
        }

        return lifecycleWrap;
    }
}
