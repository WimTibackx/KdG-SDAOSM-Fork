package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by delltvgateway on 2/28/14.
 * Copied this because the html should be allowed to vary.
 */
public class AddCarPage extends Page {

    private static final String SEL_CDFORM="form[name='cdForm']";
    private static final String SEL_CIFORM="form[name='ciForm']";
    private static final String SEL_ERRPLH=" span.error[ng-show='%s']";

    public AddCarPage(RemoteWebDriver rwd) {
        super(rwd);
    }

    public CdForm getCdForm() { return new CdForm(super.rwd); }
    public CiForm getCiForm() { return new CiForm(super.rwd); }

    public static class CdForm extends Page {

        public CdForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getBrandField() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+" input[name='brand']"); }
        public WebElement getTypeField() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+" input[name='type']"); }
        public Select getFueltypeField() { return new Select(super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+" select[name='fueltype']")); }
        public WebElement getConsumptionField() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+" input[name='consumption']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+" input[name='submit']"); }

        public WebElement errorConsumptionPattern() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+String.format(AddCarPage.SEL_ERRPLH,"cdForm.consumption.$error.pattern")); }
        public WebElement errorRequired() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CDFORM+String.format(AddCarPage.SEL_ERRPLH,"cdForm.$error.required")); }
    }

    public static class CiForm extends Page {

        public CiForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getFileField() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+" input[name='file']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+" input[name='submit']"); }
        public WebElement getContinueButton() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+" input[name='continue']"); }
        public WebElement getSkipButton() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+" input[name='skip']"); }
        public WebElement getCancelButton() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+" input[name='cancel']"); }

        public WebElement errorUnknown() { return super.rwd.findElementByCssSelector(AddCarPage.SEL_CIFORM+String.format(AddCarPage.SEL_ERRPLH,"ciForm.$error.unknown")); }
    }

}
