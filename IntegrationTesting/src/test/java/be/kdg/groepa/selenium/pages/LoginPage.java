package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class LoginPage extends Page {
    public LoginPage(RemoteWebDriver rwd) {
        super(rwd);
    }

    public RegisterPage navigateRegisterPage() {
        super.rwd.findElementByCssSelector("#loginIcons #registerIcon").click();
        return new RegisterPage(this.rwd);
    }
}
