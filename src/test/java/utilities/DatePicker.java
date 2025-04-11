package utilities;

import org.openqa.selenium.*;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
public class DatePicker {
    private static final Logger logger = Logger.getLogger(DatePicker.class.getName());
    private WebDriver driver;
    private String targetDateString;
    private static final Map<String, Month> MONTH_MAP = new HashMap<>();
    static {
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
    }
    private Month parseMonthName(String monthName) throws InterruptedException {
        //System.out.println("parseMonthName" + "===" + monthName);
        if (monthName == null || monthName.trim().isEmpty()) {
            throw new IllegalArgumentException("Month name is empty or null.");
        }
        Month month = MONTH_MAP.get(monthName.toLowerCase());
        if (month == null) {
            throw new IllegalArgumentException("No matching Month for: " + monthName);
        }
        return month;
    }
    public DatePicker(WebDriver driver, String targetDateString) throws InterruptedException {
        this.driver = driver;
        this.targetDateString = targetDateString;
        selectDate();
    }
    public void selectDate() throws InterruptedException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate targetDate = LocalDate.parse(targetDateString, dateFormatter);
        int targetYear = targetDate.getYear();
        String targetMonth = targetDate.getMonth().name();
        int targetDay = targetDate.getDayOfMonth();
        openCalendarWidget();
        navigateToYear(targetYear);
        navigateToMonth(targetMonth);
        selectDay(targetDay);
        logger.info("Date selected successfully: " + targetDateString);
    }
    private void openCalendarWidget() {
        String calendarIconXPath = "//button[@aria-label='Choose date']//*[name()='svg']";
        driver.findElement(By.xpath(calendarIconXPath)).click();
        logger.info("Calendar widget opened successfully.");
    }
    private void navigateToYear(int targetYear) throws InterruptedException {
        Thread.sleep(1000);
        String yearDropdownXPath = "//button[@aria-label='calendar view is open, switch to year view']";
        driver.findElement(By.xpath(yearDropdownXPath)).click();
        String yearOptionXPath = "//div[@role='radiogroup']/div/button[text()='" + targetYear + "']";
        WebElement yearElement = driver.findElement(By.xpath(yearOptionXPath));
        yearElement.click();
        logger.info("Navigated to Year: " + targetYear);
    }
    private void navigateToMonth(String targetMonthStr) throws InterruptedException {
        Thread.sleep(1000);
        Month targetMonthEnum = parseMonthName(targetMonthStr);
        String displayedMonthXPath = "//div[button[contains(@aria-label,'calendar view')]]/div";
        String nextMonthButtonXPath = "//button[@title='Next month']//*[name()='svg']";
        String previousMonthButtonXPath = "//button[@title='Previous month']//*[name()='svg']";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (true) {
            WebElement element = driver.findElement(By.xpath(displayedMonthXPath));
            String displayedMonthText = (String) js.executeScript("return arguments[0].textContent;", element);
//			String displayedMonthText = driver.findElement(By.xpath(displayedMonthXPath)).getText();
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
        logger.info("Navigated to Month: " + targetMonthStr);
    }
    private void selectDay(int targetDay) throws InterruptedException {
        int i = 1;
        while (i <= 6) {
            String dayElementXPath = "//div[@role='row' and @aria-rowindex='" + i + "']//button[text()='" + targetDay
                    + "']";
            try {
                WebElement dayElement = driver.findElement(By.xpath(dayElementXPath));
                if (dayElement != null) {

                    dayElement.click();
                    logger.info("Day selected successfully: " + targetDay);
                    return;
                }
            } catch (NoSuchElementException ex) {
                logger.warning("Target day not found in row " + i + ", checking next row.");
                //Thread.sleep(100);
                i++;
            }
        }
        throw new RuntimeException("Target day not found in the calendar: " + targetDay);
    }
}

