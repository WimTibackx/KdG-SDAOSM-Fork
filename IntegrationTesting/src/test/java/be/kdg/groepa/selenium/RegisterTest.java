package be.kdg.groepa.selenium;

import be.kdg.groepa.selenium.pages.FrontendWebsite;
import be.kdg.groepa.selenium.pages.ProfilePage;
import be.kdg.groepa.selenium.pages.RegisterPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;

/**
 * Created by Thierry on 24/02/14.
 */
public class RegisterTest {
    private static boolean setup = false;

    @Before
    public void init(){
        if (setup) return;
        Helper.setup();
        setup = true;
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
        this.doCdForm(rp, "Ford");

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getContinueButton().click();
        Helper.wait(Helper.WAIT_SHORT);

        this.assertOnProfile(driver);

        driver.close();
    }

    private void doCdForm(RegisterPage rp, String brand) {
        RegisterPage.CdForm cdForm = rp.getCdForm();
        cdForm.getBrandField().sendKeys(brand);
        cdForm.getTypeField().sendKeys("Fiesta");
        cdForm.getFueltypeField().selectByIndex(2);
        cdForm.getConsumptionField().sendKeys("8.1");
        cdForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_SHORT);
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
        uiForm.getFileField().sendKeys(Helper.getAvatarPath());
        uiForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        uiForm.getCancelButton().click();
        Helper.wait(Helper.WAIT_SHORT);
        uiForm.getSkipButton().click();
        Helper.wait(Helper.WAIT_SHORT);

        this.assertOnProfile(driver);
        driver.close();
    }

    @Test
    public void tryCancelReuploadAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();
        this.udFormPassenger(rp, "NewTestUser@trycancelreuploadavatar.register.example.com");
        Helper.wait(Helper.WAIT_SHORT);
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(Helper.getAvatarPath());
        uiForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        uiForm.getCancelButton().click();
        Helper.wait(Helper.WAIT_SHORT);
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
        Helper.wait(Helper.WAIT_SHORT);
        RegisterPage.UdForm udForm = rp.getUdForm();
        udForm.getUsernameField().sendKeys("NewTestUser@trynonexistingdate.register.example.com");
        udForm.getPasswordField().sendKeys("Password1");
        udForm.getNameField().sendKeys("Test User Name");
        udForm.getGenderFemaleField().click();
        udForm.getSmokerTrueField().click();
        udForm.getAccounttypePassengerField().click();
        udForm.getDobField().sendKeys("1993-02-40");
        udForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_SHORT);
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
        Helper.wait(Helper.WAIT_SHORT);
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
        Helper.wait(Helper.WAIT_SHORT);
    }

    @Test
    public void tryUploadingTextForAvatar() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormPassenger(rp, "foo@uploadtxt.register.example.com");
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(new File("src/test/java/resources/foo.txt").getAbsolutePath());
        uiForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
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
        Helper.wait(Helper.WAIT_SHORT);
        Assert.assertTrue(cdForm.errorRequired().isDisplayed());

        driver.close();
    }

    @Test
    public void noImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "registerA@selenium.example.com");
        this.skipAvatar(rp);
        this.doCdForm(rp, "Ford");

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue(ciForm.errorUnknown().isDisplayed());
        driver.close();
    }

    @Test
    public void trySkipImage() {
        final String brand = "SeleniumH";
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "registerB@selenium.example.com");
        this.skipAvatar(rp);
        this.doCdForm(rp, brand);

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getSkipButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue("The car should've been found", pp.hasCar(brand));
        driver.close();
    }

    @Test
    public void tryCancelSkipImage() {
        final String brand = "SeleniumI";
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "registerC@selenium.example.com");
        this.skipAvatar(rp);
        this.doCdForm(rp, brand);

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getCancelButton().click();
        Helper.wait(Helper.WAIT_SHORT);
        ciForm.getSkipButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue("The car should've been found", pp.hasCar(brand));
        driver.close();
    }

    @Test
    public void tryCancelReuploadImage() {
        final String brand = "SeleniumJ";
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "registerD@selenium.example.com");
        this.skipAvatar(rp);
        this.doCdForm(rp, brand);

        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getCancelButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getContinueButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue("The car should've been found", pp.hasCar(brand));
        driver.close();
    }

    @Test
    public void tryTxtForImage() {
        final String brand = "SeleniumK";
        RemoteWebDriver driver = Helper.startFirefox();
        RegisterPage rp = FrontendWebsite.enterAtLogin(driver).navigateRegisterPage();

        this.udFormDriver(rp, "registerE@selenium.example.com");
        this.skipAvatar(rp);
        this.doCdForm(rp, brand);
        Helper.wait(2000); //The thing hadn't been loaded in time...
        RegisterPage.CiForm ciForm = rp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getTxtPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue(ciForm.errorUnknown().isDisplayed());
        driver.close();
    }

    private void uploadAvatar(RegisterPage rp, RemoteWebDriver driver) {
        RegisterPage.UiForm uiForm = rp.getUiForm();
        uiForm.getFileField().sendKeys(Helper.getAvatarPath());
        uiForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        uiForm.getContinueButton().click();
        Helper.wait(Helper.WAIT_SHORT);
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
        Helper.wait(Helper.WAIT_SHORT);
    }

    private void udFormDriver(RegisterPage rp, String email) {
        RegisterPage.UdForm udForm = this.udFormBaseuser(rp, email);
        udForm.getAccounttypeDriverField().click();
        udForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_SHORT);
    }

    private void assertOnProfile(RemoteWebDriver driver) {
        ProfilePage pp = new ProfilePage(driver);
        Assert.assertTrue(pp.assertCurrentlyOnProfile("Test User Name"));
    }
}
