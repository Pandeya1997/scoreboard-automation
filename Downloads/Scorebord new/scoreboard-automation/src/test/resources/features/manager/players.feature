@Scoreboard_Players
Feature: Scoreboard --> Players page
  Here we will test the functionality of players page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_AddPlayerVerification
  Scenario: Scenario_01: Add player and verify the added player.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "1" with following details and verify.
      | Gender |
      | Men    |
    Then user click on "players module" button.
    Then user will add "1" players as "new" for "1" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
      | player.jpg   | 01-01-1997 | India   | Rajasthan | jaipur | Men    | Batsman | 3      |

  @Scoreboard_EditPlayerVerification
  Scenario: Scenario_02: edit player and verify the edited player.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "1" with following details and verify.
      | Gender |
      | Women  |
    Then user click on "players module" button.
    Then user search already added player.
    Then user will edit "1" players for "1" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role   | Skills |
      | player.jpg   | 03-02-1997 | India   | Rajasthan | jaipur | Women  | Bowler | 4      |

  @Scoreboard_PlayerHeaderVerification
  Scenario: Scenario_3: verify the player page headers.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "players module" button.
    Then user verify the following headers.
      | No. | Name | Player Image | Date Of Birth | Country | State/Province | City | Gender | Created At | Status | Actions |

  @Scoreboard_DeletePlayerVerification
  Scenario: Scenario_4: delete player and verify the deleted player.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "players module" button.
    Then user search already added player.
    Then user delete the added player.

  @Scoreboard_SCOR35
  Scenario: Scenario_05: verification of the player date descending order.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "players module" button.
    Then user verify the dates order on "player" page.

  @Scoreboard_SCOR38
  Scenario: Scenario_06: delete a player and verify it.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "1" with following details and verify.
      | Gender |
      | Men    |
    Then user click on "players module" button.
    Then user will add "1" players as "new" for "1" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
      | player.jpg   | 01-01-1997 | India   | Rajasthan | jaipur | Men    | Batsman | 3      |
#    Then user search already added player.
    Then user will delete the added "players".

  @Scoreboard_SCOR114
  Scenario: Scenario_07: verify country name and count on player creation popup.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "players module" button.
    Then user verify country name and count player on create player popup.