import org.testng.Assert;
import org.testng.annotations.*;

public class TestSuiteRunner {

    @BeforeSuite
    public void suiteSetup() {
        System.out.println("Starting Test Suite");
    }

    @BeforeMethod
    public void testSetup() {
        System.out.println("Setting up Test");
    }

    @Test(priority = 1)
    public void loginTest() {
        System.out.println("Running Login Test");
        Assert.assertEquals(2 + 2, 4, "Math should work");
    }

    @Test(priority = 2)
    public void paymentTest() {
        System.out.println("Running Payment Test");
        Assert.assertTrue(100 > 50, "100 should be greater than 50");
    }

    @Test(priority = 3)
    public void apiTest() {
        System.out.println("Running API Test");
        Assert.assertNotNull("respone", "Response should not be null");
    }

    @Test(priority = 4)
    public void failingTest() {
        System.out.println("Running Failing Test");
        Assert.assertTrue(100 < 50, "100 should be greater than 50");
    }

    @AfterMethod
    public void testTearDown() {
        System.out.println("Cleaning up Test");
    }

    @AfterSuite
    public void suiteTearDown() {
        System.out.println("Test Suite Completed");
    }
}
