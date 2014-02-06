package be.kdg.groepa.service.api;

import be.kdg.groepa.model.User;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserService {

    public boolean checkLogin(String username, String password);
    public void addUser(User user) throws Exception;
    public User getUser(String username);
}
