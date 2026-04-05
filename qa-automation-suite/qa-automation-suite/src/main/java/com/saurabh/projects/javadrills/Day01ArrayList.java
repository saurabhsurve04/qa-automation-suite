package com.saurabh.projects.javadrills;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day01ArrayList {
    public static void main(String[] args) {
        ArrayList<String> testCases = new ArrayList<>(List.of("Login Test", "Signup Test", "Checkout Test", "Search Test", "Profile Update Test", "Invalid Login Test","Empty Login Test", "Payment Gateway Test","Invalid Signup Test","Invalid Checkout Test"));
        // Remove elements containing "Login" safely
        testCases.removeIf(testCase -> testCase.contains("Login"));
        Collections.reverse(testCases);
        System.out.println(testCases);
    }
}
