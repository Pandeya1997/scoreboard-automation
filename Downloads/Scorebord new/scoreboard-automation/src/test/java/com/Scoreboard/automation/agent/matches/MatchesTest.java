package com.Scoreboard.automation.agent.matches;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class MatchesTest extends CommonMethods {
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),getTestBase().getWebDriverWait());
    private static final Logger logger = Logger.getLogger(MatchesTest.class);

    MatchesTest(){
        PageFactory.initElements(TestBase.getWebDriver(),this);
    }

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']//button")
    WebElement agentDropdown;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']//input[@type='checkbox']")
    WebElement agentCheckbox;



    public void searchAddedMatch(){
        baseUtil.searchData(searchBox,matchList.get(matchList.size()-1));
    }

    public void assignAgent(){
        try{
            baseUtil.click(agentDropdown);
            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
            baseUtil.click(agentCheckbox);
        }catch (Exception e){
            Assert.fail("Unable to assign agent to the match due to "+e.getMessage());
        }finally {
            logout();
        }
    }

    public void verifyMatch(boolean visiblity){
        try{
            String xpath= "xpath===//td[contains(text(),'#matchName#')]".
                    replaceAll("#matchName#",matchList.get(matchList.size()-1));
            if (visiblity) {
                baseUtil.isElementDisplayed(TestBase.getWebDriver().findElement(By.xpath(xpath)));
            }else{
                baseUtil.elementShouldNotBeVisible(TestBase.getWebDriver(),xpath);
            }
        }catch (Exception e){
            Assert.fail("Unable to verify the match on the agent UI due to "+e.getMessage());
        }finally {
            logout();
        }
    }


}
