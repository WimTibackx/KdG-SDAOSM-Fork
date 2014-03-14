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

import javax.servlet.http.Cookie;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by Thierry on 13/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class AndroidControllerTest {
    private MockMvc mockMvc;
    private static Cookie cookie;


    @Autowired
    private UserService userService;

    private String testUsername = "username@ac.test.com";
    private String androidId = "BBBBBBBBBBBBBBBBBBBBBBBBBBBAAAAAAAAAAAAAAAAAAAJJJJAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL9999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL99999999AAAAAAAAAAAAAAAAAAAJJJJJJJJJJJJJJJJJJJJJJKKKKKKKKKKKKKKKKKKKKKKKKKLLLLLLLLLLLLLLLLLLLL999999999999999999999999_____________________SSSSSSSSSSSSSSSSSSSSTTTTTTTTTTTTTTTTTTTTRRRRRRRRRRRRRRRRRRRRRRIIIIIIIIIIIIIIIIIIIIINNNNNNNNNNNNNNNNNNNNGGGGGGGGGGGGGGG";

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static boolean init = false;
    private static boolean firstDone = false;


    @Before
    public void init(){
        if(!init){
            try {
                userService.addUser(new User("Android user", User.Gender.MALE, false, "Password1", LocalDate.of(1993, 10, 20), testUsername));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void succesSetAndroidId() throws Exception {
        JSONObject json = new JSONObject();
        json.put("username", testUsername);
        json.put("id", androidId);
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        firstDone = true;
        mockMvc.perform(post("/authorized/user/registerandroid")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(status().isOk());
    }

    @Test
    public void succesAndroidIdFromUserTest() throws Exception {
        if(!firstDone){
            succesSetAndroidId();
        }
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/authorized/user/getandroid/{id}", userService.getUser(testUsername).getId()))
                .andExpect(jsonPath("androidId", is(androidId)));
    }
}
