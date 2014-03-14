package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.model.User;
import org.json.JSONObject;
import org.threeten.bp.LocalTime;

/**
 * Created by Pieter-Jan on 11-3-14.
 */
public class RideDTO {

    private double startLat;
    private double startLon;
    private double endLat;
    private double endLon;
    private User.Gender g;
    private boolean smoker;
    private double radius;
    private LocalTime dep;
    private int timeDiff;

    public RideDTO (JSONObject data) throws MissingDataException {
        if (!data.getJSONObject("start").has("lat")) throw new MissingDataException("start.lat");
        if (!data.getJSONObject("start").has("lng")) throw new MissingDataException("start.lng");
        if (!data.getJSONObject("end").has("lat")) throw new MissingDataException("end.lat");
        if (!data.getJSONObject("end").has("lng")) throw new MissingDataException("end.lng");
        if (!data.has("radius")) throw new MissingDataException("radius");
        if (!data.has("radiusHours")) throw new MissingDataException("radiusHours");
        if (!data.has("radiusMinutes")) throw new MissingDataException("radiusMinutes");
        if (!data.has("hours")) throw new MissingDataException("hours");
        if (!data.has("minutes")) throw new MissingDataException("minutes");
        if (!data.has("gender")) throw new MissingDataException("gender");
        if (!data.has("smoker")) throw new MissingDataException("smoker");

        // All data is present, fill it in
        this.startLat = data.getJSONObject("start").getDouble("lat");
        this.startLon = data.getJSONObject("start").getDouble("lng");
        this.endLat = data.getJSONObject("end").getDouble("lat");
        this.endLon = data.getJSONObject("end").getDouble("lng");
        if (data.getString("gender").equals("female")) this.g = User.Gender.FEMALE;
        else this.g = User.Gender.MALE;
        this.timeDiff = (data.getInt("radiusHours") * 60 *60) + (data.getInt("radiusMinutes") * 60);
        this.smoker = data.getBoolean("smoker");
        this.radius = data.getDouble("radius");
        this.dep = LocalTime.of(data.getInt("hours"), data.getInt("minutes"));
    }

    public double getStartLat() {
        return startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public double getEndLat() {
        return endLat;
    }

    public double getEndLon() {
        return endLon;
    }

    public User.Gender getG() {
        return g;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public double getRadius() {
        return radius;
    }

    public LocalTime getDep() {
        return dep;
    }

    public int getTimeDiff() {
        return timeDiff;
    }
}
