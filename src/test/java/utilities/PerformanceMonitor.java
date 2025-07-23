package utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Performance monitoring utility to track test execution metrics
 * Helps identify bottlenecks and optimize test performance
 */
public class PerformanceMonitor {
    
    private static final ConcurrentHashMap<String, Long> testStartTimes = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Long> testExecutionTimes = new ConcurrentHashMap<>();
    private static final AtomicLong totalTestTime = new AtomicLong(0);
    private static final AtomicLong testCount = new AtomicLong(0);
    
    private PerformanceMonitor() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Start timing a test method
     * @param testName Name of the test method
     */
    @Step("Starting performance monitoring for: {0}")
    public static void startTest(String testName) {
        long startTime = System.currentTimeMillis();
        testStartTimes.put(testName, startTime);
        LoggerUtils.debug("Performance monitoring started for: " + testName);
    }
    
    /**
     * End timing a test method and calculate execution time
     * @param testName Name of the test method
     * @return Execution time in milliseconds
     */
    @Step("Ending performance monitoring for: {0}")
    public static long endTest(String testName) {
        long endTime = System.currentTimeMillis();
        Long startTime = testStartTimes.get(testName);
        
        if (startTime != null) {
            long executionTime = endTime - startTime;
            testExecutionTimes.put(testName, executionTime);
            totalTestTime.addAndGet(executionTime);
            testCount.incrementAndGet();
            
            LoggerUtils.info("Test '" + testName + "' executed in " + executionTime + "ms");
            
            // Add to Allure report
            Allure.addAttachment("Execution Time", "text/plain", 
                    "Test: " + testName + "\nExecution Time: " + executionTime + "ms");
            
            // Clean up
            testStartTimes.remove(testName);
            
            return executionTime;
        } else {
            LoggerUtils.warn("No start time found for test: " + testName);
            return 0;
        }
    }
    
    /**
     * Get execution time for a specific test
     * @param testName Name of the test method
     * @return Execution time in milliseconds, or 0 if not found
     */
    public static long getTestExecutionTime(String testName) {
        return testExecutionTimes.getOrDefault(testName, 0L);
    }
    
    /**
     * Get average test execution time
     * @return Average execution time in milliseconds
     */
    public static double getAverageExecutionTime() {
        long count = testCount.get();
        if (count == 0) {
            return 0.0;
        }
        return (double) totalTestTime.get() / count;
    }
    
    /**
     * Get total execution time for all tests
     * @return Total execution time in milliseconds
     */
    public static long getTotalExecutionTime() {
        return totalTestTime.get();
    }
    
    /**
     * Get total number of tests executed
     * @return Number of tests
     */
    public static long getTestCount() {
        return testCount.get();
    }
    
    /**
     * Generate performance summary report
     * @return Performance summary as string
     */
    @Step("Generating performance summary")
    public static String generatePerformanceSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== PERFORMANCE SUMMARY ===\n");
        summary.append("Total Tests: ").append(getTestCount()).append("\n");
        summary.append("Total Execution Time: ").append(getTotalExecutionTime()).append("ms\n");
        summary.append("Average Execution Time: ").append(String.format("%.2f", getAverageExecutionTime())).append("ms\n");
        summary.append("=== INDIVIDUAL TEST TIMES ===\n");
        
        testExecutionTimes.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> summary.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("ms\n"));
        
        String summaryString = summary.toString();
        LoggerUtils.info("Performance Summary:\n" + summaryString);
        
        // Add to Allure report
        Allure.addAttachment("Performance Summary", "text/plain", summaryString);
        
        return summaryString;
    }
    
    /**
     * Reset all performance metrics
     */
    public static void reset() {
        testStartTimes.clear();
        testExecutionTimes.clear();
        totalTestTime.set(0);
        testCount.set(0);
        LoggerUtils.info("Performance metrics reset");
    }
    
    /**
     * Check if a test is taking too long and log warning
     * @param testName Name of the test method
     * @param thresholdMs Threshold in milliseconds
     */
    public static void checkPerformanceThreshold(String testName, long thresholdMs) {
        Long startTime = testStartTimes.get(testName);
        if (startTime != null) {
            long currentTime = System.currentTimeMillis();
            long runningTime = currentTime - startTime;
            
            if (runningTime > thresholdMs) {
                String warning = "Test '" + testName + "' is running longer than expected: " + 
                               runningTime + "ms (threshold: " + thresholdMs + "ms)";
                LoggerUtils.warn(warning);
                Allure.addAttachment("Performance Warning", "text/plain", warning);
            }
        }
    }
}