package com.toolbox.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Created by: aviat
 * Date: 5/6/2015
 */
public interface WebDriverFactory {

    /**
     * Get default WebDriver instance;
     *
     * @param browserType BrowserType enum (firefox, etc..)
     * @return the desired browser
     */
    public WebDriver createWebDriver(BrowserType browserType);

    /**
     * Get WebDriver instance with capabilities;
     *
     * @param browserType         BrowserType enum (firefox, etc..)
     * @param desiredCapabilities selenium DesiredCapabilities
     * @return the desired browser with his desired capabilities.
     */
    public WebDriver createWebDriver(BrowserType browserType, DesiredCapabilities desiredCapabilities);

}
