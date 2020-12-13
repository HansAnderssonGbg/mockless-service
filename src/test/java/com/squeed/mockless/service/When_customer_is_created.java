package com.squeed.mockless.service;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.testutils.EmbeddedPostgresResource;
import com.squeed.mockless.service.testutils.HoverflyResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static com.squeed.mockless.service.testutils.HoverflyResource.externalServices;
import static com.squeed.mockless.service.testutils.OrderServiceSimulation.orderService;
import static com.squeed.mockless.service.testutils.OrderServiceSimulation.order;
import static com.squeed.mockless.service.testutils.OrderServiceSimulation.row;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(EmbeddedPostgresResource.class)
@QuarkusTestResource(HoverflyResource.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class When_customer_is_created {

    static Customer customer;

    @BeforeAll
    static void when_customer_is_created() {
        customer =
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
                    .body("lastName", is("Person"))
                .extract().body().as(Customer.class);
    }

    @Test
    void it_shows_up_when_listing_customers() {
        when()
            .get("/customers")
        .then()
            .assertThat()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].firstName", is(customer.getFirstName()))
                .body("[0].lastName", is(customer.getLastName()));
    }

    @Test
    void it_can_be_retrieved_by_id() {
        given()
            .pathParam("id", customer.getId())
        .when()
            .get("/customers/{id}")
        .then()
            .assertThat()
                .statusCode(200)
                .body("firstName", is(customer.getFirstName()))
                .body("lastName", is(customer.getLastName()));
    }

    @Test
    void its_last_name_can_be_updated() {
        customer =
            given()
                .contentType(ContentType.JSON)
                .pathParam("id", customer.getId())
                .body(customer.toBuilder().lastName("Changed").build())
            .when()
                .put("/customers/{id}")
            .then()
                .assertThat()
                    .statusCode(200)
                    .body("firstName", is(customer.getFirstName()))
                    .body("lastName", is("Changed"))
                .extract().body().as(Customer.class);
    }

    @Test
    void its_orders_can_be_retrieved() {
        given()
            .spec(externalServices(
                orderService().getOrders(customer.getId()).successfullyReturns(
                    order(1,
                        row().sku("1001-A").description("Shirt").quantity(2.0),
                        row().sku("1023-B").description("Pants").quantity(1.0)
                    ),
                    order(2)
                )
            ))
            .pathParam("id", customer.getId())
        .when()
            .get("/customers/{id}/orders")
        .then()
            .assertThat()
                .statusCode(200)
                .body("[0].id", is(1))
                .body("[0].orderRows.sku", hasItems("1001-A", "1023-B"));
    }

    @AfterAll
    static void it_can_be_deleted() {
        given()
            .port(8081)
            .pathParam("id", customer.getId())
        .when()
            .delete("/customers/{id}")
        .then()
            .assertThat()
                .statusCode(204);

        given()
            .port(8081)
        .when()
            .get("/customers")
        .then()
            .assertThat()
                .body("size()", is(0));
    }

}
