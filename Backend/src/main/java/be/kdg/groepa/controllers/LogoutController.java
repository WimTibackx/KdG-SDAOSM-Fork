package be.kdg.groepa.controllers;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thierry on 20/02/14.
 */
@Controller
@RequestMapping(value = "/authorized/logout")
public class LogoutController extends BaseController{
        /*
         * Input: { "username": "username@test1.com", "password": "Password1" }
         * Output: { "error": "ParseError" } OR { "error": "LoginComboWrong" } OR { "Token": "(a token)" } + Set-Cookie header
         */
        @RequestMapping(method = RequestMethod.GET)
        public @ResponseBody
        String login(HttpServletResponse response) {
            Cookie deleteCookie = new Cookie("Token", "");
            deleteCookie.setPath("/");
            deleteCookie.setMaxAge(0);
            response.addCookie(deleteCookie);
            JSONObject json = new JSONObject();
            json.put("Succes", "Succes");
            return json.toString();
        }


    }
