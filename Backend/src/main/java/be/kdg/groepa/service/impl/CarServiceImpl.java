package be.kdg.groepa.service.impl;

import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.CarNotOfUserException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.CarService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by delltvgateway on 2/21/14.
 */
@Service("carService")
public class CarServiceImpl implements CarService {
    private Random random;
    private String carImagesPath;
    private String carImagesUrl;

    private ServletContext context;

    @Autowired
    private UserDao userDao;

    @Autowired
    public CarServiceImpl(ServletContext context) {
        this.context = context;
        this.random = new Random();
        this.carImagesPath = this.context.getRealPath(File.separator)+"carImages"+File.separator;
        this.carImagesUrl = "http://localhost:8080/BackEnd/carImages/";
        Logger log = Logger.getLogger(UserServiceImpl.class);
        log.debug("INITTING CARSERVICEIMPLE");
    }

    @Override
    public Car get(int id) throws CarNotFoundException {
        Car car = userDao.getCar(id);
        if (car == null) {
            throw new CarNotFoundException();
        }
        return car;
    }

    @Override
    public Car getOfUser(User user, int id) throws CarNotFoundException, CarNotOfUserException{
        Car car = this.get(id);
        if (!car.getUser().equals(user)) {
            throw new CarNotOfUserException();
        }
        return car;
    }

    @Override
    public void addCar(String user, Car car) {
        userDao.addCarToUser(user, car);
    }

    @Override
    public void setPicture(Car car, InputStream picture, String originalName) throws IOException {
        String imagename = String.format("%s%s%d",car.getBrand(),car.getType(),this.random.nextInt());
        String ext = originalName.substring(originalName.lastIndexOf("."), originalName.length());
        String filePath = this.carImagesPath + imagename + ext;
        ImageIO.write(ImageIO.read(picture), ext.replace(".", ""), new File(filePath));
        car.setPictureURL(imagename + ext);
        userDao.updateCar(car);
        return;
    }

    @Override
    public void removePicture(Car car){
        String fullPath = this.carImagesPath + car.getPictureURL();
        File f = new File(fullPath);
        f.delete();
        car.setPictureURL(null);
        userDao.updateCar(car);
        //userDao.removeCarPicture(car);
    }

    @Override
    public void remove(Car car) {
        userDao.deleteCar(car);
    }

    @Override
    public void update(Car car) {
        userDao.updateCar(car);
    }
}
