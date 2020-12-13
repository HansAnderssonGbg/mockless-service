package com.squeed.mockless.service.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order {

    Integer id;
    List<OrderRow> orderRows;

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class OrderRow {
        Integer id;
        String sku;
        String description;
        Double quantity;
    }

}
