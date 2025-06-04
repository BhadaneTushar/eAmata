package utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

/**
 * Utility class for centralized logging throughout the test framework.
 * Provides enhanced logging capabilities with context information and stack
 * traces.
 */
public class LoggerUtils {
    private static final Logger logger = LogManager.getLogger(LoggerUtils.class);
    private static final String CONTEXT_CLASS = "class";
    private static final String CONTEXT_METHOD = "method";
    private static final String CONTEXT_THREAD = "thread";

    /**
     * Initializes logging context for a specific class and method.
     * 
     * @param className  The name of the class
     * @param methodName The name of the method
     */
    public static void initializeContext(String className, String methodName) {
        ThreadContext.put(CONTEXT_CLASS, className);
        ThreadContext.put(CONTEXT_METHOD, methodName);
        ThreadContext.put(CONTEXT_THREAD, Thread.currentThread().getName());
    }

    /**
     * Clears the logging context.
     */
    public static void clearContext() {
        ThreadContext.clearAll();
    }

    /**
     * Logs an informational message with context.
     * 
     * @param message The message to log
     */
    public static void info(String message) {
        logger.info(formatMessage(message));
    }

    /**
     * Logs a warning message with context.
     * 
     * @param message The message to log
     */
    public static void warn(String message) {
        logger.warn(formatMessage(message));
    }

    /**
     * Logs an error message with context and stack trace.
     * 
     * @param message   The message to log
     * @param throwable The exception to log
     */
    public static void error(String message, Throwable throwable) {
        logger.error(formatMessage(message), throwable);
    }

    /**
     * Logs an error message with context.
     * 
     * @param message The message to log
     */
    public static void error(String message) {
        logger.error(formatMessage(message));
    }

    /**
     * Logs a debug message with context.
     * 
     * @param message The message to log
     */
    public static void debug(String message) {
        logger.debug(formatMessage(message));
    }

    /**
     * Logs a trace message with context.
     * 
     * @param message The message to log
     */
    public static void trace(String message) {
        logger.trace(formatMessage(message));
    }

    /**
     * Formats the log message with context information.
     * 
     * @param message The original message
     * @return The formatted message with context
     */
    private static String formatMessage(String message) {
        StringBuilder formattedMessage = new StringBuilder();

        if (ThreadContext.get(CONTEXT_CLASS) != null) {
            formattedMessage.append("[").append(ThreadContext.get(CONTEXT_CLASS)).append("] ");
        }

        if (ThreadContext.get(CONTEXT_METHOD) != null) {
            formattedMessage.append("[").append(ThreadContext.get(CONTEXT_METHOD)).append("] ");
        }

        if (ThreadContext.get(CONTEXT_THREAD) != null) {
            formattedMessage.append("[").append(ThreadContext.get(CONTEXT_THREAD)).append("] ");
        }

        formattedMessage.append(message);
        return formattedMessage.toString();
    }

    /**
     * Logs the start of a test or operation.
     * 
     * @param operationName The name of the operation
     */
    public static void logStart(String operationName) {
        info("Starting: " + operationName);
    }

    /**
     * Logs the completion of a test or operation.
     * 
     * @param operationName The name of the operation
     */
    public static void logEnd(String operationName) {
        info("Completed: " + operationName);
    }

    /**
     * Logs the failure of a test or operation.
     * 
     * @param operationName The name of the operation
     * @param throwable     The exception that caused the failure
     */
    public static void logFailure(String operationName, Throwable throwable) {
        error("Failed: " + operationName, throwable);
    }
}