package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.exceptions.UnauthorizedException;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.threeten.bp.LocalTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tim on 25/02/14.
 */
@Controller
@RequestMapping(value = "/authorized/traject")
public class TrajectController extends BaseController{
    @Autowired
    private TrajectService trajectService;

    @Autowired
    private RouteService routeService;

    @RequestMapping(value="/myupcoming", method=RequestMethod.GET)
    public @ResponseBody String getMyUpcomingTrajects(HttpServletRequest request, HttpServletResponse response) {
        List<UpcomingTrajectDTO> upcoming = trajectService.getUpcomingTrajects(super.getCurrentUser(request));
        return new JSONArray(upcoming).toString();
    }

    @RequestMapping(value="/mine", method=RequestMethod.GET)
    public @ResponseBody String getMyTrajects(HttpServletRequest request, HttpServletResponse response) {
        List<Traject> trajects = trajectService.getAcceptedTrajects(super.getCurrentUser(request));
        List<JSONObject> returndata = new ArrayList<>();
        for (Traject t : trajects) {
            JSONObject jsonT = new JSONObject();
            jsonT.put("id",t.getId());
            jsonT.put("pickup",t.getPickup().getPlace().getName());
            jsonT.put("dropoff",t.getDropoff().getPlace().getName());
            jsonT.put("weekday",t.getPickup().getWeekdayRoute().getDay());
            JSONObject jsonR = new JSONObject();
            jsonR.put("id",t.getRoute().getId());
            JSONObject jsonC = new JSONObject();
            jsonC.put("id",t.getRoute().getChauffeur().getId());
            jsonC.put("name",t.getRoute().getChauffeur().getName());
            jsonC.put("avatarURL",t.getRoute().getChauffeur().getAvatarURL());
            jsonR.put("chauffeur",jsonC);
            jsonT.put("route",jsonR);
            returndata.add(jsonT);
        }
        return new JSONArray(returndata).toString();
    }

    @RequestMapping(value="/i-requested", method=RequestMethod.GET)
    public @ResponseBody String getTrajectsIRequested(HttpServletRequest request, HttpServletResponse response) {
        List<Traject> trajects = trajectService.getRequestedTrajects(super.getCurrentUser(request));
        List<JSONObject> returndata = new ArrayList<>();
        for (Traject t : trajects) {
            JSONObject jsonT = new JSONObject();
            jsonT.put("id",t.getId());
            jsonT.put("pickup",t.getPickup().getPlace().getName());
            jsonT.put("dropoff",t.getDropoff().getPlace().getName());
            jsonT.put("weekday",t.getPickup().getWeekdayRoute().getDay());
            JSONObject jsonR = new JSONObject();
            jsonR.put("id",t.getRoute().getId());
            JSONObject jsonC = new JSONObject();
            jsonC.put("id",t.getRoute().getChauffeur().getId());
            jsonC.put("name",t.getRoute().getChauffeur().getName());
            jsonC.put("avatarURL",t.getRoute().getChauffeur().getAvatarURL());
            jsonR.put("chauffeur",jsonC);
            jsonT.put("route",jsonR);
            returndata.add(jsonT);
        }
        return new JSONArray(returndata).toString();
    }

    @RequestMapping(value="/requested-on-my-routes", method=RequestMethod.GET)
    public @ResponseBody String getTrajectsRequestedOnMyRoutes(HttpServletRequest request, HttpServletResponse response) {
        List<Traject> trajects = trajectService.getRequestedOnMyRoutes(super.getCurrentUser(request));
        List<JSONObject> returndata = new ArrayList<>();
        for (Traject t : trajects) {
            JSONObject jsonT = new JSONObject();
            jsonT.put("id",t.getId());
            jsonT.put("pickup",t.getPickup().getPlace().getName());
            jsonT.put("dropoff",t.getDropoff().getPlace().getName());
            jsonT.put("weekday",t.getPickup().getWeekdayRoute().getDay());
            JSONObject jsonR = new JSONObject();
            jsonR.put("id",t.getRoute().getId());
            JSONObject jsonC = new JSONObject();
            jsonC.put("id", t.getUser().getId());
            jsonC.put("name",t.getUser().getName());
            jsonC.put("avatarURL",t.getUser().getAvatarURL());
            jsonR.put("requester",jsonC);
            jsonT.put("route",jsonR);
            returndata.add(jsonT);
        }
        return new JSONArray(returndata).toString();
    }

    @RequestMapping(value="/{id}/accept", method=RequestMethod.POST)
    public @ResponseBody String acceptTraject(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        try {
            trajectService.acceptTraject(id, super.getCurrentUser(request));
        } catch (UnauthorizedException e) {
            Logger.getLogger(TrajectController.class).error(e.getMessage(),e);
            return super.respondSimpleAuthorized("error","Unauthorized", request, response);
        }
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

    @RequestMapping(value="/{id}/reject", method=RequestMethod.POST)
    public @ResponseBody String rejectTraject(@PathVariable("id") int id, HttpServletRequest request, HttpServletResponse response) {
        try {
            trajectService.rejectTraject(id, super.getCurrentUser(request));
        } catch (UnauthorizedException e) {
            Logger.getLogger(TrajectController.class).error(e.getMessage(),e);
            return super.respondSimpleAuthorized("error","Unauthorized", request, response);
        }
        return super.respondSimpleAuthorized("status", "ok", request, response);
    }

    @RequestMapping(value="/on-route/{routeId}", method=RequestMethod.GET)
    public @ResponseBody String getTrajectsOnRoute(@PathVariable("routeId") int routeId, HttpServletRequest request, HttpServletResponse response) {
        User user = super.getCurrentUser(request);
        Route r = routeService.getRouteById(routeId);
        if (!user.getId().equals(r.getChauffeur().getId())) {
            return super.respondSimpleAuthorized("error","Unauthorized", request, response);
        }
        List<JSONObject> returndata = new ArrayList<>();
        for (Traject t : r.getTrajects()) {
            if (!t.isAccepted()) continue;
            JSONObject jsonT = new JSONObject();
            jsonT.put("id",t.getId());
            jsonT.put("pickup",t.getPickup().getPlace().getName());
            jsonT.put("dropoff",t.getDropoff().getPlace().getName());
            jsonT.put("weekday",t.getPickup().getWeekdayRoute().getDay());
            JSONObject jsonC = new JSONObject();
            jsonC.put("id", t.getUser().getId());
            jsonC.put("name",t.getUser().getName());
            jsonC.put("avatarURL",t.getUser().getAvatarURL());
            jsonT.put("passenger",jsonC);
            returndata.add(jsonT);
        }
        super.updateCookie(request, response);
        return new JSONArray(returndata).toString();

    }
}
