package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.User;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserDao {
    public User getUser(String username);
}
