package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.UserService;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Tim on 25/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class TrajectControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserDao userDao;

    private static Cookie cookie;
    private static boolean init = false;
    private static String testUsername = "user@tcntrl.test.be";
    private static User user = new User("username", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername);
    private static Car car = new Car("Opel", "Vectra", 11, Car.FuelType.SUPER95);
    private static Route route =  new Route(false, 3, LocalDate.of(2014, 10, 3), LocalDate.of(2015, 10, 4), user, car);

    //@Before
    //TODO: The tests are disabled anyway...
    public void init(){
         if(!init){
             user.addCar(car);
             try {
                 userService.addUser(user);
             } catch (Exception e) {
                 e.printStackTrace();
             }
             new PlaceTime(LocalTime.of(10, 10), new Place("TrajContTestHome", 9, 10), route);
             new PlaceTime(LocalTime.of(11, 0), new Place("TrajContTestWork", 11.222, 0), route);
             routeService.addRoute(route);
             this.userService.checkLogin(testUsername, "Succes1");
             cookie = new Cookie("Token", userService.getUserSession(testUsername).getSessionToken());
             cookie.setPath("/");
             cookie.setMaxAge(60 * 60 * 24);
             init = true;
         }
    }

    /*@Test
    public void successAddTraject() throws Exception {
        JSONObject json = new JSONObject();
        json.put("placeTime1id", user.getRoutes().get(0).getPlaceTimes().get(0).getPlacetimeId());
        json.put("placeTime2id", user.getRoutes().get(0).getPlaceTimes().get(1).getPlacetimeId());
        json.put("newPt1startMin", 10);
        json.put("newPt1startHr", 8);
        json.put("newPt2startMin", 20);
        json.put("newPt2startHr", 9);
        json.put("newPt1lat", 10);
        json.put("newPt1long", 9);
        json.put("newPt2lat", 8);
        json.put("newPt2long", 11.4);
        json.put("newPt1PlaceName", "TrajContTestHome");
        json.put("newPt2PlaceName", "TrajContTestWork");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/traject/add")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Traject added")));
    }*/

    /*@Test
    public void successRemoveTraject() throws Exception {
        JSONObject json = new JSONObject();
        List<Route> routes = userService.getRoutesFromUser(testUsername);
        Route myRoute = routes.get(0);
        Traject traj = myRoute.getTrajects().get(0);
        json.put("routeId", route.getId());
        json.put("trajectId", traj.getId());
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/traject/remove")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Traject removed")));
    }*/

    @Test
    public void foobar() {
        //TO SOLVE "No runnable methods" -_-
       Assert.assertTrue(true);
    }
}
