package be.kdg.groepa.model;

import org.threeten.bp.LocalDateTime;

/**
 * Created by Thierry on 6/02/14.
 */
public class SessionObject {
    private final String sessionToken;
    private String username;
    private LocalDateTime experiationDate;


    public SessionObject(String username) {
        this.username = username;
        this.experiationDate = LocalDateTime.now().plusDays(1L);
        this.sessionToken = username + "123456";

        //this.sessionToken = UUID.randomUUID().toString();
    }


    public String getSessionToken() {
        return sessionToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getExperiationDate() {
        return experiationDate;
    }

    public void setExperiationDate(LocalDateTime experiationDate) {
        this.experiationDate = experiationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionObject that = (SessionObject) o;

        if (sessionToken != null ? !sessionToken.equals(that.sessionToken) : that.sessionToken != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return sessionToken != null ? sessionToken.hashCode() : 0;
    }
}
