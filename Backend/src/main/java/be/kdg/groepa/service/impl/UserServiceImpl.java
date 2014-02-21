package be.kdg.groepa.service.impl;


import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;

import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.threeten.bp.LocalDateTime;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Random;

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
                SessionObject session = userDao.getSessionByUsername(username);
                if (session != null){
                    userDao.extendSession(session);
                }else{
                    session = new SessionObject(user);
                    userDao.createSession(session);
                }

                return true;
            }
        }
        return false;
    }

    public SessionObject getUserSession(String username){
        SessionObject session = userDao.getSessionByUsername(username);
        return (this.isSessionValid(session) ? session : null);
    }

    private boolean isSessionValid(SessionObject session) {
        if (session != null && session.getExperiationDate().isAfter(LocalDateTime.now())) {
            userDao.extendSession(session);
            return true;
        }
        if (session != null) {
            userDao.deleteSession(session);
        }
        return false;
    }

    @Override
    public SessionObject getUserSessionByToken(String token) {
        SessionObject session = userDao.getSession(token);
        return (this.isSessionValid(session) ? session : null);
    }

    @Override
    public boolean isUserSession(String username) {
        return getUserSession(username) != null;
    }

    @Override
    public boolean isUserSessionByToken(String token) {
        return getUserSessionByToken(token) != null;
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

    public User getUserById(Integer id){
        return userDao.getUser(id);
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

    public void editUserPicture(String username, File newPicture){
        userDao.editUserPicture(username, newPicture);
    }

    @Override
    public void removeUserPicture(String username) {
        userDao.removeUserPicture(username);
    }




}
