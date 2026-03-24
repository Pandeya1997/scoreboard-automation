package com.Scoreboard.automation.admin.matchHistory;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.*;
import io.cucumber.java.bs.A;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class MatchHistoryPage extends CommonMethods {
    public Logger logger = Logger.getLogger(MatchHistoryPage.class);
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());
    String winningTeam = "team_x_win";
    String winningTeamA, winningTeamB, draw;

    MatchHistoryPage() {
        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "(//option[contains(text(),'Select Result')]//..)[1]")
    WebElement selectResult;

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//form")
    WebElement filterPopup;

    ParallelAgentExecutor parallelAgentExecutor = new ParallelAgentExecutor();

    public void performMatchActionOnAgentUI(boolean toss) {
        logger.info("updating match score on the agent UI...");
        WebDriver adminDriver = TestBase.getWebDriver();
        try {
            parallelAgentExecutor.executeParallelAgentsSequentialAction(
                    agentIds,
                    "P@ssword1!",
                    toss
            );
        } catch (Exception e) {
            Assert.fail("unable to update match score due to " + e.getMessage());
        } finally {
            // restore admin context
            TestBase.setWebDriver(adminDriver);
            parallelAgentExecutor.quitAgentDriver();
        }
    }


    public void updateMatchResult() {
        try {

            clickOnButton("matches module");
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.searchData(searchBox, matchList.get(matchList.size() - 1));
            clickOnButton("edit match result");
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            clickOnButton("yes btn");
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            selectResult.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            teamAScore = Integer.parseInt(teamsScoreList.get("TeamAScore"));
            teamBScore = Integer.parseInt(teamsScoreList.get("TeamBScore"));
            if (teamAScore > teamBScore) {
                selectMatchResultByTeamName(TestBase.getWebDriver(), menTeamList.get(menTeamList.size() - 1));
                winningTeamA = menTeamList.get(menTeamList.size() - 2);
                winningTeam = winningTeam.replaceAll("x", "A");
            } else if (teamAScore < teamBScore) {
                selectMatchResultByTeamName(TestBase.getWebDriver(), menTeamList.get(menTeamList.size() - 2));
                winningTeamB = menTeamList.get(menTeamList.size() - 1);
                winningTeam = winningTeam.replaceAll("x", "B");
            } else {
                selectMatchResultByTeamName(TestBase.getWebDriver(), "DRAW");
                draw = "Match Drawn";
                winningTeam = winningTeam.replaceAll("x", "draw");
            }
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            clickOnButton("save");
        } catch (Exception e) {
            Assert.fail("unable to update match result due to " + e.getMessage());
        }
    }

    public void verifyMatchOnMatchHistoryPage() {
        try {
            String actualResult = "";
            String expectedResult = "";
            if (winningTeam.equalsIgnoreCase("team_A_win")) {
                actualResult = winningTeamA + " Won By " + teamBScore + " Runs";
                logger.info("Actual Team :: " + winningTeamA);
                expectedResult = TestBase.getWebDriver().
                        findElement(By.xpath(("//div[@class='match_status' and contains(text(),'#teamName#')]").
                                replaceAll("#teamName#", winningTeamA))).getText();
            } else if (winningTeam.equalsIgnoreCase("team_B_win")) {
                actualResult = winningTeamA + " Won By " + teamAScore + " 10 Wickets";
                logger.info("Actual Team :: " + winningTeamA);
                expectedResult = TestBase.getWebDriver().
                        findElement(By.xpath(("//div[@class='match_status' and contains(text(),'#teamName#')]").
                                replaceAll("#teamName#", winningTeamB))).getText();

            } else {
                actualResult = draw;
                logger.info("Actual Team :: " + actualResult);
                String xpath = "//div[contains(text(),'#teamName#')]//..//..//..//div[@class='match_status' and contains(text(),'Match Drawn')]".
                        replaceAll("#teamName#", menTeamList.get(menTeamList.size() - 1));
                expectedResult = TestBase.getWebDriver().findElement(By.xpath(xpath)).getText();
            }

            logger.info("Expected Team :: " + expectedResult);
            Assert.assertEquals(actualResult, expectedResult);
        } catch (Exception e) {
            Assert.fail("unable to verify match result on match history page due to " + e.getMessage());
        } finally {
            parallelAgentExecutor.quitAgentDriver();
        }
    }

    public void selectMatchResultByTeamName(WebDriver driver, String teamName) {

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        WebElement selectResult = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.xpath("//select[option[text()='Select Result']]")
                )
        );

        Select select = new Select(selectResult);

        for (WebElement option : select.getOptions()) {
            if (option.getText().contains(teamName)) {
                option.click();
                return;
            }
        }

        throw new AssertionError("Result option not found for team: " + teamName);
    }

    public void verifyManagerFromFilter() {
        APICallUtil apiCallUtil = new APICallUtil();
        try {

            baseUtil.waitForElementToBePresent(filterPopup);

            List<String> apiManagers = apiCallUtil.getAllActiveManagers();
            List<String> uiManagers = getManagerNamesFromDropdown();

            Assert.assertEquals(
                    "On match history, managers are not matching with the api response",
                    new HashSet<>(apiManagers),
                    new HashSet<>(uiManagers)
            );

        } catch (Exception e) {
            Assert.fail("Unable to verify manager dropdown due to: " + e.getMessage());
        }
    }

    public List<String> getManagerNamesFromDropdown() {

        List<String> managerNames = new ArrayList<>();

        WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));

        WebElement dropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//select[@name='manager']")
        ));

        dropdown.click();
        wait.until(driver ->
                new Select(dropdown).getOptions().size() > 1
        );

        Select select = new Select(dropdown);

        List<WebElement> options = select.getOptions();

        for (WebElement option : options) {

            String name = option.getText().trim();

            if (!name.equalsIgnoreCase("Select manager") && !name.isEmpty()) {
                managerNames.add(name);
            }
        }

        return managerNames;
    }
}
