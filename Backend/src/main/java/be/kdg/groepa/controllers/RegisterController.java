package be.kdg.groepa.controllers;

import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameExistsException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.json.Cookie;
import org.json.JSONObject;
import org.json.simple.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalDate;

import javax.ejb.Local;
import javax.servlet.http.*;

import org.json.*;


/**
 * Created by Tim on 18/02/14.
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String register(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject json = new JSONObject();
        String name, gender, password, username, carbrand = null, cartype = null, carfueltype = null;
        double carconsumption = 0;
        boolean smoker;
        LocalDate dOB;

        JSONObject myJson = new JSONObject(data);
        try {
            if (!myJson.has("name")) throw new MissingDataException("name");
            name = myJson.getString("name");
            if (!myJson.has("gender")) throw new MissingDataException("gender");
            gender = myJson.getString("gender");
            if (!myJson.has("smoker")) throw new MissingDataException("smoker");
            smoker = myJson.getBoolean("smoker");
            if (!myJson.has("password")) throw new MissingDataException("password");
            password = myJson.getString("password");
            if (!myJson.has("dateofbirth")) throw new MissingDataException("dateofbirth");
            dOB = LocalDate.parse(myJson.getString("dateofbirth"));
            if (!myJson.has("username")) throw new MissingDataException("username");
            username = myJson.getString("username");
            //TODO: We don't need those anymore...
            if(myJson.has("brand") && myJson.has("type") && myJson.has("consumption") && myJson.has("fuelType")){
                carbrand = myJson.getString("brand");
                cartype = myJson.getString("type");
                carconsumption = myJson.getDouble("consumption");
                carfueltype = myJson.getString("fuelType");
            }
        } catch (MissingDataException e) {
            //We're not giving more detailed info, because front-end takes care of that, and the only way
            //  people could get this is if they sent a request directly, which they shouldn't
            //  or if they disabled javascript, in which case they shouldn't be using our website -_-
            return super.makeSimpleJsonResponse("error","MissingDataException");
        }

        Car myCar = null;
        User newUser = null;
        if(carbrand != null && cartype != null && carconsumption != 0 && carfueltype != null){
            myCar = new Car(carbrand, cartype, carconsumption, Car.FuelType.valueOf(carfueltype.toUpperCase()));
            newUser = new User(name, User.Gender.valueOf(gender.toUpperCase()), smoker, password, dOB, username, myCar);
        } else {
            newUser = new User(name, User.Gender.valueOf(gender.toUpperCase()), smoker, password, dOB, username);
        }
        try {
            userService.addUser(newUser);
        } catch (UsernameFormatException e) {
            return super.makeSimpleJsonResponse("error","UsernameFormatException");
        } catch (PasswordFormatException e) {
            return super.makeSimpleJsonResponse("error","PasswordFormatException");
        } catch (UsernameExistsException e) {
            return super.makeSimpleJsonResponse("error","UsernameExistsException");
        }

        if(userService.checkLogin(username, password)){
           json.put("result", "Logged in");
            SessionObject session = userService.getUserSession(username);
            String token = session.getSessionToken();
            javax.servlet.http.Cookie cookie = new javax.servlet.http.Cookie("Token", token);
            cookie.setPath("/");
            //Set max age of cookie to 1 day
            cookie.setMaxAge(60 * 60 * 24);

            response.addCookie(cookie);
        }
        return json.toString();

    }
}
