@Scoreboard_Agent
Feature: Scoreboard --> agent page
  Here we will test the functionality of agent page tab on admin UI.
  For this, we will login with admin credentials and will test
  the different scenarios including behaviour and function test of the web site list tab.
  For testing we will use the test data.

  @Scoreboard_HeadersOfAgentPageVerification
  Scenario: Scenario_01: verify the headers of agent page.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user verify the following headers.
      | No. | Name | Agent ID | Mobile No | Created By | Manager Name | Created At | Status | Actions |


  @Scoreboard_AgentCreationVerification
  Scenario: Scenario_02: create an agent and verify it via login.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "agent" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    Then user will logout the current user.

  @Scoreboard_InactiveAgentVerification
  Scenario: Scenario_03: agent inactivation verification.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "false" current user.
    Then user deactivate the created agent.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "agent" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "deactivation" message "Your Account Has Been Deactivated.".

  @Scoreboard_UpdateAgentVerification
  Scenario: Scenario_04: verification for agent after update the data.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "false" current user.
    Then user update the agent detail and verify.
    Then user update the agent password.
      | New Password  | Confirm Password |
      | NewP@ssword1! | NewP@ssword1!    |
    Then user will logout the current user.
    Then User log in to "scoreboard" for "agent" UI with username and password "NewP@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    Then user will logout the current user.


  @Scoreboard_SCOR10
  Scenario: Scenario_05: verification agent text heading on agent page.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user verify the page "Agents".

  @Scoreboard_SCOR29
  Scenario: Scenario_06: agent must be logged out when it got inactive.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "true" current user.
    Then User log in to "scoreboard" for "agent" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    When Admin logs in and disables agent
    Then Agent should be logged out automatically

  @Scoreboard_SCOR30
  Scenario: Scenario_07: verification on inactive agent on admin and manager UI.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "false" current user.
    Then update the agent status as "inactive".
    Then user will logout the current user.
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "success" message "Sign-In Success".
    Then user click on "agents module" button.
    Then verify the "inactive" status of agent.
    Then user will logout the current user.
    Then User log in to "scoreboard" for "agent" UI with username and password "P@ssword1!" and present at the home page.
    Then user verify the login "deactivation" message "Your Account Has Been Deactivated.".
    Then User log in to "scoreboard" for "manager" UI with username and password "P@ssword1!" and present at the home page.
    Then user click on "agents module" button.
    Then update the agent status as "active".
    Then user will logout the current user.
    Then User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then verify the "active" status of agent.
    Then user will logout the current user.

  @Scoreboard_SCOR85
  Scenario: Scenario_08: verification and validation for user login.
    Given User already present at the login page
    Then user will verify validation message for user login.
      | Username | Password  |
      | admin    | adminPass |
      | admin001 | abcde     |
      | admin001 |           |
      |          | adminPass |
      | admin001 | adminPass |

  @Scoreboard_SCOR112
  Scenario: Scenario_09: verify the eye icon on the agent creation popup.
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "agents module" button.
    Then user click on "password update" button.
    Then user verify the eye icon on "agent" creation popup.

  @Scoreboard_SCOR507
  Scenario: Scenario_09: Update Agent page does not close when updating with an already existing mobile number
    Given User log in to "scoreboard" for "admin" UI and present at the home page.
    Then user click on "managers module" button.
    Then user add "1" manager with password "P@ssword1!" and logout "false" current user.
    Then user click on "agents module" button.
    Then user add a agent with password "P@ssword1!" and logout "false" current user.
    Then user try to update agent with existing mobile number and verify the error message "Mobile Number Already Exists.".