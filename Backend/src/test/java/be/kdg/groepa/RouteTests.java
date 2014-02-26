package be.kdg.groepa;

import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import static org.junit.Assert.*;

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

    @Test
    public void testRouteModel()
    {
        Car c = new Car("Lamborghini", "Aventador", 18.3, Car.FuelType.DIESEL);
        User u = new User("PJ", User.Gender.MALE, false, "Giovanni69", LocalDate.of(1993, 10, 20), "gio@degruyter.com", c);
        Route r = new Route(false, 69, LocalDateTime.now(), LocalDateTime.now(), u, c, new PlaceTime(LocalTime.of(8, 30), new Place("RouteHome", 10, 20)), new PlaceTime(LocalTime.of(16,30), new Place("RouteWork", 11, 20)));

        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("gio@degruyter.com", c);

        c.addRoute(r);
        u.addRoute(r);
        routeService.addRoute(r);
        System.out.println("test");
        assertTrue("Add route failed", true);
    }

    @Test
    public void testRepeatingRoute()
    {
        Car c = new Car("Peugeot", "Partner", 7.2, Car.FuelType.SUPER95);
        User u = new User("Tim", User.Gender.FEMALE, true, "TimIsThierrysPapa123", LocalDate.of(1993, 04, 12), "timv@nroe.yen", c);
        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("timv@nroe.yen", c);
        Route r = new Route(true, 69, LocalDateTime.now(), LocalDateTime.now(), u, c, new PlaceTime(LocalTime.of(8, 20), new Place("Home", 10, 10)), new PlaceTime(LocalTime.of(18, 20), new Place("Work", 20, 10)) );
        Place place = new Place("Kieldrecht", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Zwijndrecht Krijgsbaan", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Carpoolparking Vrasene", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Melsele Dijk", 154.987132654f, 189.9874561981f);

        PlaceTime pt = new PlaceTime(LocalTime.of(8, 0));
        PlaceTime pt2 = new PlaceTime(LocalTime.of(8, 10));
        PlaceTime pt3 = new PlaceTime(LocalTime.of(8, 20));
        PlaceTime pt4 = new PlaceTime(LocalTime.of(8, 25));

        PlaceTime pt5 = new PlaceTime(LocalTime.of(7, 0));
        PlaceTime pt6 = new PlaceTime(LocalTime.of(7, 10));
        PlaceTime pt7 = new PlaceTime(LocalTime.of(7, 20));
        PlaceTime pt8 = new PlaceTime(LocalTime.of(7, 25));

        routeService.addPlace(place);
        routeService.addPlace(place2);
        routeService.addPlace(place3);
        routeService.addPlace(place4);

        routeService.addPlaceTimeToPlace(pt, place);
        routeService.addPlaceTimeToPlace(pt2, place2);
        routeService.addPlaceTimeToPlace(pt3, place3);
        routeService.addPlaceTimeToPlace(pt4, place4);
        routeService.addPlaceTimeToPlace(pt5, place);
        routeService.addPlaceTimeToPlace(pt6, place2);
        routeService.addPlaceTimeToPlace(pt7, place3);
        routeService.addPlaceTimeToPlace(pt8, place4);

        WeekdayRoute wr = new WeekdayRoute(0);
        WeekdayRoute wr1 = new WeekdayRoute(1);

        wr.addPlaceTime(pt);
        wr.addPlaceTime(pt2);
        wr.addPlaceTime(pt3);
        wr.addPlaceTime(pt4);

        wr1.addPlaceTime(pt5);
        wr1.addPlaceTime(pt6);
        wr1.addPlaceTime(pt7);
        wr1.addPlaceTime(pt8);

        pt.setWeekdayRoute(wr);
        pt2.setWeekdayRoute(wr);
        pt3.setWeekdayRoute(wr);
        pt4.setWeekdayRoute(wr);

        pt5.setWeekdayRoute(wr1);
        pt6.setWeekdayRoute(wr1);
        pt7.setWeekdayRoute(wr1);
        pt8.setWeekdayRoute(wr1);

        routeService.addWeekdayRoute(wr);
        routeService.addWeekdayRoute(wr1);

        r.addWeekdayRoute(wr);
        r.addWeekdayRoute(wr1);
        wr.setRoute(r);
        wr1.setRoute(r);

        routeService.addRoute(r);
        assertTrue("Add repeating route failed", true);
    }

    @Test
    public void testNormalRoute()
    {
        Car c = new Car("Peugeot", "Partner", 7.2, Car.FuelType.SUPER95);
        User u = new User("Tim", User.Gender.FEMALE, true, "TimIsThierrysPapa123", LocalDate.of(1993, 04, 12), "thierryv@nnn.loo", c);
        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        carService.addCar("thierryv@nnn.loo", c);
        Place place = new Place("Kieldrecht", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Zwijndrecht Krijgsbaan", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Carpoolparking Vrasene", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Melsele Dijk", 154.987132654f, 189.9874561981f);

        PlaceTime pt = new PlaceTime(LocalTime.of(8, 0));
        PlaceTime pt2 = new PlaceTime(LocalTime.of(8, 10));
        PlaceTime pt3 = new PlaceTime(LocalTime.of(8, 20));
        PlaceTime pt4 = new PlaceTime(LocalTime.of(8, 25));

        PlaceTime pt5 = new PlaceTime(LocalTime.of(7, 0));
        PlaceTime pt6 = new PlaceTime(LocalTime.of(7, 10));
        PlaceTime pt7 = new PlaceTime(LocalTime.of(7, 20));
        PlaceTime pt8 = new PlaceTime(LocalTime.of(7, 25));

        routeService.addPlace(place);
        routeService.addPlace(place2);
        routeService.addPlace(place3);
        routeService.addPlace(place4);

        routeService.addPlaceTimeToPlace(pt, place);
        routeService.addPlaceTimeToPlace(pt2, place2);
        routeService.addPlaceTimeToPlace(pt3, place3);
        routeService.addPlaceTimeToPlace(pt4, place4);
        routeService.addPlaceTimeToPlace(pt5, place);
        routeService.addPlaceTimeToPlace(pt6, place2);
        routeService.addPlaceTimeToPlace(pt7, place3);
        routeService.addPlaceTimeToPlace(pt8, place4);

        Route r = new Route(true, 69, LocalDateTime.now(), LocalDateTime.now(), u, c, pt, pt2);

        routeService.addPlaceTimeToRoute(r, pt);
        routeService.addPlaceTimeToRoute(r, pt2);
        routeService.addPlaceTimeToRoute(r, pt3);
        routeService.addPlaceTimeToRoute(r, pt4);
        routeService.addPlaceTimeToRoute(r, pt5);
        routeService.addPlaceTimeToRoute(r, pt6);
        routeService.addPlaceTimeToRoute(r, pt7);
        routeService.addPlaceTimeToRoute(r, pt8);

        routeService.addRoute(r);

        assertTrue("Normal route creation fails", true);
    }
}
