package be.kdg.groepa.model;

import java.security.MessageDigest;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.threeten.bp.LocalDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierry on 4/02/14.
 */
// @Entity
// @Table(name="t_user")
public class User {
    // @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // @Column(name="name")
    private String name;

    // @Column(name="gender")
    private Gender gender;

    // @Column(name="smoker")
    private boolean smoker;

    // @Column(name="password")
    private String password;

    // @Column(name="dateOfBirth")
    private LocalDate dateOfBirth;

    // @Column(name="username")
    private String username;      // username = email

    // @OneToMany(mappedBy="user")
    // @Cascade(CascadeType.SAVE_UPDATE)
    protected ArrayList<Car> cars;
    // protected List<Route> routes;
    // private String avatarURL;

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateofBirth, String username) {
        this.cars = new ArrayList<Car>();
        this.name = name;
        this.gender = gender;
        this.smoker = smoker;
        this.password = password;
        this.dateOfBirth = dateofBirth;
        this.username = username;
    }

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateOfBirth, String username, String brand, String type, double cons) {
        this(name, gender, smoker, password, dateOfBirth, username);
        Car myCar = new Car(brand, type, cons);
        this.cars.add(myCar);
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addCar(Car car) {
        this.cars.add(car);
    }

    public enum Gender {
        MALE,
        FEMALE,
    }

}
