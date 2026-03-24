package com.scoreboard.util;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.scoreboard.constants.PathConstants;

public class BaseWaiters {
    private static Logger logger = Logger.getLogger(BaseWaiters.class);

    private WebDriver webDriver;
    private WebDriverWait webDriverWait;

    private long timeout = PathConstants.defaultTimeout;
    private long pollingFrequency = PathConstants.defaultPollingFrequency;

    public BaseWaiters(WebDriver driver, WebDriverWait driverWait) {
        this.webDriver = driver;
        this.webDriverWait = driverWait;
    }

    public WebDriver getWebDriver() {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public void setWebDriverWait(WebDriverWait webDriverWait) {
        this.webDriverWait = webDriverWait;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getPollingFrequency() {
        return pollingFrequency;
    }

    public void setPollingFrequency(long pollingFrequency) {
        this.pollingFrequency = pollingFrequency;
    }

    public static Logger getLogger() {
        return logger;
    }
}
