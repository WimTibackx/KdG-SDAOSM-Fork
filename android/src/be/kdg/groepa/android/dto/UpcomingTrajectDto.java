package be.kdg.groepa.android.dto;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by delltvgateway on 3/3/14.
 */
public class UpcomingTrajectDto implements Comparable<UpcomingTrajectDto> {
    private int id;
    private Calendar nextOccurrence;
    private PlaceDto pickupPlace;
    //TODO: Transform it into something other than string
    private String pickupTime;
    private PlaceDto dropoffPlace;
    private String dropoffTime;
    private int routeId;
    private int chauffeurId;
    private String chauffeurName;

    public UpcomingTrajectDto(JSONObject data) {
        try {
            this.id = data.getInt("id");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(data.getString("nextOccurence"));
            this.nextOccurrence = Calendar.getInstance();
            this.nextOccurrence.setTime(d);
            this.pickupPlace = new PlaceDto(data.getJSONObject("pickupPlace"));
            this.pickupTime = data.getString("pickupTime");
            this.dropoffPlace = new PlaceDto(data.getJSONObject("dropoffPlace"));
            this.dropoffTime = data.getString("dropoffTime");
            this.routeId = data.getInt("routeId");
            this.chauffeurId = data.getInt("chauffeurId");
            this.chauffeurName = data.getString("chauffeurName");
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UpcomingTraject", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public Calendar getNextOccurrence() {
        return nextOccurrence;
    }

    public PlaceDto getPickupPlace() {
        return pickupPlace;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public PlaceDto getDropoffPlace() {
        return dropoffPlace;
    }

    public String getDropoffTime() {
        return dropoffTime;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getChauffeurId() {
        return chauffeurId;
    }

    public String getChauffeurName() {
        return chauffeurName;
    }

    @Override
    public int compareTo(UpcomingTrajectDto another) {
        return this.nextOccurrence.compareTo(another.nextOccurrence);
    }
}
