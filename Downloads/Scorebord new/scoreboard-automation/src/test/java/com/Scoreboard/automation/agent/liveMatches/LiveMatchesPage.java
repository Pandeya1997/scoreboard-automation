package com.Scoreboard.automation.agent.liveMatches;


import com.Scoreboard.automation.admin.manager.ManagerPage;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import io.cucumber.java.it.Ma;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LiveMatchesPage extends CommonMethods {

    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),getTestBase().getWebDriverWait());
    protected static String updatedManagerName;
    public Logger logger = Logger.getLogger(ManagerPage.class);
//    ManagerPage(){
//        PageFactory.initElements(TestBase.getWebDriver(),this);
//    }

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement AddManagerBtn;

    @FindBy(xpath = "//*[contains(@name,'name')]")
    WebElement managerNameTextbox;

    @FindBy(xpath = "//*[contains(@name,'mobile_no')]")
    WebElement managerMobileNoTextbox;

    @FindBy(xpath = "//*[contains(@name,'passwd')]")
    WebElement managerPwdTextbox;

    @FindBy(xpath = "//*[contains(@type,'submit')]")
    WebElement managerSubmitFormBtn;

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
    WebElement editManagerBtn;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement updateManagerBtn;

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    WebElement submitManagerPasswordBtn;

    @FindBy(xpath = "//i[@alt='password']")
    WebElement updateManagerPassword;

    @FindBy(xpath = "//button[@aria-label='Close']")
    WebElement closeBtnManagerEditPopup;

    @FindBy(xpath = "//input[@name='newPassword']")
    WebElement newPassword;

    @FindBy(xpath = "//input[@name='confirmPassword']")
    WebElement confirmPassword;


    public void addManager(int count,String password, Boolean flag) {
        try {
            if (!managerList.isEmpty()) {
                managerList.remove(0);
            }
            for (int i = 0; i < count; i++) {
                managerName = "AutomationManager" + getRandomString();
                mobileNo = generateUnique10DigitNumber();
                logger.info("adding manager..."+i+1);
                baseUtil.click(AddManagerBtn);
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.enterText(managerNameTextbox, managerName);
                baseUtil.enterText(managerMobileNoTextbox, mobileNo);
                baseUtil.enterText(managerPwdTextbox, password);
                baseUtil.click(managerSubmitFormBtn);
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.enterText(searchBox, managerName);
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                managerId = baseUtil.getElementText(
                        getTestBase().getReadResources().getDataFromPropertyFile("Global", "managerIDText").replaceAll("#value#", managerName)
                );
                Assert.assertTrue(
                        "Unable to verify the created manager, either it is not visible on the UI or unable to found it on UI",
                        baseUtil.isElementDisplayed(getTestBase().getReadResources()
                                .getDataFromPropertyFile("Global", "managerIDText").replaceAll("#value#", managerName))
                );
                managerList.add(managerName);
            }

            if (flag) {
                logout();
            }
        } catch (Exception e) {
            logger.error("Unable to add manager due to: ", e);
            Assert.fail("Test failed while adding manager: " + e.getMessage());
        }
    }


    public void deactivateManager(){
        try{
            logger.info("Deactivating the manager...");
            Map<String,String> map = new LinkedHashMap<>();
            for(String match:managerList){
                map.put(match,"active");
            }
            baseUtil.enterText(searchBox, managerName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            updateStatus("inactive","1",map);
        }catch (Exception e){
            Assert.fail("unable to deactivate manager due to "+e.getMessage());
        }
    }

    public void updateManagerDetails(){
        try{
            String xpath="xpath===//input[@value='#value#']";
            updatedManagerName="AutomationManager" + getRandomString();
            String updatedManagerMobNo=generateUnique10DigitNumber();
            baseUtil.enterText(searchBox, managerName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(editManagerBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(managerNameTextbox, updatedManagerName);
            baseUtil.enterText(managerMobileNoTextbox, updatedManagerMobNo);
            baseUtil.click(updateManagerBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, updatedManagerName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(editManagerBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("updated manager name is not visible on the edit manager UI.",baseUtil.isElementDisplayed
                    (xpath.replaceAll("#value#",updatedManagerName)));
            Assert.assertTrue("updated manager mobile no is not visible on edit manager UI.",baseUtil.isElementDisplayed
                    (xpath.replaceAll("#value#",updatedManagerMobNo)));

        }catch (Exception e){
            Assert.fail("unable to update the manager details due to "+e.getMessage());
        }finally {
            baseUtil.click(closeBtnManagerEditPopup);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

        }
    }

    public void updateMangerPassword(Map<String,String> data){
        try{
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, updatedManagerName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(updateManagerPassword);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(newPassword, data.get("New Password"));
            baseUtil.enterText(confirmPassword, data.get("Confirm Password"));
            baseUtil.click(submitManagerPasswordBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }catch (Exception e){
            Assert.fail("unable to update manager password due to "+e.getMessage());
        }
    }
}

