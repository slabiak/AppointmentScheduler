package com.example.slabiak.appointmentscheduler.ui;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.Testcontainers;
import org.testcontainers.containers.BrowserWebDriverContainer;

import java.io.File;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = LoginPageIT.Initializer.class)
@ActiveProfiles("integration-test")
public class LoginPageIT {

    @LocalServerPort
    private int port;

    @Rule
    public BrowserWebDriverContainer chrome = new BrowserWebDriverContainer()
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, new File("./target/"))
            .withCapabilities(new ChromeOptions());

    @Test
    public void shouldShowLoginPageAndSuccessfullyLoginToAdminAccountUsingAdminCredentials() {
        RemoteWebDriver driver = chrome.getWebDriver();
        String url = "http://host.testcontainers.internal:" + port + "/";
        driver.get(url);

        WebElement elementById = driver.findElementById("login-form");
        driver.findElementById("username").sendKeys("admin");
        driver.findElementById("password").sendKeys("qwerty123");
        driver.findElementByTagName("button").click();

        WebElement appointments = driver.findElementByLinkText("Appointments");

        assertNotNull(elementById);
        assertNotNull(appointments);
    }

    @Test
    public void shouldLoginAsRetailCustomerAndSuccessfullyBookNewAppointment() {
        RemoteWebDriver driver = chrome.getWebDriver();
        String url = "http://host.testcontainers.internal:" + port + "/";
        driver.get(url);
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);

        loginAsRetailCustomer(driver);
        navigateToNewAppointmentPage(driver);
        selectAppointmentSlot(driver);
        confirmAppointmentBooking(driver);

        // Validate the result
        validateAppointmentResult(driver);
    }

    private void loginAsRetailCustomer(RemoteWebDriver driver) {
        driver.findElementById("username").sendKeys("customer_r");
        driver.findElementById("password").sendKeys("qwerty123");
        driver.findElementByTagName("button").click();
    }

    private void navigateToNewAppointmentPage(RemoteWebDriver driver) {
        driver.findElementByLinkText("Appointments").click();
        driver.findElementByLinkText("New appointment").click();
    }

    private void selectAppointmentSlot(RemoteWebDriver driver) {
        driver.findElementByLinkText("Select").click();
        driver.findElementByLinkText("Select").click();

        // Handling dynamic XPath, retrying in case of StaleElementReferenceException
        boolean result = retryingFindClick(driver, "//*[@id=\"calendar\"]/div[1]/div[2]/div/button[2]/span");
        assertTrue(result);
    }

    private void confirmAppointmentBooking(RemoteWebDriver driver) {
        // Handling dynamic XPath, retrying in case of StaleElementReferenceException
        retryingFindClick(driver, "//*[@id=\"calendar\"]/div[2]/div/div/table/tbody/tr[2]/td[1]");
        driver.findElementByXPath("/html/body/div[2]/div/div/table/tbody/tr[8]/td/form/button").click();
    }

    private void validateAppointmentResult(RemoteWebDriver driver) {
        WebElement table = driver.findElement(By.id("appointments"));
        WebElement tableBody = table.findElement(By.tagName("tbody"));
        int rowCount = tableBody.findElements(By.tagName("tr")).size();
        assertEquals(1, rowCount);
    }

    private boolean retryingFindClick(RemoteWebDriver driver, String xpath) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.findElementByXPath(xpath).click();
                return true;
            } catch (StaleElementReferenceException e) {
                attempts++;
            }
        }
        return false;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            applicationContext.addApplicationListener((ApplicationListener<WebServerInitializedEvent>) event -> {
                Testcontainers.exposeHostPorts(event.getWebServer().getPort());
            });
        }
    }
}
