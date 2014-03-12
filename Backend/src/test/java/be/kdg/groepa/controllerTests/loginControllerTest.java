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
import org.threeten.bp.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Thierry on 13/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class loginControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    private String testUsername = "username@lc.test.com";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static boolean init = false;


    @Before
    public void init(){
        if(!init){
            try {
                userService.addUser(new User("username", User.Gender.MALE, false, "Password1", LocalDate.of(1993, 10, 20), testUsername));
            } catch (Exception e) {
                e.printStackTrace();
            }

            init = true;
        }
    }

    @Test
    public void succesControllerLogin() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", testUsername);
        json.put("password", "Password1");
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("Token").exists())
                .andExpect(status().isOk());

    }

    @Test
    public void parseExceptionError() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username2", testUsername);
        json.put("password", "Password1");
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("error", is("ParseError")));
    }

    @Test
    public void loginFailError() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", testUsername);
        json.put("password", "Password");
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("error", is("LoginComboWrong")));
    }
}
