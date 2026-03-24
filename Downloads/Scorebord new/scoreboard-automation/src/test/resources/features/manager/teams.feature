@Scoreboard_Team
Feature: Scoreboard --> Team page
  Here we will test the functionality of team page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_AddTeamVerification
  Scenario: Scenario_01: Add team and verify the added team.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "teams" with count of "1" with following details and verify
      | Gender |
      | Men    |
      | Women  |

  @Scoreboard_AddDuplicateTeamVerification
  Scenario: Scenario_02: Add duplicate team and verify the added team.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will add "1" duplicate "teams" with following details and verify error msg "Failed To Create Team".
      | Gender |
      | Men    |
      | Women  |

  @Scoreboard_EditTeamVerification
  Scenario: Scenario_03: Edit the added team and verify it
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "edit" the "teams" with count of "1" with following details and verify.
      | Gender |
      | Men    |
      | Women  |

  @Scoreboard_EditTeamVerification
  Scenario: Scenario_04: Header verification for the them page.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user verify the following headers.
      | No. | Team Name | Logo | Gender | Created At | Status | Actions |
