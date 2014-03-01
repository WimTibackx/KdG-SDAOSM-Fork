package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.List;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class ProfilePage extends Page {
    public ProfilePage(RemoteWebDriver rwd) {
        super(rwd);
    }

    public WebElement getSectionInline() { return super.rwd.findElementByCssSelector("section#inline"); }

    public WebElement getProfileheader() { return super.rwd.findElementByCssSelector("#profileheader"); }
    public WebElement getAddCarLink() { return super.rwd.findElementByName("addCarLink"); }

    public List<WebElement> getCarBrands() { return super.rwd.findElementsByCssSelector("#carField .brand"); }

    public boolean assertCurrentlyOnProfile(String username) {
        return this.getProfileheader().getText().equals("Welkom, "+username);
    }
    public boolean assertCurrentlyOnProfileByController() {
        return this.getSectionInline().getAttribute("ng-controller").equals("myProfileCtrl");
    }

    public AddCarPage navigateAddCarPage() {
        this.getAddCarLink().click();
        return new AddCarPage(super.rwd);
    }

    public boolean hasCar(String name) {
        boolean found=false;
        for (WebElement we : this.getCarBrands()) {
            if (we.getText().equals(name)) {
                found=true;
                break;
            }
        }
        return found;
    }
}
