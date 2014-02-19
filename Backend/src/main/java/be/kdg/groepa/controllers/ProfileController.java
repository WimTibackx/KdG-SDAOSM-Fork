package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.UserDTO;
import be.kdg.groepa.model.SessionObject;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thierry on 14/02/14.
 */
@Controller
public class ProfileController extends BaseController {

    @Autowired
    private UserService userService;



    /*
     * Input: { "username": "username@test1.com", "password": "Password1" }
     * Output: { "error": "ParseError" } OR { "error": "LoginComboWrong" } OR { "Token": "(a token)" } + Set-Cookie header
     */
    @RequestMapping(value = "/authorized/profile/{id}", method = RequestMethod.GET)

    public @ResponseBody String getProfile(@PathVariable("id") Integer id, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = null;

        User user = userService.getUserById(id);
        if (user == null) {
            myJson = new JSONObject();
            myJson.put("error","UserDoesNotExist");
            this.updateCookie(request, response);
            return myJson.toString();
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setCars(user.getCars());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setGender(user.getGender());
        userDTO.setName(user.getName());
        userDTO.setSmoker(user.isSmoker());
        userDTO.setUsername(user.getUsername());

        Gson gson = new Gson();
        String json = gson.toJson(userDTO);

        this.updateCookie(request, response);

        return json;

    }


    @RequestMapping(value = "/authorized/myprofile", method = RequestMethod.GET)

    public @ResponseBody String getMyProfile(HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = null;
        String json = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies ){
            if (cookie.getName().equals("Token")) {
                SessionObject session = userService.getUserSessionByToken(cookie.getValue());
                User user = session.getUser();
                UserDTO userDTO = new UserDTO();
                userDTO.setCars(user.getCars());
                userDTO.setDateOfBirth(user.getDateOfBirth());
                userDTO.setGender(user.getGender());
                userDTO.setName(user.getName());
                userDTO.setSmoker(user.isSmoker());
                userDTO.setUsername(user.getUsername());

                Gson gson = new Gson();
                json = gson.toJson(userDTO);
            }
        }
        if (json != null){
            this.updateCookie(request, response);
            return json;
        }

        myJson = new JSONObject();
        myJson.put("error","UserDoesNotExist");
        this.updateCookie(request, response);
        return myJson.toString();
    }



}