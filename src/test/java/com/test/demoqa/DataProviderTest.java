package com.test.demoqa;

import org.openqa.selenium.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTest  implements ItestMy{

	
	
	

    // ThreadLocal for WebDriver
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();

    // Set driver for current thread
    public static void setDriver(WebDriver driverInstance) {
        driver.set(driverInstance);
    }

    // Get driver for current thread
    public static WebDriver getDriver() {
        return driver.get();
    }

    // Clean up after test
    public static void unload() {
        driver.remove();
    }
	
	
	

	@Test(dataProvider=DATA_PROVIDER_NAME)
	public void m1(String userName,String passwrod) {
		
		System.out.println("UserName :: "+userName+"Add Passwrod :: "+passwrod);
	}
	
	@DataProvider(name = DATA_PROVIDER_NAME)
	public Object[][] getData() {
	    return new Object[][] {
	        {"Test001", "Password1"},
	        {"Test002", "Password2"},
	        {"Test003", "Password3"},
	        {"Test004", "Password4"},
	        {"Test005", "Hashim Amla"}
	    };
	}
}
