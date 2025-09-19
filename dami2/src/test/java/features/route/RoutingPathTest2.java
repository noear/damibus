package features.route;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.noear.dami2.bus.EventListener;
import org.noear.dami2.bus.EventListenerHolder;
import org.noear.dami2.bus.route.*;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 路由路径的单元测试
 */
@DisplayName("RoutingPath 单元测试")
class RoutingPathTest2 {

    @Nested
    @DisplayName("通配符匹配测试")
    class WildcardMatchingTests {

        @Test
        @DisplayName("测试单段通配符 '*' 的匹配")
        void testSingleSegmentWildcard() {
            RoutingPath<Object> routing = new RoutingPath<>("a.b.*", 0, null);
            assertTrue(routing.matches("a.b.c"));
            assertFalse(routing.matches("a.b.c.d"));
            assertFalse(routing.matches("a.b"));
            assertFalse(routing.matches("a.c.c"));
        }

        @Test
        @DisplayName("测试多段通配符 '**' 的匹配")
        void testMultiSegmentWildcard() {
            RoutingPath<Object> routing = new RoutingPath<>("a.b.**", 0, null);
            assertTrue(routing.matches("a.b.c"));
            assertTrue(routing.matches("a.b.c.d"));
            assertTrue(routing.matches("a.b.c.d.e"));
            assertFalse(routing.matches("a.c.d"));
            assertFalse(routing.matches("a.b"));
        }

        @Test
        @DisplayName("测试包含路径分隔符 '/' 的匹配")
        void testPathSeparator() {
            RoutingPath<Object> routing = new RoutingPath<>("a/b/**/c", 0, null);
            assertFalse(routing.matches("a/b/c"));
            assertTrue(routing.matches("a/b/x/y/z/c"));
            assertFalse(routing.matches("a/b/x"));
        }

        @Test
        @DisplayName("测试通配符在路径中间的匹配")
        void testWildcardInMiddle() {
            RoutingPath<Object> routing = new RoutingPath<>("a.*.c.**.d", 0, null);
            assertTrue(routing.matches("a.b.c.x.y.z.d"));
            assertFalse(routing.matches("a.B.c.d"));
            assertFalse(routing.matches("a.b.d.e.f"));
        }

        @Test
        @DisplayName("测试无通配符表达式的匹配")
        void testNoWildcard() {
            RoutingPath<Object> routing = new RoutingPath<>("topic.exact", 0, null);
            assertTrue(routing.matches("topic.exact"));
            assertFalse(routing.matches("topic.not.exact"));
        }
    }

    @Test
    @DisplayName("isPatterned 方法应返回 true")
    void testIsPatterned() {
        RoutingPath<Object> routing = new RoutingPath<>("a.*.c", 0, null);
        assertTrue(routing.isPatterned());
    }
}
