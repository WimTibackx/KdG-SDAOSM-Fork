package be.kdg.groepa.model;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class LocalUserData {
    private int userId;
    private String token;

    public LocalUserData(int userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}