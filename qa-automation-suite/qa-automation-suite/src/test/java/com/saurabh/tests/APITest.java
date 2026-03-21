package com.saurabh.tests;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class APITest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getUsersTest() {
        Response response = given()//setup - headers, auth, body
                .when()//action - GET, POST, PUT, DELETE
                .get("/users") //endpoint
                .then() //assertions
                .statusCode(200)//assert HTTP 200
                .extract() //extract the response
                .response(); //as a response object

        int userCount = response.jsonPath().getList("$").size();
        String firstUserName = response.jsonPath().getString("[0].name");
        String email = response.jsonPath().getString("[0].email");

        System.out.println("First Users Name: " + firstUserName);
        System.out.println("First Users Email: " + email);
        System.out.println("Total user count: " + userCount);

        Assert.assertEquals(userCount, 10, "User should be 10");
        Assert.assertNotNull(firstUserName,"First Name cannot be null");
        Assert.assertTrue(email.contains("@"), "Email address should contain @");
    }
}
