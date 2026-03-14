package com.saurabh;

public class APITest extends TestCase {

    private final String endpointURL;


    public APITest(String testName, double duration, String endpointURL) {
        super(testName, duration);
        this.endpointURL = endpointURL;
    }

    public void run() {
        System.out.println("Running API Test: " + getTestName() + " on " + endpointURL);
    }

    public String getEndpointURL() {
        return endpointURL;
    }
}
