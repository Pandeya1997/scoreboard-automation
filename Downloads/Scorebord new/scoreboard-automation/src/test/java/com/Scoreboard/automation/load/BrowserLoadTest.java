package com.Scoreboard.automation.load;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BrowserLoadTest {
    private static final String URL = "https://scorebdevlopuser-ui.jaigovinda7.com/122002";
    private static final int BROWSER_COUNT = 500;
    private static final int HOLD_TIME_MS = 30000;

    public static void main(String[] args) {

        ExecutorService executor = Executors.newFixedThreadPool(BROWSER_COUNT);

        for (int i = 1; i <= BROWSER_COUNT; i++) {
            final int browserNo = i;

            executor.submit(() -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-extensions");
                options.addArguments("--disable-infobars");
                options.addArguments("--start-maximized");
                options.addArguments("--headless");
//                options.addArguments("--blink-settings=imagesEnabled=false");

                WebDriver driver = new ChromeDriver(options);

                try {
                    System.out.println("Opening browser " + browserNo);
                    driver.get(URL);

                    Thread.sleep(HOLD_TIME_MS);

                } catch (Exception e) {
                    System.err.println("Browser " + browserNo + " failed");
                } finally {
                    driver.quit();
                    System.out.println("Closed browser " + browserNo);
                }
            });
        }

        executor.shutdown();
    }
}
