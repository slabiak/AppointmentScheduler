package com.example.slabiak.appointmentscheduler.ui;

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

        driver.findElementById("username").sendKeys("customer_r");
        driver.findElementById("password").sendKeys("qwerty123");
        driver.findElementByTagName("button").click();

        driver.findElementByLinkText("Appointments").click();
        driver.findElementByLinkText("New appointment").click();
        driver.findElementByLinkText("Select").click();
        driver.findElementByLinkText("Select").click();
        driver.findElementByXPath("//*[@id=\"calendar\"]/div[1]/div[2]/div/button[2]/span\n").click();

        boolean result = false;
        int attempts = 0;
        while (attempts < 3) {
            try {
                driver.findElementByXPath("//*[@id=\"calendar\"]/div[2]/div/div/table/tbody/tr[2]/td[1]").click();
                result = true;
                break;
            } catch (StaleElementReferenceException e) {
            }
            attempts++;
        }

        driver.findElementByXPath("/html/body/div[2]/div/div/table/tbody/tr[8]/td/form/button").click();

        WebElement table = driver.findElement(By.id("appointments"));
        WebElement tableBody = table.findElement(By.tagName("tbody"));
        int rowCount = tableBody.findElements(By.tagName("tr")).size();
        assertTrue(result);
        assertEquals(1, rowCount);
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
