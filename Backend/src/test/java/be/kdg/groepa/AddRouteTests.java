package be.kdg.groepa;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by delltvgateway on 2/17/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AddRouteTests {
    //Test DTO
    //  - With correct input
    //  - With simple value missing
    //  - With only one place, but correct structure
    //  - With no times for at least one place
    //  - With no times at all (all 0)
    //  - TODO: REPEATING might still be a problem, in combination with dates
    // IMPORTANT: When testing DTO, invalid data (such as freespots -1 or nonexsistent carId aren't relevant yet)
    //Test Controller (TODO)
    //  - With no input (parseexception)
    //  - Correct input
    //Test Service (TODO)
    //  - With correct input
    //  - With a nonexistent car
    //  - With a car that isn't his/hers
    //  - With a enddate before startdate
    //  - With times in the wrong order

    @Test
    public void dtoCorrect() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-14\",\"endDate\": \"2014-03-04\",\"repeatingDays\": [[0, 0],[\"09:00\", \"10:00\"],[0, 0],[\"09:00\", \"10:00\"],[0, 0],[0, 0],[0, 0]],\"route\": {\"start\": {\"lat\": 51.218011,\"long\": 4.421770000000038,\"address\": \"Koningin Astridplein 27, Antwerpen, België\"}, \"end\": {\"lat\": 51.2181969,\"long\": 4.400798399999985,\"address\": \"Nationalestraat 5, 2000 Antwerpen, België\"}}}";
        AddRouteDTO.PlaceDTO start = new AddRouteDTO.PlaceDTO(51.218011f, 4.421770000000038f, "Koningin Astridplein 27, Antwerpen, België");
        AddRouteDTO.PlaceDTO end = new AddRouteDTO.PlaceDTO(51.2181969f, 4.400798399999985f, "Nationalestraat 5, 2000 Antwerpen, België");
        List<AddRouteDTO.PlaceDTO> places= Arrays.asList(start, end);

        Map<DayOfWeek,List<LocalTime>> times = new HashMap<>();
        times.put(DayOfWeek.TUESDAY, Arrays.asList(LocalTime.of(9,0),LocalTime.of(10,0)));
        times.put(DayOfWeek.THURSDAY, Arrays.asList(LocalTime.of(9,0), LocalTime.of(10,0)));

        AddRouteDTO expected = new AddRouteDTO(1,3,true,LocalDate.of(2014,2,14),LocalDate.of(2014,3,4),places,times);

        AddRouteDTO result = new AddRouteDTO(new JSONObject(input));
        Assert.assertEquals("When importing a correct json thingy, the import should work", expected, result);
    }

    @Test(expected = MissingDataException.class)
    public void dtoMissingSimpleField() throws MissingDataException {
        final String input = "{\"car\": 1,\"repeating\": true,\"startDate\": \"2014-02-14\",\"endDate\": \"2014-03-04\",\"repeatingDays\": [[0, 0],[\"09:00\", \"10:00\"],[0, 0],[\"09:00\", \"10:00\"],[0, 0],[0, 0],[0, 0]],\"route\": {\"start\": {\"lat\": 51.218011,\"long\": 4.421770000000038,\"address\": \"Koningin Astridplein 27, Antwerpen, België\"}, \"end\": {\"lat\": 51.2181969,\"long\": 4.400798399999985,\"address\": \"Nationalestraat 5, 2000 Antwerpen, België\"}}}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoOnePlace() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-14\",\"endDate\": \"2014-03-04\",\"repeatingDays\": [[0, 0],[\"09:00\", \"10:00\"],[0, 0],[\"09:00\", \"10:00\"],[0, 0],[0, 0],[0, 0]],\"route\": {\"start\": {\"lat\": 51.218011,\"long\": 4.421770000000038,\"address\": \"Koningin Astridplein 27, Antwerpen, België\"}}}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoMissingTime() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-14\",\"endDate\": \"2014-03-04\",\"repeatingDays\": [[0],[\"09:00\"],[0],[\"09:00\"],[0],[0],[0]],\"route\": {\"start\": {\"lat\": 51.218011,\"long\": 4.421770000000038,\"address\": \"Koningin Astridplein 27, Antwerpen, België\"}, \"end\": {\"lat\": 51.2181969,\"long\": 4.400798399999985,\"address\": \"Nationalestraat 5, 2000 Antwerpen, België\"}}}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoNoTimes() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-14\",\"endDate\": \"2014-03-04\",\"repeatingDays\": [[0, 0],[0, 0],[0, 0],[0, 0],[0, 0],[0, 0],[0, 0]],\"route\": {\"start\": {\"lat\": 51.218011,\"long\": 4.421770000000038,\"address\": \"Koningin Astridplein 27, Antwerpen, België\"}, \"end\": {\"lat\": 51.2181969,\"long\": 4.400798399999985,\"address\": \"Nationalestraat 5, 2000 Antwerpen, België\"}}}";
        new AddRouteDTO(new JSONObject(input));
    }
}
