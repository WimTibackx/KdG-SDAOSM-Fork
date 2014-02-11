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
import org.threeten.bp.LocalDate;

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

    /*
     * Input: { "username": "username@test1.com", "password": "Password1" }
     * Output: { "error": "ParseError" } OR { "error": "LoginComboWrong" } OR { "Token": "(a token)" } + Set-Cookie header
     */
    @RequestMapping(method = RequestMethod.POST)
    public @ResponseBody String login(@RequestParam("data") String data, HttpServletResponse response) {
        JSONObject myJson = null;
        String username, password;

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


        try {   //TODO - We shouldn't even be doing this
            userService.addUser(new User("username", User.Gender.MALE, false, "Password1", LocalDate.of(1993, 10, 20), "username@test.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    //

        if (!userService.checkLogin(username, password)){
            myJson = new JSONObject();
            myJson.put("error","LoginComboWrong");
            return myJson.toString();
        }

        SessionObject session = userService.getUserSession(username);
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


}
