package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.TextMessageDTO;
import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameExistsException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import com.google.gson.Gson;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.LocalDate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * Created by Tim on 05/03/14.
 */
/*
    The AndroidController provides functions that Android needs, more specifically
    for Google Cloud Messaging.
 */
@Controller
@RequestMapping(value = "/authorized/user")
public class AndroidController extends BaseController{

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.POST, value = "/registerandroid")
    public @ResponseBody String registerAndroid(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        Logger logger = Logger.getLogger("RegisterAndroid");
        logger.debug("Starting registerAndroid");
        JSONObject json = new JSONObject();
        String username, id;

        JSONObject myJson = new JSONObject(data);
        username = myJson.getString("username");
        id = myJson.getString("id");
        logger.debug("USERNAME: " + username);
        logger.debug("ID: " + id);
        try{
            logger.debug("Attempting setUserAndroidId");
            userService.setUserAndroidId(username, id);
        } catch (Exception e){
            logger.debug("ERROR OCCURED: " + e.getMessage());
            json.put("error", "An error occured");
            return json.toString();
        }
        logger.debug("Succesfully sent to backEnd?");
        json.put("result", "Success");
        return json.toString();
    }

    @RequestMapping(value = "/getandroid/{id}", method = RequestMethod.GET)
    public @ResponseBody String getAndroidIdFromUser(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();

        User user = userService.getUserById(id);
        if (user == null) {
            myJson.put("error","User does not exist");
            return myJson.toString();
        }
        String androidId = user.getAndroidId();
        if(androidId == null || !androidId.isEmpty()){
            myJson.put("androidId", androidId);
        } else {
            myJson.put("error", "User does not have valid android id");
        }
        return myJson.toString();
    }
}
