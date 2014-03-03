package be.kdg.groepa.dtos;

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
    //TODO This probably won't suffice...

    public UpcomingTrajectDTO() {}
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
}
/*
	Trajectid
	Trajectdag (eerstvolgende waarop het wordt gereden, of meerdere instanties voor elke dag dat het wordt gereden?)
	PickupPlacetime
		Place
		Time
	DropoffPlacetime
		Place
		Time
	Route
		Chauffeur
		Evt. car
	evt. Other people who will carpool along on the same WDR?
 */