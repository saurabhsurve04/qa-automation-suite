package com.saurabh.stepdefinition;

import com.saurabh.pages.LoginPage;
import com.saurabh.pages.SecurePage;
import com.saurabh.utils.DriverManager;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginSteps {

    LoginPage loginPage;
    SecurePage securePage;

    @Given("USER is on the login page")
    public void UserIsOnLoginPage() throws Throwable {
        loginPage = new LoginPage(DriverManager.getDriver());
        securePage = new SecurePage(DriverManager.getDriver());
        DriverManager.getDriver().get("https://practice.expandtesting.com/login");
    }

    @When("USER enter username {string}")
    public void userEnterUsername(String username) throws Throwable {
        loginPage.enterUsername(username);
    }

    @And("USER enter password {string}")
    public void userEnterPassword(String password) throws Throwable {
        loginPage.enterPassword(password);
    }

    @And("USER click on login button")
    public void userClickLoginButton() throws Throwable {
        loginPage.clickLogin();
    }

    @Then("USER should see {string}")
    public void userShouldSee(String expectedResult) throws Throwable {
        String actualMessage = DriverManager.getDriver().findElement(By.tagName("b")).getText();
        Assert.assertTrue(actualMessage.contains(expectedResult), "Expected result: " + expectedResult + " but got: " + actualMessage);
    }
}
