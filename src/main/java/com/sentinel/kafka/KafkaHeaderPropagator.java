package com.sentinel.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Component
public class KafkaHeaderPropagator {

    private static final String[] PROPAGATED_HEADERS = {
        "X-Correlation-ID",
        "X-Request-Source",
        "X-Trace-ID"
    };

    public Map<String, String> extractHeaders(ConsumerRecord<String, String> record) {
        Map<String, String> headers = new HashMap<>();
        for (String headerName : PROPAGATED_HEADERS) {
            Header header = record.headers().lastHeader(headerName);
            if (header != null) {
                headers.put(headerName, new String(header.value(), StandardCharsets.UTF_8));
            }
        }
        return headers;
    }
}
