package com.squeed.mockless.service.kafka;

import io.quarkus.arc.config.ConfigProperties;
import io.quarkus.runtime.annotations.ConfigItem;

import java.util.List;

@ConfigProperties(prefix = "kafka")
public class KafkaConfiguration {

    @ConfigItem(defaultValue = "localhost:9092")
    public List<String> bootstrapServers;

    @ConfigItem
    public String clientId;
}
