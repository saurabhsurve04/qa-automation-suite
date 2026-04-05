package com.saurabh.projects.javadrills;

import java.util.HashMap;
import java.util.Map;

public class Day02HashMap {

    enum TestStatus {
        PASSED, FAILED, UNEXECUTED, WIP
    }

    public static void main(String[] args) {
        HashMap<String, TestStatus> testCases = new HashMap<>();
        testCases.put("Login Test", TestStatus.PASSED);
        testCases.put("Signup Test", TestStatus.PASSED);
        testCases.put("Checkout Test", TestStatus.FAILED);
        testCases.put("Search Test", TestStatus.PASSED);
        testCases.put("Profile Update Test", TestStatus.PASSED);
        testCases.put("Invalid Login Test", TestStatus.FAILED);
        testCases.put("Empty Login Test", TestStatus.FAILED);
        testCases.put("Payment Gateway Test", TestStatus.PASSED);
        testCases.put("Invalid Signup Test", TestStatus.FAILED);
        testCases.put("Invalid Checkout Test", TestStatus.FAILED);

        int count = 0;
        for (Map.Entry<String, TestStatus> testCase : testCases.entrySet()) {
            if (testCase.getValue().equals(TestStatus.PASSED)) {
                count++;
            } else if (testCase.getValue().equals(TestStatus.FAILED)) {
                System.out.println(testCase.getKey() + " has failed.");
            }
        }
        System.out.println("Total Passed Test Cases: " + count);

    }

}

