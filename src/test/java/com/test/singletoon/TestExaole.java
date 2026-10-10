package com.test.singletoon;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
public class TestExaole {

	WebDriver driver;
	
	@BeforeMethod

	public void setUp() {
		
		WebDriverManager.chromedriver().setup();
		
		driver=new ChromeDriver();
		
		driver.get("https://www.youtube.com/");
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		
		
		
		
	}
	
	
	@Test
	public void m1() {
		
		
	}
	

	
	@BeforeMethod

	public void tearDown() {
		
		
		driver.close();
		
		
	}
	
	
	
}
