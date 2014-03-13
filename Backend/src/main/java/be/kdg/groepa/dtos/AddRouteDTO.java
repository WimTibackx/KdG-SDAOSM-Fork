package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

import java.util.*;

/**
 * Created by delltvgateway on 2/17/14.
 */
public class AddRouteDTO {
    private int carId;
    private int freeSpots;
    private boolean repeating;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<PlaceDTO> places;
    private Map<DayOfWeek,List<LocalTime>> times;

    public AddRouteDTO() {
        this.places = new ArrayList<>();
        this.times = new HashMap<>();
    }

    public AddRouteDTO (JSONObject data) throws MissingDataException {
        this();

        if (!data.has("car")) { throw new MissingDataException("car"); }
        this.carId = data.getInt("car");

        if (!data.has("freeSpots")) { throw new MissingDataException("freeSpots"); }
        this.freeSpots = data.getInt("freeSpots");

        if (!data.has("repeating")) { throw new MissingDataException("repeating"); }
        this.repeating = data.getBoolean("repeating");

        if (!data.has("startDate")) { throw new MissingDataException("startDate"); }
        this.startDate = LocalDate.parse((String)data.get("startDate"));

        if (!data.has("route")) { throw new MissingDataException("route"); }
        JSONArray places = data.getJSONArray("route");
        for (int i=0; i<places.length(); i++) {
            this.places.add(new PlaceDTO(places.getJSONObject(i)));
        }
        if (this.places.size() <= 1) { throw new MissingDataException("places"); }

        if (this.repeating) {
            if (!data.has("endDate")) { throw new MissingDataException("endDate"); }
            this.endDate = LocalDate.parse((String)data.get("endDate"));

            if (!data.has("passages")) { throw new MissingDataException("passages"); }
            JSONObject passages = data.getJSONObject("passages");

            Iterator daysIt = passages.keys();
            while (daysIt.hasNext()) {
                DayOfWeek dow = null;
                String key = (String)daysIt.next();
                switch (key) {
                    case "Mo": dow=DayOfWeek.MONDAY; break;
                    case "Tu": dow=DayOfWeek.TUESDAY; break;
                    case "We": dow=DayOfWeek.WEDNESDAY; break;
                    case "Th": dow=DayOfWeek.THURSDAY; break;
                    case "Fr": dow=DayOfWeek.FRIDAY; break;
                    case "Sa": dow=DayOfWeek.SATURDAY; break;
                    case "Su": dow=DayOfWeek.SUNDAY; break;
                }

                List<LocalTime> passageTimes = new ArrayList<>();
                JSONArray jsonTimes = passages.getJSONArray(key);
                if (jsonTimes.length()!=this.places.size()) { throw new MissingDataException("time"); }
                for (int j=0; j<jsonTimes.length(); j++) {
                    passageTimes.add(LocalTime.parse(jsonTimes.getString(j)));
                }
                if (passageTimes.size()>0) {
                    this.times.put(dow,passageTimes);
                }
            }
            if (this.times.size() == 0) { throw new MissingDataException("time"); }
        } else {
            this.endDate = this.startDate;
            if (!data.has("passages")) { throw new MissingDataException("passages"); }
            JSONArray jsonTimes = data.getJSONArray("passages");
            List<LocalTime> passageTimes = new ArrayList<>();
            if (jsonTimes.length()!=this.places.size()) { throw new MissingDataException("time"); }
            for (int j=0; j<jsonTimes.length(); j++) {
                if (jsonTimes.get(j).toString().equals("0")) { break; }
                passageTimes.add(LocalTime.parse(jsonTimes.getString(j)));
            }
            if (passageTimes.size()>0) {
                this.times.put(this.startDate.getDayOfWeek(),passageTimes);
            }
        }
    }

    public AddRouteDTO(int carId, int freeSpots, boolean repeating, LocalDate startDate, LocalDate endDate, List<PlaceDTO> places, Map<DayOfWeek, List<LocalTime>> times) {
        this();
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

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public void setRepeating(boolean repeating) {
        this.repeating = repeating;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void addPlaceDTO(PlaceDTO p) {
        this.places.add(p);
    }

    public void addTime(DayOfWeek d, LocalTime... times) {
        this.times.put(d,Arrays.asList(times));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AddRouteDTO that = (AddRouteDTO) o;

        if (carId != that.carId) return false;
        if (freeSpots != that.freeSpots) return false;
        if (repeating != that.repeating) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (places != null ? !places.equals(that.places) : that.places != null) return false;
        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
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
