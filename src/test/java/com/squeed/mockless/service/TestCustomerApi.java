package com.squeed.mockless.service;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.testutils.EmbeddedPostgresResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(EmbeddedPostgresResource.class)
public class TestCustomerApi {

    @BeforeAll
    static void createCustomer() {
        given()
            .port(8081)
            .contentType(ContentType.JSON)
            .body(Customer.builder().firstName("Cool").lastName("Person").build())
        .when()
            .post("/customers")
        .then()
            .assertThat()
                .statusCode(200)
                .body("firstName", is("Cool"))
                .body("lastName", is("Person"));
    }

    @Test
    public void filterByCategory() {
        given()
        .when()
            .get("/customers")
        .then()
            .assertThat()
                .statusCode(200)
                .body("[0].firstName", is("Cool"))
                .body("[0].lastName", is("Person"));
    }
}
