package com.test.test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class SwiggyPizzaOrder {

    public static void main(String[] args) throws InterruptedException {

        // Set ChromeDriver path
		WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

        driver.manage().window().maximize();

        // 1. Open Swiggy website
        driver.get("https://www.swiggy.com");

        // 2. Enter location
        WebElement locationInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.id("location"))
        );
        locationInput.sendKeys("Bangalore");
        Thread.sleep(2000);
        locationInput.sendKeys(Keys.ARROW_DOWN);
        locationInput.sendKeys(Keys.ENTER);

        // 3. Search for Pizza
        WebElement searchBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Search']"))
        );
        searchBtn.click();

        WebElement searchInput = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search for restaurants and food']"))
        );
        searchInput.sendKeys("Pizza");
        searchInput.sendKeys(Keys.ENTER);

        // 4. Select first pizza item
        WebElement firstPizza = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("(//div[@data-testid='food-item'])[1]"))
        );
        firstPizza.click();

        // 5. Add extra variants (if customization modal appears)
        try {
            WebElement addExtra = wait.until(
                    ExpectedConditions.elementToBeClickable(By.xpath("//div[text()='Add Item']"))
            );
            addExtra.click();
        } catch (Exception e) {
            System.out.println("No customization available for this item.");
        }

        // 6. View cart
        WebElement viewCart = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='View Cart']"))
        );
        viewCart.click();

        // 7. Proceed to checkout
        WebElement checkout = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Checkout')]"))
        );
        checkout.click();

        // 8. Login and OTP (Manual Step)
        System.out.println("⚠️ Please enter OTP manually to complete the order.");

        // Pause to allow manual OTP entry
        Thread.sleep(30000);

        // Close browser
        driver.quit();
    }
}
