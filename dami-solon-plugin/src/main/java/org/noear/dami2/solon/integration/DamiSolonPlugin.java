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
package org.noear.dami2.solon.integration;

import org.noear.dami2.Dami;
import org.noear.dami2.DamiConfig;
import org.noear.dami2.lpc.Coder;
import org.noear.dami2.bus.Interceptor;
import org.noear.dami2.solon.DamiTopicBeanBuilder;
import org.noear.dami2.solon.annotation.DamiTopic;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;

/**
 * @author noear
 * @since 1.0
 */
public class DamiSolonPlugin implements Plugin {

    @Override
    public void start(AppContext context) throws Throwable {
        context.beanBuilderAdd(DamiTopic.class, new DamiTopicBeanBuilder());

        context.subWrapsOfType(Interceptor.class, wrap -> {
            Dami.bus().intercept(wrap.index(), wrap.raw());
        });

        context.getBeanAsync(Coder.class, bean -> {
            DamiConfig.configure(bean);
        });
    }
}
