package be.kdg.groepa;

import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Tim on 19/02/14.
 */
/*
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class TrajectTests {

    @Autowired
    private TrajectService trajectService;

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

    private String testUsername = "user@tt.test.com";
    private Route route;
    private User user;
    private Car car;
    /*
    private static boolean initiated = false;
    @Before
    public void init(){
        if(!initiated){
            car = new Car("Audi", "A5", 11, Car.FuelType.SUPER95);
            user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername, car);
            route = new Route(true, 4, LocalDateTime.of(2014, 10, 3, 8, 25), LocalDateTime.of(2014, 10, 3, 17, 0), user, car);
            user.addRoute(route);
            try {
                userService.addUser(user);
                routeService.addRoute(route);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        initiated = true;
    }

    @Test
    public void addTraject(){
        PlaceTime pointA = new PlaceTime(LocalTime.of(8, 25), new Place("UserHome", 10, 20));
        PlaceTime pointB = new PlaceTime(LocalTime.of(16,30), new Place("UserWork", 11, 20));
        trajectService.addTrajectToRoute(new Traject(pointA, pointB), route);
        User u = userService.getUser(testUsername);
        List<Route> routes = user.getRoutes();
        assertEquals("Incorrect amount of PlaceTimes in route", user.getRoutes().get(0).getAllPlaceTimes().size(), 4);
    }

}   */
