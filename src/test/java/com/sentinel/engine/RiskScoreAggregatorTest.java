package com.sentinel.engine;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RiskScoreAggregatorTest {

    private final RiskScoreAggregator aggregator = new RiskScoreAggregator();

    @Test
    void shouldAggregateScoresWithWeights() {
        double result = aggregator.aggregate(50.0, 50.0, 50.0);
        assertEquals(50.0, result, 0.01);
    }

    @Test
    void shouldCapAtHundred() {
        double result = aggregator.aggregate(100.0, 100.0, 100.0);
        assertEquals(100.0, result);
    }

    @Test
    void shouldReturnZeroForZeroInputs() {
        double result = aggregator.aggregate(0.0, 0.0, 0.0);
        assertEquals(0.0, result);
    }

    @Test
    void shouldBlockHighScores() {
        String action = aggregator.determineAction(85.0);
        assertEquals("BLOCK", action);
    }

    @Test
    void shouldReviewMediumScores() {
        String action = aggregator.determineAction(50.0);
        assertEquals("REVIEW", action);
    }

    @Test
    void shouldAllowLowScores() {
        String action = aggregator.determineAction(15.0);
        assertEquals("ALLOW", action);
    }

    @Test
    void shouldHandleBoundaryScores() {
        assertEquals("BLOCK", aggregator.determineAction(70.0));
        assertEquals("REVIEW", aggregator.determineAction(30.0));
        assertEquals("ALLOW", aggregator.determineAction(29.9));
    }
}
