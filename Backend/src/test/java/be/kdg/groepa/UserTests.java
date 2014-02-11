package be.kdg.groepa;


import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.persistence.impl.UserDaoMap;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.LocalDate;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

/**
 * Created by Thierry on 4/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class UserTests {
    @Autowired
    private UserService userService;

    private String testUsername = "Thierry@test.com";

    @Before
    public void initData()
    {
        initDaoData();
    }

    @Test
    public void succesLogin(){
        assertTrue("Check login failed", userService.checkLogin(testUsername, "Succes1"));
    }

    @Test
    public void failLogin(){
        assertFalse("Supposed failed login did not fail", userService.checkLogin(testUsername, "fail"));
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

    @Test(expected = PasswordFormatException.class)
    public void tooShortPassword() throws Exception {
        userService.addUser(new User("PasswordTooShort", User.Gender.FEMALE, true, "Abc13", LocalDate.of(1993, 5, 5), "too@short.be"));
        fail();
    }

    @Test(expected = PasswordFormatException.class)
    public void tooLongPassword() throws Exception{
        userService.addUser(new User("PasswordTooLong", User.Gender.FEMALE, true, "Abc12IkBenVeelTeLangVoorEenPaswoordEnDusZalErEenFoutOptredenDertigLetters", LocalDate.of(1993, 5, 5), "too@long.be"));
        fail();
    }

    @Test(expected = PasswordFormatException.class)
    public void noNumberPassword() throws Exception {
        userService.addUser(new User("PasswordNoNumber", User.Gender.FEMALE, true, "PasswordNoNumber", LocalDate.of(1993, 5, 5), "no@number.be"));
        fail();
    }

    @Test(expected = PasswordFormatException.class)
    public void noCapitalPassword() throws Exception {
        userService.addUser(new User("PasswordNoCapital", User.Gender.FEMALE, true, "passwordnocapital123", LocalDate.of(1993, 5, 5), "no@capital.be"));
        fail();
    }

    @Test(expected = PasswordFormatException.class)
    public void noLowercasePassword() throws Exception {
        userService.addUser(new User("PasswordNoLowercase", User.Gender.FEMALE, true, "MYCAPSLOCKISSTUCK12", LocalDate.of(1993, 5, 5), "no@lowercase.be"));
        fail();
    }

    @Test(expected = PasswordFormatException.class)
    public void whitespacePassword() throws Exception {
        userService.addUser(new User("PasswordWhitespace", User.Gender.FEMALE, true, "Oh yes whitespaces12", LocalDate.of(1993, 5, 5), "white@spaces.be"));
        fail();
    }

    @Test(expected = UsernameFormatException.class)
    public void usernameNoEmail() throws Exception {
        userService.addUser(new User("SomeName", User.Gender.FEMALE, true, "Correct1", LocalDate.of(1993, 5, 5), "TimVanRoeyen"));
        fail();
    }

    @Test
    public void userWithOneCar(){
        boolean failed = false;
        try {
            userService.addUser(new User("My Name", User.Gender.MALE, true, "Correct1", LocalDate.of(1993, 5, 5), "ihaveacar@cars.car", "Audi", "C4", 10.2));
        } catch (Exception e) {
            if(e instanceof PasswordFormatException || e instanceof UsernameFormatException)
            {} else {
                failed = true;
            }
            e.printStackTrace();
        }
        assertFalse("User was incorrectly created", failed);
    }

    @Test
    public void addCarToUser(){
        User userWithOneCar = userService.getUser("Onecar@test.com");
        userWithOneCar.addCar(new Car("Honda", "Civic", 9));
        assertEquals("Wrong amount of cars", userWithOneCar.getCars().size(), 2);
    }

    @Test
    public void checkLoginCallsSession() {
        SessionObject session = new SessionObject(testUsername);
        UserDao daoMock = createMock(UserDao.class);
        userService.setUserDao(daoMock);
        expect(daoMock.getUser(testUsername)).andReturn(new User("Thierry", User.Gender.MALE, false, userService.encryptString("Succes1"), LocalDate.of(1993, 10, 20), testUsername));

        daoMock.createSession(session);
        replay(daoMock);

        userService.checkLogin(testUsername,"Succes1");
        verify(daoMock);
        userService.setUserDao(new UserDaoMap());
        initDaoData();
    }

    private void initDaoData(){
        try {
            userService.addUser(new User("Thierry", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Thierry@test.com"));
            userService.addUser(new User("OneCarUser", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Onecar@test.com", "Renault", "Civic", 9.9));
            userService.addUser(new User("OneCarUserTwo", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Onecartwo@test.com", "Renault", "Civic", 9.9));
        } catch (Exception e) {
            e.printStackTrace();
        }


    }








}
