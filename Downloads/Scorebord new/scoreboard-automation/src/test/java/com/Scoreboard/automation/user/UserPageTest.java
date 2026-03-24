package com.Scoreboard.automation.user;

import io.cucumber.java.en.Then;

public class UserPageTest {

    UserPage userPage = new UserPage();

    @Then("user verify the {string} match on the user panel.")
    public void user_append_match_event_id(String matchData){
        userPage.verifyMatchOnUsersUI(matchData);
    }

    @Then("verify the match data on the users UI.")
    public void verify_match_data(){
        userPage.verifyUIDataOnUsersPanelWithCreatedMatch();
    }

    @Then("user verify the user UI for {string} eventID.")
    public void user_verify_the_user_ui_for_invalid_eventId(String value){
        userPage.verifyUserUI(value);
    }
}
