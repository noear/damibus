package demo_api.mod2;

public interface UserEventSender {
    void created(long userId, String name);
    void updated(long userId, String name);
}
