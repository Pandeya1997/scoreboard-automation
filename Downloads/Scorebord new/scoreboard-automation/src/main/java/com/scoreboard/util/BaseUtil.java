package com.scoreboard.util;


import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.scoreboard.constants.DBQueries;
import com.scoreboard.constants.PathConstants;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class BaseUtil extends BaseWaiters{
    protected static Logger logger = getLogger();
    private WebDriver webDriver = getWebDriver();
    DBUtils dbUtils = new DBUtils();
    private static Connection conn;
    private final DBConnection dbcon = new DBConnection();

    public BaseUtil(WebDriver webDriver, WebDriverWait webDriverWait) {
        super(webDriver, webDriverWait);
    }

    public boolean pollAndWaitForElement(String locator){
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        boolean isPresent = false;

        long iCount = 0;
        logger.debug("Waiting for page locator..."+locator);
        while (iCount < 20) {
            if (!instantElementCheck(PathConstants.TYPE_DISPLAY, locator)) {
                try {
                    Thread.sleep(getPollingFrequency());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                isPresent = true;
                break;
            }
            iCount = iCount +1;
        }
        return isPresent;
    }

    public boolean pollAndWaitForElement(WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        long attempts = 0;
        logger.debug("Waiting for page element… {}"+ element);

        while (attempts < 20) {
            try {
                // Equivalent of your instantElementCheck()
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                // eat & retry
            }

            try {
                Thread.sleep(getPollingFrequency());   // keep your existing frequency helper
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();    // good practice: restore the interrupt flag
                logger.warn("Thread interrupted while polling for element", e);
            }
            attempts++;
        }
        return false;
    }


    public boolean pollAndWaitForElement(String locator, int retries){
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        boolean isPresent = false;

        long iCount = 0;
        logger.info("Waiting for page locator..."+locator);
        while (iCount < retries) {
            if (!instantElementCheck(PathConstants.TYPE_DISPLAY, locator)) {
                try {
                    Thread.sleep(getPollingFrequency());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                isPresent = true;
                break;
            }
            iCount = iCount +1 ;
        }
        return isPresent;
    }

    public void waitForSpinner(int milliSecond) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        explicitWait(milliSecond);
        long iCount = 0;
        logger.debug("Waiting for page spinner...");
        while (iCount < getTimeout()) {
            if (instantElementCheck("Display", String.valueOf(By.xpath("//div[@class='spinner']/child::img")))) {
                try {
                    Thread.sleep(getPollingFrequency());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
            iCount += getPollingFrequency();
        }
        explicitWait(milliSecond);
    }

    public void waitForSpinner() {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        explicitWait(2000);
        long iCount = 0;
        logger.debug("Waiting for page spinner...");
        while (iCount < getTimeout()) {
            if (instantElementCheck("Display", String.valueOf(By.xpath("xpath===//div[@class='cssload-container']")))) {
                try {
                    Thread.sleep(getPollingFrequency());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
            iCount += getPollingFrequency();
        }
        explicitWait(2000);
    }

    public boolean instantElementCheck(String type, String locator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        By byValue = getLocator(locator);
        return instantElementCheck(type, byValue);
    }

    public boolean instantElementCheck(String type, By byValue) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        try {
            WebElement element = getWebDriver().findElement(byValue);
            if (type.equalsIgnoreCase("Display")) {
                return element.isDisplayed();
            } else if (type.equalsIgnoreCase("Enable")) {
                return element.isEnabled();
            } else if (type.equalsIgnoreCase("Select")) {
                return element.isSelected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean instantElementCheck(String type, WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        try {
            if (type.equalsIgnoreCase("Display")) {
                return element.isDisplayed();
            } else if (type.equalsIgnoreCase("Enable")) {
                return element.isEnabled();
            } else if (type.equalsIgnoreCase("Select")) {
                return element.isSelected();
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public void checkCheckBox(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        if (!element.isSelected()) {
            waitForClickabilityOfElement(element);
            element.click();
            logger.info("The Given Element is clicked");
        }
    }

    public void waitForClickabilityOfElement(String locator) {
        logger.info("Waiting for element to be visible " + locator);

        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(getLocator(locator)));
    }

    public void waitForClickabilityOfElement(WebElement element) {
        logger.info("Waiting for element to be visible " + element);

        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForClickabilityOfElement(By locator) {
        logger.info("Waiting for element to be visible " + locator);

        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForVisibilityOfElement(objLocator);
        waitForClickabilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        element.click();
        logger.info("The Given Element is clicked");
    }

    public void click(WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        try {
            waitForElementToBePresent(element);
            waitForElementToBeVisible(element);
            waitForElementToBeClickable(element);

            element.click();
            logger.info("The given element is clicked: " + element);
        } catch (Exception e) {
            logger.error("Failed to click on element: " + element, e);
            throw e;
        }
    }

    public void waitForElementToBePresent(WebElement element) {
        getWebDriverWait().until(driver -> element != null);
    }

    public void waitForElementToBeVisible(WebElement element) {
        getWebDriverWait().until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForElementToBeClickable(WebElement element) {
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickAndWait(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForClickabilityOfElement(objLocator);
        WebElement element = waitAndgetElement(objLocator);
        explicitWait(PathConstants.WAIT_MEDIUM);
        element.click();
        logger.info("After waiting, the Given Element is clicked");
    }

    public void click(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        waitForClickabilityOfElement(element);
        element.click();
        logger.info("The Given Element is clicked");
    }

    public void clickElement(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        waitForClickabilityOfElement(element);
        element.sendKeys(Keys.ENTER);
        logger.info("The Given Element is clicked");
    }

    public void enterText(String objLocator, String text) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        waitForVisibilityOfElement(objLocator);
        logger.info("The data to be filled in the Textbox is " + text);
        element.clear();
        element.sendKeys(text);
        logger.info("The data is entered to the Text Field");
    }

    public void enterText(WebElement element, String text) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("Executing Test Step :: " + methodName);

        try {
            // Validate input
            if (element == null) {
                logger.error("WebElement is null — cannot enter text.");
                throw new IllegalArgumentException("WebElement cannot be null");
            }

            if (text == null) {
                logger.warn("Text value is null. Nothing will be entered.");
                return;
            }

            String trimmedText = text.trim();
            logger.info("Preparing to enter text: '" + trimmedText + "' into element: " + element);

            // Ensure element is ready
            pollAndWaitForElement(element);
            waitForVisibilityOfElement(element);

            // Optional: Highlight element for debugging
            try {
                highlightElement(element);
            } catch (Exception e) {
                logger.debug("Skipping element highlight: " + e.getMessage());
            }

            // Enter text safely
            element.clear();
            logger.debug("Textbox cleared successfully.");

            element.sendKeys(trimmedText);
            logger.info("Successfully entered text: '" + trimmedText + "'");

        } catch (StaleElementReferenceException e) {
            logger.error("Element became stale while entering text: " + e.getMessage(), e);
            throw e;
        } catch (TimeoutException e) {
            logger.error("Element was not visible/clickable in time: " + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error while entering text: " + e.getMessage(), e);
            throw e;
        }
    }

    private void highlightElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) getWebDriver();
        js.executeScript("arguments[0].style.border='2px solid orange'", element);
    }



    public void enterText(String objLocator, int index, String text) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        waitForVisibilityOfElement(objLocator);
        logger.info("The data to be filled in the Textbox is " + text);
        element.clear();
        element.sendKeys(text);
        logger.info("The data is entered to the Text Field");
    }

    public String getElementText(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        waitForVisibilityOfElement(objLocator);
        logger.info("Element is having the text... " + element.getText());
        return element.getText();
    }

    public String getElementText(WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        pollAndWaitForElement(element);
        waitForVisibilityOfElement(element);

        logger.info("Element is having the text... " + element.getText());
        return element.getText();
    }

    public static boolean elementShouldNotBeVisible(WebElement element) {
        try {
            return !element.isDisplayed();
        } catch (Exception e) {
            return true;
        }
    }
    public boolean noOptionsAvailable(WebDriver driver, String objLocator) {
        By locator = getLocator(objLocator);
        Duration defaultWait = driver.manage().timeouts().getImplicitWaitTimeout();
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));
            List<WebElement> elements = driver.findElements(locator);
            return !elements.isEmpty() && elements.get(0).isDisplayed();
        } finally {
            driver.manage().timeouts().implicitlyWait(defaultWait);
        }
    }



    public boolean elementShouldNotBeVisible(WebDriver driver, String objLocator) {
        By locator = getLocator(objLocator);

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(0));

        try {
            List<WebElement> elements = driver.findElements(locator);

            if (elements.isEmpty()) {
                return true;
            }

            return !elements.get(0).isDisplayed();

        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        }
    }




    public String getElementText(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        waitForVisibilityOfElement(objLocator);
        logger.info("Element is having the text... " + element.getText());
        return element.getText();
    }

    public  void selectDropDownByValue(String objLocator, String value) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForVisibilityOfElement(objLocator);

        Select select = new Select(getElementAfterLoaded(objLocator));
        List<WebElement> list = select.getOptions();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getText().equalsIgnoreCase(value)) {
                logger.info(list.get(i).getText() + "=" + value);
                select.selectByIndex(i);
                break;
            }
        }
    }

    public void selectDropDownByIndex(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        waitForVisibilityOfElement(objLocator);

        Select select = new Select(getElementAfterLoaded(objLocator));
        select.selectByIndex(index);
    }

    public void selectDropDownByValueAttribute(String objLocator, String value) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForVisibilityOfElement(objLocator);

        Select select = new Select(getElementAfterLoaded(objLocator));
        select.selectByValue(value);
    }

    public List<WebElement> getDropDownValues(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForVisibilityOfElement(objLocator);

        click(objLocator);
        Select select = new Select(getElementAfterLoaded(objLocator));
        return select.getOptions();
    }

    public String getSelectedDropDownValues(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        pollAndWaitForElement(objLocator);
        waitForVisibilityOfElement(objLocator);

        Select select = new Select(getElementAfterLoaded(objLocator));
        logger.info("Selected option is the drop down is : " + select.getFirstSelectedOption().getText());
        return select.getFirstSelectedOption().getText();
    }

    public String getDownloadsPath() {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        String userDir = System.getProperty("user.home");
        char cforwardslash = (char) 47;
        char cbackslash = (char) 92;
        String path = userDir.replace(cbackslash, cforwardslash);
        logger.info("The download path is : " + path + "/Downloads/");
        return path + "/Downloads/";
    }

    public void uploadFile(String objLocator, String filePath) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        waitForVisibilityOfElement(objLocator);

        WebElement element = getElementAfterLoaded(objLocator);
        element.sendKeys(filePath);
    }

    public boolean verifyElementDisplayed(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        WebElement element = getElementAfterLoaded(objLocator);
        return element.isDisplayed();
    }

    public boolean verifyElementDisplayed(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        return element.isDisplayed();
    }
    public void waitForVisibilityOfElement(String locator) {
        logger.info("Waiting for element to be visible " + locator);

        getWebDriverWait().until(ExpectedConditions.visibilityOfElementLocated(getLocator(locator)));
    }

    public void waitForVisibilityOfElement(WebElement element) {
        logger.info("Waiting for element to be visible: " + element);

        getWebDriverWait().until(ExpectedConditions.visibilityOf(element));
    }


    public boolean verifyElementEnabled(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        return element.isEnabled() ? true : false;
    }

    public boolean verifyElementEnabled(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        return element.isEnabled() ? true : false;
    }

    public boolean verifyElementSelected(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        return element.isSelected() ? true : false;
    }

    public boolean verifyElementSelected(String objLocator, int index) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getMultipleElementsAfterLoaded(objLocator).get(index);
        return element.isSelected() ? true : false;
    }

    public String getElementAttribute(String objLocator, String attribute) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        logger.info("The value of attribute [" + attribute + "] is : " + element.getAttribute(attribute));
        return element.getAttribute(attribute);
    }


    public void mouseOver(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        WebElement element = getElementAfterLoaded(objLocator);
        element.click();

        Actions action = new Actions(webDriver);
        action.moveToElement(element).build().perform();
    }

    public boolean verifyPresenceOfText(String objLocator, String text) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        WebElement elementLocator = getElementAfterLoaded(objLocator);
        logger.info("The text present in the element is : " + elementLocator.getText());
        if (elementLocator.getText().contains(getElementText(objLocator))) {
            return true;
        }
        return false;
    }

    public int elementsSize(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        List<WebElement> elements = getMultipleElementsAfterLoaded(objLocator);
        logger.info("Size of elements" + elements.size());
        return elements.size();
    }

    public boolean verifyCheckBoxIsSelected(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        if (element.isSelected()) {
            logger.info("Given checkbox is selected");
            return true;
        }
        return false;
    }

    public String getMouseOverText(String objLocator, String textObjectLocator) throws InterruptedException {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        waitForVisibilityOfElement(objLocator);
        WebElement element = getElementAfterLoaded(objLocator);
        Actions actions = new Actions(webDriver);
        actions.clickAndHold(element).build().perform();
        return getElementText(textObjectLocator);
    }

    public String getBasePath() {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        String filePath = new File("").getAbsolutePath();
        char cforwardslash = (char) 47;
        char cbackslash = (char) 92;
        String basePath = filePath.replace(cbackslash, cforwardslash);
        logger.info("the base path is : " + basePath);
        return basePath;
    }

    public WebElement getElementAfterLoaded(final String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        By byValue = getLocator(objLocator);
        isElementLoaded(byValue);
        return webDriver.findElement(byValue);
    }

    public WebElement getElementAfterLoaded(final WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        isElementLoaded(element); // You’ll also need to overload this method to accept WebElement
        return element;
    }

    public void assertElementDisplayed(String objLocator, String message) throws AssertionError {
        try {
            Assert.assertTrue(message, verifyElementDisplayed(objLocator));
        } catch (NoSuchElementException ex) {
            Assert.fail(message + " as element not available : " + ex.getMessage());
        } catch (ElementNotFoundException ex) {
            Assert.fail(message + " as element not available : " + ex.getMessage());
        } catch (NullPointerException ex) {
            Assert.fail(message + " due to some error : " + ex.getMessage());
        }
    }


    public void assertElementDisplayed(WebElement element, String message) throws AssertionError {
        try {
            Assert.assertTrue(message, element.isDisplayed());
        } catch (NoSuchElementException ex) {
            Assert.fail(message + " — element not found: " + ex.getMessage());
        } catch (ElementNotFoundException ex) {
            Assert.fail(message + " — element not available: " + ex.getMessage());
        } catch (NullPointerException ex) {
            Assert.fail(message + " — null reference error: " + ex.getMessage());
        }
    }

    public void safeClick(String locatorKey) {
        try {
            String xpath = locatorKey.split("===")[1];
            By by = By.xpath(xpath);
            WebElement element = getWebDriverWait().until(ExpectedConditions.elementToBeClickable(by));

            if (element.isEnabled()) {
                element.click();
            } else {
                logger.warn("Element not enabled, trying JavaScript click as fallback: {} "+ locatorKey);
                ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
            }
        } catch (Exception e) {
            logger.error("Error during safeClick for locator: {} "+ locatorKey, e);
            throw e;
        }
    }


    public boolean isElementDisplayed(WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());
        try {
            return element != null && element.isDisplayed();
        } catch (TimeoutException | NoSuchElementException | NullPointerException e) {
            return false;
        }
    }

    public boolean isElementDisplayed(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        int i=0;
        try{
            By byValue = getLocator(objLocator);
            WebElement element = webDriver.findElement(byValue);

            if (element.isDisplayed()) {
                return true;
            }
        }catch (TimeoutException | NoSuchElementException e){
            return false;
        }
        return false;
    }

    public WebElement waitAndgetElement(final String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        By byValue = getLocator(objLocator);
        isElementClickable(byValue);
        return webDriver.findElement(byValue);
    }

    public List<WebElement> getMultipleElementsAfterLoaded(final String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        By byValue = getLocator(objLocator);
        isElementLoaded(byValue);
        return webDriver.findElements(byValue);
    }

    public List<WebElement> getMultipleElementsAfterLoaded(List<WebElement> elements) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        // Wait until the first element is visible to ensure elements are loaded
        if (elements != null && !elements.isEmpty()) {
            WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOfAllElements(elements));
        } else {
            logger.warn("Element list is null or empty.");
        }

        return elements;
    }

    public void selectDropdown(WebElement element, String value) {
        WebDriver driver =TestBase.getInstance().getWebDriver();

        try {
            String tagName = element.getTagName().toLowerCase();

            if ("select".equals(tagName)) {
                Select select = new Select(element);
                select.selectByVisibleText(value);
                explicitWait(PathConstants.WAIT_VERY_LOW);
                return;
            }

            if ("input".equals(tagName)) {
                element.click();
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                element.sendKeys(value);
                explicitWait(PathConstants.WAIT_VERY_LOW);
                WebElement element1=fluentWaitForElement(element);
                element1.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                explicitWait(PathConstants.WAIT_VERY_LOW);
                return;
            }

            element.click();
            WebElement option = driver.findElement(
                    By.xpath("//div[contains(@id,'option') and normalize-space(text())='" + value + "']")
            );
            option.click();
            explicitWait(PathConstants.WAIT_VERY_LOW);
        } catch (NullPointerException e){
            logger.error("Getting error "+e.getMessage());
        }
        catch (Exception e) {
            throw new RuntimeException(
                    "Failed to select value '" + value +
                            "' from element with tag '" + element.getTagName() +
                            "'. Root cause: " + e.getMessage(), e
            );
        }
        explicitWait(PathConstants.WAIT_VERY_LOW);
    }

    public WebElement fluentWaitForElement(WebElement element) {

        WebDriver driver = TestBase.getInstance().getWebDriver();

        FluentWait<WebDriver> wait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(5))
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(driver1 -> {
            try {
                return element.isDisplayed() ? element : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    public void selectDropdownValues(WebElement element, String value) {
        WebDriver driver = TestBase.getInstance().getWebDriver();
        String expectedValue = value.trim();

        try {
            String tagName = element.getTagName().toLowerCase();
            if ("select".equals(tagName)) {
                Select select = new Select(element);

                boolean found = false;
                for (WebElement option : select.getOptions()) {
                    if (option.getText().trim().equalsIgnoreCase(expectedValue)) {
                        select.selectByVisibleText(option.getText().trim());
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    throw new RuntimeException("Exact option not found: " + expectedValue);
                }
                return;
            }

            if ("input".equals(tagName)) {
                element.click();
                element.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                element.sendKeys(expectedValue);
                explicitWait(PathConstants.WAIT_LOW);
                WebElement exactOption = driver.findElement(
                        By.xpath(
                                "//li[normalize-space(.)='" + expectedValue + "'] | " +
                                        "//div[normalize-space(.)='" + expectedValue + "']"
                        )
                );
                exactOption.click();
                return;
            }
            element.click();

            WebElement exactOption = driver.findElement(
                    By.xpath(
                            "//*[self::div or self::li or self::span]" +
                                    "[normalize-space(.)='" + expectedValue + "']"
                    )
            );

            exactOption.click();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to select EXACT value '" + expectedValue + "'. Reason: " + e.getMessage(), e
            );
        }
    }





    public boolean isElementLoaded(final By byValue) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        logger.debug("Waiting for element to be loaded. Timeout:" + getTimeout());
        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofMillis(getTimeout()))
                .pollingEvery(Duration.ofMillis(getPollingFrequency())).ignoring(NoSuchElementException.class);
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                WebElement element = d.findElement(byValue);
                return element.isDisplayed();
            }

            @Override
            public <V> Function<WebDriver, V> andThen(Function<? super Boolean, ? extends V> arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <V> Function<V, Boolean> compose(Function<? super V, ? extends WebDriver> arg0) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return wait.until(condition);
    }

    public boolean isElementLoaded(final WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        logger.debug("Waiting for element to be loaded. Timeout:" + getTimeout());
        Wait<WebDriver> wait = new FluentWait<>(webDriver)
                .withTimeout(Duration.ofMillis(getTimeout()))
                .pollingEvery(Duration.ofMillis(getPollingFrequency()))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        return wait.until(driver -> {
            try {
                return element.isDisplayed();
            } catch (Exception e) {
                return false;
            }
        });
    }

    public boolean isElementLoaded(String locator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        By byValue = getLocator(locator);
        return isElementLoaded(byValue);
    }

    public boolean isElementClickable(By byValue) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        try {
            explicitWait(1000);
            logger.debug("Waiting for element to be clickable. Timeout:" + getTimeout());
            return getWebDriverWait().until(ExpectedConditions.elementToBeClickable(byValue)).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    public boolean isElementClickable(final WebElement element) {
        logger.debug("Checking if element is really clickable...");

        try {
            getWebDriverWait().until(ExpectedConditions.visibilityOf(element));
            getWebDriverWait().until(ExpectedConditions.elementToBeClickable(element));

            // Extra check - move to element
            new Actions(webDriver).moveToElement(element).perform();

            return element.isDisplayed() && element.isEnabled();
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
            return false;
        }
    }

    public boolean checkElementClickable(final WebElement element) {
        logger.debug("Checking if element is really clickable...");

        try {
            getWebDriverWait().until(ExpectedConditions.visibilityOf(element));

            // First check native Selenium definition
            if (!element.isEnabled() || !element.isDisplayed()) {
                return false;
            }

            // Custom disabled markers
            String classAttr = element.getAttribute("class");
            if (classAttr != null && classAttr.contains("disabled")) {
                return false;
            }

            String ariaDisabled = element.getAttribute("aria-disabled");
            if ("true".equalsIgnoreCase(ariaDisabled)) {
                return false;
            }

            // Extra: check if something is covering the element
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            Boolean isCovered = (Boolean) js.executeScript(
                    "var elem = arguments[0];" +
                            "var rect = elem.getBoundingClientRect();" +
                            "var x = rect.left + rect.width/2;" +
                            "var y = rect.top + rect.height/2;" +
                            "var elAtPoint = document.elementFromPoint(x, y);" +
                            "return !(elem === elAtPoint || elem.contains(elAtPoint));",
                    element
            );

            if (isCovered != null && isCovered) {
                return false; // another element is covering it
            }

            return true;
        } catch (Exception e) {
            logger.error("Element not clickable: " + e.getMessage());
            return false;
        }
    }



    public boolean isElementClickable(String locator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        By byValue = getLocator(locator);
        return isElementClickable(byValue);
    }

    public boolean isElementEnabled(final By byValue) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver).withTimeout(Duration.ofMillis(getTimeout()))
                .pollingEvery(Duration.ofMillis(getPollingFrequency())).ignoring(NoSuchElementException.class);
        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                WebElement element = d.findElement(byValue);
                return element.isEnabled();
            }

            @Override
            public <V> Function<WebDriver, V> andThen(Function<? super Boolean, ? extends V> arg0) {
                // TODO Auto-generated method stub
                return null;
            }

            @Override
            public <V> Function<V, Boolean> compose(Function<? super V, ? extends WebDriver> arg0) {
                // TODO Auto-generated method stub
                return null;
            }
        };
        return wait.until(condition);
    }

    public boolean isElementEnabled(final WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        Wait<WebDriver> wait = new FluentWait<WebDriver>(webDriver)
                .withTimeout(Duration.ofMillis(getTimeout()))
                .pollingEvery(Duration.ofMillis(getPollingFrequency()))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class);

        ExpectedCondition<Boolean> condition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver d) {
                return element.isEnabled();
            }
        };

        return wait.until(condition);
    }

    public boolean isElementEnable(final WebElement element) {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());

        try {
            getWebDriverWait().until(ExpectedConditions.visibilityOf(element));

            // First try Selenium's native check
            if (!element.isEnabled()) {
                return false;
            }

            // Custom check: look for "disabled" keyword in class or attribute
            String classAttr = element.getAttribute("class");
            if (classAttr != null && classAttr.contains("disabled")) {
                return false;
            }

            String ariaDisabled = element.getAttribute("aria-disabled");
            if ("true".equalsIgnoreCase(ariaDisabled)) {
                return false;
            }

            return true;
        } catch (Exception e) {
            logger.error("Error checking element enabled state: " + e.getMessage());
            return false;
        }
    }



    public boolean isElementEnabled(String locator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        waitForVisibilityOfElement(locator);
        By byValue = getLocator(locator);
        return isElementEnabled(byValue);
    }

    public void waitForPageLoading(String element) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        String[] str = element.split("===");
        getWebDriverWait().until(ExpectedConditions.elementToBeClickable(By.xpath(str[1])));
    }

    public void selectDropdownByPartialText(WebElement element, String partialText) {
        Select select = new Select(element);

        for (WebElement option : select.getOptions()) {
            if (option.getText().contains(partialText)) {
                option.click();
                return;
            }
        }
        throw new RuntimeException("Option not found: " + partialText);
    }

    public void overrideTimeout(String timeout) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        if (!(null == timeout || timeout.equals(""))) {
            setTimeout(Long.parseLong(timeout));
        }
    }

    public void overridePollingFrequency(String pollingFrequency) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        if (!(null == pollingFrequency || pollingFrequency.isEmpty())) {
            setPollingFrequency(Long.parseLong(pollingFrequency));
        }
    }

    public void explicitWait(long milliSec) {
        String methodName = new Object() {}.getClass().getEnclosingMethod().getName();
        logger.debug("Executing Test Step :: " + methodName);

        if (milliSec <= 0) {
            logger.warn("Invalid wait time provided: " + milliSec + " ms. Skipping wait.");
            return;
        }

        try {
            long seconds = milliSec / 1000;
            logger.info("Waiting for [" + milliSec + " ms / " + seconds + " sec] before the next step...");
            Thread.sleep(milliSec);
            logger.debug("Wait of " + milliSec + " ms completed successfully.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            logger.error("Wait interrupted: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Error while executing explicit wait: " + e.getMessage(), e);
        }
    }


    public By getLocator(String objLocator) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());

        String[] str = objLocator.split("===");
        if (str.length < 2) {
            logger.fatal("Object Identifier is not defined for the element");
        }

        logger.info("Object Identifier " + str[0] + "\t Object Identifier Value: " + str[1]);
        By byValue = null;
        switch (str[0].toUpperCase()) {
            case "ID":
                byValue = By.id(str[1]);
                break;
            case "XPATH":
                byValue = By.xpath(str[1]);
                break;
            case "NAME":
                byValue = By.name(str[1]);
                break;
            case "LINKTEXT":
                byValue = By.linkText(str[1]);
                break;
            case "CSS":
                byValue = By.cssSelector(str[1]);
                break;
            case "CLASSNAME":
                byValue = By.className(str[1]);
                break;
            case "TAGNAME":
                byValue = By.tagName(str[1]);
                break;
        }
        return byValue;
    }

    public void searchData(WebElement searchBox,String data){
        try{
            if(isElementDisplayed(searchBox)) {
                click(searchBox);
                enterText(searchBox,data);
                explicitWait(PathConstants.WAIT_VERY_LOW);
            }
        }catch (Exception e){
            logger.error("not able to search the element :: "+data);
        }
    }

    public Connection getConn(String dbUrl, String userName, String password) throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = dbcon.getConnection(dbUrl, userName, password);
        }
        return conn;
    }

    public String getTournamentFromDB(){
        String tournament="";
        try{
            Connection connection =getConn(TestBase.getInstance().getReadDatabaseUrls()
                                    .get(TestBase.getEnv() + "_DBUrl"),
                            TestBase.getInstance().getReadDataBaseCredentials()
                                    .get(TestBase.getEnv() + "_DBUsername"),
                            TestBase.getInstance().getReadDataBaseCredentials()
                                    .get(TestBase.getEnv() + "_DBPassword"));
            tournament=dbUtils.getStringColumnValueFromDB(DBQueries.GET_TOURNAMENT,connection,"name");
        }catch (Exception e){
            logger.error("unable to get tournament name due to "+e.getMessage());
        }
        return tournament;
    }

    public String getEventIDFromDB(){
        String eventID="";
        try{
            Connection connection =getConn(TestBase.getInstance().getReadDatabaseUrls()
                            .get(TestBase.getEnv() + "_DBUrl"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBUsername"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBPassword"));
            eventID=dbUtils.getStringColumnValueFromDB(DBQueries.GET_MATCH,connection,"event_id");
        }catch (Exception e){
            logger.error("unable to get event id due to "+e.getMessage());
        }
        return eventID;
    }

    public List<Integer> getValidTeams(Connection conn, int requiredTeams, int playersPerTeam) {

        List<Integer> teamIds = new ArrayList<>();

        String query =
                "SELECT t.team_id " +
                        "FROM team t " +
                        "JOIN team_player tp ON t.team_id = tp.team_id " +
                        "JOIN player p ON tp.player_id = p.player_id " +
                        "WHERE t.is_active = 1 " +
                        "AND p.is_active = 1 " +
                        "AND t.gender = 'MALE' " +
                        "AND p.gender = 'MALE' " +
                        "AND t.name LIKE '%Automation%' " +
                        "AND p.name LIKE '%Automation%' " +
                        "GROUP BY t.team_id " +
                        "HAVING COUNT(p.player_id) >= ? " +
                        "LIMIT ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {     // gender again
            ps.setInt(1, playersPerTeam);
            ps.setInt(2, requiredTeams);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                teamIds.add(rs.getInt("team_id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamIds;
    }

    public List<String> getPlayersForTeam(Connection conn, int teamId, int playerLimit) {

        List<String> players = new ArrayList<>();

        String query =
                "SELECT p.name " +
                        "FROM team_player tp " +
                        "JOIN player p ON tp.player_id = p.player_id " +
                        "JOIN team t ON tp.team_id = t.team_id " +
                        "WHERE tp.team_id = ? " +
                        "AND p.is_active = 1 " +
                        "AND t.is_active = 1 " +
                        "AND p.gender = 'MALE' " +
                        "AND t.gender = 'MALE' " +
                        "AND p.name LIKE '%Automation%' " +
                        "AND t.name LIKE '%Automation%' " +
                        "LIMIT ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, teamId);
            ps.setInt(2, playerLimit);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                players.add(rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return players;
    }

    public String getTeamNameById(Connection conn, int teamId) {

        String teamName = null;

        String query =
                "SELECT name " +
                        "FROM team " +
                        "WHERE team_id = ? " +
                        "AND is_active = 1 " +
                        "AND gender = 'MALE' " +
                        "AND name LIKE '%Automation%'";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, teamId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                teamName = rs.getString("name");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return teamName;
    }
}
