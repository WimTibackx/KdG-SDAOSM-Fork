package be.kdg.groepa;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.util.AssertionErrors.assertTrue;

/**
 * Created by Thierry on 6/02/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class UserDaoTests {
    @Autowired
    private UserDao userDao;

    @Autowired
    private UserService userService;

    private String testUsername = "Thierry@test.com";
    private String testUsername2 = "TestUser2@test.com";
    private String testUsername3 = "TestUser3@test.com";
    private static boolean init = false;

    @Before
    public void init(){
        if(!init){
        try {
            userService.addUser(new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername));
            userService.addUser(new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername2));
            userService.addUser(new User("TestUser", User.Gender.FEMALE, false, "Succes1", LocalDate.of(1993, 10, 20), testUsername3));
        } catch (Exception e) {
            e.printStackTrace();
        }
            init = true;
        }
    }


    @Test
    public void createSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername));
        userDao.createSession(session);
        assertEquals("Session has been found", session, userDao.getSessionByUsername(testUsername));


    }

    @Test
    public void deleteSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername2));
        userDao.createSession(session);
        userDao.deleteSession(session);
        assertNull("Session should be deleted", userDao.getSessionByUsername(testUsername2));
    }

    @Test
    public void extendSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername3));
        userDao.createSession(session);
        assertNotNull("Should be added", userDao.getSessionByUsername(testUsername3));
        LocalDateTime firstVisit = session.getExperiationDate();
        //We have to sleep 1 second else there is no the difference (milliseconds get cut off)
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        userDao.extendSession(session);
        LocalDateTime secondVisit = userDao.getSessionByUsername(testUsername3).getExperiationDate();
        assertTrue("First visit earlier than second visit", firstVisit.isBefore(secondVisit));

    }
}
