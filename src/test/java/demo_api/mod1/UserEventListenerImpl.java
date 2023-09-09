package demo_api.mod1;

public class UserEventListenerImpl implements UserEventListener {
    @Override
    public void created(long userId, String name) {
        System.err.println("created: userId=" + userId + ", name=" + name);
    }

    @Override
    public void updated(long userId, String name) {
        System.err.println("updated: userId=" + userId + ", name=" + name);
    }
}
