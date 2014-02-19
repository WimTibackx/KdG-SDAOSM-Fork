package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.json.simple.JSONObject;
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
import static org.hamcrest.CoreMatchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Tim on 18/02/14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RegisterControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String testUsername = "username@rc.test.com";
    private String testUsername2 = "username2@rc.test.com";

    @Test
    public void succesControllerRegister() throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", "Test User");
        json.put("gender", "Male");
        json.put("smoker", true);
        json.put("password", "Password1");
        json.put("year", 1993);
        json.put("month", 10);
        json.put("day", 3);
        json.put("username", testUsername);
        json.put("brand", "Skoda");
        json.put("type", "Sködalike");
        json.put("consumption", 10.3);
        json.put("fuelType", "Super98");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/register")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Logged in")));
    }

    @Test
    public void succesControllerRegisterNoCar() throws Exception {
        JSONObject json = new JSONObject();
        json.put("name", "Test User");
        json.put("gender", "Male");
        json.put("smoker", true);
        json.put("password", "Password1");
        json.put("year", 1993);
        json.put("month", 10);
        json.put("day", 3);
        json.put("username", testUsername2);

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/register")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Logged in")));
    }
}
