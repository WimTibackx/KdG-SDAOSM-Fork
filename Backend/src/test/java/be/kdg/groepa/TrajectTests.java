package be.kdg.groepa;

import be.kdg.groepa.exceptions.PlaceTimesInWrongSequenceException;
import be.kdg.groepa.exceptions.PlaceTimesOfDifferentRoutesException;
import be.kdg.groepa.exceptions.PlaceTimesOfDifferentWeekdayRoutesException;
import be.kdg.groepa.exceptions.TrajectNotEnoughCapacityException;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import org.easymock.EasyMock;
import org.junit.After;
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
    private TrajectDao trajectDaoMock;

    @Autowired
    private RouteDao routeDao;
    private RouteDao routeDaoMock;

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

    /*@Test
    public void addTrajectToRoute(){
        PlaceTime pointA = new PlaceTime(LocalTime.of(8, 25), new Place("UserHome", 10, 20));
        PlaceTime pointC = new PlaceTime(LocalTime.of(12,30), new Place("UserWork", 11, 20));
        trajectService.addTraject(new Traject(pointA, pointC, routeA, user));
        assertEquals("Incorrect amount of PlaceTimes in route", 6, user.getRoutes().get(0).getAllPlaceTimes().size());  // actually expected 4, but route now gets an extra Traject by default (so 2 extra PlaceTimes)
    }*/

    /*@Test
    public void removeTrajectFromRoute(){
        Traject traj = new Traject(new PlaceTime(LocalTime.of(10, 25), new Place("UserHome", 10, 20)), new PlaceTime(LocalTime.of(15,30), new Place("UserWork", 11, 20)), routeB, user2);
        trajectService.addTraject(traj);
        assertEquals("Not enough PlaceTimes in route", 6, user2.getRoutes().get(0).getAllPlaceTimes().size()); // Same story as "addTrajectToRoute"
        trajectService.removeTrajectFromRoute(routeB, traj);
        assertEquals("Wrong amount of PlaceTimes in route", 4, user2.getRoutes().get(0).getAllPlaceTimes().size()); // Same story ^.
    }*/

    /*@Test
    public void addPlaceTimeAfterExistingRoutePoint(){
        PlaceTime newPlaceTime2 = new PlaceTime(LocalTime.of(9, 30), new Place("OtherUserHome", 9, 18));
        trajectService.insertNewRoutePoint(routeC.getPlaceTimes().get(0), newPlaceTime2);
        assertEquals("Incorrect PlaceTime at given position of route", newPlaceTime2, routeC.getAllPlaceTimes().get(1));
    }*/

    /*@Test
    public void addTrajectToRouteWithPoints(){
        PlaceTime newPlaceTime = new PlaceTime(LocalTime.of(9, 30), new Place("OtherUserHome", 9, 18));
        PlaceTime newPlaceTime3 = new PlaceTime(LocalTime.of(17,45), new Place("OtherUserWork", 9, 20));
        trajectService.addNewTrajectToRoute(routeD.getPlaceTimes().get(0), newPlaceTime, routeD.getPlaceTimes().get(1), newPlaceTime3, user4);
        assertEquals("New PlaceTime 1 is not at the right position", routeD.getPlaceTimes().get(1), newPlaceTime);
        assertEquals("New PlaceTime 2 is not at the rigth position", routeD.getPlaceTimes().get(3), newPlaceTime3);
    }*/

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

    //We can't make this an actual before, as not all tests in this class (can) use the mock
    public void initTrajectDaoMock() {
        this.trajectDaoMock = EasyMock.createMock(TrajectDao.class);
        this.routeDaoMock = EasyMock.createMock(RouteDao.class);
        this.trajectService.setTrajectDao(this.trajectDaoMock);
        this.routeService.setTrajectDao(this.trajectDaoMock);
        this.routeService.setRouteDao(this.routeDaoMock);
    }

    //Other than the fact that we CAN make this an after, because it does no harm
    //  We also have to, because some of the tests using the mock might end in an exception,
    //  and thus can't be expected to call this method.
    @After
    public void resetTrajectDaoMock() {
        this.trajectService.setTrajectDao(this.trajectDao);
        this.routeService.setTrajectDao(this.trajectDao);
        this.routeService.setRouteDao(this.routeDao);
    }

    @Test(expected=PlaceTimesOfDifferentRoutesException.class)
    public void requestTrajectDifferentRoutes() throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException {
        Route routeA = new Route(false, 3, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null);
        routeA.setId(1);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), routeA);
        placeTimeAY.setPlacetimeId(2);
        routeA.addPlaceTime(placeTimeAX);
        routeA.addPlaceTime(placeTimeAY);
        Route routeB = new Route(false, 3, LocalDateTime.now(), LocalDateTime.now(), null, null, null, null);
        routeB.setId(2);
        PlaceTime placeTimeBX = new PlaceTime(LocalTime.now(),new Place("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium", 51.208078, 4.442945), routeB);
        placeTimeBX.setPlacetimeId(3);
        PlaceTime placeTimeBY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Zwartzustersvest 47-50, 2800 Mechelen, Belgium", 51.029592, 4.488086), routeB);
        placeTimeBY.setPlacetimeId(4);
        routeB.addPlaceTime(placeTimeBX);
        routeB.addPlaceTime(placeTimeBY);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeBX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(null, routeA, 1, 3);
        Assert.fail();
    }

    @Test(expected=PlaceTimesOfDifferentWeekdayRoutesException.class)
    public void requestTrajectDifferentWdrs() throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException {
        Route routeA = new Route(true, 3, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, null, null);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        WeekdayRoute weekdayRouteB = new WeekdayRoute(routeA, LocalDateTime.now().plusDays(1).getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        weekdayRouteB.setWeekdayrouteId(2);
        routeA.addWeekdayRoute(weekdayRouteA);
        routeA.addWeekdayRoute(weekdayRouteB);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        weekdayRouteA.addPlaceTime(placeTimeAX);
        weekdayRouteA.addPlaceTime(placeTimeAY);
        PlaceTime placeTimeBX = new PlaceTime(LocalTime.now(),new Place("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium", 51.208078, 4.442945), weekdayRouteB, routeA);
        placeTimeBX.setPlacetimeId(3);
        PlaceTime placeTimeBY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Zwartzustersvest 47-50, 2800 Mechelen, Belgium", 51.029592, 4.488086), weekdayRouteB, routeA);
        placeTimeBY.setPlacetimeId(4);
        weekdayRouteB.addPlaceTime(placeTimeBX);
        weekdayRouteB.addPlaceTime(placeTimeBY);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeBX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(null, routeA, 1, 3);
        Assert.fail();
    }

    @Test(expected=PlaceTimesInWrongSequenceException.class)
    public void requestTrajectPickupAfterDropoff() throws PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentRoutesException, TrajectNotEnoughCapacityException {
        Route routeA = new Route(true, 3, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, null, null);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        weekdayRouteA.addPlaceTime(placeTimeAX);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        weekdayRouteA.addPlaceTime(placeTimeAY);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(2)).andReturn(placeTimeAY);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(null, routeA, 2, 1);
        Assert.fail();
    }

    @Test(expected=PlaceTimesInWrongSequenceException.class)
    public void requestTrajectPickupEqualsDropoff() throws PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentRoutesException, TrajectNotEnoughCapacityException {
        Route routeA = new Route(true, 3, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, null, null);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        weekdayRouteA.addPlaceTime(placeTimeAX);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        weekdayRouteA.addPlaceTime(placeTimeAY);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(null, routeA, 1, 1);
        Assert.fail();
    }

    @Test(expected=TrajectNotEnoughCapacityException.class)
    public void requestTrajectNoCapacityOnOnePart() throws TrajectNotEnoughCapacityException, PlaceTimesOfDifferentRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentWeekdayRoutesException {
        Route routeA = new Route(true, 1, LocalDateTime.now(), LocalDateTime.now().plusDays(1), null, null, null, null);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        weekdayRouteA.addPlaceTime(placeTimeAX);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(1), new Place("N177 100-122, 2850 Boom, Belgium", 51.090334, 4.365175), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        weekdayRouteA.addPlaceTime(placeTimeAY);
        PlaceTime placeTimeAZ = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAZ.setPlacetimeId(3);
        weekdayRouteA.addPlaceTime(placeTimeAZ);
        Traject trajectA = new Traject(placeTimeAX, placeTimeAY, routeA, null);
        routeA.addTraject(trajectA);
        weekdayRouteA.addTraject(trajectA);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeAZ);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(null, routeA, 1, 3);
        Assert.fail();
    }

    //TODO Hier een auto en een user nodig
    @Test
    public void requestTrajectSuccess() throws TrajectNotEnoughCapacityException, PlaceTimesOfDifferentRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentWeekdayRoutesException {
        User userA = new User("Shauni Ordelman",User.Gender.FEMALE, false, "Password1", LocalDate.of(1980,5,24),"shauni.ordelman@traj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userA.addCar(carA);
        carA.setUser(userA);
        User userB = new User("Benjamin Verdin", User.Gender.MALE, false, "Password1", LocalDate.of(1980,5,24), "benjamin.verdin@traj.example.com");
        Route routeA = new Route(true, 2, LocalDateTime.now(), LocalDateTime.now().plusDays(1), userA, carA, null, null);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        weekdayRouteA.addPlaceTime(placeTimeAX);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(1), new Place("N177 100-122, 2850 Boom, Belgium", 51.090334, 4.365175), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        weekdayRouteA.addPlaceTime(placeTimeAY);
        PlaceTime placeTimeAZ = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAZ.setPlacetimeId(3);
        weekdayRouteA.addPlaceTime(placeTimeAZ);
        Traject trajectA = new Traject(placeTimeAX, placeTimeAZ, routeA, userA);
        routeA.addTraject(trajectA);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(2)).andReturn(placeTimeAY);
        this.trajectDaoMock.addTraject(new Traject(placeTimeAX, placeTimeAZ, routeA, userB));
        EasyMock.expectLastCall();
        EasyMock.replay(this.routeDaoMock);

        Assert.assertTrue("Adding this traject should work",this.trajectService.requestTraject(userB, routeA, 1, 2));
    }
}
