package features.demo82_solon;

/**
 * 基础，公用的用户模型
 * */
public class User {
    private long userId;
    private String name;

    public User(long userId) {
        this.userId = userId;
        this.name = "user-" + userId;
    }

    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                '}';
    }
}
