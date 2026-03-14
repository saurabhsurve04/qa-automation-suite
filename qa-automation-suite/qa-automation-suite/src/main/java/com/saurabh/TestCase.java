package com.saurabh;

public class TestCase {

    private String testName;
    private String status;
    private double duration;

    public TestCase(String testName, double duration) {
        this.testName = testName;
        this.duration = duration;
        this.status = "NOT RUN";
    }

    public void run() {
        System.out.println("Running test: " + testName + "\n");
    }

    public void pass() {
        this.status = "PASSED";
        System.out.printf(testName + " PASSED in " + duration + "s\n");
    }

    public void fail() {
        this.status = "FAILED";
        System.out.printf(testName + " FAILED in " + duration + "s\n");
    }

    public void printStatus() {
        System.out.println("Test: " + testName + " | Status: " + status + " | Duration: " + duration + "s");
    }

    public String getTestName() {
        return testName;
    }

    public String getStatus() {
        return status;
    }

    public double getDuration() {
        return duration;
    }
}
