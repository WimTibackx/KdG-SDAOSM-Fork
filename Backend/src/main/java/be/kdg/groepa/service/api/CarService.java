package be.kdg.groepa.service.api;

import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.CarNotOfUserException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by delltvgateway on 2/21/14.
 */
public interface CarService {
    public Car get(int id) throws CarNotFoundException;
    public Car getOfUser(User user, int id) throws CarNotFoundException, CarNotOfUserException;
    public void addCar(String user, Car car);
    public void setPicture(Car car, InputStream picture, String originalName) throws IOException;
    public void removePicture(Car car);
    public void remove(Car car);
}
