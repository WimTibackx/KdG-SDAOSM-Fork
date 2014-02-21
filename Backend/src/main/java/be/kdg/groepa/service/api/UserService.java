package be.kdg.groepa.service.api;


import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.model.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserService {

    public boolean changePassword(String username, String oldPassword, String newPassword);
    public boolean checkLogin(String username, String password);

    public void setUserDao(UserDao userDao);
    public void addUser(User user) throws Exception;
    public User getUser(String username);
    public User getUserById(Integer id);

    public String encryptString(String password);
    public SessionObject getUserSession(String username);
    public SessionObject getUserSessionByToken(String token);
    public boolean isUserSession(String username);
    public boolean isUserSessionByToken(String token);

    public void editUserPicture(String username, File newPicture);
    public void removeUserPicture(String username);
}
