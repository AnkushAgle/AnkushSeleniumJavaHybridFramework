package com.aa.drivers;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.aa.utility.EnvironmentUtils;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DriverFactory {

    // Singleton instance of DriverFactory
    private static DriverFactory DriverFactory = new DriverFactory();

    // Private constructor to prevent instantiation (Singleton Pattern)
    private DriverFactory() {
    }

    /**
     * Creates and returns a WebDriver instance based on the given browser type.
     *
     * @param browserType The name of the browser to instantiate (e.g., "chrome", "firefox", "ie", etc.)
     * @return WebDriver instance for the specified browser
     * 
     * @author ANKUSH 
     * @throws MalformedURLException 
     * @LastModifiedDate  22/07/2025 
     */
    public static WebDriver createDriverInstance(String browserType) throws MalformedURLException {
        WebDriver driver = null;

        // Launch standard Chrome browser with logging
        if (browserType.equalsIgnoreCase("chrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            // Enable browser logging
            java.util.logging.Level logLevel = java.util.logging.Level.ALL;
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, logLevel);
            options.setCapability("goog:loggingPrefs", logPrefs);

            driver = new ChromeDriver(options);
        }

        // Launch Chrome browser in Incognito mode with logging
        else if (browserType.equalsIgnoreCase("Ichrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--incognito"); // Open in incognito
            options.addArguments("--remote-allow-origins=*");

            // Enable browser logging
            java.util.logging.Level logLevel = java.util.logging.Level.ALL;
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, logLevel);
            options.setCapability("goog:loggingPrefs", logPrefs);

            driver = new ChromeDriver(options);
        }

        // Launch Chrome in headless mode (no GUI)
        else if (browserType.equalsIgnoreCase("hchrome")) {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // Run Chrome without UI
            driver = new ChromeDriver(options);
        }

        // Launch Firefox browser
        else if (browserType.equalsIgnoreCase("firefox")) {
            WebDriverManager.firefoxdriver().setup();
            FirefoxOptions options = new FirefoxOptions();
            driver = new FirefoxDriver(options);
        }
    
          
         // Launch Internet Explorer browser
        else if (browserType.equalsIgnoreCase("ie")) {
            WebDriverManager.iedriver().setup();
            InternetExplorerOptions options = new InternetExplorerOptions();
            options.introduceFlakinessByIgnoringSecurityDomains(); // Ignore domain issues
            options.ignoreZoomSettings(); // Ignore zoom level
            driver = new InternetExplorerDriver(options);
        }

        // Launch Microsoft Edge browser in InPrivate mode
        else if (browserType.equalsIgnoreCase("edge")) {
            WebDriverManager.edgedriver().setup();
            EdgeOptions options = new EdgeOptions();
            options.addArguments("--inprivate"); // Open Edge in InPrivate mode
            driver = new EdgeDriver(options);
        }

        // (Optional) Placeholder for Opera browser setup
        else if (browserType.equalsIgnoreCase("opera")) {
            WebDriverManager.operadriver().setup();
            // OperaDriver is commented out or not yet implemented
            // driver = new OperaDriver();
        }

        else if (browserType.equalsIgnoreCase("sauce-chrome")) {
            String sauceUsername = EnvironmentUtils.getValue("SAUCE_USERNAME");
            String sauceAccessKey = EnvironmentUtils.getValue("SAUCE_ACCESS_KEY");
            String sauceUrl = "https://" + sauceUsername + ":" + sauceAccessKey +
                    "@ondemand.eu-central-1.saucelabs.com:443/wd/hub";

            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability("browserName", "chrome");
            caps.setCapability("browserVersion", "latest");
            caps.setCapability("platformName", "Windows 11");

            // ✅ sauce:options block required for W3C
            Map<String, Object> sauceOptions = new HashMap<>();
            sauceOptions.put("name", "BDD Test on Sauce Labs");
            sauceOptions.put("build", "Build-1");
            sauceOptions.put("seleniumVersion", "4.12.1");
            caps.setCapability("sauce:options", sauceOptions);

            driver = new RemoteWebDriver(new URL(sauceUrl), caps);
        }

        // Launch standard Chrome browser with logging
        else {
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            // Enable browser logging
            java.util.logging.Level logLevel = java.util.logging.Level.ALL;
            LoggingPreferences logPrefs = new LoggingPreferences();
            logPrefs.enable(LogType.BROWSER, logLevel);
            options.setCapability("goog:loggingPrefs", logPrefs);

            driver = new ChromeDriver(options);
        }

        
        // Delete all cookies after launching the browser
        if (driver != null) {
            driver.manage().deleteAllCookies();
        }

        return driver;
    }
}
