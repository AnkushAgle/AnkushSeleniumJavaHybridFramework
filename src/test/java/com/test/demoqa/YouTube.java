package com.test.demoqa;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.*;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class YouTube {

    WebDriver driver;
    WebDriverWait wait;

    @BeforeClass
    public void launchBrowser() {

    	String browser = "chrome"; // default fallback
    	System.out.println("🧪 Enter browser name (chrome / ichrome / firefox / edge): (waiting 20 sec, default = chrome)");

    	ExecutorService executor = Executors.newSingleThreadExecutor();
    	Future<String> futureInput = executor.submit(() -> new Scanner(System.in).nextLine());

    	try {
    	    browser = futureInput.get(20, TimeUnit.SECONDS); // Wait for user input
    	    if (browser == null || browser.trim().isEmpty()) {
    	        browser = "chrome";
    	    }
    	} catch (TimeoutException e) {
    	    System.out.println("⏰ No input received in 20 seconds. Using default browser: chrome");
    	    browser = "chrome";
    	} catch (Exception e) {
    	    System.out.println("⚠️ Error reading browser input. Using default: chrome");
    	    browser = "chrome";
    	} finally {
    	    executor.shutdownNow();
    	}
    
        if (browser.equalsIgnoreCase("chrome")) {
            Reporter.log("<b>🚀 Launching Chrome browser (Normal Mode)...</b><br/>", true);
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);
        } 
        else if (browser.equalsIgnoreCase("ichrome")) {
            Reporter.log("<b>🚀 Launching Chrome browser (Incognito Mode)...</b><br/>", true);
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--incognito");

            driver = new ChromeDriver(options);
        } 
        else if (browser.equalsIgnoreCase("firefox")) {
            Reporter.log("<b>🚀 Launching Firefox browser...</b><br/>", true);
            WebDriverManager.firefoxdriver().setup();

            driver = new FirefoxDriver();
        } 
        else if (browser.equalsIgnoreCase("edge")) {
            Reporter.log("<b>🚀 Attempting to launch Edge browser...</b><br/>", true);
            try {
                WebDriverManager.edgedriver().setup();
                driver = new org.openqa.selenium.edge.EdgeDriver();
                Reporter.log("<b>✅ Edge browser launched successfully.</b><br/>", true);
            } catch (Exception e) {
                Reporter.log("<b>❌ Failed to launch Edge browser:</b> " + e.getMessage() + "<br/>", true);
                Reporter.log("<b>🔁 Falling back to Chrome browser.</b><br/>", true);
                
                WebDriverManager.chromedriver().setup();
                ChromeOptions fallbackOptions = new ChromeOptions();
                fallbackOptions.addArguments("--remote-allow-origins=*");
                driver = new ChromeDriver(fallbackOptions);
            }
        }
        else {
            Reporter.log("<b>🚀 Launching Chrome browser (Normal Mode)...</b><br/>", true);
            WebDriverManager.chromedriver().setup();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");

            driver = new ChromeDriver(options);
     }

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @Test
    public void playMostViewedSongFromSearch() throws InterruptedException {
    	
    	   String favoriteSong = "Zingat Ajay Atul";
    	Reporter.log("<hr/>", true);
    	Reporter.log("<h3>🎬 <u>Test Case:</u> Play Most Viewed Song from YouTube Search</h3>", true);
    	Reporter.log("<p><b>Description:</b> This test launches YouTube in the browser, plays your favorite song first (" +
    	             "<i>"+favoriteSong+"</i>), and then allows the user to search for additional songs manually. " +
    	             "For each search, the test automatically identifies and plays the video with the highest view count " +
    	             "from the top 5 results. It also skips ads and switches to full-screen mode for better playback experience.</p>", true);
    	Reporter.log("<b>🔁 After playing the default song, the test enters an input loop to accept multiple song requests.</b><br/>", true);
    	Reporter.log("<b>✅ Purpose:</b> To automate YouTube video search and playback intelligently using Selenium WebDriver.<br/>", true);
    	Reporter.log("<hr/>", true);
    	
    	
        Scanner scanner = new Scanner(System.in);

        // ⭐ Play your favorite song "Zingat Ajay Atul" first
     
        Reporter.log("<hr/><b>🎵 Playing your favorite song first:</b> <i>" + favoriteSong + "</i><br/>", true);
        searchAndPlayMostViewedVideo(favoriteSong);

        // 🔁 Start loop to let user play more songs manually
        for (int attempt = 1; attempt <= 100; attempt++) {

            Reporter.log("<hr/><b>🎧 Attempt " + attempt + "</b><br/>", true);

            System.out.print(
                "\n🎧 Attempt " + attempt +
                ": Enter the name of the song you want to play, or type 'Skip' to end the program ➡ "
            );

            String songName = scanner.nextLine();

            // 🚪 Exit the loop if user types "Skip"
            if (songName.equalsIgnoreCase("Skip")) {
                Reporter.log("🛑 <b>Program skipped by user.</b> Exiting the loop.<br/><hr/>", true);
                return;
            }

            // 🎬 Play the song with the highest view count
            searchAndPlayMostViewedVideo(songName);
        }

        scanner.close();
    }

    public void searchAndPlayMostViewedVideo(String songName) throws InterruptedException {
        // Open YouTube and search
        driver.get("https://www.youtube.com/");
        Reporter.log("🔍 <b>Searching on YouTube:</b> <i>" + songName + "</i><br/>", true);

        try {
            WebElement acceptCookiesBtn = driver.findElement(By.xpath("//button[contains(text(),'Accept')]"));
            acceptCookiesBtn.click();
        } catch (Exception ignored) {}

        WebElement searchInput = driver.findElement(By.name("search_query"));
        searchInput.clear();
        searchInput.sendKeys(songName);

        Thread.sleep(1000);

        searchInput = driver.findElement(By.name("search_query"));
        searchInput.sendKeys(Keys.ENTER);

        Thread.sleep(3000); // wait for results

        List<WebElement> videoTitleElements = driver.findElements(By.xpath("//a[@id='video-title']"));
        List<WebElement> videoMetadataElements = driver.findElements(By.xpath("//ytd-video-renderer//div[@id='metadata-line']"));

        if (videoTitleElements.size() == 0 || videoMetadataElements.size() == 0) {
            Reporter.log("❌ <b>No videos found for:</b> " + songName + "<br/>", true);
            return;
        }

        double highestViewCount = 0;
        int indexOfMostViewed = 0;

        Reporter.log("📊 <u>Top 5 Videos (View Count):</u><br/>", true);
        for (int j = 0; j < Math.min(5, videoMetadataElements.size()); j++) {
            String viewsText = videoMetadataElements.get(j).getText().split("\n")[0]; // "6.4M views"
            String rawViews = viewsText.replace("views", "").trim(); // "6.4M"

            double numericViews = convertToNumber(rawViews);
            Reporter.log("&nbsp;&nbsp;&nbsp;&nbsp;📺 Video " + (j + 1) + ": " + rawViews + " ➡ " + numericViews + "<br/>", true);

            if (numericViews > highestViewCount) {
                highestViewCount = numericViews;
                indexOfMostViewed = j;
            }
        }

        WebElement mostViewedVideo = videoTitleElements.get(indexOfMostViewed);
        String playingTitle = mostViewedVideo.getAttribute("aria-label");
        mostViewedVideo.click();
        Reporter.log("▶️ <b>Now playing:</b> <i>" + playingTitle + "</i><br/>", true);

        try {
        	skipAdIfPresent(driver);
        } catch (Exception ignored) {} 
        
        try {
            enterFullscreenRobust(driver, wait);
        } catch (Exception e) {
            Reporter.log("⚠️ <b>Could not enter full-screen:</b> " + e.getMessage() + "<br/>", true);
        }
        Thread.sleep(3000);
    }

    @AfterClass
    public void closeBrowser() {
        Reporter.log("🛑 <b>Closing browser...</b><br/>", true);
        if (driver != null) {
             driver.quit(); // Uncomment when you're ready to close
        }
    }

    /**
     * Converts a YouTube view count string like "6.4M" or "1.2K" into a number.
     */
    public static double convertToNumber(String viewCountText) {
        viewCountText = viewCountText.toUpperCase();

        try {
            if (viewCountText.endsWith("M")) {
                return Double.parseDouble(viewCountText.replace("M", "")) * 1_000_000;
            } else if (viewCountText.endsWith("K")) {
                return Double.parseDouble(viewCountText.replace("K", "")) * 1_000;
            } else if (viewCountText.endsWith("B")) {
                return Double.parseDouble(viewCountText.replace("B", "")) * 1_000_000_000;
            } else {
                return Double.parseDouble(viewCountText.replaceAll(",", ""));
            }
        } catch (Exception e) {
            Reporter.log("⚠️ <b>Could not parse view count:</b> " + viewCountText + "<br/>", true);
            return 0;
        }
    }
    
    
    private void enterFullscreenRobust(WebDriver driver, WebDriverWait wait) {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // 0) Make sure the player is visible and reveal controls by hovering
        WebElement player = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".html5-video-player")));
        new Actions(driver).moveToElement(player, 10, 10).pause(Duration.ofMillis(200)).perform();

        // 1) Try the hotkey 'f' (works even if the button is visually hidden)
        new Actions(driver).sendKeys("f").pause(Duration.ofMillis(300)).perform();
        if (waitForFullscreenState(driver, true, 2)) {
            Reporter.log("🖥️ Entered <b>full-screen</b> via hotkey.<br/>", true);
            return;
        }

        // 2) Try a normal click on the button (use robust selector)
        try {
            WebElement fsBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.cssSelector("button.ytp-fullscreen-button")));
            new Actions(driver).moveToElement(fsBtn).pause(Duration.ofMillis(150)).click().perform();
            if (waitForFullscreenState(driver, true, 2)) {
                Reporter.log("🖥️ Entered <b>full-screen</b> via click.<br/>", true);
                return;
            }
        } catch (Exception ignored) {}

        // 3) Try JS click
        try {
            WebElement fsBtn = driver.findElement(By.cssSelector("button.ytp-fullscreen-button"));
            js.executeScript("arguments[0].click();", fsBtn);
            if (waitForFullscreenState(driver, true, 2)) {
                Reporter.log("🖥️ Entered <b>full-screen</b> via JS click.<br/>", true);
                return;
            }
        } catch (Exception ignored) {}

        // 4) Last resort: Fullscreen API (may be blocked by browser policy, but often works on youtube.com)
        try {
            Object invoked = js.executeScript(
                    "var el = document.querySelector('.html5-video-player') || document.querySelector('video');" +
                    "if (el && el.requestFullscreen) { el.requestFullscreen(); return true; } return false;");
            if (Boolean.TRUE.equals(invoked) && waitForFullscreenState(driver, true, 2)) {
                Reporter.log("🖥️ Entered <b>full-screen</b> via requestFullscreen().<br/>", true);
                return;
            }
        } catch (Exception ignored) {}

        throw new RuntimeException("All strategies to enter fullscreen failed.");
    }

    private boolean waitForFullscreenState(WebDriver driver, boolean desired, int timeoutSeconds) {
        WebDriverWait w = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return w.until(d -> {
                Object val = ((JavascriptExecutor) d).executeScript("return !!document.fullscreenElement;");
                boolean state = Boolean.TRUE.equals(val);
                return state == desired;
            });
        } catch (Exception e) {
            Object val = ((JavascriptExecutor) driver).executeScript("return !!document.fullscreenElement;");
            return desired == Boolean.TRUE.equals(val);
        }
    }

    
    private void skipAdIfPresent(WebDriver driver) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        try {
            // 1) Check if an ad is playing
            boolean adIsPlaying = (Boolean) js.executeScript(
                "return document.querySelector('.html5-video-player')?.classList.contains('ad-showing') || false;"
            );

            if (adIsPlaying) {
                Reporter.log("📢 Ad detected — checking for skip button...<br/>", true);

                // 2) Wait until the skip button appears (max 8s) & click it
                WebElement skipBtn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.cssSelector(".ytp-ad-skip-button, .ytp-ad-skip-button-modern")));
                skipBtn.click();

                Reporter.log("⏩ Ad skipped successfully.<br/>", true);
            } else {
                Reporter.log("✅ No ad detected — continuing playback.<br/>", true);
            }

        } catch (TimeoutException te) {
            Reporter.log("⚠️ Ad detected but skip button not found (maybe unskippable ad).<br/>", true);
        } catch (Exception e) {
            Reporter.log("⚠️ Error while checking/skipping ad: " + e.getMessage() + "<br/>", true);
        }
    }

}
