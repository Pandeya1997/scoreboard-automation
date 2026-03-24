@Scoreboard_Venue
Feature: Scoreboard --> Venue page
  Here we will test the functionality of venue page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_AddVenueVerification
  Scenario: Scenario_01: Add venue and verify the added venue.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user will "add" the "venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |

  @Scoreboard_EditVenueVerification
  Scenario: Scenario_02: Edit venue and verify the edited venue.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user click on "edit venue" button.
    Then user will "edit" the "venue" with count of "1" with following details and verify.
      | Country | State   | City  | Venue Image |
      | Iran    | Markazi | Karaj | venue.png   |

  @Scoreboard_OtherCityVerification
  Scenario: Scenario_03: Verify added other city name for scenario 2.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user click on "edit venue" button.
    Then user will verify manually added other city name.

  @Scoreboard_AddDuplicateVenueVerification
  Scenario: Scenario_04: Add duplicate venue and verify the added venue.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user will "add" the "venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |


  @Scoreboard_VenueHeadersVerification
  Scenario: Scenario_05: Verify headers of venue.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user verify the following headers.
      | No. | Venue Name | Country | State/Province | City | Image | Created At | Actions |

  @Scoreboard_DeleteVenueVerification
  Scenario: Scenario_06: Verify will delete the updated venue.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user delete "1" added "venue".

  @Scoreboard_ValidationErrorMsgVerification
  Scenario: Scenario_07: Verify validation message for venue.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user click on "add" button.
    Then user verify the error message for all the mandatory fields.

  @Scoreboard_DisabledContentVerification
  Scenario: Scenario_08: Verify disable content for add venue popup.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "venue module" button.
    Then user click on "add" button.
    Then user verify disable content.