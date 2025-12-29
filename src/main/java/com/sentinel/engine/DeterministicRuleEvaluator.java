package com.sentinel.engine;

import com.sentinel.model.Customer;
import com.sentinel.model.Transaction;
import com.sentinel.repository.CustomerRepository;
import com.sentinel.service.VelocityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeterministicRuleEvaluator {

    private final VelocityService velocityService;
    private final CustomerRepository customerRepository;

    private static final double HIGH_AMOUNT_THRESHOLD = 10000.0;
    private static final double VELOCITY_SCORE = 40.0;
    private static final double DEVICE_ANOMALY_SCORE = 30.0;
    private static final double AMOUNT_SPIKE_SCORE = 25.0;

    public Mono<Double> evaluate(Transaction transaction) {
        return Mono.zip(
            checkVelocity(transaction),
            checkDeviceAnomaly(transaction),
            checkAmountSpike(transaction)
        ).map(tuple -> {
            double velocityRisk = tuple.getT1();
            double deviceRisk = tuple.getT2();
            double amountRisk = tuple.getT3();
            double total = Math.min(100.0, velocityRisk + deviceRisk + amountRisk);
            log.debug("rule evaluation: velocity={} device={} amount={} total={}",
                velocityRisk, deviceRisk, amountRisk, total);
            return total;
        });
    }

    private Mono<Double> checkVelocity(Transaction transaction) {
        return velocityService.isVelocityBreached(transaction.getEntityId())
            .map(breached -> breached ? VELOCITY_SCORE : 0.0);
    }

    private Mono<Double> checkDeviceAnomaly(Transaction transaction) {
        if (transaction.getDevice() == null || transaction.getDevice().getFingerprintHash() == null) {
            return Mono.just(0.0);
        }

        return customerRepository.findByEntityId(transaction.getEntityId())
            .map(customer -> {
                if (customer.getKnownDevices() == null) {
                    return DEVICE_ANOMALY_SCORE;
                }
                boolean knownDevice = customer.getKnownDevices().stream()
                    .anyMatch(d -> d.getFingerprintHash().equals(
                        transaction.getDevice().getFingerprintHash()));
                if (!knownDevice) {
                    return DEVICE_ANOMALY_SCORE;
                }
                if (transaction.getDevice().isVpnDetected() || transaction.getDevice().isEmulatorDetected()) {
                    return DEVICE_ANOMALY_SCORE * 0.5;
                }
                return 0.0;
            })
            .defaultIfEmpty(DEVICE_ANOMALY_SCORE);
    }

    private Mono<Double> checkAmountSpike(Transaction transaction) {
        return customerRepository.findByEntityId(transaction.getEntityId())
            .map(customer -> {
                if (transaction.getAmount() > customer.getMaxSingleTransaction() * 2) {
                    return AMOUNT_SPIKE_SCORE;
                }
                if (transaction.getAmount() > HIGH_AMOUNT_THRESHOLD) {
                    return AMOUNT_SPIKE_SCORE * 0.6;
                }
                return 0.0;
            })
            .defaultIfEmpty(transaction.getAmount() > HIGH_AMOUNT_THRESHOLD ? AMOUNT_SPIKE_SCORE : 0.0);
    }
}
