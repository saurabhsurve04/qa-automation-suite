package com.saurabh.projects.grid;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import org.testng.Assert;
import org.testng.annotations.Test;

public class GridSmokeTest {

    @Test
    public void testGridSmokeTest() throws MalformedURLException {
        ChromeOptions options = new ChromeOptions();

        WebDriver driver = new RemoteWebDriver(new URL("http://localhost:4444"), options);
        try{
            driver.get("https://www.google.com");
            String title = driver.getTitle();
            System.out.println("Title: " + title);
            Assert.assertTrue(title.contains("Google"),"Title does not contain Google");

        } finally {
            driver.quit();
        }

    }
}
