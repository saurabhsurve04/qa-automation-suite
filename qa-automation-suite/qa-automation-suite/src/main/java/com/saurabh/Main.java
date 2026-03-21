package com.saurabh;

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

        TestCase test1 = new TestCase("Login Test", 2.5);
        test1.run();
        test1.pass();
        test1.printStatus();

        TestCase test2 = new TestCase("Payment Test", 4.1);
        test2.run();
        test2.fail();
        test2.printStatus();

        System.out.println(test1.getTestName());
        System.out.println(test2.getDuration());

        UITest uiTest = new UITest("Login UI Test", 3.2, "Chrome");
        uiTest.run();
        uiTest.pass();
        uiTest.printStatus();
        System.out.println("Browser: " + uiTest.getBrowserName());

        UITest uiTest2 = new UITest("Logout UI Test", 1.2, "Chrome");
        uiTest2.run();
        uiTest2.pass();
        uiTest2.generateReport();

        APITest apiTest = new APITest("Payment API Test", 3.2, "http://www.google.com");
        apiTest.run();
        apiTest.pass();
        apiTest.printStatus();
        System.out.println("Endpoint URL: " + apiTest.getEndpointURL());

        TestSuite testSuite = new TestSuite("Regression Suite");
        testSuite.addTest(new UITest("Login UI Test", 3.2, "Chrome"));
        testSuite.addTest(new UITest("Logout UI Test", 1.2, "Chrome"));
        testSuite.addTest(new APITest("Payment API", 1.2, "http://www.google.com"));

        testSuite.runAll();
        System.out.println("Total Tests: " + testSuite.getTotalTests());

        try {
            TestSuite testSuite2 = new TestSuite("Regression Suite2");
            UITest t1 = new UITest("Login UI Test", 3.2, "Chrome");
            UITest t2 = new UITest("Logout UI Test", 1.2, "Chrome");
            APITest t3 = new APITest("Payment API", 3.2, "http:\\www.google.com");

            testSuite2.addTest(null);
            testSuite2.addTest(t1);
            testSuite2.addTest(t2);
            testSuite2.addTest(t3);

            t1.pass();
            t2.pass();
            t3.fail();

            testSuite2.printResults();
        } catch (TestSuiteException e) {
            System.out.println("Error: " + e.getMessage() + "\n");

        } finally {
            System.out.printf("Suite Execution Completed\n");
        }
    }

}