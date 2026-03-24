package com.Scoreboard.automation.tests.base;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class CommonStepDefinitions extends CommonMethods {
    BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(), TestBase.getInstance().getWebDriverWait());

    @Given("User log in to {string} for {string} UI and present at the home page.")
    public void user_log_in_to_admin_url_and_is_already_present_at_the_home_page(String env, String subEnv) throws IOException {
        getTestBase().userLogin(true, env, subEnv);
        doInitialization(!subEnv.equalsIgnoreCase("user"), subEnv);
    }

    @Given("User already log in to {string} for {string} UI and present at the home page.")
    public void user_already_log_in_to_admin_url_and_is_already_present_at_the_home_page(String env, String subEnv) throws IOException {
        getTestBase().userLogin(true, env, subEnv);
//        doInitialization(!subEnv.equalsIgnoreCase("user") && !subEnv.equalsIgnoreCase(TestBase.getSubEnv()), subEnv);
    }

    @And("user verify the following headers.")
    public void verifyHeaders(DataTable dataTable) {
        List<String> expectedHeaders = dataTable.row(0);
        verifyTableHeaders(expectedHeaders);
    }

    @And("user verify the following headers of dashboard page for table {string}.")
    public void verifyHeadersDashboardPage(String tableNo, DataTable dataTable) {
        List<String> expectedHeaders = dataTable.row(0);
        verifyTableHeadersForDashboard(expectedHeaders, tableNo);
    }

    @Then("user verify the login {string} message {string}.")
    public void user_verify_the_login_message(String msgType, String msg) {
        verifyLoginMessage(msgType, msg);
    }

    @And("user click on {string} button.")
    public void user_click_on_button(String string) throws IOException {
        baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
        clickOnButton(string);
    }

    @Given("User log in to {string} for {string} UI with username and password {string} and present at the home page.")
    public void user_log_in_to_admin_url_with_username_and_password_and_is_already_present_at_the_home_page(String env, String subEnv, String password) throws IOException {
        if (subEnv.equalsIgnoreCase("Manager")) {
            getTestBase().userLogin(true, env, subEnv, managerId, password);
        } else if (subEnv.equalsIgnoreCase("Agent")) {
            getTestBase().userLogin(true, env, subEnv, agentId, password);
        }
//        else getTestBase().userLogin(true, env, subEnv, managerId, password);
//        doInitialization(false, subEnv);
    }

    @Then("user will logout the current user.")
    public void user_will_logout_current_user() {
        logout();
    }

    @Then("user will {string} the {string} with count of {string} with following details and verify.")
    public void user_will_add_or_edit_module(String value, String moduleName, String moduleCount, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        int count = Integer.parseInt(moduleCount);
        for (Map<String, String> map : data) {
            addOrEditAsset(value, moduleName, count, map);
        }
    }

    @Then("user will {string} the {string} with count of {string} with following details and verify")
    public void user_will_add_or_edit_team(String value, String moduleName, String moduleCount, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        int count = Integer.parseInt(moduleCount);
        for (Map<String, String> map : data) {
            addOrEditTeam(value, moduleName, count, map);
        }
    }

    @Then("user will add the match with existing eventID with following random details and verify error {string}.")
    public void user_will_add_match_and_verify_error(String error, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : data) {
            addMatchVerifyEventError(error,map);
        }
    }

    @Then("user delete the added match and verify the event id that is associated with added match.")
    public void user_delete_added_match_and_verify_event_id(){
        deleteMatch();
    }

    @Then("user delete {string} added {string}.")
    public void user_delete_added_module(String count, String moduleName) {
        int moduleCount = Integer.parseInt(count);
        deleteModule(moduleName, moduleCount);
    }

    @Then("user will add {string} duplicate {string} with following details and verify error msg {string}.")
    public void user_will_add_or_edit_duplicate_module(String moduleCount, String moduleName, String errorMsg, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        int count = Integer.parseInt(moduleCount);
        for (Map<String, String> map : data) {
            verifyErrorMsgForDuplicateTeamName(moduleName, errorMsg, count, map);
        }
    }

    @Then("user verify the page {string}.")
    public void verify_page(String page) {
        verifyPage(page);
    }

    @Then("user verify the dates order on {string} page.")
    public void verify_match_dates_order(String page) {
        verifyDatesAreInDescendingOrder(page);
    }

    @When("Admin logs in and disables agent")
    public void admin_login_and_disable() {
        WebDriver parent = TestBase.getWebDriver();
        WebDriver admin = null;
        try {
            admin = getTestBase().getAdminDriver("chrome");
            TestBase.setWebDriver(admin);

            getTestBase().userLoginWithDifferentDriver(true, admin, "scoreboard", "admin");

            BaseUtil baseUtil = new BaseUtil(admin,
                    new WebDriverWait(admin, Duration.ofSeconds(10)));

            baseUtil.safeClick("xpath===//span[contains(text(),'Agents')]");
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            By disableBtn = By.xpath("//input[@type='checkbox']");

            new WebDriverWait(admin, Duration.ofSeconds(10))
                    .until(ExpectedConditions.elementToBeClickable(disableBtn))
                    .click();
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
        } finally {
            assert admin != null;
            admin.quit();
            TestBase.setWebDriver(parent);
        }
    }

    @Then("Agent should be logged out automatically")
    public void verify_logout() {

        TestBase.getWebDriver().navigate().refresh();

        BaseUtil baseUtil = new BaseUtil(TestBase.getWebDriver(),
                new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10)));

        By usernameTextbox = baseUtil.getLocator(
                getTestBase().getReadResources().getDataFromPropertyFile(
                        "Global", "username_textbox_locator")
        );

        new WebDriverWait(TestBase.getWebDriver(), Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(usernameTextbox));

        Assert.assertTrue(TestBase.getWebDriver().findElement(usernameTextbox).isDisplayed());
    }

    @Then("verify the {string} status of agent.")
    public void verify_agent_status(String status) {
        verifyActiveInactiveStatus(status);
    }

    @Then("update the agent status as {string}.")
    public void update_agent_status(String status) {
        try {
            updateAgentStatus(status);
        } catch (Exception e) {
            Assert.fail("unable to update match status due to " + e.getMessage());
        }

    }

    @Then("user will delete the added {string}.")
    public void user_will_delete_added_data(String data) {
        deleteData(data);
    }

    @Then("user verify the match with date filter.")
    public void user_verify_the_match_with_date_filter() {
        verifyMatchWithDateFilter();
    }

    @Then("user add tournament for past date and verify the validation message.")
    public void user_add_tournament_for_past_date_and_verify_validations() {
        verifyValidationMessageForTournamentDate();
    }


    @Then("user verify the count of the manager with match page manager dropdown.")
    public void user_verify_the_count_of_the_manager_with_match_page_manager_dropdown() {
        verifyManagerCountBetweenManagerPageAndMatchDropdown();
    }

    @Then("user verify the teams based on the gender.")
    public void user_verify_teams_based_on_the_gender() {
        verifyTeams();
    }

    @Then("user will verify validation message for user login.")
    public void userWillVerifyValidationMessageForUserLogin(DataTable dataTable) {
        List<Map<String, String>> credentials = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> data : credentials) {
            verifyLoginValidationMsg(data);
        }
    }
    @Then("user make {string} team as {string} and verify it.")
    public void user_make_match_as_and_verify_it(String count,String status) {
        updateTeamStatus(count,status);
    }

    @Then("user verify inactive team on create player popup.")
    public void user_verify_inactive_team_on_create_player_popup(){
        verifyInactiveTeamOnCreatePlayer();
    }

    @Then("user verify the eye icon on {string} creation popup.")
    public void user_verify_the_eye_icon_on_creation_popup(String module){
        verifyEyeIcon(module);
    }

    @Then("user verify country name and count player on create player popup.")
    public void user_verify_country_name_and_count_on_player_creation_popup(){
        verifyPlayerCountryNameAndCount();
    }

//    @Then("user verify the gender update for tournament.")
//    public void verify_gender_update_for_tournament(){
//
//    }

    @Then("user verify the team {string} with less then 11 player on team dropdown.")
    public void user_verify_team_on_tournament_page(String value){
        verifyTeamOnCreateTournamentPopup(Boolean.parseBoolean(value));
    }

    @Then("user verify the toggle button after refreshing the ui.")
    public void user_verify_the_toggle_button_button_after_refreshing_ui(){
        verifyToggleButton();
    }
    @Then("user will add the {string} with count of {string} with following details and verify.")
    public void add_match_special_characters(String module,String count,DataTable dataTable){
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> values : data) {
            addMatch(module, Integer.parseInt(count),values);
        }

    }

    @Then("user update the tournament status as {string}")
    public void update_tournament_status(String status){
        updateTournamentStatus(status);
    }

    @Then("user verify that tournament in create match popup.")
    public void user_verify_tournament_in_create_match_popup(){
        verifyTournamentInDropdownOnMatchCreate();
    }

    @Then("user try to update agent with existing mobile number and verify the error message {string}.")
    public void user_update_and_verify_error_message_for_already_number_exist(String msg){
        validateExistingMobileNoForAgent(msg);
    }

    @Then("user try to unassign manager on {string} page from the page and verify the message {string}.")
    public void user_try_to_unassign_manager_from_the_match_and_verify_error(String page, String msg){
        unassignManagerAndVerifyMessage(page,msg);
    }

    @Then("user verify the date filter.")
    public void user_verify_date_filter(){
        verifyDateFilter();
    }

    @Then("user verify the filter for venue.")
    public void user_verify_filter_for_venue(){

    }
}
