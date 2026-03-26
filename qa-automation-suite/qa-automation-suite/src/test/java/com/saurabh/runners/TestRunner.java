package com.saurabh.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.saurabh.stepdefinition",
        plugin = {"pretty", "html:target/cucumber-reports/report.html"},
        monochrome = true,
        tags = "@regression"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
