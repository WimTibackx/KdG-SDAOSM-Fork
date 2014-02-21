package be.kdg.groepa.controllers;

import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.CarNotOfUserException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.CarService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by delltvgateway on 2/20/14.
 */
@Controller
public class CarController extends BaseController {

    @Autowired
    private CarService carService;

    @RequestMapping(value="/authorized/user/car/add",method= RequestMethod.POST)
    public @ResponseBody String addCar(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);
        String brand = dataOb.getString("brand");
        String model = dataOb.getString("model");
        String fuelType = dataOb.getString("fueltype");
        double consumption = dataOb.getDouble("consumption");

        User currentUser = super.getCurrentUser(request);
        Car car = new Car(brand, model, consumption, Car.FuelType.valueOf(fuelType.toUpperCase()));
        carService.addCar(currentUser.getUsername(), car);

        return super.respondSimpleAuthorized("inserted", car.getCarId().toString(), request, response);
    }

    @RequestMapping(value="/authorized/user/car/{id}/uploadphoto",method=RequestMethod.POST)
    public @ResponseBody String uploadPhoto(@PathVariable("id") Integer id, @RequestParam("file") MultipartFile photo, HttpServletRequest request, HttpServletResponse response) {
        Car car;
        try {
            car = carService.getOfUser(this.getCurrentUser(request), id);
        } catch (CarNotFoundException e) {
            return super.respondSimpleAuthorized("error", "CarNotFound", request, response);
        } catch (CarNotOfUserException e) {
            return super.respondSimpleAuthorized("error", "CarNotYours", request, response);
        }

        InputStream is = null;
        try {
            is = photo.getInputStream();
            carService.setPicture(car, is, photo.getOriginalFilename());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
            return super.respondSimpleAuthorized("error", "ImageError", request, response);
        }

        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

    @RequestMapping(value="/authorized/user/car/{id}/deletephoto", method=RequestMethod.POST)
    public @ResponseBody String deletePhoto(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        Car car;
        try {
            car = carService.getOfUser(this.getCurrentUser(request), id);
        } catch (CarNotFoundException e) {
            return super.respondSimpleAuthorized("error", "CarNotFound", request, response);
        } catch (CarNotOfUserException e) {
            return super.respondSimpleAuthorized("error", "CarNotYours", request, response);
        }

        carService.removePicture(car);
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

    @RequestMapping(value="/authorized/user/car/{id}/delete", method=RequestMethod.POST)
    public @ResponseBody String delete(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        Car car;
        try {
            car = carService.getOfUser(this.getCurrentUser(request), id);
        } catch (CarNotFoundException e) {
            return super.respondSimpleAuthorized("error", "CarNotFound", request, response);
        } catch (CarNotOfUserException e) {
            return super.respondSimpleAuthorized("error", "CarNotYours", request, response);
        }

        carService.remove(car);
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }
}