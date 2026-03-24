package com.scoreboard.util;

import com.scoreboard.constants.DBQueries;
import com.scoreboard.constants.ModuleNameConstants;
import com.scoreboard.constants.PathConstants;
import com.scoreboard.context.TestContext;
import io.restassured.response.Response;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.sql.Connection;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CommonMethods {

    private static final TestBase testBase = TestBase.getInstance();
    private static final Logger logger = Logger.getLogger(CommonMethods.class);
    DBUtils dbUtils = new DBUtils();
    private static Map<String, String> globalObjectRepo;
    private static Map<String, String> allObjectRepo;
    static protected String randomEndpointURL, copyOfEndpointURL, otherCityName;
    static protected List<String> menTeamList = new LinkedList<>();
    static protected List<String> womenTeamList = new LinkedList<>();
    static protected List<String> venueList = new LinkedList<>();
    static protected List<String> tournamentList = new LinkedList<>();
    static protected List<String> matchList = new LinkedList<>();
    static protected List<String> playerNames = new LinkedList<>();
    static protected List<String> managerList = new LinkedList<>();
    static protected List<String> agentList = new LinkedList<>();
    static protected List<String> dateForMatch = new LinkedList<>();
    public static List<String> agentIds = new LinkedList<>();
    static protected Map<String, String> dashboardData = new LinkedHashMap<>();
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), TestBase.getInstance().getWebDriverWait());
    static protected Map<String, String> playersList = new LinkedHashMap<>();
    static protected Map<String, String> teamsScoreList = new LinkedHashMap<>();
    static protected String matchEventId, scheduleName, managerId, agentId, managerName, agentName, mobileNo;
    static protected String twentyMinutesAHeadTimeForMatch = getCurrentTime(10);
    static protected int teamAScore, teamBScore, eventId;

    public CommonMethods() {
        allObjectRepo = new HashMap<>();
        try {
            globalObjectRepo = testBase.getReadResources()
                    .getValuesFromProperties("Global.properties");
            testBase.getReadResources().getAllPropertiesFiles().forEach(path -> {
                try {
                    allObjectRepo.putAll(testBase.getReadResources()
                            .getValuesFromProperties(path.getFileName().toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        PageFactory.initElements(TestBase.getWebDriver(), this);
    }

    @FindBy(xpath = "//button[@class='logout-btn']/../..")
    WebElement logoutBtn;

    @FindBy(xpath = "//*[contains(text(),'Event ID already exists')]")
    WebElement eventIdErrorMsg;

    @FindBy(xpath = "//*[contains(text(),'Event ID already exists')]/parent::div/following-sibling::button")
    WebElement eventIdErrorMsgCloseBtn;

    @FindBy(xpath = "//i[@title='Edit Match Result']")
    WebElement editMatchResultBtn;

    @FindBy(xpath = "//*[@id='cust_user_id']")
    WebElement usernameTextbox;

    @FindBy(xpath = "//input[@id='cust_user_id']//following-sibling::div")
    WebElement usernameErrorMsg;

    @FindBy(xpath = "//input[@id='passwd']//following-sibling::div")
    WebElement pwdErrorMsg;

    @FindBy(xpath = "//div[@role='alert']")
    WebElement invalidUserPwdError;

    @FindBy(xpath = "//*[contains(text(),'successfully')]")
    WebElement createSuccessMsg;

    @FindBy(xpath = "//*[contains(text(),'sign-in success')]")
    WebElement loginSuccessMsg;

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement addBtn;

    @FindBy(xpath = "//*[contains(text(),'Match created successfully')]")
    WebElement matchSuccessMsg;

    @FindBy(xpath = "//form/div/label[contains(text(),'Venue Name')]/following-sibling::input")
    WebElement venueNameTextBox;

    @FindBy(xpath = "//*[contains(@role,'alert')]")
    WebElement statusUpdateMsg;

    @FindBy(xpath = "//form/div/label[contains(text(),'Team Name')]/following-sibling::input")
    WebElement teamNameTextBox;

    @FindBy(id = "react-select-2-input")
    WebElement countryDropdown;

    @FindBy(xpath = "//*[@class='password-eye-icons']//span")
    WebElement eyeIcon;

    @FindBy(id = "react-select-3-input")
    WebElement venueStateDropdown;

    @FindBy(id = "react-select-4-input")
    WebElement venueCityDropdown;

    @FindBy(xpath = "//form//button[contains(text(),'Add')]")
    WebElement formAddBtn;

    @FindBy(xpath = "//form//button[contains(text(),'Update')]")
    WebElement formUpdateBtn;

    @FindBy(xpath = "//div[contains(text(),'Mobile number already exists.')]")
    WebElement exitingMobNoErrorMsg;

    @FindBy(xpath = "//button[@class='btn-close']")
    WebElement formCloseBtn;

    @FindBy(xpath = "//i[@title='Schedules']")
    WebElement scheduleBtn;

    @FindBy(xpath = "//button[contains(text(),'+ Add Schedule')]")
    WebElement addScheduleBtn;

    @FindBy(xpath = "//button[contains(text(),'Submit')]")
    WebElement submitBtn;

    @FindBy(xpath = "//button[contains(text(),'Clear')]")
    WebElement clearBtn;

    @FindBy(xpath = "//input[@placeholder='Search venue']")
    WebElement venueSearchBoxOnLiveMatchFilter;

    @FindBy(xpath = "//table//tbody//tr[2]")
    WebElement moduleTable;

    @FindBy(xpath = "//button[@title='Delete Schedule']")
    WebElement deleteScheduleBtn;

    @FindBy(xpath = "//i[@title='Select Players']")
    WebElement selectPlayerBtn;

    @FindBy(xpath = "//button[contains(text(),'Save Selection')]")
    WebElement saveSelectionBtn;

    @FindBy(xpath = "//i[@title='View']")
    WebElement eyeBtn;

    @FindBy(xpath = "//i[translate(@title,'ABCDEFGHIJKLMNOPQRSTUVWXYZ','abcdefghijklmnopqrstuvwxyz')='edit']")
    WebElement editBtn;

    @FindBy(xpath = "(//i[@title='password'])[1]")
    WebElement passwordUpdateBtn;

    @FindBy(xpath = "//input[@name='mobile_no']")
    WebElement agentMobileTextBox;

    @FindBy(xpath = "//button[contains(text(),'Save')]")
    WebElement saveBtn;

    @FindBy(xpath = "//div[@class='custom-dropdown']//div[@class='form-check']")
    List<WebElement> managersListOnMatchPage;

    @FindBy(xpath = "//table//tbody//tr//td[2]")
    List<WebElement> managersListOnManagerPage;

    @FindBy(xpath = "(//div[@class='dropdown-wrapper']//button)[1]")
    WebElement selectBtn;

    @FindBy(xpath = "//a[contains(text(),'View Detail')]")
    WebElement viewDetailBtnOnAgentUI;

    @FindBy(xpath = "//table//tbody//tr//td[5]")
    WebElement dateLocator;

    @FindBy(xpath = "//table//thead//tr/th")
    static List<WebElement> tableHeaders;

    @FindBy(xpath = "(//table)[1]//thead//tr/th")
    static List<WebElement> tableHeaders1;

    @FindBy(xpath = "(//table)[2]//thead//tr/th")
    static List<WebElement> tableHeaders2;

    @FindBy(xpath = "(//table)[3]//thead//tr/th")
    static List<WebElement> tableHeaders3;

    @FindBy(xpath = "//div[contains(text(),'Failed to create team')]")
    WebElement duplicateTeamErrorMsg;

    @FindBy(xpath = "//input[@placeholder='Enter new city name']/following::button[contains(text(),'Add')]")
    WebElement otherCityAddBtn;

    @FindBy(xpath = "//button[contains(text(),'Delete')]")
    WebElement deleteBtnOnDeletePopup;

    @FindBy(xpath = "//select[@name='gender']")
    WebElement teamGenderDropdown;

    @FindBy(css = "input.form-check-input")
    WebElement statusRadioBtn;

    @FindBy(xpath = "//span[contains(text(),'Players')]")
    WebElement players;

    @FindBy(xpath = "//span[contains(text(),'Agents')]")
    WebElement agents;

    @FindBy(xpath = "//span[contains(text(),'Matches')]")
    WebElement matches;

    @FindBy(xpath = "//span[contains(text(),'Live Matches')]")
    WebElement liveMatches;

    @FindBy(xpath = "//span[contains(text(),'Managers')]")
    WebElement manager;

    @FindBy(xpath = "(//input[@type='date'])[1]")
    WebElement dateFilterStartDate;

    @FindBy(xpath = "(//input[@type='date'])[2]")
    WebElement dateFilterEndDate;

    @FindBy(xpath = "//*[@class='filter-icon-btn']")
    WebElement filterBtn;

    @FindBy(xpath = "//input[@placeholder='Search event id']")
    WebElement eventTextboxOnFilterPopup;

    @FindBy(xpath = "//*[contains(@placeholder,'Search')]")
    WebElement searchBox;

    @FindBy(xpath = "//div[@class='dropdown-wrapper']/child::button")
    WebElement managerAssignDropdown;

    @FindBy(xpath = "//*[contains(text(),'Cannot unassign after toss is confirmed.')]")
    WebElement errorMsgForUnassignManager;

    @FindBy(xpath = "(//table//tbody//tr)[1]")
    WebElement latestAddedData;

    @FindBy(xpath = "//i[@title='Delete']")
    WebElement deleteBtn;

    @FindBy(xpath = "(//button[contains(text(),'Yes')])[1]")
    WebElement yesBtn;

    @FindBy(xpath = "//input[@name='name']")
    WebElement tournamentNameTextbox;

    @FindBy(xpath = "//label[contains(text(),'Match Name: ')]/following-sibling::input")
    WebElement matchNameTextbox;

    @FindBy(xpath = "//label[contains(text(),'Event ID: ')]/following-sibling::input")
    WebElement matchEventIdTextbox;

    @FindBy(xpath = "//label[contains(text(),'Tournament: ')]/following-sibling::select")
    WebElement matchTournamentDropdown;

    @FindBy(xpath = "//label[contains(text(),'Venue: ')]/following-sibling::select")
    WebElement matchVenueDropdown;

    @FindBy(xpath = "//label[contains(text(),'Schedule: ')]/following-sibling::select")
    WebElement matchScheduleDropdown;

    @FindBy(xpath = "//label[contains(text(),'Team A: ')]/following-sibling::select")
    WebElement matchTeamADropdown;

    @FindBy(xpath = "//label[contains(text(),'Team B: ')]/following-sibling::select")
    WebElement matchTeamBDropdown;

    @FindBy(xpath = "//label[contains(text(),'Match Date: ')]/following-sibling::input")
    WebElement matchDate;

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

    @FindBy(xpath = "(//div[@class='invalid-feedback'])[last()]")
    WebElement invalidDateErrorMsg;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[1]")
    WebElement tournamentCountryDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[2]")
    WebElement tournamentVenueDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[3]")
    WebElement tournamentTeamsDropdown;

    @FindBy(xpath = "(//input[contains(@id,'react-select') and contains(@id,'-input')])[4]")
    WebElement tournamentFormatDropdown;

    @FindBy(xpath = "//*[contains(text(),'Resource Added!')]")
    WebElement successfulResourceAdded;

    @FindBy(xpath = "//h5[contains(text(),'Total Matches')]/../../child::h2")
    WebElement totalNumberOfMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Managers')]/../../child::h2")
    WebElement totalManagerDashboard;

    @FindBy(xpath = "//*[contains(@class,'filter-')]")
    WebElement matchHistoryFilterBtn;

    @FindBy(xpath = "//h5[contains(text(),'Total Matches')]/../../child::h2")
    WebElement totalMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Live Matches')]/../../child::h2")
    WebElement liveMatchesDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Agents')]/../../child::h2")
    WebElement totalAgentDashboard;

    @FindBy(xpath = "//h5[contains(text(),'Total Teams')]/../../child::h2")
    WebElement totalTeamsDashboard;

    @FindBy(xpath = "//h2[contains(text(),'Matches')]")
    WebElement matchesHeadingOnMatchPage;

    @FindBy(xpath = "//h2[contains(text(),'Managers')]")
    WebElement managerHeadingOnManagerPage;

    @FindBy(xpath = "//h2[contains(text(),'Live Matches')]")
    WebElement liveMatchesHeadingOnLiveMatchPage;

    @FindBy(xpath = "//h2[contains(text(),'Agents')]")
    WebElement agentHeadingOnAgentPage;

    @FindBy(xpath = "//span[contains(text(),'Tournaments')]")
    WebElement tournament;


    @FindBy(xpath = "//h2[contains(text(),'Teams')]")
    WebElement teamsHeadingOnTeamsPage;

    @FindBy(xpath = "//input[@placeholder='Enter new city name']")
    WebElement otherCityTextbox;

    @FindBy(xpath = "//button[contains(text(),'Others...')]")
    WebElement otherCityBtn;

    @FindBy(xpath = "//*[contains(text(),'Your account has been deactivated.')]")
    WebElement accountDeactivationMsg;

    @FindBy(xpath = "//*[contains(text(),'match status updated successfully')]")
    WebElement successMsg;

    public enum SelectType {
        VISIBLE_TEXT,
        VALUE,
        INDEX
    }

    public static void scrollToElement(WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
        js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", element);
        logger.info("Scrolled to element: " + element.toString());
    }

    public void doInitialization(boolean flag, String subEnv) throws IOException {
        if (flag) {
            storeInitialDashboardData(subEnv);
            generateRandomEndpointURL(globalObjectRepo.get("endpoint_url"));
        }
    }

    public void storeInitialDashboardData(String subEnv) throws IOException {
        clickOnButton("dashboard module");
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        dashboardData.put("Total Matches", baseUtil.getElementText(totalNumberOfMatchesDashboard));
        if (subEnv.equalsIgnoreCase("Manager")) {
            dashboardData.put("Total Matches", baseUtil.getElementText(totalMatchesDashboard));
            dashboardData.put("Live Matches", baseUtil.getElementText(liveMatchesDashboard));
            dashboardData.put("Total Agents", baseUtil.getElementText(totalAgentDashboard));
            dashboardData.put("Total Teams", baseUtil.getElementText(totalTeamsDashboard));
        } else if (subEnv.equalsIgnoreCase("Agent")) {
            dashboardData.put("Total Matches", baseUtil.getElementText(totalMatchesDashboard));
            dashboardData.put("Live Matches", baseUtil.getElementText(liveMatchesDashboard));
        } else {
            dashboardData.put("Total Matches", baseUtil.getElementText(totalMatchesDashboard));
            dashboardData.put("Live Matches", baseUtil.getElementText(liveMatchesDashboard));
            dashboardData.put("Live Managers", baseUtil.getElementText(totalManagerDashboard));
            dashboardData.put("Total Agents", baseUtil.getElementText(totalAgentDashboard));
            dashboardData.put("Total Teams", baseUtil.getElementText(totalTeamsDashboard));
        }
    }

    public void generateRandomEndpointURL(String URL) {
        randomEndpointURL = URL + "/" + getRandomString().replace("_", "");
        copyOfEndpointURL = randomEndpointURL;
    }


    public WebElement getSearchedElement(String name) {
        logger.info(logger.getName() + " generating random string for league name");
        String xpath = "//td[contains(text(),'" + name + "')]";
        return TestBase.getWebDriver().findElement(By.xpath(xpath));
    }

    public boolean isButtonActive(WebElement buttonElement) {
        String titleAttribute = buttonElement.getAttribute("title");
        return "Mark Inactive".equals(titleAttribute);
    }

    public static String getRandomString() {
        return "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
    }

    public static int generateRandomNumber() {
        int digits = ThreadLocalRandom.current().nextInt(5, 9);

        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;

        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    public static String generateUnique10DigitNumber() {
        long timestampPart = System.currentTimeMillis() % 1000000;
        int randomPart = ThreadLocalRandom.current().nextInt(1000, 9999);
        return String.format("%06d%04d", timestampPart, randomPart);
    }

    public static void verifyTableHeaders(List<String> expectedHeaders) {
        scrollToElement(tableHeaders.get(0));
        List<String> actualHeaders = tableHeaders.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .toList();

        logger.info("Expected Headers: " + expectedHeaders);
        logger.info("Actual Headers: " + actualHeaders);

        if (expectedHeaders.size() != actualHeaders.size()) {
            logger.error("Header count mismatch! Expected " + expectedHeaders.size() + ", but found " + actualHeaders.size());
            Assert.fail("Header count mismatch!");
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {
            String expected = expectedHeaders.get(i);
            String actual = actualHeaders.get(i);
            if (!expected.equals(actual)) {
                logger.error("Header mismatch at column " + (i + 1) + ": expected '" + expected + "' but found '" + actual + "'");
                Assert.fail("Header mismatch at column " + (i + 1));
            } else {
                logger.info("Header matched at column " + (i + 1) + ": '" + expected + "'");
            }
        }

        logger.info("All table headers matched successfully.");
    }

    public static void verifyTableHeadersForDashboard(List<String> expectedHeaders, String tableNo) {
        List<String> actualHeaders = new LinkedList<>();
        if (tableNo.equalsIgnoreCase("1")) {
            scrollToElement(tableHeaders1.get(0));
            actualHeaders = tableHeaders1.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .toList();
        } else if (tableNo.equalsIgnoreCase("2")) {
            scrollToElement(tableHeaders2.get(0));
            actualHeaders = tableHeaders1.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .toList();
        } else if (tableNo.equalsIgnoreCase("3")) {
            scrollToElement(tableHeaders3.get(0));
            actualHeaders = tableHeaders1.stream()
                    .map(WebElement::getText)
                    .map(String::trim)
                    .toList();
        }

        logger.info("Expected Headers: " + expectedHeaders);
        logger.info("Actual Headers: " + actualHeaders);

        if (expectedHeaders.size() != actualHeaders.size()) {
            logger.error("Header count mismatch! Expected " + expectedHeaders.size() + ", but found " + actualHeaders.size());
            Assert.fail("Header count mismatch!");
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {
            String expected = expectedHeaders.get(i);
            String actual = actualHeaders.get(i);
            if (!expected.equals(actual)) {
                logger.error("Header mismatch at column " + (i + 1) + ": expected '" + expected + "' but found '" + actual + "'");
                Assert.fail("Header mismatch at column " + (i + 1));
            } else {
                logger.info("Header matched at column " + (i + 1) + ": '" + expected + "'");
            }
        }

        logger.info("All table headers matched successfully.");
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


    public static void selectFromDropdown(WebElement dropdownElement, String input, SelectType type) {
        try {
            Select select = new Select(dropdownElement);

            switch (type) {
                case VISIBLE_TEXT:
                    select.selectByVisibleText(input);
                    logger.info("Selected by visible text: " + input);
                    break;

                case VALUE:
                    select.selectByValue(input);
                    logger.info("Selected by value: " + input);
                    break;

                case INDEX:
                    int index = Integer.parseInt(input);
                    select.selectByIndex(index);
                    logger.info("Selected by index: " + index);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid selection type provided.");
            }

        } catch (Exception e) {
            System.err.println("Dropdown selection failed: " + e.getMessage());
            throw e;
        }
    }

    public static TestBase getTestBase() {
        return testBase;
    }

    public void clickOnButton(String page) throws IOException {
        WebElement element = null;
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        switch (page.trim().toLowerCase()) {
            case "venue module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Venue"));
                baseUtil.click(element);
                break;
            case "dashboard module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Dashboard"));
                baseUtil.click(element);
                break;
            case "matches module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Matches"));
                baseUtil.click(element);
                break;
            case "live matches module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Live Matches"));
                baseUtil.click(element);
                break;
            case "teams module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Teams"));
                baseUtil.click(element);
                break;
            case "players module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Players"));
                baseUtil.click(element);
                break;
            case "tournaments module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Tournaments"));
                baseUtil.click(element);
                break;
            case "managers module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Managers"));
                baseUtil.click(element);
                break;
            case "agents module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "Agents"));
                baseUtil.click(element);
                break;
            case "match history module":
                element = baseUtil.getElementAfterLoaded(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "sideBarMenu").
                        replaceAll("#menu#", "matchHistory"));
                baseUtil.click(element);
                break;
            case "edit":
                baseUtil.click(editBtn);
                break;
            case "password update":
                baseUtil.click(passwordUpdateBtn);
                break;
            case "total matches":
                baseUtil.click(totalNumberOfMatchesDashboard);
                break;
            case "total managers":
                baseUtil.click(totalManagerDashboard);
                break;
            case "filter":
                baseUtil.click(matchHistoryFilterBtn);
                break;
            case "total agents":
                baseUtil.click(totalAgentDashboard);
                break;
            case "live matches":
                baseUtil.click(liveMatchesDashboard);
                break;
            case "total teams":
                baseUtil.click(totalTeamsDashboard);
                break;
            case "yes btn":
                baseUtil.click(yesBtn);
                break;
            case "view detail":
                baseUtil.click(viewDetailBtnOnAgentUI);
                break;
            case "edit match result":
                baseUtil.click(editMatchResultBtn);
                break;
            case "add":
                baseUtil.click(addBtn);
                break;
            case "add venue":
                baseUtil.click(formAddBtn);
                break;
            case "schedule":
                baseUtil.click(scheduleBtn);
                break;
            case "add schedule":
                baseUtil.click(addScheduleBtn);
                break;
            case "submit":
                baseUtil.click(submitBtn);
                break;
            case "delete schedule":
                baseUtil.click(deleteScheduleBtn);
                break;
            case "select player":
                baseUtil.click(selectPlayerBtn);
                break;
            case "save selection":
                baseUtil.click(saveSelectionBtn);
                break;
            case "save":
                baseUtil.click(saveBtn);
                break;
            case "edit venue":
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                getTestBase().getWebDriverWait().until(ExpectedConditions.
                        visibilityOfElementLocated(By.xpath(getTestBase().getReadResources().getDataFromPropertyFile("Global", "venueEditBtn").
                                replaceAll("#value#", venueList.get(venueList.size() - 1)).split("===")[1])));
                baseUtil.click(TestBase.getWebDriver().findElement(By.xpath(
                        (getTestBase().getReadResources().getDataFromPropertyFile("Global", "venueEditBtn").
                                replaceAll("#value#", venueList.get(venueList.size() - 1)).split("===")[1])
                )));
                break;
            default:
                System.out.println("please provide a valid name :: " + page);
        }
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
    }

//    public void verifyErrorMessage(String msg,String page){
//        logger.info(logger.getName() + " verifying the error message " + msg + " for duplicating name of "+page+" page");
//        try {
//            switch (page.toLowerCase()) {
//                case "teams":
//                    if(baseUtil.isElementDisplayed(errorMsgForDuplicateTeam)) {
//                        Assert.assertEquals(msg, baseUtil.getElementText(errorMsgForDuplicateTeam));
//                    }else Assert.fail("error '"+msg+"' message for operator is not visible on the UI");
//                    break;
//                case "league":
//                    if(baseUtil.isElementDisplayed(errorMsgForDuplicateLeague)) {
//                        Assert.assertEquals(msg, baseUtil.getElementText(errorMsgForDuplicateLeague));
//                    }else Assert.fail("error '"+msg+"' message for operator is not visible on the UI");
//                    break;
//                case "category":
//                    if(baseUtil.isElementDisplayed(errorMsgForDuplicateCategory)) {
//                        Assert.assertEquals(msg, baseUtil.getElementText(errorMsgForDuplicateCategory));
//                    }else Assert.fail("error '"+msg+"' message for operator is not visible on the UI");
//                    break;
//                case "player":
//                    if(baseUtil.isElementDisplayed(errorMsgForDuplicatePlayer)) {
//                        Assert.assertEquals(msg, baseUtil.getElementText(errorMsgForDuplicatePlayer));
//                    }else Assert.fail("error '"+msg+"' message for operator is not visible on the UI");
//                    break;
//                case "operator":
//                    String msgForDuplicateOpNameAndEndpoint= baseUtil.getElementText(errorMsgForDuplicateOperator)+" && "+
//                            baseUtil.getElementText(errorMsgForDuplicateOperatorURL);
//                    if(baseUtil.isElementDisplayed(errorMsgForDuplicateOperator)
//                            && baseUtil.isElementDisplayed(errorMsgForDuplicateOperatorURL)) {
//                        Assert.assertEquals(msg, msgForDuplicateOpNameAndEndpoint);
//                    }else Assert.fail("error '"+msg+"' message for operator is not visible on the UI");
//                    break;
//                default:
//                    logger.error("page not found :: "+page);
//            }
//        }finally {
//            if(cancelBtn.isDisplayed()){
//                baseUtil.click(cancelBtn);
//            }
//        }
//
//    }

    public void verifyResourceSuccessfulMessage(String resourceCategory) {
        try {
            baseUtil.waitForElementToBePresent(successfulResourceAdded);
            if (baseUtil.isElementDisplayed(successfulResourceAdded)) {
                logger.info("Resource added successfully...");
            } else {
                logger.error("While adding the resource getting error, unable to add resource :: " + resourceCategory);
            }
        } catch (Exception e) {
            logger.error("unable to add resource due to :: " + e);
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.waitForSpinner();
        }
    }

    public void uploadLogo(String fileName, String module) {
        logger.debug("Executing Test Step::" + new Object() {
        }.getClass().getEnclosingMethod().getName());
        try {
            String filePath = System.getProperty("user.dir") +
                    "/src/main/resources/" + fileName;

            WebElement upload = TestBase.getWebDriver().findElement(By.xpath("//input[@type='file']"));

            upload.sendKeys(filePath);

        } catch (Exception e) {
            logger.error("Failed to upload logo for :: " + module + " due to " + e);
        }
    }


    public void addOrEditAsset(String type, String module, int moduleCount, Map<String, String> data) {
        boolean isDataCreatedByUI = false;
        try {
            Actions actions = new Actions(TestBase.getWebDriver());
//            Boolean messageAppeared;
            WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(5));
            Connection connection = baseUtil.getConn(TestBase.getInstance().getReadDatabaseUrls()
                            .get(TestBase.getEnv() + "_DBUrl"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBUsername"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBPassword"));
            for (int i = 0; i < moduleCount; i++) {
                if (type.equalsIgnoreCase("add")) {
                    switch (module.toLowerCase()) {
                        case "venue":
                            isDataCreatedByUI = true;
                            venueList.add(ModuleNameConstants.VENUE_NAME + getRandomString());
                            baseUtil.click(addBtn);
                            baseUtil.enterText(venueNameTextBox, venueList.get(venueList.size() - 1));
                            baseUtil.selectDropdownValues(countryDropdown, data.get("Country"));
                            baseUtil.selectDropdownValues(venueStateDropdown, data.get("State"));
                            baseUtil.selectDropdownValues(venueCityDropdown, data.get("City"));
                            uploadLogo("SMSStadium.jpg", "venue");
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.click(formAddBtn);

//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to create " + module, messageAppeared);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            break;

                        case "teams":
                            List<Integer> teamIds = baseUtil.getValidTeams(connection, moduleCount, 11);

                            if (teamIds.size() < moduleCount) {
                                isDataCreatedByUI = true;
                                TestBase.getWebDriver().navigate().refresh();
                                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                                baseUtil.click(addBtn);
                                Select select = new Select(teamGenderDropdown);

                                if (data.get("Gender").equalsIgnoreCase("men")) {
                                    menTeamList.add(ModuleNameConstants.MEN_TEAM_NAME + getRandomString());
                                    baseUtil.enterText(teamNameTextBox, menTeamList.get(menTeamList.size() - 1));
                                    select.selectByVisibleText(data.get("Gender"));
                                } else if (data.get("Gender").equalsIgnoreCase("women")) {
                                    womenTeamList.add(ModuleNameConstants.WOMEN_TEAM_NAME + getRandomString());
                                    baseUtil.enterText(teamNameTextBox, womenTeamList.get(womenTeamList.size() - 1));
                                    select.selectByVisibleText(data.get("Gender"));
                                } else {
                                    logger.error("Invalid team gender selected...");
                                }
                                uploadLogo("team.png", module);
                                baseUtil.click(formAddBtn);
//                                messageAppeared = wait.until(driver -> {
//                                    String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                    return bodyText.contains("successfully") || bodyText.contains("success");
//                                });
//
//                                Assert.assertTrue("Failed to create " + module, messageAppeared);
                                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            } else {
                                TestContext.setTeamIds(teamIds);
                                for (int teamID : TestContext.getTeamIds()) {
                                    menTeamList.add(baseUtil.getTeamNameById(connection, teamID));
                                }

                            }
                            break;
                        case "tournament":
                            isDataCreatedByUI = true;
                            baseUtil.click(addBtn);
                            tournamentList.add(ModuleNameConstants.TOURNAMENT_NAME + getRandomString());
                            baseUtil.enterText(tournamentNameTextbox, tournamentList.get(tournamentList.size() - 1));
                            baseUtil.enterText(tournamentNoOfMatchesTextbox, data.get("No. of Matches"));
                            String[] countries = data.get("Country").split("&");
                            for (String country : countries) {
                                tournamentCountryDropdown.click();
                                tournamentCountryDropdown.sendKeys(country);
                                tournamentCountryDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            }
                            baseUtil.selectDropdown(tournamentVenueDropdown, venueList.get(venueList.size() - 1));
                            baseUtil.selectDropdown(tournamentGenderTypeDropdown, data.get("Gender Type"));
                            tournamentTeamsDropdown.click();
                            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size() - 1));
                            tournamentTeamsDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                            tournamentTeamsDropdown.click();
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size() - 2));
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
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to create " + module, messageAppeared);
                            break;
                        case "match":
                            isDataCreatedByUI = true;
                            baseUtil.click(addBtn);
                            matchList.add(ModuleNameConstants.MATCH_NAME + getRandomString());
                            baseUtil.enterText(matchNameTextbox, matchList.get(matchList.size() - 1));
                            matchEventId = String.valueOf(generateRandomNumber());
                            baseUtil.enterText(matchEventIdTextbox, matchEventId);
                            Select matchTournament = new Select(matchTournamentDropdown);
                            matchTournament.selectByVisibleText(tournamentList.get(tournamentList.size() - 1));
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            Select matchVenue = new Select(matchVenueDropdown);
                            matchVenue.selectByVisibleText(venueList.get(venueList.size() - 1));
                            Select matchSchedule = new Select(matchScheduleDropdown);
                            matchSchedule.selectByVisibleText(scheduleName);
                            Select teamA = new Select(matchTeamADropdown);
                            Select teamB = new Select(matchTeamBDropdown);
                            teamA.selectByVisibleText(menTeamList.get(menTeamList.size() - 2));
                            teamB.selectByVisibleText(menTeamList.get(menTeamList.size() - 1));
                            dateForMatch.add(getCurrentDatePlusDays(Integer.parseInt(data.get("Match Date"))));
                            matchDate.sendKeys(dateForMatch.get(dateForMatch.size() - 1));
                            WebElement timeInput = TestBase.getWebDriver().findElement(By.xpath("//input[@type='time']"));
                            timeInput.clear();
                            if (!data.get("Start Time").equalsIgnoreCase("Current time")) {
                                timeInput.sendKeys(data.get("Start Time"));
                            } else {
                                timeInput.sendKeys(twentyMinutesAHeadTimeForMatch);
                            }
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.click(formAddBtn);
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to create " + module, messageAppeared);
                            break;
                        default:
                            logger.error("Unable to add the module :: " + module);
                    }

                } else if (type.equalsIgnoreCase("edit")) {
                    switch (module.toLowerCase()) {
                        case "venue":
                            isDataCreatedByUI = true;
//                            baseUtil.enterText(searchBox, venueList.get(venueList.size() - 1));
                            venueList.add(ModuleNameConstants.VENUE_NAME + getRandomString());
//                            testBase.getWebDriver().navigate().refresh();
//                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            actions.click(venueNameTextBox)
                                    .keyDown(Keys.CONTROL)
                                    .sendKeys("a")
                                    .keyUp(Keys.CONTROL)
                                    .sendKeys(Keys.BACK_SPACE)
                                    .perform();
                            baseUtil.enterText(venueNameTextBox, venueList.get(venueList.size() - 1));
                            baseUtil.selectDropdown(countryDropdown, data.get("Country"));
                            baseUtil.selectDropdown(venueStateDropdown, data.get("State"));
                            otherCityName = data.get("City") + getRandomString();
                            baseUtil.click(otherCityBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            baseUtil.enterText(otherCityTextbox, otherCityName);
                            baseUtil.click(otherCityAddBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            uploadLogo("SMSStadium.jpg", "venue");
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.click(formUpdateBtn);
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to update " + module, messageAppeared);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            break;

                        case "teams":
                            isDataCreatedByUI = true;
                            TestBase.getWebDriver().navigate().refresh();
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.click(searchBox);
                            if (data.get("Gender").equalsIgnoreCase("men")) {
                                baseUtil.enterText(searchBox, menTeamList.get(menTeamList.size() - 1));
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                menTeamList.add(ModuleNameConstants.MEN_TEAM_NAME + getRandomString());
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.click(editBtn);
                                actions.click(teamNameTextBox)
                                        .keyDown(Keys.CONTROL)
                                        .sendKeys("a")
                                        .keyUp(Keys.CONTROL)
                                        .sendKeys(Keys.BACK_SPACE)
                                        .perform();
                                teamNameTextBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                Select select = new Select(teamGenderDropdown);
                                baseUtil.enterText(teamNameTextBox, menTeamList.get(menTeamList.size() - 2));
                                select.selectByVisibleText(data.get("Gender"));
                            } else if (data.get("Gender").equalsIgnoreCase("women")) {
                                baseUtil.enterText(searchBox, womenTeamList.get(womenTeamList.size() - 1));
                                womenTeamList.add(ModuleNameConstants.WOMEN_TEAM_NAME + getRandomString());
                                baseUtil.click(editBtn);
                                actions.click(teamNameTextBox)
                                        .keyDown(Keys.CONTROL)
                                        .sendKeys("a")
                                        .keyUp(Keys.CONTROL)
                                        .sendKeys(Keys.BACK_SPACE)
                                        .perform();
                                Select select = new Select(teamGenderDropdown);
                                teamNameTextBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                                baseUtil.enterText(teamNameTextBox, womenTeamList.get(womenTeamList.size() - 2));
                                select.selectByVisibleText(data.get("Gender"));
                            } else {
                                logger.error("Invalid team gender selected...");
                            }
                            uploadLogo("team.png", module);
                            baseUtil.click(formUpdateBtn);
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to update " + module, messageAppeared);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            break;
                        case "tournament":
                            isDataCreatedByUI = true;
                            baseUtil.enterText(searchBox, tournamentList.get(tournamentList.size() - 1));
                            baseUtil.click(editBtn);
                            actions.click(tournamentNameTextbox)
                                    .keyDown(Keys.CONTROL)
                                    .sendKeys("a")
                                    .keyUp(Keys.CONTROL)
                                    .sendKeys(Keys.BACK_SPACE)
                                    .perform();
                            tournamentList.add(ModuleNameConstants.TOURNAMENT_NAME + getRandomString());
                            baseUtil.enterText(tournamentNameTextbox, tournamentList.get(tournamentList.size()-1));
                            baseUtil.enterText(tournamentNoOfMatchesTextbox, data.get("No. of Matches"));
                            String[] countries = data.get("Country").split("&");
                            for (String country : countries) {
                                tournamentCountryDropdown.click();
                                tournamentCountryDropdown.sendKeys(country);
                                tournamentCountryDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            }
                            baseUtil.selectDropdown(tournamentVenueDropdown, venueList.get(venueList.size() - 1));
                            baseUtil.selectDropdown(tournamentGenderTypeDropdown, data.get("Gender Type"));
                            tournamentTeamsDropdown.click();
                            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size() - 1));
                            tournamentTeamsDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                            tournamentTeamsDropdown.click();
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            tournamentTeamsDropdown.sendKeys(menTeamList.get(menTeamList.size() - 2));
                            tournamentTeamsDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            baseUtil.selectDropdown(tournamentFormatDropdown, data.get("Format Type"));
                            baseUtil.selectDropdown(tournamentFormatDropdown, data.get("Format Type"));
                            baseUtil.selectDropdown(tournamentTypeDropdown, data.get("Tournament Type"));
                            tournamentStartDate.sendKeys(getCurrentDatePlusDays(1));
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            tournamentEndDate.sendKeys(getCurrentDatePlusDays(5));
                            baseUtil.click(formUpdateBtn);
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to update " + module, messageAppeared);
                            break;
                        case "match":
                            isDataCreatedByUI = true;
                            baseUtil.enterText(searchBox, matchList.get(matchList.size() - 1));
                            baseUtil.click(editBtn);
                            actions.click(matchNameTextbox)
                                    .keyDown(Keys.CONTROL)
                                    .sendKeys("a")
                                    .keyUp(Keys.CONTROL)
                                    .sendKeys(Keys.BACK_SPACE)
                                    .perform();
                            matchList.add(ModuleNameConstants.MATCH_NAME + getRandomString());
                            baseUtil.enterText(matchNameTextbox, matchList.get(matchList.size() - 1));
                            matchEventId = String.valueOf(generateRandomNumber());
                            baseUtil.enterText(matchEventIdTextbox, matchEventId);
                            matchDate.sendKeys(getCurrentDatePlusDays(1));
                            WebElement timeInput = TestBase.getWebDriver().findElement(By.xpath("//input[@type='time']"));
                            JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
                            js.executeScript(
                                    "let input = arguments[0];" +
                                            "let valueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;" +
                                            "valueSetter.call(input, '" + data.get("Start Time") + "');" +
                                            "input.dispatchEvent(new Event('input', { bubbles: true }));",
                                    timeInput
                            );

                            baseUtil.click(formUpdateBtn);
//                            messageAppeared = wait.until(driver -> {
//                                String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                                return bodyText.contains("successfully") || bodyText.contains("success");
//                            });
//
//                            Assert.assertTrue("Failed to update " + module, messageAppeared);
                            break;
                        default:
                            logger.error("Unable to edit the module :: " + module);
                    }
                }
                if (isDataCreatedByUI) {
                    verifyModule(type, module, data, 1);
                }
            }
        } catch (Exception e) {
            Assert.fail("enable to " + type + " " + module + " due to " + e.getMessage());
            logger.error("Error in addAsset: " + e.getMessage());
        }
    }


    public void addOrEditTeam(String type, String module, int moduleCount, Map<String, String> data) {
        try {
            WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(5));
            Actions actions = new Actions(TestBase.getWebDriver());
            for (int i = 0; i < moduleCount; i++) {
                if (type.equalsIgnoreCase("add")) {
                    TestBase.getWebDriver().navigate().refresh();
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                    baseUtil.click(addBtn);
                    Select select = new Select(teamGenderDropdown);

                    if (data.get("Gender").equalsIgnoreCase("men")) {
                        menTeamList.add(ModuleNameConstants.MEN_TEAM_NAME + getRandomString());
                        baseUtil.enterText(teamNameTextBox, menTeamList.get(menTeamList.size() - 1));
                        select.selectByVisibleText(data.get("Gender"));
                    } else if (data.get("Gender").equalsIgnoreCase("women")) {
                        womenTeamList.add(ModuleNameConstants.WOMEN_TEAM_NAME + getRandomString());
                        baseUtil.enterText(teamNameTextBox, womenTeamList.get(womenTeamList.size() - 1));
                        select.selectByVisibleText(data.get("Gender"));
                    } else {
                        logger.error("Invalid team gender selected...");
                    }
                    uploadLogo("team.png", module);
                    baseUtil.click(formAddBtn);
//                    Boolean messageAppeared = wait.until(driver -> {
//                        String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                        return bodyText.contains("successfully") || bodyText.contains("success");
//                    });
//
//                    Assert.assertTrue("Failed to create " + module, messageAppeared);
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);


                } else if (type.equalsIgnoreCase("edit")) {
                    TestBase.getWebDriver().navigate().refresh();
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                    baseUtil.click(searchBox);
                    if (data.get("Gender").equalsIgnoreCase("men")) {
                        baseUtil.enterText(searchBox, menTeamList.get(menTeamList.size() - 1));
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        menTeamList.add(ModuleNameConstants.MEN_TEAM_NAME + getRandomString());
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.click(editBtn);
                        actions.click(teamNameTextBox)
                                .keyDown(Keys.CONTROL)
                                .sendKeys("a")
                                .keyUp(Keys.CONTROL)
                                .sendKeys(Keys.BACK_SPACE)
                                .perform();
                        teamNameTextBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        Select select = new Select(teamGenderDropdown);
                        baseUtil.enterText(teamNameTextBox, menTeamList.get(menTeamList.size() - 2));
                        select.selectByVisibleText(data.get("Gender"));
                    } else if (data.get("Gender").equalsIgnoreCase("women")) {
                        baseUtil.enterText(searchBox, womenTeamList.get(womenTeamList.size() - 1));
                        womenTeamList.add(ModuleNameConstants.WOMEN_TEAM_NAME + getRandomString());
                        baseUtil.click(editBtn);
                        actions.click(teamNameTextBox)
                                .keyDown(Keys.CONTROL)
                                .sendKeys("a")
                                .keyUp(Keys.CONTROL)
                                .sendKeys(Keys.BACK_SPACE)
                                .perform();
                        Select select = new Select(teamGenderDropdown);
                        teamNameTextBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
                        baseUtil.enterText(teamNameTextBox, womenTeamList.get(womenTeamList.size() - 2));
                        select.selectByVisibleText(data.get("Gender"));
                    } else {
                        logger.error("Invalid team gender selected...");
                    }
                    uploadLogo("team.png", module);
                    baseUtil.click(formUpdateBtn);
//                    Boolean messageAppeared = wait.until(driver -> {
//                        String bodyText = driver.findElement(By.tagName("body")).getText().toLowerCase();
//                        return bodyText.contains("successfully") || bodyText.contains("success");
//                    });
//
//                    Assert.assertTrue("Failed to update " + module, messageAppeared);
                    baseUtil.explicitWait(PathConstants.WAIT_LOW);
                    verifyModule(type, module, data, 1);
                }
            }
        } catch (Exception e) {
            Assert.fail("enable to " + type + " " + module + " due to " + e.getMessage());
            logger.error("Error in addAsset: " + e.getMessage());
        }
    }


    public void addMatchVerifyEventError(String moduleCount, Map<String, String> data) {
        try {
            baseUtil.click(addBtn);
            matchList.add(ModuleNameConstants.MATCH_NAME + getRandomString());
            baseUtil.enterText(matchNameTextbox, matchList.get(matchList.size() - 1));
            String eventID = baseUtil.getEventIDFromDB();
            baseUtil.enterText(matchEventIdTextbox, eventID);
            //select first tournament
            selectDropdownValue(By.xpath("(//select[@class='form-control '])[1]"), null);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            //select first venue
            selectDropdownValue(By.xpath("(//select[@class='form-control '])[2]"), null);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            //select first schedule
            selectDropdownValue(By.xpath("(//select[@class='form-control '])[3]"), null);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            //select first team A
            WebElement teamADropdown = TestBase.getWebDriver().findElement(By.xpath("(//select[@class='form-control '])[4]"));
            Select teamASelect = new Select(teamADropdown);
            teamASelect.selectByIndex(1);  // first valid option

            String selectedTeamAValue = teamASelect.getFirstSelectedOption().getAttribute("value");

            //select first teamB
            selectDropdownValue(By.xpath("(//select[@class='form-control '])[5]"), selectedTeamAValue);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);


            dateForMatch.add(getCurrentDatePlusDays(2));
            matchDate.sendKeys(dateForMatch.get(dateForMatch.size() - 1));
            WebElement timeInput = TestBase.getWebDriver().findElement(By.xpath("//input[@type='time']"));
            timeInput.clear();
            timeInput.sendKeys(twentyMinutesAHeadTimeForMatch);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(formAddBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertTrue("Error message is not visible for event Id that is already exist"
                    , baseUtil.isElementDisplayed(eventIdErrorMsg));
            eventId = generateRandomNumber();
            baseUtil.enterText(matchEventIdTextbox, String.valueOf(eventId));
            baseUtil.click(formAddBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        } catch (Exception e) {
            Assert.fail("enable to verify match due to " + e.getMessage());
            logger.error("Error in addAsset: " + e.getMessage());
        } finally {
            if (baseUtil.isElementDisplayed(eventIdErrorMsgCloseBtn)) {
                baseUtil.click(eventIdErrorMsgCloseBtn);
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }
        }
    }

    public void selectDropdownValue(By dropdownLocator, String excludeValue) {

        WebElement dropdownElement = TestBase.getWebDriver().findElement(dropdownLocator);
        Select select = new Select(dropdownElement);

        List<WebElement> options = select.getOptions();

        for (WebElement option : options) {

            String value = option.getAttribute("value").trim();
            String text = option.getText().trim();

            if (value.isEmpty()) {
                continue;
            }

            if (excludeValue != null && value.equals(excludeValue)) {
                continue;
            }

            select.selectByValue(value);
            logger.info("Selected: " + text);
            return;
        }

        throw new RuntimeException("No valid option found to select.");
    }

    public void deleteMatch() {
        try {
            baseUtil.searchData(searchBox, matchList.get(matchList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(deleteBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            baseUtil.click(deleteBtnOnDeletePopup);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Connection connection = baseUtil.getConn(TestBase.getInstance().getReadDatabaseUrls()
                            .get(TestBase.getEnv() + "_DBUrl"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBUsername"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBPassword"));
            String eventIDFromDB = dbUtils.getStringColumnValueFromDB
                    (DBQueries.GET_MATCH, connection, "event_id");
            Assert.assertEquals("Event Id not persist in database after deleting the match from UI as it is soft delete.",
                    String.valueOf(eventId), eventIDFromDB);
        } catch (Exception e) {
            Assert.fail("unable to delete the match due to " + e.getMessage());
        }
    }

    public String generateUniqueTournamentName(String baseName) {
        String randomPart = UUID.randomUUID().toString().substring(0, 6);
        return baseName + " @ " + randomPart + " #2026";
    }

    public void addMatch(String module, int moduleCount, Map<String, String> data) {
        try {
            baseUtil.click(addBtn);
            matchList.add(generateUniqueTournamentName(ModuleNameConstants.MATCH_NAME));
            baseUtil.enterText(matchNameTextbox, matchList.get(matchList.size() - 1));
            matchEventId = String.valueOf(generateRandomNumber());
            baseUtil.enterText(matchEventIdTextbox, matchEventId);
            Select matchTournament = new Select(matchTournamentDropdown);
            matchTournament.selectByVisibleText(tournamentList.get(tournamentList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Select matchVenue = new Select(matchVenueDropdown);
            matchVenue.selectByVisibleText(venueList.get(venueList.size() - 1));
            Select matchSchedule = new Select(matchScheduleDropdown);
            matchSchedule.selectByVisibleText(scheduleName);
            Select teamA = new Select(matchTeamADropdown);
            Select teamB = new Select(matchTeamBDropdown);
            teamA.selectByVisibleText(menTeamList.get(menTeamList.size() - 2));
            teamB.selectByVisibleText(menTeamList.get(menTeamList.size() - 1));
            dateForMatch.add(getCurrentDatePlusDays(Integer.parseInt(data.get("Match Date"))));
            matchDate.sendKeys(dateForMatch.get(dateForMatch.size() - 1));
            WebElement timeInput = TestBase.getWebDriver().findElement(By.xpath("//input[@type='time']"));
            timeInput.clear();
            if (!data.get("Start Time").equalsIgnoreCase("Current time")) {
                timeInput.sendKeys(data.get("Start Time"));
            } else {
                timeInput.sendKeys(twentyMinutesAHeadTimeForMatch);
            }
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(formAddBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertTrue("the success message is not visible... " + matchSuccessMsg.getText(),
                    baseUtil.isElementDisplayed(matchSuccessMsg));

        } catch (Exception e) {
            Assert.fail("unable to create match with the name of " + matchList.get(matchList.size() - 1) + " due to " +
                    e.getMessage());
        }
    }

    public static String getCurrentTime(int minutesToAdd) {
        LocalTime now = LocalTime.now();
        LocalTime updatedTime = now.plusMinutes(minutesToAdd);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm");
        return updatedTime.format(formatter);
    }


    public void verifyModule(String type, String moduleName, Map<String, String> data, int moduleCount) {
        try {
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            for (int i = 0; i < moduleCount; i++) {
                switch (moduleName.toLowerCase()) {
                    case "venue":
                        TestBase.getWebDriver().navigate().refresh();
                        baseUtil.explicitWait(PathConstants.WAIT_LOW);
                        if (type.equalsIgnoreCase("add")) {
                            assertElement("assetName", venueList.get(venueList.size() - 1));
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            assertElement("viewDetailsPopupForModule", data.get("Country"));
                            assertElement("viewDetailsPopupForModule", data.get("State"));

                            Assert.assertTrue("City not verified for venue :: " + venueList.get(venueList.size() - 1),
                                    baseUtil.isElementDisplayed(testBase.getReadResources()
                                            .getDataFromPropertyFile("Global", "viewDetailsPopupForModule")
                                            .replaceAll("#value#", data.get("City").toLowerCase())));

                            assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModuleUpdatedDate", getCurrentDatePlusDays(0));

                        } else if (type.equalsIgnoreCase("edit")) {
                            assertElement("assetName", venueList.get(venueList.size()-1));
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            assertElement("viewDetailsPopupForModule", data.get("Country"));
                            assertElement("viewDetailsPopupForModule", data.get("State"));
                            assertElement("viewDetailsPopupForModule", data.get("City"));
                            assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModuleUpdatedDate", getCurrentDatePlusDays(0));
                        }
                        break;

                    case "teams":
                        if (type.equalsIgnoreCase("add")) {
                            if (data.get("Gender").equalsIgnoreCase("Men")) {
                                TestBase.getWebDriver().navigate().refresh();
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.click(searchBox);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.enterText(searchBox, menTeamList.get(menTeamList.size() - 1));
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                assertElement("assetName", menTeamList.get(menTeamList.size() - 1));
                                baseUtil.click(eyeBtn);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                                assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 1));
                                assertElement("viewDetailsPopupForModule", data.get("Gender"));
                                assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));

                            } else if (data.get("Gender").equalsIgnoreCase("Women")) {
                                TestBase.getWebDriver().navigate().refresh();
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.click(searchBox);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.enterText(searchBox, womenTeamList.get(womenTeamList.size() - 1));
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                assertElement("assetName", womenTeamList.get(womenTeamList.size() - 1));
                                baseUtil.click(eyeBtn);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                                assertElement("viewDetailsPopupForModule", womenTeamList.get(womenTeamList.size() - 1));
                                assertElement("viewDetailsPopupForModule", data.get("Gender"));
                                assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            }

                        } else if (type.equalsIgnoreCase("edit")) {
                            if (data.get("Gender").equalsIgnoreCase("Men")) {
                                TestBase.getWebDriver().navigate().refresh();
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                baseUtil.click(searchBox);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.enterText(searchBox, menTeamList.get(menTeamList.size() - 2));
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                assertElement("assetName", menTeamList.get(menTeamList.size() - 2));
                                baseUtil.click(eyeBtn);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                                assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 2));
                                assertElement("viewDetailsPopupForModule", data.get("Gender"));
                                assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));

                            } else if (data.get("Gender").equalsIgnoreCase("Women")) {
                                TestBase.getWebDriver().navigate().refresh();
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                baseUtil.click(searchBox);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                                baseUtil.enterText(searchBox, womenTeamList.get(womenTeamList.size() - 2));
                                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                                assertElement("assetName", womenTeamList.get(womenTeamList.size() - 2));
                                baseUtil.click(eyeBtn);
                                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                                assertElement("viewDetailsPopupForModule", womenTeamList.get(womenTeamList.size() - 2));
                                assertElement("viewDetailsPopupForModule", data.get("Gender"));
                                assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            }
                        }
                        break;
                    case "tournament":
                        if (type.equalsIgnoreCase("add")) {
                            TestBase.getWebDriver().navigate().refresh();
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            baseUtil.click(searchBox);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.enterText(searchBox, tournamentList.get(tournamentList.size() - 1));
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            assertElement("assetName", tournamentList.get(tournamentList.size() - 1));
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);

                            assertElement("viewDetailsPopupForModule", tournamentList.get(tournamentList.size() - 1));
                            assertElement("viewDetailsPopupForModule", data.get("No. of Matches"));
//                            assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
//                            assertElement("viewDetailsPopupForModuleUpdatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModule", data.get("Format Type"));
                            assertElement("viewDetailsPopupForModule", data.get("Tournament Type").toUpperCase());
                            assertElement("viewDetailsPopupForModule", data.get("Gender Type").toUpperCase());
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            String startDate = getCurrentDatePlusDays(Integer.parseInt(data.get("Start Date")));
                            String endDate = getCurrentDatePlusDays(Integer.parseInt(data.get("End Date").split(" ")[0]));
                            assertElement("viewDetailsPopupForModule", startDate);
                            assertElement("viewDetailsPopupForModule", endDate);

//                            Assert.assertTrue("City not verified for venue :: " + tournamentList.get(i),
//                                    baseUtil.isElementDisplayed(testBase.getReadResources()
//                                            .getDataFromPropertyFile("Global", "viewDetailsPopupForModule")
//                                            .replaceAll("#value#", data.get("City").toLowerCase())));

//                            assertElement("viewDetailsPopupForModuleCreatedDate",
//                                    getCurrentDatePlusDays(Integer.parseInt(data.get("Start Date").split(" ")[0])));
//                            assertElement("viewDetailsPopupForModuleUpdatedDate",
//                                    getCurrentDatePlusDays(Integer.parseInt(data.get("End Date").split(" ")[0])));

                        } else if (type.equalsIgnoreCase("edit")) {
                            TestBase.getWebDriver().navigate().refresh();
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            baseUtil.click(searchBox);
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.enterText(searchBox, tournamentList.get(tournamentList.size() - 2));
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            assertElement("assetName", tournamentList.get(tournamentList.size()-2));
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                            assertElement("viewDetailsPopupForModule", tournamentList.get(tournamentList.size()-2));
                            assertElement("viewDetailsPopupForModule", data.get("No. of Matches"));
                            assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModuleUpdatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModule", data.get("Format Type"));
                            assertElement("viewDetailsPopupForModule", data.get("Tournament Type"));
                            assertElement("viewDetailsPopupForModule", data.get("Gender Type"));
                            //                        assertElement("viewDetailsPopupForModule", data.get("Country"));
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            String startDate = getCurrentDatePlusDays(Integer.valueOf(data.get("Start Date")));
                            String endDate = getCurrentDatePlusDays(Integer.valueOf(data.get("End Date").split(" ")[0]));
                            assertElement("viewDetailsPopupForModule", startDate);
                            assertElement("viewDetailsPopupForModule", endDate);

//                            Assert.assertTrue("City not verified for venue :: " + tournamentList.get(i),
//                                    baseUtil.isElementDisplayed(testBase.getReadResources()
//                                            .getDataFromPropertyFile("Global", "viewDetailsPopupForModule")
//                                            .replaceAll("#value#", data.get("City").toLowerCase())));

                            assertElement("viewDetailsPopupForModuleCreatedDate", getCurrentDatePlusDays(0));
                            assertElement("viewDetailsPopupForModuleUpdatedDate", getCurrentDatePlusDays(5));
                        }
                        break;
                    case "match":
                        if (type.equalsIgnoreCase("add")) {
                            TestBase.getWebDriver().navigate().refresh();
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            baseUtil.click(searchBox);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            baseUtil.enterText(searchBox, matchList.get(matchList.size() - 1));
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            assertElement("assetName", matchList.get(matchList.size() - 1));
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

//                            assertElement("viewDetailsPopupForModule", matchList.get(i));
                            assertElement("viewDetailsPopupForModule", matchEventId);
                            assertElement("viewDetailsPopupForModule", tournamentList.get(tournamentList.size() - 1));
                            assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 1));
                            assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 2));
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            assertElement("viewDetailsPopupForModule", scheduleName);
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            String MatchDate = getCurrentDatePlusDays(Integer.parseInt(data.get("Match Date")));
                            String createdDate = getCurrentDatePlusDays(0);
//                            if(!data.get("Start Time").equalsIgnoreCase("Current time")) {
//                                assertElement("viewDetailsPopupForModule", data.get("Start Time"));
//                            }else {
//                                assertElement("viewDetailsPopupForModule", twentyMinutesAHeadTimeForMatch);
//                            }
                            assertElement("viewDetailsPopupForModule", createdDate);
                            assertElement("viewDetailsPopupForModule", MatchDate);

                        } else if (type.equalsIgnoreCase("edit")) {
                            baseUtil.explicitWait(PathConstants.WAIT_LOW);
                            baseUtil.click(searchBox);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                            baseUtil.enterText(searchBox, matchList.get(matchList.size() - 1));
                            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                            assertElement("assetName", matchList.get(matchList.size() - 1));
                            baseUtil.click(eyeBtn);
                            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                            assertElement("viewDetailsPopupForModule", matchList.get(matchList.size() - 1));
                            assertElement("viewDetailsPopupForModule", matchEventId);
                            assertElement("viewDetailsPopupForModule", tournamentList.get(tournamentList.size() - 1));
                            assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 1));
                            assertElement("viewDetailsPopupForModule", menTeamList.get(menTeamList.size() - 2));
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            assertElement("viewDetailsPopupForModule", scheduleName);
                            assertElement("viewDetailsPopupForModule", venueList.get(venueList.size() - 1));
                            String MatchDate = getCurrentDatePlusDays(Integer.parseInt(data.get("Match Date")));
                            String createdDate = getCurrentDatePlusDays(Integer.parseInt(data.get("Match Date")));
                            assertElement("viewDetailsPopupForModule", data.get("Start Time"));
                            assertElement("viewDetailsPopupForModule", createdDate);
                            assertElement("viewDetailsPopupForModule", MatchDate);
                        }
                        break;

                    default:
                        logger.error("No module found with name :: " + moduleName);
                }
            }
        } catch (Exception e) {
            Assert.fail("enable to verify " + type + " " + moduleName + " due to " + e.getMessage());
            logger.error("Error in verifyModule: " + e.getMessage());
        }
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
    }


    public static String getCurrentDatePlusDays(int days) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.now().plusDays(days);
        return date.format(formatter);
    }

    public void deleteModule(String moduleName, int count) {
        try {
            for (int i = 0; i < count; i++) {
                switch (moduleName.toLowerCase()) {
                    case "venue":
                        baseUtil.click(testBase.getReadResources().
                                getDataFromPropertyFile("Global", "viewDetailsPopupForModule").
                                replaceAll("#value#", venueList.get(i + 1)));
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        baseUtil.click(deleteBtnOnDeletePopup);
                        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                        if (baseUtil.isElementDisplayed(testBase.getReadResources().
                                getDataFromPropertyFile("Global", "viewDetailsPopupForModule").
                                replaceAll("#value#", venueList.get(i + 1)))) {
                            Assert.fail("unable to delete venue");
                        }
                    default:
                        logger.error("No module found :: " + moduleName);
                }
            }
        } catch (Exception e) {
            logger.error("unable to delete the module :: " + moduleName + " due to " + e.getMessage());
        }

    }

    protected void assertElement(String key, String value) throws IOException {
        String locator = testBase.getReadResources()
                .getDataFromPropertyFile("Global", key)
                .replaceAll("#value#", value);
        Assert.assertTrue("Unable to verify: " + value, baseUtil.isElementDisplayed(locator));
    }


    public void verifyErrorMsgForDuplicateTeamName(String moduleName, String errorMsg, int count, Map<String, String> data) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            for (int i = 0; i < count; i++) {

                switch (moduleName.toLowerCase()) {
                    case "teams":
                        testBase.getWebDriver().navigate().refresh();
                        baseUtil.explicitWait(PathConstants.WAIT_LOW);
                        baseUtil.click(addBtn);
                        Select select = new Select(teamGenderDropdown);
                        if (data.get("Gender").equalsIgnoreCase("men")) {
                            baseUtil.enterText(teamNameTextBox, matchList.get(matchList.size() - 1));
                            select.selectByVisibleText(data.get("Gender"));
                        } else if (data.get("Gender").equalsIgnoreCase("women")) {
                            baseUtil.enterText(teamNameTextBox, womenTeamList.get(womenTeamList.size() - 1));
                            select.selectByVisibleText(data.get("Gender"));
                        } else {
                            logger.error("Invalid team gender selected...");
                        }
                        uploadLogo("team.png", moduleName);
                        baseUtil.click(formAddBtn);
                        verifyErrorMessageForDuplicateModule(errorMsg);
                        baseUtil.explicitWait(PathConstants.WAIT_LOW);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public void verifyErrorMessageForDuplicateModule(String errorMsg) {
        logger.info("Method is running currently..." + logger.getName());
        Assert.assertEquals(errorMsg, baseUtil.getElementText(duplicateTeamErrorMsg));
    }

    public List<String> generatePlayerNames(Integer count) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            playerNames = new ArrayList<>();
            String prefix = "AutomationPlayer";
            for (int i = 0; i < count; i++) {
                playerNames.add(prefix + getRandomString());
            }
        } catch (Exception e) {
            Assert.fail("unable to generate random player name due to :: " + e.getMessage());
        }
        return playerNames;
    }

    public void verifyStatusOfThePlayers(Map<String, String> data) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            baseUtil.click(searchBox);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, menTeamList.get(menTeamList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(eyeBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
//            assertElement("playerStatusOnTeamVerificationPopup", data.get("Active"));
//            assertElement("playerInactiveStatusOnTeamVerificationPopup", data.get("Inactive"));

        } catch (Exception e) {
            Assert.fail("Unable to verify the status of the players due to :: " + e.getMessage());
        } finally {
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        }
    }


    public void updateStatus(String status, String count, Map<String, String> moduleList) {
        logger.info("Method is running currently..." + logger.getName());
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        int i = 0;
        try {
            for (String module : moduleList.keySet()) {
                if (i == Integer.parseInt(count)) {
                    break;
                }
                i += 1;
                baseUtil.click(searchBox);
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.enterText(searchBox, module);
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
                Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
                logger.info("Current status for [" + module + "] = " + isChecked);

                if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("live")) {
                    if (!isChecked) {
                        baseUtil.click(statusRadioBtn);
                        logger.info("Changed status to ACTIVE for: " + module);
                    } else {
                        logger.info("Status is already ACTIVE for: " + module);
                    }
                    // update map
                    playersList.put(module, "active");

                } else if (status.equalsIgnoreCase("inactive") || status.equalsIgnoreCase("non live")) {
                    if (isChecked) {
                        baseUtil.click(statusRadioBtn);
                        logger.info("Changed status to INACTIVE for: " + module);
                    } else {
                        logger.info("Status is already ACTIVE for: " + module);
                    }
                    // update map
                    moduleList.put(module, "inactive");

                } else {
                    throw new IllegalArgumentException("Invalid status provided: " + status);
                }
            }
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
//            Assert.assertTrue("Status is not updated to " + status,
//                    baseUtil.isElementDisplayed(statusUpdateMsg));
        } catch (Exception e) {
            Assert.fail("Unable to change the module status to :: " + status + " due to " + e.getMessage());
        }
    }

    public void updateAgentStatus(String status) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            baseUtil.click(searchBox);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, agentList.get(agentList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
            Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
            logger.info("Current status is = " + isChecked);

            if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("live")) {
                if (Boolean.FALSE.equals(isChecked)) {
                    baseUtil.click(statusRadioBtn);
                    logger.info("Changed status to ACTIVE");
                }

            } else if (status.equalsIgnoreCase("inactive") || status.equalsIgnoreCase("non live")) {
                if (Boolean.TRUE.equals(isChecked)) {
                    baseUtil.click(statusRadioBtn);
                    logger.info("Changed status to INACTIVE for");
                }

            } else {
                throw new IllegalArgumentException("Invalid status provided: " + status);
            }
        } catch (Exception e) {
            Assert.fail("Unable to change the module status to :: " + status + " due to " + e.getMessage());
        }
    }


    public void verifyActiveInactiveStatus(String status) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            baseUtil.click(searchBox);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, agentList.get(agentList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
            Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
            logger.info("Current status is= " + isChecked);

            if (status.equalsIgnoreCase("active") || status.equalsIgnoreCase("live")) {
                Assert.assertNotEquals("the status is not active", Boolean.FALSE, isChecked);
            } else if (status.equalsIgnoreCase("inactive") || status.equalsIgnoreCase("non live")) {
                Assert.assertNotEquals("the status is not inactive", Boolean.TRUE, isChecked);

            } else {
                throw new IllegalArgumentException("Invalid status provided: " + status);
            }
        } catch (
                Exception e) {
            Assert.fail("Unable to change the module status to :: " + status + " due to " + e.getMessage());
        }
    }

    public void logout() {
        logger.info("logout the current user.");
        TestBase.getWebDriver().findElement(By.xpath("//button[@class='logout-btn']/../..")).click();
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        getTestBase().getWebDriverWait().until(ExpectedConditions.visibilityOf(usernameTextbox));
        Assert.assertTrue("Username field is not visible after logout.",
                baseUtil.isElementDisplayed(usernameTextbox));
    }

    public static String getMatchEventId() {
        return matchEventId;
    }

    public void verifyPage(String page) {
        logger.info("Method is running currently..." + logger.getName());
//        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        try {
            switch (page.toLowerCase()) {
                case "matches":
                    Assert.assertTrue("Match page not opened after clicking on the total" +
                            " matches button on dashboard page", matchesHeadingOnMatchPage.isDisplayed());
                    break;
                case "live matches":
                    Assert.assertTrue("Match page not opened after clicking on the total" +
                            " matches button on dashboard page", liveMatchesHeadingOnLiveMatchPage.isDisplayed());
                    break;
                case "managers":
                    Assert.assertTrue("Match page not opened after clicking on the total" +
                            " matches button on dashboard page", managerHeadingOnManagerPage.isDisplayed());
                    break;
                case "agents":
                    Assert.assertTrue("Match page not opened after clicking on the total" +
                            " matches button on dashboard page", agentHeadingOnAgentPage.isDisplayed());
                    break;
                case "teams":
                    Assert.assertTrue("Match page not opened after clicking on the total" +
                            " matches button on dashboard page", teamsHeadingOnTeamsPage.isDisplayed());
                    break;
//                case "agents":
//                    WebElement agentPage=TestBase.getWebDriver().findElement(By.xpath(testBase.getReadResources().
//                            getDataFromPropertyFile("Global", "pageHeading").replaceAll("#pageName#","Agents")));
//                    Assert.assertTrue("Match page not opened after clicking on the total" +
//                            " matches button on dashboard page",agentPage.isDisplayed());
//                    break;
//                case "matches heading":
//                    WebElement matchPage=TestBase.getWebDriver().findElement(By.xpath(testBase.getReadResources().
//                            getDataFromPropertyFile("Global", "pageHeading").replaceAll("#pageName#","Agents")));
//                    Assert.assertTrue("Match page not opened after clicking on the total" +
//                            " matches button on dashboard page",matchPage.isDisplayed());
//                    Assert.assertEquals("Matches", matchPage.getText());
//                    break;
//                case "live matches heading":
//                    WebElement liveMatchPage=TestBase.getWebDriver().findElement(By.xpath(testBase.getReadResources().
//                            getDataFromPropertyFile("Global", "pageHeading").replaceAll("#pageName#","Agents")));
//                    Assert.assertTrue("Match page not opened after clicking on the total" +
//                            " matches button on dashboard page",liveMatchPage.isDisplayed());
//                    Assert.assertEquals("Live Matches", liveMatchPage.getText());
//                    break;
                default:
                    Assert.fail("please provide a proper button name :: " + page + " to navigate on that page from dashboard page");
            }
        } catch (Exception e) {
            Assert.fail("unable to verify page " + page + " due to " + e.getMessage());
        }
    }

    public void verifyDatesAreInDescendingOrder(String page) {
        logger.info("Method is running currently..." + logger.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        List<LocalDate> uiDates = List.of();
        WebElement element;
        if (page.equalsIgnoreCase("TOURNAMENT")) {
            baseUtil.click(tournament);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            element = TestBase.getWebDriver().findElement(By.xpath("//table//tbody//tr//td[7]"));
            uiDates = element
                    .findElements(By.xpath(".//td"))
                    .stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .skip(1)
                    .map(text -> LocalDate.parse(text, formatter))
                    .toList();
        } else if (page.equalsIgnoreCase("MATCH")) {
            baseUtil.click(matches);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            element = TestBase.getWebDriver().findElement(By.xpath("//table//tbody//tr//td[6]"));
            uiDates = element
                    .findElements(By.xpath(".//td"))
                    .stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .skip(1)
                    .map(text -> LocalDate.parse(text, formatter))
                    .toList();
        } else if (page.equalsIgnoreCase("PLAYER")) {
            baseUtil.click(players);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            element = TestBase.getWebDriver().findElement(By.xpath("//table//tbody//tr//td[9]"));
            uiDates = element
                    .findElements(By.xpath(".//td"))
                    .stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .skip(1)
                    .map(text -> LocalDate.parse(text, formatter))
                    .toList();
        } else if (page.equalsIgnoreCase("LIVE MATCHES")) {
            baseUtil.click(liveMatches);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            element = TestBase.getWebDriver().findElement(By.xpath("//table//tbody//tr//td[4]"));
            uiDates = element
                    .findElements(By.xpath(".//td"))
                    .stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .skip(1)
                    .map(text -> LocalDate.parse(text, formatter))
                    .toList();
        } else if (page.equalsIgnoreCase("MANAGER")) {
            baseUtil.click(manager);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            element = TestBase.getWebDriver().findElement(By.xpath("//table//tbody//tr//td[5]"));
            uiDates = element
                    .findElements(By.xpath(".//td"))
                    .stream()
                    .map(e -> e.getText().trim())
                    .filter(text -> !text.isEmpty())
                    .skip(1)
                    .map(text -> LocalDate.parse(text, formatter))
                    .toList();
        } else {
            Assert.fail("Provided page name not match with UI. provided page name :: " + page);
        }


        List<LocalDate> sortedDates = uiDates.stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        Assert.assertEquals("Dates are NOT in descending order", sortedDates, uiDates);

        logger.info("Dates are in Descending Order");
    }

    public void deleteData(String moduleName) {
        logger.info("Method is running currently..." + logger.getName());
        try {
            if (moduleName.equalsIgnoreCase("players")) {
                baseUtil.searchData(searchBox, playerNames.get(playerNames.size() - 1));
                deleteBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                deleteBtnOnDeletePopup.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                Assert.assertTrue("player ::  " + playerNames.get(playerNames.size() - 1) + " is still visible after deleting it.",
                        baseUtil.isElementDisplayed(latestAddedData));
            } else if (moduleName.equalsIgnoreCase("matches")) {
                baseUtil.searchData(searchBox, matchList.get(matchList.size() - 1));
                deleteBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                deleteBtnOnDeletePopup.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                Assert.assertTrue("match ::  " + matchList.get(matchList.size() - 1) + " is still visible after deleting it.",
                        baseUtil.isElementDisplayed(latestAddedData));
            } else if (moduleName.equalsIgnoreCase("venue")) {
                baseUtil.searchData(searchBox, venueList.get(venueList.size() - 1));
                WebElement element = baseUtil.getElementAfterLoaded(getTestBase().getReadResources().
                        getDataFromPropertyFile("Global", "venueDeleteBtn").
                        replaceAll("#venueName#", venueList.get(venueList.size() - 1)));
                element.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                deleteBtnOnDeletePopup.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                Assert.assertTrue("Venue ::  " + venueList.get(venueList.size() - 1) + " is still visible after deleting it.",
                        baseUtil.isElementDisplayed(baseUtil.getElementAfterLoaded(getTestBase().getReadResources().
                                getDataFromPropertyFile("Global", "moduleData").
                                replaceAll("#name#", venueList.get(venueList.size() - 1)))));
            }


        } catch (Exception e) {
            Assert.fail("Unable to delete the data for " + moduleName + " due to " + e.getMessage());
        }
    }

    public void verifyMatchWithDateFilter() {
        logger.info("Method is running currently..." + logger.getName());
        try {
            baseUtil.click(filterBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            String originalDate = dateForMatch.get(dateForMatch.size() - 1);
            dateFilterStartDate.sendKeys(originalDate);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            dateFilterEndDate.sendKeys(originalDate);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            eventTextboxOnFilterPopup.sendKeys(matchEventId);
            submitBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("After filtering out the date from filter popup, it is not showing on the UI :: " + matchList.get(matchList.size() - 1),
                    baseUtil.isElementDisplayed(TestBase.getWebDriver().
                            findElement(By.xpath("//td[contains(text(),'#eventId#')]".
                                    replaceAll("#eventId#", matchEventId)))));
        } catch (Exception e) {
            Assert.fail("Unable to verify the match with date filter. Due to " + e.getMessage());
        }
    }

    public void verifyValidationMessageForTournamentDate() {
        try {
            logger.info("Method is running currently..." + logger.getName());
            baseUtil.click(addBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            tournamentStartDate.sendKeys("01-01-2025");
            tournamentEndDate.sendKeys("01-01-2025");
            formAddBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertEquals(getTestBase().getReadResources().
                            getDataFromPropertyFile("Global", "bothDateInPast").trim(),
                    invalidDateErrorMsg.getText().trim());

            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            tournamentStartDate.sendKeys(getCurrentDatePlusDays(2));
            tournamentEndDate.sendKeys(getCurrentDatePlusDays(1));
            formAddBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertEquals(getTestBase().getReadResources().
                            getDataFromPropertyFile("Global", "startDateLessThenEndDate").trim(),
                    invalidDateErrorMsg.getText().trim());
        } catch (Exception e) {
            Assert.fail("Unable to verify validation message for tournament dates due to " + e.getMessage());
        } finally {
            formCloseBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        }
    }

    public void verifyManagerCountBetweenManagerPageAndMatchDropdown() {

        logger.info("Verifying manager list consistency between Match page and Manager page");

        try {
            matches.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            selectBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            List<String> managerListOnMatchPage =
                    baseUtil.getMultipleElementsAfterLoaded(managersListOnMatchPage)
                            .stream()
                            .map(WebElement::getText)
                            .map(String::trim)
                            .filter(text -> !text.isEmpty())
                            .sorted()
                            .toList();

            manager.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            List<String> managerListOnManagerPage =
                    baseUtil.getMultipleElementsAfterLoaded(managersListOnManagerPage)
                            .stream()
                            .map(WebElement::getText)
                            .map(String::trim)
                            .filter(text -> !text.isEmpty())
                            .sorted()
                            .toList();

            if (!managerListOnMatchPage.equals(managerListOnManagerPage)) {

                List<String> missingFromDropdown = managerListOnManagerPage.stream()
                        .filter(name -> !managerListOnMatchPage.contains(name))
                        .toList();

                List<String> extraInDropdown = managerListOnMatchPage.stream()
                        .filter(name -> !managerListOnManagerPage.contains(name))
                        .toList();

                Assert.fail("Manager lists are not matching.\n" +
                        "Missing in dropdown: " + missingFromDropdown + "\n" +
                        "Extra in dropdown: " + extraInDropdown);
            }

            logger.info("Manager lists matched successfully");

        } catch (Exception e) {
            Assert.fail("Unable to verify manager consistency due to: " + e.getMessage());
        }
    }

    public boolean isNextButtonClickable() {

        try {
            WebElement nextBtnLi = TestBase.getWebDriver().findElement(
                    By.xpath("//li[button[normalize-space()='Next']]")
            );

            String classValue = nextBtnLi.getAttribute("class");

            return !classValue.contains("disabled");

        } catch (Exception e) {
            logger.info("Pagination not visible or Next not found");
            return false;
        }
    }

    public void collectTeamsFromCurrentPage(Set<String> menSet, Set<String> womenSet) {

        List<WebElement> menElements = TestBase.getWebDriver().findElements(
                By.xpath("//td[normalize-space()='Men']//..//child::td[2]")
        );

        for (WebElement e : menElements) {
            WebElement statusRadioBtn = e.findElement(By.xpath("./ancestor::tr//input[@type='checkbox']"));
            if (statusRadioBtn.isSelected()) {
                menSet.add(e.getText().trim());
            }
        }

        List<WebElement> womenElements = TestBase.getWebDriver().findElements(
                By.xpath("//td[normalize-space()='Women']//..//child::td[2]")
        );

        for (WebElement e : womenElements) {
            WebElement statusRadioBtn = e.findElement(By.xpath("./ancestor::tr//input[@type='checkbox']"));
            if (statusRadioBtn.isSelected()) {
                womenSet.add(e.getText().trim());
            }
        }
    }

    public Map<String, List<String>> collectAllTeamsFromAllPages() {

        Set<String> menSet = new HashSet<>();
        Set<String> womenSet = new HashSet<>();

        try {

            boolean paginationVisible = !TestBase.getWebDriver()
                    .findElements(By.cssSelector("div.rit-pagi-bx")).isEmpty();

            if (!paginationVisible) {
                collectTeamsFromCurrentPage(menSet, womenSet);
            } else {

                while (true) {

                    collectTeamsFromCurrentPage(menSet, womenSet);

                    if (isNextButtonClickable()) {

                        WebElement nextBtn = TestBase.getWebDriver()
                                .findElement(By.xpath("//button[normalize-space()='Next']"));

                        nextBtn.click();

                        baseUtil.explicitWait(PathConstants.WAIT_LOW);

                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while collecting teams", e);
        }
        Map<String, List<String>> result = new HashMap<>();
        result.put("Men", new ArrayList<>(menSet));
        result.put("Women", new ArrayList<>(womenSet));

        return result;
    }


//    public List<String> getWomenTeamsFromTable() {
//
//        womenElements = TestBase.getWebDriver().findElements(
//                By.xpath("//td[contains(text(),'Women')]//..//td[2]")
//        );
//
//        return womenElements.stream()
//                .map(e -> e.getText().trim())
//                .distinct()
//                .toList();
//    }
//
//    public List<String> getMenTeamsFromTable() {
//
//        menElements = TestBase.getWebDriver().findElements(
//                By.xpath("//td[contains(text(),'Men')]//..//td[2]")
//        );
//
//        return menElements.stream()
//                .map(e -> e.getText().trim())
//                .distinct()
//                .toList();
//    }

    public List<String> getTeamsFromPlayerPopup(String gender) {
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        baseUtil.click(players);
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        baseUtil.click(addBtn);
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        WebElement genderDropdown = TestBase.getWebDriver().findElement(By.xpath("//select[@name='gender']"));
        Select genderSelect = new Select(genderDropdown);
        genderSelect.selectByVisibleText(gender);

        baseUtil.explicitWait(PathConstants.WAIT_LOW);

        WebElement dropdown = TestBase.getWebDriver().findElement(
                By.xpath("//label[contains(text(),'Team')]//following::div[contains(@class,'css-13cymwt-control')][1]")
        );

        dropdown.click();
        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option']")));

        List<WebElement> options = TestBase.getWebDriver().findElements(By.xpath("//div[@role='option']"));

        List<String> teamList = options.stream()
                .map(e -> e.getText().trim())
                .distinct()
                .collect(Collectors.toList());

//        dropdown.sendKeys(Keys.ESCAPE);

        return teamList;
    }

    public void verifyTeams() {

        Map<String, List<String>> tableTeams = collectAllTeamsFromAllPages();

        List<String> tableMenTeams = tableTeams.get("Men");
        List<String> tableWomenTeams = tableTeams.get("Women");

        List<String> popupMenTeams = getTeamsFromPlayerPopup("Men");
        List<String> popupWomenTeams = getTeamsFromPlayerPopup("Women");

        Collections.sort(tableMenTeams);
        Collections.sort(popupMenTeams);
        Collections.sort(tableWomenTeams);
        Collections.sort(popupWomenTeams);

        Assert.assertEquals("Men Teams Mismatch!", tableMenTeams, popupMenTeams);
        Assert.assertEquals("Women Teams Mismatch!", tableWomenTeams, popupWomenTeams);
    }

    public void verifyLoginValidationMsg(Map<String, String> data) {
        logger.info("Verifying login validation message..." + logger.getName());
        try {
            String username = data.get("Username");
            String password = data.get("Password");
            logger.info("Verifying login message for valid and invalid credentials. username" +
                    " :: " + username + " and password :: " + password + ", " + logger.getName());
            enterLoginCredentials(username, password);
            baseUtil.getElementAfterLoaded(getTestBase().
                    getReadResources().getDataFromPropertyFile("Global", "loginBtn_locator")).click();

            verifyLoginValidationMessage(username, password);
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
//            clearLoginFields();
        } catch (Exception e) {
            Assert.fail("Unable to verify the login validation message due to " + e.getMessage());
        }
    }

    public void enterLoginCredentials(String username, String password) {
        logger.info("Entering login credentials..." + logger.getName());
        try {
            baseUtil.getElementAfterLoaded(getTestBase().
                    getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator")).click();
            baseUtil.getElementAfterLoaded(getTestBase().
                    getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator")).clear();

            if (username != null && !username.isEmpty()) {
                baseUtil.getElementAfterLoaded(getTestBase().
                        getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator")).sendKeys(username);
            }

            if (password != null && !password.isEmpty()) {
                baseUtil.getElementAfterLoaded(getTestBase().
                        getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator")).sendKeys(password);
            }
        } catch (Exception e) {
            Assert.fail("Unable to enter username and password due to " + e.getMessage());
        }
    }

    public void verifyLoginValidationMessage(String username, String password) {

        String actualMessage;
        baseUtil.explicitWait(PathConstants.WAIT_LOW);

        if (username == null) username = "";
        if (password == null) password = "";

        username = username.trim();
        password = password.trim();

        if (username.isEmpty()) {
            actualMessage = usernameErrorMsg.getText().trim();
            Assert.assertEquals("Please Enter Your Email.", actualMessage);
        } else if (password.isEmpty()) {
            actualMessage = pwdErrorMsg.getText().trim();
            Assert.assertEquals("Please Enter Your Password.", actualMessage);
        } else if (username.equals("admin001") && password.equals("adminPass")) {
            baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
            Assert.assertTrue(totalTeamsDashboard.isDisplayed());
            TestBase.getWebDriver().navigate().back();
        } else {
            actualMessage = invalidUserPwdError.getText().trim();
            Assert.assertEquals("Invalid Username Or Password.", actualMessage);
        }
    }


    public void clearLoginFields() {
        baseUtil.getElementAfterLoaded(getTestBase().
                getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator")).clear();
        baseUtil.getElementAfterLoaded(getTestBase().
                getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator")).clear();
    }

    public void updateTeamStatus(String count, String status) {
        try {
            Map<String, String> map = new LinkedHashMap<>();
            for (String team : menTeamList) {
                map.put(team, "active");
            }
            updateStatus(status, count, map);
            baseUtil.isElementDisplayed(successMsg);
        } catch (Exception e) {
            logger.error("unable to update match status due to " + e.getMessage());
        }
    }

    public void verifyInactiveTeamOnCreatePlayer() {
        logger.info("Verifying inactive team on create player popup..." + logger.getName());
        try {
            baseUtil.click(addBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            WebElement genderDropdown = TestBase.getWebDriver().findElement(By.xpath("//select[@name='gender']"));
            Select genderSelect = new Select(genderDropdown);
            genderSelect.selectByVisibleText("Men");

            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(text(),'Team')]//following::div[contains(@class,'css-13cymwt-control')][1]")
            ));

            ((JavascriptExecutor) TestBase.getWebDriver())
                    .executeScript("arguments[0].scrollIntoView(true);", dropdown);

            ((JavascriptExecutor) TestBase.getWebDriver())
                    .executeScript("arguments[0].click();", dropdown);
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class,'css-13cymwt-control')]//input)[2]")
            ));

            input.sendKeys(menTeamList.get(0));
            input.sendKeys(Keys.ENTER);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertTrue("Team is still visible on create player popup after making it inactive. inactive team :: " +
                    menTeamList.get(0), dropdown.getText().trim().equalsIgnoreCase(""));

        } catch (Exception e) {
            Assert.fail("unable to verify inactive team on create player popup due to " + e.getMessage());
        }
    }

    public void verifyEyeIcon(String module) {
        logger.info("Verifying eye icon on module creation popup.");
        try {
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("Eye icon is not displaying on the creation popup.",
                    baseUtil.isElementDisplayed(eyeIcon));
            baseUtil.click(formCloseBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(players);
            if (module.equalsIgnoreCase("agent")) {
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.click(agents);
            } else {
                baseUtil.explicitWait(PathConstants.WAIT_LOW);
                baseUtil.click(manager);
            }
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(passwordUpdateBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Assert.assertTrue("Eye icon is not displaying on the creation popup.",
                    baseUtil.isElementDisplayed(eyeIcon));
        } catch (Exception e) {
            Assert.fail("Unable to verify eye button on creation popup due to " + e.getMessage());
        }
    }

    public void verifyPlayerCountryNameAndCount() {
        logger.info("Verifying country name and count on player creation popup. " + logger.getName());
        try {
            List<String> apiCountryList = getCountryNameList();
            List<String> uiData = new LinkedList<>();
            List<String> uiCountryList = countryDropdownOptions();
            for (String value : uiCountryList) {
                uiData.add(value.toLowerCase());
            }

            Collections.sort(apiCountryList);
            Collections.sort(uiData);

            Assert.assertEquals("Country count mismatch between API and UI"
                    , uiData.size(), apiCountryList.size()
            );
            Assert.assertEquals("Country names mismatch between API and UI",
                    uiData, apiCountryList
            );

            logger.info("Country name and count verified successfully.");
        } catch (Exception e) {
            Assert.fail("Unable to verify country name and count on player creation popup due to " + e.getMessage());
        }
    }

    public List<String> getCountryNameList() {
        APICallUtil apiCallUtil = new APICallUtil();
        Response response = apiCallUtil.getCountries();

        List<String> countryList = response.jsonPath()
                .getList("data.country_name");

        return countryList.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .map(String::toLowerCase)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<String> countryDropdownOptions() {
        baseUtil.click(addBtn);
        baseUtil.explicitWait(PathConstants.WAIT_LOW);

        WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(15));
        JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
        List<String> list = new ArrayList<>();

        By inputLocator = By.xpath(
                "//label[contains(text(),'Country')]//following::div[contains(@class,'control')]//input"
        );

        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(inputLocator));
        js.executeScript("arguments[0].scrollIntoView({block:'center'});", input);
        input.click();

        WebElement menu = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'menu')]")
        ));

        int previousSize = 0;

        while (true) {
            List<WebElement> options = TestBase.getWebDriver()
                    .findElements(By.xpath("//div[@role='option']"));

            for (WebElement option : options) {
                String text = option.getText().trim();
                if (!text.isEmpty() && !list.contains(text)) {
                    list.add(text);
                }
            }

            if (list.size() == previousSize) {
                break;
            }

            previousSize = list.size();
            js.executeScript("arguments[0].scrollTop = arguments[0].scrollHeight;", menu);

            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        }

        return list.stream()
                .map(String::trim)
                .distinct()
                .collect(Collectors.toList());
    }

    public void verifyGenderUpdateTournament() {
        try {
            TestBase.getWebDriver().navigate().refresh();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.searchData(searchBox, tournamentList.get(tournamentList.size() - 1));
            baseUtil.click(editBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        } catch (Exception e) {
            Assert.fail("unable to verify the gender update for tournament :: " +
                    tournamentList.get(tournamentList.size() - 1) + " due to " + e.getMessage());
        }
    }

    public boolean isCheckboxSelected() {
        try {
            WebElement checkbox = baseUtil.getElementAfterLoaded(getTestBase().getReadResources().
                    getDataFromPropertyFile("Global", "checkbox").
                    replaceAll("#managerName#", managerName));

            return checkbox.isSelected();

        } catch (NoSuchElementException e) {
            System.out.println("Checkbox not found!");
            return false;
        }
    }

    public void verifyTeamOnCreateTournamentPopup(boolean flag) {
        logger.info("Verifying team on the team dropdown on tournament create popup.");
        try {
            baseUtil.click(addBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            WebElement genderDropdown = TestBase.getWebDriver().findElement(By.xpath("//select[@name='gender_type']"));
            Select genderSelect = new Select(genderDropdown);
            genderSelect.selectByVisibleText("Men");

            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));

            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//label[contains(text(),'Team')]//following::div[contains(@class,'css-13cymwt-control')][1]")
            ));

            ((JavascriptExecutor) TestBase.getWebDriver())
                    .executeScript("arguments[0].scrollIntoView(true);", dropdown);

            ((JavascriptExecutor) TestBase.getWebDriver())
                    .executeScript("arguments[0].click();", dropdown);
            WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//div[contains(@class,'css-13cymwt-control')]//input)[2]")
            ));

            input.sendKeys(menTeamList.get(menTeamList.size() - 1));
            input.sendKeys(Keys.ENTER);
            if (dropdown.getText().equalsIgnoreCase("")) {
                Assert.assertFalse("Team without 11 players is still visible on teams dropdown" +
                        " on tournament page", flag);
            } else {
                Assert.assertTrue("Team with 11 players is not visible on teams dropdown" +
                        " on tournament page", flag);
            }
        } catch (Exception e) {
            Assert.fail("Unable to verify the team on tournament create popup.");
        }
    }

    public void verifyToggleButton() {
        logger.info("Verifying toggle button..." + logger.getName());
        try {
            JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();

            String matchName = TestBase.getWebDriver().findElement(
                    By.xpath("(//table//tr//td[2])[2]")
            ).getText().trim();
            matchList.add(matchName);

            baseUtil.searchData(searchBox, matchList.get(matchList.size() - 1));
            Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
            logger.info("Current status for [" + matchList.get(matchList.size() - 1) + "] = " + isChecked);
//            Assert.assertNotEquals("toggle is false as expected.", Boolean.TRUE, isChecked);
            if (Boolean.TRUE.equals(isChecked)) {
                updateMatchStatus("inactive", false);
            } else {
                updateMatchStatus("active", true);
            }
        } catch (Exception e) {
            Assert.fail("unable to verify toggle button status due to " + e.getMessage());
        }
    }

    public void updateMatchStatus(String status, boolean isTrue) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String match : matchList) {
            map.put(match, "active");
        }
        updateStatus(status, "1", map);
        TestBase.getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        baseUtil.click(searchBox);
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        baseUtil.enterText(searchBox, matchList.get(matchList.size() - 1));
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
        Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
        logger.info("Current status for [" + matchList.get(matchList.size() - 1) + "] = " + isChecked);
        if (isTrue) Assert.assertNotEquals("toggle is false as expected.", Boolean.FALSE, isChecked);
        else Assert.assertNotEquals("toggle is false as expected.", Boolean.TRUE, isChecked);
    }

    public void updateTournamentStatus(String status) {
        Map<String, String> map = new LinkedHashMap<>();
        for (String tournament : tournamentList) {
            map.put(tournament, "active");
        }
        List<WebElement> rows = TestBase.getWebDriver().findElements(By.xpath("//table//tr"));

        for (WebElement row : rows) {
            List<WebElement> checkboxList = row.findElements(
                    By.xpath(".//input[@type='checkbox']")
            );
            if (!checkboxList.isEmpty()) {
                WebElement checkbox = checkboxList.get(0);
                if (checkbox.isSelected()) {
                    String tournamentName = row.findElement(
                            By.xpath("./td[2]")
                    ).getText().trim();
                    if (tournamentList.isEmpty()) {
                        tournamentList.add(tournamentName);
                    }
                    break;
                }
            }
        }
        baseUtil.searchData(searchBox, tournamentList.get(tournamentList.size() - 1));
        JavascriptExecutor js = (JavascriptExecutor) TestBase.getWebDriver();
        Boolean isChecked = (Boolean) js.executeScript("return arguments[0].checked;", statusRadioBtn);
        logger.info("Current status for [" + tournamentList.get(tournamentList.size() - 1) + "] = " + isChecked);
//        Assert.assertNotEquals("toggle is false as expected.", Boolean.TRUE, isChecked);
        if (Boolean.TRUE.equals(isChecked)) {
            baseUtil.click(statusRadioBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        }
    }

    public void verifyTournamentInDropdownOnMatchCreate() {
        try {
            baseUtil.click(addBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            Select select = new Select(TestBase.getWebDriver().findElement(
                    By.xpath("//select[contains(@class,'form-control')]")
            ));

            List<WebElement> allOptions = select.getOptions();
            boolean flag = true;
            for (WebElement option : allOptions) {
                if (option.getText().trim().equalsIgnoreCase(tournamentList.get(tournamentList.size() - 1))) {
                    flag = false;
                }
            }
            Assert.assertTrue("Inactive tournament is visible on the create match popup.", flag);
        } catch (Exception e) {
            Assert.fail("unable to verify tournament ");
        }
    }

    public void validateExistingMobileNoForAgent(String msg) {
        try {
            Connection connection = baseUtil.getConn(TestBase.getInstance().getReadDatabaseUrls()
                            .get(TestBase.getEnv() + "_DBUrl"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBUsername"),
                    TestBase.getInstance().getReadDataBaseCredentials()
                            .get(TestBase.getEnv() + "_DBPassword"));
            baseUtil.searchData(searchBox, agentList.get(agentList.size() - 1));
            baseUtil.click(editBtn);
            String mobileNo = dbUtils.getStringColumnValueFromDB(DBQueries.GET_AGENT_MOBILE, connection, "mobile_no");
            baseUtil.enterText(agentMobileTextBox, mobileNo);
            baseUtil.click(formUpdateBtn);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertEquals("Error message is not visible or incorrect",
                    msg, exitingMobNoErrorMsg.getText().trim());
        } catch (Exception e) {
            Assert.fail("Unable to verify error message while updating the exiting mobile no. due to " + e.getMessage());
        }
    }

    public void unassignManagerAndVerifyMessage(String page, String msg) {
        try {
            if (page.equalsIgnoreCase("match")) {
                baseUtil.click(matches);
            } else if (page.equalsIgnoreCase("live match")) {
                baseUtil.click(liveMatches);
            }
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.enterText(searchBox, matchList.get(matchList.size() - 1));
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(managerAssignDropdown);
            baseUtil.click(getTestBase().getReadResources().
                    getDataFromPropertyFile("Global", "managerInMatchAssignManagerDropdown").
                    replaceAll("#agentName#", managerList.get(managerList.size() - 1)));
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            Assert.assertTrue("The error message for live match is not visible after clicking on checkbox to unassign manager", baseUtil.isElementDisplayed(errorMsgForUnassignManager));

        } catch (Exception e) {
            logger.error("Unable to assign the manager to the match due to " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void verifyDateFilter() {
        try {
            //selecting start date
            selectDate("start");

            //selecting end date
            selectDate("end");

            baseUtil.click(submitBtn);
            Assert.assertTrue("Table data is not visible after applying date filter", baseUtil.isElementDisplayed(moduleTable));
        } catch (Exception e) {
            Assert.fail("unable to verify date filter due to " + e.getMessage());
        }
    }

    public void selectDate(String type) {

        WebDriverWait wait = new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10));
        WebElement dateField = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        LocalDate today = LocalDate.now();
        String startDate = today.minusYears(2).format(formatter);
        String endDate = today.plusDays(10).format(formatter);

        if (type.equalsIgnoreCase("start")) {

            dateField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//input[@type='date'])[1]")
            ));

            dateField.clear();
            dateField.sendKeys(startDate);

        } else if (type.equalsIgnoreCase("end")) {

            dateField = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//input[@type='date'])[2]")
            ));

            dateField.clear();
            dateField.sendKeys(endDate);
        }
    }

    public void verifyVenueFilter() {
        try {
            baseUtil.enterText(venueSearchBoxOnLiveMatchFilter, "Automation_");
            baseUtil.click(submitBtn);
            baseUtil.click(filterBtn);
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            baseUtil.click(clearBtn);
            Assert.assertEquals("Filter is not cleared after clicking on the clear button", "", venueSearchBoxOnLiveMatchFilter.getText().trim());
        } catch (Exception e) {
            Assert.fail("Unable to verify the filter on live match page due to " + e.getMessage());
        }
    }
}


