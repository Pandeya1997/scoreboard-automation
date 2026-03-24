package com.Scoreboard.automation.admin.match;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import io.cucumber.java.an.E;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.ssl.X509;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.util.*;
import java.util.NoSuchElementException;

public class MatchTest extends CommonMethods {

    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());
    private static final Logger logger = Logger.getLogger(MatchTest.class);

    MatchTest() {
        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//h5[contains(text(),'Total Matches')]/../../child::h2")
    WebElement totalNumberOfMatchesDashboard;

    @FindBy(xpath = "//div[@role='menu']")
    WebElement assignToManagerDropdown;

    @FindBy(xpath = "//h5[contains(text(),'Live Matches')]/../../child::h2")
    WebElement liveMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Agents')]/../../child::h2")
    WebElement totalAgentDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Teams')]/../../child::h2")
    WebElement totalTeamsDashboard;

    @FindBy(xpath = "//*[contains(text(),'match status updated successfully')]")
    WebElement successMsg;

    @FindBy(xpath = "//div[contains(text(),'Cannot select more than 2 managers!')]")
    WebElement errorMsgForManagerAssign;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']/child::button")
    WebElement managerAssignDropdown;

    @FindBy(xpath = "//*[contains(text(),'Match assigned to Manager successfully')]")
    WebElement managerAssignSuccessMsg;


    public void searchAddedMatch() {
        baseUtil.searchData(searchBox, "Automation_Match_4829b");
    }

    public void verifyDashBoardData(String day) throws IOException {
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        Assert.assertEquals(String.valueOf(Integer.parseInt(dashboardData.get("Total Matches")) + 1)
                , totalNumberOfMatchesDashboard.getText());
        if (day.equalsIgnoreCase("tomorrow")) {
            logger.info("Verifying match on dashboard page for tomorrow.");
            WebElement element = baseUtil.getElementAfterLoaded(getTestBase().getReadResources().getDataFromPropertyFile("Global", "matchListOnDashboardPage").
                    replaceAll("#value1#", "Tomorrow Matches").replaceAll("#value2#", matchList.get(matchList.size() - 1)));
            Assert.assertTrue("Match is not visible for tomorrow", baseUtil.isElementDisplayed(element));
        } else if (day.equalsIgnoreCase("upcoming")) {
            logger.info("Verifying upcoming match on dashboard page.");
            WebElement element = baseUtil.getElementAfterLoaded(getTestBase().getReadResources().getDataFromPropertyFile("Global", "matchListOnDashboardPage").
                    replaceAll("#value1#", "Upcoming Matches").replaceAll("#value2#", matchList.get(matchList.size() - 1)));
            Assert.assertTrue("Match is not visible for upcoming", baseUtil.isElementDisplayed(element));
        } else if (day.equalsIgnoreCase("Today")) {
            logger.info("Verifying match on dashboard page for today");
            WebElement element = baseUtil.getElementAfterLoaded(getTestBase().getReadResources().getDataFromPropertyFile("Global", "matchListOnDashboardPage").
                    replaceAll("#value1#", "Today Matches").replaceAll("#value2#", matchList.get(matchList.size() - 1)));
            Assert.assertTrue("Match is not visible for today", baseUtil.isElementDisplayed(element));
        } else {
            Assert.fail("Provide a proper match timing. No data available for " + day);
        }

    }

    public void updateMatchStatus(String count, String status) {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            for (String match : matchList) {
                map.put(match, "active");
            }
            updateStatus(status, count, map);
            baseUtil.isElementDisplayed(successMsg);
        } catch (Exception e) {
            logger.error("unable to update match status due to " + e.getMessage());
        }
    }

    public void verifyMatchOnLiveMatchPage(String status) {
        try {
            if (status.equalsIgnoreCase("live")) {
                Assert.assertFalse(baseUtil.elementShouldNotBeVisible(
                        TestBase.getWebDriver(), getTestBase().getReadResources().
                                getDataFromPropertyFile("Global", "liveMatchPageData")
                                .replaceAll("#value#", matchList.get(matchList.size() - 1))));
            } else if (status.equalsIgnoreCase("non live")) {
                Assert.assertTrue(baseUtil.elementShouldNotBeVisible(
                        TestBase.getWebDriver(), getTestBase().getReadResources().
                                getDataFromPropertyFile("Global", "liveMatchPageData")
                                .replaceAll("#value#", matchList.get(matchList.size() - 1))));
            } else {
                logger.error("please provide a valid status :: " + status);
            }
        } catch (Exception e) {
            Assert.fail("unable to verify match on live match page due to " + e.getMessage());
        }
    }

    public void assignedManagerToMatch(int managerCount){
        try{
            baseUtil.enterText(searchBox,matchList.get(matchList.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(managerAssignDropdown);
            for (int i = managerCount - 1; i >= 0; i--) {
                baseUtil.click(getTestBase().getReadResources().
                        getDataFromPropertyFile("Global", "managerInMatchAssignManagerDropdown").
                        replaceAll("#agentName#", managerList.get(i)));
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
        } catch (Exception e) {
            logger.error("Unable to assign the manager to the match due to " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void verifyErrorMsgForMoreThenTwoManagerAssigned(String errorMsg) {
        try {
            Assert.assertEquals(baseUtil.getElementText(errorMsgForManagerAssign), errorMsg);
        } catch (Exception e) {
            logger.error("Unable to verify the error message due to " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

//    public boolean isCheckboxChecked(String xpath) {
//        WebElement checkbox = TestBase.getWebDriver().findElement(By.xpath(xpath));
//        JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
//        Object result = js.executeScript("return arguments[0]?.checked;", checkbox);
//        if (result == null) {
//            logger.info("JavaScript returned null for checkbox: " + xpath);
//            return false;
//        }
//        return Boolean.TRUE.equals(result);
//    }

    public boolean isCheckboxChecked(String locatorKey) {
        logger.debug("Executing Test Step :: " + new Object() {
        }.getClass().getEnclosingMethod().getName());

        WebDriver driver = TestBase.getWebDriver();
        WebDriverWait wait = getTestBase().getWebDriverWait();

        try {
            By menuLocator = By.xpath("//div[@role='menu']");
            By buttonLocator = By.xpath("//button[@class='btn btn-secondary']");

            boolean isMenuVisible = false;
            try {
                WebElement menu = wait.until(ExpectedConditions.presenceOfElementLocated(menuLocator));
                isMenuVisible = menu.isDisplayed();
            } catch (TimeoutException ignored) {
                logger.debug("Dropdown menu not visible initially.");
            }

            if (!isMenuVisible) {
                logger.info("Menu not visible — clicking secondary button to open it...");
                try {
                    WebElement button = wait.until(ExpectedConditions.elementToBeClickable(buttonLocator));
                    button.click();
                    logger.info("Clicked the secondary button successfully.");
                } catch (Exception e) {
                    logger.warn("Unable to click the secondary button: " + e.getMessage());
                }

                Thread.sleep(500);
            } else {
                logger.info("Menu is already visible — skipping button click.");
            }

            String[] locatorParts = locatorKey.split("===", 2);
            if (locatorParts.length != 2) {
                logger.error("Invalid locator format for key: " + locatorKey);
                return false;
            }

            String locatorType = locatorParts[0].trim().toLowerCase();
            String locatorPath = locatorParts[1].trim();
            logger.info("The locator for the checkbox element is: " + locatorPath);

            WebElement checkbox;
            switch (locatorType) {
                case "id":
                    checkbox = driver.findElement(By.id(locatorPath));
                    break;
                case "name":
                    checkbox = driver.findElement(By.name(locatorPath));
                    break;
                case "xpath":
                    checkbox = driver.findElement(By.xpath(locatorPath));
                    break;
                case "css":
                    checkbox = driver.findElement(By.cssSelector(locatorPath));
                    break;
                default:
                    logger.error("Unsupported locator type: " + locatorType);
                    return false;
            }

            JavascriptExecutor js = (JavascriptExecutor) driver;
            Object result = js.executeScript("return arguments[0] ? arguments[0].checked : false;", checkbox);

            boolean isChecked = false;
            if (result instanceof Boolean) {
                isChecked = (Boolean) result;
            } else if (result instanceof String) {
                isChecked = Boolean.parseBoolean((String) result);
            }

            logger.info("Checkbox status for key [" + locatorKey + "] = " + isChecked);
            return isChecked;

        } catch (Exception e) {
            logger.error("Error while verifying checkbox for key: " + locatorKey, e);
            return false;
        }
    }


    public void verifyAddedManager(boolean shouldBeChecked, int count, String pageName) {
        logger.info("Verifying added manager checkboxes on " + pageName + " page...");
        if (managerList.size() == 3) {
            managerList.remove(0);
        }
        try {
            WebDriver driver = TestBase.getWebDriver();
            WebDriverWait wait = getTestBase().getWebDriverWait();
            driver.navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            baseUtil.enterText(searchBox, matchList.get(matchList.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            try {
                if (isMenuClosed()) {
                    logger.info("Dropdown menu not open — clicking dropdown button...");
                    wait.until(ExpectedConditions.elementToBeClickable(managerAssignDropdown)).click();
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                } else {
                    logger.info("Dropdown menu already open — skipping click.");
                }
            } catch (Exception e) {
                logger.warn("Dropdown click attempt failed, retrying once...");
                try {
                    baseUtil.click(managerAssignDropdown);
                } catch (Exception ignored) {
                }
            }
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            String checkboxXpathTemplate = getTestBase()
                    .getReadResources()
                    .getDataFromPropertyFile("Global", "managerCheckboxByManagerName");

            List<String> subList = managerList.subList(0, Math.min(count, managerList.size()));

            List<String> failed = new ArrayList<>();

            for (String managerName : subList) {
                String finalXpath = checkboxXpathTemplate.replace("#value#", managerName);

                try {
                    WebElement cb = TestBase.getWebDriver().findElement(By.xpath(finalXpath.split("===", 2)[1]));
                    new WebDriverWait(driver, Duration.ofSeconds(3))
                            .until(ExpectedConditions.visibilityOf(cb));
                } catch (Exception ignored) {
                }

                boolean isChecked = isCheckboxChecked(finalXpath);

                if (shouldBeChecked && !isChecked) {
                    failed.add(managerName + " (Expected: Checked)");
                } else if (!shouldBeChecked && isChecked) {
                    failed.add(managerName + " (Expected: Unchecked)");
                }
            }

            if (!failed.isEmpty()) {
                Assert.fail("Verification failed on " + pageName + " for managers: " + String.join(", ", failed));
            }

        } catch (Exception e) {
            Assert.fail("Error verifying managers on " + pageName + ": " + e.getMessage());
        }
    }


//    public boolean verifyCheckedManagers(boolean expectedChecked, int expectedCount) {
//        WebDriver driver = TestBase.getWebDriver();
//        String locatorValue="";
//        try {
//            locatorValue = getTestBase().getReadResources().
//                    getDataFromPropertyFile("Global","managerCheckboxByManagerName");
//            if (locatorValue == null || locatorValue.isEmpty()) {
//                logger.error("Locator not found for key: " + locatorValue);
//                return false;
//            }
//
//            String[] locatorParts = locatorValue.split("===", 2);
//            if (locatorParts.length != 2) {
//                logger.error("Invalid locator format for key: " + locatorValue);
//                return false;
//            }
//
//            String locatorType = locatorParts[0].trim().toLowerCase();
//            String locatorPath = locatorParts[1].trim();
//
//            List<WebElement> checkboxes;
//
//            switch (locatorType) {
//                case "xpath":
//                    checkboxes = driver.findElements(By.xpath(locatorPath));
//                    break;
//                case "css":
//                    checkboxes = driver.findElements(By.cssSelector(locatorPath));
//                    break;
//                default:
//                    logger.error("Unsupported locator type: " + locatorType);
//                    return false;
//            }
//
//            if (checkboxes.isEmpty()) {
//                logger.error("No checkboxes found for locator: " + locatorValue);
//                return false;
//            }
//
//            JavascriptExecutor js = (JavascriptExecutor) driver;
//            int checkedCount = 0;
//
//            for (WebElement checkbox : checkboxes) {
//                Object result = js.executeScript("return arguments[0] ? arguments[0].checked : false;", checkbox);
//                boolean isChecked = result instanceof Boolean ? (Boolean) result
//                        : result instanceof String ? Boolean.parseBoolean((String) result)
//                        : false;
//
//                if (isChecked) checkedCount++;
//
//                if (expectedChecked && !isChecked) {
//                    logger.warn("Expected checkbox to be checked but found unchecked: " + checkbox.getAttribute("id"));
//                } else if (!expectedChecked && isChecked) {
//                    logger.warn("Expected checkbox to be unchecked but found checked: " + checkbox.getAttribute("id"));
//                }
//            }
//
//            logger.info("Total checked managers found: " + checkedCount);
//
//            if (checkedCount == expectedCount) {
//                logger.info("Manager checkbox count matches expected count: " + expectedCount);
//                return true;
//            } else {
//                logger.error("Expected " + expectedCount + " checked, but found " + checkedCount);
//                return false;
//            }
//
//        } catch (Exception e) {
//            logger.error("Error verifying checked managers for locator: " + locatorValue, e);
//            return false;
//        }
//    }


    public void editManagerForMatch(int count, String page) {
        try {
            WebDriver driver = TestBase.getWebDriver();
            WebDriverWait wait = getTestBase().getWebDriverWait();
            baseUtil.enterText(searchBox, matchList.get(matchList.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            try {
                if (isMenuClosed()) {
                    logger.info("Dropdown menu not open — clicking dropdown button...");
                    wait.until(ExpectedConditions.elementToBeClickable(managerAssignDropdown)).click();
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                } else {
                    logger.info("Dropdown menu already open — skipping click.");
                }
            } catch (Exception e) {
                logger.warn("Dropdown click attempt failed, retrying once...");
                try {
                    baseUtil.click(managerAssignDropdown);
                } catch (Exception ignored) {
                }
            }
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            String tpl = getTestBase().getReadResources()
                    .getDataFromPropertyFile("Global", "managerCheckboxByManagerName");

            String targetManager = managerList.get(managerList.size() - count);
            String finalXpath = tpl.replace("#value#", targetManager);

            WebElement cb = driver.findElement(By.xpath(finalXpath.split("===", 2)[1]));
            boolean before = cb.isSelected();
            cb.click();

            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementSelectionStateToBe(cb, !before));

            boolean after = cb.isSelected();
            if (!after && managerList.contains(targetManager)) {
                managerList.remove(targetManager);
                logger.info("Removed deselected manager from managerList: " + targetManager);
            }

            // (optional) close dropdown so the next step reopens a fresh DOM
            cb.sendKeys(Keys.ESCAPE);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            logger.info("Edited manager: " + targetManager + " on " + page);
        } catch (Exception e) {
            Assert.fail("unable to edit assigned manager on " + page + " page due to " + e.getMessage());
        }
    }


    private boolean isMenuClosed() {
        try {
            return !baseUtil.isElementDisplayed(assignToManagerDropdown);
        } catch (NoSuchElementException e) {
            return true;
        }
    }

    public void verifyMessage(String msg) {
        Assert.assertEquals(msg, managerAssignSuccessMsg.getText());
    }

}
