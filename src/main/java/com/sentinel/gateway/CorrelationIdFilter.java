package com.sentinel.gateway;

import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Order(-1)
public class CorrelationIdFilter implements WebFilter {

    private static final String CORRELATION_HEADER = "X-Correlation-ID";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String correlationId = exchange.getRequest().getHeaders().getFirst(CORRELATION_HEADER);
        if (correlationId == null || correlationId.isEmpty()) {
            correlationId = UUID.randomUUID().toString();
        }
        exchange.getResponse().getHeaders().add(CORRELATION_HEADER, correlationId);
        String cid = correlationId;
        return chain.filter(exchange)
            .contextWrite(ctx -> ctx.put("correlationId", cid))
            .doFirst(() -> MDC.put("correlationId", cid))
            .doFinally(signal -> MDC.remove("correlationId"));
    }
}
