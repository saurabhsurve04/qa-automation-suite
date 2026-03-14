package com.saurabh;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class TestSuite {

    private final String suiteName;
    private final ArrayList<TestCase> tests;

    public TestSuite(String suiteName) {
        this.suiteName = suiteName;
        this.tests = new ArrayList<>();
    }

    public void addTest(TestCase test) {
        if (test == null) {
            throw new TestSuiteException("Test cannot be null - Check your test configuration");
        }
        tests.add(test);
        System.out.println("Added " + test.getTestName() + "\n");
    }

    public void runAll() {
        System.out.println("Running suite: " + suiteName + "\n");
        for (TestCase test : tests) {
            test.run();
        }
    }

    public int getTotalTests() {
        return tests.size();
    }

    public void printResults() {
        LinkedHashMap<String, String> results = new LinkedHashMap<>();

        for (TestCase test : tests) {
            results.put(test.getTestName(), test.getStatus());
        }

        System.out.println("Test Results:");
        for (String testName : results.keySet()) {
            System.out.println(testName + " : " + results.get(testName));
        }
    }

}
