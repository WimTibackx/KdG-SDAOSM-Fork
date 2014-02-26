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

import static org.junit.Assert.*;

/**
 * Created by Tim on 19/02/14.
 */

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
    private String testUsername2 = "user2@tt.test.com";
    private String testUsername3 = "user3@tt.test.com";
    private String testUsername4 = "user4@tt.test.com";
    private static Route routeA;
    private static Route routeB;
    private static Route routeC;
    private static Route routeD;
    private static Route routeE;
    private static User user;
    private static User user2;
    private static User user3;
    private static User user4;
    private static Car car;

    private static boolean initiated = false;
    @Before
    public void init(){
        if(!initiated){
            car = new Car("Audi", "A5", 11, Car.FuelType.SUPER95);
            user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername, car);
            user2 = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername2, car);
            user3 = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername3, car);
            user4 = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername4, car);
            try{
                userService.addUser(user);
                userService.addUser(user2);
                userService.addUser(user3);
                userService.addUser(user4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            routeA = new Route(true, 4, LocalDateTime.of(2014, 10, 3, 8, 25), LocalDateTime.of(2014, 10, 3, 17, 0), user, car, new PlaceTime(LocalTime.of(8, 30), new Place("RouteAHome", 10, 20)), new PlaceTime(LocalTime.of(16,30), new Place("RouteAWork", 11, 20)));
            routeB = new Route(true, 4, LocalDateTime.of(2014, 10, 3, 8, 25), LocalDateTime.of(2014, 10, 3, 17, 0), user2, car, new PlaceTime(LocalTime.of(8, 30), new Place("RouteBHome", 10, 20)), new PlaceTime(LocalTime.of(16,30), new Place("RouteBWork", 11, 20)));
            routeC = new Route(true, 4, LocalDateTime.of(2014, 10, 3, 8, 25), LocalDateTime.of(2014, 10, 3, 17, 0), user3, car, new PlaceTime(LocalTime.of(8, 30), new Place("RouteCHome", 10, 20)), new PlaceTime(LocalTime.of(16,30), new Place("RouteCWork", 11, 20)));
            routeD = new Route(true, 4, LocalDateTime.of(2014, 10, 3, 8, 25), LocalDateTime.of(2014, 10, 3, 17, 0), user4, car, new PlaceTime(LocalTime.of(8, 30), new Place("RouteCHome", 10, 20)), new PlaceTime(LocalTime.of(16,30), new Place("RouteCWork", 11, 20)));
            user.addRoute(routeA);
            user2.addRoute(routeB);
            user3.addRoute(routeC);
            user4.addRoute(routeD);
            routeService.addRoute(routeA);
            routeService.addRoute(routeB);
            routeService.addRoute(routeC);
            routeService.addRoute(routeD);
        }
        initiated = true;
    }

    @Test
    public void addTrajectToRoute(){
        PlaceTime pointA = new PlaceTime(LocalTime.of(8, 25), new Place("UserHome", 10, 20));
        PlaceTime pointC = new PlaceTime(LocalTime.of(12,30), new Place("UserWork", 11, 20));
        trajectService.addTraject(new Traject(pointA, pointC, routeA, user));
        assertEquals("Incorrect amount of PlaceTimes in route", 6, user.getRoutes().get(0).getAllPlaceTimes().size());  // actually expected 4, but route now gets an extra Traject by default (so 2 extra PlaceTimes)
    }

    @Test
    public void removeTrajectFromRoute(){
        Traject traj = new Traject(new PlaceTime(LocalTime.of(10, 25), new Place("UserHome", 10, 20)), new PlaceTime(LocalTime.of(15,30), new Place("UserWork", 11, 20)), routeB, user2);
        trajectService.addTraject(traj);
        assertEquals("Not enough PlaceTimes in route", 6, user2.getRoutes().get(0).getAllPlaceTimes().size()); // Same story as "addTrajectToRoute"
        trajectService.removeTrajectFromRoute(routeB, traj);
        assertEquals("Wrong amount of PlaceTimes in route", 4, user2.getRoutes().get(0).getAllPlaceTimes().size()); // Same story ^.
    }

    @Test
    public void addPlaceTimeAfterExistingRoutePoint(){
        PlaceTime newPlaceTime2 = new PlaceTime(LocalTime.of(9, 30), new Place("OtherUserHome", 9, 18));
        trajectService.insertNewRoutePoint(routeC.getPlaceTimes().get(0), newPlaceTime2);
        assertEquals("Incorrect PlaceTime at given position of route", newPlaceTime2, routeC.getAllPlaceTimes().get(1));
    }

    @Test
    public void addTrajectToRouteWithPoints(){
        PlaceTime newPlaceTime = new PlaceTime(LocalTime.of(9, 30), new Place("OtherUserHome", 9, 18));
        PlaceTime newPlaceTime3 = new PlaceTime(LocalTime.of(17,45), new Place("OtherUserWork", 9, 20));
        trajectService.addNewTrajectToRoute(routeD.getPlaceTimes().get(0), newPlaceTime, routeD.getPlaceTimes().get(1), newPlaceTime3, user4);
        assertEquals("New PlaceTime 1 is not at the right position", routeD.getPlaceTimes().get(1), newPlaceTime);
        assertEquals("New PlaceTime 2 is not at the rigth position", routeD.getPlaceTimes().get(3), newPlaceTime3);
    }

    //@Test
    public void removeTrajectWithDoublePlaceTimes(){

    }
}
