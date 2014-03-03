package be.kdg.groepa.controllerTests;

import be.kdg.groepa.TestUtil;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameExistsException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Tim on 27/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class TextMessageControllerTest {
    private static String username1 = "user@tmc.test.com";
    private static String username2 = "user2@tmc.test.com";
    private static boolean init = false;
    private User sender, receiver;
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void init() throws PasswordFormatException, UsernameExistsException, UsernameFormatException {
        if(!init){
            sender = new User(username1, User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 03), username1);
            receiver =  new User(username2, User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 03), username2);
            userService.addUser(sender);
            userService.addUser(receiver);
            init = true;
        }
    }

    @Test
    public void succesSendMessageTest() throws Exception {
        JSONObject json = new JSONObject();
        json.put("senderUsername", username1);
        json.put("receiverUsername", username2);
        json.put("messageSubject", "New message header");
        json.put("messageBody", "New message body");

        String myString = json.toString();
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(post("/authorized/textmessage/send")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Message succesfully sent.")));
        // Sending a 2nd message.
        json.put("senderUsername", username1);
        json.put("receiverUsername", username2);
        json.put("messageSubject", "New message header TWO");
        json.put("messageBody", "New message body TWO");

        myString = json.toString();
        mockMvc.perform(post("/authorized/textmessage/send")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(myString))
                .andExpect(jsonPath("result", is("Message succesfully sent.")));
    }

    @Test
    public void succesGetMessagesFromUserTest() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(get("/authorized/textmessage/get/{id}", userService.getUser(username1).getId())).andExpect(status().isOk());
    }


}
