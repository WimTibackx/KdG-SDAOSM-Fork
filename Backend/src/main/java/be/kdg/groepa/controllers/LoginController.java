package be.kdg.groepa.controllers;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public  @ResponseBody SessionObject login(@RequestParam("data") String data) {
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(data);
            String username = (String) json.get("username");
            String password = (String) json.get("password");
            SessionObject session = null;
            if (userService.checkLogin(username, userService.encryptString(password))){
                session = userService.getUserSession(username);
                //Cookie cookie = new Cookie("Token", session.getSessionToken());
                //Set max age of cookie to 1 day
                //cookie.setMaxAge(86400000);

                //response.addCookie(cookie);
            }
            return session;




        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }





}
