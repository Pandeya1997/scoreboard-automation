package com.Scoreboard.automation.admin.venue;

import com.scoreboard.util.CommonMethods;
import io.cucumber.java.en.Then;

import java.io.IOException;

public class VenuePageTest extends CommonMethods {
    VenuePage venuePage= new VenuePage();
    @Then("user will verify manually added other city name.")
    public void user_will_verify_added_other_city_name() throws IOException {
        venuePage.verifyAddedOtherCityNameManually();
    }

    @Then("user verify the error message for all the mandatory fields.")
    public void verify_error_message(){
        venuePage.verifyErrorMessageForVenueFields();
    }

    @Then("user verify disable content.")
    public void user_verify_disable_content(){
        venuePage.verifyDisableContent();
    }
}
