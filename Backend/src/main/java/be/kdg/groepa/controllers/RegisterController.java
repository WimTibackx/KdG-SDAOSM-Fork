package be.kdg.groepa.controllers;

import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalDate;
import javax.servlet.http.HttpServletResponse;
import org.json.*;


/**
 * Created by Tim on 18/02/14.
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String register(@RequestBody String data, HttpServletResponse response){
        JSONObject json = new JSONObject();
        String name, gender, password, username, carbrand, cartype, carfueltype;
        int birthyear, birthmonth, birthday;
        double carconsumption;
        boolean smoker;

        try {
            JSONObject myJson = new JSONObject(data);
            name = (String) myJson.get("name");
            gender = (String) myJson.get("gender");
            smoker = (boolean) myJson.get("smoker");
            password = (String) myJson.get("password");
            birthyear = (int) myJson.get("year");
            birthmonth = (int) myJson.get("month");
            birthday = (int) myJson.get("day");
            username = (String) myJson.get("username");
            carbrand = (String) myJson.get("brand");
            cartype = (String) myJson.get("type");
            carconsumption = (double) myJson.get("consumption");
            carfueltype = (String) myJson.get("fuelType");

            LocalDate birthDate = LocalDate.of(birthyear, birthmonth, birthday);
            Car myCar = null;
            User newUser = null;
            if(carbrand != null && cartype != null && carconsumption != 0 && carfueltype != null){
                myCar = new Car(carbrand, cartype, carconsumption, Car.FuelType.valueOf(carfueltype.toUpperCase()));
                newUser = new User(name, User.Gender.valueOf(gender.toUpperCase()), smoker, password, birthDate, username, myCar);
            } else {
                newUser = new User(name, User.Gender.valueOf(gender.toUpperCase()), smoker, password, birthDate, username);
            }
            userService.addUser(newUser);
            if(userService.checkLogin(username, password)){
               json.put("result", "Logged in");
            }

        } catch (Exception e) {
            json.put("result", e.getMessage());
        }

        return json.toString();

    }
}
