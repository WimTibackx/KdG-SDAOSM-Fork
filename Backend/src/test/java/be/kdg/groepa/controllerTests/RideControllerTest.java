package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Pieter-Jan on 12-3-14.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RideControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void testConfirmRide()
    {
        JSONObject json = new JSONObject();
        json.put("routeId", 3);
        json.put("year", 2014);
        json.put("month", 3);
        json.put("day", 12);
        json.put("hours", 9);
        json.put("minutes", 45);
        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        try
        {
            mockMvc.perform(post("/authorized/ride/confirmRide")
                    .contentType(TestUtil.APPLICATION_JSON_UTF8)
                    .content(myString))
                    .andExpect(status().isOk());
        }   catch (Exception e){
            e.printStackTrace();
        }

    }


}
