package com.Scoreboard.automation.admin.agent;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public class AgentPage extends CommonMethods {
    public Logger logger = Logger.getLogger(AgentPage.class);
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());
    protected static String updatedAgentName;

    AgentPage() {
        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement AddAgentBtn;

    @FindBy(xpath = "//*[contains(@name,'name')]")
    WebElement agentNameTextbox;

    @FindBy(xpath = "//div[contains(@id,'react-select') and contains(@id,'placeholder')]/parent::div")
    WebElement managerDropdown;

    @FindBy(xpath = "//*[contains(@name,'mobile_no')]")
    WebElement agentMobileNoTextbox;

    @FindBy(xpath = "//*[contains(@name,'passwd')]")
    WebElement agentPwdTextbox;

    @FindBy(xpath = "//*[contains(@type,'submit')]")
    WebElement agentSubmitFormBtn;

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//button[@class='logout-btn']/../..")
    WebElement logoutBtn;

    @FindBy(xpath = "//*[@id='cust_user_id']")
    WebElement usernameTextbox;

    @FindBy(xpath = "//*[contains(text(),'sign-in success')]")
    WebElement loginSuccessMsg;

    @FindBy(xpath = "//*[contains(text(),'Your account has been deactivated.')]")
    WebElement accountDeactivationMsg;

    @FindBy(xpath = "//i[@alt='edit']")
    WebElement editAgentBtn;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement updateAgentBtn;

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    WebElement submitAgentPasswordBtn;

    @FindBy(xpath = "//i[@alt='password']")
    WebElement updateAgentPassword;

    @FindBy(xpath = "//button[@aria-label='Close']")
    WebElement closeBtnAgentEditPopup;

    @FindBy(xpath = "//input[@name='newPassword']")
    WebElement newPassword;

    @FindBy(xpath = "//input[@name='confirmPassword']")
    WebElement confirmPassword;

    public void addAgent(String username, String mobileNo, String password, Boolean flag) {
        try {
            logger.info("adding agent...");
            if (!agentList.isEmpty()) {
                agentList.remove(0);
            }
            baseUtil.click(AddAgentBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(agentNameTextbox, username);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.selectDropdown(managerDropdown,managerName);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            baseUtil.enterText(agentMobileNoTextbox, mobileNo);
            baseUtil.enterText(agentPwdTextbox, password);
            baseUtil.click(agentSubmitFormBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, username);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            agentId = baseUtil.getElementText(
                    getTestBase().getReadResources().getDataFromPropertyFile("Global", "agentIDText").replaceAll("#value#", username)
            );
            Assert.assertTrue(
                    "Unable to verify the created agent, either it is not visible on the UI or unable to found it on UI",
                    baseUtil.isElementDisplayed(getTestBase().getReadResources()
                            .getDataFromPropertyFile("Global", "agentIDText").replaceAll("#value#", username))
            );
            agentList.add(username);
            if (flag) {
                logout();
            }
        } catch (Exception e) {
            logger.error("Unable to add agent due to: ", e);
            Assert.fail("Test failed while adding agent: " + e.getMessage());
        }
    }

    public void verifyLoginMessage(String msgType, String msg) {
        try {
            if (msgType.equalsIgnoreCase("success")) {
                Assert.assertEquals(msg, baseUtil.getElementText(loginSuccessMsg));
            } else if (msgType.equalsIgnoreCase("deactivation")) {
                Assert.assertEquals(msg, baseUtil.getElementText(accountDeactivationMsg));
            }
        } catch (Exception e) {
            logger.error("Unable to verify login message due to " + e.getMessage());
        }
    }

    public void deactivateAgent() {
        try {
            logger.info("Deactivating the agent...");
            Map<String, String> map = new LinkedHashMap<>();
            for (String match : agentList) {
                map.put(match, "active");
            }
            baseUtil.enterText(searchBox, agentName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            updateStatus("inactive", "1", map);
        } catch (Exception e) {
            Assert.fail("unable to deactivate agent due to " + e.getMessage());
        }
    }

    public void updateAgentDetails() {
        try {
            String xpath = "xpath===//input[@value='#value#']";
            updatedAgentName = "AutomationAgent" + getRandomString();
            String updatedAgentMobNo = generateUnique10DigitNumber();
            baseUtil.enterText(searchBox, agentName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(editAgentBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(agentNameTextbox, updatedAgentName);
            baseUtil.enterText(agentMobileNoTextbox, updatedAgentMobNo);
            baseUtil.click(updateAgentBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, updatedAgentName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(editAgentBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("updated agent name is not visible on the edit agent UI.", baseUtil.isElementDisplayed
                    (xpath.replaceAll("#value#", updatedAgentName)));
            Assert.assertTrue("updated agent mobile no is not visible on edit agent UI.", baseUtil.isElementDisplayed
                    (xpath.replaceAll("#value#", updatedAgentMobNo)));

        } catch (Exception e) {
            Assert.fail("unable to update the agent details due to " + e.getMessage());
        } finally {
            baseUtil.click(closeBtnAgentEditPopup);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

        }
    }

    public void updateMangerPassword(Map<String, String> data) {
        try {
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, updatedAgentName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(updateAgentPassword);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(newPassword, data.get("New Password"));
            baseUtil.enterText(confirmPassword, data.get("Confirm Password"));
            baseUtil.click(submitAgentPasswordBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        } catch (Exception e) {
            Assert.fail("unable to update agent password due to " + e.getMessage());
        }
    }
}
