
/* Subversion
*
* Project Application Development
* Karel de Grote-Hogeschool
* 2013-2014
*
 */


package be.kdg.groepa;
import be.kdg.groepa.model.User;
import be.kdg.groepa.persistence.api.UserDao;
import be.kdg.groepa.service.api.UserService;
import junit.framework.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.time.LocalDate;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("file:src/main/webapp/WEB-INF/mvc-dispatcher-servlet.xml")
public class RegisterTest {

    @Autowired
    private UserService userService;


    @Test
    public void registerNoCarUser()
    {
        try {
            userService.addUser(new User("Wimmetje", User.Gender.MALE, false, "testPassword", LocalDate.of(1993, 10, 20), "test@user.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
