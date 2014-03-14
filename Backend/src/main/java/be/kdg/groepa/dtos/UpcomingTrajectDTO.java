package be.kdg.groepa.dtos;

import be.kdg.groepa.model.PlaceTime;
import be.kdg.groepa.model.Traject;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalTime;

/**
 * Created by delltvgateway on 3/2/14.
 */
public class UpcomingTrajectDTO {
    private int id;
    private String nextOccurence;
    private PlaceDTO pickupPlace;
    private String pickupTime;
    private PlaceDTO dropoffPlace;
    private String dropoffTime;
    private int routeId;
    private int chauffeurId;
    private String chauffeurName;
    private String chauffeurUsername;
    private String[] passengerUsernames;
    private String[] passengerNames;
    private String[] passengerPictures;
    private String[] routePlaces;

    public UpcomingTrajectDTO() {
    }

    public UpcomingTrajectDTO(Traject traject, LocalDate nextOccurence) {
        this();
        this.id = traject.getId();
        this.nextOccurence = nextOccurence.toString();
        this.pickupPlace = new PlaceDTO(traject.getPickup().getPlace());
        this.pickupTime = traject.getPickup().getTime().toString();
        this.dropoffPlace = new PlaceDTO(traject.getDropoff().getPlace());
        this.dropoffTime = traject.getDropoff().getTime().toString();
        this.routeId = traject.getRoute().getId();
        this.chauffeurId = traject.getRoute().getChauffeur().getId();
        this.chauffeurName = traject.getRoute().getChauffeur().getName();
        this.chauffeurUsername = traject.getRoute().getChauffeur().getUsername();
        // These sizes should be big enough. We're not expecting more than 8 passengers on a (weekday)route, nor more than 16 places in 1 (weekday)route.
        this.passengerNames = new String[8];
        this.passengerUsernames = new String[8];
        this.passengerPictures = new String[8];
        this.routePlaces = new String[16];

        if (traject.getRoute().isRepeating()) {
            int i = 0, j = 0;
            for (Traject t : traject.getWeekdayRoute().getTrajects()) {
                if (!t.getUser().getUsername().equals(chauffeurUsername)) {
                    passengerUsernames[i] = t.getUser().getUsername();
                    passengerNames[i] = t.getUser().getName();
                    passengerPictures[i] = t.getUser().getAvatarURL();
                }
                i++;
            }
            for(PlaceTime pt:traject.getWeekdayRoute().getPlaceTimes()){
                routePlaces[j] = pt.getPlace().getName();
                j++;
            }
        } else {
            int i = 0, j = 0;
            for (Traject t : traject.getRoute().getTrajects()) {
                if (!t.getUser().getUsername().equals(chauffeurUsername)) {
                    passengerUsernames[i] = t.getUser().getUsername();
                    passengerNames[i] = t.getUser().getName();
                    passengerPictures[i] = t.getUser().getAvatarURL();
                }
                i++;
            }
            for(PlaceTime pt:traject.getWeekdayRoute().getPlaceTimes()){
                routePlaces[j] = pt.getPlace().getName();
                j++;
            }
        }

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNextOccurence() {
        return nextOccurence;
    }

    public void setNextOccurence(LocalDate nextOccurence) {
        this.nextOccurence = nextOccurence.toString();
    }

    public PlaceDTO getPickupPlace() {
        return pickupPlace;
    }

    public void setPickupPlace(PlaceDTO pickupPlace) {
        this.pickupPlace = pickupPlace;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(LocalTime pickupTime) {
        this.pickupTime = pickupTime.toString();
    }

    public PlaceDTO getDropoffPlace() {
        return dropoffPlace;
    }

    public void setDropoffPlace(PlaceDTO dropoffPlace) {
        this.dropoffPlace = dropoffPlace;
    }

    public String getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(LocalTime dropoffTime) {
        this.dropoffTime = dropoffTime.toString();
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getChauffeurId() {
        return chauffeurId;
    }

    public void setChauffeurId(int chauffeurId) {
        this.chauffeurId = chauffeurId;
    }

    public String getChauffeurName() {
        return chauffeurName;
    }

    public void setChauffeurName(String chauffeurName) {
        this.chauffeurName = chauffeurName;
    }

    public String getChauffeurUsername() {
        return chauffeurUsername;
    }

    public void setChauffeurUsername(String chauffeurUsername) {
        this.chauffeurUsername = chauffeurUsername;
    }

    public String[] getPassengerUsernames() {
        return passengerUsernames;
    }

    public void setPassengerUsernames(String[] passengerUsernames) {
        this.passengerUsernames = passengerUsernames;
    }

    public String[] getPassengerNames() {
        return passengerNames;
    }

    public void setPassengerNames(String[] passengerNames) {
        this.passengerNames = passengerNames;
    }

    public String[] getPassengerPictures() {
        return passengerPictures;
    }

    public void setPassengerPictures(String[] passengerPictures) {
        this.passengerPictures = passengerPictures;
    }

    public String[] getRoutePlaces() {
        return routePlaces;
    }

    public void setRoutePlaces(String[] routePlaces) {
        this.routePlaces = routePlaces;
    }
}