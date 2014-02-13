package be.kdg.groepa.service.impl;


import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;

import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import java.security.MessageDigest;

/**
 * Created by Thierry on 4/02/14.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public UserServiceImpl(){

    }

    public boolean changePassword(String username, String oldPassword, String newPassword)
    {
        User user = userDao.getUser(username);
        if (user != null) {
            if (user.getPassword().equals(encryptString(oldPassword))) {
                userDao.changePassword(username, encryptString(newPassword));
                return true;
            }
        }
        return false;
    }

    public boolean checkLogin(String username, String password) {
        User user = userDao.getUser(username);
        if (user != null) {
            if (user.getPassword().equals(encryptString(password))) {
                SessionObject session = new SessionObject(user);
                userDao.createSession(session);
                return true;
            }
        }
        return false;
    }

    public SessionObject getUserSession(String username){
        SessionObject session = userDao.getSessionByUsername(username);
        if (session.getExperiationDate().isAfter(LocalDateTime.now())){
            return session;
        }
        return null;
    }

    public void addCarToUser(String user, Car car) {
        userDao.addCarToUser(user, car);
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public void addUser(User user) throws Exception {
        if (!isValidUsername(user.getUsername())) throw new UsernameFormatException("Invalid username format (e-mail)");
        if (!isValidPassword(user.getPassword()))
            throw new PasswordFormatException("Invalid password format [1 uppercase, 1 lowercase, 1 digit, no whitespaces, 7-30 length]");
        user.setPassword(encryptString(user.getPassword()));   // Encrypt user password before adding
        try {
            userDao.addUser(user);
        } catch (Exception e) {
            throw e;
        }
    }



    public User getUser(String username) {
        return userDao.getUser(username);
    }

    private boolean isValidPassword(String pw) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{7,30}$";
        return pw.matches(regex);
    }

    private boolean isValidUsername(String username) {
        String regex = "^[_a-z0-9-A-Z-]+(\\.[_a-z0-9-A-Z-]+)*@[a-z0-9-A-Z-]+(\\.[a-z0-9-A-Z-]+)*(\\.[a-zA-Z]{2,4})$";
        return username.matches(regex);
    }

    public String encryptString(String password) {
        byte[] bytesOfMessage = null;
        MessageDigest md = null;
        try {
            bytesOfMessage = password.getBytes("UTF-8");
            md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        byte[] digest = md.digest(bytesOfMessage);
        for (byte b : digest) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
