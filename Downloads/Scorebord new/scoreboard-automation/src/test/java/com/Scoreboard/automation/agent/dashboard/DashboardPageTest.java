package com.Scoreboard.automation.agent.dashboard;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;

public class DashboardPageTest {

    DashboardTest dashboardTest = new DashboardTest();

    @Then("verify the dashboard data as following.")
    public void verify_the_dashboard_data(DataTable dataTable){
        Map<String, String> expectedData =
                dataTable.asMaps(String.class, String.class).get(0);

        dashboardTest.verifyDashboardCounts(expectedData);
    }

    @Then("user assign manager or agent to the added match with match count {string} and logout {string}.")
    public void user_assign_manager_to_the_added_match_with_match_count(String matchCount,String flag){
        boolean bool = Boolean.parseBoolean(flag);
        dashboardTest.assignedManagerToMatch(Integer.parseInt(matchCount),bool);
    }

    @Then("user will add the {string} with following details and verify.")
    public void user_will_add_or_edit_module( String module, DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : data) {
            dashboardTest.addTournament(map);
        }
    }
}
