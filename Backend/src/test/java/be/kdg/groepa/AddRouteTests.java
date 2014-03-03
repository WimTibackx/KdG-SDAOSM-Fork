package be.kdg.groepa;

import be.kdg.groepa.dtos.AddRouteDTO;
import be.kdg.groepa.dtos.PlaceDTO;
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
    public void dtoCorrectNonrepeating() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": false,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-19\",\"passages\": [\"08:00\",\"08:45\"],\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        PlaceDTO start = new PlaceDTO(51.21523, 4.398739999999975, "Nationalestraat, 2000 Antwerpen, België");
        PlaceDTO end = new PlaceDTO(51.2171198, 4.4008122000000185, "Kammenstraat, 2000 Antwerpen, België");
        List<PlaceDTO> places= Arrays.asList(start, end);

        Map<DayOfWeek,List<LocalTime>> times = new HashMap<>();
        times.put(DayOfWeek.WEDNESDAY, Arrays.asList(LocalTime.of(8,0),LocalTime.of(8,45)));

        AddRouteDTO expected = new AddRouteDTO(1,3,false,LocalDate.of(2014,2,19),LocalDate.of(2014,2,19),places,times);

        AddRouteDTO result = new AddRouteDTO(new JSONObject(input));
        Assert.assertEquals("When importing a correct json thingy, the import should work", expected, result);
    }

    @Test
    public void dtoCorrectRepeating() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Di\": [\"09:30\", \"10:00\"],\"Do\": [\"09:30\", \"10:00\"],\"Vr\": [\"09:30\", \"10:00\"],\"Wo\": [\"12:30\", \"13:00\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        PlaceDTO start = new PlaceDTO(51.21523, 4.398739999999975, "Nationalestraat, 2000 Antwerpen, België");
        PlaceDTO end = new PlaceDTO(51.2171198, 4.4008122000000185, "Kammenstraat, 2000 Antwerpen, België");
        List<PlaceDTO> places= Arrays.asList(start, end);

        Map<DayOfWeek,List<LocalTime>> times = new HashMap<>();
        times.put(DayOfWeek.TUESDAY, Arrays.asList(LocalTime.of(9,30),LocalTime.of(10,0)));
        times.put(DayOfWeek.THURSDAY, Arrays.asList(LocalTime.of(9,30),LocalTime.of(10,0)));
        times.put(DayOfWeek.FRIDAY, Arrays.asList(LocalTime.of(9,30),LocalTime.of(10,0)));
        times.put(DayOfWeek.WEDNESDAY, Arrays.asList(LocalTime.of(12,30),LocalTime.of(13,0)));

        AddRouteDTO expected = new AddRouteDTO(1,3,true,LocalDate.of(2014,2,19),LocalDate.of(2014,2,27),places,times);

        AddRouteDTO result = new AddRouteDTO(new JSONObject(input));
        Assert.assertEquals("When importing a correct json thingy, the import should work", expected, result);
    }

    @Test(expected = MissingDataException.class)
    public void dtoMissingSimpleField() throws MissingDataException {
        final String input = "{\"car\": 1,\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Di\": [\"09:30\", \"10:00\"],\"Do\": [\"09:30\", \"10:00\"],\"Vr\": [\"09:30\", \"10:00\"],\"Wo\": [\"12:30\", \"13:00\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoOnePlace() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Di\": [\"09:30\"],\"Do\": [\"09:30\"],\"Vr\": [\"09:30\"],\"Wo\": [\"12:30\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"}]}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoMissingTime() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Di\": [\"09:30\"],\"Do\": [\"09:30\"],\"Vr\": [\"09:30\"],\"Wo\": [\"12:30\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        new AddRouteDTO(new JSONObject(input));
    }

    @Test(expected = MissingDataException.class)
    public void dtoNoTimes() throws MissingDataException {
        final String input = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        new AddRouteDTO(new JSONObject(input));
    }
}
