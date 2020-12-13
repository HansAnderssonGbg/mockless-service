package com.squeed.mockless.service.testutils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.SingleInstancePostgresExtension;
import lombok.SneakyThrows;

import java.util.Map;

public class EmbeddedPostgresResource implements QuarkusTestResourceLifecycleManager {

    private static SingleInstancePostgresExtension spg = EmbeddedPostgresExtension.singleInstance()
        .customize(builder -> builder.setPort(65339));

    @Override
    @SneakyThrows
    public Map<String, String> start() {
        spg.beforeTestExecution(null);
        String jdbcUrl = spg.getEmbeddedPostgres().getJdbcUrl("mockless_service", "postgres");
        String flywayJdbcUrl = spg.getEmbeddedPostgres().getJdbcUrl("postgres", "postgres");
        return Map.of(
            "quarkus.datasource.jdbc.url", jdbcUrl + "&stringtype=unspecified",
            "quarkus.datasource.owner.jdbc.url", flywayJdbcUrl + "&stringtype=unspecified");
    }

    @Override
    public void stop() {
        spg.afterTestExecution(null);
    }

}
