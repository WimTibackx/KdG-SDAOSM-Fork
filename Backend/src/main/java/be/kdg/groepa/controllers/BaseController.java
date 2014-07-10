package be.kdg.groepa.controllers;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;

import org.json.JSONObject;
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
	
	protected final static int COOKIELENGTH = 86400; // 1 day

    @Autowired
    private UserService userService;
    
    protected void makeCookie(String token, HttpServletResponse response) {
    	Cookie cookie = new Cookie("Token", token);
        cookie.setPath("/");
        cookie.setMaxAge(BaseController.COOKIELENGTH);
        response.addCookie(cookie);
    }

    protected void updateCookie(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies){
            if (cookie.getName() != null && cookie.getName().equals("Token")) {
                Logger.getGlobal().info("Token cookie value is: "+cookie.getValue());
                if (userService.isUserSessionByToken(cookie.getValue())) {
                    this.makeCookie(cookie.getValue(), response);
                }
            }
        }
    }

    protected User getCurrentUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies.length == 0) { return null; }
        for (Cookie cookie : cookies ){
            if (cookie.getName().equals("Token")) {
                SessionObject session = userService.getUserSessionByToken(cookie.getValue());
                if (session == null) { return null; }
                return session.getUser();
            }
        }
        return null;
    }

    protected String makeSimpleJsonResponse(String key, String value) {
        JSONObject obj = new JSONObject();
        obj.put(key, value);
        return obj.toString();
    }

    protected String respondSimpleAuthorized(String key, String value, HttpServletRequest request, HttpServletResponse response) {
        this.updateCookie(request, response);
        return this.makeSimpleJsonResponse(key, value);
    }
}
