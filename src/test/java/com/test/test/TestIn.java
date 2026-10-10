package com.test.test;

import java.time.Duration;

import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

public class TestIn {

	

	WebDriver driver;
	
	
	@BeforeMethod
	public void setUp() {
		
		
		WebDriverManager.chromedriver().setup();
		driver=new ChromeDriver();
		
		driver.get("https://www.wikipedia.org/");
		
		driver.manage().window().maximize();
		
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		
		
		
	}
	
	
	
	@Test
	
	public void test001() {
		
		
		WebElement searchboxelement = driver.findElement(By.xpath("//input[@id='searchInput']"));
		searchboxelement.sendKeys("Selenium_(software)");
		
		
		
		WebElement searchButton = driver.findElement(By.xpath("//button[@type='submit']"));

		searchButton.click();
		
		Actions act=new Actions(driver);
		
		act.contextClick().click(searchButton).perform();
		
		
		
		
		
		
	//	=driver.findElement(By.xpath("//th[text()='Initial release']/parent::tr//td")).get;
		
	
	}
	
}
