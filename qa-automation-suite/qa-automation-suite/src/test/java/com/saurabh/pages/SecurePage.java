package com.saurabh.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class SecurePage extends BasePage {

    private final By successMessage = By.tagName("b");
    private final By logoutButton = By.xpath("//i[contains(text(),'Logout')]");

    public SecurePage(WebDriver driver) {
        super(driver);
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
        return driver.findElement(successMessage).getText();
    }

    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}
