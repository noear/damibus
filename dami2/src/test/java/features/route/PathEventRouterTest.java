package features.route;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.noear.dami2.bus.route.EventRouter;
import org.noear.dami2.bus.route.PathEventRouter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * EventRouterPatterned 单元测试
 */
class PathEventRouterTest {
    private EventRouter router;

    @BeforeEach
    void setUp() {
        router = new PathEventRouter();
    }

    @Test
    void testExactMatch() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};

        // 执行
        router.add("user.created", 0, listener1);
        router.add("user.updated", 1, listener2);

        // 验证
        List<EventListenerHolder> result1 = router.matching("user.created");
        assertEquals(1, result1.size());
        assertEquals(listener1, result1.get(0).getListener());

        List<EventListenerHolder> result2 = router.matching("user.updated");
        assertEquals(1, result2.size());
        assertEquals(listener2, result2.get(0).getListener());

        List<EventListenerHolder> result3 = router.matching("user.deleted");
        assertTrue(result3.isEmpty());
    }

    @Test
    void testPatternMatchSingleStar() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};

        // 执行
        router.add("user.*", 0, listener1);
        router.add("order.*", 1, listener2);

        // 验证
        List<EventListenerHolder> result1 = router.matching("user.created");
        assertEquals(1, result1.size());
        assertEquals(listener1, result1.get(0).getListener());

        List<EventListenerHolder> result2 = router.matching("user.updated");
        assertEquals(1, result2.size());
        assertEquals(listener1, result2.get(0).getListener());

        List<EventListenerHolder> result3 = router.matching("order.created");
        assertEquals(1, result3.size());
        assertEquals(listener2, result3.get(0).getListener());

        List<EventListenerHolder> result4 = router.matching("product.created");
        assertTrue(result4.isEmpty());
    }

    @Test
    void testPatternMatchDoubleStar() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};

        // 执行
        router.add("user.**", 0, listener1);
        router.add("order.**", 1, listener2);

        // 验证
        List<EventListenerHolder> result1 = router.matching("user.created");
        assertEquals(1, result1.size());
        assertEquals(listener1, result1.get(0).getListener());

        List<EventListenerHolder> result2 = router.matching("user.profile.updated");
        assertEquals(1, result2.size());
        assertEquals(listener1, result2.get(0).getListener());

        List<EventListenerHolder> result3 = router.matching("user.account.settings.changed");
        assertEquals(1, result3.size());
        assertEquals(listener1, result3.get(0).getListener());

        List<EventListenerHolder> result4 = router.matching("order.items.added");
        assertEquals(1, result4.size());
        assertEquals(listener2, result4.get(0).getListener());
    }

    @Test
    void testMixedExactAndPatternMatching() {
        // 准备
        EventListener<String> exactListener = event -> {};
        EventListener<String> patternListener = event -> {};

        // 执行
        router.add("user.created", 0, exactListener);
        router.add("user.*", 1, patternListener);

        // 验证
        List<EventListenerHolder> result = router.matching("user.created");
        assertEquals(2, result.size());

        // 验证顺序（精确匹配应该排在前面）
        assertEquals(exactListener, result.get(0).getListener());
        assertEquals(patternListener, result.get(1).getListener());
    }

    @Test
    void testRemoveExactListener() {
        // 准备
        EventListener<String> listener = event -> {};

        // 执行
        router.add("user.created", 0, listener);
        router.remove("user.created", listener);

        // 验证
        List<EventListenerHolder> result = router.matching("user.created");
        assertTrue(result.isEmpty());
    }

    @Test
    void testRemovePatternListener() {
        // 准备
        EventListener<String> listener = event -> {};

        // 执行
        router.add("user.*", 0, listener);
        router.remove("user.*", listener);

        // 验证
        List<EventListenerHolder> result = router.matching("user.created");
        assertTrue(result.isEmpty());
    }

    @Test
    void testRemoveAllListenersForTopic() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};

        // 执行
        router.add("user.created", 0, listener1);
        router.add("user.created", 1, listener2);
        router.remove("user.created");

        // 验证
        List<EventListenerHolder> result = router.matching("user.created");
        assertTrue(result.isEmpty());
    }

    @Test
    void testOrderingOfMultipleListeners() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};
        EventListener<String> listener3 = event -> {};

        // 执行
        router.add("user.*", 2, listener1);
        router.add("user.*", 1, listener2);
        router.add("user.*", 0, listener3);

        // 验证
        List<EventListenerHolder> result = router.matching("user.created");
        assertEquals(3, result.size());

        // 验证顺序（索引小的排在前面）
        assertEquals(listener3, result.get(0).getListener());
        assertEquals(listener2, result.get(1).getListener());
        assertEquals(listener1, result.get(2).getListener());
    }

    @Test
    void testComplexPatternMatching() {
        // 准备
        EventListener<String> listener1 = event -> {};
        EventListener<String> listener2 = event -> {};
        EventListener<String> listener3 = event -> {};

        // 执行
        router.add("*.created", 0, listener1);
        router.add("user.*.updated", 1, listener2);
        router.add("**", 2, listener3); // 匹配所有

        // 验证
        List<EventListenerHolder> result1 = router.matching("user.created");
        assertEquals(2, result1.size()); // *.created 和 **

        List<EventListenerHolder> result2 = router.matching("user.profile.updated");
        assertEquals(2, result2.size()); // user.*.updated 和 **

        List<EventListenerHolder> result3 = router.matching("order.items.added");
        assertEquals(1, result3.size()); // 只有 **
    }

    @Test
    void testDotSeparatorInPatterns() {
        // 准备
        EventListener<String> listener = event -> {};

        // 执行
        router.add("user.*.profile", 0, listener);

        // 验证
        List<EventListenerHolder> result1 = router.matching("user.123.profile");
        assertEquals(1, result1.size());

        List<EventListenerHolder> result2 = router.matching("user.abc.profile");
        assertEquals(1, result2.size());

        List<EventListenerHolder> result3 = router.matching("user.123.settings");
        assertTrue(result3.isEmpty());
    }
}
