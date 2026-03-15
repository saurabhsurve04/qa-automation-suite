package com.saurabh;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;

public class FirstTest {

    WebDriver driver;

    @BeforeSuite
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.IMPLICIT_WAIT));
    }

    @Test
    public void openGoogle() {
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        System.out.println("Page title is: " + title);
        Assert.assertTrue(title.contains("Google"), "Title does not contain Google");
    }

    @Test
    public void loginTest() {
        driver.get("https://practice.expandtesting.com/login");
//        find elements and interact
        driver.findElement(By.id("username")).sendKeys("practice");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
//        Below wait no more needed since we are using JavaScript Executor
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
        WebElement loginButton = driver.findElement(By.id("submit-login"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click()", loginButton);
//        Below does not work because there is a iframe for ad
//        wait.until(ExpectedConditions.elementToBeClickable(By.id("submit-login"))).click();
//        Below does not work if there is a lag in loading
//        driver.findElement(By.id("submit-login")).click();

//        Assert we landed on dashboard
        String currentURL = driver.getCurrentUrl();
        System.out.println("Current URL: " + currentURL);
        Assert.assertTrue(currentURL.contains("secure"), "Login failed. Not on secure page");

//        Find the success message and assert it
        WebElement successMessage = driver.findElement(By.tagName("b"));
        String messageText = successMessage.getText();
        Assert.assertTrue(messageText.contains("You logged into a secure area!"), "Success message not found");
    }

    @Test
    public void navigationTest(){
        // Go to Google
        driver.get("https://www.google.com");
        System.out.println("Page 1 title is: " + driver.getTitle());

        driver.navigate().to("https://practice.expandtesting.com");
        System.out.println("Page 2 title is: " + driver.getTitle());

        driver.navigate().back();
        System.out.println("After back, title is: " + driver.getTitle());

        driver.navigate().forward();
        System.out.println("After forward, title is: " + driver.getTitle());

        driver.navigate().refresh();
        System.out.println("After refresh, title is: " + driver.getTitle());

    }

    @AfterSuite
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}