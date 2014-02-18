package be.kdg.groepa.dtos;

import be.kdg.groepa.exceptions.MissingDataException;
import org.json.JSONObject;

/**
* Created by delltvgateway on 2/18/14.
*/
public class PlaceDTO {
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
