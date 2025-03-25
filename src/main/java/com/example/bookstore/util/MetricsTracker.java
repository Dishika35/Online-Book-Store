package com.example.bookstore.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MetricsTracker {

    // Logger to print tracking messages for performance monitoring
    private static final Logger log = LoggerFactory.getLogger(MetricsTracker.class);

    // Name of the operation being tracked
    private final String operationName;
    // Timestamp when the operation started
    private final long startTime;

    // Constructor that initializes the operation name and records the start time
    public MetricsTracker(String operationName) {
        this.operationName = operationName;
        this.startTime = System.currentTimeMillis();  // Record start time
        log.info("Starting operation: {}", operationName);
    }

    // Call this method to stop tracking and log the duration of the operation
    public void stop() {
        long endTime = System.currentTimeMillis();  // Record end time
        long duration = endTime - startTime;          // Calculate total duration
        log.info("Operation '{}' completed in {} ms", operationName, duration);
        // Log a divider to separate this operation's log from the next
        log.info("--------------------------------------------------\n");
    }
}
