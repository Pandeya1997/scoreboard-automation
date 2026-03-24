package com.Scoreboard.automation.agent.dashboard;

import com.Scoreboard.automation.admin.match.MatchTest;
import com.scoreboard.constants.ModuleNameConstants;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DashboardTest extends CommonMethods {

    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),getTestBase().getWebDriverWait());
    private static final Logger logger = Logger.getLogger(DashboardTest.class);
    List<String> listOfTournament = new LinkedList<>();

    DashboardTest(){
        PageFactory.initElements(TestBase.getWebDriver(),this);
    }

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']/child::button")
    WebElement managerAssignDropdown;

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement addBtn;
    
    @FindBy(xpath = "//form//button[contains(text(),'Add')]")
    WebElement formAddBtn;

    @FindBy(xpath = "//input[@name='name']")
    WebElement tournamentNameTextbox;


    @FindBy(xpath = "//input[@name='num_of_matches']")
    WebElement tournamentNoOfMatchesTextbox;

    @FindBy(xpath = "//*[@name='gender_type']")
    WebElement tournamentGenderTypeDropdown;

    @FindBy(xpath = "//form/div/label[contains(text(),'Tournament Type')]/following-sibling::select")
    WebElement tournamentTypeDropdown;

    @FindBy(xpath = "//form/div/label[contains(text(),'Start Date')]/following-sibling::input")
    WebElement tournamentStartDate;

    @FindBy(xpath = "//form/div/label[contains(text(),'End Date')]/following-sibling::input")
    WebElement tournamentEndDate;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[1]")
    WebElement tournamentCountryDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[2]")
    WebElement tournamentVenueDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[3]")
    WebElement tournamentTeamsDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[4]")
    WebElement tournamentFormatDropdown;

    @FindBy(xpath = "(//a[@class='stat-link-container']//div//h2)[1]")
    WebElement totalMatch;

    @FindBy(xpath = "(//a[@class='stat-link-container']//div//h2)[2]")
    WebElement liveMatch;



    public void assignedManagerToMatch(int matchCount,boolean flag){
        try{

            for (int i = 1; i <= matchCount; i++) {
                baseUtil.enterText(searchBox,matchList.get(matchList.size()-1));
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.click(getTestBase().getReadResources().
                        getDataFromPropertyFile("Global","assignManagerDropdown")
                        .replaceAll("#matchDate#",dateForMatch.get(i-1)));
                baseUtil.click(getTestBase().getReadResources().
                        getDataFromPropertyFile("Global","assignManagerDropdownCheckbox").
                        replaceAll("#index#",String.valueOf(1)).replaceAll("#matchDate#",dateForMatch.get(i-1)));
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                TestBase.getWebDriver().navigate().refresh();
                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                searchBox.click();
                searchBox.sendKeys(Keys.CONTROL + "a");
                searchBox.sendKeys(Keys.DELETE);
            }
        } catch (Exception e) {
            logger.error("Unable to assign the manager to the match due to "+e.getMessage());
            throw new RuntimeException(e);
        }finally {
            if (flag) {
                logout();
            }
        }
    }
    
    public void addTournament(Map<String, String> data){
        try{
            baseUtil.click(addBtn);
            listOfTournament.add(ModuleNameConstants.TOURNAMENT_NAME + getRandomString());
            baseUtil.enterText(tournamentNameTextbox, listOfTournament.get(listOfTournament.size()-1));
            baseUtil.enterText(tournamentNoOfMatchesTextbox, data.get("No. of Matches"));
            String[] countries = data.get("Country").split("&");
            for (String country : countries) {
                tournamentCountryDropdown.click();
                tournamentCountryDropdown.sendKeys(country);
                tournamentCountryDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
            baseUtil.selectDropdown(tournamentVenueDropdown, venueList.get(venueList.size()-1));
            baseUtil.selectDropdown(tournamentGenderTypeDropdown, data.get("Gender Type"));
            tournamentTeamsDropdown.click();
            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size()-1));
            tournamentTeamsDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
            tournamentTeamsDropdown.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size()-1 + 1));
            tournamentTeamsDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            scrollToElement(formAddBtn);
            baseUtil.selectDropdown(tournamentFormatDropdown, data.get("Format Type"));
//                            baseUtil.selectDropdown(tournamentFormatDropdown, data.get("Format Type"));
            baseUtil.selectDropdown(tournamentTypeDropdown, data.get("Tournament Type"));
            tournamentStartDate.sendKeys(getCurrentDatePlusDays(Integer.parseInt(data.get("Start Date"))));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            tournamentEndDate.sendKeys(getCurrentDatePlusDays(Integer.parseInt(data.get("End Date"))));
            baseUtil.click(formAddBtn);
        }catch (Exception e){
            
        }
    }

    public void verifyDashboardCounts(Map<String, String> expectedData) {
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        try {
            Assert.assertEquals(
                    totalMatch.getText().trim(), expectedData.get("Total Matches")

            );

            Assert.assertEquals(
                    liveMatch.getText().trim(),
                    expectedData.get("Live Matches")
            );

            String matchName = matchList.get(matchList.size()-1);

            verifyMatchCount(
                    "(//td[contains(text(),'" + matchName + "')])[1]",
                    expectedData.get("Today Matches")
            );

            verifyMatchCount(
                    "(//td[contains(text(),'" + matchName + "')])[2]",
                    expectedData.get("Tomorrow Matches")
            );

            verifyMatchCount(
                    "(//td[contains(text(),'" + matchName + "')])[3]",
                    expectedData.get("Upcoming Matches")
            );
        }catch (Exception e){
            Assert.fail("Unable to verify dashboard data due to "+e.getMessage());
        }finally {
            logout();
        }
    }

    private void verifyMatchCount(String xpath, String expectedCount) {
        List<WebElement> elements =
                TestBase.getWebDriver().findElements(By.xpath(xpath));

        Assert.assertEquals(
                String.valueOf(elements.size()),
                expectedCount
        );
    }


}
