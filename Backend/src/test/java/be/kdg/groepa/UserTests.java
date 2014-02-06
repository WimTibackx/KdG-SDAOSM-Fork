package be.kdg.groepa;


import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UserExistException;
import be.kdg.groepa.model.User;
import be.kdg.groepa.service.api.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

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


    @Before
    public void initData()
    {
        try {
            userService.addUser(new User("Thierry", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Thierry@test.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void succesLogin(){
        assertTrue("Check login failed", userService.checkLogin("Thierry@test.com", "Succes1"));
    }

    @Test
    public void failLogin(){
        assertFalse("Supposed failed login did not fail", userService.checkLogin("Thierry@test.com", "fail"));
    }

    @Test
    public void registerNoCarUser()
    {
        try {
            userService.addUser(new User("Wimmetje", User.Gender.MALE, false, "testPassword", LocalDate.of(1993, 10, 20), "test@user.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void tooShortPassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordTooShort", User.Gender.FEMALE, true, "Abc13", LocalDate.of(1993, 5, 5), "too@short.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
            failed = true;
        }
        assertTrue("Password was correctly used", failed);

    }
}
