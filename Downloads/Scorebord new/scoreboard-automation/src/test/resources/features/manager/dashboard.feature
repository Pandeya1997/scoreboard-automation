@Scoreboard_Dashboard
Feature: Scoreboard --> dashboard page
  Here we will test the functionality of dashboard page tab on manager UI.
  For this, we will login with manager credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_SCOR5
  Scenario: Scenario_01: verification for SCOR-5
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Total Matches" button.
    Then user verify the page "Matches".
    Then user click on "dashboard module" button.
    Then user click on "Live Matches" button.
    Then user verify the page "Live Matches".
    Then user click on "dashboard module" button.
    Then user click on "Total Managers" button.
    Then user verify the page "Managers".
    Then user click on "dashboard module" button.
    Then user click on "Total Agents" button.
    Then user verify the page "Agents".
    Then user click on "dashboard module" button.
    Then user click on "Total teams" button.
    Then user verify the page "Teams".
    Then user click on "dashboard module" button.

  @Scoreboard_SCOR5
  Scenario: Scenario_02: verification for SCOR-5
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "dashboard module" button.
    Then user verify the following headers of dashboard page for table "1".
      | No. | Match | Date & Time | Venue |
    Then user verify the following headers of dashboard page for table "2".
      | No. | Match | Date & Time | Venue |
    Then user verify the following headers of dashboard page for table "2".
      | No. | Match | Date & Time | Venue |