package com.Scoreboard.automation.admin.deleteResources;

import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import io.cucumber.java.en.Then;

public class DeleteResource extends CommonMethods {
    @Then("delete all the resources in the end of the automation scripts")
    public void delete_all_the_resources_in_the_end_of_the_automation_script() {
        if (null != TestBase.getWebDriver()) {
            TestBase.getWebDriver().quit();
        }
    }
}
