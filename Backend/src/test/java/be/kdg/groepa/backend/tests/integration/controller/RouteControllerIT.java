package be.kdg.groepa.backend.tests.integration.controller;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.threeten.bp.LocalDate;

import javax.servlet.http.Cookie;

/**
 * Created by delltvgateway on 2/18/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RouteControllerIT {
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private static boolean init = false;

    private Cookie cookie;

    @Before
    public void init() throws Exception {
        if (init) { return; }
        String username = "username@route.controller.it.example.com";
        String password = "Password1";
        User user = new User("TestUser", User.Gender.FEMALE, false, password, LocalDate.of(1993, 1, 1), username);
        this.userService.addUser(user);
        this.userService.checkLogin(username, password);
        SessionObject session = this.userService.getUserSession(username);
        this.cookie = new Cookie("Token", session.getSessionToken());
        cookie.setPath("/");
        //Set max age of cookie to 1 day
        cookie.setMaxAge(60 * 60 * 24);
        RouteControllerIT.init = true;
    }

    @Test
    public void addErroringData() throws Exception {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/route/add")
            .cookie(this.cookie)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content("{ \"car\": 1, \"freeSpots\": \"3\" }"))
            .andExpect(MockMvcResultMatchers.jsonPath("error").value("ParseError"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void addSuccessfulData() throws Exception {
        String data = "{\"car\": 1,\"freeSpots\": \"3\",\"repeating\": true,\"startDate\": \"2014-02-19\",\"endDate\": \"2014-02-27\",\"passages\": {\"Di\": [\"09:30\", \"10:00\"],\"Do\": [\"09:30\", \"10:00\"],\"Vr\": [\"09:30\", \"10:00\"],\"Wo\": [\"12:30\", \"13:00\"]},\"route\": [{\"lat\": 51.21523,\"long\": 4.398739999999975,\"address\": \"Nationalestraat, 2000 Antwerpen, België\"},{\"lat\": 51.2171198,\"long\": 4.4008122000000185,\"address\": \"Kammenstraat, 2000 Antwerpen, België\"}]}";
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authorized/route/add")
            .cookie(this.cookie)
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(data))
            .andExpect(MockMvcResultMatchers.jsonPath("test").value("foobar"))
            .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
