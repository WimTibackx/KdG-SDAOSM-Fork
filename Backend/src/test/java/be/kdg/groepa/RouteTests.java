package be.kdg.groepa;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.ChangeRouteDTO;
import be.kdg.groepa.dtos.PlaceDTO;
import be.kdg.groepa.model.*;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.RouteService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;

import java.util.List;

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
    }
}
