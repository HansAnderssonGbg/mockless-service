package com.squeed.mockless.service.testutils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squeed.mockless.service.api.model.Order;
import io.restassured.http.ContentType;
import io.specto.hoverfly.junit.core.SimulationSource;
import io.specto.hoverfly.junit.core.model.RequestFieldMatcher;
import io.specto.hoverfly.junit.dsl.RequestMatcherBuilder;
import io.specto.hoverfly.junit.dsl.ResponseCreators;
import lombok.SneakyThrows;

import javax.ws.rs.core.HttpHeaders;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static io.specto.hoverfly.junit.core.SimulationSource.dsl;
import static io.specto.hoverfly.junit.dsl.HoverflyDsl.service;

public class OrderServiceSimulation {

    private String url;
    private RequestMatcherBuilder builder;

    public static OrderServiceSimulation orderService(String basePath) {
        var service = new OrderServiceSimulation();
        service.url = basePath;
        return service;
    }

    public static OrderServiceSimulation orderService() {
        return orderService("http://ourown.domain.com");
    }

    public OrdersResponseHandler getOrders(Integer id) {
        this.builder = service(url)
            .get(RequestFieldMatcher.newGlobMatcher("/orders/*"));

        return new OrdersResponseHandler();
    }

    public class OrdersResponseHandler {
        @SneakyThrows
        public SimulationSource successfullyReturns(Order.OrderBuilder... orders) {
            List<Order> builtOrders = Arrays.stream(orders).map(Order.OrderBuilder::build).collect(Collectors.toList());
            return dsl(
                builder.willReturn(ResponseCreators.success()
                    .header(HttpHeaders.CONTENT_TYPE, ContentType.JSON.toString())
                    .body(serialize(builtOrders))));
        }

    }

    public static Order.OrderBuilder order(int id, Order.OrderRow.OrderRowBuilder... rows) {
        return Order.builder()
            .id(id)
            .orderRows(
                Arrays.stream(rows)
                    .map(Order.OrderRow.OrderRowBuilder::build)
                    .collect(Collectors.toList()));
    }

    public static Order.OrderRow.OrderRowBuilder row() {
        return Order.OrderRow.builder();
    }

    @SneakyThrows
    private static String serialize(Object o) {
        return new ObjectMapper().writeValueAsString(o);
    }
}
