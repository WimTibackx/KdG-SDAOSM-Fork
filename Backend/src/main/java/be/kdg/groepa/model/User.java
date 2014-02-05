package be.kdg.groepa.model;

/**
 * Created by Thierry on 4/02/14.
 */
public class User {
    private final String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
