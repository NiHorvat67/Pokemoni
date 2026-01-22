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
public class SellerProfileNonExistingTest {

    @Test
    public void testOpeningNonExistingProfileShows404Page() {
        System.setProperty("webdriver.gecko.driver", "/usr/local/bin/geckodriver");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("/opt/firefox/firefox-bin");
        // options.addArguments("-headless");

        WebDriver driver = new FirefoxDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(20));

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(12));

            // 1) Otvori nepostojeći profil trgovca
            driver.get("http://localhost:5173/profile/999999");

            // 2) Provjera da se prikazuje 404 stranica ili poruka o grešci
            boolean has404Text = wait.until(ExpectedConditions.or(
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(.,'Sorry we are unable to find the page')]")
                    ),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[normalize-space(.)='404' or contains(.,'404')]")
                    ),
                    ExpectedConditions.presenceOfElementLocated(
                            By.xpath("//*[contains(.,'not found')]")
                    )
            )) != null;

            assertTrue(has404Text);

        } finally {
            driver.quit();
        }
    }
}
