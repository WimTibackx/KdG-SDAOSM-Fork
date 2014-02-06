package be.kdg.groepa.service.impl;




import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Thierry on 4/02/14.
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public boolean checkLogin(String username, String password) {
        User user = userDao.getUser(username);
        if (user != null){
            if(user.getPassword().equals(encryptString(password))){
                return true;
            }
        }
        return false;
    }

    @Override
    public void addUser(User user) throws Exception {
        if (!isValidPassword(user.getPassword())) throw new PasswordFormatException("Invalid password format [1 uppercase, 1 lowercase, 1 digit, no whitespaces, 7-30 length]");
        user.setPassword(encryptString(user.getPassword()));   // Encrypt user password before adding
        try {
            userDao.addUser(user);
        } catch (Exception e){
            throw e;
        }
    }

    private boolean isValidPassword(String pw)
    {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{7,30}$";
        return pw.matches(regex);
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
        for (byte b : digest)
        {
            sb.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
