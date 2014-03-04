package be.kdg.groepa.android.dto;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class PlaceDto {
    private double lat;
    private double lon;
    private String name;

    public PlaceDto(JSONObject data) {
        try {
            this.lat = data.getDouble("lat");
            this.lon = data.getDouble("lon");
            this.name = data.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("PlaceDto", e.getMessage());
        }
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }
}
