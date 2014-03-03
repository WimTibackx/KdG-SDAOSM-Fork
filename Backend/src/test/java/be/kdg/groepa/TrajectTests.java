package be.kdg.groepa;

import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Assert;
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

    @Autowired
    private TrajectDao trajectDao;

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

    /*
     * This method tests TrajectService.getNextDayOfTraject(Traject)
     * We'll need:  a repeating traject that ends in a couple of weeks
     *              a repeating traject that ends today
     *              a repeating traject that ended yesterday (expected: null)
     *              a non-repeating traject that is yet to come
     *              a non-repeating traject that is today
     *              a non-repeating traject that was yesterday
     *
     * Because of the way the model works, we'll also need
     *  - Placetime
     *  - Weekdayroute (dayofweek)
     *  - Route (route date boundaries)
     *
     * We won't need to mock dao's, as this method will be entirely model-based
     * As we don't need to use dao's, we'll fill in as little as possible :)
     */
    @Test
    public void nextDayOfTrajectCalculations() {
        WeekdayRoute wdrPlusTwo = new WeekdayRoute(LocalDate.now().plusDays(2).getDayOfWeek().getValue()-1);
        Traject multipleWeeksLeftRepeating = new Traject(new PlaceTime(null, null, wdrPlusTwo), new PlaceTime(null, null, wdrPlusTwo),
                new Route(true, 0, LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusDays(6).plusMonths(1), null, null, null, null), null);
        Assert.assertEquals(LocalDate.now().plusDays(2), trajectService.getNextDayOfTraject(multipleWeeksLeftRepeating));

        WeekdayRoute wdrToday = new WeekdayRoute(LocalDate.now().getDayOfWeek().getValue()-1);
        Traject endsTodayRepeating = new Traject(new PlaceTime(null, null, wdrToday), new PlaceTime(null, null, wdrToday),
                new Route(true, 0, LocalDateTime.now().minusMonths(1), LocalDateTime.now(), null, null, null, null), null);
        Assert.assertEquals(LocalDate.now(), trajectService.getNextDayOfTraject(endsTodayRepeating));

        WeekdayRoute wdrYesterday = new WeekdayRoute(LocalDate.now().minusDays(1).getDayOfWeek().getValue()-1);
        Traject endsYesterdayRepeating = new Traject(new PlaceTime(null, null, wdrYesterday), new PlaceTime(null, null, wdrYesterday),
                new Route(true, 0, LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusDays(1), null, null, null, null), null);
        Assert.assertNull(trajectService.getNextDayOfTraject(endsYesterdayRepeating));

        Traject twoDaysFromNowNonRepeating = new Traject(new PlaceTime(null, null, wdrPlusTwo), new PlaceTime(null, null, wdrPlusTwo),
                new Route(false, 0, LocalDateTime.now().plusDays(2), LocalDateTime.now().plusDays(2), null, null, null, null), null);
        Assert.assertEquals(LocalDate.now().plusDays(2), trajectService.getNextDayOfTraject(twoDaysFromNowNonRepeating));

        Traject todayNonRepeating = new Traject(new PlaceTime(null, null, wdrToday), new PlaceTime(null, null, wdrToday),
                new Route(false, 0, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null), null);
        Assert.assertEquals(LocalDate.now(), trajectService.getNextDayOfTraject(todayNonRepeating));

        Traject yesterdayNonrepeating = new Traject(new PlaceTime(null, null, wdrYesterday), new PlaceTime(null, null, wdrYesterday),
                new Route(false, 0, LocalDateTime.now().minusDays(1), LocalDateTime.now().minusDays(1), null, null, null, null), null);
        Assert.assertNull(trajectService.getNextDayOfTraject(yesterdayNonrepeating));
    }

    //Bad placement, possibly temporary
    @Test
    public void checkComplicatedQueryDoesntThrowError() {
        trajectDao.getAcceptedTrajects(userService.getUser(this.testUsername));
        Assert.assertTrue(true);
    }
}
