package com.Scoreboard.automation.admin.match;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.sl.In;
import org.junit.Assert;

import java.io.IOException;

public class MatchPageTest {

    MatchTest matchTest = new MatchTest();

    @Then("user will search the added match.")
    public void user_will_search_the_added_match() {
        matchTest.searchAddedMatch();
    }
    @Then("user verify dashboard data for {string} match.")
    public void user_verify_dashboard_data(String day) throws IOException {
        matchTest.verifyDashBoardData(day);
    }
    @Then("user make {string} match as {string} and verify it.")
    public void user_make_match_as_and_verify_it(String count,String status) {
        matchTest.updateMatchStatus(count,status);
    }
    @Then("user verify the {string} match on live match module.")
    public void user_verify_the_match_on_live_match_module(String status) {
        matchTest.verifyMatchOnLiveMatchPage(status);
    }

    @Then("user assigned {string} manager to the added match.")
    public void user_assigned_manager_to_the_added_match(String count) {
        matchTest.assignedManagerToMatch(Integer.parseInt(count));
    }

    @Then("user verify the validation message {string}.")
    public void user_verify_the_validation_message(String msg) {
        matchTest.verifyErrorMsgForMoreThenTwoManagerAssigned(msg);
    }

//    @Then("verify added manager {string} with the count of {string} on {string} page.")
//    public void verify_added_manager_with_count(String expectedChecked, int expectedCount, String pageName) {
//        boolean expected = Boolean.parseBoolean(expectedChecked);
//        boolean result = matchTest.verifyCheckedManagers(expected, expectedCount);
//        Assert.assertTrue("Manager checkbox count mismatch on " + pageName, result);
//    }

    @Then("verify added manager {string} with the count of {string} on {string} page.")
    public void verify_added_manager_on_page(String flag, String count, String page) {
        matchTest.verifyAddedManager(Boolean.parseBoolean(flag),Integer.parseInt(count),page);
    }
    @Then("edit {string} assigned manager on {string} page.")
    public void edit_the_assigned_manager_on_page(String count,String page) {
        matchTest.editManagerForMatch(Integer.parseInt(count),page);
    }

    @Then("verify success message {string}")
    public void verify_success_message(String message){
        matchTest.verifyMessage(message);
    }
}
