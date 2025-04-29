package com.fitnessApp.userservice;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;


public class UserTest {
    @BeforeAll
    public static void beforeAll() {
        RestAssured.baseURI = "http://localhost:8080/api/users";
    }

    @Test
    public void shouldRegisterUserWithValidParams(){
        String registerLayout = """
                {
                    "email":"test@test3.com",
                    "password":"testpassword",
                    "firstName":"test"
                }
                """;

        given()
                .contentType("application/json")
                .body(registerLayout)
                .post("/register")
                .then()
                .statusCode(200);

    }

    @Test
    public void shouldReturnBadRequestWithInvalidParams(){
        String registerLayout = """
                {
                    "email":"test",
                    "password":"testpassword"
                }
                """;
        given()
        .contentType("application/json")
                .body(registerLayout)
                .post("/register")
                .then()
                .statusCode(400);
    }
}
