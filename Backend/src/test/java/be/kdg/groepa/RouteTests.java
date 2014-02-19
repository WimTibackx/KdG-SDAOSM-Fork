package be.kdg.groepa;

import be.kdg.groepa.model.*;
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

    @Test
    public void testRouteModel()
    {
        Car c = new Car("Lamborghini", "Aventador", 18.3, Car.FuelType.DIESEL);
        User u = new User("PJ", User.Gender.MALE, false, "Giovanni69", LocalDate.of(1993, 10, 20), "gio@degruyter.com", c);
        Route r = new Route(false, 69, LocalDateTime.now(), LocalDateTime.now(), u, c);

        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userService.addCarToUser("gio@degruyter.com", c);

        c.addRoute(r);
        u.addRoute(r);
        routeService.addRoute(r);
        System.out.println("test");
        assertTrue("Add route failed", true);
    }
    /*
    @Test
    public void testRepeatingRoute()
    {
        Car c = new Car("Peugeot", "Partner", 7.2);
        User u = new User("Tim", User.Gender.FEMALE, true, "TimIsThierrysPapa123", LocalDate.of(1993, 04, 12), "timv@nroe.yen", c);
        try {
            userService.addUser(u);
        } catch (Exception e) {
            e.printStackTrace();
        }
        userService.addCarToUser("timv@nroe.yen", c);
        Route r = new Route(true, 69, LocalDateTime.now(), LocalDateTime.now(), u, c);
        Place place = new Place("Kieldrecht", 231.988796454f, 132.56684684f);
        Place place2 = new Place("Zwijndrecht Krijgsbaan", 431.98987133664f, 411.9889459684f);
        Place place3 = new Place("Carpoolparking Vrasene", 564.98731478966f, 342.97136455781f);
        Place place4 = new Place("Melsele Dijk", 154.987132654f, 189.9874561981f);

        routeService.addPlace(place);
        routeService.addPlace(place2);
        routeService.addPlace(place3);
        routeService.addPlace(place4);

        PlaceTime pt = new PlaceTime(LocalTime.of(8, 0), place);
        PlaceTime pt2 = new PlaceTime(LocalTime.of(8, 10), place2);
        PlaceTime pt3 = new PlaceTime(LocalTime.of(8, 20), place3);
        PlaceTime pt4 = new PlaceTime(LocalTime.of(8, 25), place4);

        PlaceTime pt5 = new PlaceTime(LocalTime.of(7, 0), place);
        PlaceTime pt6 = new PlaceTime(LocalTime.of(7, 10), place2);
        PlaceTime pt7 = new PlaceTime(LocalTime.of(7, 20), place3);
        PlaceTime pt8 = new PlaceTime(LocalTime.of(7, 25), place4);

        routeService.addPlaceTime(pt);
        routeService.addPlaceTime(pt2);
        routeService.addPlaceTime(pt3);
        routeService.addPlaceTime(pt4);
        routeService.addPlaceTime(pt5);
        routeService.addPlaceTime(pt6);
        routeService.addPlaceTime(pt7);
        routeService.addPlaceTime(pt8);

        WeekdayRoute wr = new WeekdayRoute(r, 0);
        WeekdayRoute wr1 = new WeekdayRoute(r, 1);
        WeekdayRoute wr2 = new WeekdayRoute(r, 2);
        WeekdayRoute wr3 = new WeekdayRoute(r, 3);
        WeekdayRoute wr4 = new WeekdayRoute(r, 4);
        WeekdayRoute wr5 = new WeekdayRoute(r, 5);
        WeekdayRoute wr6 = new WeekdayRoute(r, 6);

        wr.addPlaceTime(pt);
        wr.addPlaceTime(pt2);
        wr.addPlaceTime(pt3);
        wr.addPlaceTime(pt4);

        wr1.addPlaceTime(pt5);
        wr1.addPlaceTime(pt6);
        wr1.addPlaceTime(pt7);
        wr1.addPlaceTime(pt8);

        wr2.addPlaceTime(pt5);
        wr2.addPlaceTime(pt6);
        wr2.addPlaceTime(pt7);
        wr2.addPlaceTime(pt8);

        wr3.addPlaceTime(pt5);
        wr3.addPlaceTime(pt6);
        wr3.addPlaceTime(pt7);
        wr3.addPlaceTime(pt8);

        wr4.addPlaceTime(pt5);
        wr4.addPlaceTime(pt6);
        wr4.addPlaceTime(pt7);
        wr4.addPlaceTime(pt8);

        wr5.addPlaceTime(pt5);
        wr5.addPlaceTime(pt6);
        wr5.addPlaceTime(pt7);
        wr5.addPlaceTime(pt8);

        wr6.addPlaceTime(pt5);
        wr6.addPlaceTime(pt6);
        wr6.addPlaceTime(pt7);
        wr6.addPlaceTime(pt8);

        routeService.addWeekdayRoute(wr);
        routeService.addWeekdayRoute(wr1);
        routeService.addWeekdayRoute(wr2);
        routeService.addWeekdayRoute(wr3);
        routeService.addWeekdayRoute(wr4);
        routeService.addWeekdayRoute(wr5);
        routeService.addWeekdayRoute(wr6);

        r.addWeekdayRoute(wr);
        r.addWeekdayRoute(wr1);
        r.addWeekdayRoute(wr2);
        r.addWeekdayRoute(wr3);
        r.addWeekdayRoute(wr4);
        r.addWeekdayRoute(wr5);
        r.addWeekdayRoute(wr6);

        routeService.addRoute(r);
    }   */

}
