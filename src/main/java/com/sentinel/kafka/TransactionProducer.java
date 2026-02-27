package com.sentinel.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TransactionProducer {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(TransactionProducer.class);


    private final KafkaTemplate<String, String> kafkaTemplate;

    public TransactionProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Value("${sentinel.kafka.topic:transactions}")
    private String topic;

    public void sendTransaction(String key, String payload, String correlationId) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, payload);
        record.headers().add("X-Correlation-ID", correlationId.getBytes(StandardCharsets.UTF_8));

        kafkaTemplate.send(record)
            .whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("failed to send transaction to kafka topic={}", topic, ex);
                } else {
                    log.debug("sent transaction to kafka partition={} offset={}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
                }
            });
    }
}
