package com.test.parabank;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.aa.drivers.BaseClass;
import com.aa.drivers.BrowserDriverManager;
import com.aa.utility.ExcelReader;
import com.aa.utility.Library;
import com.aa.utility.ScreenshotUtil;
import com.pom.demoqa.ParaBank;

public class TestParaBank extends BaseClass {
	public final String DATAPROVIDER_NAME="TestData";

	public String excelPath = System.getProperty("user.dir") + "\\ExcelFiles\\LOGIN.xlsx";

	
	
	@Test(dataProvider = DATAPROVIDER_NAME)
	
	public void loginParaBank(int index) throws Exception {
		  String sheetName = "LOGIN";

	        Library.iterationNumberLoggingForDataDriven(index);
	        
	        
	       WebDriver driver = BrowserDriverManager.getDriver();
	       
	       
	       driver.navigate().to("https://parabank.parasoft.com/parabank/index.htm?ConnType=JDBC");
	        ExcelReader excelReader = new ExcelReader(excelPath);

		    String userName = excelReader.getCellData(sheetName, "UserName", index);
		    String pass = excelReader.getCellData(sheetName, "PassWord", index);
		
	        
	        ParaBank paraBankPage=new ParaBank( driver);
	        
	        paraBankPage.enterUsername(userName);
	        paraBankPage.enterPassword(pass);
	        paraBankPage.clickLogin();
	        ScreenshotUtil.captureScreenshot("Login");
	        paraBankPage.clickLogout();
	        
	        ScreenshotUtil.captureScreenshot("LogOut");

	}
	
	
	
	
	@DataProvider(name = DATAPROVIDER_NAME)
	public Object[][] provideIndex() {
		  String sheetName = "LOGIN";
		  ExcelReader excelReader = new ExcelReader(excelPath);

		    String fullName[] = excelReader.getColumnData(sheetName, "UserName");
		
	    int maximumNumberOfData = fullName.length; // You can change this value as needed

	    
	    maximumNumberOfData=2;
	    
	    
	    Object[][] data = new Object[maximumNumberOfData][1];

	    for (int i = 0; i < maximumNumberOfData; i++) {
	        data[i][0] = i + 1; // Excel data usually starts from row 1 (excluding header)
	    }

	    return data;
	}
	
	
}
