package com.scoreboard.util;

import com.scoreboard.constants.PathConstants;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;

public class ParallelAgentExecutor extends CommonMethods {
    private static final Logger logger = Logger.getLogger(ParallelAgentExecutor.class);
    private static final ThreadLocal<WebDriver> AGENT_DRIVER = new ThreadLocal<>();
    Actions actions;
    WebDriver driver;

    public void executeParallelAgentsSequentialAction(
            List<String> agentUsers,
            String password, boolean toss
    ) throws InterruptedException {

        int count = agentUsers.size();
        ExecutorService executor = Executors.newFixedThreadPool(count);
        CountDownLatch latch = new CountDownLatch(count);

        WebDriver[] drivers = new WebDriver[count];

        for (int i = 0; i < count; i++) {
            final int index = i;

            executor.submit(() -> {
                try {
                    driver = createAgentDriver();
                    drivers[index] = driver;
                    TestBase.setWebDriver(driver);
                    getTestBase().agentLogin(
                            driver,
                            true,
                            "scoreboard",
                            "",
                            agentUsers.get(index),
                            password
                    );

                } finally {
                    latch.countDown();
                }
            });

        }

        latch.await(30, TimeUnit.SECONDS);
        executor.shutdown();

        for (WebDriver driver : drivers) {
            TestBase.setWebDriver(driver);

            driver.navigate().refresh();
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(5000));
            BaseUtil baseUtil = new BaseUtil(driver, wait);

            WebElement liveMatch =
                    driver.findElement(By.xpath("//span[contains(text(),'Live Matches')]"));
            liveMatch.click();

            baseUtil.explicitWait(PathConstants.WAIT_LOW);

            WebElement viewDetail =
                    driver.findElement(By.xpath("//a[contains(text(),'View Detail')]"));
            viewDetail.click();
            if (toss) {
                teamToss(driver);
            }
//            playingTeams(driver);
        }
        for (int j = 0; j < 2; j++) {
            int runValue = 0;
            int playerSelectionCount = 1;
            for (int i = 0; i < 3; i++) {
                runValue = ThreadLocalRandom.current().nextInt(0, 7);
                for (WebDriver driver : drivers) {
                    TestBase.setWebDriver(driver);
                    driver.navigate().refresh();
                    baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                    playingTeams(driver, runValue, playerSelectionCount);
                }
                playerSelectionCount++;
            }
            for (WebDriver driver : drivers) {

                logger.info("declare inning..." + logger.getName());
                TestBase.setWebDriver(driver);
                driver.navigate().refresh();

                //declare inning
                TestBase.getWebDriver().navigate().refresh();
                baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                WebElement declareBtn = driver.findElement(By.xpath("//button[contains(text(),'DECLARE INNING')]"));
                declareBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            }
        }
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        String teamAScore = drivers[0].findElement(By.xpath("(//div[@class='team_score'])[1]")).
                getText().split("/")[0];
        teamsScoreList.put("TeamAScore", teamAScore);
        String teamBScore = drivers[0].findElement(By.xpath("(//div[@class='team_score'])[2]")).
                getText().split("/")[0];
        teamsScoreList.put("TeamBScore", teamBScore);
    }


    @FunctionalInterface
    public interface RunnableWithDriver {
        void run(WebDriver driver) throws IOException;
    }

    public static WebDriver createAgentDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");

        WebDriver driver = new ChromeDriver(options);
        AGENT_DRIVER.set(driver);
        return driver;
    }

    public static WebDriver getAgentDriver() {
        return AGENT_DRIVER.get();
    }

    public void quitAgentDriver() {
        if (AGENT_DRIVER.get() != null) {
            AGENT_DRIVER.get().quit();
            AGENT_DRIVER.remove();
        }
    }

    private static void loginToAgentUI(WebDriver driver, String username, String password) {
        TestBase testBase = new TestBase();

        testBase.agentLogin(
                driver, true,
                "scoreboard",
                "agent",
                username,
                password
        );
    }

    public void teamToss(WebDriver driver) {
        TestBase testBase = new TestBase();
        logger.info("toss going on between teams " + logger.getName());
        try {
            baseUtil.explicitWait(PathConstants.WAIT_LOW);
            WebElement headToss = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "headToss")));

            WebElement teamATossSelection = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "teamATossSelection")));

            WebElement teamATossResult = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "teamATossResult")));


            //Toss selection
            headToss.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            teamATossSelection.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            teamATossResult.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            WebElement select11PlayerBtn = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "select11PlayerBtn")));

            select11PlayerBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

            WebElement saveBtn = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "saveBtn")));
            saveBtn.click();
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);


        } catch (Exception e) {
            Assert.fail("unable to toss between teams due to " + e.getMessage());
        }
    }

    public void playingTeams(WebDriver driver, int runValue, int playerSelectionCount) {
        TestBase testBase = new TestBase();
        logger.info("teams are playing... " + logger.getName());
        try {
            actions = new Actions(driver);

            //select players for batting and bowling
            if (playerSelectionCount == 1) {
                WebElement teamAViewBtn = driver.findElement(By.xpath(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "teamAViewBtn")));

                WebElement teamBViewBtn = driver.findElement(By.xpath(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "teamBViewBtn")));

                teamAViewBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                teamBViewBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }


            WebElement startBallBtn = driver.findElement(By.xpath(testBase.getReadResources().
                    getDataFromPropertyFile("Global", "startBallBtn")));

            if (playerSelectionCount == 1) {
                WebElement firstBattingPlayers = driver.findElement(By.xpath(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "firstBattingPlayers")));

                WebElement secondBattingPlayers = driver.findElement(By.xpath(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "secondBattingPlayers")));

                WebElement firstBowlingPlayers = driver.findElement(By.xpath(testBase.getReadResources().
                        getDataFromPropertyFile("Global", "firstBowlingPlayers")));

                actions.doubleClick(firstBattingPlayers).perform();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                secondBattingPlayers.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

                firstBowlingPlayers.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
            }

            WebElement runBtn = driver.findElement(
                    By.xpath(testBase.getReadResources()
                            .getDataFromPropertyFile("Global", "runBtn")
                            .replace("#run#", String.valueOf(runValue)))
            );
            boolean isRunDisabled =
                    !runBtn.isEnabled() ||
                            runBtn.getAttribute("disabled") != null;

            if (isRunDisabled) {
                startBallBtn.click();
                baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
                runBtn = driver.findElement(
                        By.xpath(testBase.getReadResources()
                                .getDataFromPropertyFile("Global", "runBtn")
                                .replace("#run#", String.valueOf(runValue)))
                );
                runBtn.click();
            } else {
                runBtn.click();
            }
            baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

        } catch (Exception e) {
            Assert.fail("unable to playing match by the teams due to " + e.getMessage());
        }
    }
}
