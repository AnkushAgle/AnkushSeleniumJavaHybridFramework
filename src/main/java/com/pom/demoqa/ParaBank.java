package com.pom.demoqa;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

import com.aa.utility.Library;

public class ParaBank {

    WebDriver driver;

    // 🔹 Constructor
    public ParaBank(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    // 🔹 Locators (Login Panel)
    @FindBy(how = How.NAME, using = "username")
    private WebElement usernameInput;

    @FindBy(how = How.NAME, using = "password")
    private WebElement passwordInput;

    @FindBy(how = How.CSS, using = "input.button[value='Log In']")
    private WebElement loginButton;

    @FindBy(how = How.LINK_TEXT, using = "Forgot login info?")
    private WebElement forgotLoginLink;

    @FindBy(how = How.LINK_TEXT, using = "Register")
    private WebElement registerLink;

    // 🔹 Locators (Left Panel – Account Services)
    @FindBy(how = How.LINK_TEXT, using = "Open New Account")
    private WebElement openNewAccountLink;

    @FindBy(how = How.LINK_TEXT, using = "Accounts Overview")
    private WebElement accountsOverviewLink;

    @FindBy(how = How.LINK_TEXT, using = "Transfer Funds")
    private WebElement transferFundsLink;

    @FindBy(how = How.LINK_TEXT, using = "Bill Pay")
    private WebElement billPayLink;

    @FindBy(how = How.LINK_TEXT, using = "Find Transactions")
    private WebElement findTransactionsLink;

    @FindBy(how = How.LINK_TEXT, using = "Update Contact Info")
    private WebElement updateContactInfoLink;

    @FindBy(how = How.LINK_TEXT, using = "Request Loan")
    private WebElement requestLoanLink;

    @FindBy(how = How.LINK_TEXT, using = "Log Out")
    private WebElement logoutLink;

    // 🔹 Actions (Login Panel)
    public void enterUsername(String username) {
        usernameInput.clear();
        Library.customSendkeysWithValdation(usernameInput, username, "Username");
    }

    public void enterPassword(String password) {
        passwordInput.clear();
        Library.customSendkeysWithValdation(passwordInput, password, "Password");
    }

    public void clickLogin() {
        Library.customClickFunction(loginButton);
    }

    public void clickForgotLogin() {
        Library.customClickFunction(forgotLoginLink);
    }

    public void clickRegister() {
        Library.customClickFunction(registerLink);
    }

    // 🔹 Actions (Account Services)
    public void clickOpenNewAccount() {
        Library.customClickFunction(openNewAccountLink);
    }

    public void clickAccountsOverview() {
        Library.customClickFunction(accountsOverviewLink);
    }

    public void clickTransferFunds() {
        Library.customClickFunction(transferFundsLink);
    }

    public void clickBillPay() {
        Library.customClickFunction(billPayLink);
    }

    public void clickFindTransactions() {
        Library.customClickFunction(findTransactionsLink);
    }

    public void clickUpdateContactInfo() {
        Library.customClickFunction(updateContactInfoLink);
    }

    public void clickRequestLoan() {
        Library.customClickFunction(requestLoanLink);
    }

    public void clickLogout() {
        Library.customClickFunction(logoutLink);
    }
}
