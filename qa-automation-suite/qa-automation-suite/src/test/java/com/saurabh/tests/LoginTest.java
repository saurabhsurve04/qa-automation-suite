package com.saurabh.tests;

import com.saurabh.listeners.TestListeners;
import com.saurabh.pages.LoginPage;
import com.saurabh.pages.SecurePage;
import com.saurabh.utils.DriverManager;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Map;
@Listeners({TestListeners.class})
public class LoginTest {

    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--diable-save-passwpord-bubble");
        options.addArguments("--disable-notifications");
        options.addArguments("--incognito");
        options.setExperimentalOption("prefs", Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false));

        DriverManager.setDriver(new ChromeDriver(options));
        DriverManager.getDriver().manage().window().maximize();
        driver = DriverManager.getDriver();
        loginPage = new LoginPage(driver);
    }

    @Test
    public void validLoginTest() {
        driver.get("https://practice.expandtesting.com/login");
        loginPage.enterUsername("practice");
        loginPage.enterPassword("SuperSecretPassword!");
        loginPage.clickLogin();

        SecurePage securePage = new SecurePage(driver);
        Assert.assertTrue(securePage.getCurrentUrl().contains("secure"), "Login failed");
        Assert.assertTrue(securePage.getSuccessMessage().contains("You logged into a secure area!"), "Success message not found");
        securePage.clickLogoutButton();
        Assert.assertTrue(securePage.getCurrentUrl().contains("login"), "Login failed");
        System.out.println("Valid Login Test passed");
    }

    @Test
    public void invalidLoginTest() {
        driver.get("https://practice.expandtesting.com/login");
        loginPage.enterUsername("wrongpractice");
        loginPage.enterPassword("SuperSecretPassword!");
        loginPage.clickLogin();

        Assert.assertTrue(loginPage.getCurrentUrl().contains("login"), "Should stay on Login page");
        Assert.assertTrue(loginPage.getErrorMessage().contains("invalid"), "Error message not found");
        System.out.println("Invalid Login Test passed");
    }

    @AfterMethod
    public void tearDown() {
        DriverManager.quitDriver();
    }
}
