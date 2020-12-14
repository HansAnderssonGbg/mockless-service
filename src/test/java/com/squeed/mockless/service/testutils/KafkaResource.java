package com.squeed.mockless.service.testutils;

import com.opentable.kafka.embedded.EmbeddedKafkaBroker;
import com.opentable.kafka.embedded.EmbeddedKafkaBuilder;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class KafkaResource implements QuarkusTestResourceLifecycleManager {

    private static EmbeddedKafkaBroker ekb;

    private static KafkaConsumer<String, String> customerChangeConsumer;

    private static final String CUSTOMER_CHANGE_TOPIC = "mockless-dev.customerchange";

    @Override
    public Map<String, String> start() {
        ekb = new EmbeddedKafkaBuilder().withTopics(CUSTOMER_CHANGE_TOPIC).start();

        customerChangeConsumer = ekb.createConsumer("test-test-3");
        customerChangeConsumer.subscribe(Collections.singletonList(CUSTOMER_CHANGE_TOPIC));

        System.out.println("kafka connect: " + ekb.getKafkaBrokerConnect());
        return Map.of("kafka.bootstrap-servers", ekb.getKafkaBrokerConnect(),
            "kafka.bootstrap.servers", ekb.getKafkaBrokerConnect());
    }

    @Override
    @SneakyThrows
    public void stop() {
        customerChangeConsumer.close();;
        ekb.close();
    }

    public static String pollChanges() {
        ConsumerRecords<String, String> records = customerChangeConsumer.poll(Duration.ofSeconds(1));
        int i = 0;
        while(records.isEmpty()) {
            records = customerChangeConsumer.poll(Duration.ofSeconds(1));
            if (i++ > 3) break;
        }
        List<String> changes = new ArrayList<>();
        records.forEach(x -> changes.add(x.value()));
        return changes.isEmpty() ? "" : changes.get(0);
    }

}
