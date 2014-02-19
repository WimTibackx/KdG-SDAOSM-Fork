package be.kdg.groepa.controllers;

import be.kdg.groepa.service.api.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

/**
 * Created by delltvgateway on 2/19/14.
 */
@Controller
public class BaseController {

    @Autowired
    private UserService userService;

    protected void updateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies ){
            if (cookie.getName() != null && cookie.getName().equals("Token")) {
                Logger.getGlobal().info("Token cookie value is: "+cookie.getValue());
                if (userService.isUserSessionByToken(cookie.getValue())) {
                    Cookie updatedCookie = new Cookie("Token", cookie.getValue());
                    updatedCookie.setPath("/");
                    //Set max age of cookie to 1 day
                    updatedCookie.setMaxAge(60 * 60 * 24);
                    response.addCookie(updatedCookie);
                }
            }
        }
    }
}
