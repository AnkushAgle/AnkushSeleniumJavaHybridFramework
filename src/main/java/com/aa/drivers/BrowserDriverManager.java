package com.aa.drivers;


import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;

public class BrowserDriverManager {

    // ThreadLocal variable to hold WebDriver instance per thread
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    private BrowserDriverManager() {
        // prevent instantiation
    }

    /**
     * Initialize driver for the given browser type
     */
    public static void initDriver(String browserType) throws MalformedURLException {
        if (driver.get() == null) {
            driver.set(DriverFactory.createDriverInstance(browserType));
        }
    }

    /**
     * Get driver for current thread
     */
    public static WebDriver getDriver() {
    	
    	//fall Back Added when we haven;t  write the line 
    	
    	 if (driver.get() == null) {
    	        try {
    	            // default to chrome if someone forgot initDriver
    	            initDriver("chrome");
    	        } catch (Exception e) {
    	            throw new RuntimeException("WebDriver was not initialized! Call initDriver() first.", e);
    	        }
    	    }
        return driver.get();
    }

    /**
     * Quit driver for current thread
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
