package be.kdg.groepa.controllers;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thierry on 6/02/14.
 */

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;



    /*
     * Input: { "username": "username@test1.com", "password": "Password1" }
     * Output: { "error": "ParseError" } OR { "error": "LoginComboWrong" } OR { "Token": "(a token)" } + Set-Cookie header
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestBody String data, HttpServletResponse response) {
        JSONObject myJson = null;
        String username, password;

        Logger logger =  Logger.getLogger(LoginController.class.getName());
        logger.info("DATA IS "+data);
        try {
            JSONObject json = (JSONObject) new JSONParser().parse(data);
            if (!json.containsKey("username") || !json.containsKey("password")) {
                throw new ParseException(1); //Not sure what the 1 is. Would like a more generic general system
            }

            username = (String) json.get("username");
            password = (String) json.get("password");
        } catch (ParseException e) {
            myJson = new JSONObject();
            myJson.put("error","ParseError");
            return myJson.toString();
        }

        if (!userService.checkLogin(username, password)){
            myJson = new JSONObject();
            myJson.put("error","LoginComboWrong");
            return myJson.toString();
        }

        SessionObject session = userService.getUserSession(username);
        User u = userService.getUser(username);
        myJson = new JSONObject();
        String token = session.getSessionToken();
        myJson.put("Token", token);
        myJson.put("UserId", u.getId());
        Cookie cookie = new Cookie("Token", token);
        cookie.setPath("/");
        //Set max age of cookie to 1 day
        cookie.setMaxAge(60 * 60 * 24);

        response.addCookie(cookie);
        return myJson.toString();
    }

    @RequestMapping(value="/authorized/checkAuthorization")
    public @ResponseBody String checkAuthorization(HttpServletRequest request, HttpServletResponse response) {
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

}
