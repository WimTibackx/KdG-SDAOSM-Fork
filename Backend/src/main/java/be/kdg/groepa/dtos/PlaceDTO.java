package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import be.kdg.groepa.model.Place;
import org.json.JSONObject;

/**
* Created by delltvgateway on 2/18/14.
*/
public class PlaceDTO {
    private double lat;
    private double lon; //Long is a reserved keyword :D
    private String name;

    public PlaceDTO(JSONObject data) throws MissingDataException {
        if (!data.has("lat")) { throw new MissingDataException("lat"); }
        this.lat = data.getDouble("lat");

        if (!data.has("long")) { throw new MissingDataException("long"); }
        this.lon = data.getDouble("long");

        if (!data.has("address")) { throw new MissingDataException("address"); }
        this.name = data.getString("address");
    }

    public PlaceDTO(double lat, double lon, String name) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
    }

    public PlaceDTO(Place place) {
        this.lat = place.getLat();
        this.lon = place.getLon();
        this.name = place.getName();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlaceDTO placeDTO = (PlaceDTO) o;

        if (Double.compare(placeDTO.lat, lat) != 0) return false;
        if (Double.compare(placeDTO.lon, lon) != 0) return false;
        if (name != null ? !name.equals(placeDTO.name) : placeDTO.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lat);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lon);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
