package com.example.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;

public class GoogleSearchTest {
    private WebDriver driver;
    private WebDriverWait wait;

    // 🔑 Hardcoded LambdaTest credentials
    private String username = "harshc";
    private String accessKey = "LT_88k1bI6lm83G4JpruXAGqLXZze0X4jprV7hYS68o1UDweyA";

    @BeforeClass
    public void setUp() throws MalformedURLException {
        // ✅ Define browser + OS
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("browserName", "Chrome");
        caps.setCapability("browserVersion", "latest");
        caps.setCapability("platformName", "Windows 11");

        // ✅ Optional: Meta info for LT Dashboard
        caps.setCapability("project", "Harness-LT-Demo");
        caps.setCapability("build", "Build_01");
        caps.setCapability("name", "Google Search Test");

        // ✅ LambdaTest Grid URL with hardcoded creds
        String gridURL = "https://" + username + ":" + accessKey + "@hub.lambdatest.com/wd/hub";

        driver = new RemoteWebDriver(new URL(gridURL), caps);
        driver.manage().window().maximize();

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

        // ✅ Wait for results
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3")));

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
