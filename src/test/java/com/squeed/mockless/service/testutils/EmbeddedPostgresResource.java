package com.squeed.mockless.service.testutils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.zonky.test.db.postgres.junit5.EmbeddedPostgresExtension;
import io.zonky.test.db.postgres.junit5.SingleInstancePostgresExtension;
import lombok.SneakyThrows;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.net.URL;
import java.sql.Connection;
import java.util.Map;

public class EmbeddedPostgresResource implements QuarkusTestResourceLifecycleManager {

    private static final SingleInstancePostgresExtension spg = EmbeddedPostgresExtension.singleInstance()
        .customize(builder -> builder.setPort(65339));

    @Override
    @SneakyThrows
    public Map<String, String> start() {
        spg.beforeTestExecution(null);
        String jdbcUrl = spg.getEmbeddedPostgres().getJdbcUrl("mockless_service", "postgres");
        String flywayJdbcUrl = spg.getEmbeddedPostgres().getJdbcUrl("postgres", "postgres");
        executeSqlScript();
        return Map.of(
            "quarkus.datasource.jdbc.url", jdbcUrl + "&stringtype=unspecified",
            "quarkus.datasource.owner.jdbc.url", flywayJdbcUrl + "&stringtype=unspecified");
    }

    private void executeSqlScript() {
        ScriptRunner sr = new ScriptRunner(getConnection());
        sr.runScript(reader("test_specifics.sql"));
    }

    @SneakyThrows
    private static Connection getConnection() {
        return spg.getEmbeddedPostgres().getPostgresDatabase().getConnection();
    }

    @SneakyThrows
    private static Reader reader(String fileStr) {
        URL resource = EmbeddedPostgresResource.class.getClassLoader().getResource(fileStr);
        String file = resource.getFile();
        return new BufferedReader(new FileReader(file));
    }

    @Override
    public void stop() {
        spg.afterTestExecution(null);
    }

}
