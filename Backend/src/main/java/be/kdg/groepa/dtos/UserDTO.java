package be.kdg.groepa.dtos;

import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thierry on 14/02/14.
 */
public class UserDTO {

    private String name;

    private String gender;

    private boolean smoker;

    private String password;

    private String dateOfBirth;

    private String username;      // username = email
    private String avatarURL;

    protected List<CarDTO> cars = new ArrayList<CarDTO>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }
    public User.Gender getGenderAsEnum() { return User.Gender.valueOf(gender); }

    public void setGender(User.Gender gender) {
        this.gender = gender.name();
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth.toString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public List<CarDTO> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = new ArrayList<>();
        for(Car car: cars){
            CarDTO carDTO = new CarDTO(car);
            this.cars.add(carDTO);
        }
    }

    /*
     * This creates a basic userDTO based on a user
     * It has the recorddata (minus password and id) with no relationships, like cars
     */
    public static UserDTO createBasic(User user) {
        UserDTO dto = new UserDTO();
        dto.setName(user.getName());
        dto.setGender(user.getGender());
        dto.setSmoker(user.isSmoker());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAvatarURL(user.getAvatarURL());
        return dto;
    }
}
