@Scoreboard_Manager
Feature: Scoreboard --> manager page
  Here we will test the functionality of manager page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_HeadersOfManagerPageVerification
  Scenario: Scenario_01: verify the headers of manager page.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user verify the following headers.
      | No. | Name | Manager Id | Mobile No | Created At | Status | Actions |


  @Scoreboard_ManagerCreationVerification
  Scenario: Scenario_02: create a new manager and verify it.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    Then user will logout the current user.

  @Scoreboard_InactiveManagerVerification
  Scenario: Scenario_03: Manager inactivation verification.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user deactivate the created manager.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "deactivation" message "Your Account Has Been Deactivated.".

  @Scoreboard_UpdateManagerVerification
  Scenario: Scenario_04: verification for manager after update the data.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user update the manager detail and verify.
    Then user update the manager password.
      | New Password  | Confirm Password |
      | NewP@ssword1! | NewP@ssword1!    |
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "NewP@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    Then user will logout the current user.

  @Scoreboard_SCOR21
  Scenario: Scenario_05: verify manager date descending order.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user verify the dates order on "manager" page.

  @Scoreboard_SCOR23
  Scenario: Scenario_06: mobile number validation message verification.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user verify the validation messages for manager mobile number

  @Scoreboard_SCOR24
  Scenario: Scenario_07: paginator verification on manager page.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then verify the paginator on the "manager" page.


  @Scoreboard_SCOR112
  Scenario: Scenario_08: verify the eye icon on the agent creation popup.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user click on "password update" button.
    Then user verify the eye icon on "manager" creation popup.