package features.route;


import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.route.PathRouting;

import static org.junit.jupiter.api.Assertions.*;

/**
 * RoutingPath 单元测试
 */
class RoutingPathTest {

    @Test
    void testExactMatch() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.created", 0, listener);

        // 验证
        assertTrue(routing.matches("user.created"));
        assertFalse(routing.matches("user.updated"));
        assertFalse(routing.matches("user"));
    }

    @Test
    void testSingleStarMatch() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.*", 0, listener);

        // 验证
        assertTrue(routing.matches("user.created"));
        assertTrue(routing.matches("user.updated"));
        assertTrue(routing.matches("user.deleted"));
        assertFalse(routing.matches("user"));
        assertFalse(routing.matches("user.profile.updated"));
        assertFalse(routing.matches("order.created"));
    }

    @Test
    void testDoubleStarMatch() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.**", 0, listener);

        // 验证
        assertTrue(routing.matches("user.created"));
        assertTrue(routing.matches("user.profile.updated"));
        assertTrue(routing.matches("user.account.settings.changed"));
        assertFalse(routing.matches("user"));
        assertFalse(routing.matches("order.created"));
    }

    @Test
    void testMiddleDoubleStarMatch() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.**.settings", 0, listener);

        // 验证
        assertTrue(routing.matches("user.account.settings"));
        assertTrue(routing.matches("user.profile.account.settings"));
        assertFalse(routing.matches("user.settings")); // ** 需要至少一个段
        assertFalse(routing.matches("user.account.preferences"));
    }

    @Test
    void testComplexPatternMatch() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("*.service.*.log", 0, listener);

        // 验证
        assertTrue(routing.matches("user.service.request.log"));
        assertTrue(routing.matches("order.service.response.log"));
        assertFalse(routing.matches("service.request.log"));
        assertFalse(routing.matches("user.service.log"));
        assertFalse(routing.matches("user.service.request.error"));
    }

    @Test
    void testMultipleStarsInPattern() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.*.*.log", 0, listener);

        // 验证
        assertTrue(routing.matches("user.service.request.log"));
        assertTrue(routing.matches("user.api.response.log"));
        assertFalse(routing.matches("user.service.log"));
        assertFalse(routing.matches("user.service.request.error"));
    }

    @Test
    void testPatternWithMixedStars() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user.*.log.**", 0, listener);

        // 验证
        assertTrue(routing.matches("user.service.log.error"));
        assertTrue(routing.matches("user.api.log.warn.debug"));
        assertFalse(routing.matches("user.log.error"));
        assertFalse(routing.matches("user.service.error.log"));
    }

    @Test
    void testIsPatterned() {
        // 准备
        EventListener<String> listener = event -> {};

        // 验证精确匹配
        PathRouting<String> exactRouting = new PathRouting<>("user.created", 0, listener);
        assertFalse(exactRouting.isPatterned());

        // 验证模式匹配
        PathRouting<String> patternRouting = new PathRouting<>("user.*", 0, listener);
        assertTrue(patternRouting.isPatterned());

        PathRouting<String> doubleStarRouting = new PathRouting<>("user.**", 0, listener);
        assertTrue(doubleStarRouting.isPatterned());
    }

    @Test
    void testGetExpr() {
        // 准备
        EventListener<String> listener = event -> {};
        String expectedExpr = "user.*.log";

        // 执行
        PathRouting<String> routing = new PathRouting<>(expectedExpr, 0, listener);

        // 验证
        assertEquals(expectedExpr, routing.getExpr());
    }

    @Test
    void testSlashSeparatorInPatterns() {
        // 准备
        EventListener<String> listener = event -> {};
        PathRouting<String> routing = new PathRouting<>("user/*/profile", 0, listener);

        // 验证
        assertTrue(routing.matches("user/123/profile"));
        assertTrue(routing.matches("user/abc/profile"));
        assertFalse(routing.matches("user/123/settings"));
        assertFalse(routing.matches("user/profile"));
    }
}