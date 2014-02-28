package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class RegisterPage extends Page {

    private static final String SEL_UDFORM="form[name='udForm']";
    private static final String SEL_UIFORM="form[name='uiForm']";
    private static final String SEL_CDFORM="form[name='cdForm']";
    private static final String SEL_CIFORM="form[name='ciForm']";
    private static final String SEL_ERRPLH=" span.error[ng-show='%s']";

    public RegisterPage(RemoteWebDriver rwd) {
        super(rwd);
    }

    public UdForm getUdForm() { return new UdForm(super.rwd); }
    public UiForm getUiForm() { return new UiForm(super.rwd); }
    public CdForm getCdForm() { return new CdForm(super.rwd); }
    public CiForm getCiForm() { return new CiForm(super.rwd); }

    public static class UdForm extends Page {

        public UdForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getUsernameField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='username']"); }
        public WebElement getPasswordField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='password']"); }
        public WebElement getNameField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='name']"); }
        public WebElement getGenderMaleField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='gender'][value='MALE']"); }
        public WebElement getGenderFemaleField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='gender'][value='FEMALE']"); }
        public WebElement getSmokerTrueField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='smoker'][value='true']"); }
        public WebElement getSmokerFalseField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='smoker'][value='false']"); }
        public WebElement getDobField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='dob']"); }
        public WebElement getAccounttypePassengerField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='accounttype'][value='passenger']"); }
        public WebElement getAccounttypeDriverField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='accounttype'][value='driver']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+" input[name='registreer']"); }

        public WebElement errorFormParse() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.$error.parse")); }
        public WebElement errorFormUnknown() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.$error.unknown")); }
        public WebElement errorFormPasswPattern() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.password.$error.pattern")); }
        public WebElement errorFormUsernPattern() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.username.$error.pattern")); }
        public WebElement errorFormRequired() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.$error.required")); }
        public WebElement errorFormUsernExists() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UDFORM+String.format(RegisterPage.SEL_ERRPLH,"udForm.username.$error.exists")); }

    }

    public static class UiForm extends Page {

        public UiForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getFileField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+" input[name='file']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+" input[name='submit']"); }
        public WebElement getContinueButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+" input[name='continue']"); }
        public WebElement getSkipButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+" input[name='skip']"); }
        public WebElement getCancelButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+" input[name='cancel']"); }

        public WebElement errorUnknown() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_UIFORM+String.format(RegisterPage.SEL_ERRPLH,"uiForm.$error.unknown")); }
    }

    public static class CdForm extends Page {

        public CdForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getBrandField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+" input[name='brand']"); }
        public WebElement getTypeField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+" input[name='type']"); }
        public Select getFueltypeField() { return new Select(super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+" select[name='fueltype']")); }
        public WebElement getConsumptionField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+" input[name='consumption']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+" input[name='submit']"); }

        public WebElement errorConsumptionPattern() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+String.format(RegisterPage.SEL_ERRPLH,"cdForm.consumption.$error.pattern")); }
        public WebElement errorRequired() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CDFORM+String.format(RegisterPage.SEL_ERRPLH,"cdForm.$error.required")); }
        //cdForm.$error.required
    }

    public static class CiForm extends Page {

        public CiForm(RemoteWebDriver rwd) {
            super(rwd);
        }

        public WebElement getFileField() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CIFORM+" input[name='file']"); }

        public WebElement getSubmitButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CIFORM+" input[name='submit']"); }
        public WebElement getContinueButton() { return super.rwd.findElementByCssSelector(RegisterPage.SEL_CIFORM+" input[name='continue']"); }
    }
}
