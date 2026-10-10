package com.test.test;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.io.FileHandler;

import java.io.File;
import java.time.Duration;
import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TestArray {

	public int testStep=1;
	
	
	public WebDriver driver;
	@BeforeMethod
	public void setUp() {
		
		WebDriverManager.chromedriver().setup();
		
		WebDriver driver=new ChromeDriver();
		
		
		
		this.driver=driver;
		driver.manage().window().maximize();
		driver.get("https://www.amazon.in");
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
	}
	
	@Test
	public void amazone() throws Exception {

		
		//driver.switchTo().
		
		//Alert alt=new Alert(driver);
		
		testStep=1;
		
		try {
			WebElement submitbtn=driver.findElement(By.xpath("//button[@type='submit']"));
			
			isLoadedAndClick(driver,submitbtn,"Continue With Shpoing Button");
			
			
			//submitbtn.click();
		
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
		
		String optionTobeAddInToCart="Apple iPhone 15 (256 GB) - Black";
		
		
		WebElement searchBox = driver.findElement(By.xpath("//input[@id='twotabsearchtextbox']"));
		
		searchBox.sendKeys(optionTobeAddInToCart);
		
		Thread.sleep(2000);
		
		
		WebElement searchButton = driver.findElement(By.xpath("//input[@id='nav-search-submit-button']"));

		isLoadedAndClick(driver,searchButton,"search Button");
	
//	JavascriptExecutor js=(JavascriptExecutor) driver;
	
	
	
	
		//searchButton.click();
		Thread.sleep(2000);
		
		boolean isFound=false;
		List<WebElement> listOfProducts = driver.findElements(By.xpath("//div[@data-cy='title-recipe']//a[starts-with(@class,'a-link-normal')]"));
		
		for(int i=0;i<listOfProducts.size();i++) {
			
			String currentProductName=listOfProducts.get(i).getText();
			
			System.out.println(currentProductName);
			if(currentProductName.equals(optionTobeAddInToCart)) {
				isFound=true;
				listOfProducts.get(i).click();
				
				break;
				
			}
			
		}
		

		TakesScreenshot ts=(TakesScreenshot) driver;
		
		File source = ts.getScreenshotAs(OutputType.FILE);
		String path=System.getProperty("user.dir")+"//newScreenShot.png";
		
		File detination=new File(path);
		
		FileHandler.copy(source, detination);
	
		if(isFound) {
		Thread.sleep(20000);
		
		WebElement addToCartButton = driver.findElement(By.xpath("//div[@id='desktop_qualifiedBuyBox']//input[@id='add-to-cart-button']"));

		isLoadedAndClick(driver,addToCartButton,"Add To Cart Button");
		//js.executeScript("arguments[0].click()", addToCartButton);

		//addToCartButton.click();
		Thread.sleep(2000);
	

		WebElement goToCartButton = driver.findElement(By.xpath("//a[contains(text(),'Go to Cart')]"));
		isLoadedAndClick(driver,addToCartButton,"Go To Cart Button");

	
		JavascriptExecutor js=(JavascriptExecutor)driver;

		
		
		//js.executeScript("arguments[0].click()", goToCartButton);

		//goToCartButton.click();
		Thread.sleep(2000);
	
		}
	
		
		
	}
	
	@AfterMethod
	public void tearDown() throws Exception {
		
		//driver.quit();
	}
	
	
	public void isLoadedAndClick(WebDriver driver,WebElement element,String fieldName) {
		
		boolean isClicked=false;
		
		try {
			
			element.click();

			isClicked=true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!isClicked) {
			
			JavascriptExecutor js=(JavascriptExecutor)driver;
			
			js.executeScript("arguments[0].click()", element);
			
			isClicked=true;
		}
		
		
		
		if(isClicked) {
			
		String massage="Step ( "+testStep+") -->"+" Click Operation SucessFully Complted On Field <b>"+fieldName+" </b> ";	
			Reporter.log(massage);
		}
		
		
		else {
			
			String massage="Step ( "+testStep+") -->"+" Fail To Click   On Field <b>"+fieldName+" </b> ";	
			Reporter.log(massage);

		}
		
		testStep=testStep+1;
	}
	
	
	
}
