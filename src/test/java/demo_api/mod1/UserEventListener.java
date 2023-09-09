package demo_api.mod1;

public interface UserEventListener {
    void created(long userId, String name);
    void updated(long userId, String name);
}
