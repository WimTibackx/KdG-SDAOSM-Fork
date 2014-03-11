package be.kdg.groepa.selenium;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

/**
 * Created by Thierry on 11/03/14.
 */
public class MessagingTests {

    private static boolean setup = false;

    @Before
    public void init(){
        if (setup) return;
        Helper.setup();
        setup = true;
    }

    @Test
    public void getInboxValues(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Helper.doLogin(driver,"gio@degruyter.com", "Giovanni69");
        driver.get("http://localhost:8080/frontend/app/index.html#/inbox");

    }
}
