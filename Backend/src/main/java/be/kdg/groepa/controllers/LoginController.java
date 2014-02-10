package be.kdg.groepa.controllers;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import be.kdg.groepa.service.impl.UserServiceImpl;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalDate;

import javax.json.Json;
import javax.json.JsonObject;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thierry on 6/02/14.
 */

@Controller
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody String login(@RequestParam("data") String data, HttpServletResponse response) {
        JSONObject myJson = null;
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(data);

            String username = (String) json.get("username");
            String password = (String) json.get("password");
            SessionObject session = null;
            userService.addUser(new User("username", User.Gender.MALE, false, "Password1", LocalDate.of(1993, 10, 20), "username@test.com"));

            if (userService.checkLogin(username, password)){
                session = userService.getUserSession(username);
                myJson = new JSONObject();
                String token = session.getSessionToken();
                myJson.put("Token", token);
                Cookie cookie = new Cookie("Token", token);
                cookie.setPath("/");
                //Set max age of cookie to 1 day
                cookie.setMaxAge(60 * 60 * 24);

                response.addCookie(cookie);
                return myJson.toString();
            }
            //TODO - Return something if username-password combo is wrong




        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }





}
