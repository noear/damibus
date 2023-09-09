package demo_api.mod1;

public class UserEventListenerImpl {
    public long created(long userId, String name) {
        System.err.println("created: userId=" + userId + ", name=" + name);
        return userId;
    }
    public void updated(long userId, String name) {
        System.err.println("updated: userId=" + userId + ", name=" + name);
    }
}
