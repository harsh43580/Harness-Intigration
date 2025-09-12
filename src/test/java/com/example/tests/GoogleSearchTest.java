package com.example.tests;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

public class GoogleSearchTest {
    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeClass
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();

        // ✅ Explicit wait (10 seconds)
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    public void testDuckDuckGoSearch() {
        driver.get("https://duckduckgo.com/");

        WebElement searchBox = wait.until(
                ExpectedConditions.visibilityOfElementLocated(By.name("q"))
        );
        searchBox.sendKeys("Harness CI");
        searchBox.submit();

        // ✅ Wait for results to load
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));

        // ✅ Collect all results
        List<WebElement> results = driver.findElements(By.cssSelector("h3"));

        boolean found = results.stream()
                .anyMatch(e -> e.getText().toLowerCase().contains("harness"));

        Assert.assertTrue(found,
                "Expected at least one result to contain 'Harness' but got: " +
                        results.stream().map(WebElement::getText).toList());
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
