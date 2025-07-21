package utilities;

import org.openqa.selenium.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utility class for handling date selection in web applications.
 * Provides support for multiple date formats and improved error handling.
 */
public class DatePicker {
    private static final Logger logger = Logger.getLogger(DatePicker.class.getName());
    private final WebDriver driver;
    private String targetDateString;
    private static final Map<String, Month> MONTH_MAP = new HashMap<>();
    private static final Map<String, DateTimeFormatter> DATE_FORMATTERS = new HashMap<>();

    static {
        // Initialize month mappings
        MONTH_MAP.put("january", Month.JANUARY);
        MONTH_MAP.put("february", Month.FEBRUARY);
        MONTH_MAP.put("march", Month.MARCH);
        MONTH_MAP.put("april", Month.APRIL);
        MONTH_MAP.put("may", Month.MAY);
        MONTH_MAP.put("june", Month.JUNE);
        MONTH_MAP.put("july", Month.JULY);
        MONTH_MAP.put("august", Month.AUGUST);
        MONTH_MAP.put("september", Month.SEPTEMBER);
        MONTH_MAP.put("october", Month.OCTOBER);
        MONTH_MAP.put("november", Month.NOVEMBER);
        MONTH_MAP.put("december", Month.DECEMBER);
        MONTH_MAP.put("jan", Month.JANUARY);
        MONTH_MAP.put("feb", Month.FEBRUARY);
        MONTH_MAP.put("mar", Month.MARCH);
        MONTH_MAP.put("apr", Month.APRIL);
        MONTH_MAP.put("jun", Month.JUNE);
        MONTH_MAP.put("jul", Month.JULY);
        MONTH_MAP.put("aug", Month.AUGUST);
        MONTH_MAP.put("sep", Month.SEPTEMBER);
        MONTH_MAP.put("oct", Month.OCTOBER);
        MONTH_MAP.put("nov", Month.NOVEMBER);
        MONTH_MAP.put("dec", Month.DECEMBER);

        // Initialize date formatters
        DATE_FORMATTERS.put("dd/MM/yyyy", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        DATE_FORMATTERS.put("MM/dd/yyyy", DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        DATE_FORMATTERS.put("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        DATE_FORMATTERS.put("dd-MM-yyyy", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    /**
     * Constructor for DatePicker with default date format (dd/MM/yyyy).
     *
     * @param driver           The WebDriver instance
     * @param targetDateString The date string to select
     * @throws IllegalArgumentException if date format is invalid
     * @throws DateTimeParseException   if date string cannot be parsed
     */
    public DatePicker(WebDriver driver, String targetDateString) {
        this(driver, targetDateString, "dd/MM/yyyy");
        selectDate();
    }

    /**
     * Constructor for DatePicker.
     *
     * @param driver           The WebDriver instance
     * @param targetDateString The date string to select
     * @param dateFormat       The format of the date string
     * @throws IllegalArgumentException if date format is invalid
     * @throws DateTimeParseException   if date string cannot be parsed
     */
    public DatePicker(WebDriver driver, String targetDateString, String dateFormat) {
        this.driver = driver;
        this.targetDateString = targetDateString;

        if (!DATE_FORMATTERS.containsKey(dateFormat)) {
            throw new IllegalArgumentException("Unsupported date format: " + dateFormat);
        }

        try {
            LocalDate.parse(targetDateString, DATE_FORMATTERS.get(dateFormat));
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format for string: " + targetDateString,
                    targetDateString, 0, e);
        }
        selectDate();
    }

    /**
     * Selects the date in the calendar widget.
     *
     * @throws RuntimeException if date selection fails
     */
    public void selectDate() {
        try {
            DateTimeFormatter formatter = DATE_FORMATTERS.values().iterator().next();
            LocalDate targetDate = LocalDate.parse(targetDateString, formatter);
            int targetYear = targetDate.getYear();
            String targetMonth = targetDate.getMonth().name();
            int targetDay = targetDate.getDayOfMonth();

            openCalendarWidget();
            navigateToYear(targetYear);
            navigateToMonth(targetMonth);
            selectDay(targetDay);

            LoggerUtils.info("Date selected successfully: " + targetDateString);
        } catch (Exception e) {
            LoggerUtils.error("Failed to select date: " + targetDateString, e);
            throw new RuntimeException("Failed to select date: " + targetDateString, e);
        }
    }

    /**
     * Opens the calendar widget.
     *
     * @throws RuntimeException if calendar widget cannot be opened
     */
    private void openCalendarWidget() {
        try {
            String calendarIconXPath = "//button[@aria-label='Choose date']//*[name()='svg']";
            WebElement calendarIcon = driver.findElement(By.xpath(calendarIconXPath));
            calendarIcon.click();
            LoggerUtils.debug("Calendar widget opened successfully");
        } catch (Exception e) {
            LoggerUtils.error("Failed to open calendar widget", e);
            throw new RuntimeException("Failed to open calendar widget", e);
        }
    }

    /**
     * Navigates to the specified year in the calendar.
     *
     * @param targetYear The year to navigate to
     * @throws RuntimeException if year navigation fails
     */
    private void navigateToYear(int targetYear) {
        try {
            String yearDropdownXPath = "//button[@aria-label='calendar view is open, switch to year view']";
            WebElement yearDropdown = driver.findElement(By.xpath(yearDropdownXPath));
            
            // Use JavaScript to click the dropdown to avoid ElementClickInterceptedException
            JavascriptExecutor js = (JavascriptExecutor) driver;
            js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", yearDropdown);
            
            LoggerUtils.debug("Clicked year dropdown using JavaScript");
            Thread.sleep(1000); // Add a small delay after clicking
            
            // Try to find the year using various methods
            String yearOptionXPath = "//div[@role='radiogroup']/div/button[text()='" + targetYear + "']";
            WebElement yearElement = null;
            
            try {
                yearElement = driver.findElement(By.xpath(yearOptionXPath));
            } catch (NoSuchElementException e) {
                LoggerUtils.debug("Year not found with first selector, trying alternative selectors");
                
                // Try alternative XPath patterns
                String[] alternativeXPaths = {
                    "//div[contains(@class, 'year')]//button[contains(text(), '" + targetYear + "')]",
                    "//div[@role='radiogroup']//button[contains(text(), '" + targetYear + "')]",
                    "//*[contains(text(), '" + targetYear + "') and (local-name()='button' or local-name()='div')]"
                };
                
                for (String xpath : alternativeXPaths) {
                    try {
                        yearElement = driver.findElement(By.xpath(xpath));
                        if (yearElement != null) {
                            LoggerUtils.debug("Found year element using alternative selector: " + xpath);
                            break;
                        }
                    } catch (NoSuchElementException ex) {
                        // Continue to the next selector
                    }
                }
                
                // If year element still not found, try to use JavaScript to set the date directly
                if (yearElement == null) {
                    LoggerUtils.debug("Year not found with alternative selectors, trying to set date directly via JavaScript");
                    
                    // Format the target date as needed by the date picker
                    LocalDate targetDate = LocalDate.now().withYear(targetYear).withMonth(1).withDayOfMonth(1);
                    String formattedDate = targetDate.format(DateTimeFormatter.ISO_DATE); // Format as YYYY-MM-DD
                    
                    // Try to set date value directly using JavaScript
                    js.executeScript(
                        "var dateInputs = document.querySelectorAll('input[placeholder=\"MM-DD-YYYY\"]');" +
                        "if(dateInputs.length > 0) {" +
                        "  dateInputs[0].value = arguments[0];" +
                        "  var event = new Event('change', { bubbles: true });" +
                        "  dateInputs[0].dispatchEvent(event);" +
                        "}", formattedDate.replace("-", "/"));
                    
                    LoggerUtils.debug("Attempted to set date directly via JavaScript");
                    return;
                }
            }

            if (yearElement != null) {
                // Use JavaScript to click the year to avoid ElementClickInterceptedException
                js.executeScript("arguments[0].scrollIntoView(true); arguments[0].click();", yearElement);
                LoggerUtils.debug("Navigated to year: " + targetYear);
            } else {
                throw new NoSuchElementException("Target year element not found: " + targetYear);
            }
        } catch (Exception e) {
            LoggerUtils.error("Failed to navigate to year: " + targetYear, e);
            throw new RuntimeException("Failed to navigate to year: " + targetYear, e);
        }
    }

    /**
     * Navigates to the specified month in the calendar.
     *
     * @param targetMonthStr The month to navigate to
     * @throws RuntimeException if month navigation fails
     */
    private void navigateToMonth(String targetMonthStr) {
        try {
            Month targetMonthEnum = parseMonthName(targetMonthStr);
            String displayedMonthXPath = "//div[button[contains(@aria-label,'calendar view')]]/div";
            String nextMonthButtonXPath = "//button[@title='Next month']//*[name()='svg']";
            String previousMonthButtonXPath = "//button[@title='Previous month']//*[name()='svg']";
            JavascriptExecutor js = (JavascriptExecutor) driver;

            while (true) {
                WebElement element = driver.findElement(By.xpath(displayedMonthXPath));
                String displayedMonthText = (String) js.executeScript("return arguments[0].textContent;", element);
                String displayedMonthName = displayedMonthText.split(" ")[0];
                Month displayedMonthEnum = parseMonthName(displayedMonthName);

                if (displayedMonthEnum == targetMonthEnum) {
                    break;
                }

                if (displayedMonthEnum.getValue() < targetMonthEnum.getValue()) {
                    driver.findElement(By.xpath(nextMonthButtonXPath)).click();
                } else {
                    driver.findElement(By.xpath(previousMonthButtonXPath)).click();
                }
            }

            LoggerUtils.debug("Navigated to month: " + targetMonthStr);
        } catch (Exception e) {
            LoggerUtils.error("Failed to navigate to month: " + targetMonthStr, e);
            throw new RuntimeException("Failed to navigate to month: " + targetMonthStr, e);
        }
    }

    /**
     * Selects the specified day in the calendar.
     *
     * @param targetDay The day to select
     * @throws RuntimeException if day selection fails
     */
    private void selectDay(int targetDay) {
        try {
            Thread.sleep(3000);
            for (int i = 1; i <= 6; i++) {
                String dayElementXPath = "//div[@role='row' and @aria-rowindex='" + i + "']//button[text()='"
                        + targetDay + "']";
                try {
                    WebElement dayElement = driver.findElement(By.xpath(dayElementXPath));
                    if (dayElement != null) {
                        dayElement.click();
                        LoggerUtils.debug("Selected day: " + targetDay);
                        return;
                    }
                } catch (NoSuchElementException ex) {
                    LoggerUtils.debug("Day not found in row " + i + ", checking next row");
                }
            }
            throw new NoSuchElementException("Target day not found in the calendar: " + targetDay);
        } catch (Exception e) {
            LoggerUtils.error("Failed to select day: " + targetDay, e);
            throw new RuntimeException("Failed to select day: " + targetDay, e);
        }
    }

    /**
     * Parses a month name to a Month enum.
     *
     * @param monthName The name of the month
     * @return The corresponding Month enum
     * @throws IllegalArgumentException if month name is invalid
     */
    private Month parseMonthName(String monthName) {
        if (monthName == null || monthName.trim().isEmpty()) {
            throw new IllegalArgumentException("Month name is empty or null");
        }
        Month month = MONTH_MAP.get(monthName.toLowerCase());
        if (month == null) {
            throw new IllegalArgumentException("No matching Month for: " + monthName);
        }
        return month;
    }
}
