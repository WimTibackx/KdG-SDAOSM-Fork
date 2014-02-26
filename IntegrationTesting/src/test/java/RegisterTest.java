import com.ibatis.common.jdbc.ScriptRunner;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.DriverManager;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thierry on 24/02/14.
 */
public class RegisterTest {
    private static boolean setup = false;

    @Before
    public void init(){
        if(!setup){
            String script = "src/test/resources/Query.sql";
            FirefoxDriver driver = new FirefoxDriver();
            driver.manage().window().setSize(new Dimension(1024, 860));
            driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
            driver.get("http://localhost:8080/frontend/app/index.html#/login");
            WebElement element = driver.findElementByName("username");
            element.sendKeys("profile@test.com");

            element = driver.findElementByName("password");
            element.sendKeys("Succes1");

            element = driver.findElementByName("login");
            element.click();
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.close();
            try {

                Class.forName("com.mysql.jdbc.Driver").newInstance();
                new ScriptRunner(DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/groepA", "groepA", "groepA"), false, false)
                        .runScript(new BufferedReader(new FileReader(script)));
            } catch (Exception e) {
                System.err.println(e);
            }
            setup = true;
        }

    }


    @Test
    public void succesPassengerRegister(){
        FirefoxProfile firefoxProfile = new ProfilesIni().getProfile("default");
        FirefoxDriver driver = new FirefoxDriver(firefoxProfile);
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://localhost:8080/frontend/app/index.html#/login");

        WebElement element = driver.findElementById("registerIcon");
        element.click();

        element = driver.findElementByCssSelector("#registerform [name='username']");
        element.sendKeys("NewTestUser@testRegister.com");

        element = driver.findElementByCssSelector("#registerform [name='password']");
        element.sendKeys("Password1");

        element = driver.findElementByCssSelector("#registerform [name='name']");
        element.sendKeys("Test User Name");

        element = driver.findElementByCssSelector("input[value='FEMALE']");
        element.click();

        element = driver.findElementByCssSelector("input[value='true']");
        element.click();



        element = driver.findElementByCssSelector("#registerform [name='dob']");
        element.sendKeys("1996-07-19");

        element = driver.findElementByCssSelector("input[value='passenger']");
        element.click();

        element = driver.findElementByName("registreer");
        element.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementById("userimage");
        element.sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());

        element = driver.findElementByCssSelector("#userimageform [name='submit']");
        element.click();

        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        element = driver.findElementByCssSelector("#userimageform [name='continue']");
        element.click();



    }

}
