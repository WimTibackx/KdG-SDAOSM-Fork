package be.kdg.groepa.service.impl;




import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

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
            if(user.getPassword() == password){
                return true;
            }
        }
        return false;
    }
}
