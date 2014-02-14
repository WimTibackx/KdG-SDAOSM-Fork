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

    private User.Gender gender;

    private boolean smoker;

    private String password;

    private LocalDate dateOfBirth;

    private String username;      // username = email

    protected List<CarDTO> cars = new ArrayList<CarDTO>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User.Gender getGender() {
        return gender;
    }

    public void setGender(User.Gender gender) {
        this.gender = gender;
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

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<CarDTO> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = new ArrayList<>();
        for(Car car: cars){
            CarDTO carDTO = new CarDTO();
            carDTO.setBrand(car.getBrand());
            carDTO.setCarId(car.getCarId());
            carDTO.setConsumption(car.getConsumption());
            carDTO.setType(car.getType());
            this.cars.add(carDTO);
        }
    }
}
