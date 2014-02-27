package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class ProfilePage extends Page {
    public ProfilePage(RemoteWebDriver rwd) {
        super(rwd);
    }

    public WebElement getProfileheader() { return super.rwd.findElementByCssSelector("#profileheader"); }

    public boolean assertCurrentlyOnProfile(String username) {
        return this.getProfileheader().getText().equals("Welkom, "+username);
    }
}
