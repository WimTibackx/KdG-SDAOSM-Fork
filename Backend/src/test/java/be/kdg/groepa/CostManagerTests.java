package be.kdg.groepa;

import be.kdg.groepa.helpers.CostManager;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.Place;
import be.kdg.groepa.model.PlaceTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.LocalTime;
import static org.junit.Assert.*;

/**
 * Created by Pieter-Jan on 14-3-14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class CostManagerTests {

    @Test
    public void testCostManager()
    {
        Place place = new Place("Kieldrecht", 21.21412, 12.21351);
        Place place1 = new Place("Vrasene", 21.532423, 14.343413);
        Car c = new Car("Lamborghini", "Aventador", 18.800, Car.FuelType.DIESEL);
        PlaceTime pt1 = new PlaceTime(LocalTime.of(8, 0), place);
        PlaceTime pt2 = new PlaceTime(LocalTime.of(10, 0), place1);

        // 8.1 km = distance
        double temp = CostManager.calculateCost(pt1, pt2, c);
        assertEquals("Test cost manager fails - costs do not match", 1.5228, temp, 0.09999);
    }
}
