package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Thierry on 4/02/14.
 */
@Repository("userDao")
public class UserDaoMap implements UserDao {
    private final ConcurrentHashMap<String, User> users;
    private final ConcurrentHashMap<String, SessionObject> sessions;

    public UserDaoMap(){
        users = new ConcurrentHashMap<String, User>();
        sessions = new ConcurrentHashMap<String, SessionObject>();
        users.put("Thierry", new User("Thierry", "succes"));
    }

    public User getUser(String username){
        return users.get(username);
    }

    @Override
    public void createSession(SessionObject session) {
        sessions.put(session.getSessionToken(), session);
    }

    @Override
    public SessionObject getSession(String token) {
        return sessions.get(token);
    }

    @Override
    public void deleteSession(SessionObject session) {
        sessions.remove(session.getSessionToken());
    }

    @Override
    public void extendSession(String token) {
        SessionObject session = sessions.get(token);
        session.setExperiationDate(LocalDateTime.now().plusDays(1L));
    }
}
