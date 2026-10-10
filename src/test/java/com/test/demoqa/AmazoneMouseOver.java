package com.test.demoqa;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import io.github.bonigarcia.wdm.WebDriverManager;

public class AmazoneMouseOver {
	
	
	public static void main(String[] args) throws Exception {
		
		
		WebDriverManager.chromedriver().setup();;
        // WebDriverManager.chromedriver().browserVersion("latest").setup();
         ChromeOptions options = new ChromeOptions();
         options.addArguments("--remote-allow-origins=*");
        
     
      
         WebDriver driver = new ChromeDriver(options);
         
         driver.get("https://www.amazon.in/");
         
         driver.manage().window().maximize();
         
         driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
         
         
         String expanButtonXpath="//button[@aria-label='Expand Account and Lists']";
         
        WebElement expandButtonElement = driver.findElement(By.xpath(expanButtonXpath));
        
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
         
      
         
        for(int i=0;i<5;i++) {
        	
        	expandButtonElement = driver.findElement(By.xpath(expanButtonXpath));
        	String ariaexpanded = expandButtonElement.getAttribute("aria-expanded");
        	
        	
        	if(ariaexpanded==null) {
        		
        		ariaexpanded="false";
        	}
        	
        	if(ariaexpanded.equals("true")) {
        		
        		break;
        	}
        	
        	else {
        		
        		expandButtonElement.click();        		
        	}
        }
         
        
        String listOflinks="//div[@id='nav-flyout-accountList']//a";
        
        
       List<WebElement> list = driver.findElements(By.xpath(listOflinks));
        
       
       String linkTobeClicked="Your Prime Video";
       
       for(int i=0;i<list.size();i++) {
    	   
    	   WebElement element = list.get(i);
    	   
    	   String option=element.getText();
    	   
    	   
    	   if(linkTobeClicked.equals(option)) {
    		   
    		   element.click();
    		   break;
    	   }
    	   
    	   System.out.println(option);
    	   
       }
       
       
        System.out.println();
         Thread.sleep(5000);
         
     
	}

}
