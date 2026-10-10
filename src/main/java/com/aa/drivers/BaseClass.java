package com.aa.drivers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import com.aa.utility.ExcelReader;
import com.aa.constant.ITestConstants;
import com.aa.constant.ITestGroups;

public abstract class BaseClass implements ITestConstants ,ITestGroups{

    public static int TOTAL_NUMBER_OF_TEST_CASES = 0;
    public static String SCREENSHOTPATH = "";
    public static String TOASTERMASSAGE = ""; 
    protected String TESTLOGINSHEET = "";
    public static String TEST_CASE_STATUS = "PASS";
    public static String TESTURL;
    public static long TEST_START_TIME;
    public static long TEST_END_TIME;
    public static String TESTBROWSER;
    public static String BROWSER_OPEN_OR_CLOSE;
    public static long high;
    public static long low;
    public static long mdm = 10;
    public static int logStepVar = 1;

    @Parameters({"browser", "environment"})
    @BeforeClass(groups = {"smoke","Regression","sanity"}, alwaysRun = true)
    public void beforeClass(@Optional("chrome") String browser,
                            @Optional("openqa") String environment) throws Exception {
        
        String excelpath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";
        ExcelReader Ecel = new ExcelReader(excelpath);
        String sheetloin = "LOGIN";
        TESTURL = Ecel.getCellData(sheetloin, "value", 6);
        BROWSER_OPEN_OR_CLOSE = Ecel.getCellData(sheetloin, "value", 8);

        String LOW = "2";
        low = Long.parseLong(LOW);
        String High = "3";
        high = Long.parseLong(High);

        long implicitlywait = 1;
        String ImplicitlyWait = Ecel.getCellData("WAITS", "ImplicitlyWait", 1);

        TESTBROWSER = browser;

        // ✅ Initialize thread-safe driver
        BrowserDriverManager.initDriver(browser);

        // ✅ Decide URL based on environment
        if (environment.equalsIgnoreCase("NHP")) {
            TESTURL = "https://demoqa.com/";
        } else if (environment.equalsIgnoreCase("main")) {
            TESTURL = "https://demoqa.com/";
        } else if (environment.equalsIgnoreCase("prod")) {
            TESTURL = "https://demoqa.com/";
        } else if (environment.equalsIgnoreCase("openqa")) {
            TESTURL = "https://demoqa.com/";
        }

        BrowserDriverManager.getDriver().get(TESTURL);
        BrowserDriverManager.getDriver().manage().window().maximize();

        if (!ImplicitlyWait.equalsIgnoreCase("NOWAIT")) {
            if (ImplicitlyWait.length() == 1) {
                implicitlywait = Long.parseLong(ImplicitlyWait);
            }
            BrowserDriverManager.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }

    @BeforeMethod(groups = {"smoke","Regression"}, alwaysRun = true)
    public void beforeMethod() {
        System.out.println("------------------------------------------------------------------");
        System.out.println("Test case started in thread: " + Thread.currentThread().getId());
    }

    @AfterMethod(groups = {"smoke","Regression"}, alwaysRun = true)
    public void afterMethod() throws Exception {
        System.out.println("Test case status: " + TEST_CASE_STATUS);

        if (TEST_CASE_STATUS.equalsIgnoreCase("FAIL")) {
            
        	System.out.println("Test Case Fail");
        }

        System.out.println("Test script finished with status: " + TEST_CASE_STATUS);
        System.out.println("------------------------------------------------------------------");
    }

    @AfterClass(groups = {"smoke","Regression","sanity"}, alwaysRun = true)
    public void tearDown() throws Exception {
        Thread.sleep(1000);

        if (BROWSER_OPEN_OR_CLOSE.equalsIgnoreCase("close")) {
            BrowserDriverManager.quitDriver();
        } else {
            BrowserDriverManager.quitDriver();
        }
    }
    
    
    public static WebDriver getDriver() {
		// TODO Auto-generated method stub
		return BrowserDriverManager.getDriver();
	}


}
