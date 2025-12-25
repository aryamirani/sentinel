package com.sentinel.gateway;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sentinel.model.AlertPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Slf4j
@Component
public class AlertWebSocketHandler implements WebSocketHandler {

    private final Sinks.Many<AlertPayload> alertSink = Sinks.many().multicast().onBackpressureBuffer();
    private final ObjectMapper objectMapper;

    public AlertWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Flux<WebSocketMessage> output = alertSink.asFlux()
            .map(alert -> {
                try {
                    return session.textMessage(objectMapper.writeValueAsString(alert));
                } catch (JsonProcessingException e) {
                    log.error("failed to serialize alert", e);
                    return session.textMessage("{}");
                }
            });
        return session.send(output);
    }

    // used by alert service to push alerts to connected clients
    public void broadcast(AlertPayload alert) {
        alertSink.tryEmitNext(alert);
    }
}
