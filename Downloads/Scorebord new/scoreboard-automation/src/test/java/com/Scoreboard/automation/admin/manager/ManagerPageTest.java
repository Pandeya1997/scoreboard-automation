package com.Scoreboard.automation.admin.manager;

import com.scoreboard.util.CommonMethods;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;

import java.util.List;
import java.util.Map;


public class ManagerPageTest extends CommonMethods {
    ManagerPage managerPage = new ManagerPage();

    @Then("user add {string} manager with password {string} and logout {string} current user.")
    public void user_add_manager_with_following_details(String count,String pwd, String isLogout) {
        managerPage.addManager(Integer.parseInt(count), pwd, Boolean.parseBoolean(isLogout));
    }

    @Then("user verify the validation messages for manager mobile number")
    public void user_verify_validation_messages_for_manager_mobile_number() {
        managerPage.managerMobileNoValidation();
    }

    @Then("user deactivate the created manager.")
    public void user_deactivate_current_manager() {
        managerPage.deactivateManager();

    }

    @Then("verify the paginator on the {string} page.")
    public void verify_the_paginator_on_the_page(String page){
        managerPage.verifyPaginator(page);
    }

    @Then("user update the manager detail and verify.")
    public void user_update_manager_details_and_verify() {
        managerPage.updateManagerDetails();
    }

    @Then("user update the manager password.")
    public void user_update_manager_password(DataTable dataTable) {
        List<Map<String, String>> data = dataTable.asMaps(String.class, String.class);
        for (Map<String, String> map : data) {

            managerPage.updateMangerPassword(map);
        }
    }
}
