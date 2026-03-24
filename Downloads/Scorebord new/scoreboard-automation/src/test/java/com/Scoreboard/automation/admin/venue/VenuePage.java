package com.Scoreboard.automation.admin.venue;

import com.scoreboard.constants.PathConstants;
import com.scoreboard.util.BaseUtil;
import com.scoreboard.util.CommonMethods;
import com.scoreboard.util.TestBase;
import org.junit.Assert;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.io.IOException;
import java.util.List;

public class VenuePage extends CommonMethods {
    BaseUtil baseUtil = new BaseUtil(TestBase.getInstance().getWebDriver(), TestBase.getInstance().getWebDriverWait());

    VenuePage() {
        PageFactory.initElements(TestBase.getInstance().getWebDriver(), this);
    }

    @FindBy(xpath = "//button[contains(text(),'Add')]")
    WebElement addBtn;

    @FindBy(xpath = "//form/div/label[contains(text(),'Venue Name')]/following-sibling::input")
    WebElement venueNameTextBox;

    @FindBy(id = "react-select-2-input")
    WebElement countryDropdown;

    @FindBy(id = "react-select-3-input")
    WebElement venueStateDropdown;

    @FindBy(id = "react-select-4-input")
    WebElement venueCityDropdown;

    @FindBy(xpath = "//form/div/label[contains(text(),'Venue Image')]/following-sibling::div")
    WebElement venueImage;

    @FindBy(xpath = "//form//button[contains(text(),'Add')]")
    WebElement formAddBtn;

    @FindBy(xpath = "//div[contains(text(),'Venue name is required')]")
    WebElement errorMessageVenueName;

    @FindBy(xpath = "//div[contains(text(),'Country is required')]")
    WebElement errorMessageVenueCountry;

    @FindBy(xpath = "//div[contains(text(),'State is required')]")
    WebElement errorMessageVenueState;

    @FindBy(xpath = "//div[contains(text(),'City is required')]")
    WebElement errorMessageVenueCity;

    @FindBy(xpath = "//div[contains(text(),'Image is required')]")
    WebElement errorMessageVenueImage;

    @FindBy(xpath = "//div[contains(text(),'Venue name cannot exceed 60 characters.')]")
    WebElement errorMessageVenueMaxLenName;


    public void verifyAddedOtherCityNameManually() throws IOException {
        venueCityDropdown.click();
        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        assertElement("cityNameFromDropdown",otherCityName);
    }

    public void verifyErrorMessageForVenueFields() {
        //Max Length Venue Name
        baseUtil.enterText(venueNameTextBox, getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "maxLength"));

        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueMaxLenName), getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "nameWithMaxLength"));

        venueNameTextBox.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);
        baseUtil.click(formAddBtn);

        //Empty venue name
        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueName), getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "nameField"));

        //empty image field
        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueImage), getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "imageField"));

        //empty country filed
        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueCountry), getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "countryField"));

        //empty state field
        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueState), getTestBase().getReadResources().
                getDataFromPropertyFile("Global", "stateField"));

        //empty city field
        Assert.assertEquals(baseUtil.getElementText(errorMessageVenueCity),getTestBase().getReadResources().
                getDataFromPropertyFile("Global","cityField"));


    }
    public void verifyDisableContent() {
        String xpath = "xpath===//div[@class='select__control select__control--is-disabled css-1de4n0f-control']";
        List<WebElement> elements = baseUtil.getMultipleElementsAfterLoaded(xpath);

        for (WebElement element : elements) {
            // Verify element is disabled
            Assert.assertFalse("Element should be disabled", baseUtil.isElementEnable(element));
            Assert.assertFalse("Element should not be clickable", baseUtil.checkElementClickable(element));
        }

        countryDropdown.click();
        countryDropdown.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        countryDropdown.sendKeys("India");
        countryDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

        venueStateDropdown.click();
        venueStateDropdown.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.DELETE);
        venueStateDropdown.sendKeys("Rajasthan");
        venueStateDropdown.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        baseUtil.explicitWait(PathConstants.WAIT_VERY_LOW);

        for (WebElement element : elements) {
            Assert.assertTrue("Element should not be enabled", baseUtil.isElementEnable(element));
            Assert.assertTrue("Element should not be clickable", baseUtil.checkElementClickable(element));
        }
    }

}
