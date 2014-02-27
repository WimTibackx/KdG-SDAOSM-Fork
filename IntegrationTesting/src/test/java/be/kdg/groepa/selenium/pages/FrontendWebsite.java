package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by delltvgateway on 2/27/14.
 */
public class FrontendWebsite {

    public static LoginPage enterAtLogin(RemoteWebDriver rwd) {
        rwd.get("http://localhost:8080/frontend/app/index.html#/login");
        return new LoginPage(rwd);
    }
}
