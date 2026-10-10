package com.aa.utility;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import org.testng.Reporter;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

import net.bytebuddy.utility.RandomString;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class ScreenshotUtil extends Library {

    // Utility method to ensure directory exists
    private static void ensureDirectoryExists(String filePath) {
        File file = new File(filePath);
        File parentDir = file.getParentFile();
        if (!parentDir.exists()) {
            boolean created = parentDir.mkdirs();
            if (created) {
                System.out.println("Directory created: " + parentDir.getAbsolutePath());
            } else {
                System.out.println("Failed to create directory: " + parentDir.getAbsolutePath());
            }
        }
    }

    public static void screenshot(String name) throws Exception {
        // Example placeholder
    }

    public static void attachscreenshotreport(String Title) throws Exception {
        test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromPath(reportpath(), Title).build());
    }

    public static String reportpath() throws Exception {
        WebDriver driver = getDriver();
        String rm = RandomString.make(2);
        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);

        String path = System.getProperty("user.dir") + "\\ScreenShots\\failedtest" + rm + ".png";
        ensureDirectoryExists(path);

        File destn = new File(path);
        FileHandler.copy(src, destn);
        return path;
    }

    public static String captureScreenshotWithRandomName() throws Exception {
        WebDriver driver = getDriver();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String rm = dateFormat.format(date);

        String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\valdn" + rm + ".png";
        ensureDirectoryExists(path);

        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        File Destnation = new File(path);
        FileHandler.copy(src, Destnation);

        test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(), "<b>Validation Screenshot</b>").build());

        Library.massagelog_TIME_DATE("Sceenshot Path::" + path);
        System.out.println("Screenshot path==" + path);

        String Time = Library.timeStampLogging();
        Reporter.log("<h4>Time :" + Time + "</h4>");
        Reporter.log("<br>");
        Reporter.log("<img src=\"" + path + "\"" + " width=\"1000\" height=\"500\" >" + "" + "</img>");
        Reporter.log("<br>");
        Reporter.log("<br>");

        return path;
    }

    public static String reportpathBase() throws Exception {
        WebDriver driver = getDriver();
        TakesScreenshot ts = (TakesScreenshot) driver;
        String dest = ts.getScreenshotAs(OutputType.BASE64);
        return "data:image/jpg;base64, " + dest;
    }

    public static void attachScreenshotBase64Format(String Title) throws Exception {
        System.out.println("Looking for screenshot ");
        captureScreenshot(Title);
        System.out.println("Screenshot taken successfully ");
    }

    public static void Fullpagescreenshot(String Screenshotname) throws Exception {
        WebDriver driver = getDriver();

        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String rm = dateFormat.format(date);

        String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\" + Screenshotname + rm + ".png";
        ensureDirectoryExists(path);

        Screenshot s = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(5000)).takeScreenshot(driver);
        ImageIO.write(s.getImage(), "PNG", new File(path));

        System.out.println("Full page screenshot successfully taken==" + path);
    }

    public static void captureScreenshot(String screenshotname) throws Exception {
        WebDriver driver = getDriver();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");
        String timeStamp = dateFormat.format(calendar.getTime());

        String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\" + screenshotname + timeStamp + ".png";
        ensureDirectoryExists(path);

        System.out.println("Screenshot path == " + path);

        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(path);
        FileHandler.copy(src, destination);

        test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(),
                "<b>Validation Screenshot::" + screenshotname + "</b>").build());

        String Time = Library.timeStampLogging();
        Reporter.log("<h4>Time :" + Time + "</h4>");
        Reporter.log("<br>");
        Reporter.log("<img src=\"" + path + "\"" + " width=\"1000\" height=\"500\" " + " title=\"" + path + "\"" + "></img>");
        Reporter.log("<br>");
        Reporter.log("<br>");
    }

    public static String captureScreenshotWithOriginalName(String screenshotname) throws Exception {
        WebDriver driver = getDriver();

        String path = System.getProperty("user.dir") + "\\ScreenShots\\Validation\\" + screenshotname + ".png";
        ensureDirectoryExists(path);

        System.out.println("Screenshot path == " + path);

        TakesScreenshot ts = (TakesScreenshot) driver;
        File src = ts.getScreenshotAs(OutputType.FILE);
        File destination = new File(path);
        FileHandler.copy(src, destination);

        test.log(Status.INFO, MediaEntityBuilder.createScreenCaptureFromBase64String(reportpathBase(),
                "<b>Validation Screenshot::" + screenshotname + "</b>").build());

        String Time = Library.timeStampLogging();
        Reporter.log("<h4>Time :" + Time + "</h4>");
        Reporter.log("<br>");
        Reporter.log("<img src=\"" + path + "\"" + " width=\"1000\" height=\"500\" " + " title=\"" + path + "\"" + "></img>");
        Reporter.log("<br>");
        Reporter.log("<br>");

        return path;
    }

    public static String captureFullPageScreenshot(WebDriver driver, String filePath) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        ensureDirectoryExists(filePath);

        try {
            BufferedImage fullImage = ImageIO.read(screenshot);

            String currentUrl = driver.getCurrentUrl();

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String timestamp = dateFormat.format(date);

            Graphics2D graphics = fullImage.createGraphics();
            Font font = new Font("Arial", Font.BOLD, 14);
            graphics.setFont(font);
            graphics.setColor(Color.RED);

            graphics.drawString("URL: " + currentUrl, 10, fullImage.getHeight() - 30);
            graphics.drawString("Timestamp: " + timestamp, 10, fullImage.getHeight() - 50);

            ImageIO.write(fullImage, "png", new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }
}
