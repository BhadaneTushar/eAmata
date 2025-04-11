package utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

public class WindowUtils {
    private static final Logger logger = LoggerFactory.getLogger(WindowUtils.class);
    private final WebDriver driver;
    private final String mainWindowHandle;

    public WindowUtils(WebDriver driver) {
        this.driver = driver;
        this.mainWindowHandle = driver.getWindowHandle();
    }


    // Switch to the first new window
    public void switchToNewWindow() {
        switchToNewWindow(0);
    }

    // Switch to nth new window (0-based index)
    public void switchToNewWindow(int windowIndex) {
        waitForAtLeastWindows(2, Duration.ofSeconds(10));
        List<String> newHandles = driver.getWindowHandles().stream()
                .filter(h -> !h.equals(mainWindowHandle))
                .collect(Collectors.toList());

        if (windowIndex < newHandles.size()) {
            driver.switchTo().window(newHandles.get(windowIndex));
        } else {
            throw new NoSuchWindowException("Window index " + windowIndex + " not found");
        }
    }

    public void switchToWindowByTitle(String partialTitle, Duration timeout) {
        new WebDriverWait(driver, timeout).until(d -> {
            for (String handle : d.getWindowHandles()) {
                d.switchTo().window(handle);
                if (d.getTitle().contains(partialTitle)) return true;
            }
            return false;
        });
    }

    public void switchToMainWindow() {
        if (driver.getWindowHandles().contains(mainWindowHandle)) {
            driver.switchTo().window(mainWindowHandle);
        } else {
            throw new NoSuchWindowException("Main window unavailable");
        }
    }

    public void closeAllExtraWindows() {
        Set<String> handles = driver.getWindowHandles();
        if (handles.size() == 1) {
            logger.info("No extra windows to close");
            return;
        }
        handles.stream()
                .filter(h -> !h.equals(mainWindowHandle))
                .forEach(h -> {
                    try {
                        driver.switchTo().window(h);
                        driver.close();
                    } catch (NoSuchWindowException e) {
                        logger.warn("Window already closed: {}", h);
                    }
                });
        switchToMainWindow();
    }

    private void waitForAtLeastWindows(int minCount, Duration timeout) {
        new WebDriverWait(driver, timeout)
                .until(d -> d.getWindowHandles().size() >= minCount);
    }

    public void printAllWindowInfo() {
        driver.getWindowHandles().forEach(h -> {
            driver.switchTo().window(h);
            logger.debug("Window [{}]: Title='{}', URL='{}'", h, driver.getTitle(), driver.getCurrentUrl());
        });
    }
}