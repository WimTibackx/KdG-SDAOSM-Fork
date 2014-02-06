package be.kdg.groepa;


import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UserExistException;
import be.kdg.groepa.exceptions.UsernameFormatException;
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
        boolean failed = false;
        try {
            userService.addUser(new User("Wimmetje", User.Gender.MALE, false, "testPassword1", LocalDate.of(1993, 10, 20), "test@user.com"));
        } catch (Exception e) {
            failed = true;
            e.printStackTrace();
        }
        assertFalse("The user could not be registered", failed);
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

    @Test
    public void tooLongPassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordTooLong", User.Gender.FEMALE, true, "Abc12IkBenVeelTeLangVoorEenPaswoordEnDusZalErEenFoutOptredenDertigLetters", LocalDate.of(1993, 5, 5), "too@long.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
                failed = true;
        }
        assertTrue("Password was correctly used", failed);
    }

    @Test
    public void noNumberPassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordNoNumber", User.Gender.FEMALE, true, "PasswordNoNumber", LocalDate.of(1993, 5, 5), "no@number.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
                failed = true;
        }
        assertTrue("Password was correctly used", failed);
    }

    @Test
    public void noCapitalPassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordNoCapital", User.Gender.FEMALE, true, "passwordnocapital123", LocalDate.of(1993, 5, 5), "no@capital.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
                failed = true;
        }
        assertTrue("Password was correctly used", failed);
    }

    @Test
    public void noLowercasePassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordNoLowercase", User.Gender.FEMALE, true, "MYCAPSLOCKISSTUCK12", LocalDate.of(1993, 5, 5), "no@lowercase.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
                failed = true;
        }
        assertTrue("Password was correctly used", failed);
    }

    @Test
    public void whitespacePassword() {
        boolean failed = false;
        try {
            userService.addUser(new User("PasswordWhitespace", User.Gender.FEMALE, true, "Oh yes whitespaces12", LocalDate.of(1993, 5, 5), "white@spaces.be"));
        } catch (Exception e)
        {
            if(e instanceof PasswordFormatException)
                failed = true;
        }
        assertTrue("Password was correctly used", failed);
    }

    @Test
    public void usernameNoEmail() {
        boolean failed = false;
        try {
            userService.addUser(new User("SomeName", User.Gender.FEMALE, true, "Correct1", LocalDate.of(1993, 5, 5), "TimVanRoeyen"));
        } catch (Exception e)
        {
            if(e instanceof UsernameFormatException)
                failed = true;
        }
        assertTrue("Username was correctly entered", failed);
    }
}
