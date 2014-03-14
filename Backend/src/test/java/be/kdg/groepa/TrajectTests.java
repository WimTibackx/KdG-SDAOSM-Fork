package be.kdg.groepa;

import be.kdg.groepa.exceptions.*;
import be.kdg.groepa.model.*;
import be.kdg.groepa.persistence.api.RouteDao;
import be.kdg.groepa.persistence.api.TextMessageDao;
import be.kdg.groepa.persistence.api.TrajectDao;
import be.kdg.groepa.service.api.*;
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

import java.util.List;

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
    private TextMessageService textMessageService;

    @Autowired
    private TrajectDao trajectDao;
    private TrajectDao trajectDaoMock;

    @Autowired
    private RouteDao routeDao;
    private RouteDao routeDaoMock;

    @Autowired
    private TextMessageDao textMessageDao;
    private TextMessageDao textMessageDaoMock;

    @Autowired
    private CarService carService;

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
    //@Before
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
            routeA = new Route(true, 4, LocalDate.of(2014, 10, 3), LocalDate.of(2014, 10, 3), user, car);
            new PlaceTime(LocalTime.of(8, 30), new Place("RouteAHome", 10, 20), routeA);
            new PlaceTime(LocalTime.of(16,30), new Place("RouteAWork", 11, 20), routeA);
            routeB = new Route(true, 4, LocalDate.of(2014, 10, 3), LocalDate.of(2014, 10, 3), user2, car);
            new PlaceTime(LocalTime.of(8, 30), new Place("RouteBHome", 10, 20), routeB);
            new PlaceTime(LocalTime.of(16,30), new Place("RouteBWork", 11, 20), routeB);
            routeC = new Route(true, 4, LocalDate.of(2014, 10, 3), LocalDate.of(2014, 10, 3), user3, car);
            new PlaceTime(LocalTime.of(8, 30), new Place("RouteCHome", 10, 20), routeC);
            new PlaceTime(LocalTime.of(16,30), new Place("RouteCWork", 11, 20), routeC);
            routeD = new Route(true, 4, LocalDate.of(2014, 10, 3), LocalDate.of(2014, 10, 3), user4, car);
            new PlaceTime(LocalTime.of(8, 30), new Place("RouteCHome", 10, 20), routeD);
            new PlaceTime(LocalTime.of(16,30), new Place("RouteCWork", 11, 20), routeD);
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
        User userA = new User("Foo",User.Gender.FEMALE,false,"Foo1",LocalDate.now(),"foo@example.com");
        User userB = new User("Bar",User.Gender.FEMALE,false,"Bar1",LocalDate.now(),"bar@example.com");
        Car carA = new Car("Ford","Fiesta",8.3,Car.FuelType.DIESEL);
        Place placeA = new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710);
        Place placeB = new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118);
        LocalTime timeA = LocalTime.of(7, 0);
        LocalTime timeB = LocalTime.of(8, 0);

        Route routeA = new Route(true, 1, LocalDate.now().minusMonths(1), LocalDate.now().plusDays(6).plusMonths(1), userA, carA);
        WeekdayRoute wdrPlusTwo = new WeekdayRoute(routeA, LocalDate.now().plusDays(2).getDayOfWeek().getValue()-1);
        PlaceTime ptAA = new PlaceTime(timeA, placeA, wdrPlusTwo, routeA);
        PlaceTime ptAB = new PlaceTime(timeB, placeB, wdrPlusTwo, routeA);
        Traject multipleWeeksLeftRepeating = new Traject(ptAA, ptAB, routeA, userB);
        Assert.assertEquals(LocalDate.now().plusDays(2), trajectService.getNextDayOfTraject(multipleWeeksLeftRepeating));

        Route routeB = new Route(true, 1, LocalDate.now().minusMonths(1), LocalDate.now(), userA, carA);
        WeekdayRoute wdrToday = new WeekdayRoute(routeB, LocalDate.now().getDayOfWeek().getValue()-1);
        final PlaceTime ptBA = new PlaceTime(timeA, placeA, wdrToday);
        final PlaceTime ptBB = new PlaceTime(timeB, placeB, wdrToday);
        Traject endsTodayRepeating = new Traject(ptBA, ptBB, routeB, userB);
        Assert.assertEquals(LocalDate.now(), trajectService.getNextDayOfTraject(endsTodayRepeating));

        Route routeC = new Route(true, 1, LocalDate.now().minusMonths(1), LocalDate.now().minusDays(1), userA, carA);
        WeekdayRoute wdrYesterday = new WeekdayRoute(routeC, LocalDate.now().minusDays(1).getDayOfWeek().getValue()-1);
        final PlaceTime ptCA = new PlaceTime(timeA, placeA, wdrYesterday);
        final PlaceTime ptCB = new PlaceTime(timeB, placeB, wdrYesterday);
        Traject endsYesterdayRepeating = new Traject(ptCA, ptCB, routeC, userB);
        Assert.assertNull(trajectService.getNextDayOfTraject(endsYesterdayRepeating));

        Route routeD = new Route(false, 1, LocalDate.now().plusDays(2), LocalDate.now().plusDays(2), userA, carA);
        WeekdayRoute wdrPlusTwoD = new WeekdayRoute(routeD, LocalDate.now().plusDays(2).getDayOfWeek().getValue()-1);
        final PlaceTime ptDA = new PlaceTime(timeA, placeA, wdrPlusTwoD);
        final PlaceTime ptDB = new PlaceTime(timeB, placeB, wdrPlusTwoD);
        Traject twoDaysFromNowNonRepeating = new Traject(ptDA, ptDB, routeD, userB);
        Assert.assertEquals(LocalDate.now().plusDays(2), trajectService.getNextDayOfTraject(twoDaysFromNowNonRepeating));

        Route routeE = new Route(false, 1, LocalDate.now(), LocalDate.now(), userA, carA);
        WeekdayRoute wdrTodayE = new WeekdayRoute(routeE, LocalDate.now().getDayOfWeek().getValue()-1);
        final PlaceTime ptEA = new PlaceTime(timeA, placeA, wdrTodayE);
        final PlaceTime ptEB = new PlaceTime(timeB, placeB, wdrTodayE);
        Traject todayNonRepeating = new Traject(ptEA, ptEB, routeE, userB);
        Assert.assertEquals(LocalDate.now(), trajectService.getNextDayOfTraject(todayNonRepeating));

        Route routeF = new Route(false, 1, LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), userA, carA);
        WeekdayRoute wdrYesterdayF = new WeekdayRoute(routeF, LocalDate.now().minusDays(1).getDayOfWeek().getValue()-1);
        final PlaceTime ptFA = new PlaceTime(timeA, placeA, wdrYesterdayF);
        final PlaceTime ptFB = new PlaceTime(timeB, placeB, wdrYesterdayF);
        Traject yesterdayNonrepeating = new Traject(ptFA, ptFB, routeF, userB);
        Assert.assertNull(trajectService.getNextDayOfTraject(yesterdayNonrepeating));
    }

    //We can't make this an actual before, as not all tests in this class (can) use the mock
    public void initTrajectDaoMock() {
        this.trajectDaoMock = EasyMock.createMock(TrajectDao.class);
        this.routeDaoMock = EasyMock.createMock(RouteDao.class);
        this.textMessageDaoMock = EasyMock.createMock(TextMessageDao.class);
        this.trajectService.setTrajectDao(this.trajectDaoMock);
        this.routeService.setTrajectDao(this.trajectDaoMock);
        this.routeService.setRouteDao(this.routeDaoMock);
        this.textMessageService.setTextMessageDao(this.textMessageDaoMock);
    }

    //Other than the fact that we CAN make this an after, because it does no harm
    //  We also have to, because some of the tests using the mock might end in an exception,
    //  and thus can't be expected to call this method.
    @After
    public void resetTrajectDaoMock() {
        this.trajectService.setTrajectDao(this.trajectDao);
        this.routeService.setTrajectDao(this.trajectDao);
        this.routeService.setRouteDao(this.routeDao);
        this.textMessageService.setTextMessageDao(this.textMessageDao);
    }

    @Test(expected=PlaceTimesOfDifferentRoutesException.class)
    public void requestTrajectDifferentRoutes() throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException {
        User userA = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"diffRoutesA@reqTraj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        User userB = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"diffRoutesB@reqTraj.example.com");
        Route routeA = new Route(false, 3, LocalDate.now(), LocalDate.now(), userA, carA);
        routeA.setId(1);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), routeA);
        placeTimeAY.setPlacetimeId(2);
        Route routeB = new Route(false, 3, LocalDate.now(), LocalDate.now(), userA, carA);
        routeB.setId(2);
        PlaceTime placeTimeBX = new PlaceTime(LocalTime.now(),new Place("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium", 51.208078, 4.442945), routeB);
        placeTimeBX.setPlacetimeId(3);
        PlaceTime placeTimeBY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Zwartzustersvest 47-50, 2800 Mechelen, Belgium", 51.029592, 4.488086), routeB);
        placeTimeBY.setPlacetimeId(4);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeBX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(userB, routeA, 1, 3);
        Assert.fail();
    }

    @Test(expected=PlaceTimesOfDifferentWeekdayRoutesException.class)
    public void requestTrajectDifferentWdrs() throws PlaceTimesOfDifferentRoutesException, PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, TrajectNotEnoughCapacityException {
        User userA = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"diffWdrsA@reqTraj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        User userB = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"diffWdrsB@reqTraj.example.com");
        Route routeA = new Route(true, 3, LocalDate.now(), LocalDate.now().plusDays(1), userA, carA);
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
        PlaceTime placeTimeBX = new PlaceTime(LocalTime.now(),new Place("Luitenant Lippenslaan 55, 2140 Borgerhout, Belgium", 51.208078, 4.442945), weekdayRouteB, routeA);
        placeTimeBX.setPlacetimeId(3);
        PlaceTime placeTimeBY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Zwartzustersvest 47-50, 2800 Mechelen, Belgium", 51.029592, 4.488086), weekdayRouteB, routeA);
        placeTimeBY.setPlacetimeId(4);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeBX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(userB, routeA, 1, 3);
        Assert.fail();
    }

    @Test(expected=PlaceTimesInWrongSequenceException.class)
    public void requestTrajectPickupAfterDropoff() throws PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentRoutesException, TrajectNotEnoughCapacityException {
        User userA = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"pickupAftDropoffA@reqTraj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        User userB = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"pickupAftDropoffB@reqTraj.example.com");
        Route routeA = new Route(true, 3, LocalDate.now(), LocalDate.now().plusDays(1), userA, carA);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(2)).andReturn(placeTimeAY);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(userB, routeA, 2, 1);
        Assert.fail();
    }

    @Test(expected=PlaceTimesInWrongSequenceException.class)
    public void requestTrajectPickupEqualsDropoff() throws PlaceTimesOfDifferentWeekdayRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentRoutesException, TrajectNotEnoughCapacityException {
        User userA = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"pickupEqDropoffA@reqTraj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        User userB = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"pickupEqDropoffB@reqTraj.example.com");
        Route routeA = new Route(true, 3, LocalDate.now(), LocalDate.now().plusDays(1), userA, carA);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(userB, routeA, 1, 1);
        Assert.fail();
    }

    @Test(expected=TrajectNotEnoughCapacityException.class)
    public void requestTrajectNoCapacityOnOnePart() throws TrajectNotEnoughCapacityException, PlaceTimesOfDifferentRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentWeekdayRoutesException {
        User userA = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"notEnoughCapA@reqTraj.example.com");
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        User userB = new User("Foobar",User.Gender.FEMALE,false,"Password1",LocalDate.of(1993,1,1),"notEnoughCapB@reqTraj.example.com");
        Route routeA = new Route(true, 1, LocalDate.now(), LocalDate.now().plusDays(1), userA, carA);
        routeA.setId(1);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        weekdayRouteA.setWeekdayrouteId(1);
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        placeTimeAX.setPlacetimeId(1);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(1), new Place("N177 100-122, 2850 Boom, Belgium", 51.090334, 4.365175), weekdayRouteA, routeA);
        placeTimeAY.setPlacetimeId(2);
        PlaceTime placeTimeAZ = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        placeTimeAZ.setPlacetimeId(3);
        Traject trajectA = new Traject(placeTimeAX, placeTimeAY, routeA, null);
        routeA.addTraject(trajectA);
        weekdayRouteA.addTraject(trajectA);

        this.initTrajectDaoMock();
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(1)).andReturn(placeTimeAX);
        EasyMock.expect(this.routeDaoMock.getPlaceTimeById(3)).andReturn(placeTimeAZ);
        EasyMock.replay(this.routeDaoMock);

        this.trajectService.requestTraject(userB, routeA, 1, 3);
        Assert.fail();
    }

    @Test
    public void requestTrajectSuccess() throws TrajectNotEnoughCapacityException, PlaceTimesOfDifferentRoutesException, PlaceTimesInWrongSequenceException, PlaceTimesOfDifferentWeekdayRoutesException, PasswordFormatException, UsernameExistsException, UsernameFormatException {
        User userA = new User("Foobar",User.Gender.FEMALE, false, "Password1", LocalDate.of(1980,5,24),"successA@reqTraj.example.com");
        //TextMessageDao MOCKEN
        Car carA = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(userA);
        carService.addCar("successA@reqTraj.example.com",carA);
        User userB = new User("Foobar", User.Gender.MALE, false, "Password1", LocalDate.of(1980,5,24), "successB@reqTraj.example.com");
        userService.addUser(userB);
        Route routeA = new Route(true, 2, LocalDate.now(), LocalDate.now().plusDays(1), userA, carA);
        WeekdayRoute weekdayRouteA = new WeekdayRoute(routeA, LocalDateTime.now().getDayOfWeek().getValue());
        routeA.addWeekdayRoute(weekdayRouteA);
        PlaceTime placeTimeAX = new PlaceTime(LocalTime.now(),new Place("N14 163-193, 2320 Hoogstraten, Belgium", 51.400110, 4.760710), weekdayRouteA, routeA);
        PlaceTime placeTimeAY = new PlaceTime(LocalTime.now().plusHours(1), new Place("N177 100-122, 2850 Boom, Belgium", 51.090334, 4.365175), weekdayRouteA, routeA);
        PlaceTime placeTimeAZ = new PlaceTime(LocalTime.now().plusHours(2), new Place("Willebroekkaai 35, 1000 Brussel, Belgium", 50.862557, 4.352118), weekdayRouteA, routeA);
        routeService.addRoute(routeA);

        List<PlaceTime> placeTimes = routeService.getRoutes(userService.getUser("successA@reqTraj.example.com")).get(0).getPlaceTimes();
        int idPtAX = placeTimes.get(0).getPlacetimeId();
        int idPtAY = placeTimes.get(1).getPlacetimeId();
        Assert.assertTrue("Adding this traject should work",this.trajectService.requestTraject(userService.getUser("successB@reqTraj.example.com"), routeService.getRoutes(userService.getUser("successA@reqTraj.example.com")).get(0), idPtAX, idPtAY));
    }
}
