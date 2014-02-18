package be.kdg.groepa.controllers;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * Created by delltvgateway on 2/18/14.
 */
@Controller
@RequestMapping(value = "/authorized/route")
public class RouteController {

    //Just to be clear, this means it is at /authorized/route/add
    @RequestMapping(value="/add", method= RequestMethod.POST)
    public @ResponseBody String addRoute(@RequestBody String data, HttpServletResponse response) {

        try {
            AddRouteDTO dto = new AddRouteDTO(new JSONObject(data));
        } catch (MissingDataException e) {
            JSONObject missingDataJson = new JSONObject();
            missingDataJson.put("error","ParseError");
            return missingDataJson.toString();
        }

        JSONObject respJson = new JSONObject();
        respJson.put("test","foobar");
        return respJson.toString();
    }
}
