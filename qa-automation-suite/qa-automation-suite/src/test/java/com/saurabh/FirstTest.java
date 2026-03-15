package com.saurabh;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.openqa.selenium.JavascriptExecutor;
import java.time.Duration;

public class FirstTest {

    WebDriver driver;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
    }

    @Test
    public void openGoogle() {
        driver.get("https://www.google.com");
        String title = driver.getTitle();
        System.out.println("Page title is: " + title);
        Assert.assertTrue(title.contains("Google"),"Title does not contain Google");
    }

    @Test
    public void loginTest(){
        driver.get("https://practice.expandtesting.com/login");
//        find elements and interact
        driver.findElement(By.id("username")).sendKeys("practice");
        driver.findElement(By.id("password")).sendKeys("SuperSecretPassword!");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(FrameworkConstants.EXPLICIT_WAIT));
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
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}