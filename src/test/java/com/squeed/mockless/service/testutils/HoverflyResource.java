package com.squeed.mockless.service.testutils;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.specto.hoverfly.junit.core.Hoverfly;
import io.specto.hoverfly.junit.core.HoverflyMode;
import io.specto.hoverfly.junit.core.SimulationSource;

import java.util.Map;

import static io.specto.hoverfly.junit.core.HoverflyConfig.localConfigs;

public class HoverflyResource  implements QuarkusTestResourceLifecycleManager {

    private static final Hoverfly hoverfly = new Hoverfly(localConfigs(), HoverflyMode.SIMULATE);

    @Override
    public Map<String, String> start() {
        //hoverfly.getHoverflyConfig().setTlsVerificationDisabled(true);
        hoverfly.start();
        return null;
    }

    public void inject(Object testInstance) {
        hoverfly.reset();
    }

    @Override
    public void stop() {
        hoverfly.close();
    }

    public static RequestSpecification externalServices(SimulationSource simulationSources) {
        hoverfly.simulate(simulationSources);
        return new RequestSpecBuilder().build();
    }

}