package com.Scoreboard.automation.agent.matches;

import io.cucumber.java.en.Then;

public class MatchesPageTest {
    MatchesTest matchesTest = new MatchesTest();

    @Then("user assign agent to the match.")
    public void user_assign_agent_to_the_match(){
        matchesTest.searchAddedMatch();
        matchesTest.assignAgent();
    }

    @Then("verify the added match as {string} on the {string} UI.")
    public void verify_the_added_match_to_the_role(String role,String visiblity){
        boolean bool= Boolean.parseBoolean(visiblity);
        matchesTest.verifyMatch(bool);
    }
}
