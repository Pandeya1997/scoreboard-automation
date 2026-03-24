package com.Scoreboard.automation.admin.agent;

import com.scoreboard.util.CommonMethods;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class AgentPageTest extends CommonMethods {
    AgentPage agentPage = new AgentPage();


    @Then("user add a agent with password {string} and logout {string} current user.")
    public void user_add_agent_with_following_details(String pwd, String isLogout) {
        agentName = "AutomationAgent" + getRandomString();
        mobileNo = generateUnique10DigitNumber();
        agentPage.addAgent(agentName, mobileNo, pwd, Boolean.parseBoolean(isLogout));
    }

    @Then("user deactivate the created agent.")
    public void user_deactivate_current_agent() {
        agentPage.deactivateAgent();

    }

    @Then("user update the agent detail and verify.")
    public void user_update_agent_details_and_verify() {
        agentPage.updateAgentDetails();
    }

    @Then("user update the agent password.")
    public void user_update_agent_password(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : data) {
            agentPage.updateMangerPassword(map);
        }
    }

    @Given("User already present at the login page")
    public void user_already_present_at_the_home_page() {
        getTestBase().userLogin(true, "scoreboard", "admin", "admin", "adminPass", "true");
    }
}
