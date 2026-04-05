package com.saurabh.projects.javadrills;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class Day04HashSet {
    public static void main(String[] args) {
        ArrayList<String> testCases = new ArrayList<>();
        testCases.add("Login Test");
        testCases.add("Signup Test");
        testCases.add("Search Test");
        testCases.add("Profile Update Test");
        testCases.add("Invalid Login Test");
        testCases.add("Empty Login Test");
        testCases.add("Payment Gateway Test");
        testCases.add("Invalid Signup Test");
        testCases.add("Invalid Checkout Test");
        testCases.add("Checkout Test");
        testCases.add("Empty Login Test");
        testCases.add("Payment Gateway Test");
        testCases.add("Invalid Signup Test");
        testCases.add("Invalid Checkout Test");
        testCases.add("Checkout Test");
        HashSet<String> uniqueTestCases = new HashSet<>();
        for (String testCase : testCases) {
            if(uniqueTestCases.add(testCase)) {
                System.out.println(testCase + " added to the set.");
            } else {
                System.out.println(testCase + " is a duplicate and was not added.");
            }
        }
    }
}
