package com.back.app.selenium;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Tag("selenium")
public class LogoutWithoutLoginTest {

    @Test
    public void testLogoutWithoutLoginDoesNotCrashApplication() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/opt/firefox/firefox-bin");
        // options.addArguments("-headless");

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

            // 1) Otvori početnu stranicu (korisnik NIJE prijavljen)
            driver.get("http://localhost:5173");

            // 2) Direktan pokušaj odjave bez prijave
            driver.get("http://localhost:5173/logout");

            // 3) Provjera da sustav ispravno reagira
            boolean redirectedCorrectly = wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/login"),
                    ExpectedConditions.urlToBe("http://localhost:5173/"),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(text(),'Login') or contains(text(),'Sign in')]")
                    )
            )) != null;

            assertTrue(redirectedCorrectly);

        } finally {
            driver.quit();
        }
    }
}
