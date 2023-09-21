package org.noear.dami.bus.impl;

import org.noear.dami.api.impl.MethodTopicListener;
import org.noear.dami.bus.*;
import org.noear.dami.exception.DamiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 主题路由器（模式匹配实现方案；支持 * 和 ** 占位符；支持 / 或 . 做为间隔）
 *
 *
 * @example /a/*, /a/**b
 * @author noear
 * @since 1.0
 */
public class TopicRouterPatterned<C,R> extends TopicRouterBase<C,R> {
    static final Logger log = LoggerFactory.getLogger(TopicRouterHashtable.class);

    /**
     * 主题路由记录
     */
    private List<Routing<C, R>> routingList = new ArrayList<>();


    public TopicRouterPatterned() {
        super();
    }


    /**
     * 添加监听
     *
     * @param topic    主题
     * @param index    顺序位
     * @param listener 监听器
     */
    @Override
    public synchronized void add(final String topic, final int index, final TopicListener<Payload<C, R>> listener) {
        assertTopic(topic);

        routingList.add(new Routing<>(topic, index, listener));
        routingList.sort(Comparator.comparing(x -> x.getIndex()));

        if (log.isDebugEnabled()) {
            if (MethodTopicListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("TopicRouter listener added(@{}): {}", topic, listener);
            } else {
                log.debug("TopicRouter listener added(@{}): {}", topic, listener.getClass().getName());
            }
        }
    }

    /**
     * 移除监听
     *
     * @param topic    主题
     * @param listener 监听器
     */
    @Override
    public synchronized void remove(final String topic, final TopicListener<Payload<C, R>> listener) {
        assertTopic(topic);

        for (int i = 0; i < routingList.size(); i++) {
            Routing<C, R> routing = routingList.get(i);
            if (routing.matches(topic)) {
                if (routing.getListener() == listener) {
                    routingList.remove(i);
                    i--;
                }
            }
        }

        if (log.isDebugEnabled()) {
            if (MethodTopicListener.class.isAssignableFrom(listener.getClass())) {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener);
            } else {
                log.debug("TopicRouter listener removed(@{}): {}", topic, listener.getClass().getName());
            }
        }
    }

    /**
     * 事件拦截并路由分发
     */
    @Override
    public void doIntercept(Payload<C, R> payload, InterceptorChain chain) {
        assertTopic(payload.getTopic());

        if (log.isTraceEnabled()) {
            log.trace("{}", payload);
        }

        final List<Routing<C, R>> routings = routingList.stream()
                .filter(r -> r.matches(payload.getTopic()))
                .sorted(Comparator.comparing(r -> r.getIndex()))
                .collect(Collectors.toList());

        if (routings != null && routings.size() > 0) {
            try {
                for (Routing<C, R> r1 : routings) {
                    r1.getListener().onEvent(payload);
                }
                payload.setHandled(true);
            } catch (InvocationTargetException e) {
                throw new DamiException(e.getTargetException());
            } catch (UndeclaredThrowableException e) {
                throw new DamiException(e.getUndeclaredThrowable());
            } catch (Throwable e) {
                throw new DamiException(e);
            }
        } else {
            if (log.isWarnEnabled()) {
                log.warn("There's no matching listening on the topic(@{})", payload.getTopic());
            }
        }
    }

    /**
     * 监听路由记录
     */
    public static class Routing<C, R> {
        private TopicListener<Payload<C, R>> listener;
        private int index;
        private String patternStr;
        private Pattern pattern;

        /**
         * 获取顺序位
         */
        public int getIndex() {
            return index;
        }

        /**
         * 获取监听器
         */
        public TopicListener<Payload<C, R>> getListener() {
            return listener;
        }

        /**
         * @param expr     表达式（* 表示一段，** 表示不限段）
         * @param index    顺序位
         * @param listener 监听器
         */
        public Routing(String expr, int index, TopicListener<Payload<C, R>> listener) {
            this.listener = listener;
            this.index = index;
            this.patternStr = expr;

            if (expr.contains("*")) {
                expr = expr.replace(".", "\\."); //支持 . 或 / 做为隔断

                //替换中间的**值
                expr = expr.replace("**", ".[]");

                //替换*值
                expr = expr.replace("*", "[^/\\.]*");

                //替换**值
                expr = expr.replace(".[]", ".*");

                //加头尾
                expr = "^" + expr + "$";

                this.pattern = Pattern.compile(expr);
            } else {
                this.pattern = null;
            }
        }

        /**
         * 匹配
         *
         * @param topic 主题
         */
        public boolean matches(String topic) {
            if (patternStr.equals(topic)) {
                return true;
            }

            if (pattern != null) {
                return pattern.matcher(topic).find();
            }

            return false;
        }
    }
}