@Scoreboard_Users
Feature: Scoreboard --> User panel
  Here we will test the functionality of user UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_LiveMatchVerificationOnUserPanel
  Scenario: Scenario_01: Verify the match on the user panel
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "teams module" button.
    Then user will "add" the "Teams" with count of "2" with following details and verify.
      | Gender |
      | Men    |
    Then user click on "players module" button.
    Then user will add "11" players as "old" for "2" team with following details and verify them.
      | Player Image | Player DOB | Country | State     | City   | Gender | Role    | Skills |
      | player.jpg   | 01-01-1997 | India   | Rajasthan | jaipur | Men    | Batsman | 3      |
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
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "schedule" button.
    Then user click on "add schedule" button.
    Then user will "Add" a schedule and verify it.
    Then user will search the added tournament.
    Then user click on "select player" button.
    Then user add "11" players for team1 and "11" players for team2 and verify.
    Then user click on "matches module" button.
    Then user will "add" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time   |
      | 1          | current time |
    Then user make "1" match as "live" and verify it.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "matches module" button.
    Then user assigned "1" manager to the added match.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "user" UI and present at the home page.
    Then user verify the "live" match on the user panel.


  @Scoreboard_NonLiveMatchVerificationOnUserPanel
  Scenario: Scenario_02: Verify the match on the user panel
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will "add" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time   |
      | 2          | current time |
#    Then user make "1" match as "live" and verify it.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "matches module" button.
    Then user assigned "1" manager to the added match.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "user" UI and present at the home page.
    Then user verify the "non live" match on the user panel.

  @Scoreboard_LiveTossNotHappenedMatchVerificationOnUserPanel
  Scenario: Scenario_03: Verify the match on the user panel
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will "add" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time   |
      | 3          | current time |
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "matches module" button.
    Then user assigned "1" manager to the added match.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "user" UI and present at the home page.
    Then user verify the "toss not happened" match on the user panel.

  @Scoreboard_LiveTossHappenedMatchVerificationOnUserPanel
  Scenario: Scenario_04: Verify the match on the user panel
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will "add" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time   |
      | 4          | current time |
    Then user make "1" match as "live" and verify it.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "matches module" button.
    Then user assigned "1" manager to the added match.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "matches module" button.
    Then user assign agent to the match.
    Then User login parallel 2 agent UI and perform same action one by one on both agent UI with toss "true"
    Then User log in to "scoreboard" for "user" UI and present at the home page.
    Then user verify the "toss happened" match on the user panel.

  @Scoreboard_SCOR504
  Scenario: Scenario_05: Default screen not displayed when no event_id or invalid event_id is present
    Given User log in to "scoreboard" for "user" UI and present at the home page.
    Then user verify the user UI for "invalid" eventID.