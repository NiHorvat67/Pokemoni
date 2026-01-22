package com.back.app.selenium;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SearchNonExistingItemTest {

    @Test
    public void testNonExistingSportShowsNoResults() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/opt/firefox/firefox-bin");
        //options.addArguments("-headless");

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));

        try {
            driver.get("http://localhost:5173");

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            WebElement searchInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.name("query"))
            );
            searchInput.clear();
            searchInput.sendKeys("Hoverboard");

            WebElement searchButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.cssSelector(".w-6"))
            );
            searchButton.click();
            
            WebElement msg = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(text(),'No advertisements found')]")
                    )
            );
            assertTrue(msg.isDisplayed());

            List<WebElement> products = driver.findElements(By.cssSelector(".product-card"));
            assertEquals(0, products.size());

        } finally {
            driver.quit();
        }
    }
}
