package be.kdg.groepa.android.dto;

import android.util.Log;
import org.json.JSONArray;
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
    private String pickupTime;
    private PlaceDto dropoffPlace;
    private String dropoffTime;
    private int routeId;
    private int chauffeurId;
    private String chauffeurName;
    private String chauffeurUsername;
    private JSONArray passengerUsernames;
    private JSONArray passengerNames;
    private JSONArray passengerPictures;
    private JSONArray routePlaces;
    private int routeYear, routeMonth, routeDay, routeStartHour, routeStartMinute;

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
            this.chauffeurUsername = data.getString("chauffeurUsername");
            System.out.println("UPCOMINGTRAJDTO: INITIALISING JSONARRAYS");
            this.passengerUsernames = (JSONArray)data.get("passengerUsernames");
            System.out.println("PASSENGERUSERNAMES: " + passengerUsernames);
            this.passengerNames = (JSONArray)data.get("passengerNames");
            this.passengerPictures = (JSONArray)data.get("passengerPictures");
            System.out.println("ROUTEPLACES: " + routePlaces);
            this.routePlaces = (JSONArray)data.get("routePlaces");
            this.routeYear = data.getInt("routeYear");
            this.routeMonth = data.getInt("routeMonth");
            this.routeDay = data.getInt("routeDay");
            this.routeStartHour = data.getInt("routeStartHour");
            this.routeStartMinute = data.getInt("routeStartMinute");

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("UpcomingTraject", e.getMessage());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println("DONE CREATING UPCOMINGTRAJDTO");
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

    public String getChauffeurUsername() {
        return chauffeurUsername;
    }

    public JSONArray getPassengerUsernames() {
        return passengerUsernames;
    }

    public JSONArray getPassengerNames() {
        return passengerNames;
    }

    public JSONArray getPassengerPictures() {
        return passengerPictures;
    }

    public JSONArray getRoutePlaces() {
        return routePlaces;
    }

    public int getRouteYear() {
        return routeYear;
    }

    public int getRouteMonth() {
        return routeMonth;
    }

    public int getRouteDay() {
        return routeDay;
    }

    public int getRouteStartHour() {
        return routeStartHour;
    }

    public int getRouteStartMinute() {
        return routeStartMinute;
    }

    @Override
    public int compareTo(UpcomingTrajectDto another) {
        return this.nextOccurrence.compareTo(another.nextOccurrence);
    }
}
