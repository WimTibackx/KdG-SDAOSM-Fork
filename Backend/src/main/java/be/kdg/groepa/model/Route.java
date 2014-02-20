package be.kdg.groepa.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.threeten.bp.LocalDateTime;

import javax.persistence.*;
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
    private LocalDateTime beginDate;
    @Column(name="end_date")
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name="userId", nullable=false)
    private User chauffeur;

    @ManyToOne
    @JoinColumn(name="carId", nullable=false)
    private Car car;

    @OneToMany(mappedBy="route")
    @Cascade(CascadeType.ALL)
    private List<PlaceTime> placeTimes = new ArrayList<>();

    @OneToMany(mappedBy="route")
    @Cascade(CascadeType.ALL)
    private List<WeekdayRoute> weekdayRoutes = new ArrayList<>();

    public Route() {}

    public Route(boolean repeating, int capacity, LocalDateTime beginDate, LocalDateTime endDate, User chauffeur, Car car) {
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
}