package com.squeed.mockless.service;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.testutils.EmbeddedPostgresResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;
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
                .body("size()", is(1))
                .body("[0].name", is("a"))
                .body("[0].category", is("cat2"))
                .body("[0].resource", is("cat2:a"))
                .body("[0].actions", hasItem("write"));
    }

    @Test
    public void filterByCategory() {
        given()
            .queryParam("sort", "test")
        .when()
            .get("/customers")
        .then()
            .assertThat()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("a"))
                .body("[0].category", is("cat2"))
                .body("[0].resource", is("cat2:a"))
                .body("[0].actions", hasItem("write"));
    }
}
