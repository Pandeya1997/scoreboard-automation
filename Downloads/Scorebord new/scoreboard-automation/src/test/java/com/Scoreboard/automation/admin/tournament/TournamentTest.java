package com.Scoreboard.automation.admin.tournament;

import com.scoreboard.constants.ModuleNameConstants;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

public class TournamentTest extends CommonMethods {
    private static final Logger logger = Logger.getLogger(TournamentTest.class);
    TournamentTest(){
        PageFactory.initElements(TestBase.getWebDriver(),this);
    }
    @FindBy(xpath = "//h5[contains(text(),'Total Matches')]/../../child::h2")
    WebElement totalNumberOfMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Managers')]/../../child::h2")
    WebElement totalManagerDashboard;

    @FindBy(xpath = "//input[@name='name']")
    WebElement tournamentNameTextbox;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[1]")
    WebElement tournamentCountryDropdown;

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement addBtn;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[2]")
    WebElement tournamentVenueDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[3]")
    WebElement tournamentTeamsDropdown;

    @FindBy(xpath = "//button[@aria-label='Close']")
    WebElement tournamentCloseBtn;

    @FindBy(xpath = "//h5[contains(text(),'Live Matches')]/../../child::h2")
    WebElement liveMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Agents')]/../../child::h2")
    WebElement totalAgentDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Teams')]/../../child::h2")
    WebElement totalTeamsDashboard;

    @FindBy(xpath = "//input[@placeholder='Schedule Name']")
    WebElement scheduleNameTextbox;

    @FindBy(xpath = "//input[@name='num_of_matches']")
    WebElement tournamentNoOfMatchesTextbox;

    @FindBy(xpath = "//*[@name='gender_type']")
    WebElement tournamentGenderTypeDropdown;

    @FindBy(xpath = "//h4[contains(text(),'Existing Schedules')]/../child::ul/li/span")
    WebElement addedScheduleOnPopup;

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//button[@class='btn-close']")
    WebElement formCloseBtn;

    @FindBy(xpath = "//div[@class='Toastify__toast-body']")
    WebElement validationMsgForSchedule;

    @FindBy(xpath = "//div[contains(text(),'Schedules')]/../..")
    WebElement schedulePopup;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and @type='text'])[1]")
    WebElement firstPlayerDropdownOnSelectPlayerPopup;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and @type='text'])[2]")
    WebElement secondPlayerDropdownOnSelectPlayerPopup;

    @FindBy(xpath = "//button[contains(text(),'Save Selection')]")
    WebElement saveSelectionBtn;

    @FindBy(xpath = "//i[@title='Select Players']")
    WebElement selectPlayerBtn;

    @FindBy(xpath = "((//div[contains(@class,'select__menu')])[2])[1]")
    WebElement firstDropdownOptions;

    @FindBy(xpath = "((//div[contains(@class,'select__menu')])[2])[1]/child::div[contains(text(),'No options')]")
    WebElement firstDropdownOptionsWithNoOptions;

    @FindBy(xpath = "//label[contains(text(),'Tournament: ')]/following-sibling::select[@class='form-control ']")
    WebElement tournamentDropdownOnCreateMatchPopup;


    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),getTestBase().getWebDriverWait());
    public void verifyTournamentDataOnDashboard(int count) throws IOException {
        clickOnButton("dashboard module");
        int totalTeams=Integer.parseInt(dashboardData.get("Total Teams"))+2;
        Assert.assertEquals(String.valueOf(totalTeams),baseUtil.getElementText(totalTeamsDashboard));
    }

    public void addOrDeleteSchedule(String action){
        scheduleName = "Test"+getRandomString();
        try{
            logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());
            if(BaseUtil.elementShouldNotBeVisible(schedulePopup)){
                clickOnButton("schedule");
            }
            if(BaseUtil.elementShouldNotBeVisible(scheduleNameTextbox)){
                clickOnButton("add schedule");
            }
            if(action.equalsIgnoreCase("add")){
                baseUtil.enterText(scheduleNameTextbox,scheduleName);
                clickOnButton("submit");
            }else {
                clickOnButton("delete schedule");
            }
        verifySchedule(action,scheduleName);
        }catch (Exception e) {
            Assert.fail("unable to " + action + " the schedule");
        }
    }

    public void verifySchedule(String action,String name){
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        try{
            if(action.equalsIgnoreCase("Add")){
                baseUtil.enterText(searchBox,tournamentList.get(tournamentList.size()-1));
                clickOnButton("schedule");
                String actual = baseUtil.getElementText(addedScheduleOnPopup).trim().toLowerCase();
                String expected = name.trim().toLowerCase();
                Assert.assertEquals("Text mismatch after normalization",actual, expected);
//                verifyValidationMsg(getTestBase().getReadResources().
//                        getDataFromPropertyFile("Global","success_msg"));

            }else {
                baseUtil.enterText(searchBox,tournamentList.get(tournamentList.size()-1));
                clickOnButton("schedule");
                Assert.assertTrue("schedule still visible on the UI after delete.",
                        BaseUtil.elementShouldNotBeVisible(addedScheduleOnPopup));
//                verifyValidationMsg(getTestBase().getReadResources().
//                        getDataFromPropertyFile("Global","delete_msg"));
            }
        }catch (Exception e){
            logger.error("unable to perform action :: "+action+" on schedule.");
        }finally {
            baseUtil.click(formCloseBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }

    public void searchTournament(){
        try{
            baseUtil.click(searchBox);
            baseUtil.enterText(searchBox,tournamentList.get(tournamentList.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }catch (Exception e){
            logger.error("unable to search the tournament due to "+e.getMessage());
        }
    }

    public void verifyValidationMsg(String msg){
        try{
            Assert.assertEquals(msg,baseUtil.getElementText(validationMsgForSchedule));
        }catch (Exception e){
            logger.error("unable to verify validation message without the schedule name :: "+msg+
                    " due to "+e.getMessage());
        }
    }

    public void addPlayerToTheTeamForTournamentAndVerify(int count1,int count2){
        try{
            List<String> list=new LinkedList<>();
            for(String key:playersList.keySet()){
                list.add(key);
            }
            for (int i = 0; i <  count1; i++) {
                firstPlayerDropdownOnSelectPlayerPopup.click();
                firstPlayerDropdownOnSelectPlayerPopup.sendKeys(list.get(i));
                firstPlayerDropdownOnSelectPlayerPopup.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
            if(!BaseUtil.elementShouldNotBeVisible(firstDropdownOptions)) {
                WebElement body = TestBase.getWebDriver().findElement(By.tagName("body"));
                body.click();
            }
            for (int i = count2; i <list.size(); i++) {
                secondPlayerDropdownOnSelectPlayerPopup.click();
                secondPlayerDropdownOnSelectPlayerPopup.sendKeys(list.get(i));
                secondPlayerDropdownOnSelectPlayerPopup.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
            baseUtil.click(saveSelectionBtn);
            verifyPlayers();
        }catch (Exception e){
            logger.error("unable to add player due to "+e.getMessage());
        }finally {
            baseUtil.click(formCloseBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }
    public void verifyPlayers(){
        baseUtil.click(selectPlayerBtn);
        for (int i = 0; i < playerNames.size()-1; i++) {
            Assert.assertTrue(baseUtil.isElementDisplayed(baseUtil.getElementAfterLoaded(getTestBase().getReadResources().
                    getDataFromPropertyFile("Global","playersOnSelectPlayerPopupForTournament").
                    replaceAll("#value#",playerNames.get(i)))));
        }
    }
    public void enableOrDisableTheTournament(String action,String count){
        Map<String,String> map = new LinkedHashMap<>();
        for(String tournament:tournamentList){
            map.put(tournament,"active");
        }
        updateStatus(action,count,map);
    }

    public void verifyEnabledOrDisabledTournamentForAMatch(String action) throws IOException {
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());
        try{
            clickOnButton("matches module");
            clickOnButton("add");
            baseUtil.click(tournamentDropdownOnCreateMatchPopup);
            String xpath=getTestBase().getReadResources().
                    getDataFromPropertyFile("Global","tournamentOnCreateMatchPopup").
                    replaceAll("#value#",tournamentList.get(tournamentList.size()-1));

            if(action.equalsIgnoreCase("enabled")){
                Assert.assertFalse("Tournament not visible on the create match popup even it is enabled",
                        baseUtil.elementShouldNotBeVisible(TestBase.getWebDriver(),xpath));
            }else {
                Assert.assertTrue("Tournament not visible on the create match popup even it is enabled",
                        baseUtil.elementShouldNotBeVisible(TestBase.getWebDriver(),xpath));
            }
        }catch (Exception e){
            logger.error("unable to verify the tournament with type of "+action+" due to "+e.getMessage());
        }finally {
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            clickOnButton("tournaments module");
        }
    }

    public void makePlayerActiveOrInactive(String count,String action){
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());
        baseUtil.click(searchBox);
        List<String> list = new LinkedList<>(playersList.keySet());
        baseUtil.searchData(searchBox,list.get(0));
        updateStatus(action,count,playersList);
    }

    public void verifyPlayerOnSelectPlayerWindow(String status){
        logger.debug("Executing Test Step::" + new Object() {}.getClass().getEnclosingMethod().getName());
        String xpath="xpath===((//div[contains(@class,'select__menu')])[2])/child::div[contains(text(),'No options')]";
        try{
            List<String> list = new LinkedList<>(playersList.keySet());
            if(status.equalsIgnoreCase("inactive")) {
                Assert.assertTrue("Player still visible and selected for the team even it is disabled",
                        baseUtil.elementShouldNotBeVisible(TestBase.getWebDriver(),getTestBase().getReadResources().
                        getDataFromPropertyFile("Global", "playersOnSelectPlayerPopupForTournament").
                        replaceAll("#value#", list.get(0))));
                firstPlayerDropdownOnSelectPlayerPopup.click();
                baseUtil.enterText(firstPlayerDropdownOnSelectPlayerPopup,list.get(0));
                Assert.assertTrue("player :: "+list.get(0)+"still visible on the select player dropdown after inactive"
                        ,baseUtil.noOptionsAvailable(TestBase.getWebDriver(),xpath));
                if(BaseUtil.elementShouldNotBeVisible(firstDropdownOptions)) {
                    WebElement body = TestBase.getWebDriver().findElement(By.tagName("body"));
                    body.click();
                }
            }else {
                firstPlayerDropdownOnSelectPlayerPopup.click();
                Assert.assertFalse("Player still visible and selected for the team even it is disabled",
                        baseUtil.noOptionsAvailable(TestBase.getWebDriver(),xpath));
                if(!BaseUtil.elementShouldNotBeVisible(firstDropdownOptions)) {
                    firstPlayerDropdownOnSelectPlayerPopup.sendKeys(list.get(0));
                    firstPlayerDropdownOnSelectPlayerPopup.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                    baseUtil.click(saveSelectionBtn);
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                }
            }
        }catch (Exception e){
            logger.error("unable to verify player on select player window due to "+e.getMessage());
        }finally {
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }

    public void updateStatus(String module,String status) {
        try {
            logger.info("updating status for player as :: "+status);
            Map<String, String> map = new LinkedHashMap<>();
            for (String player : playerNames) {
                map.put(player, "active");
            }
            baseUtil.enterText(searchBox, playerNames.get(playerNames.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            updateStatus(status, "1", map);
        } catch (Exception e) {
            Assert.fail("unable to deactivate manager due to " + e.getMessage());
        }
    }

    public void verifyInactivePlayerInTournament(){
        try{
            baseUtil.click(addBtn);
            tournamentList.add(ModuleNameConstants.TOURNAMENT_NAME + getRandomString());
            baseUtil.enterText(tournamentNameTextbox, tournamentList.get(tournamentList.size()-1));
            baseUtil.enterText(tournamentNoOfMatchesTextbox, "1");
            String[] countries = {"India","Pakistan"};
            for (String country : countries) {
                tournamentCountryDropdown.click();
                tournamentCountryDropdown.sendKeys(country);
                tournamentCountryDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
            baseUtil.selectDropdown(tournamentVenueDropdown, venueList.get(venueList.size()-1));
            baseUtil.selectDropdown(tournamentGenderTypeDropdown, "Men");
            tournamentTeamsDropdown.click();
            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size()-1));
            WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));

            By dropdownOptions = By.xpath("//div[contains(@class,'menu-notice')]");
            // Wait until at least one option is visible
            wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownOptions));

            List<WebElement> options = TestBase.getWebDriver().findElements(dropdownOptions);

            List<String> optionTexts = options.stream()
                    .map(e -> e.getText().trim())
                    .toList();

            logger.info("Dropdown options: " + optionTexts);

            boolean isPresent = optionTexts.stream()
                    .anyMatch(text -> text.equalsIgnoreCase(menTeamList.get(menTeamList.size()-1)));

            Assert.assertFalse("Value '" + menTeamList.get(menTeamList.size()-1) + "' should NOT be present in dropdown!", isPresent);
        }catch (Exception e){
            Assert.fail("unable to verify the inactive player on the create tournament popup due to "+e.getMessage());
        }finally {
            tournamentCloseBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }
}
