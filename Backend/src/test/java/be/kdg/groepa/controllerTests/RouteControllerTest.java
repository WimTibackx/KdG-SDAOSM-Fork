package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * Created by Pieter-Jan on 12-3-14.
 */
public class RouteControllerTest {

    /*@Test
    public void testSearchRoutes()
    {
        JSONObject json = new JSONObject();
        json.put("username", testUsername);
        json.put("password", "Password");
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/login")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("error", is("LoginComboWrong")));
    } */
}
