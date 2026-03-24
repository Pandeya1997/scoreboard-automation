@Scoreboard_Tournaments
Feature: Scoreboard --> Tournaments page
  Here we will test the functionality of Tournaments page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_TournamentHeadersVerification
  Scenario: Scenario_01: Verify headers of tournament.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "tournaments module" button.
    Then user verify the following headers.
      | No. | Tournament Name | Gender Type | Created At | Format Type | Tournament Type | Start At | End At | Status | Actions |

  @Scoreboard_AddTournamentE2EVerification
  Scenario: Scenario_02: Add tournament and verify the added tournament.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "2" with following details and verify.
      | Gender |
      | Men    |
    Then user click on "players module" button.
    Then user will add "11" players as "old" for "2" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
      | player.jpg   | 01-01-1997 | India   | Rajasthan | Jaipur | Men    | Batsman | 3      |
#    Then user click on "teams module" button.
#    Then user verify the active or inactive players for the match with following data.
#      | Active | Inactive |
#      | 11     | 0        |
#    Then user click on "players module" button.
#    Then make "1" players as "inactive".
#    Then user click on "teams module" button.
#    Then user verify the active or inactive players for the match with following data.
#      | Active | Inactive |
#      | 10     | 1        |
#    Then user click on "players module" button.
#    Then make "1" players as "active".
#    Then user click on "teams module" button.
#    Then user verify the active or inactive players for the match with following data.
#      | Active | Inactive |
#      | 11     | 0        |
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 1          | 5        |
#    Then verify dashboard data for tournament for "2" teams.


  @Scoreboard_TournamentsScheduleVerification
  Scenario: Scenario_03: Add schedule tournament and verify the added tournament schedule.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "schedule" button.
    Then user click on "add schedule" button.
    Then user click on "submit" button.
    Then verify the validation message "Schedule Name Is Required".
    Then user will "Add" a schedule and verify it.
    Then user will "delete" a schedule and verify it.
    Then user will "Add" a schedule and verify it.

  @Scoreboard_TournamentsAddPlayerVerification
  Scenario: Scenario_04: Add players to the tournament and verify the added players.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "select player" button.
    Then user click on "Save Selection" button.
    Then verify the validation message "Each Team Must Have A Minimum Of 11 And A Maximum Of 25 Active Players.".
    Then user add "11" players for team1 and "11" players for team2 and verify.

  @Scoreboard_DisableAndEnableTournamentsVerification
  Scenario: Scenario_05: Enable and disable the tournament and verify it on create match popup.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user "inactive" the tournament with count of "1".
    Then user verify "disabled" tournament for a new match.
    Then user will search the added tournament.
    Then user "active" the tournament with count of "1".
    Then user verify "enabled" tournament for a new match.

  @Scoreboard_DisableAndEnablePlayerForTheTournamentsVerification
  Scenario: Scenario_06: Enable and disable the players and verify it on tournament select player popup.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Players module" button.
    Then user will search the added player with count of "1" and make it as "inactive".
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "Select player" button.
    Then user verify the "inactive" player on tournament select player window.
    Then user click on "Players module" button.
    Then user will search the added player with count of "1" and make it as "active".
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "Select player" button.
    Then user verify the "active" player on tournament select player window.

  @Scoreboard_EditTournamentsVerification
  Scenario: Scenario_07: Edit the tournament and verify the edited tournament.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user will "edit" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country       | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 10             | India&Algeria | Men         | ODI         | International   | 3          | 7 Days   |

  @Scoreboard_SCOR52
  Scenario: Scenario_08: Inactive team verification in team dropdown of create tournament popup.
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "2" with following details and verify.
      | Gender |
      | Men    |
    Then user click on "players module" button.
    Then user will add "1" players as "old" for "1" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
      | player.jpg   | 01-01-1997 | India   | Rajasthan | Jaipur | Men    | Batsman | 3      |
    Then user make "player" as "inactive".
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user verify the inactive player should not be visible on the players dropdown.

  @Scoreboard_SCOR60
  Scenario: Scenario_09: Add tournament and verify the added tournament for past date.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "tournaments module" button.
    Then user add tournament for past date and verify the validation message.

  @Scoreboard_SCOR76
  Scenario: Scenario_10: verify the tournament dates descending order
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "tournament module" button.
    Then user verify the dates order on "tournament" page.

#  @Scoreboard_SCOR148
#  Scenario: Scenario_11: Verify tournament gender update.
#    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
#    Then user click on "teams module" button.
#    Then user will "add" the "Teams" with count of "2" with following details and verify.
#      | Gender |
#      | Men    |
#    Then user click on "players module" button.
#    Then user will add "11" players as "old" for "2" team with following details and verify them.
#      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
#      | player.jpg   | 01-01-1997 | India   | Rajasthan | Jaipur | Men    | Batsman | 3      |
#    Then user click on "venue module" button.
#    Then user will "add" the "Venue" with count of "1" with following details and verify.
#      | Country | State     | City   | Venue Image |
#      | India   | Rajasthan | jaipur | venue.png   |
#    Then user click on "tournaments module" button.
#    Then user will "add" the "tournament" with count of "1" with following details and verify.
#      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
#      | 5              | India&Pakistan | Men         | T20         | Domestic        | 1          | 5        |
#    Then user verify the gender update for tournament.


#  @Scoreboard_SCOR546
#  Scenario: Scenario_12: Verify the limitation of adding player on tournament page. #max25 player per team
#    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
#    Then user click on "teams module" button.
#    Then user will "add" the "Teams" with count of "2" with following details and verify.
#      | Gender |
#      | Men    |
#    Then user click on "players module" button.
#    Then user will add "30" players as "old" for "2" team with following details and verify them.
#      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
#      | player.jpg   | 01-01-1997 | India   | Rajasthan | Jaipur | Men    | Batsman | 3      |
#    Then user click on "venue module" button.
#    Then user will "add" the "Venue" with count of "1" with following details and verify.
#      | Country | State     | City   | Venue Image |
#      | India   | Rajasthan | jaipur | venue.png   |
#    Then user click on "tournaments module" button.
#    Then user will "add" the "tournament" with count of "1" with following details and verify.
#      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
#      | 5              | India&Pakistan | Men         | T20         | Domestic        | 1          | 5        |
#    Then user click on "select player" button.
#    Then user click on "Save Selection" button.
#    Then verify the validation message "Each Team Must Have A Minimum Of 11 And A Maximum Of 25 Active Players.".
#    Then user add "26" players for team1 and "26" players for team2 and verify.

  @Scoreboard_SCOR-426
  Scenario: Scenario_13: Date range filter not working on Tournament module
    Given User already log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "Tournaments module" button.
    Then user click on "filter" button.
    Then user verify the date filter.