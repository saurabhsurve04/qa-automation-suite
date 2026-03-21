package com.saurabh.tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class APITest {

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
        RestAssured.filters(new io.restassured.filter.log.RequestLoggingFilter(),
                new io.restassured.filter.log.ResponseLoggingFilter());
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
    @Test
    public void createUserTest(){
        String requestBody = "{\n" +
                "   \"name\":   \"Saurabh\",\n" +
                "   \"job\":    \"QA Lead\"\n" +
                "}";

        Response response = given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .extract()
                .response();

        int id = response.jsonPath().getInt("id");
        String title = response.jsonPath().getString("title");

        System.out.println("Created post Id:" + id);
        System.out.println("Response: " + response.body().asString());

        Assert.assertTrue(id > 0,"ID should be greater than 0");
    }

    @Test
    public void updateUserTest(){
        String responseBody = "{\n" +
            "   \"name\":   \"Saurabh Surve\",\n" +
            "   \"job\":   \"Senior QA Lead\"\n" +
            "}";
        Response response = given()
                .contentType("application/json")
                .body(responseBody)
                .when()
                .put("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String name = response.jsonPath().getString("name");
        String job = response.jsonPath().getString("job");

        System.out.println("Updated post Name: " + name);
        System.out.println("Updated post. Job: " + job);

        Assert.assertEquals(name, "Saurabh Surve","Name should be updated");
        Assert.assertEquals(job, "Senior QA Lead","Job should be updated");
    }

    @Test
    public void deleteUserTest(){
        Response response = given()
                .when()
                .delete("/posts/1")
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("Deleted status code: " + response.statusCode());
        System.out.println("Deleted response: " + response.body().asString());
        Assert.assertEquals(response.statusCode(),200,"Deleted status code should be 200");

    }
}
