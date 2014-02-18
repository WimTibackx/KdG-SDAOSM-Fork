package be.kdg.groepa;

import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.impl.UserDaoImpl;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Thierry on 14/02/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")

public class ProfileControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private String testUsername = "profile@test.com";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static boolean init = false;


    @Before
    public void init(){
        userService.setUserDao(new UserDaoImpl());
        if (!init)
        {
            Car car = new Car("Audi", "A5", 11);
            User user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername, car);

            try {
                userService.addUser(user);
                userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
                userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
                userService.addCarToUser(testUsername,new Car("Renault", "Civic", 9.9));
            } catch (Exception e) {
                e.printStackTrace();
            }
            init = true;
        }
    }

    @Test
    public void succesfullUserFind() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        User user = userService.getUser(testUsername);

        mockMvc.perform(get("/authorized/profile/{id}", user.getId())).andExpect(status().isOk());
    }
}
