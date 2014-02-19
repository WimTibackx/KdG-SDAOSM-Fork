package be.kdg.groepa.model;

import be.kdg.groepa.helpers.ImageHelper;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.threeten.bp.LocalDate;

import javax.persistence.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierry on 4/02/14.
 */
@Entity
@Table(name="t_user")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="name")
    private String name;

    @Column(name="gender")
    private Gender gender;

    @Column(name="smoker")
    private boolean smoker;

    @Column(name="password")
    private String password;

    @Column(name="dateOfBirth")
    private LocalDate dateOfBirth;


    @Column(name="username", unique = true)
    private String username;      // username = email

    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    protected List<Car> cars = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = javax.persistence.CascadeType.ALL)
    private SessionObject sessionObject;

    @OneToMany
    @JoinColumn(name="routeId")
    protected List<Route> routes = new ArrayList<>();


    @Column(name="avatarURL", nullable = true)
    private String avatarURL;

    public User(){}

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateofBirth, String username) {
        this.name = name;
        this.gender = gender;
        this.smoker = smoker;
        this.password = password;
        this.dateOfBirth = dateofBirth;
        this.username = username;
    }

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateOfBirth, String username, Car car) {
        this(name, gender, smoker, password, dateOfBirth, username);
        this.cars.add(car);
    }

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateofBirth, String username, File picture) {
        this.name = name;
        this.gender = gender;
        this.smoker = smoker;
        this.password = password;
        this.dateOfBirth = dateofBirth;
        this.username = username;
        this.avatarURL = ImageHelper.writeUserImage(picture, name);
    }

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateOfBirth, String username, Car car, File picture) {
        this(name, gender, smoker, password, dateOfBirth, username);
        this.avatarURL = ImageHelper.writeUserImage(picture, name);
        this.cars.add(car);
    }

    public String getUsername() {
        return username;
    }

    public List<Car> getCars() {
        return cars;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public Integer getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    public void addRoute(Route r) {
        this.routes.add(r);
    }

    public enum Gender {
        MALE,
        FEMALE,
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void editImage(File newPicture){
        this.avatarURL = ImageHelper.editUserImage(newPicture, this.avatarURL);
    }

    public void removeImage(){
        ImageHelper.removeImage(this.avatarURL);
        this.avatarURL = null;
    }

    public void setSessionObject(SessionObject sessionObject) {
        this.sessionObject = sessionObject;
    }


}
