package com.aa.constant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import com.aa.drivers.BaseClass;
import com.aa.utility.ScrollHelper;

import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyLibrary extends BaseClass {

	public static WebDriver driver;
	
    // 🔹 Existing methods
    public static void signIn(String email, String pwd) {
        isElementPresent("btn_signin_xPath").click();
        isElementPresent("btn_email").sendKeys(email);
        isElementPresent("btn_password_xPath").sendKeys(pwd);
        isElementPresent("btn_Signin_xPath").click();
    }

    private static WebElement isElementPresent(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void signOut() {
        isElementPresent("btn_signout_xPath").click();
    }

    // ==============================================================  
    // 🔹 CSV Generation & Upload Methods
    // ==============================================================  

    // Generate CSV file by type
    public static String generateCSV(String type) throws IOException {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String timestamp = LocalDateTime.now().format(dtf);
       // String filePath = System.getProperty("user.dir") + "/target/" + type + "_" + timestamp + ".csv";
        String filePath  = Paths.get(System.getProperty("user.dir"), "target", type + "_" + timestamp + ".csv").toString();


        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Date,Description,Amount\n");

            switch (type) {
                case "PositiveCase":
                    writer.append("2025-09-19,Salary_" + timestamp + ",+5000\n");
                    writer.append("2025-09-20,Rent_" + timestamp + ",-2000\n");
                    break;

                case "DuplicateName":
                    writer.append("2025-09-19,Transaction1,100\n");
                    writer.append("2025-09-19,Transaction1,-200\n"); // duplicate
                    break;

                case "InvalidDateFormat":
                    writer.append("19-09-2025,Transaction2_" + timestamp + ",300\n");
                    writer.append("2025-09-19,Transaction3_" + timestamp + ",-150\n");
                    break;

                case "WrongAmount":
                    writer.append("2025-09-19,Transaction4_" + timestamp + ",ABC\n");
                    writer.append("2025-09-19,Transaction5_" + timestamp + ",500\n");
                    break;
            }
        }

        return filePath;
    }

    // Select CSV file
    public static void selectFile(String filePath) {
        WebElement selectFileInput = isElementPresent("tb_ChooseCsvfile_byXpath"); 
        selectFileInput.sendKeys(filePath);
        System.out.println("CSV File Selected: " + filePath);
    }

    // Upload CSV file
    public static void uploadFile() {
    	
    	try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
        WebElement uploadBtn = isElementPresent("btn_uploadCsvFile_byid"); 
     
		ScrollHelper.scrollToElement(driver, uploadBtn);//to use scroll bar
        uploadBtn.click();        
        System.out.println("CSV File Uploaded");
        
        try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

    // NEW: Upload CSV file and check success/failure
    public static boolean uploadFileAndCheck(String filePath, boolean expectSuccess) {
        selectFile(filePath);
        uploadFile();

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement messageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[text()='Upload failed'] | //div[text()='Upload successful']")
        ));
        
        ScrollHelper.scrollToElement(driver, messageElement);//to use scroll bar
        String uploadMessage = messageElement.getText();
        System.out.println("Upload message: " + uploadMessage);
        MyLibrary.dialogMassageLog(uploadMessage);

        if (uploadMessage.contains("success")) {
            return uploadMessage.equals("Upload successful");
        } else {
            return uploadMessage.contains("failed");
        }
    }

    // Optional: capture warning message separately if needed
    public static String getWarningMessage() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement warningMsg = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='alert alert-warning']"))
        );
        return warningMsg.getText(); 
    }
    
    
    
    

public static void logPcNameAndHostName() {
	

try {
    String userName = System.getProperty("user.name").toUpperCase();
    String hostName = InetAddress.getLocalHost().getHostName();

    String userInfoMessage = "<div style=\"border: 1px solid #ddd; padding: 8px; margin: 5px 0; border-radius: 4px; background-color: #f7f7f7; display: inline-block;\">" +
            "<strong>💻 #System Info</strong> :: <strong>PC Name</strong> ⇒ <span style=\"color: #001dff;\">" + hostName + "</span> &nbsp;&nbsp;|&nbsp;&nbsp; <strong>User</strong> ⇒ <span style=\"color: #006400;\">" + userName + "</span></div>";

    // Logging the System Info message
    Reporter.log(userInfoMessage);
    //test.log(Status.INFO, MarkupHelper.createLabel(" PC: " + hostName + " | User: " + userName, ExtentColor.BLUE));

} catch (Exception e) {
    e.printStackTrace(); // or use custom logging
}

	
}

public static void dialogMassageLog(String msg) {

	// Creating the HTML structure for the dialog box
	StringBuffer logvar = new StringBuffer();

	// Div style now adjusts based on the content size and aligns at the start
	logvar.append("<div style=\"background-color:lightblue; border:2px solid blue; ");
	logvar.append("border-radius:10px; padding:10px 15px; text-align:left; ");
	logvar.append("margin:10px 0; display:inline-block;\">");

	// Message content
	logvar.append("<p style='font-size:18px; margin:0;'><b>" + msg + "</b></p>");

	logvar.append("</div>");

	// Logging the HTML in the console and report
//	System.out.println("Dialog text :: " + logvar.toString());
	Reporter.log(logvar.toString(), true); // 'true' ensures the message is printed to the report and console
}

}
