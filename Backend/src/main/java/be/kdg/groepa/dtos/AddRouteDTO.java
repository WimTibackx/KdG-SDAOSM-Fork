package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by delltvgateway on 2/17/14.
 */
public class AddRouteDTO {
    public static class PlaceDTO {
        private float lat;
        private float lon; //Long is a reserved keyword :D
        private String name;

        public PlaceDTO(JSONObject data) throws MissingDataException {
            if (!data.has("lat")) { throw new MissingDataException("lat"); }
            this.lat = Float.parseFloat(data.get("lat").toString());

            if (!data.has("long")) { throw new MissingDataException("long"); }
            this.lon = Float.parseFloat(data.get("long").toString());

            if (!data.has("address")) { throw new MissingDataException("address"); }
            this.name = data.getString("address");
        }

        public PlaceDTO(float lat, float lon, String name) {
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PlaceDTO placeDTO = (PlaceDTO) o;

            if (Float.compare(placeDTO.lat, lat) != 0) return false;
            if (Float.compare(placeDTO.lon, lon) != 0) return false;
            if (name != null ? !name.equals(placeDTO.name) : placeDTO.name != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = (lat != +0.0f ? Float.floatToIntBits(lat) : 0);
            result = 31 * result + (lon != +0.0f ? Float.floatToIntBits(lon) : 0);
            result = 31 * result + (name != null ? name.hashCode() : 0);
            return result;
        }
    }

    private int carId;
    private int freeSpots;
    private boolean repeating;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PlaceDTO> places;
    private Map<DayOfWeek,List<LocalTime>> times;

    public AddRouteDTO (JSONObject data) throws MissingDataException {
        this.places = new ArrayList<>();
        this.times = new HashMap<>();

        if (!data.has("car")) { throw new MissingDataException("car"); }
        this.carId = data.getInt("car");

        if (!data.has("freeSpots")) { throw new MissingDataException("freeSpots"); }
        this.freeSpots = data.getInt("freeSpots");

        if (!data.has("repeating")) { throw new MissingDataException("repeating"); }
        this.repeating = data.getBoolean("repeating");

        if (!data.has("startDate")) { throw new MissingDataException("startDate"); }
        this.startDate = LocalDate.parse((String)data.get("startDate"));

        if (!data.has("endDate")) { throw new MissingDataException("endDate"); }
        this.endDate = LocalDate.parse((String)data.get("endDate"));

        if (!data.has("route")) { throw new MissingDataException("route"); }
        // TODO UPDATE THIS WHEN PETER MAKES THE NEW STRUCTURE
        JSONObject route = data.getJSONObject("route");
        if (route.has("start")) {
            this.places.add(new PlaceDTO(route.getJSONObject("start")));
        }
        if (route.has("end")) {
            this.places.add(new PlaceDTO(route.getJSONObject("end")));
        }
        if (this.places.size() <= 1) { throw new MissingDataException("places"); }

        if (!data.has("repeatingDays")) { throw new MissingDataException("repeatingDays"); }
        JSONArray times = data.getJSONArray("repeatingDays");
        //TODO UPDATE THIS WHEN PETER MAKES THE NEW STRUCTURE
        //FOR NOW THIS NEEDS TO HAVE SEVEN THINGIES
        //IMPORTANT: NOT ADDING THE ZERO-ELEMENTS. FRONT-END: ONLY PASS US THE FILLED STUFF!
        for (int i=0; i<times.length(); i++) {
            JSONArray time = times.getJSONArray(i);
            if (time.length()!=places.size()) { throw new MissingDataException("time"); }
            List<LocalTime> localTimes = new ArrayList<>();
            for (int j=0; j<time.length(); j++) {
                if (time.get(j).toString().equals("0")) { break; }
                localTimes.add(LocalTime.parse(time.getString(j)));
            }
            if (localTimes.size()>0) {
                this.times.put(DayOfWeek.of(i+1),localTimes);
            }
        }
        if (this.times.size() == 0) { throw new MissingDataException("time"); }
    }

    public AddRouteDTO(int carId, int freeSpots, boolean repeating, LocalDate startDate, LocalDate endDate, List<PlaceDTO> places, Map<DayOfWeek, List<LocalTime>> times) {
        this.carId = carId;
        this.freeSpots = freeSpots;
        this.repeating = repeating;
        this.startDate = startDate;
        this.endDate = endDate;
        this.places = places;
        this.times = times;
    }

    public int getCarId() {
        return carId;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public boolean isRepeating() {
        return repeating;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public List<PlaceDTO> getPlaces() {
        return places;
    }

    public Map<DayOfWeek, List<LocalTime>> getTimes() {
        return times;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddRouteDTO that = (AddRouteDTO) o;

        if (carId != that.carId) return false;
        if (freeSpots != that.freeSpots) return false;
        if (repeating != that.repeating) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;

        if (places != null ? !places.equals(that.places) : that.places != null) return false;

        if (times != null ? !times.equals(that.times) : that.times != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = carId;
        result = 31 * result + freeSpots;
        result = 31 * result + (repeating ? 1 : 0);
        result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (places != null ? places.hashCode() : 0);
        result = 31 * result + (times != null ? times.hashCode() : 0);
        return result;
    }
}
