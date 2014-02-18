package be.kdg.groepa.backend.tests.unit.service;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.threeten.bp.LocalDate;

import java.util.UUID;

/**
 * Created by delltvgateway on 2/16/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class SessionTests {

    //TODO: Extend is not called often enough (only when logging in), delete is never called

    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    private UserDao userDaoMock;

    @Before
    public void initDb() {
        this.userDaoMock = EasyMock.createMock(UserDao.class);
        this.userService.setUserDao(this.userDaoMock);
    }

    @After
    public void cleanUpDb() {
        this.userService.setUserDao(this.userDao); //TODO: Find out if we actually need to do this or if Spring takes care of this for us
    }

    @Test
    public void getSessionByUsernameValid() {
        final User user = this.makeDummyUser("sbuv");
        final SessionObject expectedSession = new SessionObject(user);

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSession(user.getUsername());
        Assert.assertEquals("The session is valid, thus we should get it.", expectedSession, returnedSession);
    }

    @Test
    public void getSessionByUsernameInvalid() {
        final User user = this.makeDummyUser("sbui");
        final SessionObject expectedSession = new SessionObject(user);
        expectedSession.setExperiationDate(expectedSession.getExperiationDate().minusDays(2));

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSession(user.getUsername());
        Assert.assertNull("The session is invalid, thus we should get null.", returnedSession);
    }

    @Test
    public void getSessionByUsernameNonexistent() {
        final User user = this.makeDummyUser("sbun");

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(null);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSession(user.getUsername());
        Assert.assertNull("The session is nonexistent, thus we should get null.", returnedSession);
    }

    @Test
    public void getSessionByTokenValid() {
        final User user = this.makeDummyUser("sbtv");
        final SessionObject expectedSession = new SessionObject(user);

        EasyMock.expect(this.userDaoMock.getSession(expectedSession.getSessionToken()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSessionByToken(expectedSession.getSessionToken());
        Assert.assertEquals("The session is valid, thus we should get it.", expectedSession, returnedSession);
    }

    @Test
    public void getSessionByTokenInvalid() {
        final User user = this.makeDummyUser("sbti");
        final SessionObject expectedSession = new SessionObject(user);
        expectedSession.setExperiationDate(expectedSession.getExperiationDate().minusDays(2));

        EasyMock.expect(this.userDaoMock.getSession(expectedSession.getSessionToken()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSessionByToken(expectedSession.getSessionToken());
        Assert.assertNull("The session is invalid, thus we should get null.", returnedSession);
    }

    @Test
    public void getSessionByTokenNonexistent() {
        final User user = this.makeDummyUser("sbtn");
        final String dummyToken = "foobar";

        EasyMock.expect(this.userDaoMock.getSession(dummyToken))
                .andReturn(null);
        EasyMock.replay(this.userDaoMock);

        final SessionObject returnedSession = userService.getUserSessionByToken(dummyToken);
        Assert.assertNull("The session is nonexistent, thus we should get null.", returnedSession);
    }

    @Test
    public void isSessionByUsernameValid() {
        final User user = this.makeDummyUser("isbuv");
        final SessionObject expectedSession = new SessionObject(user);

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSession(user.getUsername());
        Assert.assertTrue("The session is valid, thus we should get true.", result);
    }

    @Test
    public void isSessionByUsernameInvalid() {
        final User user = this.makeDummyUser("isbui");
        final SessionObject expectedSession = new SessionObject(user);
        expectedSession.setExperiationDate(expectedSession.getExperiationDate().minusDays(2));

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSession(user.getUsername());
        Assert.assertFalse("The session is invalid, thus we should get false.", result);
    }

    @Test
    public void isSessionByUsernameNonexistent() {
        final User user = this.makeDummyUser("isbun");

        EasyMock.expect(this.userDaoMock.getSessionByUsername(user.getUsername()))
                .andReturn(null);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSession(user.getUsername());
        Assert.assertFalse("The session is nonexistent, thus we should get false.", result);
    }

    @Test
    public void isSessionByTokenValid() {
        final User user = this.makeDummyUser("isbtv");
        final SessionObject expectedSession = new SessionObject(user);

        EasyMock.expect(this.userDaoMock.getSession(expectedSession.getSessionToken()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSessionByToken(expectedSession.getSessionToken());
        Assert.assertTrue("The session is valid, thus we should get true.", result);
    }

    @Test
    public void isSessionByTokenInvalid() {
        final User user = this.makeDummyUser("isbti");
        final SessionObject expectedSession = new SessionObject(user);
        expectedSession.setExperiationDate(expectedSession.getExperiationDate().minusDays(2));

        EasyMock.expect(this.userDaoMock.getSession(expectedSession.getSessionToken()))
                .andReturn(expectedSession);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSessionByToken(expectedSession.getSessionToken());
        Assert.assertFalse("The session is invalid, thus we should get false.", result);
    }

    @Test
    public void isSessionByTokenNonexistent() {
        final User user = this.makeDummyUser("isbtn");
        final String dummyToken = "foobar";

        EasyMock.expect(this.userDaoMock.getSession(dummyToken))
                .andReturn(null);
        EasyMock.replay(this.userDaoMock);

        final boolean result = userService.isUserSessionByToken(dummyToken);
        Assert.assertFalse("The session is nonexistent, thus we should get false.", result);
    }

    private User makeDummyUser(String code) {
        return new User(code, User.Gender.FEMALE, false, userService.encryptString("P"+code+"1"), LocalDate.of(1993, 1, 1), code+"@session.tests.example.com");
    }
}
