import be.kdg.groepa.selenium.pages.FrontendWebsite;
import be.kdg.groepa.selenium.pages.LoginPage;
import be.kdg.groepa.selenium.pages.ProfilePage;
import be.kdg.groepa.selenium.pages.RegisterPage;
import com.ibatis.common.jdbc.ScriptRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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
        RemoteWebDriver driver = Helper.startFirefox();

        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("NewTestUser@testRegister.com");
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getDobField().sendKeys("1996-07-19");
        udForm.getAccounttypePassengerField().click();
        udForm.getSubmitButton().click();
        Helper.wait(2000);

        this.uploadAvatar(rp);

        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue(pp.assertCurrentlyOnProfile("Test User Name"));

        //TODO: Assert that the image has been uploaded

        driver.close();
    }

    @Test
    public void succesDriverRegister() {
        RemoteWebDriver driver = Helper.startFirefox();

        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("NewTestUser@driver.register.example.com");
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getDobField().sendKeys("1996-07-19");
        udForm.getAccounttypeDriverField().click();
        udForm.getSubmitButton().click();
        Helper.wait(2000);

        this.uploadAvatar(rp);

        RegisterPage.CdForm cdForm = rp.getCdForm();
        cdForm.getBrandField().sendKeys("Ford");
        cdForm.getTypeField().sendKeys("Fiesta");
        cdForm.getFueltypeField().selectByIndex(2);
        cdForm.getConsumptionField().sendKeys("8.1");
        cdForm.getSubmitButton().click();
        Helper.wait(2000);

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());
        ciForm.getSubmitButton().click();
        Helper.wait(8000);
        ciForm.getContinueButton().click();
        Helper.wait(2000);

        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue(pp.assertCurrentlyOnProfile("Test User Name"));
        //TODO: Assert that the image has been uploaded
        //TODO: Use an image other than the avatar

        driver.close();
    }

    private void uploadAvatar(RegisterPage rp) {
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(8000);
        uiForm.getContinueButton().click();
        Helper.wait(2000);
    }

}
