package be.kdg.groepa.model;

import org.hibernate.annotations.*;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.*;

@Entity
@Table(name="t_route")
public class Route {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //Is this one actually needed? Wouldn't you know this just by the one-or-more RouteOccurances combined with BeginDate/EndDate
    @Column(name="repeating")
    private boolean repeating;

    @Column(name="capacity")
    private int capacity;

    @Column(name="begin_date")
    @Type(type="org.jadira.usertype.dateandtime.threetenbp.PersistentLocalDate")
    private LocalDate beginDate;
    @Column(name="end_date")
    @Type(type="org.jadira.usertype.dateandtime.threetenbp.PersistentLocalDate")
    private LocalDate endDate;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User chauffeur;

    @ManyToOne
    @JoinColumn(name="carId", nullable=false)
    private Car car;


    @OneToMany(mappedBy="route")
    @LazyCollection(LazyCollectionOption.FALSE)
    @OrderBy("time")
    private List<PlaceTime> placeTimes = new ArrayList<>();

    @OneToMany(mappedBy="route")
    private List<WeekdayRoute> weekdayRoutes = new ArrayList<>();

    @OneToMany(mappedBy="route")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Traject> trajects = new ArrayList<>();

    public Route() {}

    public void setPlaceTimes(List<PlaceTime> placeTimes) {
        this.placeTimes = placeTimes;
    }

    public Route(boolean repeating, int capacity, LocalDate beginDate, LocalDate endDate, User chauffeur, Car car) {
        this.repeating = repeating;
        this.capacity = capacity;


        this.beginDate = beginDate;
        this.endDate = endDate;
        this.chauffeur = chauffeur;
        this.car = car;
    }

    public boolean isRepeating()
    {
        return repeating;
    }

    public void addWeekdayRoute(WeekdayRoute wr)
    {
        this.weekdayRoutes.add(wr);
    }

    protected void addPlaceTime(PlaceTime pt)
    {
        this.placeTimes.add(pt);
    }

    public void addTraject(Traject traject){
        this.trajects.add(traject);
    }

    public List<PlaceTime> getAllPlaceTimes(){
        List<PlaceTime> allPlaceTimes = new ArrayList<PlaceTime>();
        allPlaceTimes.addAll(this.placeTimes);
        for(Traject traject:trajects){
            allPlaceTimes.add(traject.getPickup());
            allPlaceTimes.add(traject.getDropoff());
        }
        return allPlaceTimes;
    }

    public User getChauffeur() {
        return chauffeur;
    }

    public void removeTraject(Traject traj){
        if(trajects.contains(traj)){
            trajects.remove(traj);
        }
    }

    public List<PlaceTime> getPlaceTimes() {
        return placeTimes;
    }

    public List<Traject> getTrajects() {
        return trajects;
    }

    public int getId() {
        return id;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Car getCar() {
        return car;
    }

    public List<WeekdayRoute> getWeekdayRoutes() {
        return weekdayRoutes;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapacity() {
        return capacity;
    }
}