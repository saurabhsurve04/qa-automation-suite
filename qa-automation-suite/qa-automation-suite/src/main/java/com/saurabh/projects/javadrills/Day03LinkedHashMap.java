package com.saurabh.projects.javadrills;

import java.util.LinkedHashMap;

public class Day03LinkedHashMap {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> testCases = new LinkedHashMap<String, Integer>();
        testCases.put("Login Test", 1451);
        testCases.put("Signup Test", 4122);
        testCases.put("Checkout Test", 4510);
        testCases.put("Search Test", 5451);
        testCases.put("Profile Update Test", 4454);
        testCases.put("Invalid Login Test", 7875);
        testCases.put("Empty Login Test", 4121);
        testCases.put("Payment Gateway Test", 7856);
        testCases.put("Invalid Signup Test", 4212);
        testCases.put("Invalid Checkout Test", 4561);

        for (String testCase : testCases.keySet()) {
            System.out.println(testCase + " took " + testCases.get(testCase) + " ms");
        }
    }
}
