package com.scoreboard.util;

import com.scoreboard.constants.PathConstants;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TestBase {
    public Logger logger = Logger.getLogger(TestBase.class);
    private static WebDriver webDriver = null;
    private final Map<String, String> readConfigData;
    private ReadResources readResources;
    private static WebDriverWait webDriverWait;
    private long timeout = PathConstants.defaultTimeout;
    private final Map<String, String> readEnvironmentUrls;
    private static TestBase instance;
    private static String env;
    private static String subEnv;
    private static String userName;
    private static String password;
    private static String envUrl, userEnvUrl;
    private String dbUrl;
    private String dbUserName;
    private String dbPassword;
    private final Map<String, String> readUserLoginCredentials;
    private final Map<String, String> readDataBaseUrls;
    private final Map<String, String> readDataBaseCredentials;
    private String chromeClearCachePage;
    private String ChromeSettingsUI;
    int loginRetries = 0;
    private long pollingFrequency = PathConstants.defaultPollingFrequency;
    String effectiveEnv = "";
    String effectiveSubEnv = "";
    private String currentEnv = "";
    protected static String currentUrl;

    BaseUtil baseUtil;

    public TestBase() {
        logger.info("TestBase constructor called!");
        readResources = new ReadResources();
        readConfigData = readResources.getValuesFromXml("Configuration.xml", "scoreboard");
        readUserLoginCredentials = readResources.getValuesFromXml("Configuration.xml", "userlogincredentials");
        readEnvironmentUrls = readResources.getValuesFromXml("Configuration.xml", "environment_url");
        readDataBaseUrls = readResources.getValuesFromXml("Configuration.xml", "databaseconfig");
        readDataBaseCredentials = readResources.getValuesFromXml("Configuration.xml", "databaseuserconfig");
        if (null == webDriver)
            if (System.getProperty("browser") == null || System.getProperty("browser").trim().isEmpty()) {
                webDriver = initializeWebDriver(readConfigData.get("browser"), subEnv);
            } else {
                webDriver = initializeWebDriver(System.getProperty("browser").trim(), subEnv);
            }
        if (null == webDriverWait)
            webDriverWait = new WebDriverWait(webDriver, Duration.ofMillis(timeout));

        PageFactory.initElements(webDriver, this);
        baseUtil = new BaseUtil(getWebDriver(), getWebDriverWait());
    }


    @FindBy(xpath = "//span[contains(text(),'Dashboard')]")
    WebElement dashboard;

    @FindBy(xpath = "//nav[contains(@class,'header navbar')]//span")
    WebElement loginRole;

    @FindBy(xpath = "(//div[contains(@class,'profile-menu')])[1]")
    WebElement logoutBtn;


    private WebDriver adminDriver;

    public WebDriver getAdminDriver(String browser) {
        if (adminDriver == null) {
            adminDriver = initializeWebDriver(browser, "admin");
        }
        return adminDriver;
    }


    public WebDriver initializeWebDriver(String browser, String role) {

        boolean isCI = isRunningInCI();

        WebDriverManager.chromedriver().setup();

        File downloadDir = new File(System.getProperty("java.io.tmpdir"),
                "selenium-downloads-" + role);

        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", downloadDir.getAbsolutePath());
        prefs.put("profile.default_content_setting_values.notifications", 2);
        prefs.put("safebrowsing.enabled", true);
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);

        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-extensions");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-infobars");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        // Headless for CI or if explicitly passed
        if (isCI || browser.equalsIgnoreCase("chrome_headless")) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080");
            options.addArguments("--ignore-certificate-errors");
        }

        WebDriver driver = new ChromeDriver(options);

        if (isCI || browser.equalsIgnoreCase("chrome_headless")) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }

        logger.info(role + " ChromeDriver launched successfully.");

        return driver;
    }

    private boolean isRunningInCI() {
        return System.getenv("CI") != null
                || System.getenv("JENKINS_HOME") != null
                || System.getenv("TF_BUILD") != null
                || System.getenv("CIRCLECI") != null;
    }


    public static TestBase getInstance() {
        if (instance == null) {
            instance = new TestBase();
        }
        return instance;
    }


    public void initializeGlobalVariables(boolean flag) {

        if (flag) {
            logger.info("Deleting All cookies.");
            webDriver.manage().deleteAllCookies();
        }
        if (effectiveEnv.equals("")) {
            env = System.getProperty("env");
            if (env == null || env.trim().isEmpty()) {
                env = readConfigData.get("env");
            }
        } else env = effectiveEnv;

        if (effectiveSubEnv.equals("")) {
            subEnv = System.getProperty("env");
            if (subEnv == null || subEnv.trim().isEmpty()) {
                subEnv = readConfigData.get("env");
            }
        }

        envUrl = System.getProperty("url");
        if (envUrl == null || envUrl.trim().isEmpty() && env.equalsIgnoreCase("scoreboard")) {
            envUrl = readEnvironmentUrls.get("url_" + env);
        }

        userEnvUrl = System.getProperty("url_user");
        if (userEnvUrl == null || userEnvUrl.trim().isEmpty() && env.equalsIgnoreCase("scoreboard")) {
            userEnvUrl = readEnvironmentUrls.get("url_user");
        }

        userName = System.getProperty("username");
        if (userName == null || userName.trim().isEmpty() || !effectiveSubEnv.equalsIgnoreCase(subEnv)) {
            userName = readUserLoginCredentials.get(effectiveSubEnv + "_userName");
        }

        password = System.getProperty("password");
        if (password == null || password.trim().isEmpty() || !effectiveSubEnv.equalsIgnoreCase(subEnv)) {
            password = readUserLoginCredentials.get(effectiveSubEnv + "_password");
        }

        dbUrl = System.getProperty("dburl");
        if (dbUrl == null || dbUrl.trim().isEmpty()) {
            dbUrl = readDataBaseCredentials.get(env + "_DBUrl");
        }

        dbUserName = System.getProperty("dbusername");
        if (dbUserName == null || dbUserName.trim().isEmpty()) {
            dbUserName = readDataBaseCredentials.get(env + "_DBUsername");
        }

        dbPassword = System.getProperty("dbpassword");
        if (dbPassword == null || dbPassword.trim().isEmpty()) {
            dbPassword = readDataBaseCredentials.get(env + "_DBPassword");
        }

        chromeClearCachePage = System.getProperty("url");
        if (chromeClearCachePage == null || chromeClearCachePage.trim().isEmpty()) {
            chromeClearCachePage = readEnvironmentUrls.get("ChromeSettingsPageClearCache");
        }

        ChromeSettingsUI = System.getProperty("url");
        if (ChromeSettingsUI == null || ChromeSettingsUI.trim().isEmpty()) {
            ChromeSettingsUI = readEnvironmentUrls.get("ChromeSettingsUI");
        }

        String eol = System.getProperty("line.separator");
        logger.info("System will initialized with following values: " + eol + "Environment : " + env + eol
                + "Environment URL : " + envUrl + eol + "Username : " + userName + eol + "Password : " + password + eol
                + "Database URL : " + dbUrl + eol + "DataBase Username : " + dbUserName + eol + "Database Password : "
                + dbPassword + eol);
    }

    public void userLogin(boolean clearCookiesFlag, String... loginData) {
        getWebDriver().navigate().refresh();
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofMillis(timeout));

        // Parse input arguments safely
        String providedEnv = getValueSafely(loginData, 0);
        String providedSubEnv = getValueSafely(loginData, 1);
        String providedUsername = getValueSafely(loginData, 2);
        String providedPassword = getValueSafely(loginData, 3);
        String loginPage = getValueSafely(loginData, 4);

        // Use defaults if not provided
        effectiveEnv = !providedEnv.isBlank() ? providedEnv.toLowerCase() : readConfigData.get("env").trim().toLowerCase();
        effectiveSubEnv = !providedSubEnv.isBlank() ? providedSubEnv.toLowerCase() : readConfigData.get("subenv").trim().toLowerCase();

        // Clear cookies if env changes
        if (!effectiveEnv.equalsIgnoreCase(currentEnv)) {
            clearCookiesFlag = true;
        }

        try {
            currentUrl = webDriver.getCurrentUrl();
            logger.info("Current URL: {}" + currentUrl);

            if (!"data:,".equals(currentUrl) && effectiveEnv.equalsIgnoreCase(currentEnv) && !baseUtil.isElementDisplayed(
                    getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"))
                    && readEnvironmentUrls.get("url_scoreboard").equals(currentUrl) && subEnv.equalsIgnoreCase("admin")) {
                handleLoggedInState(clearCookiesFlag, effectiveEnv, providedUsername, providedPassword);
                return;
            }

            currentEnv = effectiveEnv;
            if (effectiveEnv.equals("scoreboard")) {
                login(clearCookiesFlag, providedUsername, providedPassword, loginPage);
            } else {
                throw new IllegalArgumentException("Unsupported env: " + effectiveEnv);
            }


        } catch (WebDriverException | IOException e) {
            logger.error("Login failed, retrying …", e);
            if (++loginRetries < 2) {
                userLogin(clearCookiesFlag, effectiveEnv, effectiveSubEnv, providedUsername, providedPassword);
            }
        }
    }

    public void verifyLoginPage() {
        baseUtil.isElementDisplayed(getReadResources().
                getDataFromPropertyFile("Global", "loginForm"));
    }

    public void userLoginWithDifferentDriver(boolean clearCookiesFlag, WebDriver driver, String... loginData) {
        driver.navigate().refresh();
        BaseUtil baseUtil = new BaseUtil(driver, getWebDriverWait());
        baseUtil.explicitWait(PathConstants.WAIT_LOW);
        webDriverWait = new WebDriverWait(webDriver, Duration.ofMillis(timeout));

        // Parse input arguments safely
        String providedEnv = getValueSafely(loginData, 0);
        String providedSubEnv = getValueSafely(loginData, 1);
        String providedUsername = getValueSafely(loginData, 2);
        String providedPassword = getValueSafely(loginData, 3);

        // Use defaults if not provided
        effectiveEnv = !providedEnv.isBlank() ? providedEnv.toLowerCase() : readConfigData.get("env").trim().toLowerCase();
        effectiveSubEnv = !providedSubEnv.isBlank() ? providedSubEnv.toLowerCase() : readConfigData.get("subenv").trim().toLowerCase();

        // Clear cookies if env changes
        if (!effectiveEnv.equalsIgnoreCase(currentEnv)) {
            clearCookiesFlag = true;
        }

        try {
            currentUrl = webDriver.getCurrentUrl();
            logger.info("Current URL: {}" + currentUrl);

            if (!"data:,".equals(currentUrl) && effectiveEnv.equalsIgnoreCase(currentEnv) && !baseUtil.isElementDisplayed(
                    getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"))
                    && readEnvironmentUrls.get("url_scoreboard").equals(currentUrl) && subEnv.equalsIgnoreCase("admin")) {
                handleLoggedInState(clearCookiesFlag, effectiveEnv, providedUsername, providedPassword);
                return;
            }
//            if(effectiveEnv.equalsIgnoreCase(currentEnv) && baseUtil.isElementDisplayed(
//                    getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"))){
//                if (effectiveEnv.equals("scoreboard")) {
//                    login(clearCookiesFlag, providedUsername, providedPassword);
//                } else {
//                    throw new IllegalArgumentException("Unsupported env: " + effectiveEnv);
//                }
//            }

            currentEnv = effectiveEnv;

            if (effectiveEnv.equals("scoreboard")) {
                login(clearCookiesFlag, driver, providedUsername, providedPassword);
            } else {
                throw new IllegalArgumentException("Unsupported env: " + effectiveEnv);
            }

        } catch (WebDriverException | IOException e) {
            logger.error("Login failed, retrying …", e);
            if (++loginRetries < 2) {
                userLoginWithDifferentDriver(clearCookiesFlag, driver, effectiveEnv, effectiveSubEnv, providedUsername, providedPassword);
            }
        }
    }

    public void agentLogin(WebDriver driver, boolean clearCookiesFlag, String... loginData) {

        String providedEnv = getValueSafely(loginData, 0);
        String providedUsername = getValueSafely(loginData, 2);
        String providedPassword = getValueSafely(loginData, 3);

        String env = !providedEnv.isBlank()
                ? providedEnv.toLowerCase()
                : readConfigData.get("env").trim().toLowerCase();

        String url = readEnvironmentUrls.get("url_" + env);

        try {
            driver.manage().deleteAllCookies();
            driver.get(url);

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(timeout));
            BaseUtil baseUtil = new BaseUtil(driver, wait);

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            baseUtil.getLocator(
                                    getReadResources().getDataFromPropertyFile(
                                            "Global",
                                            "username_textbox_locator"
                                    )
                            )
                    )
            );

            baseUtil.enterText(
                    getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"),
                    providedUsername
            );

            baseUtil.enterText(
                    getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator"),
                    providedPassword
            );

            baseUtil.safeClick(
                    getReadResources().getDataFromPropertyFile("Global", "loginBtn_locator")
            );

            wait.until(ExpectedConditions.visibilityOf(dashboard));
            baseUtil.explicitWait(PathConstants.WAIT_HIGH);

        } catch (Exception e) {
            logger.error("Agent login failed for user: {}" + providedUsername, e);
            throw new RuntimeException("Agent login failed", e);
        }
    }


    private void login(boolean clearCookiesFlag, String... cred) throws IOException {
        BaseUtil baseUtil = new BaseUtil(webDriver, getWebDriverWait());
        boolean isLoginPage = Boolean.parseBoolean(getValueSafely(cred, 2));
        initializeGlobalVariables(clearCookiesFlag);
        subEnv = effectiveSubEnv;
        if (!Objects.equals(subEnv, "user")) {
            webDriver.get(envUrl);
            if (baseUtil.isElementDisplayed(loginRole)) {
                if (!loginRole.getText().split("-")[0].trim().equalsIgnoreCase(subEnv)) {
                    logger.info("SubEnv mismatch. Logging out...");
                    baseUtil.click(logoutBtn);
                    baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                }
            }
            if (baseUtil.isElementDisplayed(dashboard)) {
                new Actions(webDriver).keyDown(Keys.CONTROL).sendKeys(Keys.F5).keyUp(Keys.CONTROL).perform();
                return;
            }

            BaseUtil base = new BaseUtil(webDriver, webDriverWait);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                    baseUtil.getLocator(getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"))
            ));
            if (!isLoginPage) {

                String enteredUsername = (cred.length > 0 && !cred[0].isBlank()) ? cred[0] : userName;
                String enteredPassword = (cred.length > 1 && !cred[1].isBlank()) ? cred[1] : password;

                base.enterText(getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"), enteredUsername);
                base.enterText(getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator"), enteredPassword);
                base.safeClick(getReadResources().getDataFromPropertyFile("Global", "loginBtn_locator"));
            }
        } else {
            String usersUrl = userEnvUrl + "/" + CommonMethods.getMatchEventId();
            webDriver.get(usersUrl);
        }

//        webDriverWait.until(ExpectedConditions.visibilityOf(dashboard));
    }

    private void login(boolean clearCookiesFlag, WebDriver driver, String... cred) throws IOException {
        BaseUtil baseUtil = new BaseUtil(driver, getWebDriverWait());
        initializeGlobalVariables(clearCookiesFlag);
        subEnv = effectiveSubEnv;
        if (!Objects.equals(subEnv, "user")) {
            driver.get(envUrl);
            if (baseUtil.isElementDisplayed(loginRole)) {
                if (!loginRole.getText().split("-")[0].trim().equalsIgnoreCase(subEnv)) {
                    logger.info("SubEnv mismatch. Logging out...");
                    baseUtil.click(logoutBtn);
                    baseUtil.explicitWait(PathConstants.WAIT_MEDIUM);
                }
            } else if (baseUtil.isElementDisplayed(dashboard)) {
                new Actions(webDriver).keyDown(Keys.CONTROL).sendKeys(Keys.F5).keyUp(Keys.CONTROL).perform();
                return;
            }

            BaseUtil base = new BaseUtil(driver, webDriverWait);
            webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(
                    baseUtil.getLocator(getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"))
            ));

            String enteredUsername = (cred.length > 0 && !cred[0].isBlank()) ? cred[0] : userName;
            String enteredPassword = (cred.length > 1 && !cred[1].isBlank()) ? cred[1] : password;

            base.enterText(getReadResources().getDataFromPropertyFile("Global", "username_textbox_locator"), enteredUsername);
            base.enterText(getReadResources().getDataFromPropertyFile("Global", "password_textbox_locator"), enteredPassword);
            base.safeClick(getReadResources().getDataFromPropertyFile("Global", "loginBtn_locator"));
        } else {
            String usersUrl = userEnvUrl + "/" + CommonMethods.getMatchEventId();
            driver.get(usersUrl);
        }

//        webDriverWait.until(ExpectedConditions.visibilityOf(dashboard));
    }

    private String getValueSafely(String[] arr, int index) {
        return (arr != null && arr.length > index && arr[index] != null) ? arr[index].trim() : "";
    }


    private void handleLoggedInState(boolean clearCookiesFlag, String envType, String providedUsername, String providedPassword) throws IOException {
        initializeGlobalVariables(clearCookiesFlag);
        logger.info("Already logged in, refreshing …");
        webDriver.navigate().refresh();
        webDriverWait.until(ExpectedConditions.visibilityOf(dashboard));
    }


    public byte[] takeScreenshot() {
        return ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
    }

    public void logout() {
        BaseUtil base = new BaseUtil(webDriver, webDriverWait);
        try {
            base.safeClick(getReadResources().getDataFromPropertyFile("Global", "logoutBtn_locator"));
            base.explicitWait(PathConstants.WAIT_VERY_LOW);
            base.safeClick(getReadResources().getDataFromPropertyFile("Global", "logoutBtn_locator"));
            base.explicitWait(PathConstants.WAIT_MEDIUM);
        } catch (Exception e) {
            logger.error("Unable to logout due to :: " + e);
        }
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    public static void setWebDriver(WebDriver webDriverr) {
        webDriver = webDriverr;
    }

    public ReadResources getReadResources() {
        return readResources;
    }

    public void setReadResources(ReadResources readResources) {
        this.readResources = readResources;
    }

    public Map<String, String> getReadConfigData() {
        return readConfigData;
    }

    public Map<String, String> getReadDatabaseUrls() {
        return readDataBaseUrls;
    }

    public Map<String, String> getReadDataBaseCredentials() {
        return readDataBaseCredentials;
    }

    public WebDriverWait getWebDriverWait() {
        return webDriverWait;
    }

    public void setWebDriverWait(WebDriverWait webDriverWaitt) {
        webDriverWait = webDriverWaitt;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public long getPollingFrequency() {
        return pollingFrequency;
    }

    public void setPollingFrequency(long pollingFrequency) {
        this.pollingFrequency = pollingFrequency;
    }

    public static String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public static String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static String getEnvUrl() {
        return envUrl;
    }

    public static String getSubEnv() {
        return subEnv;
    }

    public String getAPIEndpointUrl() {
        return envUrl.substring(0, envUrl.lastIndexOf("/"));
    }

    public void setEnvUrl(String envUrl) {
        this.envUrl = envUrl;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUsername() {
        return dbUserName;
    }

    public void setDbUsername(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Map<String, String> getReadEnvironmentUrls() {
        return readEnvironmentUrls;
    }

    public String getChromeClearCachePage() {
        return chromeClearCachePage;
    }

    public String getChromeSettingsUI() {
        return ChromeSettingsUI;
    }

    public static String getCurrentUrl() {
        return currentUrl;
    }

    public static String getUsername() {
        return userName;
    }

    public static String getUsersURL() {
        return userEnvUrl;
    }

}
