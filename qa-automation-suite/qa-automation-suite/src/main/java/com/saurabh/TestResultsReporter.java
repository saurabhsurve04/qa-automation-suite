package com.saurabh;

public class TestResultsReporter {

    static void result(String testName, double passed, double totalTests) {
        double passPercentage = percentageCal(passed, totalTests);
        if (passPercentage >= 80) {
            System.out.printf("%s passed with %.2f \n", testName, passPercentage);
        } else if (passPercentage < 80 && passPercentage >= 50) {
            System.out.printf("%s has a build warning with %.2f \n", testName, passPercentage);
        } else if (passPercentage < 50) {
            System.out.printf("%s has failed with %.2f \n", testName, passPercentage);
        }
    }

    static double percentageCal(double passedTests, double totalTests) {
        double passPercentage = 0;
        if (totalTests <= 0) {
            System.out.println("Total Tests cannot be less than 0.");
        } else if (totalTests <= passedTests) {
            System.out.println("Total Tests cannot be less than Passed Tests");
        } else {
            passPercentage = (passedTests / totalTests) * 100;
        }
        return passPercentage;
    }
}
