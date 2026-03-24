@Scoreboard_Match
Feature: Scoreboard --> match page
  Here we will test the functionality of match page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_AddMatchVerification
  Scenario: Scenario_01: Add match and verify the added match.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
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
      | Match Date | Start Time |
      | 1          | 11:20      |

  @Scoreboard_HeadersOfMatchPageVerification
  Scenario: Scenario_02: verify the headers of match page.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user verify the following headers.
      | No. | Match Name | Event ID | Venue | Schedule | Match Date | Start Time | Assignee To Agent | Is Live | Actions |

  @Scoreboard_LiveNonLiveMatchVerification
  Scenario: Scenario_03: verify match live and non live status on match, dashboard and match history page.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will search the added match.
    Then user click on "dashboard module" button.
    Then user verify dashboard data for "Tomorrow" match.
    Then user click on "matches module" button.
    Then user make "1" match as "live" and verify it.
    Then user click on "dashboard module" button.
    Then user verify dashboard data for "Tomorrow" match.
    Then user click on "matches module" button.
    Then user make "1" match as "non live" and verify it.
    Then user click on "dashboard module" button.
    Then user verify dashboard data for "Tomorrow" match.
    Then user click on "Live matches module" button.
    Then user verify the "non live" match on live match module.
    Then user click on "matches module" button.
    Then user make "1" match as "live" and verify it.
    Then user click on "Live matches module" button.
    Then user verify the "live" match on live match module.

#  @Scoreboard_ManagerAssignedMatchVerification
#  Scenario: Scenario_04: verify assigned manager on match and match history page.
#    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
#    Then user click on "managers module" button.
#    Then user add "3" manager with password "P@ssword1!" and logout "false" current user.
#    Then user click on "matches module" button.
#    Then user assigned "3" manager to the added match.
#    Then user verify the validation message "Cannot Select More Than 2 Managers!".
#    Then verify added manager "true" with the count of "2" on "match" page.
#    Then user click on "live matches module" button.
#    Then verify added manager "true" with the count of "2" on "live matches" page.
#    Then edit "1" assigned manager on "Live Matches" page.
#    Then verify added manager "true" with the count of "1" on "live matches" page.
#    Then user click on "matches module" button.
#    Then verify added manager "true" with the count of "1" on "match" page.
#    Then edit "1" assigned manager on "Live Matches" page.
#    Then verify added manager "false" with the count of "2" on "live matches" page.

  @Scoreboard_EditMatchVerification
  Scenario: Scenario_05: edit match and verify the edited match.
    Given User already log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will search the added match.
    Then user will "edit" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time |
      | 0          | 12:20      |

  @Scoreboard_SCOR11
  Scenario: Scenario_06: verification matches text heading on matches page.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user verify the page "Matches".

  @Scoreboard_SCOR12
  Scenario: Scenario_07: verification live matches text heading on live matches page.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "live matches module" button.
    Then user verify the page "live matches".

  @Scoreboard_SCOR13
  Scenario: Scenario_08: only live match must be visible on the live match page
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user will "add" the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time |
      | 1          | 11:20      |
    Then user make "1" match as "live" and verify it.
    Then user click on "live matches module" button.
    Then user verify the "Live" match on live match module.

  @Scoreboard_SCOR14
  Scenario: Scenario_09: verify the match dates descending order
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user verify the dates order on "match" page.

  @Scoreboard_SCOR57_
  Scenario: Scenario_10: Add match and verify the added match.
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
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
      | Match Date | Start Time |
      | 1          | 11:20      |
    Then user verify the match with date filter.

  @Scoreboard_SCOR69
  Scenario: Scenario_11: Verification of manager assignment for the live match
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
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
      | Match Date | Start Time |
      | 1          | 11:20      |
    Then user make "1" match as "live" and verify it.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "matches module" button.
    Then user assigned "1" manager to the added match.
    Then verify success message "Match Assigned To Manager Successfully"

  @Scoreboard_SCOR72
  Scenario: Scenario_12: Manager must be able to see the match that are created by itself.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
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
      | Match Date | Start Time |
      | 1          | 11:20      |
    Then user will logout the current user.
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "match module" button.
    Then verify the created match by first manager for the second manager.

  @Scoreboard_SCOR162
  Scenario: Scenario_13: verify auto manager assign to the match
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
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
      | Match Date | Start Time |
      | 1          | 11:20      |
    Then user will logout the current user.
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user verify the assigned manager to the created match.

  @Scoreboard_SCOR171
  Scenario: Scenario_14: Active/Inactive Toggle Button Reverts to Old Status After Refresh
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "matches module" button.
    Then user verify the toggle button after refreshing the ui.

  @Scoreboard_SCOR177
  Scenario: Scenario_15: Match Name Field Should Allow Alphabets, Numbers & Special Characters
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
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
    Then user click on "venue module" button.
    Then user will "add" the "Venue" with count of "1" with following details and verify.
      | Country | State     | City   | Venue Image |
      | India   | Rajasthan | jaipur | venue.png   |
    Then user click on "tournaments module" button.
    Then user will "add" the "tournament" with count of "1" with following details and verify.
      | No. of Matches | Country        | Gender Type | Format Type | Tournament Type | Start Date | End Date |
      | 5              | India&Pakistan | Men         | T20         | Domestic        | 0          | 5        |
#    Then verify dashboard data for tournament for "2" teams.
    Then user click on "Tournaments module" button.
    Then user will search the added tournament.
    Then user click on "schedule" button.
    Then user click on "add schedule" button.
    Then user will "Add" a schedule and verify it.
    Then user will search the added tournament.
    Then user click on "select player" button.
    Then user add "11" players for team1 and "11" players for team2 and verify.
    Then user click on "matches module" button.
    Then user will add the "Match" with count of "1" with following details and verify.
      | Match Date | Start Time |
      | 1          | 11:20      |

  @Scoreboard_SCOR178
  Scenario: Scenario_16: Inactive or Expired Tournaments Should Not Be Displayed in "Add New Match" Page Dropdown
    Given User log in to "scoreboard" for "manager" UI and present at the home page.
    Then user click on "tournaments module" button.
    Then user update the tournament status as "false"
    Then user click on "matches module" button.
    Then user verify that tournament in create match popup.
