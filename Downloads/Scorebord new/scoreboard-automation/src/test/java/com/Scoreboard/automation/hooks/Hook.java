package com.Scoreboard.automation.hooks;

import com.scoreboard.util.TestBase;
import io.cucumber.java.After;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class Hook extends TestBase {

    @After
    public void tearDown(Scenario scenario) {

        if (scenario.isFailed()) {
            byte[] screenshot = ((TakesScreenshot) getWebDriver())
                    .getScreenshotAs(OutputType.BYTES);

            scenario.attach(screenshot, "image/png", "Failure Screenshot");
        }
    }
}
