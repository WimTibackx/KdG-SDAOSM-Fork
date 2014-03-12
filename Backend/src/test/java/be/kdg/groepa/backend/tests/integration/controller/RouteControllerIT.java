package be.kdg.groepa.backend.tests.integration.controller;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import javax.ejb.Local;
import javax.servlet.http.Cookie;

/**
 * Created by delltvgateway on 2/18/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RouteControllerIT {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static boolean init = false;

    private Cookie cookie;

    @Autowired
    private CarService carService;

    @Autowired
    private RouteService routeService;

    private int carId;

    @Before
    public void init() throws Exception {
        String username = "username@route.controller.it.example.com";
        String password = "Password1";
        if (!init) {
            User user = new User("TestUser", User.Gender.FEMALE, false, password, LocalDate.of(1993, 1, 1), username);
            this.userService.addUser(user);
            Car car = new Car("Ford", "Fiesta",8.1, Car.FuelType.DIESEL);
            carService.addCar(username, car);
            this.carId = car.getCarId();
        }
        this.userService.checkLogin(username, password);
        SessionObject session = this.userService.getUserSession(username);
        this.cookie = new Cookie("Token", session.getSessionToken());
        cookie.setPath("/");
        //Set max age of cookie to 1 day
        cookie.setMaxAge(60 * 60 * 24);
        RouteControllerIT.init = true;
    }

    @Test
    public void addErroringData() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/route/add")
            .cookie(this.cookie)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content("{ \"car\": "+this.carId+", \"freeSpots\": \"3\" }"))
            .andExpect(MockMvcResultMatchers.jsonPath("error").value("ParseError"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    public void addSuccessfulData() throws Exception {

        String data = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Tu\": [\"09:30\", \"10:00\"],\"Th\": [\"09:30\", \"10:00\"],\"Fr\": [\"09:30\", \"10:00\"],\"We\": [\"12:30\", \"13:00\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/route/add")
            .cookie(this.cookie)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(data))
            .andExpect(MockMvcResultMatchers.jsonPath("status").value("ok"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void requestTraj() throws Exception {
        User userA = new User("Benjamin Verdin", User.Gender.MALE, false, "Password1", LocalDate.of(1975,07,29), "benjamin.verdin1@traj.example.com");
        userService.addUser(userA);
        User userB = new User("Jeanne Wijffels", User.Gender.FEMALE, false, "Password1", LocalDate.of(1956,07,10), "jeanne.wijffels1@traj.example.com");
        userService.addUser(userB);
        Car carA = new Car("Ford", "Fiesta", 8.1, Car.FuelType.DIESEL);
        carService.addCar("benjamin.verdin1@traj.example.com",carA);
        Route route = new Route(false, 2, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2), userA, carA);
        Place placeA = new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710);
        Place placeB = new Place("N115 2-30, 2960 Brecht, Belgium", 51.351255, 4.641555);
        Place placeC = new Place("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium", 51.208078, 4.442945);
        PlaceTime ptA = new PlaceTime(LocalTime.of(6, 45),placeA,route);
        PlaceTime ptB = new PlaceTime(LocalTime.of(7,3), placeB, route);
        PlaceTime ptC = new PlaceTime(LocalTime.of(7,17), placeC, route);
        routeService.addRoute(route);

        Route routeReloaded = routeService.getRoutes(userService.getUser("benjamin.verdin1@traj.example.com")).get(0);
        int pickupId=-1, dropoffId = -1;
        for (PlaceTime pt : routeReloaded.getPlaceTimes()) {
            if (pt.getPlace().getName().equals("N14 163-193, 2320 Hoogstraten, Belgium")) {
                pickupId = pt.getPlacetimeId();
            }
            if (pt.getPlace().getName().equals("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium")) {
                dropoffId = pt.getPlacetimeId();
            }
        }

        this.userService.checkLogin("jeanne.wijffels1@traj.example.com", "Password1");
        SessionObject session = this.userService.getUserSession("jeanne.wijffels1@traj.example.com");
        Cookie cookie = new Cookie("Token", session.getSessionToken());
        cookie.setPath("/");
        //Set max age of cookie to 1 day
        cookie.setMaxAge(60 * 60 * 24);
        String data = "{\"pickup\": "+pickupId+", \"dropoff\": "+dropoffId+"}";
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/route/"+routeReloaded.getId()+"/request-traject")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(data))
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("ok"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
