package com.saurabh;

import org.openqa.selenium.remote.Browser;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

//        String[] testNames = {"Login Test", "UI Test", "Payment Test", "Gateway Test", "API Test"};
//        double[] passedTests = {50.0, 30.0, 20.0, 10.0, 15.0};
//        double totalTests = 55;
//        TestResultsReporter testResultsReport = new TestResultsReporter();
//        for (int i = 0; i < testNames.length; i++) {
//            testResultsReport.result(testNames[i], passedTests[i], totalTests);
//        }

        TestCase test1 = new TestCase("Login Test",2.5);
        test1.run();
        test1.pass();
        test1.printStatus();

        TestCase test2 = new TestCase("Payment Test",4.1);
        test2.run();
        test2.fail();
        test2.printStatus();

        System.out.println(test1.getTestName());
        System.out.println(test2.getDuration());

        UITest uiTest = new UITest("Login UI Test",3.2,"Chrome");
        uiTest.run();
        uiTest.pass();
        uiTest.printStatus();
        System.out.println("Browser: " + uiTest.getBrowserName());

        UITest uiTest2 = new UITest("Logout UI Test",1.2,"Chrome");
        uiTest2.run();
        uiTest2.pass();
        uiTest2.generateReport();

        APITest apiTest = new APITest("Payment API Test",3.2,"http:\\www.google.com");
        apiTest.run();
        apiTest.pass();
        apiTest.printStatus();
        System.out.println("Endpoint URL: " + apiTest.getEndpointURL());
    }

}