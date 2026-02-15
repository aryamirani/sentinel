package com.sentinel.service;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class VelocityService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VelocityService.class);


    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public VelocityService(@org.springframework.beans.factory.annotation.Qualifier("reactiveStringRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static final Duration WINDOW_DURATION = Duration.ofMinutes(5);
    private static final long VELOCITY_THRESHOLD = 10;

    public Mono<Long> incrementAndGetCount(String entityId) {
        String key = "velocity:" + entityId;
        return redisTemplate.opsForValue().increment(key)
            .flatMap(count -> {
                if (count == 1) {
                    return redisTemplate.expire(key, WINDOW_DURATION).thenReturn(count);
                }
                return Mono.just(count);
            });
    }

    // checks if entity has exceeded the sliding window threshold
    public Mono<Boolean> isVelocityBreached(String entityId) {
        return incrementAndGetCount(entityId)
            .map(count -> count > VELOCITY_THRESHOLD);
    }

    public Mono<Long> getCurrentCount(String entityId) {
        String key = "velocity:" + entityId;
        return redisTemplate.opsForValue().get(key)
            .map(Long::parseLong)
            .defaultIfEmpty(0L);
    }
}
