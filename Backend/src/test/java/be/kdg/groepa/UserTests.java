package be.kdg.groepa;

import be.kdg.groepa.service.api.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Thierry on 4/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class UserTests {
    @Autowired
    private UserService userService;

    @Test
    public void succesLogin(){
        assertTrue("Succesful login", userService.checkLogin("Thierry", "succes"));
    }

    @Test
    public void failLogin(){
        assertFalse("Fail login", userService.checkLogin("Thierry", "fail"));
    }
}
