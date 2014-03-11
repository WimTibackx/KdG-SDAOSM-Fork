package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.exceptions.CarNotFoundException;
import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Route;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.UserService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

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

    int car;
    boolean repeating;
    String startDate, endDate, passageStart, passageEnd, address1, address2, freeSpots;
    double lat1, long1, lat2, long2;


    //Just to be clear, this means it is at /authorized/route/add
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public @ResponseBody String addRoute(@RequestBody String data, HttpServletRequest request, HttpServletResponse response) {
        JSONObject dataOb = new JSONObject(data);
        try {
            AddRouteDTO dto = new AddRouteDTO(new JSONObject(data));
        } catch (MissingDataException e) {
            JSONObject missingDataJson = new JSONObject();
            missingDataJson.put("error","ParseError");
            this.updateCookie(request, response);
            return missingDataJson.toString();
        }
        User user = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies ){
            if (cookie.getName().equals("Token")) {
                SessionObject session = userService.getUserSessionByToken(cookie.getValue());
                user = session.getUser();
            }
        }
        car = (int) dataOb.get("car");
        freeSpots = (String) dataOb.get("freeSpots");
        repeating = (boolean) dataOb.get("repeating");
        startDate = (String) dataOb.get("startDate");
        endDate = (String) dataOb.get("endDate");
        String classtype = dataOb.get("passages").toString();
        JSONArray passageStr = dataOb.getJSONArray("passages");
        String[] passageBeginStr = ((String)passageStr.get(0)).split(":");
        String[] passageBeginStrDate = startDate.split("-");
        String[] passageEndStr = ((String)passageStr.get(1)).split(":");
        String[] passageEndStrDate = startDate.split("-");
        JSONArray placeTimeMaps = dataOb.getJSONArray("route");
        HashMap startMap = (HashMap) placeTimeMaps.get(0);
        HashMap endMap = (HashMap) placeTimeMaps.get(1);
        LocalDateTime beginDate = LocalDateTime.of(Integer.parseInt(passageBeginStrDate[0]), Integer.parseInt(passageBeginStrDate[1]), Integer.parseInt(passageBeginStrDate[2]), Integer.parseInt(passageBeginStr[0]), Integer.parseInt(passageBeginStr[1]));
        LocalDateTime endDate = LocalDateTime.of(Integer.parseInt(passageEndStrDate[0]), Integer.parseInt(passageEndStrDate[1]), Integer.parseInt(passageEndStrDate[2]), Integer.parseInt(passageEndStr[0]), Integer.parseInt(passageEndStr[1]));
        PlaceTime start = new PlaceTime(LocalTime.of(Integer.parseInt(passageBeginStr[0]), Integer.parseInt(passageBeginStr[1])));
        PlaceTime end = new PlaceTime(LocalTime.of(Integer.parseInt(passageEndStr[0]), Integer.parseInt(passageEndStr[1])));
        try {
            Route newRoute = new Route(repeating, Integer.parseInt(freeSpots), beginDate, endDate, user, carService.get(car), start, end);
        } catch (CarNotFoundException e) {
            //TODO: Loggen
            e.printStackTrace();
        }
        JSONObject respJson = new JSONObject();
        respJson.put("test","foobar");
        this.updateCookie(request, response);
        return respJson.toString();
    }
}
