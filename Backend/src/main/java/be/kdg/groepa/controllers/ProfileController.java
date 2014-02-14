package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.UserDTO;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Thierry on 14/02/14.
 */
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;



    /*
     * Input: { "username": "username@test1.com", "password": "Password1" }
     * Output: { "error": "ParseError" } OR { "error": "LoginComboWrong" } OR { "Token": "(a token)" } + Set-Cookie header
     */
    @RequestMapping(value = "/authorized/profile/{id}", method = RequestMethod.GET)

    public @ResponseBody String getProfile(@PathVariable("id") Integer id) {
        JSONObject myJson = null;
        String testUsername = "test@test.com";
        /*User user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername);

        try {
            userService.addUser(user);
            userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
            userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
            userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
        } catch (Exception e) {
            e.printStackTrace();
        } */
        User user = userService.getUserById(id);

        UserDTO userDTO = new UserDTO();
        userDTO.setCars(user.getCars());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setSmoker(user.isSmoker());
        userDTO.setUsername(user.getUsername());

        Gson gson = new Gson();
        String json = gson.toJson(userDTO);

        return json;

    }



}