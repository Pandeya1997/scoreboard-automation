package com.Scoreboard.automation.admin.matchHistory;

import io.cucumber.java.en.Then;

public class MatchHistoryPageTest {
    MatchHistoryPage matchHistoryPage = new MatchHistoryPage();
    @Then("user verify the manager from filter dropdown.")
    public void user_verify_manager_from_filter(){
        matchHistoryPage.verifyManagerFromFilter();
    }
//
//    @Then("update match score by agent UI.")
//    public void update_match_score_by_agent_UI(){
//
//    }

    @Then("user update the result on the match page.")
    public void update_the_match_result(){
        matchHistoryPage.updateMatchResult();
    }

    @Then("user verify the match history for the completed match.")
    public void user_verify_the_match_history_for_the_completed_match(){
        matchHistoryPage.verifyMatchOnMatchHistoryPage();
    }

    @Then("User login parallel 2 agent UI and perform same action one by one on both agent UI with toss {string}")
    public void parallel_agent_login_and_action(String toss) throws InterruptedException {
        boolean bool = Boolean.parseBoolean(toss);
        matchHistoryPage.performMatchActionOnAgentUI(bool);
    }
}
