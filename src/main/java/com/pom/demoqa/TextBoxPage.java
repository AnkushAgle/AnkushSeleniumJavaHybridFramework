package com.pom.demoqa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;

import java.util.function.BiConsumer;
import com.aa.utility.Library;



public class TextBoxPage {

	public WebDriver driver;

	public TextBoxPage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	@FindBy(how = How.ID, using = "userName")
	private WebElement fullNameInput;

	@FindBy(how = How.ID, using = "userEmail")
	private WebElement emailInput;

	@FindBy(how = How.ID, using = "currentAddress")
	private WebElement currentAddressInput;

	@FindBy(how = How.ID, using = "permanentAddress")
	private WebElement permanentAddressInput;

	@FindBy(how = How.ID, using = "submit")
	private WebElement submitButton;

	@FindBy(how = How.ID, using = "name")
	private WebElement outputName;

	@FindBy(how = How.ID, using = "email")
	private WebElement outputEmail;

	// Business Methods with Library Utility
	public void enterFullName(String name) {
		Library.customSendkeysWithValdation(fullNameInput, name, "Full Name");
	}

	public void enterEmail(String email) {
		Library.customSendkeysWithValdation(emailInput, email, "Email");
	}

	public void enterCurrentAddress(String address) {
		Library.customSendkeysWithValdation(currentAddressInput, address, "Current Address");
	}

	public void enterPermanentAddress(String address) {
		Library.customSendkeysWithValdation(permanentAddressInput, address, "Permanent Address");
	}

	public void clickSubmitButton() {
		Library.isLoadedAndClick(submitButton, "Submit Button");
	}

	public String getSubmittedName() {
		//var a=10;
		return Library.getTextOfWebElementFunction(outputName);
	}

	public String getSubmittedEmail() {
		return Library.getTextOfWebElementFunction(outputEmail);
	}

	// Getter Methods (if needed)
	public WebElement getFullNameInput() {
		return fullNameInput;
	}

	public WebElement getEmailInput() {
		return emailInput;
	}

	public WebElement getCurrentAddressInput() {
		return currentAddressInput;
	}

	public WebElement getPermanentAddressInput() {
		return permanentAddressInput;
	}

	public WebElement getSubmitButton() {
		return submitButton;
	}

	public WebElement getOutputName() {
		return outputName;
	}

	public WebElement getOutputEmail() {
		return outputEmail;
	}
	@FindBy(how = How.ID, using = "currentAddress")
	private WebElement outputCurrentAddress;

	@FindBy(how = How.ID, using = "permanentAddress")
	private WebElement outputPermanentAddress;

	public String getSubmittedCurrentAddress() {
	    return Library.getTextOfWebElementFunction(outputCurrentAddress);
	}

	public String getSubmittedPermanentAddress() {
	    return Library.getTextOfWebElementFunction(outputPermanentAddress);
	}

	
	public void validateSubmittedData(String expectedName, String expectedEmail, String expectedCurrentAddress, String expectedPermanentAddress) {

		/*
		
		
		Reporter.log("=======================  Validate Result ==================");
		
		Reporter.log("<br></br>");
	    String[] headers = { "Field", "Expected", "Actual", "Status" };
	    StringBuffer logTable = new StringBuffer();

	    // Table start with borders and styling
	    logTable.append("<table border='1' style='border-collapse: collapse;'>");
	    logTable.append("<tr>");
	    for (String header : headers) {
	        logTable.append("<th style='padding:5px;'>").append(header).append("</th>");
	    }
	    logTable.append("</tr>");

	    // Validator: compare expected vs extracted actual value
	    BiConsumer<String, String> validator = (field, expected) -> {
	        String actualFullText = "";
	        switch (field) {
	            case "Name":
	                actualFullText = getSubmittedName();
	                break;
	            case "Email":
	                actualFullText = getSubmittedEmail();
	                break;
	            case "Current Address":
	                actualFullText = getSubmittedCurrentAddress();
	                break;
	            case "Permanent Address":
	                actualFullText = getSubmittedPermanentAddress();
	                break;
	        }

	        // Extract value after colon
	        String actual = actualFullText.contains(":") ? actualFullText.split(":", 2)[1].trim() : actualFullText.trim();
	        String status = actual.equals(expected) ? "✅ Pass" : "❌ Fail";

	        // Build table row
	        logTable.append("<tr>");
	        logTable.append("<td style='padding:5px;'>").append(field).append("</td>");
	        logTable.append("<td style='padding:5px;'>").append(expected).append("</td>");
	        logTable.append("<td style='padding:5px;'>").append(actual).append("</td>");
	        logTable.append("<td style='padding:5px;'>").append(status).append("</td>");
	        logTable.append("</tr>");
	    };

	    // Run comparisons
	    validator.accept("Name", expectedName);
	    validator.accept("Email", expectedEmail);
	    validator.accept("Current Address", expectedCurrentAddress);
	    validator.accept("Permanent Address", expectedPermanentAddress);

	    logTable.append("</table>");

	    // Log to report
	    Reporter.log(logTable.toString());
	
	
	
	
	    
	    
	*/
	
	
	}
}
