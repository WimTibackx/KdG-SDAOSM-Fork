package be.kdg.groepa.selenium.pages;

import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * Created by delltvgateway on 2/27/14.
 */
public abstract class Page {
    protected RemoteWebDriver rwd;

    public Page(RemoteWebDriver rwd) {
        this.rwd = rwd;
    }
}
