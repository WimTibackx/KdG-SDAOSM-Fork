package be.kdg.groepa.selenium;

import be.kdg.groepa.selenium.pages.FrontendWebsite;
import be.kdg.groepa.selenium.pages.LoginPage;
import com.ibatis.common.jdbc.ScriptRunner;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class Helper {
    public static final int WAIT_SHORT = 5000;
    public static final int WAIT_LONG = 10000;

    public static RemoteWebDriver startFirefox() {
        //FirefoxProfile firefoxProfile = new ProfilesIni().getProfile("default");
        FirefoxDriver driver = new FirefoxDriver(/*firefoxProfile*/);
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return driver;
    }

    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void setup() {
        resetBackend();
        fillDb();
    }

    private static void fillDb() {
        String script = "src/test/resources/Query.sql";
        try {

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            new ScriptRunner(DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/groepA", "groepA", "groepA"), false, false)
                    .runScript(new BufferedReader(new FileReader(script)));
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static void resetBackend() {
        RemoteWebDriver driver = Helper.startFirefox();
        LoginPage lp = FrontendWebsite.enterAtLogin(driver);
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        Helper.wait(Helper.WAIT_SHORT);
        driver.close();
    }

    public static void doLogin(RemoteWebDriver driver) {
        FrontendWebsite.enterAtLogin(driver);
        WebElement element = driver.findElementByName("username");
        element.sendKeys("profile@test.com");

        element = driver.findElementByName("password");
        element.sendKeys("Succes1");

        element = driver.findElementByName("login");
        element.click();
        //ImplicitlyWait doesn't work for angular JS so we need to sleep
        Helper.wait(Helper.WAIT_LONG);
    }

    public static String getAvatarPath() {
        return new File("src/test/resources/avatar.JPG").getAbsolutePath();
    }

    public static String getCarPath() {
        return new File("src/test/resources/car.jpg").getAbsolutePath();
    }

    public static String getTxtPath() {
        return new File("src/test/resources/foo.txt").getAbsolutePath();
    }
}
