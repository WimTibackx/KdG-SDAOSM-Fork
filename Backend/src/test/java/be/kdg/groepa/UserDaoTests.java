package be.kdg.groepa;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.persistence.api.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.threeten.bp.LocalDateTime;

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

    private String testUsername = "Thierry@test.com";


    @Test
    public void createSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername));
        userDao.createSession(session);
        assertEquals("Session has been found", session, userDao.getSession(testUsername + "123456"));


    }

    @Test
    public void deleteSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername));
        userDao.createSession(session);
        assertEquals("Session has been found", session, userDao.getSession(testUsername + "123456"));
        userDao.deleteSession(session);
        assertNull("Session should be deleted", userDao.getSession(testUsername + "123456"));
    }

    @Test
    public void extendSession() {
        SessionObject session = new SessionObject(userDao.getUser(testUsername));
        userDao.createSession(session);
        assertEquals("Session has been found", session, userDao.getSession(testUsername + "123456"));
        LocalDateTime firstVisit = session.getExperiationDate();
        //We have to sleep 1 milisecond else maven doesn't notice the difference
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();

        }
        SessionObject ses = userDao.getSession(testUsername + "123456");
        userDao.extendSession(ses);
        LocalDateTime secondVisit = userDao.getSession(testUsername + "123456").getExperiationDate();
        assertTrue("First visit earlier than second visit", firstVisit.isBefore(secondVisit));

    }
}
