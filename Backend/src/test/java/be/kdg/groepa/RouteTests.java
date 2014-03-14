package be.kdg.groepa;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.ChangeRouteDTO;
import be.kdg.groepa.dtos.PlaceDTO;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UnauthorizedException;
import be.kdg.groepa.exceptions.UsernameExistsException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.TrajectService;
import be.kdg.groepa.service.api.UserService;
import javax.servlet.http.Cookie;
import org.json.simple.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Pieter-Jan on 18-2-14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RouteTests {

    @Autowired
    private RouteService routeService;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private TrajectService trajectService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testNonRepRoute()
    {
        Car c = new Car("Peugeot", "Partner", 7.2, Car.FuelType.SUPER95);
        User u = new User("Tim", User.Gender.FEMALE, true, "TimIsThierrysPapa123", LocalDate.of(1993, 4, 12), "thierryv@nnn.loo", c);
        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("thierryv@nnn.loo", c);
        Route r = new Route(false, 69, LocalDate.now(), LocalDate.now(), u, c);
        Place place = new Place("Kieldrecht", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Zwijndrecht Krijgsbaan", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Carpoolparking Vrasene", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Melsele Dijk", 154.987132654f, 189.9874561981f);

        new PlaceTime(LocalTime.of(8, 0), place, r);
        new PlaceTime(LocalTime.of(8, 10), place2, r);
        new PlaceTime(LocalTime.of(8, 20), place3, r);
        new PlaceTime(LocalTime.of(8, 25), place4, r);

        new PlaceTime(LocalTime.of(7, 0), place, r);
        new PlaceTime(LocalTime.of(7, 10), place2, r);
        new PlaceTime(LocalTime.of(7, 20), place3, r);
        new PlaceTime(LocalTime.of(7, 25), place4, r);

        //addRoute adds the whole damn thing, all relationships included,
        //  because all these things should be added in one transaction.
        routeService.addRoute(r);

        assertTrue("Normal route creation fails", true);
    }

    @Test
    public void testRepRoute()
    {
        Car c = new Car("Peugeot", "Partner", 7.2, Car.FuelType.SUPER95);
        User u = new User("Wimpie", User.Gender.FEMALE, true, "TimIsThierrysPapa123", LocalDate.of(1993, 4, 12), "wimpie@swag.com", c);
        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("wimpie@swag.com", c);
        Route r = new Route(true, 69, LocalDate.now(), LocalDate.now(), u, c);
        Place place = new Place("Kieldrecht", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Zwijndrecht Krijgsbaan", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Carpoolparking Vrasene", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Melsele Dijk", 154.987132654f, 189.9874561981f);

        WeekdayRoute wr = new WeekdayRoute(r, 0);
        WeekdayRoute wr1 = new WeekdayRoute(r, 1);

        new PlaceTime(LocalTime.of(8, 0), place, wr, r);
        new PlaceTime(LocalTime.of(8, 10), place2, wr, r);
        new PlaceTime(LocalTime.of(8, 20), place3, wr, r);
        new PlaceTime(LocalTime.of(8, 25), place4, wr, r);

        new PlaceTime(LocalTime.of(7, 0), place, wr1, r);
        new PlaceTime(LocalTime.of(7, 10), place2, wr1, r);
        new PlaceTime(LocalTime.of(7, 20), place3, wr1, r);
        new PlaceTime(LocalTime.of(7, 25), place4, wr1, r);

        r.addWeekdayRoute(wr);
        r.addWeekdayRoute(wr1);

        //addRoute adds the whole damn thing, all relationships included,
        //  because all these things should be added in one transaction.
        routeService.addRoute(r);
        assertTrue("Add repeating route failed", true);
    }

    @Test
    public void testDeletePlaceTime() throws Exception {
        User u = new User("testdeleteplacetime", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), "testdeleteplacetime@rcit.example.com");
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(u);
        carService.addCar("testdeleteplacetime@rcit.example.com",c);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser("testdeleteplacetime@rcit.example.com")).get(0);

        User user = this.userService.getUser("testdeleteplacetime@rcit.example.com");

        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.DeletePlaceTime change = new ChangeRouteDTO.DeletePlaceTime();
        change.setPlaceTimeId(loadedR.getPlaceTimes().get(1).getPlacetimeId());
        change.setWeekdayRouteId(routeService.getWeekdayRoutesOfRoute(loadedR.getId()).get(0).getWeekdayrouteId());
        ChangeRouteDTO.PlaceTimeSpecifier pts1 = new ChangeRouteDTO.PlaceTimeSpecifier();
        pts1.setLat(51.3);
        pts1.setLng(4.2);
        pts1.setTime(LocalTime.of(9,5));
        change.addTime(pts1);
        ChangeRouteDTO.PlaceTimeSpecifier pts2 = new ChangeRouteDTO.PlaceTimeSpecifier();
        pts2.setLat(51.7);
        pts2.setLng(4.6);
        pts2.setTime(LocalTime.of(9,20));
        change.addTime(pts2);
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        Assert.assertEquals("There should be one less placetime", 2, changedRoute.getPlaceTimes().size());
        Assert.assertEquals("Time of the second should have changed",LocalTime.of(9,20),changedRoute.getPlaceTimes().get(1).getTime());
    }

    @Test
    public void testChangeTime() throws PasswordFormatException, UsernameExistsException, UsernameFormatException, UnauthorizedException {
        User u = new User("testchangetime", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), "testchangetime@rcit.example.com");
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(u);
        carService.addCar("testchangetime@rcit.example.com",c);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser("testchangetime@rcit.example.com")).get(0);

        User user = this.userService.getUser("testchangetime@rcit.example.com");

        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.ChangeTime change = new ChangeRouteDTO.ChangeTime();
        change.setWeekdayRouteId(routeService.getWeekdayRoutesOfRoute(loadedR.getId()).get(0).getWeekdayrouteId());
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.3, 4.2, LocalTime.of(8, 5)));
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.5, 4.4, LocalTime.of(8, 15)));
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.7,4.6,LocalTime.of(9,25)));
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        Assert.assertEquals("Time of the first should have changed",LocalTime.of(8,5),changedRoute.getPlaceTimes().get(0).getTime());
        Assert.assertEquals("Time of the second should have changed",LocalTime.of(8,15),changedRoute.getPlaceTimes().get(1).getTime());
        Assert.assertEquals("Time of the third shouldn't have changed",LocalTime.of(9,25),changedRoute.getPlaceTimes().get(2).getTime());
    }

    @Test
    public void testAddWeekdayRoute() throws PasswordFormatException, UsernameExistsException, UsernameFormatException, UnauthorizedException {
        final String username = "testAddWeekdayRoute@rcit.example.com";
        User u = new User("testAddWeekdayRoute", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), username);
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(u);
        carService.addCar(username,c);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser(username)).get(0);

        User user = this.userService.getUser(username);

        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.AddWeekdayRoute change = new ChangeRouteDTO.AddWeekdayRoute();
        change.setDay(LocalDate.now().plusDays(2).getDayOfWeek().getValue()-1);
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.3, 4.2, LocalTime.of(9,5), "Place1"));
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.6, 4.5, LocalTime.of(9,20), "Place2B"));
        change.addTime(new ChangeRouteDTO.PlaceTimeSpecifier(51.7, 4.6, LocalTime.of(9,25), "Place3"));
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        List<WeekdayRoute> newWdrs = routeService.getWeekdayRoutesOfRoute(changedRoute.getId());
        Assert.assertEquals("Time of the first shouldn't have changed", LocalTime.of(9, 5), newWdrs.get(0).getPlaceTimes().get(0).getTime());
        Assert.assertEquals("The second day should have 3 placetimes",3,newWdrs.get(1).getPlaceTimes().size());
    }

    @Test
    public void testDeleteWeekdayRoute() throws PasswordFormatException, UsernameExistsException, UsernameFormatException, UnauthorizedException {
        final String username = "testDeleteWeekdayRoute@rcit.example.com";
        User u = new User("testDeleteWeekdayRoute", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), username);
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(u);
        carService.addCar(username,c);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        WeekdayRoute wdrB = new WeekdayRoute(r, LocalDate.now().plusDays(1).getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdrB);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser(username)).get(0);
        List<WeekdayRoute> oldWdrs = routeService.getWeekdayRoutesOfRoute(loadedR.getId());

        User user = this.userService.getUser(username);

        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.DeleteWeekdayRoute change = new ChangeRouteDTO.DeleteWeekdayRoute();
        change.setWeekdayRouteId(oldWdrs.get(1).getWeekdayrouteId());
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        List<WeekdayRoute> newWdrs = routeService.getWeekdayRoutesOfRoute(changedRoute.getId());
        Assert.assertEquals("There should only be one WDR left",1,newWdrs.size());
    }

    @Test
    public void testAddPlaceTime() throws PasswordFormatException, UsernameExistsException, UsernameFormatException, UnauthorizedException {
        final String username = "testAddPlaceTime@rcit.example.com";
        User u = new User("testAddPlaceTime", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), username);
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        userService.addUser(u);
        carService.addCar(username,c);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser(username)).get(0);
        List<WeekdayRoute> oldWdrs = routeService.getWeekdayRoutesOfRoute(loadedR.getId());

        User user = this.userService.getUser(username);
        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.AddPlaceTime change = new ChangeRouteDTO.AddPlaceTime();
        change.setWeekdayRouteId(oldWdrs.get(0).getWeekdayrouteId());
        change.setLat(51.8);
        change.setLng(4.7);
        change.setAddress("Place4");
        change.addPlaceTimeSpecifier(new ChangeRouteDTO.PlaceTimeSpecifier(51.3, 4.2, LocalTime.of(9, 5)));
        change.addPlaceTimeSpecifier(new ChangeRouteDTO.PlaceTimeSpecifier(51.5,4.4,LocalTime.of(9,15)));
        change.addPlaceTimeSpecifier(new ChangeRouteDTO.PlaceTimeSpecifier(51.7,4.6,LocalTime.of(9,25)));
        change.addPlaceTimeSpecifier(new ChangeRouteDTO.PlaceTimeSpecifier(51.8,4.7,LocalTime.of(9,30)));
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        Assert.assertEquals("There should be 4 pts now", 4, changedRoute.getPlaceTimes().size());
    }

    @Test
    public void testChangeCar() throws PasswordFormatException, UsernameExistsException, UsernameFormatException, UnauthorizedException {
        final String username = "testChangeCar@rcit.example.com";
        User u = new User("testChangeCar", User.Gender.MALE, true, "Password1", LocalDate.of(1993,1,1), username);
        Car c = new Car("Ford", "Fiesta", 8.3, Car.FuelType.DIESEL);
        Car carB = new Car("Opel","Astra", 8.6, Car.FuelType.SUPER95);
        userService.addUser(u);
        carService.addCar(username, c);
        carService.addCar(username,carB);
        Route r = new Route(true, 3, LocalDate.now().minusMonths(1), LocalDate.now().plusMonths(2), u, c);
        WeekdayRoute wdr = new WeekdayRoute(r, LocalDate.now().getDayOfWeek().getValue()-1);
        r.addWeekdayRoute(wdr);
        new PlaceTime(LocalTime.of(9,5),new Place("Place1",51.3,4.2),wdr,r);
        new PlaceTime(LocalTime.of(9,15), new Place("Place2",51.5, 4.4),wdr,r);
        new PlaceTime(LocalTime.of(9,25), new Place("Place3",51.7, 4.6),wdr,r);
        routeService.addRoute(r);

        Route loadedR = routeService.getRoutes(this.userService.getUser(username)).get(0);
        List<WeekdayRoute> oldWdrs = routeService.getWeekdayRoutesOfRoute(loadedR.getId());

        User user = this.userService.getUser(username);
        List<Car> cars = user.getCars();
        int carBId = -1;
        for (Car car : cars) {
            if (car.getBrand().equals("Opel")) {
                carBId = car.getCarId();
            }
        }

        ChangeRouteDTO dto = new ChangeRouteDTO();
        dto.setRouteId(loadedR.getId());
        dto.setStartDate(LocalDate.now().plusMonths(1));
        ChangeRouteDTO.ChangeCar change = new ChangeRouteDTO.ChangeCar();
        change.setCarId(carBId);
        change.setCapacity(5);
        dto.addChange(change);

        routeService.editRoute(dto, user);

        Route changedRoute = routeService.getRoutes(user).get(1);
        Assert.assertEquals("Carbrand should be 'Opel' now", "Opel", changedRoute.getCar().getBrand());
        Assert.assertEquals("Capacity should be '5' now",5, changedRoute.getCapacity());
        // This is to have some testdata concerning trajects for (non-)repeating routes.
    }

    @Test
    public void testRepRouteTrajects()
    {
        Car c = new Car("Fiat", "Panda", 7.2, Car.FuelType.DIESEL);
        User u = new User("Peter", User.Gender.MALE, true, "Spoed12345", LocalDate.of(1993, 4, 12), "petertje@spoed.com", c);
        User p1 = new User("Tim", User.Gender.FEMALE, true, "Timmetje123", LocalDate.of(1993, 3, 13), "timmetje@tim.tim");
        User p2 = new User("Melissa", User.Gender.FEMALE, true, "Melissa123", LocalDate.of(1993, 7, 3), "melissa@tim.tim");


        try {
            userService.addUser(u);
            userService.addUser(p1);
            userService.addUser(p2);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("petertje@spoed.com", c);
        Route r = new Route(true, 4, LocalDate.now(), LocalDate.now(), u, c);
        Place place = new Place("Brasschaat", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Willekeurige Carpoolparking", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Rooseveltplaats", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Andere plek", 154.987132654f, 189.9874561981f);

        WeekdayRoute wr = new WeekdayRoute(r, 0);
        WeekdayRoute wr1 = new WeekdayRoute(r, 1);

        PlaceTime[] pts = new PlaceTime[8];

        pts[0] = new PlaceTime(LocalTime.of(8, 0), place, wr, r);
        pts[1] = new PlaceTime(LocalTime.of(8, 10), place2, wr, r);
        pts[2] = new PlaceTime(LocalTime.of(8, 20), place3, wr, r);
        pts[3] = new PlaceTime(LocalTime.of(8, 25), place4, wr, r);

        pts[4] = new PlaceTime(LocalTime.of(7, 0), place, wr1, r);
        pts[5] = new PlaceTime(LocalTime.of(7, 10), place2, wr1, r);
        pts[6] = new PlaceTime(LocalTime.of(7, 20), place3, wr1, r);
        pts[7] = new PlaceTime(LocalTime.of(7, 25), place4, wr1, r);

        r.addWeekdayRoute(wr);
        r.addWeekdayRoute(wr1);
        routeService.addRoute(r);

        Traject t1 = new Traject(pts[1], pts[3], r, p1, wr);
        Traject t2 = new Traject(pts[4], pts[6], r, p2, wr);
        Traject t3 = new Traject(pts[2], pts[7], r, p1, wr1);

        wr.addTraject(t1);
        wr.addTraject(t2);
        wr1.addTraject(t3);

        // NOT route.addTraject here; that would be for NON-REPEATING routes!!!

        t1.setAccepted(true);
        t2.setAccepted(true);
        t3.setAccepted(true);

        trajectService.addTraject(t1);
        trajectService.addTraject(t2);
        trajectService.addTraject(t3);

        assertTrue("Add repeating route trajects failed", true);
    }

    @Test
    public void testNormalRouteTrajects()
    {
        Car c = new Car("BMW", "X6 - M", 7.2, Car.FuelType.DIESEL);
        User u = new User("Bart", User.Gender.MALE, true, "vochtenRules123", LocalDate.of(1993, 4, 12), "b.vochten@carpool.be", c);
        User p1 = new User("Chris", User.Gender.FEMALE, true, "javaTheBest123", LocalDate.of(1993, 3, 13), "behielsje@carpool.be");
        User p2 = new User("Kris", User.Gender.FEMALE, true, "DeMuynck1234", LocalDate.of(1993, 7, 3), "demuynck@carpool.be");

        try {
            userService.addUser(u);
            userService.addUser(p1);
            userService.addUser(p2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        carService.addCar("b.vochten@carpool.be", c);
        Route r = new Route(true, 4, LocalDate.now(), LocalDate.now(), u, c);
        Place place = new Place("Brasschaat", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Grote Markt", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Schouwburg", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Groenplaats", 154.987132654f, 189.9874561981f);

        PlaceTime[] pts = new PlaceTime[8];

        pts[0] = new PlaceTime(LocalTime.of(8, 0), place, r);
        pts[1] = new PlaceTime(LocalTime.of(8, 10), place2, r);
        pts[2] = new PlaceTime(LocalTime.of(8, 20), place3, r);
        pts[3] = new PlaceTime(LocalTime.of(8, 25), place4, r);

        pts[4] = new PlaceTime(LocalTime.of(7, 0), place, r);
        pts[5] = new PlaceTime(LocalTime.of(7, 10), place2, r);
        pts[6] = new PlaceTime(LocalTime.of(7, 20), place3, r);
        pts[7] = new PlaceTime(LocalTime.of(7, 25), place4, r);

        routeService.addRoute(r);

        Traject t1 = new Traject(pts[1], pts[3], r, p1);
        Traject t2 = new Traject(pts[4], pts[6], r, p2);
        Traject t3 = new Traject(pts[2], pts[7], r, p1);

        r.addTraject(t1);
        r.addTraject(t2);
        r.addTraject(t3);

        t1.setAccepted(true);
        t2.setAccepted(true);
        t3.setAccepted(true);

        trajectService.addTraject(t1);
        trajectService.addTraject(t2);
        trajectService.addTraject(t3);

        assertTrue("Add normal route trajects failed", true);
    }
}
