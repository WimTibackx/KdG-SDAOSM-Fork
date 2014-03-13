package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.GetRouteDTO;
import be.kdg.groepa.exceptions.*;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by delltvgateway on 2/18/14.
 */
@Controller
@RequestMapping(value = "/authorized/route")
public class RouteController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private TrajectService trajectService;

    //Just to be clear, this means it is at /authorized/route/add
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public @ResponseBody String addRoute(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);
        AddRouteDTO dto;
        try {
            dto = new AddRouteDTO(new JSONObject(data));
        } catch (MissingDataException e) {
            JSONObject missingDataJson = new JSONObject();
            missingDataJson.put("error","ParseError");
            this.updateCookie(request, response);
            return missingDataJson.toString();
        }

        routeService.addRouteByAddRouteDTO(dto, super.getCurrentUser(request));

        JSONObject respJson = new JSONObject();
        respJson.put("status","ok");
        this.updateCookie(request, response);
        return respJson.toString();
    }

    @RequestMapping(value="/{id}",method=RequestMethod.GET)
    public @ResponseBody String getRoute(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        Route r = this.routeService.getRouteById(id);
        return new JSONObject(GetRouteDTO.createFromModel(r,this.routeService.getWeekdayRoutesOfRoute(id))).toString();
    }

    @RequestMapping(value="/mine",method=RequestMethod.GET)
    public @ResponseBody String getMyRoutes(HttpServletRequest request, HttpServletResponse response) {
        List<Route> routes = this.routeService.getRoutes(super.getCurrentUser(request));
        List<JSONObject> returndata = new ArrayList<>();
        for (Route r : routes) {
            JSONObject jsonR = new JSONObject();
            jsonR.put("id",r.getId());
            jsonR.put("startDate",r.getBeginDate().toString());
            jsonR.put("endDate",r.getEndDate().toString());
            jsonR.put("repeating",r.isRepeating());
            returndata.add(jsonR);
        }
        //TODO There isn't much we can really say about routes as such
        //  Maybe it would be more useful if we returned weekdayroutes
        super.updateCookie(request, response);
        return new JSONArray(returndata).toString();
    }

    // Could also do a put here, but maybe it's better to remain consistent and only use GET and POST now
    @RequestMapping(value="/{id}/request-traject", method=RequestMethod.POST)
    public @ResponseBody String requestTraject(@PathVariable("id") int id, @RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        User user = super.getCurrentUser(request);
        Route route = routeService.getRouteById(id);
        JSONObject jsonData = new JSONObject(data);
        int idPickup = jsonData.getInt("pickup");
        int idDropoff = jsonData.getInt("dropoff");

        try {
            trajectService.requestTraject(user, route, idPickup, idDropoff);
        } catch (PlaceTimesOfDifferentRoutesException e) {
            Logger.getLogger(RouteController.class).error(e.getMessage());
            return super.respondSimpleAuthorized("error","PlaceTimesOfDifferentRoutes",request, response);
        } catch (TrajectNotEnoughCapacityException e) {
            Logger.getLogger(RouteController.class).error(e.getMessage());
            return super.respondSimpleAuthorized("error","TrajectNotEnoughCapacity",request, response);
        } catch (PlaceTimesInWrongSequenceException e) {
            Logger.getLogger(RouteController.class).error(e.getMessage());
            return super.respondSimpleAuthorized("error","PlaceTimesInWrongSequence",request, response);
        } catch (PlaceTimesOfDifferentWeekdayRoutesException e) {
            Logger.getLogger(RouteController.class).error(e.getMessage());
            return super.respondSimpleAuthorized("error","PlaceTimesOfDifferentWeekdayRoutes",request, response);
        }

        return super.respondSimpleAuthorized("status","ok",request,response);
    }
}
