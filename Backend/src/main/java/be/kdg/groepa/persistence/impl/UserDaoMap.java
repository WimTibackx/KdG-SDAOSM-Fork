package be.kdg.groepa.persistence.impl;

import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UserExistException;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import org.springframework.stereotype.Repository;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Repository("userDao")
public class UserDaoMap implements UserDao {
    private final ConcurrentHashMap<String, User> users;

    public UserDaoMap(){
        users = new ConcurrentHashMap<String, User>();
    }

    public User getUser(String username){
        return users.get(username);
    }

    public void addUser(User u) throws Exception
    {
        if (users.contains(u.getUsername())) throw new UserExistException("User already exists");
        users.put(u.getUsername(), u);
    }


}
