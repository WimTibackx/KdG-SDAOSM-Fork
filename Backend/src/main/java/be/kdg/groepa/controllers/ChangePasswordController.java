package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.UserDTO;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tim on 19/02/14.
 */

@Controller
@RequestMapping(value = "/authorized/changepassword")
public class ChangePasswordController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(method= RequestMethod.POST)
    public @ResponseBody String changePassword(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject(), inJson = new JSONObject(data);
        String oldpassword, newpassword;
        User user = null;

        oldpassword = (String) inJson.get("oldpassword");
        newpassword = (String) inJson.get("newpassword");

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies ){
            if (cookie.getName().equals("Token")) {
                SessionObject session = userService.getUserSessionByToken(cookie.getValue());
                user = session.getUser();
            }
        }

        if(user != null){
            int resultValue = userService.changePassword(user.getUsername(), oldpassword, newpassword);
            if(resultValue == 1){
                myJson.put("result", "PasswordChanged");
            } else if (resultValue == 2) {
                myJson.put("result", "OldPasswordWrong");
            } else if (resultValue == 3) {
                myJson.put("result", "NewPasswordFormatWrong");
            }
        } else {
            myJson.put("result", "UserNotFound");
        }
        super.updateCookie(request, response);
        return myJson.toString();





    }

}
