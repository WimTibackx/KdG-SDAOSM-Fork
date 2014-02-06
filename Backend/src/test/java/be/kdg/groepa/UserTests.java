package be.kdg.groepa;

import be.kdg.groepa.model.SessionObject;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.persistence.impl.UserDaoMap;
import be.kdg.groepa.service.api.UserService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
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
        userService.setUserDao(new UserDaoMap());
        assertTrue("Succesful login", userService.checkLogin("Thierry", "succes"));
    }

    @Test
    public void failLogin(){
        userService.setUserDao(new UserDaoMap());
        assertFalse("Fail login", userService.checkLogin("Thierry", "fail"));
    }

    @Test
    public void checkLoginCallsSession() {
        SessionObject session = new SessionObject("Thierry");
        UserDao daoMock = createMock(UserDao.class);
        userService.setUserDao(daoMock);
        expect(daoMock.getUser("Thierry")).andReturn(new User("Thierry","succes"));
        daoMock.createSession(session);
        replay(daoMock);

        userService.checkLogin("Thierry","succes");
        verify(daoMock);
    }





}
