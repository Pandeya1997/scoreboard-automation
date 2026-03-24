package com.Scoreboard.automation.manager.agent;

import com.scoreboard.util.CommonMethods;
import io.cucumber.java.en.Then;

public class AgentPageTest extends CommonMethods {

    AgentTest agentPage = new AgentTest();
    @Then("user add a agent with password {string} and logout {string} current user")
    public void user_add_agent_with_following_details(String pwd, String isLogout) {
        agentName = "AutomationAgent" + getRandomString();
        mobileNo = generateUnique10DigitNumber();
        agentPage.addAgent(agentName, mobileNo, pwd, Boolean.parseBoolean(isLogout));
    }
}
