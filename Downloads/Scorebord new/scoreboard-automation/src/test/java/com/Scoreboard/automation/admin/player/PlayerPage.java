package com.Scoreboard.automation.admin.player;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.context.TestContext;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import io.cucumber.datatable.DataTable;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.sql.Connection;
import java.util.*;

public class PlayerPage extends CommonMethods {
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), getTestBase().getWebDriverWait());
    static protected List<String> addedSkills = new ArrayList<>();
    private static final Logger logger = Logger.getLogger(PlayerPage.class);

    PlayerPage() {
        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "//input[@name='name']")
    WebElement playerNameTextbox;

    @FindBy(xpath = "//div[@class='venue-img-upload-bx']")
    WebElement playerImage;

    @FindBy(xpath = "//input[@name='date_of_birth']")
    WebElement playerDOB;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[1]")
    WebElement countryDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[2]")
    WebElement stateDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[3]")
    WebElement cityDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[4]")
    WebElement teamDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[5]")
    WebElement roleDropdown;

    @FindBy(xpath = "//form//input[@type='checkbox']")
    List<WebElement> skillCheckboxes;

    @FindBy(xpath = "//*[@name='gender']")
    WebElement genderDropdown;

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement addBtn;

    @FindBy(xpath = "//form//button[contains(text(),'Add')]")
    WebElement formAddBtn;

    @FindBy(xpath = "//form//button[contains(text(),'Update')]")
    WebElement formUpdateBtn;

    @FindBy(xpath="//*[contains(@placeholder,'Search')]")
    WebElement playerSearchBox;

    @FindBy(xpath = "//i[@title='View']")
    WebElement eyeBtn;

    @FindBy(xpath = "//i[@title='Edit']")
    WebElement editBtn;

    @FindBy(xpath = "//i[@title='Delete']")
    WebElement deleteBtn;

    @FindBy(xpath = "//button[contains(text(),'Delete')]")
    WebElement confirmPopupDeleteBtn;


    public void addPlayers(int playerCount,String type,int teamCount, Map<String,String> data) {
        try {
            Connection connection =baseUtil.getConn(TestBase.getInstance().getReadDatabaseUrls()
                            .get(TestBase.getEnv() + "_DBUrl"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBUsername"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBPassword"));
            List<Integer> teamIds = TestContext.getTeamIds();
            if(!TestContext.getTeamIds().isEmpty() && !type.equalsIgnoreCase("new")){

                for(Integer teamId : teamIds){
                    List<String> players = baseUtil.getPlayersForTeam(connection, teamId, playerCount);
                    for(String player: players){
                        playersList.put(player,"active");
                        playerNames.add(player);
                    }
                }
            }else {
                for (int i = 0; i < teamCount; i++) {
                    for (String name : generatePlayerNames(playerCount)) {
                        playersList.put(name, "active");
                        baseUtil.click(addBtn);
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.enterText(playerNameTextbox, name);
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        uploadLogo(data.get("Player Image"), "players");
                        playerDOB.sendKeys(data.get("Player DOB"));
                        baseUtil.selectDropdownValues(countryDropdown, data.get("Country"));
                        baseUtil.selectDropdown(stateDropdown, data.get("State"));
                        baseUtil.selectDropdown(cityDropdown, data.get("City"));
                        baseUtil.selectDropdown(genderDropdown, data.get("Gender"));
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
//                    teamDropdown.click();
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.enterText(teamDropdown, menTeamList.get(menTeamList.size() - 1));
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.selectDropdown(teamDropdown, menTeamList.get(menTeamList.size() - 1));
                        baseUtil.selectDropdown(roleDropdown, data.get("Role"));
                        selectSkills(Integer.parseInt(data.get("Skills")));
                        scrollToElement(formAddBtn);
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.click(formAddBtn);
                        baseUtil.explicitWait(PathConstants.WAIT_LOW);
                        verifyAddedPlayers(name, data);
                    }
                }
            }
        }catch (Exception e) {
            Assert.fail("unable to add players due to :: " + e.getMessage());
        }
    }

    public void editPlayers(int playerCount,int teamCount, Map<String,String> data) {
        try {
            Actions actions = new Actions(TestBase.getWebDriver());
            List<String> list = new ArrayList<>(playersList.keySet());
            playersList.remove(list.get(0));
            for (int i = 0; i < teamCount; i++) {
                for (String name : generatePlayerNames(playerCount)) {
                    playersList.put(name, "active");
                    baseUtil.click(editBtn);
                    actions.click(playerNameTextbox)
                            .keyDown(Keys.CONTROL)
                            .sendKeys("a")
                            .keyUp(Keys.CONTROL)
                            .sendKeys(Keys.BACK_SPACE)
                            .perform();
                    baseUtil.enterText(playerNameTextbox, name);
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                    uploadLogo(data.get("Player Image"), "players");
                    playerDOB.sendKeys(data.get("Player DOB"));
                    baseUtil.selectDropdownValues(countryDropdown, data.get("Country"));
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                    baseUtil.selectDropdown(stateDropdown, data.get("State"));
                    baseUtil.selectDropdown(cityDropdown, data.get("City"));
                    baseUtil.selectDropdown(genderDropdown, data.get("Gender"));
                    baseUtil.selectDropdown(teamDropdown, womenTeamList.get(womenTeamList.size()-1));
                    baseUtil.selectDropdown(roleDropdown, data.get("Role"));
                    selectSkills(Integer.parseInt(data.get("Skills")));
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                    scrollToElement(formUpdateBtn);
                    baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                    baseUtil.click(formUpdateBtn);
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                    verifyAddedPlayers(name, data);
                }
            }
        } catch (Exception e) {
            Assert.fail("unable to add players due to :: " + e.getMessage());
        }
    }

        public void selectSkills(Integer skillCount) {
        int i = 0;
        List<WebElement> skills = baseUtil.getMultipleElementsAfterLoaded(skillCheckboxes);
        for (WebElement skill : skills) {
            baseUtil.click(skill);
            addedSkills.add(skill.getText());
            i++;
            if (i == skillCount) {
                break;
            }
        }

    }
    public void verifyAddedPlayers(String playerName,Map<String,String> data){
        try{
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
            baseUtil.click(playerSearchBox);
            baseUtil.enterText(playerSearchBox,playerName);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(eyeBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            assertElement("viewDetailsPopupForModule", playerName);
            assertElement("viewDetailsPopupForModule", data.get("Player DOB"));
            assertElement("viewDetailsPopupForModule", data.get("Country"));
            assertElement("viewDetailsPopupForModule", data.get("State"));
            assertElement("viewDetailsPopupForModule", data.get("City").toLowerCase());
            assertElement("viewDetailsPopupForModule", data.get("Gender"));
            assertElement("viewDetailsPopupForModule", data.get("Role"));
            for (String skill: addedSkills){
                assertElement("viewDetailsPopupForModule", skill);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }

    public void searchAlreadyAddedPlayer(){
        try{
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            List<String> list = new LinkedList<>(playersList.keySet());
            getTestBase().getWebDriverWait().until(ExpectedConditions.visibilityOf(playerSearchBox));
            baseUtil.searchData(playerSearchBox,list.get(list.size()-1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }catch (Exception e){
            logger.error("unable to search the player");
        }
    }

    public void deletePlayer(){
        baseUtil.click(deleteBtn);
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        baseUtil.click(confirmPopupDeleteBtn);
        List<String> list = new ArrayList<>(playersList.keySet());
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        Assert.assertTrue("Player still visible after deletion of the team.",baseUtil.elementShouldNotBeVisible(TestBase.getWebDriver()
                ,getTestBase().getReadResources().getDataFromPropertyFile("Global","playerOnPlayerPage").
                replaceAll("#value#",list.get(0))));
        playersList.remove(list.get(0));
    }

}
