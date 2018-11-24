package com.toolbox.selenium;

import com.google.common.io.Files;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by: aviat
 * Date: 5/6/2015
 */
public class WebDriverFactoryImpl implements WebDriverFactory {

    private static final Logger log = LoggerFactory.getLogger(WebDriverFactoryImpl.class);

    private static final String CHROME_DRIVER_EXE = "chromedriver.exe";
    private static final String FIREFOX_DRIVER_EXE = "geckodriver.exe";
    private static final String CHROME_DRIVER_PROP = "webdriver.chrome.driver";
    private static final String FIREFOX_DRIVER_PROP = "webdriver.gecko.driver";
    private static final String EXPLORER_DRIVER_PROP = "webdriver.ie.driver";
    private static final String EXPLORER_DRIVER_EXE = "IEDriverServer.exe";
    private static final String DRIVER_FOLDER = "C:\\driver";


    public long WAIT_TIME =10;

    @Override
    public WebDriver createWebDriver(BrowserType browserType) {
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                initBrowserWebDriver(browserType);
                driver = new ChromeDriver(getDefaultChromeCapabilities());
                break;
            case HTML_UNIT_DRIVER:
                driver = new HtmlUnitDriver(true);
                break;
            case FIREFOX:
                initBrowserWebDriver(browserType);
                driver = new FirefoxDriver(getDefaultFFOptions());
                break;
            case IE:
                initBrowserWebDriver(browserType);
                driver = new InternetExplorerDriver(getDefaultIECapabilities());
                break;
        }

        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.switchTo().window(driver.getWindowHandle());
        log.info("Successfully initialised '" + browserType.name() + "' browser.");
        return driver;
    }

    @Override
    public WebDriver createWebDriver(BrowserType browserType, DesiredCapabilities desiredCapabilities) {
        WebDriver driver = null;

        log.info(String.format("Initializing driver for %s", browserType.getName()));
        switch (browserType) {
            case CHROME:
                initBrowserWebDriver(browserType);
                driver = new ChromeDriver(desiredCapabilities);
                break;
            case HTML_UNIT_DRIVER:
                driver = new HtmlUnitDriver(desiredCapabilities);
                break;
            case FIREFOX:
                initBrowserWebDriver(browserType);
                driver = new FirefoxDriver(desiredCapabilities);
                break;
            case IE:
                initBrowserWebDriver(browserType);
                driver = new InternetExplorerDriver(desiredCapabilities);
                break;
        }

        driver.manage().timeouts().implicitlyWait(WAIT_TIME, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        driver.switchTo().window(driver.getWindowHandle());
        log.info("Successfully initialised '" + browserType.name() + "' browser.");
        return driver;
    }

    private FirefoxOptions getDefaultFFOptions() {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        return firefoxOptions.merge(getDefaultFFCapabilities());
    }

    private DesiredCapabilities getDefaultFFCapabilities() {
        DesiredCapabilities dc = DesiredCapabilities.firefox();
        dc.setCapability(FirefoxDriver.PROFILE, createDefaultFirefoxProfile());
        return dc;
    }

    private DesiredCapabilities getDefaultIECapabilities() {
        DesiredCapabilities dc = DesiredCapabilities.internetExplorer();
        dc.setCapability("disable-popup-blocking", true);
        dc.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
        dc.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
        dc.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
        dc.setCapability(InternetExplorerDriver.NATIVE_EVENTS, true);
        dc.setCapability(InternetExplorerDriver.ENABLE_PERSISTENT_HOVERING, false);
        dc.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, false);

        log.info(dc.toString());
        return dc;
    }

    private DesiredCapabilities getDefaultChromeCapabilities() {
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", "C:\\downloads");
        prefs.put("plugins.always_open_pdf_externally", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("disable-infobars", "--start-maximized");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);

        return capabilities;
    }

    private FirefoxProfile createDefaultFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();

        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.manager.showWhenStarting", false);
        profile.setPreference("browser.download.dir", "C:\\downloads");
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/csv, application/zip, application/pdf");

        profile.setPreference("pdfjs.disabled", true);
        profile.setPreference("plugin.scan.Acrobat", "99.0");
        profile.setPreference("plugin.scan.plid.all", false);

        return profile;
    }

    private void initBrowserWebDriver(BrowserType browserType) {
        String fileName;
        String driverProperty;
        String driverFolder;
        switch (browserType) {
            case CHROME:
                fileName = CHROME_DRIVER_EXE;
                driverProperty = CHROME_DRIVER_PROP;
                driverFolder = browserType.getName();
                break;
            case FIREFOX:
                fileName = FIREFOX_DRIVER_EXE;
                driverProperty = FIREFOX_DRIVER_PROP;
                driverFolder = browserType.getName();
                break;
            case IE:
                fileName = EXPLORER_DRIVER_EXE;
                driverProperty = EXPLORER_DRIVER_PROP;
                driverFolder = "IE";
                break;
            default:
                log.error(String.format("Invalid or no browser type specified %s", browserType.toString()));
                return;
        }

        try {
            String driverPath = String.format(DRIVER_FOLDER + "\\%s", fileName);
            File targetFile = new File(driverPath);

            Files.createParentDirs(targetFile);
            Files.touch(targetFile);
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(String.format("com/cx/automation/adk/selenium/%s/%s", driverFolder, fileName));
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(IOUtils.toByteArray(inputStream));
            outStream.close();

            System.setProperty(driverProperty, driverPath);
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error initializing driver");
        }
    }
}
