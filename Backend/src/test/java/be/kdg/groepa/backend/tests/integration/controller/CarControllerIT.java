package be.kdg.groepa.backend.tests.integration.controller;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.CarService;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;

import javax.servlet.http.Cookie;

/**
 * Created by delltvgateway on 2/20/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class CarControllerIT {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private Cookie cookie;
    private static boolean init = false;

    @Before
    public void init() throws Exception {
        String username = "username@addcar.controller.it.example.com";
        String password = "Password1";
        if (!init) {
            User user = new User("TestUser", User.Gender.FEMALE, false, password, LocalDate.of(1993, 1, 1), username);
            this.userService.addUser(user);
        }
        this.userService.checkLogin(username, password);
        SessionObject session = this.userService.getUserSession(username);
        this.cookie = new Cookie("Token", session.getSessionToken());
        cookie.setPath("/");
        //Set max age of cookie to 1 day
        cookie.setMaxAge(60 * 60 * 24);
        CarControllerIT.init = true;
    }

    @Test
    public void addCar() throws Exception {
        String data = "{ \"brand\": \"Ford\", \"type\": \"Fiesta\", \"fueltype\": \"DIESEL\", \"consumption\": \"8\" }";
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/user/car/add")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(data))
                .andExpect(MockMvcResultMatchers.jsonPath("inserted").exists())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteNonExistingCar() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/user/car/0/delete")
                .cookie(this.cookie))
                .andExpect(MockMvcResultMatchers.jsonPath("error").value("CarNotFound"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void deleteOthersCar() throws Exception {
        User u = new User("otherUser", User.Gender.FEMALE, false, "Password1", LocalDate.of(1993, 1, 1), "otheruser@car.controller.it.example.com");
        this.userService.addUser(u);
        Car c = new Car("Ford", "Fiesta", 8.0, Car.FuelType.DIESEL);
        this.carService.addCar(u.getUsername(), c);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/user/car/"+c.getCarId()+"/delete")
                .cookie(this.cookie))
                .andExpect(MockMvcResultMatchers.jsonPath("error").value("CarNotYours"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
