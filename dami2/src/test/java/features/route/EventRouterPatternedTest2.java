package features.route;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.noear.dami2.bus.route.EventRouterPatterned;
import org.noear.dami2.bus.route.Routing;
import org.noear.dami2.bus.route.RoutingFactory;
import org.noear.dami2.bus.route.RoutingPath;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 事件路由器 EventRouterPatterned 的单元测试
 */
@DisplayName("EventRouterPatterned 单元测试")
class EventRouterPatternedTest2 {

    private EventRouterPatterned router;

    // 辅助的模拟类
    static class MockEventListener implements EventListener<Object> {
        private String name;

        public MockEventListener(String name) {
            this.name = name;
        }

        @Override
        public void onEvent(org.noear.dami2.bus.Event<Object> event) throws Throwable {
        }
    }

    // 辅助的路由工厂，用于创建 RoutingPath 或 Routing 实例
    private static class MockRoutingFactory implements RoutingFactory {
        @Override
        public <P> Routing create(String topicExpr, int index, EventListener<P> listener) {
            if (topicExpr.contains("*")) {
                // 如果主题表达式包含通配符，则创建 RoutingPath
                return new RoutingPath<>(topicExpr, index, listener);
            } else {
                // 否则创建 Routing
                return new Routing<>(topicExpr, index, listener);
            }
        }
    }

    @BeforeEach
    void setUp() {
        router = new EventRouterPatterned(new MockRoutingFactory());
    }

    @Nested
    @DisplayName("添加和匹配测试")
    class AddAndMatchingTests {

        @Test
        @DisplayName("测试精确匹配的添加和匹配")
        void testExactMatching() {
            MockEventListener listener1 = new MockEventListener("listener1");
            router.add("user.login", 0, listener1);

            List<EventListenerHolder> holders = router.matching("user.login");
            assertEquals(1, holders.size());
            assertEquals(listener1, holders.get(0).getListener());
        }

        @Test
        @DisplayName("测试模式匹配的添加和匹配")
        void testPatternMatching() {
            MockEventListener listener2 = new MockEventListener("listener2");
            router.add("user.*", 0, listener2);

            List<EventListenerHolder> holders = router.matching("user.logout");
            assertEquals(1, holders.size());
            assertEquals(listener2, holders.get(0).getListener());
        }

        @Test
        @DisplayName("测试精确和模式匹配的组合")
        void testCombinedMatching() {
            MockEventListener exactListener = new MockEventListener("exactListener");
            MockEventListener patternListener = new MockEventListener("patternListener");

            router.add("user.profile", 0, exactListener);
            router.add("user.*", 0, patternListener);

            List<EventListenerHolder> holders = router.matching("user.profile");
            assertEquals(2, holders.size());
            List<String> names = holders.stream()
                    .map(h -> ((MockEventListener) h.getListener()).name)
                    .collect(Collectors.toList());
            assertTrue(names.contains("exactListener"));
            assertTrue(names.contains("patternListener"));
        }

        @Test
        @DisplayName("测试多层通配符的匹配")
        void testMultiLayerPatternMatching() {
            MockEventListener listener1 = new MockEventListener("listener1");
            MockEventListener listener2 = new MockEventListener("listener2");
            MockEventListener listener3 = new MockEventListener("listener3");

            router.add("a.b.**", 0, listener1);
            router.add("a.b.c.*", 0, listener2);
            router.add("a.b.c.d", 0, listener3);

            List<EventListenerHolder> holders = router.matching("a.b.c.d");
            assertEquals(3, holders.size());
            List<String> names = holders.stream()
                    .map(h -> ((MockEventListener) h.getListener()).name)
                    .collect(Collectors.toList());
            assertTrue(names.contains("listener1"));
            assertTrue(names.contains("listener2"));
            assertTrue(names.contains("listener3"));
        }

        @Test
        @DisplayName("测试无匹配结果")
        void testNoMatch() {
            router.add("a.b.c", 0, new MockEventListener("listener1"));
            List<EventListenerHolder> holders = router.matching("a.b.d");
            assertTrue(holders.isEmpty());
        }
    }

    @Nested
    @DisplayName("排序测试")
    class SortingTests {

        @Test
        @DisplayName("确保匹配结果按 index 升序排列")
        void testMatchingResultIsSortedByIndex() {
            MockEventListener listener10 = new MockEventListener("listener10");
            MockEventListener listener20 = new MockEventListener("listener20");
            MockEventListener listener5 = new MockEventListener("listener5");
            MockEventListener listener15 = new MockEventListener("listener15");

            router.add("app.event.*", 20, listener20);
            router.add("app.event.**", 10, listener10);
            router.add("app.event.test", 15, listener15);
            router.add("app.event.test", 5, listener5);

            List<EventListenerHolder> holders = router.matching("app.event.test");
            assertEquals(4, holders.size());

            // 验证排序顺序
            assertEquals(5, holders.get(0).getIndex());
            assertEquals(10, holders.get(1).getIndex());
            assertEquals(15, holders.get(2).getIndex());
            assertEquals(20, holders.get(3).getIndex());
        }
    }

    @Nested
    @DisplayName("移除测试")
    class RemoveTests {

        private MockEventListener listener1;
        private MockEventListener listener2;

        @BeforeEach
        void setupRemoveTests() {
            listener1 = new MockEventListener("listener1");
            listener2 = new MockEventListener("listener2");
            router.add("topic.test", 0, listener1);
            router.add("topic.*", 0, listener2);
        }

        @Test
        @DisplayName("通过主题和监听器对象移除")
        void testRemoveByTopicAndListener() {
            router.remove("topic.test", listener1);

            List<EventListenerHolder> holders = router.matching("topic.test");
            assertEquals(1, holders.size());
            assertEquals(listener2, holders.get(0).getListener());
        }

        @Test
        @DisplayName("通过主题表达式移除所有监听器")
        void testRemoveByTopicExpression() {
            router.remove("topic.test");

            List<EventListenerHolder> holders = router.matching("topic.test");
            assertEquals(1, holders.size()); // 模式匹配的 listener2 仍然存在
            assertEquals(listener2, holders.get(0).getListener());
        }

        @Test
        @DisplayName("移除模式匹配的监听器")
        void testRemovePatternedListener() {
            router.remove("topic.*", listener2);

            List<EventListenerHolder> holders = router.matching("topic.test");
            assertEquals(1, holders.size()); // 精确匹配的 listener1 仍然存在
            assertEquals(listener1, holders.get(0).getListener());
        }
    }
}