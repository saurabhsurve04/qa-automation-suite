package com.saurabh;

public class UITest extends TestCase implements Executable {

    private String browserName;

    public UITest(String testName, double duration, String browserName) {
        super(testName, duration);
        this.browserName = browserName;
    }
    @Override
    public void run() {
        System.out.println("Running UITest: " + getTestName() + " on " + getBrowserName());
    }

    public String getBrowserName() {
        return browserName;
    }


    @Override
    public void execute() {
        System.out.println("Executing UITest: " + getTestName() + " on " + getBrowserName());
    }

    @Override
    public void generateReport() {
        System.out.println("Report Name: " + getTestName() + " | Status: " + getStatus() + " | Browser: " + getBrowserName());

    }
}
