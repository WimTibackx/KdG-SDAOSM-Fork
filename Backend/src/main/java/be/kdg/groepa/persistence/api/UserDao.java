package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserDao {
    public User getUser(String username);

    public void createSession(SessionObject session);

    public SessionObject getSession(String token);

    public void deleteSession(SessionObject session);

    public void extendSession(String token);

    public void addUser(User u) throws Exception;

    public SessionObject getSessionByUserame(String username);
}
