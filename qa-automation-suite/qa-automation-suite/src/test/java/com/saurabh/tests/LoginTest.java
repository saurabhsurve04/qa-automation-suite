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
import org.testng.annotations.*;

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

    @DataProvider(name = "loginData")
    public Object[][] loginData() {
        return new Object[][]{
                {"practice","SuperSecretPassword!",true,"secure"},
                {"wronguser","wrongpassword",false,"login"},
                {"practice","wrongpassword",false,"login"}
        };
    }

    @Test(dataProvider = "loginData")
    public void dataDriverLoginTest(String username, String password, boolean shouldPass, String expectedUrl) throws InterruptedException {
        driver.get("https://practice.expandtesting.com/login");
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
        loginPage.clickLogin();

        Assert.assertTrue(driver.getCurrentUrl().contains(expectedUrl),"URL assertion failed for user: " + username);
        System.out.println("Test passed for user: " + username);

        if(shouldPass){
            Assert.assertTrue(loginPage.getSuccessMessage().contains("You logged into a secure area!"),"Success message not found for user:" + username);
            System.out.println("Valid login verified for user: " + username);
        } else {
            Assert.assertTrue(loginPage.getErrorMessage().contains("invalid"),"Error message not found for user:" + username);
            System.out.println("Invalid login verified for user: " + username);
        }

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
