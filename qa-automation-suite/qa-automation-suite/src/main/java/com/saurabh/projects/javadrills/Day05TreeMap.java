package com.saurabh.projects.javadrills;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Day05TreeMap {
    public static void main(String[] args) {
        TreeMap<String,Integer> testCases = new TreeMap<>();
        testCases.put("Login Test", 1223);
        testCases.put("Payment Gateway Test", 2222);
        testCases.put("Checkout Test", 3232);
        testCases.put("Empty Login Test", 1222);
        testCases.put("Invalid Login Test",1217);

        System.out.println("Sorted by Testcase name:");
        testCases.forEach((testCase, time) -> System.out.println(testCase + " took " + time + " ms"));
        System.out.println("=======================================");
        System.out.println("Sorted by execution time using option A - LinkedHasMap:");

        Map<String, Integer> sortedByValue = testCases.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1,e2) -> e1,
                        LinkedHashMap::new
                ));
        for (Map.Entry<String, Integer> entry : sortedByValue.entrySet()) {
            System.out.println(entry.getKey() + " took " + entry.getValue() + " ms");
        }
        System.out.println("=======================================");
        System.out.println("Sorted by execution time using option B - Stream:");
        testCases.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(entry -> System.out.println(entry.getKey() + " took " + entry.getValue() + " ms"));
    }
}
