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
public class ReservationNonExistingAdvertisementTest {

    @Test
    public void testReservationForNonExistingAdvertisementShows404Page() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/opt/firefox/firefox-bin");
        // options.addArguments("-headless");

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

            // 1) Otvori nepostojeću rutu
            driver.get("http://localhost:5173/advertisements/999999/reserve");

            // 2) Provjera da se prikazuje 404 stranica
            boolean has404Text = wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(.,'Sorry we are unable to find the page')]")
                    ),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[normalize-space(.)='404' or contains(.,'404')]")
                    )
            )) != null;

            assertTrue(has404Text);

        } finally {
            driver.quit();
        }
    }
}
