package com.Scoreboard.automation.user;

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

public class UserPage extends CommonMethods {
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());
    public Logger logger = Logger.getLogger(UserPage.class);

    UserPage() {
        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "//div[@class='nw-scoreboard-team-score team-a']/div[1]")
    WebElement teamA;

    @FindBy(xpath = "//div[@class='nw-scoreboard-team-score team-b']/span[1]")
    WebElement teamB;

    @FindBy(xpath = "//div[@class='nw-scoreboard-match-title']/h2")
    WebElement vsTeams;

    @FindBy(xpath = "//div[@class='nw-scoreboard-match-title']/p")
    WebElement matchDetail;

    @FindBy(xpath = "//div[@class='nw-scoreboard-player-info']//div[2]//p")
    WebElement tossDetails;

    @FindBy(xpath = "//h5[contains(text(),'Match not found...')]")
    WebElement userUI;

    public void verifyUIDataOnUsersPanelWithCreatedMatch() {
        logger.info("Verifying the admin created match data with user's UI...");
        try {
            Map<String, String> expectedMatchData = new LinkedHashMap<>();
            Map<String, String> actualMatchData = new LinkedHashMap<>();
            expectedMatchData.put("teamAName", teamA.getText());
            expectedMatchData.put("teamBName", teamB.getText());
            expectedMatchData.put("vsTeam", vsTeams.getText());
            expectedMatchData.put("matchDetail", matchDetail.getText());
            actualMatchData.put("teamAName", menTeamList.get(menTeamList.size()-1));
            actualMatchData.put("teamBName", menTeamList.get(1));
            actualMatchData.put("vsTeam", menTeamList.get(menTeamList.size()-1) + menTeamList.get(1) + ",T20 (Men)");
            actualMatchData.put("matchDetail", "Match has not started. Starts at " + getCurrentDatePlusDays(1) + twentyMinutesAHeadTimeForMatch + "am");
            Assert.assertEquals(expectedMatchData, actualMatchData);
        } catch (Exception e) {
            Assert.fail("unable to verify the user panel data with admin created match due to " + e.getMessage());
        }
    }

    public void verifyMatchOnUsersUI(String matchData) {
        String toss, UIData;
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        try {
            if (matchData.equalsIgnoreCase("toss not happened")) {
                toss = tossDetails.getText();
                UIData = "Toss Will Take Place Shortly";
                Assert.assertEquals(UIData, toss);
            } else if (matchData.equalsIgnoreCase("toss happened")) {
                toss = tossDetails.getText();
                Assert.assertEquals("", toss);
            } else if (matchData.equalsIgnoreCase("live")) {
                Assert.assertTrue("Match is not visible after making it live", baseUtil.isElementDisplayed(matchDetail));
            } else if (matchData.equalsIgnoreCase("non live")) {
                Assert.assertTrue("Match is not visible after making it non live", baseUtil.isElementDisplayed(matchDetail));
            }
        } catch (Exception e) {
            Assert.fail(" unable to verify match data " + matchData + " on users UI due to " + e.getMessage());
        }
    }

    public void verifyUserUI(String value){
        String invalidEventId="";
        try{
            invalidEventId=getRandomString();
            TestBase.getWebDriver().get(TestBase.getUsersURL()+"/"+invalidEventId);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("Default screen is not visible for invalid eventID :: "+invalidEventId
                    ,baseUtil.isElementDisplayed(userUI));
        }catch (Exception e){
            Assert.fail("Unable to verify user's UI for invalid event id :: "+invalidEventId);
        }

    }
}
