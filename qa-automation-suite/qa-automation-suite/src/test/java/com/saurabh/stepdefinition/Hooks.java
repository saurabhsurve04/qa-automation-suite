package com.saurabh.stepdefinition;

import com.saurabh.utils.DriverManager;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Map;

public class Hooks {

    @Before
    public void beforeScenario() {
        System.out.println("Starting scenario.......");
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--incognito");
        options.addArguments("--disable-notifications");
        options.setExperimentalOption("prefs", Map.of("credentials_enable_service", false, "profile.password_manager_enabled", false));

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        DriverManager.setDriver(driver);
    }

    @After
    public void afterScenario(Scenario scenario) {
        if(scenario.isFailed()){
            try{
                byte[] screenshot = ((TakesScreenshot) DriverManager.getDriver()).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", scenario.getName());
                System.out.println("Screenshot attached to Cucumber Report");
            } catch (Exception e){
                System.out.println(STR."Could not take screenshot: \{e.getMessage()}");
            }
        }
        System.out.println("Finishing scenario.......");
        DriverManager.quitDriver();
    }
}
