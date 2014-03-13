package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.RideDTO;
import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TextMessageService;
import be.kdg.groepa.service.api.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalDateTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pieter-Jan on 8-3-14.
 */
@Controller
@RequestMapping("/authorized/ride")
public class RideController extends BaseController {

    @Autowired
    private RouteService routeService;

    @RequestMapping(value="/confirmRide",method= RequestMethod.POST)
    public @ResponseBody void confirmRide(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);
        LocalDateTime date = null;
        int routeId = 0;
        try {
            if (!dataOb.has("routeId")) throw new MissingDataException("routeId");
            if (!dataOb.has("year")) throw new MissingDataException("year");
            if (!dataOb.has("month")) throw new MissingDataException("month");
            if (!dataOb.has("day")) throw new MissingDataException("day");
            if (!dataOb.has("hours")) throw new MissingDataException("hours");
            if (!dataOb.has("minutes")) throw new MissingDataException("minutes");
            // if (!dataOb.has("seconds")) throw new MissingDataException("seconds");  -> Not really necessary
        } catch (MissingDataException e) {
            JSONObject missingDataJson = new JSONObject();
            missingDataJson.put("error","ParseError");
            this.updateCookie(request, response);
            //return missingDataJson.toString();
        }
        routeId = dataOb.getInt("routeId");
        date = LocalDateTime.of(dataOb.getInt("year"), dataOb.getInt("month"), dataOb.getInt("day"), dataOb.getInt("hours"), dataOb.getInt("minutes"));
        routeService.confirmRide(routeId, date);
    }
}