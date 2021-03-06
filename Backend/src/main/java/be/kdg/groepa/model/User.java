package be.kdg.groepa.model;

import be.kdg.groepa.helpers.ImageHelper;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.Type;
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
    @Type(type="org.jadira.usertype.dateandtime.threetenbp.PersistentLocalDate")
    private LocalDate dateOfBirth;


    @Column(name="username", unique = true)
    private String username;      // username = email

    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    @Cascade(CascadeType.ALL)
    protected List<Car> cars = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = javax.persistence.CascadeType.ALL)
    private SessionObject sessionObject;

    @OneToMany(mappedBy = "chauffeur")
    @Cascade(CascadeType.ALL)
    protected List<Route> routes = new ArrayList<>();


    @Column(name="avatarURL", nullable = true)
    private String avatarURL;

    @Column(name="androidId", length = 1023)
    private String androidId;

    @OneToMany(mappedBy="receiver")
    private List<TextMessage> inbox;

    @OneToMany(mappedBy="sender")
    private List<TextMessage> outbox;

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

    public void setAndroidId(String androidId) {
        this.androidId = androidId;
    }

    public String getAndroidId() {
        return androidId;
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

    public List<Route> getRoutes() {
        return routes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public List<TextMessage> getInbox() {
        return inbox;
    }

    public List<TextMessage> getOutbox() {
        return outbox;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
