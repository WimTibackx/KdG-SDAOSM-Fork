package be.kdg.groepa.model;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thierry on 4/02/14.
 */
public class User {

    public enum Gender {
        MALE,
        FEMALE,
    }

    private String name;
    private Gender gender;
    private boolean smoker;
    private String password;
    private LocalDate dateOfBirth;
    private String username;      // username = email
    protected List<Car> cars;
    // protected List<Route> routes;
    // private String avatarURL;

    public User(String name, Gender gender, boolean smoker, String password, LocalDate dateofBirth, String username) {
        this.name = name;
        this.gender = gender;
        this.smoker = smoker;
        this.password = password;
        this.dateOfBirth = dateofBirth;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
