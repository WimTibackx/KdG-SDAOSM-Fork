package be.kdg.groepa.service.api;


import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.model.User;

import java.io.File;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserService {

    public boolean changePassword(String username, String oldPassword, String newPassword);
    public boolean checkLogin(String username, String password);

    public void setUserDao(UserDao userDao);
    public void addUser(User user) throws Exception;
    public User getUser(String username);
    public String encryptString(String password);
    public SessionObject getUserSession(String username);

    public void addCarToUser(String user, Car car);
    public void editUserPicture(String username, File newPicture);
    public void removeUserPicture(String username);
}
