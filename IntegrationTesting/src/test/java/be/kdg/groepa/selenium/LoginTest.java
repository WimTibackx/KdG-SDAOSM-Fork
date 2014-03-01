package be.kdg.groepa.selenium;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Thierry on 20/02/14.
 */
public class LoginTest {
    private static boolean setup = false;

    @Before
    public void init(){
        if (setup) return;
        Helper.setup();
        setup = true;
    }

    @Test
    public void succesLogin(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        Helper.doLogin(driver);
        WebElement element = driver.findElementById("profileheader"); //TODO Hier iets fout met het element ofzo
        assertTrue(element.getText().equals("Welkom, TestUser"));
        driver.close();

    }


    @Test
    public void navigateProfileWhileNotAuthorized(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/myProfile");

        //ImplicitlyWait doesn't work for angular JS so we need to sleep
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String url = driver.getCurrentUrl();
        assertEquals("User should be redirected to login page when he/she isn't authorized", "http://localhost:8080/frontend/app/index.html#/login", url);

        WebElement element = driver.findElementById("error");
        assertEquals(element.getText(), "Authorization is required");
        driver.close();

    }

    @Test
    public void wrongLoginCredidentails(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");

        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Fail1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        element = driver.findElementById("error");
        assertEquals(element.getText(), "Combination username/password is wrong");
        driver.close();
    }


    @Test
    public void loggingInTwice(){
        RemoteWebDriver driver = Helper.startFirefox();
        /*FirefoxProfile firefoxProfile = new ProfilesIni().getProfile("default");
        FirefoxDriver driver = new FirefoxDriver(firefoxProfile);
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);*/
        driver.get("http://localhost:8080/frontend/app/index.html#/login");

        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        String url = driver.getCurrentUrl();
        assertEquals("User should be redirected to profile page because he was logged in earlier", "http://localhost:8080/frontend/app/index.html#/myProfile", url);
        driver.close();


    }




}
