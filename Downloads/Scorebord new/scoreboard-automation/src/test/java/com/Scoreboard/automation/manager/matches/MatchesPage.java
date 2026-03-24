package com.Scoreboard.automation.manager.matches;

import com.Scoreboard.automation.admin.manager.ManagerPage;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;

public class MatchesPage extends CommonMethods {
    Logger logger = Logger.getLogger(MatchesPage.class);
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),getTestBase().getWebDriverWait());

    MatchesPage(){
        PageFactory.initElements(TestBase.getWebDriver(),this);
    }

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "(//span[contains(text(),'Matches')])[1]")
    WebElement matchesPage;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']/button")
    WebElement assignToManagerDropdown;

    public void verifyMatch() throws IOException {
        logger.info("Verifying match on the manager UI..."+ logger.getName());
        baseUtil.searchData(searchBox,matchList.get(matchList.size()-1));
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        String locator = getTestBase().getReadResources()
                .getDataFromPropertyFile("Global", "assetName")
                .replaceAll("#value#", matchList.get(matchList.size()-1));
        Assert.assertFalse("Unable to verify: " + matchList.get(matchList.size()-1), baseUtil.isElementDisplayed(locator));
    }

    public void verifyAssignedManagerToMatch(){
        logger.info("Verifying the assigned manager to the match that is created by the manager on admin panel "+
                logger.getName());
        try{
            baseUtil.click(matchesPage);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.searchData(searchBox,matchList.get(matchList.size()-1));
            baseUtil.click(assignToManagerDropdown);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertTrue("Checkbox is not selected as the match is created" +
                    " by that manager it must be selected on the admin ui for the same match.",
                    isCheckboxSelected());
        }catch (Exception e){
            Assert.fail("Unable to verify the assigned manager to the match that are created by the manager due to "+
                    e.getMessage());
        }
    }

}
