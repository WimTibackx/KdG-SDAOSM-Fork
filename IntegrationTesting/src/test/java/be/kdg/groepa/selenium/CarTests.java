package be.kdg.groepa.selenium;

import be.kdg.groepa.selenium.pages.AddCarPage;
import be.kdg.groepa.selenium.pages.FrontendWebsite;
import be.kdg.groepa.selenium.pages.ProfilePage;
import be.kdg.groepa.selenium.pages.RegisterPage;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;

/**
 * Created by Thierry on 27/02/14.
 */
public class CarTests {
    private static boolean setup = false;

    @Before
    public void init(){
        if (setup) return;
        Helper.setup();
        setup = true;
    }

    //TODO: De CANCEL knop werkt niet. Ineens de CSS fixen...
    // AddCar
    // MissingData
    // WrongConsumptionPattern
    // NoImage (required)
    // SkipImage
    // CancelRetryImage
    // CancelSkipImage
    // TxtForImage

    @Test
    public void addCar(){
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();

        doCarForm(acp, "SeleniumA");

        AddCarPage.CiForm ciForm = acp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getContinueButton().click();
        Helper.wait(Helper.WAIT_SHORT);

        Assert.assertTrue("The car should've been found", pp.hasCar("SeleniumA"));
        driver.close();
    }

    private void doCarForm(AddCarPage acp, String name) {
        AddCarPage.CdForm cdForm = acp.getCdForm();
        cdForm.getBrandField().sendKeys(name);
        cdForm.getTypeField().sendKeys("Foobar");
        cdForm.getFueltypeField().selectByIndex(2);
        cdForm.getConsumptionField().sendKeys("8.1");
        cdForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_SHORT);
    }

    @Test
    public void missingData() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        AddCarPage.CdForm cdForm = acp.getCdForm();
        cdForm.getBrandField().sendKeys("SeleniumB");
        Assert.assertTrue(cdForm.errorRequired().isDisplayed());
        driver.close();
    }

    @Test
    public void wrongConsumptionPattern() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        AddCarPage.CdForm cdForm = acp.getCdForm();
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

    @Test
    public void noImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();

        AddCarPage.CdForm cdForm = acp.getCdForm();
        cdForm.getBrandField().sendKeys("SeleniumC");
        cdForm.getTypeField().sendKeys("Foobar");
        cdForm.getFueltypeField().selectByIndex(2);
        cdForm.getConsumptionField().sendKeys("8.1");
        cdForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_SHORT);

        AddCarPage.CiForm ciForm = acp.getCiForm();
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue(ciForm.errorUnknown().isDisplayed());
        driver.close();
    }

    @Test
    public void trySkipImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        this.doCarForm(acp,"SeleniumD");

        AddCarPage.CiForm ciForm = acp.getCiForm();
        ciForm.getSkipButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue("The car should've been found", pp.hasCar("SeleniumD"));
        driver.close();
    }

    @Test
    public void tryCancelSkipImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        this.doCarForm(acp,"SeleniumE");

        AddCarPage.CiForm ciForm = acp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getCarPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        ciForm.getCancelButton().click();
        Helper.wait(Helper.WAIT_SHORT);
        ciForm.getSkipButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue("The car should've been found", pp.hasCar("SeleniumE"));
        driver.close();
    }

    @Test
    public void tryCancelReuploadImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        this.doCarForm(acp,"SeleniumF");

        AddCarPage.CiForm ciForm = acp.getCiForm();
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
        Assert.assertTrue("The car should've been found", pp.hasCar("SeleniumF"));
        driver.close();
    }

    @Test
    public void tryTxtForImage() {
        RemoteWebDriver driver = Helper.startFirefox();
        Helper.doLogin(driver);
        ProfilePage pp = new ProfilePage(driver);
        AddCarPage acp = pp.navigateAddCarPage();
        this.doCarForm(acp,"SeleniumG");
        AddCarPage.CiForm ciForm = acp.getCiForm();
        ciForm.getFileField().sendKeys(Helper.getTxtPath());
        ciForm.getSubmitButton().click();
        Helper.wait(Helper.WAIT_LONG);
        Assert.assertTrue(ciForm.errorUnknown().isDisplayed());
        driver.close();
    }
}
