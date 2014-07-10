package be.kdg.groepa.controllers;

import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Thierry on 6/02/14.
 */

@Controller
public class LoginController extends BaseController {

    private UserService userService;
    
    @Autowired
    public void setUserService(UserService userService) {
    	this.userService = userService;
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public @ResponseBody String login(@RequestBody String data, HttpServletResponse response) {
        JSONObject dataOb = null;
        String username, password;
        Logger logger =  Logger.getLogger(LoginController.class.getName());
        
        try {
        	dataOb = new JSONObject(data);
        } catch (JSONException e) {
        	logger.error("LoginController::login hit a JSONException for data "+data,e);
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	return new JSONObject().put("error", "JSONException").toString();
        }
        
        try {
        	if (!dataOb.has("username")) throw new MissingDataException("username");
        	if (!dataOb.has("password")) throw new MissingDataException("password");
        } catch (MissingDataException e) {
        	logger.error("MissingDataException \""+e.getMessage()+"\" for "+data, e);
        	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        	return new JSONObject().put("error","MissingDataException").put("missing", e.getMessage()).toString();
        }
        
        username = dataOb.getString("username");
        password = dataOb.getString("password");
        
        if (!userService.checkLogin(username, password)) {
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        	return new JSONObject().toString();
        }        

        SessionObject session = userService.getUserSession(username);
        String token = session.getSessionToken();
        
        super.makeCookie(token, response);
        
        JSONObject responseContent = new JSONObject();
        responseContent.put("Token", token);
        responseContent.put("UserId", session.getUser().getId());
        return responseContent.toString();
    }

    @RequestMapping(value="/authorized/checkAuthorization")
    public @ResponseBody String checkAuthorization(HttpServletRequest request, HttpServletResponse response) {
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/resetPassword")
    public @ResponseBody String resetPassword(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataJson = new JSONObject(data), myJson = new JSONObject();
        String username = dataJson.getString("username"), result;
        try {
            result = userService.resetPassword(username);
        } catch (Exception e) {
            myJson.put("error", e.getMessage());
            return myJson.toString();
        }
        myJson.put("success", 1);
        return myJson.toString();
    }

}
