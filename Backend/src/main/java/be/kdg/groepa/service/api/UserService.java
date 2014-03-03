package be.kdg.groepa.service.api;


import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameExistsException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.UserDao;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserService {

    public int changePassword(String username, String oldPassword, String newPassword);
    public boolean checkLogin(String username, String password);

    public void setUserDao(UserDao userDao);
    public void addUser(User user) throws UsernameFormatException, PasswordFormatException, UsernameExistsException;
    public User getUser(String username);
    public User getUserById(Integer id);

    public String encryptString(String password);
    public SessionObject getUserSession(String username);
    public SessionObject getUserSessionByToken(String token);
    public boolean isUserSession(String username);
    public boolean isUserSessionByToken(String token);

    public void editUserPicture(String username, File newPicture);
    public void removeUserPicture(User user);
    public void setUserPicture(User user, InputStream picture, String originalName) throws IOException;

    List<Route> getRoutesFromUser(String testUsername);
}
