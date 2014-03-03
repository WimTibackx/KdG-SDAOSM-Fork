package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.UpcomingTrajectDTO;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Tim on 25/02/14.
 */
@Controller
@RequestMapping(value = "/authorized/traject")
public class TrajectController extends BaseController{
    @Autowired
    private UserService userService;

    @Autowired
    private TrajectService trajectService;

    @Autowired
    private RouteService routeService;



    @RequestMapping(value="/add", method= RequestMethod.POST)
    public @ResponseBody String addTraject(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();
        JSONObject dataOb = new JSONObject(data);

        int placeTime1id, placeTime2id, newPt1startMin, newPt1startHr, newPt2startHr, newPt2startMin;
        double newPt1lat, newPt1long, newPt2lat, newPt2long;
        String newPt1PlaceName, newPt2PlaceName;

        newPt1PlaceName = dataOb.getString("newPt1PlaceName");
        newPt2PlaceName = dataOb.getString("newPt2PlaceName");
        placeTime1id = dataOb.getInt("placeTime1id");
        placeTime2id = dataOb.getInt("placeTime2id");
        newPt1startMin = dataOb.getInt("newPt1startMin");
        newPt1startHr = dataOb.getInt("newPt1startHr");
        newPt2startMin = dataOb.getInt("newPt2startMin");
        newPt2startHr = dataOb.getInt("newPt2startHr");
        newPt1lat = dataOb.getDouble("newPt1lat");
        newPt1long = dataOb.getDouble("newPt1long");
        newPt2lat = dataOb.getDouble("newPt2lat");
        newPt2long = dataOb.getDouble("newPt2long");

        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies ){
            if (cookie.getName().equals("Token")) {
                SessionObject session = userService.getUserSessionByToken(cookie.getValue());
                user = session.getUser();
            }
        }

        PlaceTime newPlaceTime1 = new PlaceTime(LocalTime.of(newPt1startHr, newPt1startMin), new Place(newPt1PlaceName, newPt1lat, newPt1long));
        PlaceTime newPlaceTime2 = new PlaceTime(LocalTime.of(newPt2startHr, newPt2startMin), new Place(newPt2PlaceName, newPt2lat, newPt2long));

        PlaceTime previousPlaceTime = routeService.getPlaceTimeById(placeTime1id);
        PlaceTime previousPlaceTime2 = routeService.getPlaceTimeById(placeTime2id);

        try{
            trajectService.addNewTrajectToRoute(previousPlaceTime, newPlaceTime1, previousPlaceTime2, newPlaceTime2, user);
        } catch (Exception e){
            myJson.put("result", "Traject not added: exception occured.");
            e.printStackTrace();
            return myJson.toString();
        }
        myJson.put("result", "Traject added");
        return myJson.toString();
    }

    @RequestMapping(value="/remove", method= RequestMethod.POST)
    public @ResponseBody String removeTraject(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject myJson = new JSONObject();
        JSONObject dataOb = new JSONObject(data);

        int routeId, trajectId;

        routeId = dataOb.getInt("routeId");
        trajectId = dataOb.getInt("trajectId");

        Route route = routeService.getRouteById(routeId);
        Traject traject = trajectService.getTrajectById(trajectId);
        try{
            trajectService.removeTrajectFromRoute(route, traject);
        } catch (Exception e){
            myJson.put("result", "Route not removed: error occured.");
            e.printStackTrace();
            return myJson.toString();
        }
        myJson.put("result", "Traject removed");
        return myJson.toString();
    }

    @RequestMapping(value="/myupcoming", method=RequestMethod.GET)
    public @ResponseBody String getMyUpcomingTrajects(HttpServletRequest request, HttpServletResponse response) {
        List<UpcomingTrajectDTO> upcoming = trajectService.getUpcomingTrajects(super.getCurrentUser(request));
        return new JSONArray(upcoming).toString();
    }
}
