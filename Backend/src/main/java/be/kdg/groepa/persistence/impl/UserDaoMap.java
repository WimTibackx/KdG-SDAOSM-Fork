package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Thierry on 4/02/14.
 */
@Repository("userDao")
public class UserDaoMap implements UserDao {
    private final ConcurrentHashMap<String, User> users;

    public UserDaoMap(){
        users = new ConcurrentHashMap<String, User>();
        users.put("Thierry", new User("Thierry", "succes"));
    }

    public User getUser(String username){
        return users.get(username);
    }
}
