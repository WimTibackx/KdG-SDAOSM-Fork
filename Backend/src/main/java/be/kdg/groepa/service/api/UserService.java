package be.kdg.groepa.service.api;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.persistence.api.UserDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserService {

    public boolean checkLogin(String username, String password);

    public void setUserDao(UserDao userDao);
}
