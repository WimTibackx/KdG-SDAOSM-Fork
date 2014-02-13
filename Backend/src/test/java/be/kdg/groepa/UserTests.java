package be.kdg.groepa;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.Car;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import be.kdg.groepa.controllers.LoginController;
import be.kdg.groepa.exceptions.PasswordFormatException;
import be.kdg.groepa.exceptions.UsernameFormatException;
import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.persistence.impl.UserDaoImpl;
import be.kdg.groepa.service.api.UserService;

import be.kdg.groepa.model.Car;
import org.json.simple.JSONObject;
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

    private static boolean setupRun = false;

    private final static String testUsername = "Thierry@test.com";

    @Before
    public void start()
    {
        if (!setupRun)
        {
            Car car = new Car("Audi", "A5", 11);
            User user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername, car);
            User user2 = new User("OneCarUser", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Onecar@test.com");
            try {
                userService.addUser(user);
                userService.addUser(user2);
            } catch (Exception e) {
                e.printStackTrace();
            }
            setupRun = true;
        }
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
            System.out.println(e.getMessage());
            String temp = e.getMessage();
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
        Car car = new Car("Audi", "C4", 10.2);
        User user = new User("My Name", User.Gender.MALE, true, "Correct1", LocalDate.of(1993, 5, 5), "ihaveacar@cars.car", car);
        try {
            userService.addUser(user);
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

        userService.addCarToUser("Onecar@test.com", new Car("BMW", "X9", 11));
        userService.addCarToUser("Onecar@test.com",new Car("Renault", "Civic", 9.9));
        userService.addCarToUser("Onecar@test.com",new Car("Renault", "Civic", 9.9));
        userService.addCarToUser("Onecar@test.com",new Car("Renault", "Civic", 9.9));
        User u = userService.getUser("Onecar@test.com");
        assertEquals("Wrong amount of cars", 4, u.getCars().size());
    }

    @Test
    public void checkLoginCallsSession() {
        SessionObject session = new SessionObject(userService.getUser(testUsername));
        UserDao daoMock = createMock(UserDao.class);
        userService.setUserDao(daoMock);
        expect(daoMock.getUser(testUsername)).andReturn(new User("Thierry", User.Gender.MALE, false, userService.encryptString("Succes1"), LocalDate.of(1993, 10, 20), testUsername));

        daoMock.createSession(session);
        replay(daoMock);

        userService.checkLogin(testUsername,"Succes1");
        verify(daoMock);
        userService.setUserDao(new UserDaoImpl());
    }

    @Test
    public void changePassword() {
        String oldPass = "testPw1";
        String newPass = "Pwtest3";

        try {
            userService.addUser(new User("PJ", User.Gender.MALE, true, oldPass, LocalDate.of(1914, 2, 8), "pieterjanvdp@gmail.com"));
            userService.changePassword("pieterjanvdp@gmail.com", oldPass, newPass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertTrue("Change password fails", userService.checkLogin("pieterjanvdp@gmail.com", newPass));
    }

    public void testLogin(){
        LoginController controller = new LoginController();
        JSONObject obj = new JSONObject();
        obj.put("username", "username");
        obj.put("password", "password");

        //String myString = controller.login(obj.toJSONString());
    }

    /*
    private void initDaoData(){
        try {
            Car car = new Car("Audi", "A5", 11);
            User user = new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername, car);
            userService.addUser(user);
/*          userService.addUser(new User("Thierry", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Thierry@test.com"));
            userService.addUser(new User("OneCarUser", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Onecar@test.com", "Renault", "Civic", 9.9));
            userService.addUser(new User("OneCarUserTwo", User.Gender.MALE, false, "Succes1", LocalDate.of(1993, 10, 20), "Onecartwo@test.com", "Renault", "Civic", 9.9));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */


    }
