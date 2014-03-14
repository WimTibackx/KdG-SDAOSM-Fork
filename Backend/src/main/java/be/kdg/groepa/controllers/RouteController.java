package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.ChangeRouteDTO;
import be.kdg.groepa.dtos.GetRouteDTO;
import be.kdg.groepa.dtos.RideDTO;
import be.kdg.groepa.exceptions.*;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.User;
import be.kdg.groepa.model.WeekdayRoute;
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
    int car;
    boolean repeating;
    String startDate, endDate, passageStart, passageEnd, address1, address2, freeSpots;
    double lat1, long1, lat2, long2;

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
            if (r.isRepeating()) {
                for (WeekdayRoute wdr : routeService.getWeekdayRoutesOfRoute(r.getId())) {
                    JSONObject jsonR = new JSONObject();
                    jsonR.put("id",r.getId());
                    jsonR.put("startDate", r.getBeginDate().toString());
                    jsonR.put("endDate",r.getEndDate().toString());
                    jsonR.put("repeating",r.isRepeating());
                    jsonR.put("startPlace",  wdr.getPlaceTimes().get(0).getPlace().getName());
                    jsonR.put("endPlace", wdr.getPlaceTimes().get(wdr.getPlaceTimes().size()-1).getPlace().getName());
                    jsonR.put("day",wdr.getDay());
                    returndata.add(jsonR);
                }
            } else {
                JSONObject jsonR = new JSONObject();
                jsonR.put("id",r.getId());
                jsonR.put("startDate", r.getBeginDate().toString());
                jsonR.put("endDate",r.getEndDate().toString());
                jsonR.put("repeating",r.isRepeating());
                jsonR.put("startPlace",  r.getPlaceTimes().get(0).getPlace().getName());
                jsonR.put("endPlace", r.getPlaceTimes().get(r.getPlaceTimes().size()-1).getPlace().getName());
                jsonR.put("day",r.getBeginDate().getDayOfWeek().getValue()-1);
                returndata.add(jsonR);
            }
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

    @RequestMapping(value="/findCarpoolers", method=RequestMethod.POST)
    public @ResponseBody String findCarpoolers(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);

        RideDTO dto;
        Logger logger =  Logger.getLogger(LoginController.class.getName());
        try {
            dto = new RideDTO(dataOb);
        } catch (MissingDataException e) {

            logger.info("DATA IS "+ e);
            JSONObject missingDataJson = new JSONObject();
            missingDataJson.put("error","ParseError");
            this.updateCookie(request, response);
            return missingDataJson.toString();
        }

        logger.info("TimeDiff is: " + dto);

        List<Integer> routes = routeService.findCarpoolers(dto.getStartLat(), dto.getStartLon(), dto.getEndLat(), dto.getEndLon(), dto.getG(), dto.isSmoker(), dto.getRadius(), dto.getDep(), dto.getTimeDiff());
        logger.info(routes.size());
        // TODO: return found routes in JSON objects
        List<JSONObject> returndata = new ArrayList<>();
        for(Integer routeId : routes){
            Route r = routeService.getRouteById(routeId);
            if (r.isRepeating()) {
                for (WeekdayRoute wdr : routeService.getWeekdayRoutesOfRoute(r.getId())) {
                    JSONObject jsonR = new JSONObject();
                    jsonR.put("id",r.getId());
                    jsonR.put("startDate", r.getBeginDate().toString());
                    jsonR.put("endDate",r.getEndDate().toString());
                    jsonR.put("repeating",r.isRepeating());
                    jsonR.put("startPlace",  wdr.getPlaceTimes().get(0).getPlace().getName());
                    jsonR.put("endPlace", wdr.getPlaceTimes().get(wdr.getPlaceTimes().size()-1).getPlace().getName());
                    jsonR.put("day",wdr.getDay());
                    returndata.add(jsonR);
                }
            } else {
                JSONObject jsonR = new JSONObject();
                jsonR.put("id",r.getId());
                jsonR.put("startDate", r.getBeginDate().toString());
                jsonR.put("endDate",r.getEndDate().toString());
                jsonR.put("repeating",r.isRepeating());
                jsonR.put("startPlace",  r.getPlaceTimes().get(0).getPlace().getName());
                jsonR.put("endPlace", r.getPlaceTimes().get(r.getPlaceTimes().size()-1).getPlace().getName());
                jsonR.put("day",r.getBeginDate().getDayOfWeek().getValue()-1);
                returndata.add(jsonR);
            }
           }

        this.updateCookie(request, response);
        return new JSONArray(returndata).toString();
        //return super.respondSimpleAuthorized("confirmed", "ride confirmed", request, response);
    }

    @RequestMapping(value="/{id}/change", method=RequestMethod.POST)
    public @ResponseBody String change(@PathVariable("id") int id, @RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        ChangeRouteDTO dto=null;
        try {
            dto = new ChangeRouteDTO(new JSONObject(data));
        } catch (MissingDataException e) {
            Logger.getLogger(RouteController.class).error(e.getMessage(), e);
            super.updateCookie(request, response);
            JSONObject errorData = new JSONObject();
            errorData.put("error","MissingData");
            errorData.put("specific",e.getMessage());
            return errorData.toString();
        }

        try {
            routeService.editRoute(dto,super.getCurrentUser(request));
        } catch (UnauthorizedException e) {
            return super.respondSimpleAuthorized("error","Unauthorized", request, response);
        }
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }
}
