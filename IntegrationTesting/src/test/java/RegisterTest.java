import be.kdg.groepa.selenium.pages.FrontendWebsite;
import be.kdg.groepa.selenium.pages.ProfilePage;
import be.kdg.groepa.selenium.pages.RegisterPage;
import com.ibatis.common.jdbc.ScriptRunner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
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

        this.udFormPassenger(rp, "NewTestUser@testRegister.com");
        this.uploadAvatar(rp, driver);

        this.assertOnProfile(driver);
        driver.close();
    }

    @Test
    public void succesDriverRegister() {
        RemoteWebDriver driver = Helper.startFirefox();

        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "NewTestUser@driver.register.example.com");

        this.uploadAvatar(rp, driver);

        RegisterPage.CdForm cdForm = rp.getCdForm();
        cdForm.getBrandField().sendKeys("Ford");
        cdForm.getTypeField().sendKeys("Fiesta");
        cdForm.getFueltypeField().selectByIndex(2);
        cdForm.getConsumptionField().sendKeys("8.1");
        cdForm.getSubmitButton().click();
        Helper.wait(2000);

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(new File("src/test/resources/car.jpg").getAbsolutePath());
        ciForm.getSubmitButton().click();
        Helper.wait(8000);
        ciForm.getContinueButton().click();
        Helper.wait(3000);

        this.assertOnProfile(driver);

        driver.close();
    }

    @Test
    public void trySkipAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        this.udFormPassenger(rp, "NewTestUser@tryskipavatar.register.example.com");

        skipAvatar(rp);

        this.assertOnProfile(driver);
        driver.close();
    }

    @Test
    public void tryCancelSkipAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        this.udFormPassenger(rp, "NewTestUser@trycancelskipavatar.register.example.com");

        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(8000);
        uiForm.getCancelButton().click();
        Helper.wait(3000);
        uiForm.getSkipButton().click();
        Helper.wait(3000);

        this.assertOnProfile(driver);
        driver.close();
    }

    @Test
    public void tryCancelReuploadAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        this.udFormPassenger(rp, "NewTestUser@trycancelreuploadavatar.register.example.com");
        Helper.wait(3000);
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(8000);
        uiForm.getCancelButton().click();
        Helper.wait(3000);
        this.uploadAvatar(rp, driver);

        this.assertOnProfile(driver);
        driver.close();
    }

    @Test
    public void tryMissingUDData() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("NewTestUser@trymissinguddata.register.example.com");
        Assert.assertTrue(udForm.errorFormRequired().isDisplayed());
        driver.close();
    }

    @Test
    public void tryNonexistingDate() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        Helper.wait(2000);
        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("NewTestUser@trynonexistingdate.register.example.com");
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getAccounttypePassengerField().click();
        udForm.getDobField().sendKeys("1993-02-40");
        udForm.getSubmitButton().click();
        Helper.wait(4000);
        Assert.assertTrue(udForm.errorFormUnknown().isDisplayed());
        driver.close();
    }

    @Test
    public void tryWrongUsername() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("Foobar");
        Assert.assertTrue(udForm.errorFormUsernPattern().isDisplayed());
        driver.close();
    }

    @Test
    public void tryWrongPassword() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getPasswordField().sendKeys("passwd");
        Assert.assertTrue(udForm.errorFormPasswPattern().isDisplayed());
        driver.close();
    }

    @Test
    public void tryExistingUsername() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("profile@test.com");
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getDobField().sendKeys("1996-07-19");
        udForm.getAccounttypePassengerField().click();
        udForm.getSubmitButton().click();
        Helper.wait(4000);
        Assert.assertTrue(udForm.errorFormUsernExists().isDisplayed());
        driver.close();
    }

    @Test
    public void tryWrongConsumption() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "foo@wrongconsumption.register.example.com");
        skipAvatar(rp);

        RegisterPage.CdForm cdForm = rp.getCdForm();
        cdForm.getConsumptionField().sendKeys("foo");
        Assert.assertTrue(cdForm.errorConsumptionPattern().isDisplayed());
        cdForm.getConsumptionField().clear();
        cdForm.getConsumptionField().sendKeys("5,0");
        Assert.assertTrue(cdForm.errorConsumptionPattern().isDisplayed());
        cdForm.getConsumptionField().clear();
        cdForm.getConsumptionField().sendKeys("5");
        Assert.assertTrue(cdForm.errorConsumptionPattern().isDisplayed());

        driver.close();
    }

    private void skipAvatar(RegisterPage rp) {
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getSkipButton().click();
        Helper.wait(3000);
    }

    @Test
    public void tryUploadingTextForAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormPassenger(rp, "foo@uploadtxt.register.example.com");
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/java/resources/foo.txt").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(8000);
        Assert.assertTrue(uiForm.errorUnknown().isDisplayed());

        driver.close();
    }

    @Test
    public void tryMissingDataCD() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "foo@missingCD.register.example.com");
        this.skipAvatar(rp);
        RegisterPage.CdForm cdForm = rp.getCdForm();
        cdForm.getBrandField().sendKeys("foo");
        cdForm.getSubmitButton().click();
        Helper.wait(3000);
        Assert.assertTrue(cdForm.errorRequired().isDisplayed());

        driver.close();
    }

    private void uploadAvatar(RegisterPage rp, RemoteWebDriver driver) {
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/resources/avatar.JPG").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(8000);
        uiForm.getContinueButton().click();
        Helper.wait(3000);
    }

    private RegisterPage.UdForm udFormBaseuser(RegisterPage rp, String email) {
        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys(email);
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getDobField().sendKeys("1996-07-19");
        return udForm;
    }

    private void udFormPassenger(RegisterPage rp, String email) {
        RegisterPage.UdForm udForm = this.udFormBaseuser(rp, email);
        udForm.getAccounttypePassengerField().click();
        udForm.getSubmitButton().click();
        Helper.wait(4000);
    }

    private void udFormDriver(RegisterPage rp, String email) {
        RegisterPage.UdForm udForm = this.udFormBaseuser(rp, email);
        udForm.getAccounttypeDriverField().click();
        udForm.getSubmitButton().click();
        Helper.wait(4000);
    }

    private void assertOnProfile(RemoteWebDriver driver) {
        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue(pp.assertCurrentlyOnProfile("Test User Name"));
    }
}
