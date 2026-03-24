package com.Scoreboard.automation.manager.matches;

import io.cucumber.java.en.Then;

import java.io.IOException;

public class MatchesPageTest {

    MatchesPage matchesPage = new MatchesPage();
    @Then("verify the created match by first manager for the second manager.")
    public void verify_match_on_manager_UI() throws IOException {
        matchesPage.verifyMatch();
    }

    @Then("user verify the assigned manager to the created match.")
    public void user_verify_assigned_manager_to_the_created_match(){
        matchesPage.verifyAssignedManagerToMatch();
    }
}
