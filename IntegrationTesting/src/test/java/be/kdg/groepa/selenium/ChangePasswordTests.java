package be.kdg.groepa.selenium;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by Thierry on 27/02/14.
 */
public class ChangePasswordTests {
    private static boolean setup = false;

    @Before
    public void init(){
        if (setup) return;
        Helper.setup();
        setup = true;
    }

    @Test
    public void succesPassChange(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("changePassword");
        element.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("current");
        element.sendKeys("Succes1");

        element = driver.findElementByName("new");
        element.sendKeys("NewPass1");

        element = driver.findElementByName("repeating");
        element.sendKeys("NewPass1");

        element = driver.findElementByName("submit");
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("current");
        element.clear();
        element.sendKeys("NewPass1");

        element = driver.findElementByName("new");
        element.clear();
        element.sendKeys("Succes1");

        element = driver.findElementByName("repeating");
        element.clear();
        element.sendKeys("Succes1");

        element = driver.findElementByName("submit");
        element.click();

        element = driver.findElementByName("message");
        System.out.println( element.getText());
        assertEquals("Wachtwoord is succesvol aangepast", element.getText());

        driver.close();
    }

    @Test
    public void oldPasswordWrong(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("changePassword");
        element.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("current");
        element.sendKeys("Fail1");

        element = driver.findElementByName("new");
        element.sendKeys("NewPass1");

        element = driver.findElementByName("repeating");
        element.sendKeys("NewPass1");

        element = driver.findElementByName("submit");
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("errorMessage");
        assertEquals("Het oude wachtwoord is onjuist", element.getText());

        driver.close();
    }

    @Test
    public void newPasswordsDontMatch(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("changePassword");
        element.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("current");
        element.sendKeys("Succes1");

        element = driver.findElementByName("new");
        element.sendKeys("NewPass1");

        element = driver.findElementByName("repeating");
        element.sendKeys("NewPass2");

        element = driver.findElementByName("submit");
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("errorMessage");
        assertEquals("Wachtwoorden komen niet overeen.", element.getText());

        driver.close();
    }

    @Test
    public void newPasswordsWrongFormat(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("changePassword");
        element.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("current");
        element.sendKeys("Succes1");

        element = driver.findElementByName("new");
        element.sendKeys("Fail");

        element = driver.findElementByName("repeating");
        element.sendKeys("Fail");

        element = driver.findElementByName("submit");
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("errorMessage");
        assertEquals("Wachtwoord formaat is verkeerd, 1 Hoofdletter, 1 kleine letter en tussen de 7 en 30 lang", element.getText());

        driver.close();
    }

    @Test
    public void serviceUnavailable(){
        FirefoxDriver driver = new FirefoxDriver();
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("changePassword");
        element.click();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("submit");
        element.click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByName("errorMessage");
        assertEquals("Onze service is niet beschikbaar, vul alle velden zeker in", element.getText());

        driver.close();
    }
}


