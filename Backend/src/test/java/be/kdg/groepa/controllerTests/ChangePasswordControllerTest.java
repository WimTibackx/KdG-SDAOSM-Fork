package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.SessionObject;
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

import javax.servlet.http.Cookie;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Tim on 19/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class ChangePasswordControllerTest {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private String testUsername = "username@rc.test.com";
    private String oldPassword = "Password1";
    private static boolean init = false;
    private static Cookie cookie;

    @Before
    public void init(){
        if(!init){
            try {
                userService.addUser(new User("username", User.Gender.MALE, false, oldPassword, LocalDate.of(1993, 10, 20), testUsername));
            } catch (Exception e) {
                e.printStackTrace();
            }
            init = true;
        }
        this.userService.checkLogin(testUsername, oldPassword);
        cookie = new Cookie("Token", userService.getUserSession(testUsername).getSessionToken());
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24);

    }

    @Test
    public void succesControllerChangePassword() throws Exception {

        JSONObject json = new JSONObject();
        json.put("oldpassword", oldPassword);
        json.put("newpassword", "NewPassword");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/changepassword")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("PasswordChanged")));
    }

    @Test
    public void failedControllerChangePassword() throws Exception {

        JSONObject json = new JSONObject();
        json.put("oldpassword", "WrongPassword");
        json.put("newpassword", "NewPassword2");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/changepassword")
                .cookie(this.cookie)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("PasswordNotChanged")));
    }

    @Test
    public void failedControllerChangePasswordUserNotFound() throws Exception {

        JSONObject json = new JSONObject();
        json.put("oldpassword", "NewPassword2");
        json.put("newpassword", "NewPassword3");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/changepassword")
                // No cookie so user can't be found
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("UserNotFound")));
    }
}
