package com.squeed.mockless.service;

import com.squeed.mockless.service.api.model.Customer;
import com.squeed.mockless.service.api.model.Order;
import com.squeed.mockless.service.testutils.EmbeddedPostgresResource;
import com.squeed.mockless.service.testutils.HoverflyResource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.squeed.mockless.service.testutils.HoverflyResource.externalServices;
import static com.squeed.mockless.service.testutils.OrderServiceSimulation.orderService;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(EmbeddedPostgresResource.class)
@QuarkusTestResource(HoverflyResource.class)
public class WhenCustomerIsCreated {

    static Customer customer;
    @BeforeAll
    static void whenCustomerIsCreated() {
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
    public void thenReadCustomerRetrievesIt() {
        given()
        .when()
            .get("/customers")
        .then()
            .assertThat()
                .statusCode(200)
                .body("[0].firstName", is("Cool"))
                .body("[0].lastName", is("Person"));
    }

    @Test
    public void thenOrdersForCustomerCanBeretrieved() {
        given()
            .spec(externalServices(
                orderService()
                    .getOrders(customer.getId())
                    .successfullyReturns(order())))
            .pathParam("id", customer.getId())
            .when()
            .get("customers/{id}/orders")
            .then()
            .assertThat()
            .statusCode(200)
            .body("[0].id", is(1));
    }

    private Order.OrderBuilder order() {
        return Order.builder()
                .id(1)
                .orderRows(Arrays.asList(
                    Order.OrderRow.builder()
                        .sku("1001-A")
                        .description("Shirt")
                        .quantity(2.0)
                        .build()
                ));
    }

}
