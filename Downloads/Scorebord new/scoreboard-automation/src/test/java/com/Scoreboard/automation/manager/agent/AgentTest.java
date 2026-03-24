package com.Scoreboard.automation.manager.agent;

import com.Scoreboard.automation.admin.agent.AgentPage;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AgentTest extends CommonMethods {
    public Logger logger = Logger.getLogger(AgentTest.class);
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());

    AgentTest() {
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
            getTestBase().getWebDriverWait().until(ExpectedConditions.visibilityOf(agentNameTextbox));
            baseUtil.enterText(agentNameTextbox, username);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
//            baseUtil.selectDropdown(managerDropdown,managerName);
//            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
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
            agentIds.add(agentId);
            agentList.add(username);
            if (flag) {
                logout();
            }
        } catch (Exception e) {
            logger.error("Unable to add agent due to: ", e);
            Assert.fail("Test failed while adding agent: " + e.getMessage());
        }
    }
}
