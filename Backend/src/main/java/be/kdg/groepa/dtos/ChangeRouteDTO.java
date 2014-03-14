package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by delltvgateway on 3/12/14
 * { routeId: 3, changes: [
 { type: "deletePlaceTime", placeTimeId: 1 },
 { type: "changeTime", times: ["09:00", "09:05", "09:30"], weekdayroute: 1 },
 { type: "addWeekdayRoute", day: 1, times: ["09:00", "0", "09:20"] },
 { type: "deleteWeekdayRoute", id: 6 },
 { type: "addPlaceTime", lat: X, lng: Y, address: Z }
 ] }.
 */
public class ChangeRouteDTO {
    private int routeId;
    private LocalDate startDate;
    private List<Change> changes;

    public ChangeRouteDTO() {
        this.changes = new ArrayList<>();
    }

    public ChangeRouteDTO(JSONObject jsonData) throws MissingDataException {
        if (!jsonData.has("routeId")) throw new MissingDataException("routeId");
        this.routeId = jsonData.getInt("routeId");
        if (!jsonData.has("startDate")) throw new MissingDataException("startDate");
        this.startDate = LocalDate.parse(jsonData.getString("startDate"));
        if (!jsonData.has("changes")) throw new MissingDataException("changes");
        this.changes = new ArrayList<>();
        JSONArray changesArr = jsonData.getJSONArray("changes");
        for (int i=0; i<changesArr.length(); i++) {
            JSONObject changeJson = changesArr.getJSONObject(i);
            if (!changeJson.has("type")) throw new MissingDataException("type");
            switch (changeJson.getString("type")) {
                case "deletePlaceTime":
                    this.changes.add(new DeletePlaceTime(changeJson));
                    break;
                case "changeTime":
                    this.changes.add(new ChangeTime(changeJson));
                    break;
                case "addWeekdayRoute":
                    this.changes.add(new AddWeekdayRoute(changeJson));
                    break;
                case "deleteWeekdayRoute":
                    this.changes.add(new DeleteWeekdayRoute(changeJson));
                    break;
                case "addPlaceTime":
                    this.changes.add(new AddPlaceTime(changeJson));
                    break;
                case "changeCar":
                    this.changes.add(new ChangeCar(changeJson));
                    break;
            }
        }
    }

    public int getRouteId() {
        return routeId;
    }

    public List<Change> getChanges() {
        return changes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void addChange(Change change) {
        this.changes.add(change);
    }

    public static class DeletePlaceTime implements Change {
        private int placeTimeId;
        private int weekdayRouteId;
        private List<PlaceTimeSpecifier> times;

        public DeletePlaceTime() {
            this.times = new ArrayList<>();
        }

        public DeletePlaceTime(JSONObject json) throws MissingDataException {
            if (!json.has("placeTimeId")) throw new MissingDataException("placeTimeId");
            this.placeTimeId = json.getInt("placeTimeId");
            if (json.has("weekdayroute")) {
                this.weekdayRouteId = json.getInt("weekdayroute");
            }
            if (!json.has("times")) throw new MissingDataException("times");
            this.times = new ArrayList<>();
            JSONArray timesJson = json.getJSONArray("times");
            for (int i=0; i<timesJson.length(); i++) {
                this.times.add(new PlaceTimeSpecifier(timesJson.getJSONObject(i)));
            }
        }

        public int getPlaceTimeId() {
            return placeTimeId;
        }

        public int getWeekdayRouteId() {
            return weekdayRouteId;
        }

        public List<PlaceTimeSpecifier> getTimes() {
            return times;
        }

        public void setPlaceTimeId(int placeTimeId) {
            this.placeTimeId = placeTimeId;
        }

        public void setWeekdayRouteId(int weekdayRouteId) {
            this.weekdayRouteId = weekdayRouteId;
        }

        public void addTime(PlaceTimeSpecifier pts) {
            this.times.add(pts);
        }
    }

    public static class ChangeTime implements Change {
        private int weekdayRouteId;
        private List<PlaceTimeSpecifier> times;

        public ChangeTime() {
            this.times = new ArrayList<>();
        }

        public ChangeTime(JSONObject json) throws MissingDataException {
            if (json.has("weekdayroute")) {
                this.weekdayRouteId = json.getInt("weekdayroute");
            }
            if (!json.has("times")) throw new MissingDataException("times");
            this.times = new ArrayList<>();
            JSONArray timesJson = json.getJSONArray("times");
            for (int i=0; i<timesJson.length(); i++) {
                this.times.add(new PlaceTimeSpecifier(timesJson.getJSONObject(i)));
            }
        }

        public int getWeekdayRouteId() {
            return weekdayRouteId;
        }

        public List<PlaceTimeSpecifier> getTimes() {
            return times;
        }

        public void setWeekdayRouteId(int weekdayRouteId) {
            this.weekdayRouteId = weekdayRouteId;
        }

        public void addTime(PlaceTimeSpecifier pts) {
            this.times.add(pts);
        }
    }

    public static class AddWeekdayRoute implements Change {
        public int day;
        private List<PlaceTimeSpecifier> times;

        public AddWeekdayRoute() {
            this.times = new ArrayList<>();
        }

        public AddWeekdayRoute(JSONObject json) throws MissingDataException {
            if (!json.has("day")) throw new MissingDataException("day");
            this.day = json.getInt("day");
            if (!json.has("times")) throw new MissingDataException("times");
            this.times = new ArrayList<>();
            JSONArray timesJson = json.getJSONArray("times");
            for (int i=0; i<timesJson.length(); i++) {
                this.times.add(new PlaceTimeSpecifier(timesJson.getJSONObject(i)));
            }
        }

        public int getDay() {
            return day;
        }

        public List<PlaceTimeSpecifier> getTimes() {
            return times;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public void addTime(PlaceTimeSpecifier pts) {
            this.times.add(pts);
        }
    }

    public static class DeleteWeekdayRoute implements Change {
        private int weekdayRouteId;

        public DeleteWeekdayRoute() {}

        public DeleteWeekdayRoute(JSONObject json) throws MissingDataException {
            if (!json.has("weekdayRouteId")) throw new MissingDataException("weekdayRouteId");
            this.weekdayRouteId = json.getInt("weekdayRouteId");
        }

        public int getWeekdayRouteId() {
            return weekdayRouteId;
        }

        public void setWeekdayRouteId(int weekdayRouteId) {
            this.weekdayRouteId = weekdayRouteId;
        }
    }

    public static class AddPlaceTime implements Change {
        private double lat;
        private double lng;
        private String address;
        private int weekdayRouteId;
        private List<PlaceTimeSpecifier> placeTimeSpecifiers;

        public AddPlaceTime() {
            this.placeTimeSpecifiers = new ArrayList<>();
        }

        public AddPlaceTime(JSONObject json) throws MissingDataException {
            if (!json.has("lat")) throw new MissingDataException("lat");
            this.lat = json.getDouble("lat");
            if (!json.has("lng")) throw new MissingDataException("lng");
            this.lng = json.getDouble("lng");
            if (!json.has("address")) throw new MissingDataException("address");
            this.address = json.getString("address");
            if (json.has("weekdayroute")) {
                this.weekdayRouteId = json.getInt("weekdayroute");
            }
            if (!json.has("times")) throw new MissingDataException("times");
            this.placeTimeSpecifiers = new ArrayList<>();
            JSONArray timesJson = json.getJSONArray("times");
            for (int i=0; i<timesJson.length(); i++) {
                this.placeTimeSpecifiers.add(new PlaceTimeSpecifier(timesJson.getJSONObject(i)));
            }
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public String getAddress() {
            return address;
        }

        public int getWeekdayRouteId() {
            return weekdayRouteId;
        }

        public List<PlaceTimeSpecifier> getPlaceTimeSpecifiers() {
            return placeTimeSpecifiers;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setWeekdayRouteId(int weekdayRouteId) {
            this.weekdayRouteId = weekdayRouteId;
        }

        public void addPlaceTimeSpecifier(PlaceTimeSpecifier pts) {
            this.placeTimeSpecifiers.add(pts);
        }
    }

    public static class ChangeCar implements Change {
        private int carId;
        private int capacity;

        public ChangeCar() {}

        public ChangeCar(JSONObject json) throws MissingDataException {
            if (!json.has("carId")) throw new MissingDataException("carId");
            this.carId = json.getInt("carId");
            if (!json.has("capacity")) throw new MissingDataException("capacity");
            this.capacity = json.getInt("capacity");
        }

        public int getCarId() {
            return carId;
        }

        public int getCapacity() {
            return capacity;
        }

        public void setCarId(int carId) {
            this.carId = carId;
        }

        public void setCapacity(int capacity) {
            this.capacity = capacity;
        }
    }

    public interface Change { }

    public static class PlaceTimeSpecifier {
        private double lat;
        private double lng;
        private LocalTime time;
        private String address;

        public PlaceTimeSpecifier() {}

        public PlaceTimeSpecifier(double lat, double lng, LocalTime time) {
            this.lat = lat;
            this.lng = lng;
            this.time = time;
        }

        public PlaceTimeSpecifier(double lat, double lng, LocalTime time, String address) {
            this(lat, lng, time);
            this.address = address;
        }

        public PlaceTimeSpecifier(JSONObject json) throws MissingDataException {
            if (!json.has("lat")) throw new MissingDataException("lat");
            this.lat = json.getInt("lat");
            if (!json.has("lng")) throw new MissingDataException("lng");
            this.lng = json.getInt("lng");
            if (!json.has("time")) throw new MissingDataException("time");
            this.time = LocalTime.parse(json.getString("time"));
            if (json.has("address")) {
                this.address = json.getString("address");
            }
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }

        public LocalTime getTime() {
            return time;
        }

        public String getAddress() {
            return address;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public void setTime(LocalTime time) {
            this.time = time;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}

