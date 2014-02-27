package be.kdg.groepa.persistence.api;

import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;

import java.io.File;
import java.util.List;

/**
 * Created by Thierry on 4/02/14.
 */
public interface UserDao {
    public User getUser(String username);

    public User getUser(Integer id);

    public void changePassword(String username, String newPassword);

    public void createSession(SessionObject session);

    public SessionObject getSession(String token);

    public void deleteSession(SessionObject session);

    public void extendSession(SessionObject session);

    public void addUser(User u);

    public SessionObject getSessionByUsername(String username);

    public void addCarToUser(String username, Car c);
    public void updateCar(Car c);

    public void editUserPicture(String username, File newPicture);

    public void removeUserPicture(String username);

    public void removeCarPicture(Car car);

    public Car getCar(int id);

    public void deleteCar(Car car);

    public void updateUser(User user);

    List<Route> getRoutesFromUser(String testUsername);
}
