package com.squeed.mockless.service.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.concurrent.Future;

@ApplicationScoped
public class CustomerChangeProducer {

    @Inject
    KafkaProducer<String, String> producer;

    @ConfigProperty(name = "topics.customerchange")
    String customerChangeTopic;


    public Future<RecordMetadata> send(Long customerId) {
        return producer.send(new ProducerRecord<>(customerChangeTopic, "key", customerId.toString()));
    }

}
