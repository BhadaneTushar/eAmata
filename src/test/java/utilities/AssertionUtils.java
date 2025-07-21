package utilities;

import io.qameta.allure.Step;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

/**
 * Utility class for assertions with enhanced logging and reporting
 */
public class AssertionUtils {
    
    private AssertionUtils() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Assert that two strings are equal
     * @param actual Actual value
     * @param expected Expected value
     * @param message Assertion message
     */
    @Step("Asserting that \"{actual}\" equals \"{expected}\": {message}")
    public static void assertEquals(String actual, String expected, String message) {
        try {
            Assert.assertEquals(actual, expected, message);
            LoggerUtils.info("PASS: " + message + " - Expected: \"" + expected + "\", Actual: \"" + actual + "\"");
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message + " - Expected: \"" + expected + "\", Actual: \"" + actual + "\"");
            throw e;
        }
    }
    
    /**
     * Assert that a condition is true
     * @param condition Condition to check
     * @param message Assertion message
     */
    @Step("Asserting that condition is true: {message}")
    public static void assertTrue(boolean condition, String message) {
        try {
            Assert.assertTrue(condition, message);
            LoggerUtils.info("PASS: " + message);
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message);
            throw e;
        }
    }
    
    /**
     * Assert that a condition is false
     * @param condition Condition to check
     * @param message Assertion message
     */
    @Step("Asserting that condition is false: {message}")
    public static void assertFalse(boolean condition, String message) {
        try {
            Assert.assertFalse(condition, message);
            LoggerUtils.info("PASS: " + message);
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message);
            throw e;
        }
    }
    
    /**
     * Assert that an object is not null
     * @param object Object to check
     * @param message Assertion message
     */
    @Step("Asserting that object is not null: {message}")
    public static void assertNotNull(Object object, String message) {
        try {
            Assert.assertNotNull(object, message);
            LoggerUtils.info("PASS: " + message);
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message);
            throw e;
        }
    }
    
    /**
     * Assert that an element is displayed
     * @param element WebElement to check
     * @param message Assertion message
     */
    @Step("Asserting that element is displayed: {message}")
    public static void assertElementDisplayed(WebElement element, String message) {
        try {
            Assert.assertTrue(ElementActions.isDisplayed(element), message);
            LoggerUtils.info("PASS: Element is displayed - " + message);
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: Element is not displayed - " + message);
            throw e;
        }
    }
    
    /**
     * Assert that an element contains text
     * @param element WebElement to check
     * @param expectedText Text to check for
     * @param message Assertion message
     */
    @Step("Asserting that element contains text \"{expectedText}\": {message}")
    public static void assertElementContainsText(WebElement element, String expectedText, String message) {
        String actualText = ElementActions.getText(element);
        try {
            Assert.assertTrue(actualText.contains(expectedText), 
                    message + " - Expected to contain: \"" + expectedText + "\", Actual: \"" + actualText + "\"");
            LoggerUtils.info("PASS: " + message + " - Text \"" + expectedText + "\" found in \"" + actualText + "\"");
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message + " - Text \"" + expectedText + "\" not found in \"" + actualText + "\"");
            throw e;
        }
    }
    
    /**
     * Assert that an element has exact text
     * @param element WebElement to check
     * @param expectedText Text to check for
     * @param message Assertion message
     */
    @Step("Asserting that element has exact text \"{expectedText}\": {message}")
    public static void assertElementHasText(WebElement element, String expectedText, String message) {
        String actualText = ElementActions.getText(element);
        try {
            Assert.assertEquals(actualText, expectedText, 
                    message + " - Expected: \"" + expectedText + "\", Actual: \"" + actualText + "\"");
            LoggerUtils.info("PASS: " + message + " - Expected: \"" + expectedText + "\", Actual: \"" + actualText + "\"");
        } catch (AssertionError e) {
            LoggerUtils.error("FAIL: " + message + " - Expected: \"" + expectedText + "\", Actual: \"" + actualText + "\"");
            throw e;
        }
    }
    
    /**
     * Soft assert that two strings are equal (doesn't throw exception)
     * @param actual Actual value
     * @param expected Expected value
     * @param message Assertion message
     * @return true if assertion passes, false otherwise
     */
    @Step("Soft asserting that \"{actual}\" equals \"{expected}\": {message}")
    public static boolean softAssertEquals(String actual, String expected, String message) {
        boolean result = actual.equals(expected);
        if (result) {
            LoggerUtils.info("PASS: " + message + " - Expected: \"" + expected + "\", Actual: \"" + actual + "\"");
        } else {
            LoggerUtils.warn("SOFT FAIL: " + message + " - Expected: \"" + expected + "\", Actual: \"" + actual + "\"");
        }
        return result;
    }
    
    /**
     * Soft assert that a condition is true (doesn't throw exception)
     * @param condition Condition to check
     * @param message Assertion message
     * @return true if assertion passes, false otherwise
     */
    @Step("Soft asserting that condition is true: {message}")
    public static boolean softAssertTrue(boolean condition, String message) {
        if (condition) {
            LoggerUtils.info("PASS: " + message);
        } else {
            LoggerUtils.warn("SOFT FAIL: " + message);
        }
        return condition;
    }
} 