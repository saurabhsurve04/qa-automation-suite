package com.saurabh;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        String[] testNames = {"Login Test","UI Test","Payment Test","Gateway Test","API Test"};
        double[] passedTests = {50.0,30.0,20.0,10.0,15.0};
        double totalTests = 55;
        TestResultsReporter testResultsReport = new TestResultsReporter();
        for (int i = 0; i < testNames.length; i++) {
            testResultsReport.result(testNames[i],passedTests[i], totalTests);

        }
    }

}