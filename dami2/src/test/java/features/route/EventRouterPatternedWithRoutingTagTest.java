package features.route;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.noear.dami2.bus.route.EventRouterPatterned;
import org.noear.dami2.bus.route.RoutingFactory;
import org.noear.dami2.bus.route.RoutingTag;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EventRouterPatterned + RoutingTag 组合测试
 *
 * @author noear
 * @since 1.0
 */
public class EventRouterPatternedWithRoutingTagTest {
    private EventRouterPatterned router;
    private RoutingFactory routingFactory;

    @BeforeEach
    public void setUp() {
        // 使用 RoutingTag 工厂创建路由
        routingFactory = RoutingTag::new;
        router = new EventRouterPatterned(routingFactory);
    }

    @Test
    public void testExactMatch() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加精确匹配监听
        router.add("user:created", 0, listener);

        // 匹配精确主题
        List<EventListenerHolder> matches = router.matching("user:created");
        assertEquals(1, matches.size());
        assertEquals(listener, matches.get(0).getListener());

        // 不匹配其他主题
        assertEquals(0, router.matching("user:updated").size());
        assertEquals(1, router.matching("user").size());
    }

    @Test
    public void testTagMatch() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加标签监听
        router.add("user:created,updated", 0, listener);

        // 匹配包含的标签
        assertTrue(router.matching("user:created").size() > 0);
        assertTrue(router.matching("user:updated").size() > 0);
        assertTrue(router.matching("user:created,updated").size() > 0);
        assertTrue(router.matching("user:updated,created").size() > 0);

        // 不匹配其他标签
        assertEquals(0, router.matching("user:deleted").size());
        assertEquals(1, router.matching("user").size());
    }

    @Test
    public void testMultipleTags() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加多标签监听
        router.add("user:created,updated,deleted", 0, listener);

        // 匹配各种标签组合
        assertTrue(router.matching("user:created").size() > 0);
        assertTrue(router.matching("user:updated").size() > 0);
        assertTrue(router.matching("user:deleted").size() > 0);
        assertTrue(router.matching("user:created,updated").size() > 0);
        assertTrue(router.matching("user:created,deleted").size() > 0);
        assertTrue(router.matching("user:updated,deleted").size() > 0);
        assertTrue(router.matching("user:created,updated,deleted").size() > 0);

        // 不匹配其他标签
        assertEquals(0, router.matching("user:archived").size());
    }

    @Test
    public void testTopicOnly() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加只有主题的监听
        router.add("user", 0, listener);

        // 匹配主题
        assertTrue(router.matching("user").size() > 0);
        assertTrue(router.matching("user:").size() > 0); // 空标签也应该匹配

        // 不匹配带标签的
        assertEquals(1, router.matching("user:created").size());
        assertEquals(1, router.matching("user:updated").size());
    }

    @Test
    public void testEmptyTags() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加空标签监听
        router.add("user:", 0, listener);

        // 应该匹配任何 user 主题的标签
        assertTrue(router.matching("user:").size() > 0);
        assertTrue(router.matching("user:created").size() > 0);
        assertTrue(router.matching("user:updated").size() > 0);
        assertTrue(router.matching("user:created,updated").size() > 0);

        // 不匹配其他主题
        assertEquals(0, router.matching("order:").size());
        assertEquals(0, router.matching("order:created").size());
    }

    @Test
    public void testRemoveListener() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 添加监听
        router.add("user:created", 0, listener);
        assertEquals(1, router.matching("user:created").size());

        // 移除监听
        router.remove("user:created", listener);
        assertEquals(0, router.matching("user:created").size());
    }

    @Test
    public void testRemoveAllListeners() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener1 = event -> count.incrementAndGet();
        EventListener<String> listener2 = event -> count.addAndGet(2);

        // 添加多个监听
        router.add("user:created", 0, listener1);
        router.add("user:created", 1, listener2);
        assertEquals(2, router.matching("user:created").size());

        // 移除所有监听
        router.remove("user:created");
        assertEquals(0, router.matching("user:created").size());
    }

    @Test
    public void testPriorityOrder() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener1 = event -> count.set(1);
        EventListener<String> listener2 = event -> count.set(2);
        EventListener<String> listener3 = event -> count.set(3);

        // 添加不同优先级的监听
        router.add("user:created", 10, listener1);
        router.add("user:created", 5, listener2); // 中间优先级
        router.add("user:created", 1, listener3); // 最高优先级

        List<EventListenerHolder> matches = router.matching("user:created");
        assertEquals(3, matches.size());

        // 检查顺序：优先级数字小的先执行
        assertEquals(1, matches.get(0).getIndex()); // 最高优先级
        assertEquals(5, matches.get(1).getIndex());
        assertEquals(10, matches.get(2).getIndex()); // 最低优先级
    }

    @Test
    public void testMixedExactAndTagRoutes() {
        AtomicInteger exactCount = new AtomicInteger();
        AtomicInteger tagCount = new AtomicInteger();

        EventListener<String> exactListener = event -> exactCount.incrementAndGet();
        EventListener<String> tagListener = event -> tagCount.incrementAndGet();

        // 添加精确匹配和标签匹配
        router.add("user:created", 0, exactListener);
        router.add("user:created,updated", 0, tagListener);

        // 精确匹配应该找到两个监听器
        List<EventListenerHolder> matches = router.matching("user:created");
        assertEquals(2, matches.size());

        // 标签匹配应该找到一个监听器
        matches = router.matching("user:updated");
        assertEquals(1, matches.size());
        assertEquals(tagListener, matches.get(0).getListener());
    }

    @Test
    public void testComplexTagScenarios() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 测试各种标签格式
        router.add("user:created,updated", 0, listener);

        assertTrue(router.matching("user:created").size() > 0);
        assertTrue(router.matching("user:updated").size() > 0);
        assertTrue(router.matching("user:created,updated").size() > 0);
        assertTrue(router.matching("user:updated,created").size() > 0); // 顺序无关
        assertTrue(router.matching("user:created,updated,deleted").size() > 0); // 包含即可

        assertEquals(0, router.matching("user:deleted").size());
        assertEquals(1, router.matching("user:created,deleted").size()); // 必须全部包含
    }

    @Test
    public void testMalformedTopics() {
        AtomicInteger count = new AtomicInteger();
        EventListener<String> listener = event -> count.incrementAndGet();

        // 测试格式不正确的主题
        router.add("user::created", 0, listener); // 双冒号
        router.add("user:created,", 0, listener); // 结尾逗号
        router.add("user:,created", 0, listener); // 空标签

        // 这些应该都能正常处理
        assertTrue(router.matching("user::created").size() > 0);
        assertTrue(router.matching("user:created,").size() > 0);
        assertTrue(router.matching("user:,created").size() > 0);
    }
}
